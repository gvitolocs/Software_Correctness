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
   * first line "BOX x1 y1 x2 y2", then one line per shape (LINE, CIRCLE, RECTANGLE, TEXT, etc.)
   * with optional colour. The Java GUI shows errors in the error box and passes the part
   * after "OK\n" to the canvas. The first line of the program must be (BOUNDING-BOX (x1 y1) (x2 y2)).
   * Then you have (DRAW c g1 g2 ...), (FILL c g), or single shapes. Default colour is black.
   * Output shapes in the same order they appear in the program.
   */
  def interpret(program: String): String = {
    Parser.parseInput(program) match {
      case Left(err) => "ERR\n" + err
      case Right(commands) =>
        val engine = new DrawingEngine()
        engine.execute(commands) match {
          case Left(err) => "ERR\n" + err
          case Right(_) =>
            // Serialize commands to the format Java canvas expects (high-level shapes)
            "OK\n" + commandsToOutputString(commands)
        }
    }
  }

  /** Convert parsed commands to string format for Java: BOX first, then LINE/CIRCLE/RECTANGLE/TEXT with optional color. */
  private def commandsToOutputString(commands: List[Command]): String = {
    def colorName(c: Color): String = c match {
      case drawing.model.Black => "black"
      case drawing.model.Red => "red"
      case drawing.model.Green => "green"
      case drawing.model.Blue => "blue"
    }
    def go(cmds: List[Command], currentColor: Color): List[String] = cmds.flatMap {
      case BoundingBox(x1, y1, x2, y2) => List(s"BOX $x1 $y1 $x2 $y2")
      case Line(x1, y1, x2, y2) => List(s"LINE $x1 $y1 $x2 $y2 ${colorName(currentColor)}")
      case Rectangle(x1, y1, x2, y2) => List(s"RECTANGLE $x1 $y1 $x2 $y2 ${colorName(currentColor)}")
      case Circle(x, y, r) => List(s"CIRCLE $x $y $r ${colorName(currentColor)}")
      case TextAt(x, y, t) => List(s"TEXT $x $y $t ${colorName(currentColor)}")
      case Draw(c, items) => items.flatMap(item => go(List(item), c))
      case Fill(c, item) => go(List(item), c)
    }
    if (commands.isEmpty) ""
    else go(commands, Black).mkString("\n")
  }
}
