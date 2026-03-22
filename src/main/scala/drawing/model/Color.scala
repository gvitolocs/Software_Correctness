package drawing.model

sealed trait Color {
  def toHex: String
}

case object Black extends Color {
  override def toHex: String = "#000000"
}

case object Red extends Color {
  override def toHex: String = "#FF0000"
}

case object Green extends Color {
  override def toHex: String = "#00FF00"
}

case object Blue extends Color {
  override def toHex: String = "#0000FF"
}
