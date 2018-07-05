# Fractional calculator

This project takes in arbitrary rational numbers (and basic operators) and evaluates them. The result is given as a rational number.

## Preparing

Ensure that you have Java JDK 8 (or later) on your build path. One that's done, just clone the project

> git clone https://github.com/eberle1080/fractional-calc.git<br>
> cd fractional-calc

## Building

> ./sbt compile

## Testing

> ./sbt test

## Running

> ./sbt run

## Usage

Type an equation and press enter. The solution will print on the next line (along with a floating-point evaluation).

 > \> 1/2 * 3_3/4 [ENTER]<br>
 1_7/8 {1.875}

To quit, type "exit", or "quit", or press Ctrl+D

 >\> exit<br>
 goodbye

