package com.rogelioorts.rotten.potatoes.series.rating;

import com.rogelioorts.rotten.potatoes.series.rating.repositories.RatingsRepository;
import com.rogelioorts.rotten.potatoes.series.rating.routing.rating.CreateRatingHandler;
import com.rogelioorts.rotten.potatoes.shared.BaseApplication;
import com.rogelioorts.rotten.potatoes.shared.exceptions.JsonExceptionHandler;
import com.rogelioorts.rotten.potatoes.shared.exceptions.ResourceNotFoundHandler;
import com.rogelioorts.rotten.potatoes.shared.repositories.BaseRepository;

import io.vertx.core.http.HttpMethod;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class Application extends BaseApplication {

  private static final String RATING_PATH = "/api/v1/series/:idSerie/rating";

  public static final String SERVICE_NAME = "series.rating";

  @Override
  protected String getServiceName() {
    return SERVICE_NAME;
  }

  @Override
  protected Router getRouter() {
    final MongoClient client = BaseRepository.createClient(vertx);
    final RatingsRepository ratingsRepository = new RatingsRepository(vertx, client);

    final Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());

    router.route(HttpMethod.POST, RATING_PATH).handler(new CreateRatingHandler(ratingsRepository));

    router.route().handler(new ResourceNotFoundHandler()).failureHandler(new JsonExceptionHandler());

    return router;
  }

}
