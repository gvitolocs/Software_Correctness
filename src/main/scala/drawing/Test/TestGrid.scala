package drawing.Test

import drawing.engine.DrawingEngine
import drawing.model._

object TestGrid {

  def main(args: Array[String]): Unit = {
    println("Programma partito")

    val engine = new DrawingEngine()

    val commands = List(
      Line(1, 1, 10, 5),
      Circle(15, 7, 4)
    )

    engine.execute(commands) match {
      case Right(drawables) =>
        val pixels = drawables.collect { case p: Pixel => p }
        println(s"Numero pixel: ${pixels.size}")
        printAsciiGrid(pixels)

      case Left(error) =>
        println("Errore: " + error)
    }

    println("Programma finito")
  }

  def printAsciiGrid(pixels: List[Pixel]): Unit = {
    if (pixels.isEmpty) {
      println("Nessun pixel da stampare.")
      return
    }

    val minX = pixels.map(_.x).min
    val maxX = pixels.map(_.x).max
    val minY = pixels.map(_.y).min
    val maxY = pixels.map(_.y).max

    val padding = 1

    val points = pixels.map(p => (p.x, p.y)).toSet

    for (y <- (minY - padding) to (maxY + padding)) {
      for (x <- (minX - padding) to (maxX + padding)) {
        if (points.contains((x, y))) print("X")
        else print(".")
      }
      println()
    }
  }
}