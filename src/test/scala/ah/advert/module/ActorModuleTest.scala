package ah.advert.module

import akka.actor.ActorSystem

trait ActorModuleTest {

  val actorSystem = ActorSystem("main")
  implicit lazy val ec = actorSystem.dispatchers.lookup("akka-http-routes-dispatcher")


}
