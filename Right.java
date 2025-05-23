package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import java.util.*;

/**
 * Classe Right représentant une stratégie de résolution de labyrinthe
 * en suivant le mur de droite. Hérite de la classe abstraite Resolution.
 * @author Jawad Harizi, @author Cherf Noam
 */
public class Right extends Resolution {

    /** Nombre total de cases explorées durant l'algorithme. */
    int nbCase = 0;

    /** Nombre de cases faisant partie du chemin final. */
    int nbPath = 0;

    /**
     * Recherche les voisins d'une case selon l'ordre imposé
     * par l'algorithme qui suit le mur de droite.
     *
     * @param position La position actuelle [y, x]
     * @param maze Le labyrinthe
     * @param direction La direction actuelle ("N", "E", "S", "W")
     * @return Liste des positions voisines accessibles
     */
    public List<int[]> searchNeighborRight(int[] position, MazeGenerator maze, String direction) {
        List<int[]> voisins = new ArrayList<>();
        int[][] grid = maze.getMazeGrid();
        int y = position[0];
        int x = position[1];
        int height = grid.length;
        int width = grid[0].length;

        // Ordre inversé pour DFS (derrière, gauche, devant, droite)
        int[][] directions;

        switch (direction) {
            case "N":
                directions = new int[][]{{1, 0}, {0, -1}, {-1, 0}, {0, 1}};
                break;
            case "E":
                directions = new int[][]{{0, -1}, {-1, 0}, {0, 1}, {1, 0}};
                break;
            case "S":
                directions = new int[][]{{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
                break;
            case "W":
                directions = new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
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
     * @param from Position d'origine
     * @param to Position de destination
     * @return Direction sous forme de chaîne ("N", "S", "E", "W")
     */
    public String updateDirection(int[] from, int[] to) {
        int dy = to[0] - from[0];
        int dx = to[1] - from[1];

        if (dy == -1 && dx == 0) return "N";
        if (dy == 1 && dx == 0) return "S";
        if (dy == 0 && dx == 1) return "E";
        if (dy == 0 && dx == -1) return "W";

        return "E"; // par défaut
    }

    /**
     * Résout le labyrinthe avec l'algorithme du mur de droite.
     *
     * @param maze Le labyrinthe à résoudre
     * @return Liste du chemin trouvé
     */
    public List<int[]> resRight(MazeGenerator maze) {
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
            List<int[]> voisins = searchNeighborRight(courant, maze, dir);

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
        for (int[] pastraiter: atraiter) {
            dejavu.remove(pastraiter);
            nbCase--;
            nbPath--;
        }
        dejavu.add(arrivee);

        return dejavu;
    }

    /**
     * Initialise les distances du labyrinthe à 0 (hors murs).
     *
     * @param maze Le labyrinthe à initialiser
     */
    public void initDistance(MazeGenerator maze) {
        int height = maze.getHeight();
        int width = maze.getWidth();
        int[][] grid = maze.getMazeGrid();
        for (int i = 0; i < maze.getHeight()-1; i++) {
            for (int j = 0; j < maze.getWidth()-1; j++) {
                if (grid[i][j] != -1) {
                    grid[i][j] = 0;
                }
            }
        }
    }

    /**
     * Résout le labyrinthe avec animation visuelle.
     *
     * @param maze Le labyrinthe à résoudre
     * @return Liste des cases du chemin
     */
    public List<int[]> resRightAnimation(MazeGenerator maze) {
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
            List<int[]> voisins = searchNeighborRight(courant, maze, dir);

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
        for (int[] pastraiter: atraiter) {
            dejavu.remove(pastraiter);
            nbCase--;
            nbPath--;
        }
        dejavu.add(arrivee);
        Timeline timeline = new Timeline();
        for (int k = 0; k < dejavu.size(); k++) {
            Button btn = buttonGrid[dejavu.get(k)[0]][dejavu.get(k)[1]];
            KeyFrame keyFrame = new KeyFrame(Duration.millis((3000 * k)/(height+width)), e -> {
                btn.setStyle("-fx-background-color: green;");
            });
            timeline.getKeyFrames().add(keyFrame);
        }
        timeline.play();
        return dejavu;
    }

    /**
     * Réinitialise le labyrinthe et l'affichage des cases.
     *
     * @param maze Le labyrinthe à réinitialiser
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
        buttonGrid[1][0].setStyle("-fx-background-color : green");
    }

    /**
     * Vérifie si un élément est contenu dans une liste de positions.
     *
     * @param liste Liste de positions
     * @param element Élément recherché
     * @return true si trouvé, false sinon
     */
    public boolean contains(List<int[]> liste, int[] element) {
        boolean dejaVu = false;
        for (int[] elt : liste) {
            if (elt[0] == element[0] && elt[1] == element[1]) {
                dejaVu = true;
                return dejaVu;
            }
        }  
        return dejaVu;
    }

    /**
     * Colore le chemin trouvé en vert sur l'affichage du labyrinthe.
     *
     * @param maze Le labyrinthe à modifier
     */
    public void highlightPath(MazeGenerator maze) {
        List<int[]> dejavu = resRight(maze);    
        Button[][] buttonGrid = maze.getButtonGrid();
        for (int[] dejaVu : dejavu) {
            buttonGrid[dejaVu[0]][dejaVu[1]].setStyle("-fx-background-color: green;");
        }
    }
}
	