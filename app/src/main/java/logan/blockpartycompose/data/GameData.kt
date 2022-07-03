package logan.blockpartycompose.data

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameData @Inject constructor(@ApplicationContext context : Context){

    private val prefs: SharedPreferences = context.getSharedPreferences("levels", Context.MODE_PRIVATE)

    var easyProgress = 0
    var mediumProgress = 0
    var hardProgress = 0

    var easyLevelProgress = mutableListOf<Int>()
    var mediumLevelProgress = mutableListOf<Int>()
    var hardLevelProgress = mutableListOf<Int>()

    init {
        prefs.getString(LevelSet.EASY.name,"").also { easyProgress ->
            if (!easyProgress.isNullOrEmpty()){
                easyLevelProgress = easyProgress.map { it.code-48 } as MutableList<Int>
            }
            while (easyLevelProgress.size <= 10)easyLevelProgress.add(0)
        }
        prefs.getString(LevelSet.MEDIUM.name,"").also { mediumProgress ->
            if (!mediumProgress.isNullOrEmpty()){
                mediumLevelProgress = mediumProgress.map { it.code-48 } as MutableList<Int>
            }
            while (mediumLevelProgress.size <= 10)mediumLevelProgress.add(0)
        }
        prefs.getString(LevelSet.HARD.name,"").also { hardProgress ->
            if (!hardProgress.isNullOrEmpty()){
                hardLevelProgress = hardProgress.map { it.code-48 } as MutableList<Int>
            }
            while (hardLevelProgress.size <= 10)hardLevelProgress.add(0)
        }
        if (easyLevelProgress.isNotEmpty()){
            easyProgress = easyLevelProgress.sum()
        }
        if (mediumLevelProgress.isNotEmpty()){
            mediumProgress = mediumLevelProgress.sum()
        }
        if (hardLevelProgress.isNotEmpty()){
            hardProgress = hardLevelProgress.sum()
        }
    }

    private fun updateLevel(difficulty: LevelSet, currentProgress: String, level: Int, stars: Int){
        if(currentProgress[level-1].code-48 < stars) {
                val newProgress =
                    StringBuilder(currentProgress).also { it.setCharAt(level-1, Character.forDigit(stars, 10) ) }.toString()
                prefs.edit().putString(difficulty.name, newProgress)
                    .apply()
            }

    }

    fun updateProgress(difficulty: LevelSet, level: Int, stars: Int) {
        var currentProgress = prefs.getString(difficulty.name, "")
        if(currentProgress.isNullOrEmpty()){ // TODO update to scale
            currentProgress = "0000000000"
        }
        when(difficulty) {
            LevelSet.EASY -> {
                updateLevel(difficulty, currentProgress, level, stars)
                if(easyLevelProgress.size <= level)easyLevelProgress.add(stars)
                else easyLevelProgress[level-1] = stars
                easyProgress = easyLevelProgress.sum()
            }
            LevelSet.MEDIUM -> {
                updateLevel(difficulty, currentProgress, level-10, stars)
                if(mediumLevelProgress.size <= level -10)mediumLevelProgress.add(stars)
                mediumLevelProgress[level-11] = stars
                mediumProgress = mediumLevelProgress.sum()
            }
            LevelSet.HARD -> {
                updateLevel(difficulty, currentProgress, level-20, stars)
                if(hardLevelProgress.size <= level -20)hardLevelProgress.add(stars)
                hardLevelProgress[level-21] = stars
                hardProgress = hardLevelProgress.sum()
            }
            LevelSet.CUSTOM -> TODO()
        }
    }
}