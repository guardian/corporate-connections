package services

import models.{Appointment, CompanyOfficersResponse, Officer, OfficerAppointmentResponse}
import models.OfficerAppointmentResponse._
import play.api.libs.ws.{WSAuthScheme, WSClient}
import io.circe.parser.decode

import scala.concurrent.{ExecutionContext, Future}

sealed trait CompaniesHouseError
case class FetchError(message: String) extends CompaniesHouseError
case class ApiError(message: String) extends CompaniesHouseError
case class JsonError(error: io.circe.Error) extends CompaniesHouseError

class CompaniesHouseService(wsClient: WSClient, apiKey: String)(implicit ec: ExecutionContext) {

  private def fetch(url: String): Future[Either[CompaniesHouseError, String]] = {
    wsClient
      .url(url)
      .withAuth(apiKey,"", WSAuthScheme.BASIC)
      .get
      .map(response => {
        response.status match {
          case 200 => Right(response.body)
          case _ => Left(ApiError(s"Error fetching $url: ${response.status} ${response.statusText}"))
        }
      })
      .recover(error => Left(FetchError(s"Error fetching $url: ${error.getMessage}")))
  }

  def getCompaniesRaw(officerId: String): Future[Either[CompaniesHouseError,String]] = {
    fetch(s"https://api.company-information.service.gov.uk/officers/$officerId/appointments")
  }

  def getAppointments(officerId: String): Future[Either[CompaniesHouseError, List[Appointment]]] =
    getCompaniesRaw(officerId).map { result =>
      result.flatMap { json =>
        decode[OfficerAppointmentResponse](json) match {
          case Right(response) => Right(response.items)
          case Left(error) => Left(JsonError(error))
        }
      }
    }

  def getOfficers(companyNumber: String): Future[Either[CompaniesHouseError, List[Officer]]] =
    fetch(s"https://api.company-information.service.gov.uk/company/$companyNumber/officers")
      .map { result =>
        result.flatMap { json =>
          decode[CompanyOfficersResponse](json) match {
            case Right(response) => Right(response.items)
            case Left(error) => Left(JsonError(error))
          }
        }
      }
}
