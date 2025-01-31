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

        undo.setOnAction(this::handleDelete);
        clear.setOnAction(this::handleDelete);
    }

    //Allows the values of the buttons to be input into the text field
    public void handleButtonClick(String value) {
        output.appendText(value);
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
}
