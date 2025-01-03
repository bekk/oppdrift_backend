object BowlingCalculator {
    fun getScore(frames:String):Int = getFrameScores(frames.map { parseFrame(it) }).sum()

    private fun getFrameScores(results: List<RollResult>) = sequence  {
        var index = 0
        var frameCount = 0
        while (index < results.size && ++frameCount <= 10) {
            if (results[index] is RollResult.Strike) {
                yield(coneScore(results, index))
                index += 1
            } else {
                yield(coneScore(results, index) + coneScore(results, index + 1))
                index += 2
            }
        }
    }

    private fun parseFrame(frame:Char) = when(frame) {
        '-' -> RollResult.Gutter
        in '0'..'9' -> RollResult.Cones(frame.code - '0'.code)
        '/' -> RollResult.Spare
        'X' -> RollResult.Strike
        else -> throw Error("Invalid frame $frame")
    }


    private fun coneScore(results: List<RollResult>, index: Int): Int {
        if(index < 0 || index >= results.size) return 0
        val roll = results[index]
        return when(roll) {
            is RollResult.Strike -> 10 + coneCount(results, index+1) + coneCount(results, index + 2)
            is RollResult.Spare -> coneCount(results, index) + coneCount(results, index + 1)
            else -> coneCount(results, index)
        }
    }

    private fun coneCount(results: List<RollResult>, index: Int): Int {
        if(index < 0 || index >= results.size) return 0
        val roll = results[index]
        return when(roll) {
            is RollResult.Strike -> 10
            is RollResult.Gutter -> 0
            is RollResult.Cones -> roll.count
            is RollResult.Spare -> 10 - coneCount(results, index - 1)
            else -> throw Error("Invalid roll $roll")
        }
    }
}

sealed class RollResult {
    data class Cones(val count: Int): RollResult()
    object Gutter: RollResult()
    object Spare: RollResult()
    object Strike: RollResult()
}

