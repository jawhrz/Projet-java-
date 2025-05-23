package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe BFS - Implémente l'algorithme de parcours en largeur (Breadth-First Search)
 * pour résoudre un labyrinthe et visualiser son exécution avec des animations.
 * 
 * @author Jawad Harizi
 * @author Cherf Noam
 */
public class BFS extends ResolutionFS {

    int nbCase = 0;
    int nbPath = 0;

    /**
     * Recherche les voisins accessibles (non murs) d'une position donnée dans le labyrinthe.
     *
     * @param position La position actuelle [ligne, colonne].
     * @param maze     Le labyrinthe dans lequel chercher.
     * @return Une liste des positions voisines accessibles.
     */
    public List<int[]> searchNeighbor(int[] position, MazeGenerator maze) {
        List<int[]> voisins = new ArrayList<>();
        int[][] mazegrid = maze.getMazeGrid();
        int ligne = position[0];
        int colonne = position[1];
        int height = mazegrid.length;
        int width = mazegrid[0].length;

        if (colonne + 1 < width && mazegrid[ligne][colonne + 1] != -1)
            voisins.add(new int[]{ligne, colonne + 1});
        if (ligne + 1 < height && mazegrid[ligne + 1][colonne] != -1)
            voisins.add(new int[]{ligne + 1, colonne});
        if (colonne - 1 >= 0 && mazegrid[ligne][colonne - 1] != -1)
            voisins.add(new int[]{ligne, colonne - 1});
        if (ligne - 1 >= 0 && mazegrid[ligne - 1][colonne] != -1)
            voisins.add(new int[]{ligne - 1, colonne});

        return voisins;
    }

    /**
     * Initialise les distances dans le labyrinthe en réinitialisant toutes les cases accessibles à 0.
     *
     * @param maze Le labyrinthe à réinitialiser.
     */
    public void initDistance(MazeGenerator maze) {
        int[][] grid = maze.getMazeGrid();
        for (int i = 0; i < maze.getHeight() - 1; i++) {
            for (int j = 0; j < maze.getWidth() - 1; j++) {
                if (grid[i][j] != -1) {
                    grid[i][j] = 0;
                }
            }
        }
    }

    /**
     * Exécute l'algorithme BFS pour remplir le labyrinthe avec les distances depuis la sortie.
     *
     * @param maze Le labyrinthe à résoudre.
     */
    public void bfs(MazeGenerator maze) {
        nbCase = 1;
        int[][] grid = maze.getMazeGrid();
        initDistance(maze);

        int distance = 1;
        List<int[]> dejavu = new ArrayList<>();
        List<int[]> atraiter = new ArrayList<>();

        int[] position = {maze.getHeight() - 2, maze.getWidth() - 1};
        grid[position[0]][position[1]] = distance;
        atraiter.add(position);
        dejavu.add(position);

        while (grid[1][1] == 0 && !atraiter.isEmpty()) {
            int size = atraiter.size();
            distance++;

            for (int k = 0; k < size; k++) {
                int[] courant = atraiter.remove(0);

                for (int[] neighbor : searchNeighbor(courant, maze)) {
                    if (!contains(dejavu, neighbor)) {
                        int a = neighbor[0];
                        int b = neighbor[1];
                        grid[a][b] = distance;
                        atraiter.add(new int[]{a, b});
                        dejavu.add(new int[]{a, b});
                        nbCase++;
                    }
                }
            }
        }
    }

    /**
     * Exécute BFS avec une animation visuelle du remplissage des distances.
     *
     * @param maze Le labyrinthe à résoudre avec animation.
     */
    public void bfsAnimation(MazeGenerator maze) {
        nbCase = 1;
        int[][] grid = maze.getMazeGrid();
        initDistance(maze);
        int height = maze.getHeight();
	    int width = maze.getWidth();
        int distance = 1;
        List<int[]> dejavu = new ArrayList<>();
        List<int[]> atraiter = new ArrayList<>();
        List<Button> pathButtons = new ArrayList<>();
        Button[][] buttonGrid = maze.getButtonGrid();

        int[] position = {maze.getHeight() - 2, maze.getWidth() - 1};
        grid[position[0]][position[1]] = distance;
        atraiter.add(position);
        dejavu.add(position);

        while (grid[1][1] == 0 && !atraiter.isEmpty()) {
            int size = atraiter.size();
            distance++;

            for (int k = 0; k < size; k++) {
                int[] courant = atraiter.remove(0);

                for (int[] neighbor : searchNeighbor(courant, maze)) {
                    int a = neighbor[0];
                    int b = neighbor[1];

                    if (!contains(dejavu, neighbor)) {
                        grid[a][b] = distance;
                        atraiter.add(new int[]{a, b});
                        dejavu.add(new int[]{a, b});
                        pathButtons.add(buttonGrid[a][b]);
                        nbCase++;
                    }
                }
            }
        }

        Timeline timeline = new Timeline();
        for (int k = 0; k < pathButtons.size(); k++) {
            Button btn = pathButtons.get(k);
            KeyFrame keyFrame = new KeyFrame(Duration.millis((500 * k)/(height+width)), e -> {
                btn.setStyle("-fx-background-color: da4c80;");
            });
            timeline.getKeyFrames().add(keyFrame);
        }

        timeline.play();
    }

    /**
     * Met en surbrillance le chemin optimal trouvé en suivant les distances.
     *
     * @param maze Le labyrinthe à analyser.
     */
    public void highlightPath(MazeGenerator maze) {
        nbPath = 1;
        int[][] grid = maze.getMazeGrid();
        Button[][] buttonGrid = maze.getButtonGrid();
        int i = 1, j = 1;
        int distance = grid[i][j];

        buttonGrid[1][1].setStyle("-fx-background-color: green;");

        while (distance > 1) {
            for (int[] dir : new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}}) {
                int ni = i + dir[0];
                int nj = j + dir[1];

                if (ni >= 0 && nj >= 0 && ni < maze.getHeight() && nj < maze.getWidth()) {
                    if (grid[ni][nj] == distance - 1) {
                        i = ni;
                        j = nj;
                        buttonGrid[i][j].setStyle("-fx-background-color: green;");
                        distance--;
                        nbPath++;
                        break;
                    }
                }
            }
        }
    }

    /**
     * Anime la mise en surbrillance du chemin optimal.
     *
     * @param maze Le labyrinthe contenant le chemin à animer.
     */
    public void highlightPathAnimation(MazeGenerator maze) {
        int[][] grid = maze.getMazeGrid();
        int height = maze.getHeight();
	    int width = maze.getWidth();
        Button[][] buttonGrid = maze.getButtonGrid();
        List<Button> pathButtons = new ArrayList<>();
        nbPath = 1;

        int i = 1, j = 1;
        int distance = grid[i][j];

        buttonGrid[1][1].setStyle("-fx-background-color: green;");

        while (distance > 1) {
            for (int[] dir : new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}}) {
                int ni = i + dir[0];
                int nj = j + dir[1];

                if (ni >= 0 && nj >= 0 && ni < maze.getHeight() && nj < maze.getWidth()) {
                    if (grid[ni][nj] == distance - 1) {
                        i = ni;
                        j = nj;
                        pathButtons.add(buttonGrid[i][j]);
                        distance--;
                        nbPath++;
                        break;
                    }
                }
            }
        }

        Timeline timeline = new Timeline();
        for (int k = 0; k < pathButtons.size(); k++) {
            Button btn = pathButtons.get(k);
            KeyFrame keyFrame = new KeyFrame(Duration.millis(500 * k/(height+width)), e -> {
                btn.setStyle("-fx-background-color: green;");
            });
            timeline.getKeyFrames().add(keyFrame);
        }

        timeline.play();
    }

    /**
     * Réinitialise le labyrinthe en réinitialisant les distances et les couleurs des boutons.
     *
     * @param maze Le labyrinthe à réinitialiser.
     */
    public void reset(MazeGenerator maze) {
        int[][] mazeGrid = maze.getMazeGrid();
        Button[][] buttonGrid = maze.getButtonGrid();
        int height = maze.getHeight();
        int width = maze.getWidth();
        initDistance(maze);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (mazeGrid[i][j] != -1) {
                    maze.colorButton(buttonGrid[i][j], 0);
                }
            }
        }
    }

    /**
     * Vérifie si une liste de positions contient une position donnée.
     *
     * @param liste   La liste des positions déjà visitées.
     * @param element La position à vérifier.
     * @return true si la position est déjà dans la liste, sinon false.
     */
    public boolean contains(List<int[]> liste, int[] element) {
        for (int[] elt : liste) {
            if (elt[0] == element[0] && elt[1] == element[1]) {
                return true;
            }
        }
        return false;
    }
}