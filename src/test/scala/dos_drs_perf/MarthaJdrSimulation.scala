package dos_drs_perf

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.HeaderValues
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class MarthaJdrSimulation extends Simulation {

  val httpProtocolBuilder: HttpProtocolBuilder = http
    .baseUrl(SimulationConfiguration.marthaBaseUrl)
    .contentTypeHeader(HeaderValues.ApplicationJson)
    .authorizationHeader(s"Bearer ${Authorization.newServiceAccessToken()}")

  val scenarioBuilder: ScenarioBuilder =
    scenario("Martha JDR scenario")
      .feed(SimulationConfiguration.randFeederBuilder)
      .exec(
        http("Martha JDR request")
          .post(
            "/martha_v3"
          )
          .queryParam("random", _("rand").validate[String])
          .body(
            StringBody(
              s"""{"url": "drs://jade.datarepo-dev.broadinstitute.org/v1_93dc1e76-8f1c-4949-8f9b-07a087f3ce7b_8b07563a-542f-4b5c-9e00-e8fe6b1861de?random=$${rand}"}"""
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
