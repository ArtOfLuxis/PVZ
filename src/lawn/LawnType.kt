package lawn

import korlibs.io.file.VfsFile
import tile.*

data class LawnType(
    val id: String,
    val asset: VfsFile,
    val sunSprite: VfsFile,
    val rows: Int,
    val columns: Int,
    val tileSize: Pair<Int, Int>,
    val lawnUpperLeftCorner: Pair<Int, Int>,
    val plantSize: Float,
    val zombieSize: Float,
    val tileSet: List<List<TileType>>
) {
    init {
        if (tileSet.size != rows) throw IllegalArgumentException(
                "Tileset for lawn '$id' has ${tileSet.size} rows, but the lawn requires $rows rows"
        )
        tileSet.forEachIndexed { index, columnsList ->
            if (columnsList.size != columns) throw IllegalArgumentException(
                "Row #${index + 1} in the tileset for lawn '$id' has ${columnsList.size} columns, but the lawn requires $columns columns"
            )
        }
    }

    fun getTileCenter(x: Int, y: Int): Pair<Int, Int> {
        return  lawnUpperLeftCorner.first  + x * tileSize.first to
                lawnUpperLeftCorner.second + y * tileSize.second
    }
}
