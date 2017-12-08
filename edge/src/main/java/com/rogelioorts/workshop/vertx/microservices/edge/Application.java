package com.rogelioorts.workshop.vertx.microservices.edge;

import com.rogelioorts.workshop.vertx.microservices.edge.routing.ProxyHandler;
import com.rogelioorts.workshop.vertx.microservices.scafolder.BaseApplication;
import com.rogelioorts.workshop.vertx.microservices.scafolder.exceptions.JsonExceptionHandler;
import com.rogelioorts.workshop.vertx.microservices.scafolder.exceptions.ResourceNotFoundHandler;

import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class Application extends BaseApplication {

  private static final String PATH_ID_SUFFIX = "/:id";

  private static final String SERIES_SERVICE = "series.data";
  private static final String SERIES_PATH = "/api/v1/series";

  private static final String COMMENTS_SERVICE = "series.comments";
  private static final String COMMENTS_PATH = "/api/v1/series/:idSerie/comments";

  private static final String EPISODES_SERVICE = "series.episodes";
  private static final String EPISODES_PATH = "/api/v1/series/:idSerie/episodes";

  private static final String RATING_SERVICE = "series.rating";
  private static final String RATING_PATH = "/api/v1/series/:idSerie/rating";

  public static final String SERVICE_NAME = "edge";

  @Override
  protected String getServiceName() {
    return SERVICE_NAME;
  }

  @Override
  protected Router getRouter() {
    final Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());

    // SERIES DATA
    addRoute(router, SERIES_SERVICE, HttpMethod.GET, SERIES_PATH + PATH_ID_SUFFIX);
    addRoute(router, SERIES_SERVICE, HttpMethod.GET, SERIES_PATH);
    addRoute(router, SERIES_SERVICE, HttpMethod.POST, SERIES_PATH);
    addRoute(router, SERIES_SERVICE, HttpMethod.PUT, SERIES_PATH + PATH_ID_SUFFIX);
    addRoute(router, SERIES_SERVICE, HttpMethod.DELETE, SERIES_PATH + PATH_ID_SUFFIX);

    // SERIES COMMENTS
    addRoute(router, COMMENTS_SERVICE, HttpMethod.GET, COMMENTS_PATH);
    addRoute(router, COMMENTS_SERVICE, HttpMethod.POST, COMMENTS_PATH);
    addRoute(router, COMMENTS_SERVICE, HttpMethod.PUT, COMMENTS_PATH + PATH_ID_SUFFIX);
    addRoute(router, COMMENTS_SERVICE, HttpMethod.DELETE, COMMENTS_PATH + PATH_ID_SUFFIX);

    // SERIES EPISODES
    addRoute(router, EPISODES_SERVICE, HttpMethod.GET, EPISODES_PATH + PATH_ID_SUFFIX);
    addRoute(router, EPISODES_SERVICE, HttpMethod.GET, EPISODES_PATH);
    addRoute(router, EPISODES_SERVICE, HttpMethod.POST, EPISODES_PATH);
    addRoute(router, EPISODES_SERVICE, HttpMethod.PUT, EPISODES_PATH + PATH_ID_SUFFIX);
    addRoute(router, EPISODES_SERVICE, HttpMethod.DELETE, EPISODES_PATH + PATH_ID_SUFFIX);

    // SERIES RATING
    addRoute(router, RATING_SERVICE, HttpMethod.POST, RATING_PATH);

    router.route("/api/*").handler(new ResourceNotFoundHandler()).failureHandler(new JsonExceptionHandler());

    return router;
  }

  private void addRoute(final Router router, final String service, final HttpMethod method, final String path) {
    router.route(method, path).handler(new ProxyHandler(service, method, path));
  }

}
