package com.example.pemdas_calculator;

// JavaFX imports for UI elements and event handling
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.event.ActionEvent;

// Java utility imports for data structures and exception handling
import java.util.Stack;
import java.util.EmptyStackException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Java regex imports for expression validation
import java.util.regex.Pattern;
import java.util.regex.Matcher;

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
    private TextArea errorLog;

    @FXML
    private Button exponent;

    @FXML
    private Button five;

    @FXML
    private Button four;

    @FXML
    private Button genCorrect;

    @FXML
    private Button genRanSyntactical;

    @FXML
    private Button genRandom;

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

        //handles other math stuff
        log.setOnAction(event -> handleButtonClick("log("));
        ln.setOnAction(event -> handleButtonClick("ln("));
        sine.setOnAction(event -> handleButtonClick("sin("));
        cosine.setOnAction(event -> handleButtonClick("cos("));
        tangent.setOnAction(event -> handleButtonClick("tan("));

        //handles exponent values
        exponent.setOnAction(event -> handleButtonClick("^"));

        //handles the undo and clear button
        undo.setOnAction(this::handleDelete);
        clear.setOnAction(this::handleDelete);

        //handles the random expression generation buttons
        genCorrect.setOnAction(event -> handleRandomGeneration("correct"));
        genRandom.setOnAction(event -> handleRandomGeneration("random"));
        genRanSyntactical.setOnAction(event -> handleRandomGeneration("syntactical"));
    }

    public void handleRandomGeneration(String type) {
        Random random = new Random();
        switch (type) {
            case "correct":
                StringBuilder expression = new StringBuilder();
                // Generate a random number of tokens (between 3 and 7 tokens)
                int tokenCount = random.nextInt(5) + 3; // yields a value from 3 to 7

                for (int i = 0; i < tokenCount; i++) {
                    String token = "";
                    // 50% chance to wrap the token with a function (log, ln, sin, cos, or tan)
                    if (random.nextDouble() < 0.5) {
                        String[] functions = { "log", "ln", "sin", "cos", "tan" };
                        String func = functions[random.nextInt(functions.length)];
                        // For log and ln, ensure a positive argument; for trigonometric functions, any number works
                        double arg = random.nextDouble() * 100 + 1; // ensures the argument is > 0
                        token = func + "(" + String.format("%.2f", arg) + ")";
                    } else {
                        // Generate a plain number (either integer or decimal)
                        if (random.nextBoolean()) {
                            token = String.valueOf(random.nextInt(100));
                        } else {
                            token = String.format("%.2f", random.nextDouble() * 100);
                        }
                    }

                    // With a 30% chance, append an exponentiation operator and an exponent (e.g. n^m)
                    if (random.nextDouble() < 0.3) {
                        token += "^";
                        // Use an exponent value between 1 and 10 (integer)
                        token += String.valueOf(random.nextInt(10) + 1);
                    }

                    expression.append(token);

                    // Append an operator if this is not the last token
                    if (i < tokenCount - 1) {
                        String[] operators = { "+", "-", "*", "/" };
                        expression.append(operators[random.nextInt(operators.length)]);
                    }
                }

                // Optionally wrap the entire expression in parentheses 50% of the time
                if (random.nextBoolean()) {
                    expression.insert(0, "(");
                    expression.append(")");
                }

                output.setText(expression.toString());
                break;
            case "syntactical":
                //Generate an expression with random syntactical errors
                errorLog.appendText("Generated syntactical expression\n");
                break;
            case "random":
                //Generate a completely random expression
                errorLog.appendText("Generated random expression\n");
                break;
            default:
                errorLog.appendText("Unknown generation type\n");
                break;
        }
    }

    public void handleEquals() {
        String currentText = output.getText();
        if (currentText.isEmpty()) return;

        // Pre-validate the expression to collect all errors.
        List<String> validationErrors = validateExpression(currentText);
        if (!validationErrors.isEmpty()) {
            for (String error : validationErrors) {
                errorLog.appendText(error + "\n");
            }
            // Scroll to ensure the latest error is visible.
            errorLog.positionCaret(errorLog.getLength());
            output.setText("Errors detected. Check error log.");
            return;
        }

        //If no validation errors, proceed to evaluate the expression.
        try {
            currentText = autoCloseBrackets(currentText);
            currentText = preprocessExpression(currentText);
            currentText = evaluateFunctions(currentText);

            while (currentText.contains("(")) {
                currentText = evaluateInnermostExpression(currentText, '(', ')');
            }
            while (currentText.contains("{")) {
                currentText = evaluateInnermostExpression(currentText, '{', '}');
            }

            double result = evaluateExpression(currentText);
            output.setText(String.valueOf(result));
        } catch (Exception e) {
            handleErrors("Syntax Error in expression \"" + currentText + "\": " + e.getMessage(), e);
        }
    }

    public void handleErrors(String errorMessage, Exception e) {
        // Create a detailed message including the exception's class and its message.
        String detailedMessage = errorMessage + " (" + e.getClass().getSimpleName() + ": " + e.getMessage() + ")";
        errorLog.appendText(detailedMessage + "\n");
    }

    //Generated by chatgpt to list out more detailed error messages, should include all errors within an expression
    private List<String> validateExpression(String expression) {
        List<String> errors = new ArrayList<>();

        //Check for extra (unbalanced) closing parentheses and curly braces. (except for those handled by the missingclosingbrackets function)
        int parenCount = 0;
        int curlyCount = 0;
        for (char c : expression.toCharArray()) {
            if (c == '(') {
                parenCount++;
            } else if (c == ')') {
                parenCount--;
                if (parenCount < 0) {
                    errors.add("Mismatched parentheses: extra closing parenthesis detected");
                    parenCount = 0; // Reset to avoid duplicate messages
                }
            } else if (c == '{') {
                curlyCount++;
            } else if (c == '}') {
                curlyCount--;
                if (curlyCount < 0) {
                    errors.add("Mismatched curly braces: extra closing curly brace detected");
                    curlyCount = 0;
                }
            }
        }

        //Validate number formats: detect tokens with more than one decimal point.
        Pattern invalidNumberPattern = Pattern.compile("\\b\\d*\\.\\d*\\.[\\d.]*\\b");
        Matcher matcher = invalidNumberPattern.matcher(expression);
        while (matcher.find()) {
            errors.add("Invalid number format: '" + matcher.group() + "'");
        }

        //Validate operator sequences.
        Pattern operatorPattern = Pattern.compile("([+\\-*/]{2,})");
        matcher = operatorPattern.matcher(expression);
        while (matcher.find()) {
            String opSeq = matcher.group();
            //Allow valid sequences: "+-", "*-", "/-", "^-", and "--" are interpreted as binary operator followed by a unary minus.
            if (opSeq.equals("+-") || opSeq.equals("*-") || opSeq.equals("/-") || opSeq.equals("^-") || opSeq.equals("--")) {
                continue; // Consider these as valid operator usage.
            }
            errors.add("Invalid operator sequence: '" + opSeq + "'");
        }

        //Validate domain for log and ln functions.
        Pattern logPattern = Pattern.compile("log\\s*\\(([^)]+)\\)");
        matcher = logPattern.matcher(expression);
        while (matcher.find()) {
            String argument = matcher.group(1).trim();
            try {
                double value = Double.parseDouble(argument);
                if (value <= 0) {
                    errors.add("log function undefined for non-positive value: " + value);
                }
            } catch (NumberFormatException e) {
                errors.add("Invalid argument for log: " + argument);
            }
        }
        Pattern lnPattern = Pattern.compile("ln\\s*\\(([^)]+)\\)");
        matcher = lnPattern.matcher(expression);
        while (matcher.find()) {
            String argument = matcher.group(1).trim();
            try {
                double value = Double.parseDouble(argument);
                if (value <= 0) {
                    errors.add("ln function undefined for non-positive value: " + value);
                }
            } catch (NumberFormatException e) {
                errors.add("Invalid argument for ln: " + argument);
            }
        }

        // 5. Validate literal division by zero.
        Pattern divByZeroPattern = Pattern.compile("/\\s*0(?![\\.0-9])");
        matcher = divByZeroPattern.matcher(expression);
        while (matcher.find()) {
            errors.add("Division by zero detected near: '" + matcher.group().trim() + "'");
        }

        // 6. Validate unsupported characters.
        Pattern allowedPattern = Pattern.compile("[^0-9A-Za-z+\\-*/^().{}\\s]");
        matcher = allowedPattern.matcher(expression);
        while (matcher.find()) {
            errors.add("Unsupported character detected: '" + matcher.group() + "'");
        }

        if (expression.contains("Infinity")) {
            errors.add("Infinity: Expression value too extreme");
        }

        return errors;
    }

    //Prevents application from crashing if you don't have a closed currly bracket and instead just inserts them
    private String autoCloseBrackets(String expression) {
        int openParentheses = 0;
        int openCurlyBrackets = 0;

        //Count how many parentheses `{}` and `()` are unclosed
        for (char c : expression.toCharArray()) {
            if (c == '(') openParentheses++;
            if (c == ')') openParentheses--;
            if (c == '{') openCurlyBrackets++;
            if (c == '}') openCurlyBrackets--;
        }

        //Auto-close unclosed `(` with `)`
        while (openParentheses > 0) {
            expression += ")";
            openParentheses--;
        }

        //Auto-close unclosed `{` with `}`
        while (openCurlyBrackets > 0) {
            expression += "}";
            openCurlyBrackets--;
        }

        return expression;
    }

    //Begins by evaluating every expression within the parentheses or brackets
    private String evaluateInnermostExpression(String expression, char open, char close) {
        //handles the lack of a closing bracket
        int openIndex = expression.lastIndexOf(open);
        if (openIndex == -1) return expression;
        int closeIndex = expression.indexOf(close, openIndex);
        if (closeIndex == -1) return expression;

        //Calculates the expression
        String innerExpression = expression.substring(openIndex + 1, closeIndex);
        double innerResult = evaluateExpression(innerExpression);

        //Allows expressions like 2(5) to evaluate properly as multiplication
        String before = openIndex > 0 ? expression.substring(0, openIndex) : "";
        String after = closeIndex + 1 < expression.length() ? expression.substring(closeIndex + 1) : "";

        if (open == '{' && !before.isEmpty() && Character.isDigit(before.charAt(before.length() - 1))) {
            before += "*";
        }
        if (open == '{' && !after.isEmpty() && (Character.isDigit(after.charAt(0)) || after.charAt(0) == '(')) {
            after = "*" + after;
        }

        return before + innerResult + after;
    }

    //Evaluates the end result expression using a form of the shunting-yard algorithm provided by chatgpt
    private double evaluateExpression(String expression) {
        expression = preprocessExpression(expression); // Ensure negatives & multiplication are corrected
        expression = evaluateFunctions(expression); // Process functions first

        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();
        int i = 0;

        try {
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

                // Handle Parentheses
                if (c == '(') {
                    int closeIndex = findClosingBracket(expression, i, '(', ')');
                    if (closeIndex == -1) {
                        throw new IllegalArgumentException("Mismatched parentheses");
                    }

                    String subExpression = expression.substring(i + 1, closeIndex);
                    double subResult = evaluateExpression(subExpression);
                    numbers.push(subResult);
                    i = closeIndex + 1;
                    continue;
                }

                // Handle Operators (+, -, *, /)
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
        } catch (EmptyStackException ese) {
            // Rethrow as an IllegalArgumentException for invalid operator sequences
            throw new IllegalArgumentException("Invalid operator sequence", ese);
        }
    }

    //Evaluates the trigonometric, logorithmic, ln, and exponent functions. Provided by yours truely chatgpt with tinkering by me to fix a couple small issues
    private String evaluateFunctions(String expression) {
        String[] functions = {"sin", "cos", "tan", "log", "ln"};

        // Process functions like sin(), cos(), log(), ln()
        for (String func : functions) {
            while (expression.contains(func + "(")) {
                int startIndex = expression.lastIndexOf(func + "(");
                int endIndex = findClosingBracket(expression, startIndex + func.length(), '(', ')');

                // No matching closing parenthesis found
                if (endIndex == -1) return expression;

                String inside = expression.substring(startIndex + func.length() + 1, endIndex);
                double value = evaluateExpression(inside);
                double result = 0;

                switch (func) {
                    case "sin":
                        result = Math.sin(Math.toRadians(value));
                        break;
                    case "cos":
                        result = Math.cos(Math.toRadians(value));
                        break;
                    case "tan":
                        result = Math.tan(Math.toRadians(value));
                        break;
                    case "log":
                        // Check if the argument is valid (log is defined only for positive numbers)
                        if (value <= 0) {
                            throw new IllegalArgumentException("log function is undefined for non-positive values: " + value);
                        }
                        result = Math.log10(value);
                        break;
                    case "ln":
                        // Check if the argument is valid (ln is defined only for positive numbers)
                        if (value <= 0) {
                            throw new IllegalArgumentException("ln function is undefined for non-positive values: " + value);
                        }
                        result = Math.log(value);
                        break;
                }

                // Replace the function call with the computed result in the expression
                expression = expression.substring(0, startIndex) + result + expression.substring(endIndex + 1);
            }
        }

        // Process exponentiation and return the modified expression
        while (expression.contains("^")) {
            int lastIndex = expression.lastIndexOf("^"); // Find rightmost '^'

            // Find base (left-side number or expression)
            int baseStart = lastIndex - 1;
            if (expression.charAt(baseStart) == ')') {
                // If the character is ')', find the matching '('
                baseStart = findOpeningBracket(expression, baseStart, '(', ')');
            } else {
                // Move left to capture digits and decimals.
                // Include '-' only if it’s a unary minus (i.e. either at the very start or preceded by an operator)
                while (baseStart >= 0 && (
                        Character.isDigit(expression.charAt(baseStart)) ||
                                expression.charAt(baseStart) == '.'
                                || (expression.charAt(baseStart) == '-' &&
                                (baseStart == 0 || isOperator(Character.toString(expression.charAt(baseStart - 1))))
                        )
                )) {
                    baseStart--;
                }
                baseStart++; // Adjust to the actual start of the base
            }

            // Find exponent (right-side number or expression)
            int expStart = lastIndex + 1;
            int expEnd = expStart;
            if (expression.charAt(expStart) == '(') {
                expEnd = findClosingBracket(expression, expStart, '(', ')') + 1;
            } else {
                while (expEnd < expression.length() &&
                        (Character.isDigit(expression.charAt(expEnd)) || expression.charAt(expEnd) == '.'
                                || (expression.charAt(expEnd) == '-' && expEnd == lastIndex + 1))) {
                    expEnd++;
                }
            }

            // Evaluate the base and exponent parts
            double base = evaluateExpression(expression.substring(baseStart, lastIndex));
            double exponent = evaluateExpression(expression.substring(expStart, expEnd));
            double result = Math.pow(base, exponent);

            // Replace the exponentiation portion with the computed result
            expression = expression.substring(0, baseStart) + result + expression.substring(expEnd);
        }
        return expression;
    }

    //Finds the closing bracket for a given opening bracket, and the opening bracket for a given closed bracket
    private int findClosingBracket(String expression, int openIndex, char open, char close) {
        int balance = 0;
        for (int i = openIndex; i < expression.length(); i++) {
            if (expression.charAt(i) == open) balance++;
            if (expression.charAt(i) == close) balance--;
            if (balance == 0) return i;
        }
        return -1; //No matching bracket found
    }
    private int findOpeningBracket(String expression, int closeIndex, char open, char close) {
        int balance = 0;
        for (int i = closeIndex; i >= 0; i--) {
            if (expression.charAt(i) == close) balance++;
            if (expression.charAt(i) == open) balance--;
            if (balance == 0) return i; // Found the matching open bracket
        }
        return -1; //No matching bracket found
    }

    private String preprocessExpression(String expression) {
        //Handles nested negatives
        while (expression.contains("(-(-")) {
            expression = expression.replace("(-(-", "");
            expression = expression.replace("))", "");
        }

        //Insert Explicit Multiplication for Parentheses and Curly Brackets
        expression = expression.replaceAll("(\\d)(\\()", "$1*(");  // 2(5) → 2*(5)
        expression = expression.replaceAll("(\\))(\\d)", ")*$2");  // (2)5 → (2)*5
        expression = expression.replaceAll("(\\d)(\\{)", "$1*{");  // 3{4} → 3*{4}
        expression = expression.replaceAll("(\\})(\\d)", "}*$2");  // {3}4 → {3}*4
        expression = expression.replaceAll("(\\))(\\{)", ")*{");   // (2){3} → (2)*{3}
        expression = expression.replaceAll("(\\})(\\()", "}*(");   // {3}(4) → {3}*(4)

        return expression;
    }

    //Determines operator precedence
    private int precedence(char operator) {
        switch (operator) {
            case '+': case '-': return 1;
            case '*': case '/': return 2;
            case '^': return 3;
            default: return -1;
        }
    }

    //Applies the mathematical operation
    private double applyOperation(char operator, double b, double a) {
        switch (operator) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                // Explicitly check for division by zero
                if (b == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                return a / b;
            case '^':
                return Math.pow(a, b);
            default:
                return 0;
        }
    }

    //Allows the values of the buttons to be input into the text field
    public void handleButtonClick(String value) {
        String currentText = output.getText();

        if (isOperator(value)) {
            //Prevent multiple operators in a row
            if (currentText.isEmpty() || isOperator(Character.toString(currentText.charAt(currentText.length() - 1)))) {
                return;
            }
        }

        //Prevents an exponent from being used in the wrong spot
        if (value.equals("^")) {
            if (currentText.isEmpty()) {
                return;
            }
            char lastChar = currentText.charAt(currentText.length() - 1);
            if (!Character.isDigit(lastChar) && lastChar != ')') {
                return;
            }
        }

        output.appendText(value);
    }

    //Checks for operators
    private boolean isOperator(String value) {
        return value.equals("+") || value.equals("-") || value.equals("*") || value.equals("/");
    }

    //Allows the clearing or undoing of those two actions to function
    public void handleDelete(ActionEvent event) {
        String currentText = output.getText();

        //Handles the undo button
        if (event.getSource() == undo && !currentText.isEmpty()) {
            output.setText(currentText.substring(0, currentText.length() - 1));
        }
        //Handles the clear button
        else if (event.getSource() == clear) {
            output.clear();
        }
    }
    //Toggles the negative (utilized chatgpt for the logic) (this function was such a headache to get working)
    public void handleNegativeToggle() {
        String currentText = output.getText().trim();
        if (currentText.isEmpty()) return;

        //Case: If the last character is an open parenthesis `(` or `{`, insert `-` immediately
        if (currentText.endsWith("(") || currentText.endsWith("{")) {
            output.setText(currentText + "-");
            return;
        }

        //Check if the entire input is a single number (not part of an expression)
        try {
            double number = Double.parseDouble(currentText);
            number = -number; // Toggle sign
            output.setText(String.valueOf(number)); // Update display
            return;
        } catch (NumberFormatException e) {
            //If not a standalone number, continue checking expression
        }

        //Find the last operator position
        int lastOperatorIndex = -1;
        for (int i = currentText.length() - 1; i >= 0; i--) {
            char c = currentText.charAt(i);
            if (isOperator(Character.toString(c))) {
                lastOperatorIndex = i;
                break;
            }
        }

        //Extract last number (including cases where it's inside parentheses `{}` or `()`)
        String lastNumber = currentText.substring(lastOperatorIndex + 1).trim();

        //Case: Unclosed Parenthesis or Bracket with a Number (e.g., `(2` → `(-2` or `{2` → `{-2`)
        if ((lastNumber.startsWith("(") && !lastNumber.endsWith(")")) ||
                (lastNumber.startsWith("{") && !lastNumber.endsWith("}"))) {
            char bracketType = lastNumber.charAt(0); //Detect the bracket type `(` or `{`
            output.setText(currentText.substring(0, lastOperatorIndex + 1) + bracketType + "-" + lastNumber.substring(1));
            return;
        }

        //Case: Completed Parentheses `(2)` → `(-2)`, `{2}` → `{-2}`
        if ((lastNumber.startsWith("(") && lastNumber.endsWith(")")) ||
                (lastNumber.startsWith("{") && lastNumber.endsWith("}"))) {
            char bracketType = lastNumber.charAt(0); // Preserve original bracket type
            String innerValue = lastNumber.substring(1, lastNumber.length() - 1).trim();
            try {
                double num = Double.parseDouble(innerValue);
                num = -num; //Toggle the sign
                output.setText(currentText.substring(0, lastOperatorIndex + 1) + bracketType + num + bracketType);
                return;
            } catch (NumberFormatException ignored) {
                //Continue to other cases if parsing fails
            }
        }

        //Case: Number at start of expression (no operators before it)
        if (lastOperatorIndex == -1) {
            try {
                double num = Double.parseDouble(lastNumber);
                num = -num;
                output.setText(String.valueOf(num)); //Directly update display
            } catch (NumberFormatException ignored) {
            }
            return;
        }

        //Case: Toggle last number in expression
        try {
            double lastNum = Double.parseDouble(lastNumber);
            lastNum = -lastNum;
            output.setText(currentText.substring(0, lastOperatorIndex + 1) + lastNum);
        }
        catch (NumberFormatException ignored) {
        }
    }

    //Handles the decimal, so they can't be used more than once per number
    private void handleDecimal() {
        String currentText = output.getText();

        if (currentText.isEmpty()) {
            output.setText("0.");
            return;
        }

        //Find the last number in the expression
        int lastOperatorIndex = -1;
        for (int i = currentText.length() - 1; i >= 0; i--) {
            char c = currentText.charAt(i);
            if (c == '+' || c == '-' || c == '*' || c == '/') {
                lastOperatorIndex = i;
                break;
            }
        }

        //Extract the last number
        String lastNumber = currentText.substring(lastOperatorIndex + 1).trim();

        //Check if the last number already has a decimal
        if (!lastNumber.contains(".")) {
            output.setText(currentText + ".");
        }
    }
}
