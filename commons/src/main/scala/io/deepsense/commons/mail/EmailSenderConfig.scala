package io.deepsense.commons.mail

import java.util.Properties
import javax.mail.{Authenticator, PasswordAuthentication, Session}

import com.typesafe.config.{Config, ConfigFactory}

case class EmailSenderAuthorizationConfig(
    user: String,
    password: String)

case class EmailSenderConfig(
    smtpHost: String,
    smtpPort: Int,
    from: String,
    authorizationConfig: Option[EmailSenderAuthorizationConfig]) {

  val sessionProperties: Properties = {
    val res = new Properties()
    res.put("mail.smtp.host", smtpHost)
    res.put("mail.smtp.port", smtpPort.toString)
    res.put("mail.from", from)
    res
  }

  private def mailAuthenticator: Option[Authenticator] = authorizationConfig.map { auth =>
    new Authenticator {
      override def getPasswordAuthentication: PasswordAuthentication = {
        new PasswordAuthentication(auth.user, auth.password)
      }
    }
  }

  def session: Session = mailAuthenticator.map { authenticator =>
    Session.getInstance(sessionProperties, authenticator)
  }.getOrElse(Session.getInstance(sessionProperties))
}

object EmailSenderConfig {
  def apply(config: Config): EmailSenderConfig = {
    val smtpHost = config.getString(smtpHostKey)
    val smtpPort = config.getInt(smtpPortKey)
    val from = config.getString(fromKey)
    val auth = EmailSenderAuthorizationConfig(config)
    EmailSenderConfig(
      smtpHost = smtpHost,
      smtpPort = smtpPort,
      from = from,
      authorizationConfig = auth)
  }
  def apply(): EmailSenderConfig = EmailSenderConfig(ConfigFactory.load().getConfig(emailSenderKey))

  val emailSenderKey = "email-sender"

  val smtpHostKey = "smtp.host"
  val smtpPortKey = "smtp.port"
  val fromKey = "from"
}

object EmailSenderAuthorizationConfig {
  def apply(config: Config): Option[EmailSenderAuthorizationConfig] = {
    if (config.hasPath(user) && config.hasPath(password)) {
      Some(EmailSenderAuthorizationConfig(
        user = config.getString(user),
        password = config.getString(password)
      ))
    } else {
      None
    }
  }

  val user = "user"
  val password = "pass"
}
