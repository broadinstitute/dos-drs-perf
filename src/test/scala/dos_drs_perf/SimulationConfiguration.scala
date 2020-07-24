package dos_drs_perf

import java.io.InputStream
import java.nio.file.{Files, Paths}

import com.typesafe.config.{Config, ConfigFactory, ConfigRenderOptions}
import com.typesafe.scalalogging.StrictLogging
import io.gatling.core.Predef._
import io.gatling.core.feeder.FeederBuilder
import io.gatling.core.structure.{PopulationBuilder, ScenarioBuilder}
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration._
import scala.util.Random

object SimulationConfiguration extends StrictLogging {
  lazy val rootConfig: Config = ConfigFactory.load()

  lazy val dosDrsPerfConfig: Config = {
    val conf = rootConfig.getConfig("dos-drs-perf")
    logger.whenDebugEnabled {
      val confString = conf
        .root()
        .render(
          ConfigRenderOptions
            .defaults()
            .setOriginComments(false)
            .setComments(false)
            .setJson(false)
        )
      confString
        .split("""\r?\n""")
        .foreach(line => logger.debug(s"Config: dos-drs-perf.$line"))
    }
    conf
  }

  lazy val marthaBaseUrl: String = dosDrsPerfConfig.getString("martha-base-url")

  lazy val fenceValidationUrl: String =
    dosDrsPerfConfig.getString("fence-validation-url")

  lazy val fenceLinkUrl: String = dosDrsPerfConfig.getString("fence-link-url")

  lazy val serviceAccountUser: String =
    dosDrsPerfConfig.getString("service-account-user")

  lazy val rampUpUsers: Double = dosDrsPerfConfig.getDouble("ramp-up-users")

  lazy val rampUpDuration: FiniteDuration =
    Duration.fromNanos(dosDrsPerfConfig.getDuration("ramp-up-duration").toNanos)

  lazy val userAgent: String = dosDrsPerfConfig.getString("ramp-up-users")

  lazy val randFeederBuilder: FeederBuilder =
    Iterator.continually(Map("rand" -> Random.alphanumeric.take(20).mkString))

  def buildSimulation(
      scenarioBuilder: ScenarioBuilder,
      httpProtocolBuilder: HttpProtocolBuilder,
    ): PopulationBuilder =
    scenarioBuilder
      .inject(
        rampUsersPerSec(0)
          .to(rampUpUsers)
          .during(rampUpDuration)
      )
      .protocols(
        httpProtocolBuilder.userAgentHeader(userAgent)
      )

  def newServiceAccountJsonStream(): InputStream =
    if (dosDrsPerfConfig.hasPath("service-account-json"))
      Files.newInputStream(
        Paths.get(dosDrsPerfConfig.getString("service-account-json"))
      )
    else {
      val stream = Thread
        .currentThread()
        .getContextClassLoader
        .getResourceAsStream("firecloud_automation.json")
      Option(stream).getOrElse(
        sys.error(
          "firecloud_automation.json not found. See the README.md for more info."
        )
      )
    }
}
