import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RomanNumeralCalculatorTests {

@Test fun `render 0 returns empty string`() = assertEquals("", RomanNumeralCalculator.render(0))
@Test fun `render 1 returns I`() = assertEquals("I", RomanNumeralCalculator.render(1))
@Test fun `render 2 returns II`() = assertEquals("II", RomanNumeralCalculator.render(2))
@Test fun `render 4 returns IV`() = assertEquals("IV", RomanNumeralCalculator.render(4))
@Test fun `render 5 returns V`() = assertEquals("V", RomanNumeralCalculator.render(5))
@Test fun `render 6 returns VI`() = assertEquals("VI", RomanNumeralCalculator.render(6))
@Test fun `render 10 returns X`() = assertEquals("X", RomanNumeralCalculator.render(10))
@Test fun `render 9 returns IX`() = assertEquals("IX", RomanNumeralCalculator.render(9))
@Test fun `render 11 returns XI`() = assertEquals("XI", RomanNumeralCalculator.render(11))
@Test fun `render 500 returns D`() = assertEquals("D", RomanNumeralCalculator.render(500))
@Test fun `render 100 returns C`() = assertEquals("C", RomanNumeralCalculator.render(100))
@Test fun `render 2019 returns MMXIX`() = assertEquals("MMXIX", RomanNumeralCalculator.render(2019))
@Test fun `render 3000 returns MMM`() = assertEquals("MMM", RomanNumeralCalculator.render(3000))
@Test fun `render 65 returns LXV`() = assertEquals("LXV", RomanNumeralCalculator.render(65))
@Test fun `render 1379 returns MCCCLXXIX`() = assertEquals("MCCCLXXIX", RomanNumeralCalculator.render(1379))
}

