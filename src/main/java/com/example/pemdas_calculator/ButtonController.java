package com.example.pemdas_calculator;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

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
            if (c == '+' || c == '-' || c == '*' || c == '/') {
                lastOperatorIndex = i;
                break;
            }
        }

        // Extract last number
        String lastNumber = currentText.substring(lastOperatorIndex + 1).trim();

        // Toggle negative sign properly
        String newText;
        if (lastNumber.startsWith("(-") && lastNumber.endsWith(")")) {
            // Remove the (-...) notation
            newText = currentText.substring(0, lastOperatorIndex + 1) + lastNumber.substring(2, lastNumber.length() - 1);
        } else if (lastNumber.startsWith("-") && !lastNumber.startsWith("(-")) {
            // Remove simple negative sign
            newText = currentText.substring(0, lastOperatorIndex + 1) + lastNumber.substring(1);
        } else {
            // Add (-...) notation
            newText = currentText.substring(0, lastOperatorIndex + 1) + "(-" + lastNumber + ")";
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
