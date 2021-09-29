package logan.blockpartycompose.utils

class GameUtils {

    companion object {
        fun isTouchingBlue(index: Int, blueIndex: Int, x: Int): Boolean {
            if (isEdge(blueIndex, x))
                return isValidEdgeMove(index, blueIndex, x)

            if (blueIndex + 1 == index || blueIndex - 1 == index || blueIndex + x == index || blueIndex - x == index)
                return true

            return false
        }

        fun isEdge(index: Int, x: Int): Boolean {
            return index % x == 0 || index % x == x - 1
        }

        private fun isValidEdgeMove(index: Int, blueIndex: Int, x: Int): Boolean {
            return when {
                blueIndex % x == 0 -> {
                    blueIndex + 1 == index || blueIndex + x == index || blueIndex - x == index
                }
                blueIndex % x == x - 1 -> {
                    blueIndex - 1 == index || blueIndex + x == index || blueIndex - x == index
                }
                else -> false
            }
        }
    }
}