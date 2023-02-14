package services

import java.util.concurrent.Future

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
    return Future.successful(retStr)
  }
}
