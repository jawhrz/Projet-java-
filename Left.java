package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import java.util.*;

/**
 * Classe représentant l'algorithme de résolution par la main gauche (Left-hand rule).
 * Cet algorithme suit systématiquement le mur situé à gauche pour atteindre la sortie.
 *
 * @author Jawad Harizi
 * @author Cherf Noam
 */
public class Left extends Resolution {

    int nbCase = 0;
    int nbPath = 0;

    /**
     * Recherche les voisins accessibles depuis une position selon la direction courante.
     *
     * @param position  La position actuelle {ligne, colonne}.
     * @param maze      Le générateur de labyrinthe utilisé.
     * @param direction La direction actuelle ("N", "E", "S", "W").
     * @return La liste des positions voisines accessibles, triées selon la stratégie "main gauche".
     */
    public List<int[]> searchNeighborLeft(int[] position, MazeGenerator maze, String direction) {
        List<int[]> voisins = new ArrayList<>();
        int[][] grid = maze.getMazeGrid();
        int y = position[0];
        int x = position[1];
        int height = grid.length;
        int width = grid[0].length;

        int[][] directions;
        switch (direction) {
            case "N":
                directions = new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
                break;
            case "E":
                directions = new int[][]{{0, -1}, {1, 0}, {0, 1}, {-1, 0}};
                break;
            case "S":
                directions = new int[][]{{-1, 0}, {0, -1}, {1, 0}, {0, 1}};
                break;
            case "W":
                directions = new int[][]{{0, 1}, {-1, 0}, {0, -1}, {1, 0}};
                break;
            default:
                directions = new int[][]{};
        }

        for (int[] d : directions) {
            int ny = y + d[0];
            int nx = x + d[1];
            if (ny >= 0 && ny < height && nx >= 0 && nx < width && grid[ny][nx] != -1) {
                voisins.add(new int[]{ny, nx});
            }
        }

        return voisins;
    }

    /**
     * Met à jour la direction en fonction du déplacement entre deux positions.
     *
     * @param from Position de départ.
     * @param to   Position d’arrivée.
     * @return La nouvelle direction après le déplacement ("N", "S", "E", "W").
     */
    public String updateDirection(int[] from, int[] to) {
        int dy = to[0] - from[0];
        int dx = to[1] - from[1];

        if (dy == -1 && dx == 0) return "N";
        if (dy == 1 && dx == 0) return "S";
        if (dy == 0 && dx == 1) return "E";
        if (dy == 0 && dx == -1) return "W";

        return "E";
    }

    /**
     * Résolution du labyrinthe selon l’algorithme de la main gauche (version non animée).
     *
     * @param maze Le générateur de labyrinthe.
     * @return La liste des positions visitées constituant le chemin.
     */
    public List<int[]> resLeft(MazeGenerator maze) {
        List<int[]> dejavu = new ArrayList<>();
        List<int[]> atraiter = new ArrayList<>();
        List<String> directions = new ArrayList<>();

        int[][] grid = maze.getMazeGrid(); 
        int height = maze.getHeight();
        int width = maze.getWidth();
        initDistance(maze);
        nbCase = 1;
        nbPath = 1;

        int[] courant = {1, 1};
        int[] arrivee = {height - 2, width - 1};
        dejavu.add(courant);
        atraiter.add(courant);
        directions.add("E");

        while (!atraiter.isEmpty() && !contains(dejavu, arrivee)) {
            courant = atraiter.remove(atraiter.size() - 1);
            String dir = directions.remove(directions.size() - 1);
            List<int[]> voisins = searchNeighborLeft(courant, maze, dir);

            for (int[] voisin : voisins) {
                if (!contains(dejavu, voisin)) {
                    atraiter.add(voisin);
                    directions.add(updateDirection(courant, voisin));
                    dejavu.add(voisin);
                    nbCase++;
                    nbPath++;
                }
            }
        }

        for (int[] pastraiter : atraiter) {
            dejavu.remove(pastraiter);
            nbCase--;
            nbPath--;
        }

        dejavu.add(arrivee);
        return dejavu;
    }

    /**
     * Initialise les distances dans le labyrinthe à zéro (hors murs).
     *
     * @param maze Le générateur de labyrinthe.
     */
    public void initDistance(MazeGenerator maze) {
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
    }

    /**
     * Résolution animée du labyrinthe avec stratégie de la main gauche.
     *
     * @param maze Le générateur de labyrinthe.
     * @return La liste des positions constituant le chemin.
     */
    public List<int[]> resLeftAnimation(MazeGenerator maze) {
        List<int[]> dejavu = new ArrayList<>();
        List<int[]> atraiter = new ArrayList<>();
        List<String> directions = new ArrayList<>();
        Button[][] buttonGrid = maze.getButtonGrid();
        int[][] grid = maze.getMazeGrid(); 
        int height = maze.getHeight();
        int width = maze.getWidth();
        initDistance(maze);
        nbCase = 1;
        nbPath = 1;

        int[] courant = {1, 1};
        int[] arrivee = {height - 2, width - 1};
        dejavu.add(courant);
        atraiter.add(courant);
        directions.add("E");

        while (!atraiter.isEmpty() && !contains(dejavu, arrivee)) {
            courant = atraiter.remove(atraiter.size() - 1);
            String dir = directions.remove(directions.size() - 1);
            List<int[]> voisins = searchNeighborLeft(courant, maze, dir);

            for (int[] voisin : voisins) {
                if (!contains(dejavu, voisin)) {
                    atraiter.add(voisin);
                    directions.add(updateDirection(courant, voisin));
                    dejavu.add(voisin);
                    nbCase++;
                    nbPath++;
                }
            }
        }

        for (int[] pastraiter : atraiter) {
            dejavu.remove(pastraiter);
            nbCase--;
            nbPath--;
        }

        dejavu.add(arrivee);

        Timeline timeline = new Timeline();
        for (int k = 0; k < dejavu.size(); k++) {
            Button btn = buttonGrid[dejavu.get(k)[0]][dejavu.get(k)[1]];
            KeyFrame keyFrame = new KeyFrame(Duration.millis(30 * k), e -> {
                btn.setStyle("-fx-background-color: green;");
            });
            timeline.getKeyFrames().add(keyFrame);
        }
        timeline.play();

        return dejavu;
    }

    /**
     * Réinitialise les couleurs des boutons et distances du labyrinthe.
     *
     * @param maze Le générateur de labyrinthe.
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
     * @param liste   La liste à vérifier.
     * @param element L’élément recherché.
     * @return true si la position est dans la liste, sinon false.
     */
    public boolean contains(List<int[]> liste, int[] element) {
        for (int[] elt : liste) {
            if (elt[0] == element[0] && elt[1] == element[1]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Met en surbrillance le chemin trouvé (non animé).
     *
     * @param maze Le générateur de labyrinthe.
     */
    public void highlightPath(MazeGenerator maze) {
        List<int[]> dejavu = resLeft(maze);
        Button[][] buttonGrid = maze.getButtonGrid();
        for (int[] dejaVu : dejavu) {
            buttonGrid[dejaVu[0]][dejaVu[1]].setStyle("-fx-background-color: green;");
        }
    }
}
