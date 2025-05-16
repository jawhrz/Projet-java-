package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import java.util.*;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class resolution {
	  	/*List<int[]> dejavu=new ArrayList<>();
	    List<int[]> atraiter=new ArrayList<>();
	    
	    private void DFS(MazeGenerator maze,List<int[]>atraiter,List<int[]> dejavu, int[] depart) {
	    	dejavu.add(depart);
	    	atraiter.add(depart);
	    }*/
	    
	public static List<int[]> searchNeighbor(int[] position, MazeGenerator maze) {
	    List<int[]> voisins = new ArrayList<>();
	    int[][] mazegrid = maze.getMazeGrid();
	    int ligne = position[0];
	    int colonne = position[1];
	    int height = mazegrid.length;
	    int width = mazegrid[0].length;

	    // voisin a droite
	    if (colonne + 1 < width && mazegrid[ligne][colonne + 1] != -1) {
	        voisins.add(new int[]{ligne, colonne + 1});
	    }
	    // voisin d'en bas
	    if (ligne + 1 < height && mazegrid[ligne + 1][colonne] != -1) {
	        voisins.add(new int[]{ligne + 1, colonne});
	    }
	    // voisin de gauche
	    if (colonne - 1 >= 0 && mazegrid[ligne][colonne - 1] != -1) {
	        voisins.add(new int[]{ligne, colonne - 1});
	    }
	    // voisin a gauche
	    if (ligne - 1 >= 0 && mazegrid[ligne - 1][colonne] != -1) {
	        voisins.add(new int[]{ligne - 1, colonne});
	    }

	    return voisins;
	}
	
	public static void initDistance(MazeGenerator maze) {
		int height=maze.getHeight();
		int width=maze.getWidth();
        int[][] grid = maze.getMazeGrid();
        for (int i = 0; i < maze.getHeight()-1; i++) {
               for (int j = 0; j < maze.getWidth()-1; j++) {
                   if(grid [i] [j]!=-1) {
                       grid [i] [j]=0;
                   }
               }
        }
	}

	public static void resDistance(MazeGenerator maze) {
	    int height = maze.getHeight();
	    int width = maze.getWidth();
	    int[][] grid = maze.getMazeGrid();
	    initDistance(maze);

	    int distance = 1;
	    List<int[]> dejavu = new ArrayList<>();
	    List<int[]> atraiter = new ArrayList<>();

	    int[] position = {height - 2, width - 1}; // cellule de sortie
	    grid[position[0]][position[1]] = distance;
	    atraiter.add(position);
	    dejavu.add(position);

	    while (grid[1][1] == 0 && !atraiter.isEmpty()) {
	        int size = atraiter.size();
	        distance++; // on augmente la distance à chaque "couche"
	        
	        for (int k = 0; k < size; k++) {
	            int[] courant = atraiter.remove(0);

	            for (int[] neighbor : searchNeighbor(courant, maze)) {
	                int a = neighbor[0];
	                int b = neighbor[1];

	                // Vérification manuelle pour éviter le problème des références d'objets
	                boolean dejaVu = false;
	                for (int[] vu : dejavu) {
	                    if (vu[0] == a && vu[1] == b) {
	                        dejaVu = true;
	                        break;
	                    }
	                }

	                if (!dejaVu) {
	                    grid[a][b] = distance;
	                    atraiter.add(new int[]{a, b});
	                    dejavu.add(new int[]{a, b});
	                }
	            }
	        }
	    }
	}

	public static void highlightShortestPath(MazeGenerator maze) {
	    int[][] grid = maze.getMazeGrid();
        Button[][] buttonGrid=maze.getButtonGrid();
	    int i = 1;
	    int j = 1;
	    int distance = grid[i][j];
	    Button btn1 = buttonGrid[1][1]; 
        btn1.setStyle("-fx-background-color: green;");
	    while (distance > 1) {
	        for (int[] dir : new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}}) {
	            int ni = i + dir[0];
	            int nj = j + dir[1];

	            if (ni >= 0 && nj >= 0 && ni < maze.getHeight() && nj < maze.getWidth()) {
	                if (grid[ni][nj] == distance - 1) {
	                    i = ni;
	                    j = nj;
	                    Button btn = buttonGrid[i][j]; 
	                    btn.setStyle("-fx-background-color: green;");
	                    distance--;
	                    break;
	                }
	            }
	        }
	    }
	}


   }
	

