package dos_drs_perf

import java.net.{HttpURLConnection, URL}

import com.google.auth.oauth2.{GoogleCredentials, ServiceAccountCredentials}
import io.gatling.http.HeaderNames

object Authorization {

  private lazy val serviceAccountCredentials: GoogleCredentials =
    ServiceAccountCredentials
      .fromStream(SimulationConfiguration.newServiceAccountJsonStream())
      .createDelegated(SimulationConfiguration.serviceAccountUser)

  def newServiceAccessToken(): String =
    newToken(serviceAccountCredentials)

  def newApplicationDefaultToken(linkBond: Boolean): String = {
    val token = newToken(GoogleCredentials.getApplicationDefault)
    if (linkBond)
      if (!existingBondToken(token))
        linkBondToken(token)
    token
  }

  private def newToken(credentials: GoogleCredentials): String = {
    val scoped = credentials.createScoped("email", "openid")
    scoped.refresh()
    scoped.getAccessToken.getTokenValue
  }

  private def existingBondToken(token: String): Boolean = {
    val url = new URL(SimulationConfiguration.fenceValidationUrl)
    val connection = url.openConnection.asInstanceOf[HttpURLConnection]
    try {
      connection.setRequestMethod("GET")
      connection.setRequestProperty(HeaderNames.Authorization, s"Bearer $token")
      connection.setInstanceFollowRedirects(true)
      connection.connect()
      if (connection.getResponseCode == 200)
        true
      else if (connection.getResponseCode == 404)
        false
      else
        sys.error(
          s"Got HTTP ${connection.getResponseCode} ${connection.getResponseMessage} when verifying the link to Bond"
        )
    }
    finally Option(connection).foreach(_.disconnect())
  }

  private def linkBondToken(token: String): Unit = {
    val url = new URL(SimulationConfiguration.fenceLinkUrl)
    val connection = url.openConnection.asInstanceOf[HttpURLConnection]
    try {
      connection.setRequestMethod("POST")
      connection.setRequestProperty(HeaderNames.Authorization, s"Bearer $token")
      connection.setInstanceFollowRedirects(true)
      connection.setFixedLengthStreamingMode(0)
      connection.setDoOutput(true)
      connection.connect()
      if (connection.getResponseCode % 100 != 2)
        sys.error(
          s"Got HTTP ${connection.getResponseCode} ${connection.getResponseMessage} when linking to Bond. " +
            s"See README.md for more info."
        )
    }
    finally Option(connection).foreach(_.disconnect())
  }

}
