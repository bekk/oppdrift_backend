import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class BowlingCalculatorTests {
 @Test fun `getScore gutter game returns 0`() = Assertions.assertEquals(0, BowlingCalculator.getScore("--------------------"))
 @Test fun `getScore one cone in every frame returns 20`() = Assertions.assertEquals(20, BowlingCalculator.getScore("11111111111111111111"))
 @Test fun `getScore spare in first, one in rest returns 29`() = Assertions.assertEquals(29, BowlingCalculator.getScore("9/111111111111111111"))
 @Test fun `getScore spare in last, one in rest returns 29`() = Assertions.assertEquals(29, BowlingCalculator.getScore("1111111111111111111/1"))
 @Test fun `getScore strike in first, one in rest returns 30`() = Assertions.assertEquals(30, BowlingCalculator.getScore("X111111111111111111"))
 @Test fun `getScore strike in last, one in rest returns 30`() = Assertions.assertEquals(30, BowlingCalculator.getScore("111111111111111111X11"))
 @Test fun `getScore golden game returns 300`() = Assertions.assertEquals(300, BowlingCalculator.getScore("XXXXXXXXXXXX"))
 @Test fun `getScore random results returns 167`() = Assertions.assertEquals(167, BowlingCalculator.getScore("X7/9-X-88/-6XXX81"))
}

