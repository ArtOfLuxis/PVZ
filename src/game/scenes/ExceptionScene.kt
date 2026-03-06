package game.scenes

import korlibs.image.color.*
import korlibs.korge.scene.*
import korlibs.korge.view.*
import korlibs.math.geom.vector.*

class ExceptionScene(private val exception: Exception) : Scene() {
    override suspend fun SContainer.sceneMain() {
        val padding = 20.0
        val crossSize = 30.0

        val cross = graphics {
            stroke(Colors.RED, StrokeInfo(4.0)) {
                moveTo(0f, 0f)
                lineTo(crossSize.toFloat(), crossSize.toFloat())
                moveTo(0f, crossSize.toFloat())
                lineTo(crossSize.toFloat(), 0f)
            }
        }
        cross.xy(padding, padding)

        text("Encountered Exception", textSize = 24.0, color = Colors.WHITE) {
            this.xy(padding + crossSize + 10, padding)
        }

        val stackText = exception.stackTraceToString()
        val lines = stackText.lines()
        val lineHeight = 16.0
        val startY = padding + crossSize + 10
        lines.forEachIndexed { index, line ->
            text(line, textSize = 14.0, color = Colors.WHITE) {
                this.xy(padding, startY + index * lineHeight)
            }
        }
    }
}
