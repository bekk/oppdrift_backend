import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FizzbuzzerTests {
 @Test
 fun `1 should receive number and return number`() {
  val result = FizzBuzzer.evaluate(1)
  assertEquals("1", result)
 }

 @Test
 fun `2 should return Fizz if number is multiple of three`() {
  val result =  FizzBuzzer.evaluate(3)
  assertEquals("Fizz", result)
 }

 @Test
 fun `3 should return Buzz if number is multiple of five`() {
  val result =  FizzBuzzer.evaluate(5)
  assertEquals("Buzz", result)
 }

 @Test
 fun `15 should return Fizzbuzz if number is multiple of three and five`() {
  val result =  FizzBuzzer.evaluate(15)
  assertEquals("Fizzbuzz", result)
 }

 @Test
 fun `30 should return Fizzbuzz if number is multiple of three and five`() {
  val result =  FizzBuzzer.evaluate(30)
  assertEquals("Fizzbuzz", result)
 }
}