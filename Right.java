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

public class Right extends Resolution{
	int nbCase=0;
	int nbPath=0;

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
                // droite (E), devant (N), gauche (W), derrière (S)
                // ordre inversé → S, W, N, E
                directions = new int[][]{{1, 0}, {0, -1}, {-1, 0}, {0, 1}};
                break;
            case "E":
                // droite (S), devant (E), gauche (N), derrière (W)
                directions = new int[][]{{0, -1}, {-1, 0}, {0, 1}, {1, 0}};
                break;
            case "S":
                // droite (W), devant (S), gauche (E), derrière (N)
                directions = new int[][]{{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
                break;
            case "W":
                // droite (N), devant (W), gauche (S), derrière (E)
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
	
	 public String updateDirection(int[] from, int[] to) {
	        int dy = to[0] - from[0];
	        int dx = to[1] - from[1];

	        if (dy == -1 && dx == 0) return "N";
	        if (dy == 1 && dx == 0) return "S";
	        if (dy == 0 && dx == 1) return "E";
	        if (dy == 0 && dx == -1) return "W";

	        return "E"; // par défaut
	    }

	   public List<int[]> resRight(MazeGenerator maze) {
	        List<int[]> dejavu = new ArrayList<>();
	        List<int[]> atraiter = new ArrayList<>();
	        List<String> directions = new ArrayList<>();
	        
	        int[][] grid = maze.getMazeGrid(); 
	        int height = maze.getHeight();
	        int width = maze.getWidth();
	        initDistance(maze);
	        nbCase=1;
	        nbPath=1;
	        int[] courant = {1, 1};
	        int[] arrivee = {height - 2, width - 2};
	        dejavu.add(courant);
	        atraiter.add(courant);
	        directions.add("E"); // départ vers la droite

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

	        return dejavu;
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
	   
	    public List<int[]> resRightAnimation(MazeGenerator maze) {
	        List<int[]> dejavu = new ArrayList<>();
	        List<int[]> atraiter = new ArrayList<>();
	        List<String> directions = new ArrayList<>();
	        Button[][] buttonGrid=maze.getButtonGrid();
	        int[][] grid = maze.getMazeGrid(); 
	        int height = maze.getHeight();
	        int width = maze.getWidth();
	        initDistance(maze);
	        nbCase=1;
	        nbPath=1;
	        int[] courant = {1, 1};
	        int[] arrivee = {height - 2, width - 2};
	        dejavu.add(courant);
	        atraiter.add(courant);
	        directions.add("E"); // départ vers la droite

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
	        Timeline timeline= new Timeline();
		    for (int k=0;k<dejavu.size();k++) {
		    	Button btn=buttonGrid[dejavu.get(k)[0]][dejavu.get(k)[1]];
		    	KeyFrame keyFrame= new KeyFrame(Duration.millis(30*k),e-> {
		    		btn.setStyle("-fx-background-color: green;");
		    	});
		    	timeline.getKeyFrames().add(keyFrame);
		    }
		    timeline.play();
	        return dejavu;
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
	    
	    public void highlightPath(MazeGenerator maze) {
	    	List<int[]> dejavu=resRight(maze);    
	    	Button[][] buttonGrid=maze.getButtonGrid();
	    	for (int[] dejaVu: dejavu) {
	    		buttonGrid[dejaVu[0]][dejaVu[1]].setStyle("-fx-background-color: green;");
	    	}
	    }
}
