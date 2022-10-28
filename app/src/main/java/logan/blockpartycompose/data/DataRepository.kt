package logan.blockpartycompose.data

import android.content.Context
import com.google.gson.Gson
import logan.blockpartycompose.data.models.Level
import logan.blockpartycompose.data.models.LevelsDTO
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet
import java.io.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepository @Inject constructor(private val gameData: GameData) {

    private val gson = Gson()
    val levelsSets = mutableMapOf<String, List<Level>>()
    private val customFileName = "custom.json"

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
    fun getLevel(levelSet: LevelSet, id: Int): Level {
        return levelsSets[levelSet.name]!!.first { it.id == id }
    }

    fun getLevels(difficulty: LevelSet, context: Context): List<Level> {
        if (levelsSets[difficulty.name] != null) return levelsSets[difficulty.name]!!
        val json =
            context.assets.open("${difficulty.name.lowercase()}.json").bufferedReader().readText()
        val levels = gson.fromJson(json, LevelsDTO::class.java).convertToLevels()
        levelsSets[difficulty.name] = levels

        return levels
    }

    fun getCustomLevels(context: Context): List<Level> {
//        if (levelsSets[LevelSet.CUSTOM.name] != null) return levelsSets[LevelSet.CUSTOM.name]!!
        var fileInputStream: FileInputStream? = null
        return try {
            fileInputStream = context.openFileInput(customFileName)
            val inputStreamReader = InputStreamReader(fileInputStream)
            val json = BufferedReader(inputStreamReader).readText()
            if (json.isEmpty()) return emptyList()
            val levels = gson.fromJson(json, LevelsDTO::class.java).convertToLevels() 
            levelsSets[LevelSet.CUSTOM.name] = levels
            levels
        } catch (e: FileNotFoundException) {
            File("${context.filesDir.path}/$customFileName").createNewFile()
            emptyList()
        }
    }

    fun addCustomLevel(level: Level, context: Context): Boolean {
        val levels = getCustomLevels(context)
        val newLevels = mutableListOf<Level>()
        newLevels.addAll(levels)
        level.id = (newLevels.size) *-1
        newLevels.add(level)
        val data = gson.toJson(LevelsDTO.getDTO(newLevels))
        val fileOutputStream: FileOutputStream
        return try {
            fileOutputStream = context.openFileOutput(customFileName, Context.MODE_PRIVATE)
            fileOutputStream.write(data.toByteArray())
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun deleteCustomLevel(id: Int, context: Context) {
        val newLevels = getCustomLevels(context).filter { it.id != id }
        val data = gson.toJson(LevelsDTO.getDTO(newLevels))
        val fileOutputStream: FileOutputStream
        try {
            fileOutputStream = context.openFileOutput(customFileName, Context.MODE_PRIVATE)
            fileOutputStream.write(data.toByteArray())
            levelsSets[LevelSet.CUSTOM.name] = newLevels
        } catch (e: Exception) {
            e.printStackTrace()
        }
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
        val currentStars = getDifficultyProgress().sum()
        gameData.updateProgress(difficulty, level, stars)
        val newStars = getDifficultyProgress().sum()

    }

    fun clearAllProgress(){
        gameData.clearAllProgress()
    }

}
