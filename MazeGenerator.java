package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Classe responsable de la génération, de l'affichage et de la manipulation
 * d'un labyrinthe sous forme de grille.
 * Le labyrinthe est représenté par une grille 2D d'entiers où les murs sont
 * codés par -1, et les cellules libres par des identifiants uniques.
 * La génération peut être instantanée ou progressive avec animation.
 * La classe supporte aussi le chargement d'un labyrinthe existant.
 * @author Jawad Harizi
 * @author Cherf Noam
 */
public class MazeGenerator {
    private final int rows;
    private final int cols;
    private final int width;
    private final int height;
    private final int[][] mazeGrid;
    private final GridPane root;
    private final List<int[]> walls;
    private final boolean isPerfect;
    private final Button[][] buttonGrid;
    private final int Speed;
    private final List<int[]> values;

    /**
     * Constructeur du générateur de labyrinthe.
     * 
     * @param rows        Nombre de lignes de cellules (non murs).
     * @param cols        Nombre de colonnes de cellules (non murs).
     * @param isPerfect   Indique si le labyrinthe doit être parfait (sans cycles).
     * @param isProgressive Indique si la génération doit être progressive (animée).
     * @param Speed       Vitesse de génération progressive en millisecondes.
     * @param isloaded    Indique si un labyrinthe existant est chargé.
     * @param loadedMazeGrid La grille de labyrinthe chargée (si isloaded=true).
     */
    public MazeGenerator(int rows, int cols, boolean isPerfect, boolean isProgressive, int Speed, boolean isloaded, int[][] loadedMazeGrid) {
        if (!isloaded) {
            this.height = 2 * rows + 1;
            this.width = 2 * cols + 1;
            this.rows = rows;
            this.cols = cols;
            this.mazeGrid = new int[height][width];
        } else {
            this.mazeGrid = loadedMazeGrid;
            this.height = mazeGrid.length;
            this.width = mazeGrid[0].length;
            this.rows = (height - 1) / 2;
            this.cols = (width - 1) / 2;
        }

        this.root = new GridPane();
        this.walls = new ArrayList<>();
        this.isPerfect = isPerfect;
        this.values = new ArrayList<>();
        this.buttonGrid = new Button[height][width];
        this.Speed = Speed;

        if (!isloaded) {
            initMazeGrid(); // initialise les murs (-1) et cellules numérotées
            collectWalls(); // collecte les murs internes (non coins)
            Collections.shuffle(walls); // mélange aléatoire des murs
            collectVallues();
            Collections.shuffle(values);
            if (isProgressive) {
                displayGrid();
                generateMazeProgressively(Speed);
            } else {
                generateMazeInstantly(); // génère sans animation
                displayGrid();
            }
            mazeGrid[height - 2][width - 1] = 0;
            mazeGrid[1][0] = 0;
            buttonGrid[height - 2][width - 1].setStyle("-fx-background-color: white;");
            buttonGrid[1][0].setStyle("-fx-background-color: green;");
        } else {
            displayGrid(); // Affiche simplement la grille chargée
        }
    }

    /**
     * Initialise la grille du labyrinthe en mettant -1 pour les murs
     * et un identifiant unique pour chaque cellule libre.
     */
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

    /**
     * Collecte tous les murs internes (non coins) dans la liste walls.
     */
    private void collectWalls() {
        for (int i = 1; i < height - 1; i++) {
            for (int j = 1; j < width - 1; j++) {
                if ((i % 2 == 1 && j % 2 == 0) || (i % 2 == 0 && j % 2 == 1)) {
                    walls.add(new int[] { i, j });
                }
            }
        }
    }

    /**
     * Collecte toutes les cellules non murs dans la liste values.
     */
    private void collectVallues() {
        for (int i = 1; i < height - 1; i++) {
            for (int j = 1; j < width - 1; j++) {
                if (mazeGrid[i][j] != -1) {
                    values.add(new int[] { i, j });
                }
            }
        }
    }

    /**
     * Génère le labyrinthe instantanément en cassant les murs selon l’algorithme.
     * Si le labyrinthe n'est pas parfait, ajoute ou enlève quelques murs supplémentaires.
     */
    private void generateMazeInstantly() {
        for (int[] wall : walls) {
            tryBreakWall(wall); // casse les murs possibles
        }

        if (!isPerfect) {
            Random r = new Random();
            int a = r.nextInt(2);
            if (a == 0) {
                breakExtraWalls(5);
            } else {
                addExtraWalls(5);
            }
        }
    }

    /**
     * Génère le labyrinthe de manière progressive avec animation.
     * 
     * @param delay Délai en millisecondes entre chaque étape de génération.
     */
    private void generateMazeProgressively(int delay) {
        Timeline timeline = new Timeline();

        List<int[]> wallsCopy = new ArrayList<>(walls);
        final int[] index = { 0 };

        KeyFrame keyFrame = new KeyFrame(Duration.millis(delay), event -> {
            if (index[0] < wallsCopy.size()) {
                int[] wall = wallsCopy.get(index[0]);
                int i = wall[0];
                int j = wall[1];

                if (tryBreakWall(wall)) {
                    // Met à jour le bouton du mur cassé
                    updateSingleButton(i, j);

                    // Met à jour les boutons des deux cellules adjacentes
                    if (i % 2 == 1) { // mur vertical
                        updateSingleButton(i, j - 1);
                        updateSingleButton(i, j + 1);
                    } else { // mur horizontal
                        updateSingleButton(i - 1, j);
                        updateSingleButton(i + 1, j);
                    }
                }

                index[0]++;
            } else {
                if (!isPerfect) {
                    Random r = new Random();
                    int a = r.nextInt(2);
                    if (a == 0) {
                        breakExtraWalls(5);
                        displayGrid();
                    } else {
                        addExtraWalls(5);
                        displayGrid();
                    }
                }
                timeline.stop();
            }
        });

        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(wallsCopy.size() + wallsCopy.size() / 5);
        timeline.play();
    }

    /**
     * Tente de casser un mur si cela connecte deux cellules distinctes.
     *
     * @param wall Coordonnées du mur à tester.
     * @return true si le mur a été cassé, false sinon.
     */
    private boolean tryBreakWall(int[] wall) {
        int i = wall[0];
        int j = wall[1];

        if (i % 2 == 1 && j % 2 == 0) { // mur vertical
            int left = mazeGrid[i][j - 1];
            int right = mazeGrid[i][j + 1];

            if (left != right) {
                mazeGrid[i][j] = left;
                replaceCellValue(right, left);
                return true;
            }
        } else if (i % 2 == 0 && j % 2 == 1) { // mur horizontal
            int top = mazeGrid[i - 1][j];
            int bottom = mazeGrid[i + 1][j];

            if (top != bottom) {
                mazeGrid[i][j] = top;
                replaceCellValue(bottom, top);
                return true;
            }
        }
        return false;
    }

    /**
     * Remplace toutes les cellules ayant l'identifiant oldId par newId.
     *
     * @param oldId Identifiant à remplacer.
     * @param newId Nouvel identifiant à appliquer.
     */
    private void replaceCellValue(int oldId, int newId) {
        for (int i = 1; i < height - 1; i++) {
            for (int j = 1; j < width - 1; j++) {
                if (mazeGrid[i][j] == oldId) {
                    mazeGrid[i][j] = newId;
                }
            }
        }
    }

    /**
     * Casse aléatoirement un certain nombre de murs supplémentaires
     * dans le labyrinthe pour créer des passages supplémentaires.
     *
     * @param count Nombre de murs à casser.
     */
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
            mazeGrid[wall[0]][wall[1]] = mazeGrid[1][1];
        }
    }

    /**
     * Ajoute aléatoirement des murs supplémentaires dans le labyrinthe,
     * bloquant certains passages.
     *
     * @param count Nombre de murs à ajouter.
     */
    private void addExtraWalls(int count) {
        List<int[]> remainingCells = new ArrayList<>();

        for (int[] value : values) {
            int i = value[0];
            int j = value[1];
            if (mazeGrid[i][j] != -1) {
                remainingCells.add(value);
            }
        }

        Collections.shuffle(remainingCells);
        for (int k = 0; k < Math.min(count, remainingCells.size()); k++) {
            int[] value = remainingCells.get(k);
            mazeGrid[value[0]][value[1]] = -1;
        }
    }

    /**
     * Affiche la grille du labyrinthe dans le GridPane en créant
     * un bouton pour chaque case et en les colorant selon leur état.
     */
    public void displayGrid() {
        root.getChildren().clear();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Button btn = new Button();
                buttonGrid[i][j] = btn;
                btn.setMinSize(1200.0 / (height + width), 1200.0 / (height + width));
                btn.setMaxSize(1200.0 / (height + width), 1200.0 / (height + width));
                colorButton(btn, mazeGrid[i][j]);

                int x = i;
                int y = j;
                btn.setOnAction(e -> {
                    changement(x, y);
                    colorButton(btn, mazeGrid[x][y]);
                });

                root.add(btn, j, i);
            }
        }
    }

    /**
     * Met à jour un seul bouton à la position donnée selon la valeur
     * de la case dans la grille du labyrinthe.
     *
     * @param i Ligne du bouton à mettre à jour.
     * @param j Colonne du bouton à mettre à jour.
     */
    private void updateSingleButton(int i, int j) {
        Button btn = new Button();
        buttonGrid[i][j] = btn;
        btn.setMinSize(1200.0 / (height + width), 1200.0 / (height + width));
        btn.setMaxSize(1200.0 / (height + width), 1200.0 / (height + width));
        colorButton(btn, mazeGrid[i][j]);

        int x = i;
        int y = j;
        btn.setOnAction(e -> {
            changement(x, y);
            colorButton(btn, mazeGrid[x][y]);
        });

        root.add(btn, j, i);
    }

    /**
     * Change l'état de la case (i, j) entre mur et passage libre,
     * sauf pour les cases sur les bords extrêmes.
     *
     * @param i Ligne de la case à changer.
     * @param j Colonne de la case à changer.
     */
    private void changement(int i, int j) {
        if (i != 0 && j != 0 && i != height - 1 && j != width - 1) {
            if (mazeGrid[i][j] == -1) {
                mazeGrid[i][j] = 0;
            } else {
                mazeGrid[i][j] = -1;
            }
        }
    }

    /**
     * Applique une couleur au bouton selon sa valeur dans la grille.
     *
     * @param btn   Bouton à colorer.
     * @param value Valeur de la case dans la grille (-1 = mur, sinon libre).
     */
    public void colorButton(Button btn, int value) {
        if (value == -1) {
            btn.setStyle("-fx-background-color: black;");
        } else {
            btn.setStyle("-fx-background-color: white;");
        }
    }

    /**
     * Retourne le GridPane contenant les boutons du labyrinthe.
     *
     * @return Le GridPane racine.
     */
    public GridPane getGridPane() {
        return root;
    }

    /**
     * Retourne la grille du labyrinthe.
     *
     * @return La grille 2D d'entiers représentant le labyrinthe.
     */
    public int[][] getMazeGrid() {
        return this.mazeGrid;
    }

    /**
     * Retourne la hauteur de la grille (nombre de lignes).
     *
     * @return Hauteur de la grille.
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Retourne la largeur de la grille (nombre de colonnes).
     *
     * @return Largeur de la grille.
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Retourne la grille des boutons.
     *
     * @return Tableau 2D de boutons représentant chaque case.
     */
    public Button[][] getButtonGrid() {
        return this.buttonGrid;
    }

    /**
     * Réinitialise le labyrinthe en remettant toutes les cases libres à 0,
     * et recolore les boutons en blanc. La case de départ est colorée en vert.
     * 
     * @param maze Instance de MazeGenerator à réinitialiser.
     */
    public void reset(MazeGenerator maze) {
        int[][] mazeGrid = maze.getMazeGrid();
        Button[][] buttonGrid = maze.getButtonGrid();
        int height = maze.getHeight();
        int width = maze.getWidth();
        int[][] grid = maze.getMazeGrid();
        for (int i = 0; i < maze.getHeight() - 1; i++) {
            for (int j = 0; j < maze.getWidth() - 1; j++) {
                if (grid[i][j] != -1) {
                    grid[i][j] = 0;
                }
            }
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (mazeGrid[i][j] == -1) {
                    buttonGrid[i][j].setStyle("-fx-background-color: black;");
                } else {
                    buttonGrid[i][j].setStyle("-fx-background-color: white;");
                }
            }
        }
        buttonGrid[1][0].setStyle("-fx-background-color: green;");
    }
}