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
    private val levelStars = 30

    var easyLevelProgress = mutableListOf<Int>()
    var mediumLevelProgress = mutableListOf<Int>()
    var hardLevelProgress = mutableListOf<Int>()

    init {
        prefs.getString(LevelSet.EASY.name,"").also { easyProgress ->
            if (!easyProgress.isNullOrEmpty()){
                easyLevelProgress = easyProgress.map { it.code-48 } as MutableList<Int>
                while (easyLevelProgress.size <= 10)easyLevelProgress.add(0)
            }
        }
        prefs.getString(LevelSet.MEDIUM.name,"").also { mediumProgress ->
            if (!mediumProgress.isNullOrEmpty()){
                mediumLevelProgress = mediumProgress.map { it.code-48 } as MutableList<Int>
            }
        }
        prefs.getString(LevelSet.HARD.name,"").also { hardProgress ->
            if (!hardProgress.isNullOrEmpty()){
                hardLevelProgress = hardProgress.map { it.code-48 } as MutableList<Int>
            }
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

    fun updateLevel(difficulty: LevelSet, level: Int, stars: Int){
        val currentProgress = prefs.getString(difficulty.name, "")
        if(currentProgress!!.length < level){
            prefs.edit().putString(difficulty.name, currentProgress.plus(stars))
                .apply()
            updateProgress(difficulty, level, stars)
        }else{
            if(currentProgress[level-1].code-48 < stars) {
                val newProgress =
                    StringBuilder(currentProgress).also { it.setCharAt(level, stars.toChar()) }.toString()
                prefs.edit().putString(difficulty.name, newProgress)
                    .apply()
                updateProgress(difficulty, level, stars)
            }
        }
    }

    private fun updateProgress(difficulty: LevelSet, level: Int, progress: Int) {
        when(difficulty) {
            LevelSet.EASY -> {
                if(easyLevelProgress.size <= level)easyLevelProgress.add(progress)
                else easyLevelProgress[level-1] = progress
                easyProgress = easyLevelProgress.sum()
            }
            LevelSet.MEDIUM -> {
                if(mediumLevelProgress.size <= level -10)mediumLevelProgress.add(progress)
                mediumLevelProgress[level-11] = progress
                mediumProgress = mediumLevelProgress.sum()
            }
            LevelSet.HARD -> {
                if(hardLevelProgress.size <= level -20)hardLevelProgress.add(progress)
                hardLevelProgress[level-21] = progress
                hardProgress = hardLevelProgress.sum()
            }
            LevelSet.CUSTOM -> TODO()
        }
    }

//    fun getStoredTag(): String {
//        return prefs.getString(PREF_TAG, "")!!
//    }
//    fun setStoredTag(query: String) {
//        prefs.edit().putString(PREF_TAG, query).apply()
//    }
}