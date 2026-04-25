package drawing.model

sealed trait Command

case class BoundingBox(x1: Int, y1: Int, x2: Int, y2: Int) extends Command
case class Line(x1: Int, y1: Int, x2: Int, y2: Int) extends Command
case class Rectangle(x1: Int, y1: Int, x2: Int, y2: Int) extends Command
case class Circle(x: Int, y: Int, radius: Int) extends Command
case class TextAt(x: Double, y: Double, text: String) extends Command
case class Draw(color: Color, items: List[Command]) extends Command
case class Fill(color: Color, item: Command) extends Command