package services

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import play.api.libs.ws.{WSAuthScheme, WSClient}

import scala.concurrent.{ExecutionContext, Future}

import models.OfficerAppointmentResponse


class CompaniesHouseService(wsClient: WSClient, apiKey: String)(implicit ec: ExecutionContext) {
  def getAppointments(officerID: String): Future[String] = {
    val url = "https://api.company-information.service.gov.uk/officers/" + officerID + "/appointments"
    fetch(url = url)
  }

  def getCompanyNumbers(officerID: String): Future[String] = {
    val url = "https://api.company-information.service.gov.uk/officers/" + officerID + "/appointments"
    val decoded = decode[OfficerAppointmentResponse](fetch(url = url))
    decoded.map(resp => resp).toString
  }
  private def fetch(url: String): Future[String] = {
    wsClient
      .url(url)
      .withAuth(apiKey, "" , WSAuthScheme.BASIC)
      .get
      .map(response => response.body)
  }
}
