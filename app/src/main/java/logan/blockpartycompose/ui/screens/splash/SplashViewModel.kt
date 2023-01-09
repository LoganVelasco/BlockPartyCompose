package logan.blockpartycompose.ui.screens.splash

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import logan.blockpartycompose.data.DataRepository
import javax.inject.Inject
import kotlin.math.absoluteValue

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repo: DataRepository
) : ViewModel() {

    fun isTutorialMode(): Boolean {
        return getTutorialProgress() == 0
    }

    private fun getTutorialProgress(): Int {
        return repo.getTutorialStage()
    }

}