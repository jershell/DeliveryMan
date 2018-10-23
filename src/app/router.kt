package tech.cadia

import io.vertx.core.Vertx
import io.vertx.core.http.HttpServerResponse
import io.vertx.ext.web.Router
import kotlinx.serialization.json.JSON

fun fail(response: HttpServerResponse) {
    response.putHeader("content-type", "text/plain")
    response.statusCode = 400
    response.end(JSON.stringify(Error("bad request")))
}

fun routerFactory(vertx: Vertx): Router {
    val router = Router.router(vertx)
    val mailer = Mailer(vertx)

    router.get("/").handler { request ->
        // This handler gets called for each request that arrives on the server
        val response = request.response()
        response.putHeader("content-type", "application/json")
        // Write to the response and end it
        response.end(JSON.stringify(Message("deliveryman is running")))
    }

    router.get("/json").handler { request ->
        // This handler gets called for each request that arrives on the server
        val response = request.response()
        response.putHeader("content-type", "application/json")
        // Write to the response and end it
        response.end(JSON.stringify(Result(true)))
    }

    router.post("/form").handler { req ->
        val res = req.response()
        res.putHeader("content-type", "application/json")

        req.request().bodyHandler {
            val formFields = try {
                JSON.parse<FormFields>(it.toString())
            } catch (e : Exception) {
                null
            }

            if(formFields != null) {
                logger.debug { formFields.toString() }
                res.end(JSON.stringify(Result(true)))
                mailer.send(formFields.toMap())
            } else {
                fail(res)
            }
        }
    }

    return router
}
