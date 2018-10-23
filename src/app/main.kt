package tech.cadia

import io.vertx.core.Vertx
import io.vertx.kotlin.core.http.HttpServerOptions
import mu.KotlinLogging


val logger = KotlinLogging.logger {}

const val PORT: Int = 8888

fun main(args: Array<String>) {
    val vertx = Vertx.vertx()
    val options = HttpServerOptions(logActivity = true)
    val server = vertx.createHttpServer(options)
    val mainRouter = routerFactory(vertx)

    server.requestHandler { request ->
        val path = request.uri()
        val method = request.rawMethod()
        logger.info { "HTTP $method $path" }
        mainRouter.accept(request)
    }

    server.listen(PORT) {
        logger.info { "Start server on port $PORT" }
    }
}