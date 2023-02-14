package services

import scala.concurrent.{Future, ExecutionContext}
import play.api.libs.ws._
class CompaniesHouseService(wsClient: WSClient)(implicit ec: ExecutionContext) {

  private def fetch(url: String): Future[String] = {
    wsClient
      .url(url)
      .withAuth("API KEY GOES HERE","", WSAuthScheme.BASIC)
      .get
      .map(response => response.body)
  }

  def getOfficerAppointments(officerId: String): Future[String] = {
    val retStr = "officerId" + ":" + officerId
    Future.successful(retStr)
  }
}
