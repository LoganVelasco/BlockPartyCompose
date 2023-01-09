package logan.blockpartycompose.ui.screens.playMenu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private var _flags = MutableLiveData<List<Boolean>>()
    val flags: LiveData<List<Boolean>> = _flags

    fun getProgress(): List<Int> {
        return repo.getDifficultyProgress()
    }

//    fun isTutorialMode():Boolean {
//        return getTutorialProgress() == 0
//    }

    fun getTutorialProgress(): Int {
        return state
    }

    fun updateTutorialProgress(progress: Int) {
        repo.updateTutorialStage(progress)
    }


}
