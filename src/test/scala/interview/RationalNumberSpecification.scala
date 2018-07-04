package interview

import org.scalacheck.{Arbitrary, Gen, Properties}
import org.scalacheck.Prop.forAll

// These tests prove that RationalNumber does proper arithmetic
object RationalNumberSpecification extends Properties("RationalNumber") {

  private val anyInteger = Arbitrary.arbitrary[Int]
  private implicit lazy val arbNumber: Arbitrary[RationalNumber] =
    Arbitrary(anyInteger.flatMap(n => anyInteger.map(d =>
      RationalNumber(n % 10000, (d.abs % 10000) + 1))))

  property("addition") = forAll { (a: RationalNumber, b: RationalNumber) =>
    val result = a + b
    val actualResult = result.toBigDecimal
    val expectedResult = a.toBigDecimal + b.toBigDecimal
    (actualResult - expectedResult).abs < 0.001
  }

  property("subtraction") = forAll { (a: RationalNumber, b: RationalNumber) =>
    val result = a - b
    val actualResult = result.toBigDecimal
    val expectedResult = a.toBigDecimal - b.toBigDecimal
    (actualResult - expectedResult).abs < 0.001
  }

  property("multiplication") = forAll { (a: RationalNumber, b: RationalNumber) =>
    val result = a * b
    val actualResult = result.toBigDecimal
    val expectedResult = a.toBigDecimal * b.toBigDecimal
    (actualResult - expectedResult).abs < 0.001
  }

  property("division") = forAll { (a: RationalNumber, b: RationalNumber) =>
    if (!b.isZero) {
      val result = a / b
      val actualResult = result.toBigDecimal
      val expectedResult = a.toBigDecimal / b.toBigDecimal
      (actualResult - expectedResult).abs < 0.001
    } else {
      // Ignore division by zero
      true
    }
  }
}
