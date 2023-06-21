package calculator

import java.util.*
import kotlin.math.pow
import kotlin.system.exitProcess

class Calculator {

    private var variables = mutableMapOf<String, Int>()
    private fun parseMinus(operator: String): String {
        val count = operator.count { it == '-' }
        return if (count % 2 == 1) "-" else "+"
    }

    private fun calculate(expression: String): Int {
        val stack = Stack<Int>()

        for (token in expression.split(" ")) {
            when {
                token.matches("\\d+".toRegex()) -> {
                    stack.push(token.toInt())
                }
                variables.containsKey(token) -> {
                    stack.push(variables[token]!!)
                }
                token.matches("[+\\-*/^]".toRegex()) -> {

                    val rightOperand = stack.pop()
                    val leftOperand = stack.pop()
                    val result = when (token) {
                        "+" -> leftOperand + rightOperand
                        "-" -> leftOperand - rightOperand
                        "*" -> leftOperand * rightOperand
                        "/" -> leftOperand / rightOperand
                        "^" -> leftOperand.toDouble().pow(rightOperand).toInt()
                        else -> throw IllegalArgumentException("Invalid operator: $token")
                    }
                    stack.push(result)
                }
            }
        }
        if (stack.isEmpty()) {
            println("Invalid expression")
            return 0
        }
        return stack.pop()
    }

    private fun infixToPostfix(expression: String): String {
        val output = StringBuilder()
        val stack = Stack<String>()
        val precedence = mapOf("^" to 3, "*" to 2, "/" to 2, "+" to 1, "-" to 1)
        val tokens = expression.split("(?=[+\\-*/^()])|(?<=[+\\-*/^()])".toRegex())
            .filter { it.isNotBlank() }
        for (token in tokens) {
            val trimmedToken = token.trim()
            if (trimmedToken.matches("\\d+".toRegex()) || variables.containsKey(trimmedToken)) {
                output.append("$token ")
            } else if (trimmedToken.matches("[+\\-*/^]".toRegex())) {
                while (!stack.isEmpty() && stack.peek() != "(" &&
                    precedence.getOrDefault(token, 0) <= precedence.getOrDefault(stack.peek(), 0)
                ) {
                    output.append("${stack.pop()} ")
                }
                stack.push(token)
            } else if (token == "(") {
                stack.push(token)

            } else if (token == ")") {
                while (!stack.isEmpty() && stack.peek() != "(") {
                    output.append("${stack.pop()} ")

                }
                stack.pop() // Discard the opening parenthesis
            }

        }
        while (!stack.isEmpty()) {
            output.append("${stack.pop()} ")
        }
        return output.toString().trim()
    }


    fun evaluateExpression(input: String) {
        val openingParenthesesCount = input.count { it == '(' }
        val closingParenthesesCount = input.count { it == ')' }

        if (openingParenthesesCount != closingParenthesesCount) {
            println("Invalid expression")
            return
        }
        var input = input.replace("\\s+".toRegex(), " ")
        var expression = StringBuilder()
        val tokens = input.split(" ")
        if (tokens.size == 1 && !variables.containsKey(tokens[0])) {
            println("Unknown variable")
            return
        }
        tokens.forEach { token ->
            if (token.matches("[+-]+".toRegex())) {
                val operator = parseMinus(token)
                expression.append(operator)
            }

            else if (token.matches("[/*]+".toRegex())) {
                val operators = token.split("").filter { it.isNotEmpty() }
                if (operators.size > 1) println("Invalid expression") else expression.append("$token ")

            } else expression.append("$token ")
        }
        expression.trim()
        val postfixExpression = infixToPostfix(expression.toString())
        val result = calculate(postfixExpression)
        println(result)
    }


    fun handleAssignment(input: String) {


        val (variableName, valueString) = input.split("=").map { it.trim() }

        if (!valueString.matches("-?\\d+".toRegex()) && !variables.containsKey(valueString)) {
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
        } else {
            println("Invalid identifier") // Invalid variable name
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
    val calculator = Calculator()
    val scanner = Scanner(System.`in`)
    while (true) {
        val input = scanner.nextLine()

        when {
            input.startsWith("/") -> handleCommand(input)
            input.contains("=") -> calculator.handleAssignment(input)
            input.isNotEmpty() -> calculator.evaluateExpression(input)
            input.isEmpty() -> continue
        }
    }
}





