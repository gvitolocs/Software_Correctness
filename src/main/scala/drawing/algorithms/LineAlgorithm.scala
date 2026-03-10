package drawing.algorithms

import drawing.model._

object LineAlgorithm {

  def bresenham(x1: Int, y1: Int, x2: Int, y2: Int, color: Color): List[Pixel] = {
    val pixels = scala.collection.mutable.ListBuffer[Pixel]()

    var x = x1
    var y = y1

    val dx = Math.abs(x2 - x1)
    val dy = Math.abs(y2 - y1)

    val sx = if (x1 < x2) 1 else -1
    val sy = if (y1 < y2) 1 else -1

    var err = dx - dy

    while (true) {
      pixels += Pixel(x, y, color)

      if (x == x2 && y == y2) {
        return pixels.toList
      }

      val e2 = 2 * err

      if (e2 > -dy) {
        err -= dy
        x += sx
      }

      if (e2 < dx) {
        err += dx
        y += sy
      }
    }

    pixels.toList
  }
}
