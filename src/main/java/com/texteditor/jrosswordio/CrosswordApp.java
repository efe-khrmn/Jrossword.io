package com.texteditor.jrosswordio;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class CrosswordApp extends Application {

    private final CellField[][] cells = new CellField[TestPuzzle.SIZE][TestPuzzle.SIZE];
    private final Label status = new Label("Type a letter in each square.");

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f7f5ef;");

        Label title = new Label("Mini Crossword");
        title.setFont(Font.font("System", 26));
        title.setPadding(new Insets(0, 0, 14, 0));
        root.setTop(title);

        root.setCenter(buildGrid());
        root.setRight(buildClues());
        root.setBottom(buildFooter());

        stage.setTitle("Jrossword.io");
        stage.setScene(new Scene(root));
        stage.show();
        cells[0][0].requestFocus();
    }

    private GridPane buildGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setStyle("-fx-border-color: #222; -fx-border-width: 2;");

        for (int r = 0; r < TestPuzzle.SIZE; r++) {
            for (int c = 0; c < TestPuzzle.SIZE; c++) {
                StackPane cellPane = new StackPane();
                cellPane.setPrefSize(62, 62);

                if (TestPuzzle.isBlocked(r, c)) {
                    cellPane.setStyle("-fx-background-color: #222; -fx-border-color: #222;");
                } else {
                    CellField field = new CellField(r, c, this::onLetterTyped);
                    cells[r][c] = field;
                    cellPane.getChildren().add(field);

                    int number = TestPuzzle.numberAt(r, c);
                    if (number > 0) {
                        Label numberLabel = new Label(String.valueOf(number));
                        numberLabel.setFont(Font.font(10));
                        numberLabel.setStyle("-fx-text-fill: #666;");
                        StackPane.setAlignment(numberLabel, Pos.TOP_LEFT);
                        StackPane.setMargin(numberLabel, new Insets(3, 0, 0, 4));
                        numberLabel.setMouseTransparent(true);
                        cellPane.getChildren().add(numberLabel);
                    }
                }
                grid.add(cellPane, c, r);
            }
        }
        return grid;
    }

    private VBox buildClues() {
        VBox box = new VBox(6);
        box.setPadding(new Insets(0, 0, 0, 28));
        box.setPrefWidth(280);

        box.getChildren().add(sectionLabel("Across"));
        TestPuzzle.acrossClues().forEach(clue -> box.getChildren().add(new Label(clue.toString())));

        Label downHeader = sectionLabel("Down");
        VBox.setMargin(downHeader, new Insets(14, 0, 0, 0));
        box.getChildren().add(downHeader);
        TestPuzzle.downClues().forEach(clue -> box.getChildren().add(new Label(clue.toString())));

        return box;
    }

    private Label sectionLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("System", 16));
        return label;
    }

    private HBox buildFooter() {
        Button check = new Button("Check");
        check.setOnAction(e -> check());

        Button reveal = new Button("Reveal");
        reveal.setOnAction(e -> reveal());

        Button clear = new Button("Clear");
        clear.setOnAction(e -> clear());

        HBox footer = new HBox(10, check, reveal, clear, status);
        footer.setAlignment(Pos.CENTER_LEFT);
        footer.setPadding(new Insets(18, 0, 0, 0));
        return footer;
    }

    private void onLetterTyped(int row, int col) {
        for (int i = row * TestPuzzle.SIZE + col + 1; i < TestPuzzle.SIZE * TestPuzzle.SIZE; i++) {
            CellField next = cells[i / TestPuzzle.SIZE][i % TestPuzzle.SIZE];
            if (next != null) {
                next.requestFocus();
                next.selectAll();
                return;
            }
        }
    }

    private void check() {
        int filled = 0;
        int correct = 0;
        for (int r = 0; r < TestPuzzle.SIZE; r++) {
            for (int c = 0; c < TestPuzzle.SIZE; c++) {
                CellField field = cells[r][c];
                if (field == null) {
                    continue;
                }
                String value = field.getText().trim().toUpperCase();
                if (value.isEmpty()) {
                    field.markNeutral();
                    continue;
                }
                filled++;
                if (value.charAt(0) == TestPuzzle.solutionAt(r, c)) {
                    correct++;
                    field.markCorrect();
                } else {
                    field.markWrong();
                }
            }
        }
        int total = openCellCount();
        if (correct == total) {
            status.setText("Solved! Nice work.");
        } else {
            status.setText(correct + " / " + total + " correct (" + filled + " filled).");
        }
    }

    private void reveal() {
        forEachCell((r, c, field) -> {
            field.setText(String.valueOf(TestPuzzle.solutionAt(r, c)));
            field.markCorrect();
        });
        status.setText("Answers revealed.");
    }

    private void clear() {
        forEachCell((r, c, field) -> {
            field.setText("");
            field.markNeutral();
        });
        status.setText("Grid cleared.");
        cells[0][0].requestFocus();
    }

    private int openCellCount() {
        int count = 0;
        for (CellField[] row : cells) {
            for (CellField field : row) {
                if (field != null) {
                    count++;
                }
            }
        }
        return count;
    }

    private interface CellVisitor {
        void visit(int row, int col, CellField field);
    }

    private void forEachCell(CellVisitor visitor) {
        for (int r = 0; r < TestPuzzle.SIZE; r++) {
            for (int c = 0; c < TestPuzzle.SIZE; c++) {
                if (cells[r][c] != null) {
                    visitor.visit(r, c, cells[r][c]);
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
