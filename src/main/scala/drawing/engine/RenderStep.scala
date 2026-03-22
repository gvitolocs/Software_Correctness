package drawing.engine

import drawing.model._

case class RenderStep(
                       command: Command,
                       allDrawables: List[Drawable],
                       highlighted: List[Drawable]
                     )