package dos_drs_perf

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.HeaderValues
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class MarthaHcaSimulation extends Simulation {

  val httpProtocolBuilder: HttpProtocolBuilder = http
    .baseUrl(SimulationConfiguration.marthaBaseUrl)
    .contentTypeHeader(HeaderValues.ApplicationJson)
    .authorizationHeader(s"Bearer ${Authorization.newServiceAccessToken()}")

  val scenarioBuilder: ScenarioBuilder =
    scenario("Martha HCA scenario")
      .feed(SimulationConfiguration.randFeederBuilder)
      .exec(
        http("Martha HCA request")
          .post("/martha_v3")
          .queryParam("random", _("rand").validate[String])
          .body(
            StringBody(
              // HCA doesn't like random query keys but send a random anchor to the server and it's ok!
              s"""{"url": "dos://drs.staging.data.humancellatlas.org/4cf48dbf-cf09-452e-bb5b-fd016af0c747#random=$${rand}"}"""
            )
          )
      )

  setUp(
    SimulationConfiguration.buildSimulation(
      scenarioBuilder,
      httpProtocolBuilder,
    )
  )
}
