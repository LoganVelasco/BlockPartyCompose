package logan.blockpartycompose.data.models

import kotlinx.serialization.Serializable
import logan.blockpartycompose.ui.screens.levelsMenu.GameState


@Serializable
data class Level(
    val name: String,
    val x: Int,
    val y: Int,
    val initialBlocks: List<Char>,
) {
    var blueIndex: Int = initialBlocks.indexOf('b')
    var redIndex: Int = initialBlocks.indexOf('r')
    var state: GameState = GameState.IN_PROGRESS
    var blocks = initialBlocks.toMutableList()

    fun resetLevel(){
        blueIndex = initialBlocks.indexOf('b')
        redIndex = initialBlocks.indexOf('r')
        blocks = initialBlocks.toMutableList()
        state = GameState.IN_PROGRESS
    }

}

@Serializable
data class Block(
    var color: BlockColor,
    val index: Int,
)

enum class BlockColor(val color: Char) {
    BLUE('b'),
    RED('r'),
    YELLOW('y'),
    GREEN('g'),
    BLACK('x'),
    GRAY('.'),
}