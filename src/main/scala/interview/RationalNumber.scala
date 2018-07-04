package interview

import scala.annotation.tailrec

// Represents a numerator + a denominator. Can do basic operations
// on instances of this class like +,-,*,/
class RationalNumber(numerator: Int, denominator: Int) {
  require(denominator != 0)
  def this(n: Int) = this(n, 1)

  // Calculate the *actual* numerator and denominator
  val (n: Int, d: Int) =
    RationalNumber.init(numerator, denominator)

  // Add two rational numbers
  def + (that: RationalNumber): RationalNumber =
    new RationalNumber((n * that.d) + (d * that.n), d * that.d)

  // Subtract two rational numbers
  def - (that: RationalNumber): RationalNumber =
    new RationalNumber(n * that.d - d * that.n, d * that.d)

  // Multiply two rational numbers
  def * (that: RationalNumber): RationalNumber =
    new RationalNumber(n * that.n, d * that.d)

  // Divide two rational numbers
  def / (that: RationalNumber): RationalNumber =
    new RationalNumber(n * that.d, d * that.n)

  // Discard the sign
  def abs: RationalNumber =
    new RationalNumber(n.abs, d.abs)

  // Is this a zero value? For unit testing
  def isZero: Boolean =
    n == 0 && d == 1

  // Convert to a human-friendly string
  override def toString: String = {
    if (d == 1) {
      n.toString
    } else if (n > d) {
      val whole = new RationalNumber(n / d)
      val frac = abs - whole
      whole.toString + "_" + frac.toString
    } else {
      n.toString + "/" + d.toString
    }
  }

  // Evaluate the rational number as a Double
  def toDouble: Double =
    n.toDouble / d.toDouble

  // Evaluate the rational number as a BigDecimal
  def toBigDecimal: BigDecimal =
    BigDecimal(n) / BigDecimal(d)

  // Determine equality to another object
  override def equals(obj: scala.Any): Boolean = {
    if (!obj.isInstanceOf[RationalNumber]) {
      false
    } else {
      val that = obj.asInstanceOf[RationalNumber]
      n == that.n && d == that.d
    }
  }
}

object RationalNumber {
  // A whole number (no fraction part)
  private val Whole = "^([-]?[0-9]+)$".r
  // A fraction number (no whole part)
  private val Fraction = "^([-]?[0-9]+)/([0-9]+)$".r
  // A mixed number (both whole and fraction present)
  private val Mixed = "^([-]?[0-9]+)_([0-9]+)/([0-9]+)$".r

  // Factory method, given a whole number convert it to a rational number
  def apply(i: Int): RationalNumber =
    new RationalNumber(i)

  // Factory method, given a numerator and denominator, create a RationalNumber
  def apply(n: Int, d: Int): RationalNumber =
    new RationalNumber(n, d)

  // Take a string and try to parse it as a rational number
  // Uses simple regular expressions for parsing
  def apply(str: String): Option[RationalNumber] =
    str match {
      case Mixed(w, n, d) => {
        val whole = new RationalNumber(w.toInt)
        val frac = new RationalNumber(n.toInt, d.toInt)
        if (w.toInt >= 0) Some(whole + frac)
        else Some(whole - frac)
      }
      case Fraction(n, d) =>
        Some(new RationalNumber(n.toInt, d.toInt))
      case Whole(n) =>
        Some(new RationalNumber(n.toInt))
      case _ =>
        None
    }

  // Find the greatest common divisor (recursively)
  @tailrec
  private def calcGcd(a: Int, b: Int): Int =
    if (b == 0) a else calcGcd(b, a % b)

  // Takes a numerator and denominator and reduces them to their
  // canonical format.
  private def init(numerator: Int, denominator: Int) = {
    val gcd = calcGcd(
      numerator.abs,
      denominator.abs)

    var sign = 1
    val tn = numerator.abs / gcd
    val td = denominator.abs / gcd

    if (numerator < 0) {
      sign = -sign
    }
    if (denominator < 0) {
      sign = -sign
    }

    (sign * tn.abs, td.abs)
  }
}
