package logan.blockpartycompose.data

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import logan.blockpartycompose.data.models.Level
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepository @Inject constructor(private val gameData: GameData) {

    val levelsSets = mutableMapOf <String, MutableList<Level>>()

    fun getNewLevel(x: Int, y: Int): Level {
        return Level(
            id = 0,
            name = "",
            levelSet = LevelSet.CUSTOM,
            x = x,
            y = y,
            initialBlocks = getBlankLayout(x, y),
            minMoves = 0
        )
    }

    private fun getBlankLayout(x: Int, y: Int): List<Char> {
        return MutableList(x * y) {
            '.'
        }
    }

//    fun getLevel(name: Int): Level {
//        return levels.first { it.name == name }
//    }
//
//    fun getLevels(levelSet: LevelSet): List<Level> {
//        return levels.filter { it.levelSet == levelSet }
//    }

    fun getLevels(difficulty: LevelSet, context: Context): List<Level> {
        if(levelsSets[difficulty.name] != null) return levelsSets[difficulty.name]!!

        val levels = mutableListOf<Level>()
        var name = ""
        var id = 0
        val blocks: MutableList<Char> = mutableListOf()
        var minMoves = 0
        var x = 0
        var y = 0

        context.assets.open("Easy.txt").bufferedReader().lines().forEach { line ->
            when {
                line.startsWith("id:") -> {
                    id = line.removePrefix("id:").toInt()
                }
                line.startsWith("name:") -> {
                    name = line.removePrefix("name:")
                }
                line.startsWith("minMovies:") -> {
                    minMoves = line.removePrefix("minMovies:").toInt()
                }
                line.startsWith("x:") -> {
                    x = line.removePrefix("x:").toInt()
                }
                line.startsWith("y:") -> {
                    y = line.removePrefix("y:").toInt()
                }
                line.startsWith("|") -> {
                    blocks.addAll(line.filter { !it.isWhitespace() && it != '|' }.toCharArray().toList())
                }
                line.startsWith("-") -> {
                    levels.add(
                        Level(
                            id = id,
                            name = name,
                            levelSet = difficulty,
                            x = x,
                            y = y,
                            initialBlocks = blocks,
                            minMoves = minMoves
                        )
                    )
                }
                else -> { }
            }
        }
        levelsSets[difficulty.name] = levels
        return levels
    }


    fun getDifficultyProgress(): List<Int> {
        return listOf(
            gameData.easyLevelProgress.sum(),
            gameData.mediumLevelProgress.sum(),
            gameData.hardLevelProgress.sum()
        )
    }

    fun getLevelsProgress(difficulty: LevelSet): List<Int> {
        return when (difficulty) {
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

    fun updateLevelProgress(difficulty: LevelSet, level: Int, stars: Int) {
        gameData.updateProgress(difficulty, level, stars)
    }

//    var level1 = listOf(
//        '.', '.', '.', '.',
//        '.', '.', '.', '.',
//        '.', 'b', '.', '.',
//        '.', '.', '.', '.',
//        '.', '.', 'y', '.',
//        '.', '.', '.', '.',
//    )
//
//    var level2 = listOf(
//        'r', '.', '.', '.',
//        '.', '.', '.', '.',
//        '.', '.', '.', '.',
//        '.', 'b', '.', 'y',
//        '.', '.', '.', '.',
//        '.', '.', '.', '.',
//    )
//
//    var level3 = listOf(
//        '.', 'r', '.', '.',
//        '.', 'x', '.', '.',
//        '.', 'b', '.', '.',
//        '.', '.', '.', '.',
//        '.', '.', '.', '.',
//        '.', '.', 'y', '.',
//    )
//    var level4 = listOf(
//        '.', '.', '.', 'r', '.', 'y',
//        '.', '.', '.', '.', '.', '.',
//        '.', '.', '.', '.', '.', '.',
//        '.', 'x', 'x', '.', '.', '.',
//        '.', 'x', '.', '.', '.', '.',
//        '.', 'x', 'x', '.', '.', '.',
//        '.', '.', '.', '.', '.', '.',
//        '.', '.', '.', 'b', '.', '.',
//    )
//    var level5 = listOf(
//        '.', '.', '.', '.', '.', 'y',
//        '.', '.', '.', '.', '.', '.',
//        '.', '.', '.', 'r', '.', '.',
//        '.', '.', '.', '.', '.', '.',
//        '.', 'x', '.', 'x', '.', '.',
//        'b', '.', '.', '.', 'x', '.',
//        '.', '.', '.', 'x', '.', '.',
//        '.', '.', '.', '.', '.', '.',
//    )
//    var level6 = listOf(
//        '.', '.', '.', '.', '.', '.',
//        '.', '.', '.', '.', '.', '.',
//        '.', 'r', '.', '.', '.', '.',
//        'y', '.', '.', 'x', '.', '.',
//        '.', '.', '.', '.', 'x', '.',
//        '.', '.', 'b', '.', '.', '.',
//        '.', '.', '.', '.', '.', '.',
//        '.', '.', '.', '.', '.', '.',
//    )
//
//    var level7 = listOf(
//        '.', 'b', '.', '.', '.', '.',
//        '.', '.', '.', '.', '.', 'x',
//        '.', '.', '.', '.', 'x', '.',
//        '.', '.', '.', '.', '.', '.',
//        '.', '.', '.', '.', '.', '.',
//        '.', '.', '.', '.', '.', '.',
//        '.', 'r', '.', '.', '.', '.',
//        'y', '.', '.', '.', '.', '.',
//    )
//    var level8 = listOf(
//        '.', '.', '.', '.', 'y', 'r',
//        '.', '.', '.', '.', '.', '.',
//        '.', 'x', '.', '.', '.', '.',
//        '.', '.', '.', '.', '.', '.',
//        '.', '.', '.', '.', 'x', '.',
//        '.', '.', '.', '.', '.', 'x',
//        '.', '.', '.', '.', '.', '.',
//        'b', '.', '.', '.', '.', '.',
//    )
//
//    var level9 = listOf(
//        '.', '.', '.', '.', '.', '.',
//        '.', 'r', '.', '.', '.', 'y',
//        '.', '.', '.', '.', '.', '.',
//        '.', '.', '.', '.', '.', '.',
//        '.', '.', 'x', '.', '.', '.',
//        '.', '.', '.', '.', 'g', 'b',
//        '.', '.', '.', '.', '.', '.',
//        '.', '.', '.', '.', '.', '.',
//    )
//    var level10 = listOf(
//        '.', '.', '.', '.', '.', 'y',
//        '.', '.', 'r', '.', '.', '.',
//        '.', '.', '.', '.', '.', '.',
//        'g', 'g', 'g', 'g', 'g', 'g',
//        'g', 'g', 'g', 'g', 'g', 'g',
//        'b', 'x', '.', '.', 'g', 'g',
//        '.', 'x', 'x', 'x', 'g', '.',
//        'g', '.', '.', 'g', '.', '.',
//    )
//
//
//    var level11 = listOf(
//        '.', '.', '.', '.', '.', '.',
//        '.', '.', '.', 'b', '.', '.',
//        '.', '.', 'x', '.', '.', '.',
//        '.', '.', 'g', '.', '.', '.',
//        '.', 'g', 'x', '.', '.', '.',
//        '.', '.', '.', '.', '.', '.',
//        '.', '.', '.', '.', '.', '.',
//        '.', 'r', 'y', '.', '.', '.',
//    )
//
//    var level12 = listOf(
//        'g', 'g', '.', '.', 'g', '.',
//        'g', 'b', '.', '.', 'g', '.',
//        '.', '.', '.', '.', 'g', '.',
//        'g', '.', '.', '.', '.', '.',
//        '.', 'g', '.', '.', '.', '.',
//        '.', '.', '.', '.', '.', '.',
//        '.', '.', '.', 'x', 'x', '.',
//        '.', '.', '.', 'x', 'r', 'y',
//    )
//    var level13 = listOf(
//        'b', '.', '.', '.', '.', '.',
//        '.', '.', '.', 'g', '.', '.',
//        '.', 'g', '.', '.', 'x', '.',
//        '.', '.', 'g', '.', '.', '.',
//        '.', 'g', '.', '.', '.', '.',
//        'g', '.', '.', '.', 'g', '.',
//        '.', '.', '.', '.', 'g', '.',
//        '.', '.', '.', 'r', 'x', 'y',
//    )
//
//    val levels = listOf(
//        Level(
//            name = 1,
//            levelSet = LevelSet.EASY,
//            x = 4,
//            y = 6,
//            initialBlocks = level1,
//            minMoves = 3
//        ),
//        Level(
//            name = 2,
//            levelSet = LevelSet.EASY,
//            x = 4,
//            y = 6,
//            initialBlocks = level2,
//            minMoves = 2
//        ),
//        Level(
//            name = 3,
//            levelSet = LevelSet.EASY,
//            x = 4,
//            y = 6,
//            initialBlocks = level3,
//            minMoves = 4
//        ),
//        Level(
//            name = 4,
//            levelSet = LevelSet.EASY,
//            x = 6,
//            y = 8,
//            initialBlocks = level4,
//            minMoves = 17
//        ),
//        Level(
//            name = 5,
//            levelSet = LevelSet.EASY,
//            x = 6,
//            y = 8,
//            initialBlocks = level5,
//            minMoves = 14
//        ),
//        Level(
//            name = 6,
//            levelSet = LevelSet.EASY,
//            x = 6,
//            y = 8,
//            initialBlocks = level6,
//            minMoves = 8
//        ),
//        Level(
//            name = 7,
//            levelSet = LevelSet.EASY,
//            x = 6,
//            y = 8,
//            initialBlocks = level7,
//            minMoves = 15
//        ),
//        Level(
//            name = 8,
//            levelSet = LevelSet.EASY,
//            x = 6,
//            y = 8,
//            initialBlocks = level8,
//            minMoves = 21
//        ),
//        Level(
//            name = 9,
//            levelSet = LevelSet.EASY,
//            x = 6,
//            y = 8,
//            initialBlocks = level9,
//            minMoves = 14
//        ),
//        Level(
//            name = 10,
//            levelSet = LevelSet.EASY,
//            x = 6,
//            y = 8,
//            initialBlocks = level10,
//            minMoves = 28
//        ),
//        Level(
//            name = 11,
//            levelSet = LevelSet.MEDIUM,
//            x = 6,
//            y = 8,
//            initialBlocks = level11,
//            minMoves = 23
//        ),
//        Level(
//            name = 12,
//            levelSet = LevelSet.MEDIUM,
//            x = 6,
//            y = 8,
//            initialBlocks = level12,
//            minMoves = 3
//        ),
//        Level(
//            name = 13,
//            levelSet = LevelSet.MEDIUM,
//            x = 6,
//            y = 8,
//            initialBlocks = level13,
//            minMoves = 3
//        ),
//    )
}
