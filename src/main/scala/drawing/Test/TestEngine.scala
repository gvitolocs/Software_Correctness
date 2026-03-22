package drawing.Test

import drawing.engine._
import drawing.model._

object TestEngine extends App {

    val engine = new DrawingEngine()

    val commands = List(
      BoundingBox(0, 0, 20, 20),

      Line(2, 2, 10, 6),

      Circle(10, 10, 4),

      Draw(Red, List(
        Line(0, 0, 5, 5)
      )),

      TextAt(5, 15, "hello")
    )

    val result = engine.execute(commands)

    result match {
      case Right(drawables) =>
        drawables.foreach(println)

      case Left(error) =>
        println("Error: " + error)
    }
  }

