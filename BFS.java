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

public class BFS extends ResolutionFS{
	
	int nbCase=0;
	int nbPath=0;

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
	
	public void bfs(MazeGenerator maze) {
		nbCase=1;
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
	                if (!contains(dejavu,neighbor)) {
	                    grid[a][b] = distance;
	                    atraiter.add(new int[]{a, b});
	                    dejavu.add(new int[]{a, b});
	                    nbCase++;
	                }
	            }
	        }
	    }
	}
	
	public void bfsAnimation(MazeGenerator maze) {
		nbCase=1;
	    int height = maze.getHeight();
	    int width = maze.getWidth();
	    int[][] grid = maze.getMazeGrid();
	    initDistance(maze);

	    int distance = 1;
	    List<int[]> dejavu = new ArrayList<>();
	    List<int[]> atraiter = new ArrayList<>();
	    List<Button> pathButtons = new ArrayList<>();
	    Button[][] buttonGrid=maze.getButtonGrid();

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
	                
	                if (!contains(dejavu,neighbor)) {
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
	        KeyFrame keyFrame = new KeyFrame(Duration.millis(30 * k), e -> {
	            btn.setStyle("-fx-background-color: red;");
	        });
	        timeline.getKeyFrames().add(keyFrame);
	    }

	    

	    timeline.play();
	    
	}
	
	public void highlightPath(MazeGenerator maze) {
		nbPath=1;
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
	                    nbPath++;
	                    break;
	                }
	            }
	        }
	    }
	}
	
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
