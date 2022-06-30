package logan.blockpartycompose.data

import logan.blockpartycompose.data.models.Level
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepository @Inject constructor(private val gameData: GameData) {

    fun getNewLevel(x: Int, y: Int): Level{
        return Level(
            name = 0,
            levelSet = LevelSet.CUSTOM,
            x = x,
            y = y,
            initialBlocks = getBlankLayout(x, y),
            minMoves = 0
        )
    }

    private fun getBlankLayout(x: Int, y: Int): List<Char> {
        return MutableList(x*y){
            '.'
        }
    }

    fun getLevel(name: Int): Level {
        return levels.first{ it.name == name }
    }

    fun getLevels(levelSet: LevelSet): List<Level> {
        return levels.filter { it.levelSet == levelSet }
    }

    fun getDifficultyProgress(): List<Int>{
        return listOf(gameData.easyProgress, gameData.mediumProgress, gameData.hardProgress)
    }

    fun getLevelsProgress(difficulty: LevelSet):List<Int>{
        return when(difficulty){
            LevelSet.EASY -> {
                gameData.easyLevelProgress
            }
            LevelSet.MEDIUM -> {
                gameData.mediumLevelProgress
            }
            LevelSet.HARD -> {
                gameData.hardLevelProgress
            }
            LevelSet.CUSTOM -> TODO()
        }
    }

    fun updateLevelProgress(difficulty: LevelSet, level: Int, stars: Int){
        gameData.updateLevel(difficulty, level, stars)
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

    val levels = listOf(
        Level(name = 1,  levelSet = LevelSet.EASY, x = 4, y = 6, initialBlocks = level1, minMoves = 3),
        Level(name = 2,  levelSet = LevelSet.EASY, x = 4, y = 6, initialBlocks = level2, minMoves = 2),
        Level(name = 3,  levelSet = LevelSet.EASY, x = 4, y = 6, initialBlocks = level3, minMoves = 4),
        Level(name = 4,  levelSet = LevelSet.EASY, x = 6, y = 8, initialBlocks = level4, minMoves = 17),
        Level(name = 5,  levelSet = LevelSet.EASY, x = 6, y = 8, initialBlocks = level5, minMoves = 14),
        Level(name = 6,  levelSet = LevelSet.EASY, x = 6, y = 8, initialBlocks = level6, minMoves = 8),
        Level(name = 7,  levelSet = LevelSet.EASY, x = 6, y = 8, initialBlocks = level7, minMoves = 15),
        Level(name = 8,  levelSet = LevelSet.EASY, x = 6, y = 8, initialBlocks = level8, minMoves = 21),
        Level(name = 9,  levelSet = LevelSet.EASY, x = 6, y = 8, initialBlocks = level9, minMoves = 14),
        Level(name = 10, levelSet = LevelSet.EASY, x = 6, y = 8, initialBlocks = level10, minMoves = 28),
        Level(name = 11, levelSet = LevelSet.MEDIUM, x = 6, y = 8, initialBlocks = level11, minMoves = 23),
        Level(name = 12, levelSet = LevelSet.MEDIUM, x = 6, y = 8, initialBlocks = level12, minMoves = 3),
        Level(name = 13, levelSet = LevelSet.MEDIUM, x = 6, y = 8, initialBlocks = level13, minMoves = 3),
    )
}
