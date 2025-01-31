package com.example.pemdas_calculator;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Stack;

public class ButtonController {

    @FXML
    private Button clear;

    @FXML
    private Button cosine;

    @FXML
    private Button decimal;

    @FXML
    private Button divides;

    @FXML
    private Button eight;

    @FXML
    private Button equals;

    @FXML
    private Button exponent;

    @FXML
    private Button five;

    @FXML
    private Button four;

    @FXML
    private Button leftCurrlyBracket;

    @FXML
    private Button leftParentheses;

    @FXML
    private Button ln;

    @FXML
    private Button log;

    @FXML
    private Button minus;

    @FXML
    private Button negativeToggle;

    @FXML
    private Button nine;

    @FXML
    private Button one;

    @FXML
    private TextField output;

    @FXML
    private Button plus;

    @FXML
    private Button rightCurrlyBracket;

    @FXML
    private Button rightParentheses;

    @FXML
    private Button seven;

    @FXML
    private Button sine;

    @FXML
    private Button six;

    @FXML
    private Button tangent;

    @FXML
    private Button three;

    @FXML
    private Button times;

    @FXML
    private Button two;

    @FXML
    private Button undo;

    @FXML
    private Button zero;

    //utilized chatgpt for syntax as I don't know this type of programming
    //initilize all buttons
    public void initialize() {
        //handles the equals function
        equals.setOnAction(event -> handleEquals());
        //handles numbers
        zero.setOnAction(event -> handleButtonClick("0"));
        one.setOnAction(event -> handleButtonClick("1"));
        two.setOnAction(event -> handleButtonClick("2"));
        three.setOnAction(event -> handleButtonClick("3"));
        four.setOnAction(event -> handleButtonClick("4"));
        five.setOnAction(event -> handleButtonClick("5"));
        six.setOnAction(event -> handleButtonClick("6"));
        seven.setOnAction(event -> handleButtonClick("7"));
        eight.setOnAction(event -> handleButtonClick("8"));
        nine.setOnAction(event -> handleButtonClick("9"));

        //handles operators
        plus.setOnAction(event -> handleButtonClick("+"));
        minus.setOnAction(event -> handleButtonClick("-"));
        times.setOnAction(event -> handleButtonClick("*"));
        divides.setOnAction(event -> handleButtonClick("/"));
        negativeToggle.setOnAction(event -> handleNegativeToggle());

        //handles brackets
        leftParentheses.setOnAction(event -> handleButtonClick("("));
        rightParentheses.setOnAction(event -> handleButtonClick(")"));
        leftCurrlyBracket.setOnAction(event -> handleButtonClick("{"));
        rightCurrlyBracket.setOnAction(event -> handleButtonClick("}"));

        //handles decimal
        decimal.setOnAction(event -> handleDecimal());

        //handles the undo and clear button
        undo.setOnAction(this::handleDelete);
        clear.setOnAction(this::handleDelete);
    }

    //function to calculate the result
    public void handleEquals() {
        String currentText = output.getText();
        if (currentText.isEmpty()) return;

        try {
            // Preprocess to simplify double negatives before evaluation
            currentText = preprocessExpression(currentText);

            // Step 1: Solve Parentheses `()`
            while (currentText.contains("(")) {
                currentText = evaluateInnermostExpression(currentText, '(', ')');
            }

            // Step 2: Solve Curly Brackets `{}` (After Parentheses)
            while (currentText.contains("{")) {
                currentText = evaluateInnermostExpression(currentText, '{', '}');
            }

            // Step 3: Solve the remaining expression using PEMDAS
            double result = evaluateExpression(currentText);

            // Display result in output field
            output.setText(String.valueOf(result));
        } catch (Exception e) {
            output.setText("Error");
        }
    }

    private String evaluateInnermostExpression(String expression, char open, char close) {
        int openIndex = expression.lastIndexOf(open);
        if (openIndex == -1) return expression; // No matching brackets

        int closeIndex = expression.indexOf(close, openIndex);
        if (closeIndex == -1) return expression; // No closing bracket

        String innerExpression = expression.substring(openIndex + 1, closeIndex);
        double innerResult = evaluateExpression(innerExpression);

        // Replace the evaluated part with the computed result
        return expression.substring(0, openIndex) + innerResult + expression.substring(closeIndex + 1);
    }

    private double evaluateExpression(String expression) {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();
        int i = 0;

        while (i < expression.length()) {
            char c = expression.charAt(i);

            // Skip spaces
            if (c == ' ') {
                i++;
                continue;
            }

            // Handle negative numbers at the start or after an operator
            if (c == '-' && (i == 0 || isOperator(Character.toString(expression.charAt(i - 1))))) {
                StringBuilder numBuffer = new StringBuilder("-");
                i++; // Move past '-'
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    numBuffer.append(expression.charAt(i));
                    i++;
                }
                numbers.push(Double.parseDouble(numBuffer.toString()));
                continue;
            }

            // Handle numbers (including decimals)
            if (Character.isDigit(c) || c == '.') {
                StringBuilder numBuffer = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    numBuffer.append(expression.charAt(i));
                    i++;
                }
                numbers.push(Double.parseDouble(numBuffer.toString()));
                continue;
            }

            // Handle Parentheses and evaluate immediately
            if (c == '(') {
                int closeIndex = findClosingBracket(expression, i, '(', ')');
                if (closeIndex == -1) return 0; // Mismatched parentheses

                String subExpression = expression.substring(i + 1, closeIndex);
                double subResult = evaluateExpression(subExpression);

                numbers.push(subResult);
                i = closeIndex + 1;
                continue;
            }

            // Handle Operators (+, -, *, /, ^)
            if (isOperator(Character.toString(c))) {
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(c)) {
                    numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.push(c);
            }

            i++;
        }

        // Process remaining operators
        while (!operators.isEmpty()) {
            numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()));
        }

        return numbers.pop();
    }

    // Finds the closing bracket for a given opening bracket
    private int findClosingBracket(String expression, int openIndex, char open, char close) {
        int balance = 0;
        for (int i = openIndex; i < expression.length(); i++) {
            if (expression.charAt(i) == open) balance++;
            if (expression.charAt(i) == close) balance--;
            if (balance == 0) return i;
        }
        return -1; // No matching bracket found
    }

    private String preprocessExpression(String expression) {
        // Simplify multiple nested negatives
        while (expression.contains("(-(-")) {
            expression = expression.replace("(-(-", "");
            expression = expression.replace("))", "");
        }

        return expression;
    }

    // Determines operator precedence
    private int precedence(char operator) {
        switch (operator) {
            case '+': case '-': return 1;
            case '*': case '/': return 2;
            case '^': return 3;
            default: return -1;
        }
    }

    // Applies the mathematical operation
    private double applyOperation(char operator, double b, double a) {
        switch (operator) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/': return a / b;
            case '^': return Math.pow(a, b);
            default: return 0;
        }
    }

    //Allows the values of the buttons to be input into the text field
    public void handleButtonClick(String value) {
        String currentText = output.getText();

        if (isOperator(value)) {
            // Prevent multiple operators in a row
            if (currentText.isEmpty() || isOperator(Character.toString(currentText.charAt(currentText.length() - 1)))) {
                return; // Do nothing if the last character is already an operator
            }
        }

        output.appendText(value);
    }
    private boolean isOperator(String value) {
        return value.equals("+") || value.equals("-") || value.equals("*") || value.equals("/");
    }

    //Allows the clearing or undoing of those two actions to function
    public void handleDelete(ActionEvent event) {
        String currentText = output.getText();

        if (event.getSource() == undo && !currentText.isEmpty()) {
            output.setText(currentText.substring(0, currentText.length() - 1));
        }
        else if (event.getSource() == clear) {
            output.clear();
        }
    }
    //Toggles the negative (utilized chatgpt for the logic)
    public void handleNegativeToggle() {
        String currentText = output.getText();
        if (currentText.isEmpty()) return;

        // Check if the last character is an operator; if so, return early
        char lastChar = currentText.charAt(currentText.length() - 1);
        if (isOperator(Character.toString(lastChar))) {
            return; // Prevent toggling if the last character is an operator
        }

        // Find the last operator position (to get last number)
        int lastOperatorIndex = -1;
        for (int i = currentText.length() - 1; i >= 0; i--) {
            char c = currentText.charAt(i);
            if (isOperator(Character.toString(c))) {
                lastOperatorIndex = i;
                break;
            }
        }

        // Extract the last number
        String lastNumber = currentText.substring(lastOperatorIndex + 1).trim();

        // Ensure lastNumber is a valid number before toggling
        if (lastNumber.isEmpty()) return;

        // Fix for double negative cases
        if (lastNumber.startsWith("(-(-") && lastNumber.endsWith("))")) {
            // Convert (-(-X)) → X
            lastNumber = lastNumber.substring(4, lastNumber.length() - 2);
        } else if (lastNumber.startsWith("(-") && lastNumber.endsWith(")")) {
            // Convert (-X) → X
            lastNumber = lastNumber.substring(2, lastNumber.length() - 1);
        } else if (lastNumber.startsWith("-")) {
            // Convert -X → X
            lastNumber = lastNumber.substring(1);
        } else {
            // Convert X → (-X)
            lastNumber = "(-" + lastNumber + ")";
        }

        // Construct new expression
        String newText;
        if (lastOperatorIndex == -1) {
            // If it's the only number, update the whole text
            newText = lastNumber;
        } else {
            // Otherwise, replace only the last number
            newText = currentText.substring(0, lastOperatorIndex + 1) + lastNumber;
        }

        // Update the display
        output.setText(newText);
    }

    //Handles the decimal, so they can't be used more than once per number
    private void handleDecimal() {
        String currentText = output.getText();

        if (currentText.isEmpty()) {
            output.setText("0.");
            return;
        }

        // Find the last number in the expression
        int lastOperatorIndex = -1;
        for (int i = currentText.length() - 1; i >= 0; i--) {
            char c = currentText.charAt(i);
            if (c == '+' || c == '-' || c == '*' || c == '/') {
                lastOperatorIndex = i;
                break;
            }
        }

        // Extract the last number
        String lastNumber = currentText.substring(lastOperatorIndex + 1).trim();

        // Check if the last number already has a decimal
        if (!lastNumber.contains(".")) {
            output.setText(currentText + ".");
        }
    }
}
