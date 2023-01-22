package services

import play.api.libs.ws.{WSAuthScheme, WSClient}

import scala.concurrent.{ExecutionContext, Future}

class CompaniesHouseService(wsClient: WSClient, apiKey: String)(implicit ec: ExecutionContext) {

  def getAppointments(officerID: String): Future[String] = {
    val url = "https://api.company-information.service.gov.uk/officers/" + officerID + "/appointments"
    fetch(url = url)
  }
  private def fetch(url: String): Future[String] = {
    wsClient
      .url(url)
      .withAuth(apiKey, "" , WSAuthScheme.BASIC)
      .get
      .map(response => response.body)
  }
}
