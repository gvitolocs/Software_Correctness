package drawing.Test

import drawing.engine._
import drawing.model._

object TestSteps extends App {

  val engine = new DrawingEngine()

  val commands = List(
    BoundingBox(0, 0, 20, 20),
    Line(1, 1, 5, 1),
    Fill(Red, Rectangle(2, 2, 4, 4)),
    Circle(10, 10, 3)
  )

  engine.executeWithSteps(commands) match {
    case Right(steps) =>
      steps.zipWithIndex.foreach { case (step, i) =>
        println(s"STEP ${i + 1}")
        println(s"Command: ${step.command}")
        println(s"Total drawables: ${step.allDrawables.size}")
        println(s"Highlighted: ${step.highlighted.size}")
        println(step.highlighted.mkString("  ", "\n  ", "\n"))
      }

    case Left(error) =>
      println("Error: " + error)
  }
}
