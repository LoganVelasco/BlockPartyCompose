package logan.blockpartycompose.data

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import logan.blockpartycompose.data.models.Level
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet
import java.io.File
import javax.inject.Inject

class DataRepository @Inject constructor() {

    fun getNewLevel(x: Int, y: Int): Level{
        return Level(
            name = "",
            x = x,
            y = y,
            initialBlocks = getBlankLayout(x, y)
        )
    }

    private fun getBlankLayout(x: Int, y: Int): List<Char> {
        return MutableList(x*y){
            '.'
        }
    }

    fun getLevels(levelSet: LevelSet): List<Level> {
        return when (levelSet) {
            LevelSet.EASY -> {
//                val file = File(context.filesDir, "easy.txt")
//                Json.decodeFromString(file.readText())
                listOf(
                    Level(name = "1",  x = 4, y = 6, initialBlocks = level1),
                    Level(name = "2",  x = 4, y = 6, initialBlocks = level2),
                    Level(name = "3",  x = 4, y = 6, initialBlocks = level3),
                    Level(name = "4",  x = 6, y = 8, initialBlocks = level4),
                    Level(name = "5",  x = 6, y = 8, initialBlocks = level5),
                    Level(name = "6",  x = 6, y = 8, initialBlocks = level6),
                    Level(name = "7",  x = 6, y = 8, initialBlocks = level7),
                    Level(name = "8",  x = 6, y = 8, initialBlocks = level8),
                    Level(name = "9",  x = 6, y = 8, initialBlocks = level9),
                    Level(name = "10", x = 6, y = 8, initialBlocks = level10),
                )
            }
            LevelSet.MEDIUM -> {
//                val text =
//                    File("../Json.decodeFromString<List<Level>>(levels/medium.txt").readText()
//                Json.decodeFromString(text)
                listOf(
                    Level(name = "11",  x = 6, y = 8, initialBlocks = level11),
                    Level(name = "12",  x = 6, y = 8, initialBlocks = level12),
                    Level(name = "13",  x = 6, y = 8, initialBlocks = level13),
                )
            }
            LevelSet.HARD -> {
                val text = File("src/main/res/hard.txt").readText()
                Json.decodeFromString(text)
            }
            LevelSet.CUSTOM -> {
                val text = File("../levels/custom.txt").readText()
                Json.decodeFromString(javaClass.getResource("/html/file.html").readText())
            }
        }
    }

    private fun createLevelList(count: Int): List<Char> {
//        val list = mutableListOf<Block>()
//        for (i in 0 until count) {
//            val block: Block = when (level3[i]){
//                '.' -> {Block(color = BlockColor.GREY, index = i)}
//                'b' -> {Block(color = BlockColor.BLUE, index = i)}
//                'r' -> {Block(color = BlockColor.RED, index = i)}
//                'x' -> {Block(color = BlockColor.BLACK, index = i)}
//                'y' -> {Block(color = BlockColor.YELLOW, index = i)}
//                else -> {Block(color = BlockColor.GREY, index = i)}
//            }
//            list.add(block)
//        }
//        return list
        return level3
    }

    var level1 = listOf(
        '.', '.', '.', '.',
        '.', '.', '.', '.',
        '.', 'b', '.', '.',
        '.', '.', '.', '.',
        '.', '.', 'y', '.',
        '.', '.', '.', '.',
    )

    var level2 = listOf(
        'r', '.', '.', '.',
        '.', '.', '.', '.',
        '.', '.', '.', '.',
        '.', 'b', '.', 'y',
        '.', '.', '.', '.',
        '.', '.', '.', '.',
    )

    var level3 = listOf(
        '.', 'r', '.', '.',
        '.', 'x', '.', '.',
        '.', 'b', '.', '.',
        '.', '.', '.', '.',
        '.', '.', '.', '.',
        '.', '.', 'y', '.',
    )
    var level4 = listOf(
        '.', '.', '.', 'r', '.', 'y',
        '.', '.', '.', '.', '.', '.',
        '.', '.', '.', '.', '.', '.',
        '.', 'x', 'x', '.', '.', '.',
        '.', 'x', '.', '.', '.', '.',
        '.', 'x', 'x', '.', '.', '.',
        '.', '.', '.', '.', '.', '.',
        '.', '.', '.', 'b', '.', '.',
    )
    var level5 = listOf(
        '.', '.', '.', '.', '.', 'y',
        '.', '.', '.', '.', '.', '.',
        '.', '.', '.', 'r', '.', '.',
        '.', '.', '.', '.', '.', '.',
        '.', 'x', '.', 'x', '.', '.',
        'b', '.', '.', '.', 'x', '.',
        '.', '.', '.', 'x', '.', '.',
        '.', '.', '.', '.', '.', '.',
    )
    var level6 = listOf(
        '.', '.', '.', '.', '.', '.',
        '.', '.', '.', '.', '.', '.',
        '.', 'r', '.', '.', '.', '.',
        'y', '.', '.', 'x', '.', '.',
        '.', '.', '.', '.', 'x', '.',
        '.', '.', 'b', '.', '.', '.',
        '.', '.', '.', '.', '.', '.',
        '.', '.', '.', '.', '.', '.',
    )

    var level7 = listOf(
        '.', 'b', '.', '.', '.', '.',
        '.', '.', '.', '.', '.', 'x',
        '.', '.', '.', '.', 'x', '.',
        '.', '.', '.', '.', '.', '.',
        '.', '.', '.', '.', '.', '.',
        '.', '.', '.', '.', '.', '.',
        '.', 'r', '.', '.', '.', '.',
        'y', '.', '.', '.', '.', '.',
    )
    var level8 = listOf(
        '.', '.', '.', '.', 'y', 'r',
        '.', '.', '.', '.', '.', '.',
        '.', 'x', '.', '.', '.', '.',
        '.', '.', '.', '.', '.', '.',
        '.', '.', '.', '.', 'x', '.',
        '.', '.', '.', '.', '.', 'x',
        '.', '.', '.', '.', '.', '.',
        'b', '.', '.', '.', '.', '.',
    )

    var level9 = listOf(
        '.', '.', '.', '.', '.', '.',
        '.', 'r', '.', '.', '.', 'y',
        '.', '.', '.', '.', '.', '.',
        '.', '.', '.', '.', '.', '.',
        '.', '.', 'x', '.', '.', '.',
        '.', '.', '.', '.', 'g', 'b',
        '.', '.', '.', '.', '.', '.',
        '.', '.', '.', '.', '.', '.',
    )
    var level10 = listOf(
        '.', '.', '.', '.', '.', 'y',
        '.', '.', 'r', '.', '.', '.',
        '.', '.', '.', '.', '.', '.',
        'g', 'g', 'g', 'g', 'g', 'g',
        'g', 'g', 'g', 'g', 'g', 'g',
        'b', 'x', '.', '.', 'g', 'g',
        '.', 'x', 'x', 'x', 'g', '.',
        'g', '.', '.', 'g', '.', '.',
    )

    var level11 = listOf(
        '.', '.', '.', '.', '.', '.',
        '.', '.', '.', 'b', '.', '.',
        '.', '.', 'x', '.', '.', '.',
        '.', '.', 'g', '.', '.', '.',
        '.', 'g', 'x', '.', '.', '.',
        '.', '.', '.', '.', '.', '.',
        '.', '.', '.', '.', '.', '.',
        '.', 'r', 'y', '.', '.', '.',
    )

    var level12 = listOf(
        'g', 'g', '.', '.', 'g', '.',
        'g', 'b', '.', '.', 'g', '.',
        '.', '.', '.', '.', 'g', '.',
        'g', '.', '.', '.', '.', '.',
        '.', 'g', '.', '.', '.', '.',
        '.', '.', '.', '.', '.', '.',
        '.', '.', '.', 'x', 'x', '.',
        '.', '.', '.', 'x', 'r', 'y',
    )
    var level13 = listOf(
        'b', '.', '.', '.', '.', '.',
        '.', '.', '.', 'g', '.', '.',
        '.', 'g', '.', '.', 'x', '.',
        '.', '.', 'g', '.', '.', '.',
        '.', 'g', '.', '.', '.', '.',
        'g', '.', '.', '.', 'g', '.',
        '.', '.', '.', '.', 'g', '.',
        '.', '.', '.', 'r', 'x', 'y',
    )

}
