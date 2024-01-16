package models

import io.circe.Decoder

case class AppointedTo(company_name: String, company_number: String, company_status: String)
case class Appointment(officer_role: String, appointed_to: AppointedTo)
case class OfficerAppointmentResponse(items: List[Appointment])

object OfficerAppointmentResponse {
  import io.circe.generic.auto._
  implicit val decoder = Decoder[OfficerAppointmentResponse]
}


case class Officer(name: String, officer_role: String)
case class CompanyOfficersResponse(items: List[Officer])

object CompanyOfficersResponse {
  import io.circe.generic.auto._
  implicit val decoder = Decoder[CompanyOfficersResponse]
}
