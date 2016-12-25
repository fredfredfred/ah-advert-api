package ah.advert

import ah.advert.module.AppModules
import akka.event.Logging
import akka.http.scaladsl.Http

object Main extends App with AppModules {

  val logger = Logging(actorSystem, getClass)


  createSchemaTables

  Http().bindAndHandle(loggedRoutes, settings.Http.interface, settings.Http.port) map { binding =>
    logger.info(s"Server started on port {}", binding.localAddress.getPort)
  } recoverWith { case _ => actorSystem.terminate() }

}
