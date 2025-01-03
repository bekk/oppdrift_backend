object RomanNumeralCalculator {
    fun render(arabic: Int): String = when(arabic){
        0 -> ""
        1 -> "I"
        in 2..3 -> render(1).repeat(arabic)
        4 -> combine(1, 5)
        5 -> "V"
        in 6..8 -> combine(5, arabic-5)
        9 -> combine(1, 10)
        10 -> "X"
        in 11..49 -> renderRange(arabic, 10, 40, 50)
        50 -> "L"
        in 51..99 -> renderRange(arabic, 50, 90, 100)
        100 -> "C"
        in 101..499 -> renderRange(arabic, 100, 400, 500)
        500 -> "D"
        in 501..999 -> renderRange(arabic, 500, 900, 1000)
        1000 -> "M"
        else -> combine(1000, arabic - 1000)
    }
    private fun combine(left:Int, right:Int) = render(left) + render(right)
    private fun renderRange(arabic:Int, lowerBound:Int, subtractiveNumber: Int, upperBound:Int) = when(arabic){
        in lowerBound + 1..<subtractiveNumber -> combine(lowerBound, arabic - lowerBound)
        subtractiveNumber -> combine(lowerBound, upperBound)
        in subtractiveNumber+1..<upperBound -> combine(subtractiveNumber, arabic - subtractiveNumber)
        else -> throw Error("Illegal arguments: $arabic $lowerBound – $upperBound")
    }
}

