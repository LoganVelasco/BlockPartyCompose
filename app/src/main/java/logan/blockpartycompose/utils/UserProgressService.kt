package logan.blockpartycompose.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

class UserProgressService @Inject constructor(context: Context) {

    private val sharedPrefs: SharedPreferences = context.getSharedPreferences("Progress", Context.MODE_PRIVATE)

    fun isHighScoreUpdated(level: String, score:Int): Boolean {
        val highScore = getScore(level)

        if(highScore == 0 || highScore > score){
            updateScore(level, score)
            return true
        }
        return false
    }

    fun getScore(level: String):Int{
        println("TESTLOGgetScore: ${sharedPrefs.getInt("Score:$level", 0)}")

        return sharedPrefs.getInt("Score:$level", 0)
    }

    fun updateScore(level: String, newScore: Int){
        sharedPrefs.edit{
            putInt("Score:$level", newScore)
            apply()
        }
    }
}