package lawn

import Position
import korlibs.io.file.VfsFile
import tile.*

class LawnType(
    val id: String,
    val asset: VfsFile,
    val sunSprite: VfsFile,
    val rows: Int,
    val columns: Int,
    val tileSize: Pair<Int, Int>,
    val lawnUpperLeftCorner: Pair<Int, Int>,
    val plantSize: Float,
    val zombieSize: Float,
    val defaultTile: TileType,
    val tileSet: HashMap<Pair<Int, Int>, TileType>,
    tileSetRows: Int,
    tileSetColumns: List<Int>
) {
    init {
        if (tileSetRows != rows) throw IllegalArgumentException(
                "Tileset for lawn '$id' has ${tileSet.size} rows, but the lawn requires $rows rows"
        )
        tileSetColumns.forEachIndexed { index, columnsInRow ->
            if (columnsInRow != columns) throw IllegalArgumentException(
                "Row #${index + 1} in the tileset for lawn '$id' has $columnsInRow columns, but the lawn requires $columns columns"
            )
        }
    }

    fun getTileCenter(x: Int, y: Int): Position {
        return Position( (x - 0.5) * tileSize.first.toDouble()  + lawnUpperLeftCorner.first,
                         (y - 0.5) * tileSize.second.toDouble() + lawnUpperLeftCorner.second
        )
    }
}
