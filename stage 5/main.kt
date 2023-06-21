package calculator

import java.util.Scanner

class Calculator() {

    var variables = mutableMapOf<String, Int>()
    fun parseMinus(operator: String): String {
        val count = operator.count { it == '-' }
        return if (count % 2 == 1) "-" else "+"
    }

    fun calculate(operation: MutableList<Int>, operators: List<String>): Int {
        if (operation.size == operators.size) {

            operation.add(0, 0)
        }

        var result = operation[0]
        for (i in 1 until operation.size) {
            val operator = operators[i - 1]
            val value = operation[i]
            if (operator == "+") {
                result += value
            } else if (operator == "-") {
                result -= value
            }
        }
        return result
    }

 

fun main() {
    val calculator = Calculator()
    val scanner = Scanner(System.`in`)
    while (true) {
        val input = scanner.nextLine()
        val regex = Regex("^[0-9+\\-\\s]+[0-9]\$")

        if (input.startsWith("/")) {
            val command = input.substring(1)

            when (command) {
                "help" -> println("The program calculates the sum of numbers")
                "exit" -> {
                    println("Bye!")
                    return
                }

                else -> println("Unknown command")
            }
        }
        if (input == "") continue
        if (regex.matches(input)) {
            val numbers = mutableListOf<Int>()
            val operators = mutableListOf<String>()


            var currentNumber = StringBuilder()
            var currentOperator = StringBuilder()

            for (char in input) {
                when (char) {
                    '+', '-' -> {
                        if (currentNumber.isNotEmpty()) {
                            numbers.add(currentNumber.toString().toInt())
                            currentNumber.clear()
                        }
                        currentOperator.append(char)

                    }

                    in '0'..'9' -> {
                        if (currentOperator.isNotEmpty()) {
                            val operator = calculator.parseMinus(currentOperator.toString())
                            operators.add(operator)
                            currentOperator.clear()
                        }
                        currentNumber.append(char)
                    }
                }
            }
                if (currentNumber.isNotEmpty()) {
                    numbers.add(currentNumber.toString().toInt())
                }

                val sum = calculator.calculate(numbers, operators)
                println(sum)
            }

        else println("Invalid expression")
    }
}





