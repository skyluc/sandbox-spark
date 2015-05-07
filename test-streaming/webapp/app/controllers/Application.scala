package controllers

import scala.annotation.implicitNotFound
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt

import org.skyluc.streaming.webapp.actors.Actors
import org.skyluc.streaming.webapp.actors.RequestLoggerActor
import org.skyluc.streaming.webapp.actors.StatsStoreActor

import akka.actor.actorRef2Scala
import akka.pattern.ask
import akka.util.Timeout
import play.api.Play.current
import play.api.mvc.Action
import play.api.mvc.Controller

object Application extends Controller {

  def index() = path("")

  def path(path: String) = Action {
    val completePath = s"/$path"
    logPath(completePath)
    Ok(views.html.index(completePath))
  }

  def stats() = Action.async {
    logPath("/stats")
    implicit val timeout = Timeout(5 seconds)
    ask(Actors.statsStore, StatsStoreActor.GetDataMsg).mapTo[StatsStoreActor.DataMsg]
      .map(msg => Ok(msg.data))
  }

  def postStats() = Action { request =>
    println(request.body.asText)
    Actors.statsStore ! StatsStoreActor.DataMsg(request.body.asText.getOrElse("<empty>"))
    logPath("/postStats")
    Ok("Yeap")
  }

  private def logPath(path: String) {
    Actors.requestLogger ! RequestLoggerActor.MessageMsg(path)
  }

}