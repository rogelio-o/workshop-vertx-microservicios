package com.rogelioorts.workshop.vertx.microservices.scafolder.exceptions;

import com.rogelioorts.workshop.vertx.microservices.scafolder.utils.Responses;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;

public class JsonExceptionHandler implements Handler<RoutingContext> {

  private final Logger log = LoggerFactory.getLogger(JsonExceptionHandler.class);

  @Override
  public void handle(final RoutingContext context) {
    final Throwable exception = context.failure();
    final JsonObject response = new JsonObject().put("error", true);
    final int statusCode = exception == null ? getStatusCodeFromContext(context) : HttpResponseStatus.INTERNAL_SERVER_ERROR.code();

    if (exception != null) {
      log.error("Error in controller action.", exception);
      response.put("message", exception.getMessage());
    }

    Responses.sendJson(context, response, statusCode);
  }

  private int getStatusCodeFromContext(final RoutingContext context) {
    final int contextStatusCode = context.statusCode();

    return contextStatusCode == HttpResponseStatus.OK.code() ? HttpResponseStatus.NOT_FOUND.code() : contextStatusCode;
  }

}