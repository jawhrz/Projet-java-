
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

/**
 * Classe DFS - Implémente l'algorithme de parcours en profondeur (Depth-First Search)
 * pour résoudre un labyrinthe et visualiser son exécution avec des animations.
 * 
 * @author Jawad Harizi
 * @author Cherf Noam
 */

public class DFS extends ResolutionFS{
	
	int nbCase=0;
	int nbPath=0;
	
	/**
	 * Recherche les voisins valides (non-mur et dans les limites) d'une position donnée dans le labyrinthe.
	 *
	 * @param position La position actuelle sous forme de tableau [ligne, colonne].
	 * @param maze Le générateur de labyrinthe.
	 * @return Une liste de tableaux représentant les positions voisines accessibles.
	 */
	
	public List<int[]> searchNeighbor(int[] position, MazeGenerator maze) {
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
	
	/**
	 * Initialise la grille du labyrinthe en mettant à zéro toutes les cases accessibles.
	 *
	 * @param maze Le générateur de labyrinthe.
	 */
	
	public void initDistance(MazeGenerator maze) {
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
	
	/**
	 * Exécute l'algorithme de recherche en profondeur (DFS) pour explorer le labyrinthe.
	 * Met à jour la grille avec les distances depuis le point de départ.
	 *
	 * @param maze Le générateur de labyrinthe.
	 */
	
	public void DFS(MazeGenerator maze) {
		nbCase=1;
	    List<int[]> dejaVu = new ArrayList<>();
	    List<int[]> atraiter = new ArrayList<>();
	    int[][] grid = maze.getMazeGrid(); 
	    int height = maze.getHeight();
	    int width = maze.getWidth();
	    initDistance(maze);
	    Button[][] buttonGrid = maze.getButtonGrid();

	    int[] start = new int[] {height - 2, width - 1};
	    atraiter.add(start);
	    dejaVu.add(start);
	    grid[start[0]][start[1]] = 1;

	    while (grid[1][1] == 0 && !atraiter.isEmpty()) {
	        // on prend le dernier ajouté
	        int[] position = atraiter.remove(atraiter.size() - 1);
	        int x = position[0];
	        int y = position[1]; 
	        int distance = grid[x][y];

	        
	        for (int[] neighbor : searchNeighbor(position, maze)) {
	            int a = neighbor[0];
	            int b = neighbor[1];

	            if (!contains(dejaVu,neighbor)) {
	                grid[a][b] = distance + 1;
	                dejaVu.add(new int[]{a, b});
	                atraiter.add(new int[]{a, b});
	                nbCase++;
	            }
	        }
	    }
	}
	
	/**
	 * Exécute une version de DFS avec animation de l'exploration du labyrinthe.
	 * Colore progressivement les cases explorées en rouge.
	 *
	 * @param maze Le générateur de labyrinthe.
	 */
	
	public void resDistanceSlowDFS(MazeGenerator maze) {
		List<Button> pathButtons = new ArrayList<>();
		List<int[]> dejaVu = new ArrayList<>();
	    List<int[]> atraiter = new ArrayList<>();
	    int[][] grid = maze.getMazeGrid(); 
	    int height = maze.getHeight();
	    int width = maze.getWidth();
	    initDistance(maze);
	    Button[][] buttonGrid = maze.getButtonGrid();
	    nbCase=1;
	    int[] start = new int[] {height - 2, width - 1};
	    atraiter.add(start);
	    dejaVu.add(start);
	    grid[start[0]][start[1]] = 1;

	    while (!atraiter.isEmpty() && grid[1][1]==0) {
	        // on prend le dernier ajouté
	        int[] position = atraiter.remove(atraiter.size() - 1);
	        int x = position[0];
	        int y = position[1]; 
	        int distance = grid[x][y];

	        if (x == 0 && y == 0) {
	            // si on arrive au debut on arrete
	            break;
	        }

	        for (int[] neighbor : searchNeighbor(position, maze)) {
	            int a = neighbor[0];
	            int b = neighbor[1];


	            if (!contains(dejaVu,neighbor)) {
	                grid[a][b] = distance + 1;
	                dejaVu.add(new int[]{a, b});
	                atraiter.add(new int[]{a, b});
	                pathButtons.add(buttonGrid[a][b]);
	                nbCase++;
	            }
	        }
	    }
	    
	    Timeline timeline = new Timeline();
	    for (int k = 0; k < pathButtons.size(); k++) {
	        Button btn = pathButtons.get(k);
	        KeyFrame keyFrame = new KeyFrame(Duration.millis(30 * k), e -> {
	            btn.setStyle("-fx-background-color: red;");
	        });
	        timeline.getKeyFrames().add(keyFrame);
	    }

	    

	    timeline.play();
	    
	}
	
	/**
	 * Met en surbrillance le chemin trouvé entre la sortie et l'entrée du labyrinthe
	 * après l'exécution de l'algorithme, sans animation.
	 *
	 * @param maze Le générateur de labyrinthe.
	 */
	
	public void highlightPath(MazeGenerator maze) {
	    int[][] grid = maze.getMazeGrid();
        Button[][] buttonGrid=maze.getButtonGrid();
        nbPath=1;
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
	                    nbPath++;
	                    break;
	                }
	            }
	        }
	    }
	}
	
	/**
	 * Anime la mise en surbrillance du chemin trouvé, en vert, de manière progressive.
	 *
	 * @param maze Le générateur de labyrinthe.
	 */
	
	
	public void highlightPathAnimation(MazeGenerator maze) {
	    int[][] grid = maze.getMazeGrid();
        Button[][] buttonGrid=maze.getButtonGrid();
        List<Button> pathButtons = new ArrayList<>();
        nbPath=1;
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
	                    pathButtons.add(buttonGrid[i][j]);
	                    distance--;
	                    nbPath++;
	                    break;
	                }
	            }
	        }
	    }
	    
	    
	    //pas à pas
	    Timeline timeline = new Timeline();
	    for (int k = 0; k < pathButtons.size(); k++) {
	        Button btn = pathButtons.get(k);
	        KeyFrame keyFrame = new KeyFrame(Duration.millis(30 * k), e -> {
	            btn.setStyle("-fx-background-color: green;");
	        });
	        timeline.getKeyFrames().add(keyFrame);
	    }

	  
	    timeline.play();
	}
	
	/**
	 * Réinitialise l'état visuel et logique du labyrinthe à sa configuration d'origine.
	 *
	 * @param maze Le générateur de labyrinthe.
	 */

	public void reset(MazeGenerator maze) {
		int[][] mazeGrid= maze.getMazeGrid();
		Button[][] buttonGrid=maze.getButtonGrid();
		int height = maze.getHeight();
		int width = maze.getWidth();
		initDistance(maze);
		
		for(int i=0;i<height;i++) {
			for(int j=0;j<width;j++) {
				if(mazeGrid[i][j]!=-1) {
					maze.colorButton(buttonGrid[i][j], 0);
				}
			}
		}
		
	}
	
	/**
	 * Vérifie si une position (int[]) est déjà contenue dans une liste de positions.
	 *
	 * @param liste La liste de positions déjà visitées.
	 * @param element La position à vérifier.
	 * @return true si la position est déjà dans la liste, false sinon.
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
	    
}
