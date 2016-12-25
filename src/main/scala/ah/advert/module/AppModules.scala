package ah.advert.module

import scala.concurrent.ExecutionContext

trait AppModules extends DaoModule with ActorModule with ServiceModule with RoutesModule {

  // dependencies
  implicit val ec: ExecutionContext
}