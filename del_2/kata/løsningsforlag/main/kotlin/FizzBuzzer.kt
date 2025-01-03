object FizzBuzzer {
    fun evaluate(value: Int): String {
        if (value % 3 == 0 && value % 5 == 0) return "Fizzbuzz"
        if (value % 3 == 0) return "Fizz"
        if (value % 5 == 0) return "Buzz"
        else return value.toString()
    }
}

