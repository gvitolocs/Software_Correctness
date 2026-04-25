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
  def interpret(program: String): String = interpret(program, 1)

  def interpret(program: String, scale: Int): String = {
    Parser.parseInput(program) match {
      case Left(err) => "ERR\n" + err
      case Right(rawCommands) =>
        val commands = if (scale == 1) rawCommands else scaleCommands(rawCommands, scale)
        val engine = new DrawingEngine()
        engine.executeWithSteps(commands) match {
          case Left(err)    => "ERR\n" + err
          case Right(steps) =>
            val lastCommand = steps.lastOption.map(_.command)
            val boxStr = commands.collectFirst {
              case BoundingBox(x1, y1, x2, y2) => s"BOX $x1 $y1 $x2 $y2"
            }.getOrElse("")
            val shapeLines = serializeCommands(commands, Black, lastCommand)
            "OK\n" + (boxStr :: shapeLines).mkString("\n")
        }
    }
  }

  private def scaleCommands(commands: List[Command], f: Int): List[Command] =
    commands.map(scaleCommand(_, f))

  private def scaleCommand(command: Command, f: Int): Command = command match {
    case BoundingBox(x1, y1, x2, y2) => BoundingBox(x1 * f, y1 * f, x2 * f, y2 * f)
    case Line(x1, y1, x2, y2)        => Line(x1 * f, y1 * f, x2 * f, y2 * f)
    case Rectangle(x1, y1, x2, y2)   => Rectangle(x1 * f, y1 * f, x2 * f, y2 * f)
    case Circle(x, y, r)             => Circle(x * f, y * f, r * f)
    case TextAt(x, y, t)             => TextAt(x * f, y * f, t)
    case Draw(color, items)          => Draw(color, items.map(scaleCommand(_, f)))
    case Fill(color, item)           => Fill(color, scaleCommand(item, f))
  }

  private def serializeCommands(cmds: List[Command], color: Color, lastCmd: Option[Command]): List[String] =
    cmds.flatMap { cmd =>
      val isLast = lastCmd.contains(cmd)
      serializeOne(cmd, color, isLast, lastCmd)
    }

  private def serializeOne(cmd: Command, color: Color, isLast: Boolean, lastCmd: Option[Command]): List[String] = {
    val flag   = if (isLast) " highlight" else ""
    cmd match {
      case BoundingBox(_, _, _, _) => Nil
      case Line(x1, y1, x2, y2) =>
        List(s"LINE $x1 $y1 $x2 $y2 ${colorName(color)}$flag")
      case Rectangle(x1, y1, x2, y2) =>
        List(s"RECTANGLE $x1 $y1 $x2 $y2 ${colorName(color)}$flag")
      case Circle(x, y, r) =>
        List(s"CIRCLE $x $y $r ${colorName(color)}$flag")
      case TextAt(x, y, t) =>
        List(s"TEXT ${formatNumber(x)} ${formatNumber(y)} $t ${colorName(color)}$flag")
      case Draw(c, items) =>
        items.flatMap(item => serializeOne(item, c, isLast, lastCmd))
      case Fill(c, item) =>
        def fillItem(inner: Command, borderColor: Color): List[String] = inner match {
          case Circle(x, y, r) =>
            List(s"FILLED-CIRCLE $x $y $r ${colorName(c)}$flag",
                 s"CIRCLE $x $y $r ${colorName(borderColor)}$flag")
          case Rectangle(x1, y1, x2, y2) =>
            List(s"FILLED-RECTANGLE $x1 $y1 $x2 $y2 ${colorName(c)}$flag",
                 s"RECTANGLE $x1 $y1 $x2 $y2 ${colorName(borderColor)}$flag")
          case Draw(dc, items) => items.flatMap(fillItem(_, dc))
          case _              => serializeOne(inner, c, isLast, lastCmd)
        }
        fillItem(item, color)
    }
  }

  private def colorName(c: Color): String = c match {
    case Black => "black"
    case Red   => "red"
    case Green => "green"
    case Blue  => "blue"
  }

  private def formatNumber(v: Double): String = {
    if (v.isWhole) v.toInt.toString
    else {
      val s = f"$v%.2f"
      s.replaceAll("0+$", "").replaceAll("\\.$", "")
    }
  }

}
