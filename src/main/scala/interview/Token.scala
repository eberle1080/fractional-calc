package interview

// Various types of parsed tokens
sealed trait Token {
  val precedence: Int
  def text: String
}
// A rational number
case class RationalToken(rational: RationalNumber) extends Token {
  override val precedence: Int = Precedence.Rational
  override def text: String = rational.toString
}
// An operator (+,-,*,/)
case class OperatorToken(op: Operator) extends Token {
  override val precedence: Int = op.precedence
  override def text: String = op.text
}
// A paren (left or right)
sealed trait Paren extends Token {
  override val precedence: Int = Precedence.Paren
}
// A left paren
case object LParen extends Paren {
  override def text: String = "("
}
// A right paren
case object RParen extends Paren {
  override def text: String = ")"
}
// Something not recognized as valid
case class InvalidToken(str: String) extends Token {
  override val precedence: Int = -1
  override def text: String = str
}
// Helper class for parsing
object Token {
  def apply(str: String): Token =
    if (str == "(") {
      LParen
    } else if (str == ")") {
      RParen
    } else {
      Operator(str) match {
        case Some(op) => OperatorToken(op)
        case None =>
          RationalNumber(str) match {
            case Some(num) => RationalToken(num)
            case None => InvalidToken(str)
          }
      }
    }
}
