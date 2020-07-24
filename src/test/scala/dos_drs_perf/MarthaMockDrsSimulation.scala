package dos_drs_perf

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.HeaderValues
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class MarthaMockDrsSimulation extends Simulation {

  val httpProtocolBuilder: HttpProtocolBuilder = http
    .baseUrl(SimulationConfiguration.marthaBaseUrl)
    .contentTypeHeader(HeaderValues.ApplicationJson)
    .authorizationHeader(
      s"Bearer ${Authorization.newApplicationDefaultToken(linkBond = true)}"
    )

  val scenarioBuilder: ScenarioBuilder =
    scenario("Martha MockDRS scenario")
      .feed(SimulationConfiguration.randFeederBuilder)
      .exec(
        http("Martha MockDRS request")
          .post("/martha_v3")
          .queryParam("random", _("rand").validate[String])
          .body(
            StringBody(
              s"""{"url": "dos://wb-mock-drs-dev.storage.googleapis.com/preview_dos.json?random=$${rand}"}"""
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
