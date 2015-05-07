package org.skyluc.streaming.webapp.actors

import akka.actor.Props
import akka.actor.Actor
import org.skyluc.serfur.Server
import akka.actor.ActorRef
import java.nio.channels.AsynchronousSocketChannel
import akka.actor.PoisonPill
import java.nio.ByteBuffer

class RequestLoggerActor extends Actor {

  import context._

  def receive: Actor.Receive = initialization

  val initialization: Actor.Receive = {
    case RequestLoggerActor.StartMsg =>
      val server = Server.apply(self)
      become(processConnectionsAndMessages(server, Nil))
  }

  def processConnectionsAndMessages(server: Server, connectionActors: List[ActorRef]): Actor.Receive = {
    case Server.IncomingConnectionMsg(socket) =>
      val connectionActor = actorOf(ConnectionActor.props(socket, self))
      become(processConnectionsAndMessages(server, connectionActor :: connectionActors))
    case m: RequestLoggerActor.MessageMsg =>
      connectionActors.foreach { _ ! m }
    case RequestLoggerActor.ConnectionClosedMsg(connectionActor) =>
      become(processConnectionsAndMessages(server, connectionActors.filterNot(_ == connectionActor)))
    case RequestLoggerActor.StopMsg =>
      connectionActors.foreach { _ ! RequestLoggerActor.StopMsg }
      server.close()
      become(initialization)
  }

}

object RequestLoggerActor {

  object StartMsg
  object StopMsg
  case class MessageMsg(message: String)
  case class ConnectionClosedMsg(actor: ActorRef)

  def props() = Props(classOf[RequestLoggerActor])

}

class ConnectionActor(socket: AsynchronousSocketChannel, logger: ActorRef) extends Actor {

  def receive: Actor.Receive = {
    case Server.ConnectionClosedMsg =>
      logger ! RequestLoggerActor.ConnectionClosedMsg(self)
      self ! PoisonPill
    case RequestLoggerActor.MessageMsg(message) =>
      socket.write(ByteBuffer.wrap(s"$message\n".getBytes))
    case RequestLoggerActor.StopMsg =>
      socket.close()
      self ! PoisonPill
  }
}

object ConnectionActor {
  def props(socket: AsynchronousSocketChannel, logger: ActorRef) = Props(classOf[ConnectionActor], socket, logger)
}

class StatsStoreActor extends Actor {

  import context._

  def receive = store("")
  
  def store(data: String): Actor.Receive = {
    case StatsStoreActor.DataMsg(data) =>
      become(store(data))
    case StatsStoreActor.GetDataMsg =>
      println("===== get stats")
      println(data)
      sender ! StatsStoreActor.DataMsg(data)
  }

}

object StatsStoreActor {

  case class DataMsg(data: String)
  object GetDataMsg

  def props() = Props[StatsStoreActor]()
}


