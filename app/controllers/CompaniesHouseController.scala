package controllers

import models.OfficerAppointmentResponse
import play.api.mvc._
import services.CompaniesHouseService
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

import scala.concurrent.{ExecutionContext, Future}

class CompaniesHouseController(components: ControllerComponents, companiesHouseService: CompaniesHouseService)(implicit ec: ExecutionContext) extends AbstractController(components) {
  def getAppointmentsProxy(officerId: String) = Action.async {
    companiesHouseService.getAppointments(officerId).map(json => Ok(json))
  }
  def getAppointments(officerId: String) = Action.async {
//    make this call a different function inside CompaniesHouseService
    companiesHouseService.getCompanyNumbers(officerId).map(json => Ok(json))
  }

}
