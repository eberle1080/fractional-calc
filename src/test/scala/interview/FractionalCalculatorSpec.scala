package interview

import org.scalatest._

class FractionalCalculatorSpec extends FlatSpec with Matchers {
  val Calc = new FractionalCalculator

  "The calculator" should "evaluate constants" in {
    Calc("1") shouldEqual RationalNumber(1)
    Calc("1/2") shouldEqual RationalNumber(1, 2)
    Calc("1_3/4") shouldEqual RationalNumber(7, 4)
    Calc("-5/6") shouldEqual RationalNumber(-5, 6)
  }

  "The calculator" should "represent fractions canonically" in {
    Calc("4/2") shouldEqual RationalNumber(2, 1)
    Calc("-6/12") shouldEqual RationalNumber(-1, 2)
    Calc("-18/6") shouldEqual RationalNumber(-3, 1)
  }

  "The calculator" should "work for the examples given in the email" in {
    Calc("1/2 * 3_3/4").toString shouldEqual "1_7/8"
    Calc("2_3/8 + 9/8").toString shouldEqual "3_1/2"
  }

  "The calculator" should "obey basic mathematical principals" in {
    Calc("0 + 1/2") shouldEqual RationalNumber(1, 2)
    Calc("0 - 1/2") shouldEqual RationalNumber(-1, 2)
    Calc("0 * 1/2") shouldEqual RationalNumber(0)
    Calc("0 / 1/2") shouldEqual RationalNumber(0)
  }
}
