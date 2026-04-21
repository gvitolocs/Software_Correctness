package plpa.graphics

import drawing.parser.Parser
import drawing.engine.DrawingEngine
import drawing.model._

/**
 * Interpreter for the graphics language. All drawing state (bounding box, colours,
 * list of shapes) must live here in Scala. The program text from the editor is passed
 * as-is; do not change it before calling interpret.
 */
object GraphicsLogic {

  /**
   * Interpret the whole program string. If there are errors, return "ERR\n" followed by
   * one error message per line. Otherwise return "OK\n" followed by the drawing data:
   * first line "BOX x1 y1 x2 y2", then one line per drawable ("PIXEL x y color" or
   * "TEXT x y text color"). The Java GUI shows errors in the error box and passes the part
   * after "OK\n" to the canvas. The first line of the program must be (BOUNDING-BOX (x1 y1) (x2 y2)).
   */
  def interpret(program: String): String = {
    Parser.parseInput(program) match {
      case Left(err) => "ERR\n" + err
      case Right(commands) =>
        val engine = new DrawingEngine()
        engine.execute(commands) match {
          case Left(err) => "ERR\n" + err
          case Right(drawables) =>
            val boxLine = commands.collectFirst { case BoundingBox(x1, y1, x2, y2) => s"BOX $x1 $y1 $x2 $y2" }.getOrElse("")
            "OK\n" + drawablesToOutputString(boxLine, drawables)
        }
    }
  }

  private def colorName(c: Color): String = c match {
    case Black => "black"
    case Red   => "red"
    case Green => "green"
    case Blue  => "blue"
  }

  /** Serialize drawables from DrawingEngine: BOX first, then one line per PIXEL or TEXT (color comes with each). */
  private def drawablesToOutputString(boxLine: String, drawables: List[Drawable]): String = {
    val lines = drawables.map {
      case Pixel(x, y, c)   => s"PIXEL $x $y ${colorName(c)}"
      case Text(x, y, t, c) => s"TEXT $x $y $t ${colorName(c)}"
    }
    if (boxLine.isEmpty) lines.mkString("\n")
    else (boxLine :: lines).mkString("\n")
  }

}
