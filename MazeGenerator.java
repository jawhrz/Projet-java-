package application;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MazeGenerator {
    private final int rows;
    private final int cols;
    private final int width;
    private final int height;
    private final int[][] mazeGrid;
    private final GridPane root;
    private final List<int[]> walls;
    private final boolean isPerfect;

    public MazeGenerator(int rows, int cols, boolean isPerfect) {
        this.rows = rows;
        this.cols = cols;
        this.width = 2 * cols + 1;
        this.height = 2 * rows + 1;
        this.mazeGrid = new int[height][width];
        this.root = new GridPane();
        this.walls = new ArrayList<>();
        this.isPerfect = isPerfect;

        initMazeGrid();
        collectWalls();
        Collections.shuffle(walls);
        generateMaze();
    }

    private void initMazeGrid() {
        int cellId = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i % 2 == 1 && j % 2 == 1) {
                    mazeGrid[i][j] = cellId++;
                } else {
                    mazeGrid[i][j] = -1; // Mur
                }
            }
        }
    }

    private void collectWalls() {
        for (int i = 1; i < height - 1; i++) {
            for (int j = 1; j < width - 1; j++) {
                if (i % 2 == 1 && j % 2 == 0) {
                    walls.add(new int[]{i, j}); // Mur vertical
                } else if (i % 2 == 0 && j % 2 == 1) {
                    walls.add(new int[]{i, j}); // Mur horizontal
                }
            }
        }
    }

    private void generateMaze() {
        for (int[] wall : walls) {
            int i = wall[0];
            int j = wall[1];

            if (i % 2 == 1 && j % 2 == 0) { // Mur vertical
                int left = mazeGrid[i][j - 1];
                int right = mazeGrid[i][j + 1];

                if (left != right) {
                    mazeGrid[i][j] = left;
                    replaceCellValue(right, left);
                }
            } else if (i % 2 == 0 && j % 2 == 1) { // Mur horizontal
                int top = mazeGrid[i - 1][j];
                int bottom = mazeGrid[i + 1][j];

                if (top != bottom) {
                    mazeGrid[i][j] = top;
                    replaceCellValue(bottom, top);
                }
            }
        }

        if (!isPerfect) {
            breakExtraWalls(5);
        }
    }

    private void replaceCellValue(int oldId, int newId) {
        for (int i = 1; i < height; i += 2) {
            for (int j = 1; j < width; j += 2) {
                if (mazeGrid[i][j] == oldId) {
                    mazeGrid[i][j] = newId;
                }
            }
        }
    }

    private void breakExtraWalls(int count) {
        List<int[]> remainingWalls = new ArrayList<>();

        for (int[] wall : walls) {
            int i = wall[0];
            int j = wall[1];
            if (mazeGrid[i][j] == -1) {
                remainingWalls.add(wall);
            }
        }

        Collections.shuffle(remainingWalls);
        for (int k = 0; k < Math.min(count, remainingWalls.size()); k++) {
            int[] wall = remainingWalls.get(k);
            mazeGrid[wall[0]][wall[1]] = 0;
        }
    }

    /**
     * Génère un GridPane avec des boutons interactifs.
     */
    public GridPane getGridPane() {
        root.getChildren().clear();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Button btn = new Button();
                btn.setMinSize(20, 20);
                btn.setMaxSize(20, 20);

                int x = i;
                int y = j;

                // Applique le style initial
                updateButtonStyle(btn, mazeGrid[i][j]);

                // Action lors du clic
                btn.setOnAction(e -> {
                    toggleWall(x, y);
                    updateButtonStyle(btn, mazeGrid[x][y]);
                });

                root.add(btn, j, i);
            }
        }

        return root;
    }

    /**
     * Bascule entre mur et case vide.
     */
    private void toggleWall(int i, int j) {
        if (mazeGrid[i][j] == -1) {
            mazeGrid[i][j] = 0; // Mur devient une case vide
        } else if (mazeGrid[i][j] == 0) {
            mazeGrid[i][j] = -1; // Case vide devient un mur
        }
    }

    /**
     * Met à jour le style d'un bouton en fonction de sa valeur.
     */
    private void updateButtonStyle(Button btn, int value) {
        if (value == -1) {
            btn.setStyle("-fx-background-color: black;");
        } else {
            btn.setStyle("-fx-background-color: white;");
        }
    }
}
