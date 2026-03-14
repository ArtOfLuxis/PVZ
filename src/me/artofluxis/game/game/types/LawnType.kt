package me.artofluxis.game.game.types

import me.artofluxis.game.Position
import me.artofluxis.game.animation.*
import me.artofluxis.game.game.objects.*

class LawnType(
    val id: String,
    val animationPack: AnimationPack,
    val rows: Int,
    val columns: Int,
    val tileSize: Pair<Int, Int>,
    val lawnUpperLeftCorner: Pair<Int, Int>,
    val teamSizes: HashMap<ObjectTeam, Double>,
    val teamOffsets: HashMap<ObjectTeam, Pair<Double, Double>>,
    val tileSet: List<List<TileType>>,
) {
    init {
        if (tileSet.size != rows) throw IllegalArgumentException(
                "Tileset for lawn '$id' has ${tileSet.size} rows, but the lawn requires $rows rows"
        )
        tileSet.forEachIndexed { index, columnsInRow ->
            if (columnsInRow.size != columns) throw IllegalArgumentException(
                "Row #${index + 1} in the tileset for lawn '$id' has $columnsInRow columns, but the lawn requires $columns columns"
            )
        }
    }

    fun getTileCenter(x: Int, y: Int): Position {
        return Position(
            (x - 0.5) * tileSize.first.toDouble()  + lawnUpperLeftCorner.first,
            (y - 0.5) * tileSize.second.toDouble() + lawnUpperLeftCorner.second
        )
    }

    override fun toString() = "${this::class.simpleName}[$id]"
}
