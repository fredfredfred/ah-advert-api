package ah.advert.module

import ah.advert.Settings
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

trait ActorModule {

  implicit val actorSystem = ActorSystem("main")
  implicit val materializer = ActorMaterializer()
  implicit lazy val ec = actorSystem.dispatchers.lookup("akka-http-routes-dispatcher")

  val settings = Settings(actorSystem)

}
