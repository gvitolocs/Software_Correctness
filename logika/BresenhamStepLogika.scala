// #Sireum #Logika
import org.sireum._

// This file is not part of the drawing application. It is a simplified Logika
// model of the mathematical update step of Bresenham's algorithm, used for
// algorithm testing.
object BresenhamStepLogika {

  def step(xIn: Z, yIn: Z, errIn: Z, dx: Z, dy: Z, sx: Z, sy: Z): (Z, Z, Z) = {
    Contract(
      Requires(
        dx >= 0,
        dy >= 0,
        sx == 1 | sx == -1,
        sy == 1 | sy == -1
      ),
      Ensures(
        Res._1 == xIn | Res._1 == xIn + sx,
        Res._2 == yIn | Res._2 == yIn + sy
      )
    )

    var x: Z = xIn
    var y: Z = yIn
    var err: Z = errIn

    val e2: Z = 2 * err

    if (e2 > -dy) {
      err = err - dy
      x = x + sx
    }

    if (e2 < dx) {
      err = err + dx
      y = y + sy
    }

    return (x, y, err)
  }

  def testMoveBoth(): Unit = {
    val r = step(0, 0, 1, 5, 3, 1, 1)
    assert(r._1 == 1)
    assert(r._2 == 1)
    assert(r._3 == 3)
  }

  def testMoveOnlyX(): Unit = {
    val r = step(0, 0, 3, 5, 3, 1, 1)
    assert(r._1 == 1)
    assert(r._2 == 0)
    assert(r._3 == 0)
  }

  def testMoveOnlyY(): Unit = {
    val r = step(0, 0, -2, 5, 3, 1, 1)
    assert(r._1 == 0)
    assert(r._2 == 1)
    assert(r._3 == 3)
  }
}
