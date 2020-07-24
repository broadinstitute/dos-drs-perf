package dos_drs_perf

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class JdrSimulation extends Simulation {

  val httpProtocolBuilder: HttpProtocolBuilder = http
    .baseUrl("https://jade.datarepo-dev.broadinstitute.org")
    .authorizationHeader(s"Bearer ${Authorization.newServiceAccessToken()}")

  val scenarioBuilder: ScenarioBuilder =
    scenario("JDR scenario")
      .feed(SimulationConfiguration.randFeederBuilder)
      .exec(
        http("JDR request")
          .get(
            s"/ga4gh/drs/v1/objects/v1_93dc1e76-8f1c-4949-8f9b-07a087f3ce7b_8b07563a-542f-4b5c-9e00-e8fe6b1861de"
          )
          .queryParam("random", _("rand").validate[String])
      )

  setUp(
    SimulationConfiguration.buildSimulation(
      scenarioBuilder,
      httpProtocolBuilder,
    )
  )
}
