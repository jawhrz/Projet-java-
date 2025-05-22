package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import javafx.scene.image.Image;

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
    
    
    
    public MazeGenerator(int rows, int cols, boolean isPerfect, boolean isProgressive, int Speed,boolean isloaded,int[][] loadedMazeGrid) {
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
        this.Speed=Speed;

       if (!isloaded) {
		initMazeGrid(); // remplacer dans mazegrid les murs par -1 et le reste par un autre nombre (de 1 a nombre de case blanche)
	        collectWalls(); //parcours tout les wall qui ne sont pas des coins 
	        Collections.shuffle(walls); //melange tout les murs pour que ce soit au hasard 
	        collectVallues();
	        Collections.shuffle(values);
	        if (isProgressive) {
	        	displayGrid();
	            generateMazeProgressively(Speed);
	        } else {
	            generateMazeInstantly(); //modifie seulement la mazegrid
	            displayGrid();
	        }
	        mazeGrid[height-2][width-1]=0;
        	mazeGrid[1][0]=0;
        	buttonGrid[height-2][width-1].setStyle("-fx-background-color: white;");
       		buttonGrid[1][0].setStyle("-fx-background-color: green;");
	        
	           
        } else {
            displayGrid(); // Si on charge une grille, il faut juste l’afficher
        }
	

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
                if ((i % 2 == 1 && j % 2 == 0) || (i % 2 == 0 && j % 2 == 1)) {
                    walls.add(new int[]{i, j});
                }
            }
        }
    }

    private void collectVallues() {
        for (int i = 1; i < height - 1; i++) {
            for (int j = 1; j < width - 1; j++) {
                if (mazeGrid[i][j]!=-1) {
                    values.add(new int[]{i, j});
                }
            }
        }
    }
    
    private void generateMazeInstantly() {
        for (int[] wall : walls) {
            tryBreakWall(wall);  // mur cassé
        }

        if (!isPerfect) {
        	Random r = new Random();
        	int a = r.nextInt(2);
        	if(a==0) {
            breakExtraWalls(5);
            }
        	else {
        		addExtraWalls(5);
        	}
        }
    }

    private void generateMazeProgressively(int delay) {// en ms
        Timeline timeline = new Timeline();

        List<int[]> wallsCopy = new ArrayList<>(walls);
        final int[] index = {0};

        KeyFrame keyFrame = new KeyFrame(Duration.millis(delay), event -> {
            if (index[0] < wallsCopy.size()) {
                int[] wall = wallsCopy.get(index[0]);
                int i = wall[0];
                int j = wall[1];

                if (tryBreakWall(wall)) {
                    // Met à jour le mur
                    updateSingleButton(i, j);

                    // Et les deux cellules reliées par le mur
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
                	if(a==0) {
                    breakExtraWalls(5);
                    displayGrid();
                    }
                	else {
                		addExtraWalls(5);
                		displayGrid();
                	}
                }
                timeline.stop();
            }
        });

        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(wallsCopy.size()+wallsCopy.size()/5);
        timeline.play();
    }


    private boolean tryBreakWall(int[] wall) {
        int i = wall[0];
        int j = wall[1];

        if (i % 2 == 1 && j % 2 == 0) { //mur verticale
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

    private void replaceCellValue(int oldId, int newId) {
        for (int i = 1; i < height-1; i ++) {
            for (int j = 1; j < width-1; j ++) {
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
            mazeGrid[wall[0]][wall[1]] = mazeGrid[1][1];
        }
    }

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
    
    
    public void displayGrid() {
        root.getChildren().clear();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Button btn = new Button(); 
                buttonGrid[i][j]=btn;
                btn.setMinSize(1200.0/(height+width),1200.0/(height+width));
                btn.setMaxSize(1200.0/(height+width), 1200.0/(height+width));
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

    private void updateSingleButton(int i, int j) {
        Button btn = new Button();
        buttonGrid[i][j]=btn;
        btn.setMinSize(1200.0/(height+width), 1200.0/(height+width));
        btn.setMaxSize(1200.0/(height+width), 1200.0/(height+width));
        colorButton(btn, mazeGrid[i][j]);

        int x = i;
        int y = j;
        btn.setOnAction(e -> {
            changement(x, y);   //change la valeur de la case en (i,j): devient un mur si c'était une case et inversement
            colorButton(btn, mazeGrid[x][y]);
        });

        root.add(btn, j, i);
    }

    private void changement(int i, int j) {
    	if (i!=0 && j!=0 && i!=height-1 && j!=width-1) {
	    	if (mazeGrid[i][j] == -1) {
	            mazeGrid[i][j] = 0;
	        } else {
	            mazeGrid[i][j] = -1;
	        }
    }}

    public void colorButton(Button btn, int value) { // donne la couleur en fonction
        if (value == -1) {
            btn.setStyle("-fx-background-color: black;");
        } else {
            btn.setStyle("-fx-background-color: white;");
        }
    }

  
    public GridPane getGridPane() {
        return root;
    }
    
    public int[][] getMazeGrid(){
    	return this.mazeGrid;
    }
    
    public int getHeight() {
    	return this.height;
    }
    public int getWidth() {
    	return this.width;
    }
    public Button[][] getButtonGrid(){
    	return this.buttonGrid;
    }
	 public void reset(MazeGenerator maze) {
		int[][] mazeGrid= maze.getMazeGrid();
		Button[][] buttonGrid=maze.getButtonGrid();
		int height = maze.getHeight();
		int width = maze.getWidth();
        int[][] grid = maze.getMazeGrid();
        for (int i = 0; i < maze.getHeight()-1; i++) {
               for (int j = 0; j < maze.getWidth()-1; j++) {
                   if(grid [i] [j]!=-1) {
                       grid [i] [j]=0;
                   }
               }
        }		
		for(int i=0;i<height;i++) {
			for(int j=0;j<width;j++) {
				if(mazeGrid[i][j]!=-1) {
					maze.colorButton(buttonGrid[i][j], 0);
				}
			}
		}
		buttonGrid[1][0].setStyle("-fx-background-color : green");
	}
}