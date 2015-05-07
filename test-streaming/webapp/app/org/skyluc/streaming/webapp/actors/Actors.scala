package org.skyluc.streaming.webapp.actors

import play.api.Application
import play.api.Plugin
import play.api.libs.concurrent.Akka
import akka.actor.ActorSystem

/**
 * Lookup for actors used by the web front end.
 */
object Actors {

  private def actors(implicit app: Application): Actors = app.plugin[Actors]
    .getOrElse(sys.error("Actors plugin not registered"))

  def requestLogger(implicit app: Application) = actors.requestLogger
  
  def statsStore(implicit app: Application) = actors.statsStore
}

class Actors(app: Application) extends Plugin {
  
  private def system: ActorSystem= Akka.system(app)
  
  private lazy val requestLogger = system.actorOf(RequestLoggerActor.props(), "requestLogger")
  
  private lazy val statsStore = system.actorOf(StatsStoreActor.props(), "statsStore")
  
  override def onStart() {
    requestLogger ! RequestLoggerActor.StartMsg
  }
  
  override def onStop() {
    requestLogger ! RequestLoggerActor.StopMsg
  }
}

