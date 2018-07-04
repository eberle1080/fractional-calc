package interview

sealed trait Operator {
  val precedence: Int
  def text: String
}
object Operator {
  def apply(str: String): Option[Operator] =
    BinaryOperator(str)
}
sealed trait BinaryOperator extends Operator {
  def apply(lhs: RationalNumber, rhs: RationalNumber): RationalNumber
}
// Abstracts all binary operators
object BinaryOperator {
  // Given a token, attempt to convert it in to an operator
  def apply(str: String): Option[BinaryOperator] =
    str match {
      case "+" => Some(AddOperator)
      case "-" => Some(SubtractOperator)
      case "*" => Some(MultiplyOperator)
      case "/" => Some(DivideOperator)
      case _ => None
    }
}
// Apply an add operation
case object AddOperator extends BinaryOperator {
  override val precedence: Int = Precedence.Add
  override def text: String = "+"
  override def apply(lhs: RationalNumber, rhs: RationalNumber): RationalNumber =
    lhs + rhs
}
// Apply a subtract operation
case object SubtractOperator extends BinaryOperator {
  override val precedence: Int = Precedence.Subtract
  override def text: String = "-"
  override def apply(lhs: RationalNumber, rhs: RationalNumber): RationalNumber =
    lhs - rhs
}
// Apply a multiply operation
case object MultiplyOperator extends BinaryOperator {
  override val precedence: Int = Precedence.Multiply
  override def text: String = "*"
  override def apply(lhs: RationalNumber, rhs: RationalNumber): RationalNumber =
    lhs * rhs
}
// Apply a divide operation
case object DivideOperator extends BinaryOperator {
  override val precedence: Int = Precedence.Divide
  override def text: String = "/"
  override def apply(lhs: RationalNumber, rhs: RationalNumber): RationalNumber =
    lhs / rhs
}
