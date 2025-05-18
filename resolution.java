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

public class Resolution {
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

	
	public static void resDistanceSlow(MazeGenerator maze) {
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
	                    pathButtons.add(buttonGrid[a][b]);
	                }
	            }
	        }
	    }
	    
	    Timeline timeline = new Timeline();
	    for (int k = 0; k < pathButtons.size(); k++) {
	        Button btn = pathButtons.get(k);
	        KeyFrame keyFrame = new KeyFrame(Duration.millis(200 * k), e -> {
	            btn.setStyle("-fx-background-color: red;");
	        });
	        timeline.getKeyFrames().add(keyFrame);
	    }

	    // la premeire case est affiché directment
	    buttonGrid[1][1].setStyle("-fx-background-color: red;");

	    timeline.play();
	    
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
	
	
	
	public static void highlightShortestPathAnimation(MazeGenerator maze) {
	    int[][] grid = maze.getMazeGrid();
        Button[][] buttonGrid=maze.getButtonGrid();
        List<Button> pathButtons = new ArrayList<>();
        
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
	                    break;
	                }
	            }
	        }
	    }
	    
	    
	    //pas à pas
	    Timeline timeline = new Timeline();
	    for (int k = 0; k < pathButtons.size(); k++) {
	        Button btn = pathButtons.get(k);
	        KeyFrame keyFrame = new KeyFrame(Duration.millis(200 * k), e -> {
	            btn.setStyle("-fx-background-color: green;");
	        });
	        timeline.getKeyFrames().add(keyFrame);
	    }

	    // la premeire case est affiché directment
	    buttonGrid[1][1].setStyle("-fx-background-color: green;");

	    timeline.play();
	}
	
	
	
	
	
	public static void DFS (MazeGenerator maze) {
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

	            boolean dejavu = false;
	            for (int[] vu : dejaVu) {
	                if (vu[0] == a && vu[1] == b) {
	                    dejavu = true;
	                    break;
	                }
	            }

	            if (!dejavu) {
	                grid[a][b] = distance + 1;
	                dejaVu.add(new int[]{a, b});
	                atraiter.add(new int[]{a, b});
	            }
	        }
	    }
	}
	
	
	public static void resDistanceSlowDFS(MazeGenerator maze) {
		List<Button> pathButtons = new ArrayList<>();
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

	    while (!atraiter.isEmpty()) {
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

	            boolean dejavu = false;
	            for (int[] vu : dejaVu) {
	                if (vu[0] == a && vu[1] == b) {
	                    dejavu = true;
	                    break;
	                }
	            }

	            if (!dejavu) {
	                grid[a][b] = distance + 1;
	                dejaVu.add(new int[]{a, b});
	                atraiter.add(new int[]{a, b});
	                pathButtons.add(buttonGrid[a][b]);
	            }
	        }
	    }
	    
	    Timeline timeline = new Timeline();
	    for (int k = 0; k < pathButtons.size(); k++) {
	        Button btn = pathButtons.get(k);
	        KeyFrame keyFrame = new KeyFrame(Duration.millis(60 * k), e -> {
	            btn.setStyle("-fx-background-color: red;");
	        });
	        timeline.getKeyFrames().add(keyFrame);
	    }

	    // la premeire case est affiché directment
	    buttonGrid[1][1].setStyle("-fx-background-color: red;");

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

    public List<int[]> resRight(MazeGenerator maze) {
        List<int[]> dejavu = new ArrayList<>();
        List<int[]> atraiter = new ArrayList<>();
        List<String> directions = new ArrayList<>();

        int[][] grid = maze.getMazeGrid(); 
        int height = maze.getHeight();
        int width = maze.getWidth();
        initDistance(maze);

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
                }
            }
        }

        return dejavu;
    }

    




    
    public void highlightPathRight(MazeGenerator maze) {
    	List<int[]> dejavu=resRight(maze);    
    	Button[][] buttonGrid=maze.getButtonGrid();
    	for (int[] dejaVu: dejavu) {
    		buttonGrid[dejaVu[0]][dejaVu[1]].setStyle("-fx-background-color: green;");
    	}
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
    


    public List<int[]> resRightAnimation(MazeGenerator maze) {
        List<int[]> dejavu = new ArrayList<>();
        List<int[]> atraiter = new ArrayList<>();
        List<String> directions = new ArrayList<>();
        Button[][] buttonGrid=maze.getButtonGrid();
        int[][] grid = maze.getMazeGrid(); 
        int height = maze.getHeight();
        int width = maze.getWidth();
        initDistance(maze);

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
                }
            }
        }
        for (int[] pastraiter: atraiter) {
        	dejavu.remove(pastraiter);
        }
        dejavu.add(arrivee);
        Timeline timeline= new Timeline();
	    for (int k=0;k<dejavu.size();k++) {
	    	Button btn=buttonGrid[dejavu.get(k)[0]][dejavu.get(k)[1]];
	    	KeyFrame keyFrame= new KeyFrame(Duration.millis(500*k),e-> {
	    		btn.setStyle("-fx-background-color: green;");
	    	});
	    	timeline.getKeyFrames().add(keyFrame);
	    }
	    timeline.play();
        return dejavu;
    }


   }
	
