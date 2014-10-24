package com.kanban

import akka.actor.Actor
import spray.http.MediaTypes._
import spray.routing._
import spray.http._
import akka.event.Logging
import spray.routing.directives._

// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class WeKanbanActor extends Actor with WeKanbanService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(appRoutes)
}

// this trait defines our service behavior independently from the service actor
trait WeKanbanService extends HttpService with WeKanbanRoutes with StaticRoutes {
  def showPath(req: HttpRequest) = LogEntry("Method = %s, Path = %s" format(req.method, req.uri), Logging.InfoLevel)

  val appRoutes = logRequest(showPath _) {
    dynamicRoutes ~ staticRoutes
  }
}
