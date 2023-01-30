package logan.blockpartycompose.ui.screens.playMenu

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import logan.blockpartycompose.data.DataRepository
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class PlayMenuViewModel @Inject constructor(
    private val repo: DataRepository
) : ViewModel() {
    var state by Delegates.notNull<Int>()

    init {
        state = repo.getTutorialStage()
    }

    fun getProgress(): List<Int> {
        return repo.getDifficultyProgress()
    }

    fun isHintsEnabled(): Boolean {
        return repo.getHintPreference()
    }
}
