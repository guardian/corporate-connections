package controllers

import play.api.mvc._
import services.{ApiError, CompaniesHouseError, CompaniesHouseService, FetchError, JsonError}
import io.circe.syntax._
import models.{Appointment, Officer}

import scala.concurrent.{ExecutionContext, Future}

class CompaniesHouseController(components: ControllerComponents, companiesHouseService: CompaniesHouseService)(implicit ec: ExecutionContext) extends AbstractController(components) {
  def getAppointmentsProxy(officerId: String) = Action.async {
    companiesHouseService.getCompaniesRaw(officerId).map {
      case Right(json) => Ok(json)
      case Left(error) => InternalServerError("Error!")
    }
  }

  def getAppointments(officerId: String) = Action.async {
    companiesHouseService.getAppointments(officerId).map {
      case Right(appointments) => Ok(appointments.map(_.appointed_to.company_number).asJson.spaces2)
      case Left(JsonError(error)) => InternalServerError("Error processing response from Companies House API")
      case Left(FetchError(message)) => InternalServerError("Error making request to Companies House API")
      case Left(ApiError(message)) => InternalServerError("Error making request to Companies House API")
    }
  }

  def getOfficers(companyNumber: String) = Action.async {
    companiesHouseService.getOfficers(companyNumber).map {
      case Right(officers) => Ok(officers.map(_.name).asJson.spaces2)
      case Left(JsonError(error)) => InternalServerError("Error processing response from Companies House API")
      case Left(FetchError(message)) => InternalServerError("Error making request to Companies House API")
      case Left(ApiError(message)) => InternalServerError("Error making request to Companies House API")
    }
  }

  private def getOfficerNamesForAppointments(appointments: List[Appointment]): Future[List[String]] =
    Future
      .sequence(  // convert List[Future[...]] to Future[List[...]]
        appointments.map(app =>
          companiesHouseService.getOfficers(app.appointed_to.company_number)
        )
      )
      // filter out any errors and just get the names
      .map((results: List[Either[CompaniesHouseError, List[Officer]]]) =>
        results.collect {
          case Right(officers) => officers.map(_.name)
        }
      )
      .map(_.flatten) // flatten the list of lists (because each company has its own list of officers)
      .map(_.toSet.toList)   // convert to a set to remove duplicates


  def getConnections(officerId: String) = Action.async {
    companiesHouseService.getAppointments(officerId).flatMap {
      case Right(appointments) =>
        getOfficerNamesForAppointments(appointments)
          .map(officers => Ok(officers.asJson.spaces2))

      case Left(error) => Future.successful(InternalServerError("Error processing response from Companies House API"))
    }
  }
}
