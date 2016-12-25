package ah.advert.module

trait TestModules extends DaoModuleTest with ActorModuleTest with ServiceModule with RoutesModule {
  //  implicit val ec: ExecutionContext
}