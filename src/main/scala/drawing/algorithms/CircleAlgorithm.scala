package drawing.algorithms

import drawing.model._


object CircleAlgorithm {

  def midpoint(cx: Int, cy: Int, radius: Int, color: Color): List[Pixel] = {
    val pixels = scala.collection.mutable.ListBuffer[Pixel]()

    var x = radius
    var y = 0
    var err = 1 - radius

    def addSymmetricPoints(px: Int, py: Int): Unit = {
      pixels += Pixel(cx + px, cy + py, color)
      pixels += Pixel(cx + py, cy + px, color)
      pixels += Pixel(cx - py, cy + px, color)
      pixels += Pixel(cx - px, cy + py, color)
      pixels += Pixel(cx - px, cy - py, color)
      pixels += Pixel(cx - py, cy - px, color)
      pixels += Pixel(cx + py, cy - px, color)
      pixels += Pixel(cx + px, cy - py, color)
    }

    while (x >= y) {
      addSymmetricPoints(x, y)
      y += 1

      if (err < 0) {
        err += 2 * y + 1
      } else {
        x -= 1
        err += 2 * (y - x) + 1
      }
    }

    pixels.toList.distinct
  }
}