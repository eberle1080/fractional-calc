package interview

import scala.collection.mutable
import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}

import com.google.common.base.Splitter

import org.jline.reader._
import org.jline.terminal._

// Core calculator functionality. Decoupled from CLI logic for easier testing.
class FractionalCalculator {

  // Convert infix notation to postfix notation for easier evaluation
  // Based on https://en.wikipedia.org/wiki/Shunting-yard_algorithm
  def infixToPostfix(tokens: List[Token]): List[Token] = {
    require(tokens.nonEmpty)

    val postfixTokens = mutable.ListBuffer[Token]()
    val operatorsStack = mutable.Stack[Token]()

    tokens.foreach {
      case token @ RationalToken(_) => postfixTokens += token
      case token @ LParen => operatorsStack.push(token)
      case RParen => {
        while (operatorsStack.nonEmpty && operatorsStack.head != LParen) {
          postfixTokens += operatorsStack.pop()
        }
        if (operatorsStack.isEmpty) {
          throw new RuntimeException("Parenthesis mismatch")
        }
        operatorsStack.pop()
      }
      case token @ OperatorToken(_) => {
        while (operatorsStack.nonEmpty && operatorsStack.head != LParen &&
               token.precedence < operatorsStack.head.precedence) {
          postfixTokens += operatorsStack.pop()
        }
        operatorsStack.push(token)
      }
      case InvalidToken(text) =>
        throw new RuntimeException("Invalid token: " + text)
    }

    if (operatorsStack.contains(LParen)) {
      throw new RuntimeException("Parenthesis mismatch")
    }

    postfixTokens ++= operatorsStack.toList
    postfixTokens.toList
  }

  // Called by main, evaluate a user's expression
  // Takes user tokens, converts them to postfix notation, and
  // then evaluates the postfix expression using a stack
  def evaluate(tokens: List[String]): Try[RationalNumber] = Try {
    val operandsStack = mutable.Stack[RationalNumber]()
    val postfix = infixToPostfix(tokens.map(Token.apply))

    // Loop over the postfix tokens. If it sees a rational number,
    // push it on to the stack. If it sees a binary operator, pop
    // the last 2 operands and evaluate, push result on to stack.
    postfix.foreach {
      case RationalToken(r) =>
        operandsStack.push(r)
      case OperatorToken(op: BinaryOperator) =>
        if (operandsStack.length < 2) {
          throw new RuntimeException("Insufficient number of operands for operator " + op.text)
        }

        val right = operandsStack.pop()
        val left = operandsStack.pop()
        val result = op.apply(left, right)

        operandsStack.push(result)
      case token =>
        throw new RuntimeException("Invalid token: " + token.text)
    }

    if (operandsStack.isEmpty) {
      RationalNumber(0)
    } else if (operandsStack.length == 1) {
      operandsStack.pop()
    } else {
      throw new RuntimeException("Invalid expression")
    }
  }

  // For unit tests
  def apply(expr: String): RationalNumber =
    evaluate(splitTokens(expr)).get

  // Utility method to split a string by spaces in to a list of tokens
  def splitTokens(str: String): List[String] =
    Splitter.on(' ')
      .trimResults()
      .omitEmptyStrings()
      .split(str)
      .asScala
      .toList
}

// Application logic, main input loop, CLI logic, etc
object FractionalCalculatorCli extends FractionalCalculator with App {

  // If you're looking for the main method, this is it...
  // REPL logic.
  readConsoleLoop { line =>
    val tokens = splitTokens(line)
    if (tokens.isEmpty) {
      // Empty token list, just ignore
      ignoreInput
    } else if (isExitToken(tokens)) {
      // User typed "quit" or "exit"
      exitProgram
    } else {
      // Looks like an equation, let's try to calculate it
      calculate(tokens)
    }
  }

  // Evaluate 1 line of input as an equation, print result
  def calculate(tokens: List[String])(term: Terminal): Unit = {
    val writer = term.writer()

    // Try to evaluate the expression
    evaluate(tokens) match {
      case Success(result) =>
        // Computation successful, print result to screen
        writer.println(s"${result.toString} {${result.toDouble}}")
      case Failure(ex) =>
        // An exception was caught, print the exception
        ex.printStackTrace(writer)
    }
  }

  // Call readLine in a loop until the user terminates. Whenever a
  // line is read, the callback is invoked. The callback is expected
  // to return an action handler
  def readConsoleLoop(process: String => Terminal => Unit): Unit = {
    val term = TerminalBuilder.terminal()
    val reader = LineReaderBuilder.builder.terminal(term).build()

    var line: String = null
    while (true) {
      line = null
      try {
        line = reader.readLine(
          "> ",
          null,
          null.asInstanceOf[MaskingCallback],
          null)
      } catch {
        case _: UserInterruptException => ignoreInput(term)
        case _: EndOfFileException => exitProgram(term)
      }
      if (line != null) {
        process(line)(term)
      }
    }
  }

  // Utility method, check to see if the user wants to exit
  def isExitToken(tokens: List[String]): Boolean = {
    if (tokens.length == 1) {
      val tok = tokens.head
      if (tok == "exit" || tok == "quit") {
        return true
      }
    }
    false
  }

  // Invoked when the user types Ctrl+C
  def ignoreInput(term: Terminal): Unit = {
    // Ignores the user's input
  }

  // Invoked when the user types Ctrl+D or "exit" or "quit"
  def exitProgram(term: Terminal): Unit = {
    term.writer().println("goodbye")
    term.flush()
    sys.exit(0)
  }
}
