package dos_drs_perf

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class HcaSimulation extends Simulation {

  val httpProtocolBuilder: HttpProtocolBuilder =
    http.baseUrl("https://drs.staging.data.humancellatlas.org")

  val scenarioBuilder: ScenarioBuilder =
    scenario("HCA scenario")
      .feed(SimulationConfiguration.randFeederBuilder)
      .exec(
        http("HCA request")
          .get(
            // HCA doesn't like random query keys but send a random anchor to the server and it's ok!
            s"/ga4gh/dos/v1/dataobjects/4cf48dbf-cf09-452e-bb5b-fd016af0c747#random=$${rand}"
          )
      )

  setUp(
    SimulationConfiguration.buildSimulation(
      scenarioBuilder,
      httpProtocolBuilder,
    )
  )
}
