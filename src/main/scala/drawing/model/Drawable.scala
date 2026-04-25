package drawing.model

sealed trait Drawable

case class Pixel(x: Int, y: Int, color: Color) extends Drawable
case class Text(x: Double, y: Double, text: String, color: Color) extends Drawable
