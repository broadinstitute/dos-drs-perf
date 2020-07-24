package dos_drs_perf

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class MockDrsSimulation extends Simulation {

  val httpProtocolBuilder: HttpProtocolBuilder = http
    .baseUrl("https://wb-mock-drs-dev.storage.googleapis.com")

  val scenarioBuilder: ScenarioBuilder =
    scenario("MockDRS scenario")
      .feed(SimulationConfiguration.randFeederBuilder)
      .exec(
        http("MockDRS request")
          .get(
            s"/ga4gh/dos/v1/dataobjects/preview_dos.json"
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
