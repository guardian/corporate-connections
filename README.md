# Corporate Connections

This is a skeleton of a Play Framework app intended for use as a Scala learning project.

The task is to build an API to find all the co-officers of a given company officer (i.e. people that have been officers of any of the companies that our person of interest has been an officer for).

This gives Scala learners an opportunity to build something with a real-world application in the investigative journalism space.


## Companies House API
### Getting an API key
- Go to [https://developer.company-information.service.gov.uk/manage-applications] and create an account with your work email (if you don’t already have one)
- Create an application (environment: Live, because the test environment does not have real data)
- Create an API key in that application

### API docs
[Companies House Public Data API reference](https://developer-specs.company-information.service.gov.uk/companies-house-public-data-api/reference)

### Auth
[Docs here](https://developer-specs.company-information.service.gov.uk/guides/authorisation)

We'll use HTTP Basic Auth, which for Companies House involves sending the API key as the username, and an empty password.

### Looking up companies by officer
e.g. for officerId `aEdQfmEjiBuB7tLwOP_Wfg-JA-8`, this curl request returns their companies:

`curl -XGET -u <api-key>: https://api.company-information.service.gov.uk/officers/aEdQfmEjiBuB7tLwOP_Wfg-JA-8/appointments`

### Looking up officers by company
And for companyNumber `03114488`, this curl request returns its officers:

`curl -XGET -u <api-key>: https://api.company-information.service.gov.uk/company/03114488/officers`

For more details, see the [Companies House API Docs](https://developer-specs.company-information.service.gov.uk/companies-house-public-data-api/reference).

## Running the webserver
Use the API_KEY environment variable to set the Companies House api key:

```
webserver-project$ API_KEY=my-api-key sbt

...

[webserver-project] $ run

--- (Running the application, auto-reloading is enabled) ---

[info] p.c.s.AkkaHttpServer - Listening for HTTP on /0:0:0:0:0:0:0:0:9000

(Server started, use Enter to stop and go back to the console...)
```

## Tasks
### Task 1: proxy appointments data for an officer
Implement the `/officer/:id/appointmentsProxy` endpoint.

Pass through the unaltered Companies House API appointments response for an officerId.

E.g. for officerId `aEdQfmEjiBuB7tLwOP_Wfg-JA-8`: `https://api.company-information.service.gov.uk/officers/aEdQfmEjiBuB7tLwOP_Wfg-JA-8/appointments`

Build on the `CompaniesHouseService` class in the `services` package for talking to the Companies House API.

### Task 2: return companyNumbers for an officer
Implement the `/officer/:officerId/appointments` endpoint.

Use Circe to decode the Companies House response into Scala case classes. Use the `OfficerAppointmentResponse` model in `models/CompaniesHouseModels.scala`.

Then return just the list of companyNumbers, as a JSON array.

### Task 3: return officer names for a given companyNumber
Implement the `/company/:companyNumber/officers` endpoint.

E.g. for companyNumber 03114488: `https://api.company-information.service.gov.uk/company/03114488/officers`

Use Circe to decode the Companies House response into Scala case classes. Use the `CompanyOfficersResponse` model in `models/CompaniesHouseModels.scala`.

### Task 4: 
Implement the `/officer/:officerId/connections` endpoint.

It should take an officerId and return a JSON array of connected officer names.

To do this, the backend must find their companies, then find all other officers of those companies.
