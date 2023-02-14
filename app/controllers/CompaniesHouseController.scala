package controllers

import play.api.mvc._
import services.CompaniesHouseService
import scala.concurrent.ExecutionContext


class CompaniesHouseController(components: ControllerComponents, companiesHouseService: CompaniesHouseService)(implicit ec: ExecutionContext) extends AbstractController(components) {
  def getAppointmentsProxy(officerId: String) = Action.async {
      companiesHouseService.getOfficerAppointments(officerId).map(str => Ok(str))
  }
}
