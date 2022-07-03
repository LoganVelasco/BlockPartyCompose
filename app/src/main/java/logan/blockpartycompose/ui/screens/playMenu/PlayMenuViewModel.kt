package logan.blockpartycompose.ui.screens.playMenu

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import logan.blockpartycompose.data.DataRepository
import javax.inject.Inject

@HiltViewModel
class PlayMenuViewModel @Inject constructor(
    private val repo: DataRepository
) : ViewModel() {

    fun getProgress(): List<Int> {
        return repo.getDifficultyProgress()
    }
}