package controllers

import play.api.mvc._
import services.{ApiError, CompaniesHouseService, FetchError, JsonError}
import io.circe.syntax._

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
}
