# Fizz Buzz

https://en.wikipedia.org/wiki/Fizz_buzz

```Kotlin
class FizzbuzzTest {
    @Test
    fun `should receive number and return number`() {
        val fizzbuzz = Fizzbuzz()
        val result = fizzbuzz.exec(1)
        assertThat(result).isEqualTo("1")
    }

    @Test
    fun `should return Fizz if number is multiple of three`() {
        val fizzbuzz = Fizzbuzz()
        val result = fizzbuzz.exec(3)
        assertThat(result).isEqualTo("Fizz")
    }

    @Test
    fun `should return Buzz if number is multiple of five`() {
        val fizzbuzz = Fizzbuzz()
        val result = fizzbuzz.exec(5)
        assertThat(result).isEqualTo("Buzz")
    }

    @ParameterizedTest
    @CsvSource(
        "15",
        "30"
    )
    fun `should return Fizzbuzz if number is multiple of three and five`(number: Int) {
        val fizzbuzz = Fizzbuzz()
        val result = fizzbuzz.exec(number)
        assertThat(result).isEqualTo("Fizzbuzz")
    }
}
//https://github.com/MayuRuru/TDD-practise-kotlin/blob/main/src/test/kotlin/fizzbuzzTest.kt
```