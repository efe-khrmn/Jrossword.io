package com.texteditor.jrosswordio;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;

public class CellField extends TextField {

    private static final String BASE =
            "-fx-font-size: 22px; -fx-alignment: center; -fx-border-color: #222; "
                    + "-fx-border-width: 0 1 1 0; -fx-background-radius: 0; -fx-padding: 0;";

    public interface OnLetter {
        void accept(int row, int col);
    }

    private final int row;
    private final int col;

    public CellField(int row, int col, OnLetter onLetter) {
        this.row = row;
        this.col = col;

        setPrefSize(62, 62);
        setFont(Font.font(22));
        setAlignment(Pos.CENTER);
        markNeutral();

        textProperty().addListener((obs, oldText, newText) -> {
            String cleaned = newText == null ? "" : newText.replaceAll("[^A-Za-z]", "").toUpperCase();
            if (cleaned.length() > 1) {
                cleaned = cleaned.substring(cleaned.length() - 1);
            }
            if (!cleaned.equals(newText)) {
                setText(cleaned);
                return;
            }
            if (!cleaned.isEmpty()) {
                markNeutral();
                onLetter.accept(this.row, this.col);
            }
        });

        focusedProperty().addListener((obs, was, isFocused) -> {
            if (isFocused) {
                selectAll();
            }
        });
    }

    public void markNeutral() {
        setStyle(BASE + " -fx-background-color: white; -fx-text-fill: #111;");
    }

    public void markCorrect() {
        setStyle(BASE + " -fx-background-color: #d8f3dc; -fx-text-fill: #1b4332;");
    }

    public void markWrong() {
        setStyle(BASE + " -fx-background-color: #ffd8d8; -fx-text-fill: #7f1d1d;");
    }
}
