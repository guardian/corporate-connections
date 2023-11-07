package services

import play.api.libs.ws.{WSAuthScheme, WSClient}

import scala.concurrent.{ExecutionContext, Future}

class CompaniesHouseService(wsClient: WSClient, apiKey: String)(implicit ec: ExecutionContext) {

  private def fetch(url: String): Future[String] = {
    wsClient
      .url(url)
      .withAuth(apiKey,"", WSAuthScheme.BASIC)
      .get
      .map(response => response.body)
  }

  def getCompanies(officerId: String): Future[String] = {
    fetch(s"https://api.company-information.service.gov.uk/officers/$officerId/appointments")
  }
}
