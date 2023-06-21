package calculator

import java.util.Scanner
import kotlin.system.exitProcess

class Calculator2() {

    private var variables = mutableMapOf<String, Int>()

    fun parseMinus(operator: String): String {
        val count = operator.count { it == '-' }
        return if (count % 2 == 1) "-" else "+"
    }

    private fun calculate(operation: MutableList<Int>, operators: List<String>): Int {
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
    fun handleAssignment(input: String) {
        val (variableName, valueString) = input.split("=").map { it.trim() }

        if (!valueString.matches("\\d+".toRegex()) && !variables.containsKey(valueString)) {
            println("Invalid assignment")
            return
        }


        if (isValidIdentifier(variableName)) {
            val value = valueString.toIntOrNull() ?: variables[valueString]

            if (value != null) {
                variables[variableName] = value

            } else {
                println("Unknown variable") // Unknown variable used in assignment
            }
        }
        else {
            println("Invalid identifier") // Invalid variable name
        }

    }


    fun evaluateExpression(input: String) {
        val expression = input.replace("\\s+".toRegex(), " ")
        val tokens = expression.split(" ")
        val operators = mutableListOf<String>()
        var isUnknownVariable = false

        tokens.forEach { if (variables[it] == null && !it.matches("\\d+|[+-]+".toRegex())) isUnknownVariable = true  }
        if (isUnknownVariable) {
            println("Unknown variable")
            return
        } else {
            val values = tokens.map { token ->
                if (token.matches("[+-]+".toRegex())) {
                    operators.add(parseMinus(token))
                    0 // Place a temporary placeholder for the operator
                } else if (token.matches("\\d+".toRegex())) {
                    token.toInt()
                } else {
                    variables[token] ?: 0 // Use 0 as the default value if variable is not found
                }
            }

            // Remove the temporary placeholders (0) from the values list
            if (values.size == 1) println(values[0])
            else {
                val valuesWithoutPlaceholders = values.filter { it != 0 }

                if (operators.size + 1 == valuesWithoutPlaceholders.size) {
                    val result = calculate(valuesWithoutPlaceholders.toMutableList(), operators)
                    println(result)
                } else {
                    println("Invalid expression")
                }
            }
        }
    }

    private fun isValidIdentifier(identifier: String): Boolean {
        return identifier.matches("[a-zA-Z]+".toRegex())
    }

}


fun handleCommand(input: String) {
    when (input) {
        "/help" -> println("Help message: ...") // Print the help message
        "/exit" -> {
            println("Bye!")
            exitProcess(0) // Terminate the program
        }
        else -> println("Unknown command") // Invalid command
    }
}



fun main() {
    val calculator = Calculator2()
    val scanner = Scanner(System.`in`)
    while (true) {
        val input = scanner.nextLine()

        when{
            input.startsWith("/") -> handleCommand(input)
            input.contains("=") -> calculator.handleAssignment(input)
            input.isNotEmpty() -> calculator.evaluateExpression(input)
            input.isEmpty() -> continue
        }
    }
}





