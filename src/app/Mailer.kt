package tech.cadia

import io.vertx.core.Vertx
import io.vertx.ext.mail.MailClient
import io.vertx.ext.mail.MailMessage
import io.vertx.kotlin.ext.mail.MailConfig
import io.vertx.ext.mail.StartTLSOptions
import kotlinx.html.*
import kotlinx.html.stream.appendHTML


class Mailer(vertx: Vertx) {
    private val config = MailConfig()
    private var mailClient: MailClient? = null
    private var mailList = listOf<String>()

    init {
        mailList = System.getenv("MAIL_LIST")!!.split(",").map { it.trim() }
        val hostname: String = System.getenv("SMTP_HOSTNAME")!!
        val port = System.getenv("SMTP_PORT") ?: "587"
        val tlsOptions = when (System.getenv("SMTP_TLS")) {
            "DISABLED" -> StartTLSOptions.DISABLED
            "OPTIONAL" -> StartTLSOptions.OPTIONAL
            "REQUIRED" -> StartTLSOptions.REQUIRED
            else -> StartTLSOptions.OPTIONAL
        }
        val username = System.getenv("SMTP_USERNAME")!!
        val password = System.getenv("SMTP_PASSWORD")!!
        logger.info { "Init Mailer: [$hostname:$port] [tls - $tlsOptions] $mailList" }
        config.hostname = hostname
        config.port = port.toInt()
        config.starttls = tlsOptions
        config.username = username
        config.password = password
        mailClient = MailClient.createNonShared(vertx, config)
    }

    private fun renderMessage(title: String, data: Map<String, String>, siteName: String = ""): String {
        return buildString {
            appendHTML(false)
            .html {
                style = "width: 100%; height: 100%;"
                head {
                    title(title)
                }
                body {
                    style = """width: 100%;
                        |height: 100%;
                        |padding-left: auto;
                        |padding-right: auto;
                        |font-family: sans-serif; font-size: 18px;
                        |background-color: #8693AB;""".trimMargin()
                    table {
                        style = """margin: 0 auto;
                            |margin-bottom: 16px; width: 80%; border: 1px solid #AFD0BF;
                            |border-radius: 3px; background-color: #AFD0BF; color: #212227;
                            |font-weight: 500;""".trimMargin()
                        id = "main"
                        caption {
                            style = "color: #AFD0BF;"
                            h4 { +title }
                            h5 { +siteName }
                        }

                        data.map {
                            tr {
                                td {
                                    style = "padding: 12px;"
                                    +it.key
                                }
                                td {
                                    style = "padding: 12px;"
                                    +it.value
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun createMessage(data: Map<String, String>): MailMessage {
        val message = MailMessage()
        val msgTitle = System.getenv("MSG_TITLE") ?: "Message from site"
        val msgSiteName = System.getenv("MSG_SITENAME") ?: ""
        message.from = "mailbot@cadiatech.com (Mailbot)"
        message.subject = "$msgTitle ($msgSiteName)"
        message.to = mailList
        message.html = renderMessage(msgTitle, data, msgSiteName)
        return message
    }


    fun send(data: Map<String, String>) {
        val msg = createMessage(data)
        logger.info { "Send to ${msg.to}" }
        mailClient!!.sendMail(msg) {
            if (it.succeeded()) {
                logger.info { "Send to ${it.result()}" }
            } else {
                logger.error { "Send to ${it.cause().printStackTrace()}" }
            }
        }
    }
}