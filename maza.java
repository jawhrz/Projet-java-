package application;
	
import java.util.*;
import javafx.util.*;
import javafx.application.Application;
import javafx.animation.*;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.control.*;

public class Maze extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			
			int[][] value = new int[37][65];
			GridPane grid = new GridPane();
			
			for (int i =0;i<65;i++) {
				ColumnConstraints col = new ColumnConstraints(23);
				grid.getColumnConstraints().add(col);
			}
			for (int j =0;j<37;j++) {
				RowConstraints row = new RowConstraints(21);
				grid.getRowConstraints().add(row);
			}
			
			for (int row = 0; row < 37; row++) {
			    for (int col = 0; col < 65; col++) {
			    	Button btn = new Button();
		            btn.setPrefSize(23, 21);
		            btn.setStyle("-fx-background-color: black;");
		            value[row][col] = -1;
		            grid.add(btn, col, row);
			    }
			}
			

	        int compteur = 0;
			
			for (int row = 1; row < 37; row+=2) {
			    for (int col = 1; col < 65; col+=2) {
			    	Button btn = new Button();
		            btn.setPrefSize(23, 21);
		            btn.setStyle("-fx-background-color: blue;");
		            value[row][col] = compteur;
		            grid.add(btn, col, row);
		            compteur++;
			    }
			}
			
			generationMazeCompleteWithoutSol(value, grid);

			//grid.setGridLinesVisible(true); // pour voir les lignes de la grille
			
			

			Scene scene = new Scene(grid,800,600);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// condition de fin : avoir que 2 valeurds dans la matrice -1 et une autre
	public boolean endCondition(int[][] mat) {
	    Set<Integer> values = new HashSet<>();

	    for (int i = 0; i < mat.length; i++) {
	        for (int j = 0; j < mat[0].length; j++) {
	            if (mat[i][j] != -1) {
	                values.add(mat[i][j]);
	                if (values.size() > 2) {
	                    return true; 
	                }
	            }
	        }
	    }

	    return false; 
	}
	
	
	public void changerCouleurDeCase(GridPane grille, int ligneVoulue, int colonneVoulue, String couleur) {
	    for (Node caseDansLaGrille : grille.getChildren()) {
	        Integer ligneCase = GridPane.getRowIndex(caseDansLaGrille);
	        Integer colonneCase = GridPane.getColumnIndex(caseDansLaGrille);

	        if (ligneCase == null) {
	        	ligneCase = 0;
	        }
	        if (colonneCase == null) {
	        	colonneCase = 0;
	        }

	        if (ligneCase == ligneVoulue && colonneCase == colonneVoulue) {
	            if (caseDansLaGrille instanceof Button) {
	                Button bouton = (Button) caseDansLaGrille;
	                bouton.setStyle("-fx-background-color: " + couleur + ";");
	            }
	            break;
	        }
	    }
	}

	
	// choisir la couleur
	/*public void changerCouleurDeCase(GridPane grille, int ligneVoulue, int colonneVoulue, String couleur) {
	    // Parcourir toutes les cases (nœuds) de la grille
	    for (Node caseDansLaGrille : grille.getChildren()) {

	        // Obtenir la position de cette case
	        Integer ligneCase = GridPane.getRowIndex(caseDansLaGrille);
	        Integer colonneCase = GridPane.getColumnIndex(caseDansLaGrille);

	        // Cas : aucune ligne/colonne n'est définie
	        if (ligneCase == null) ligneCase = 0;
	        if (colonneCase == null) colonneCase = 0;

	        // Si on a trouvé la case à modifier
	        if (ligneCase == ligneVoulue && colonneCase == colonneVoulue) {
	            // Créer un nouveau bouton avec la même taille que les cases existantes
	            Button bouton = new Button();
	            bouton.setPrefSize(23, 21);  // Taille du bouton
	            bouton.setStyle("-fx-background-color: " + couleur + ";"); // Appliquer la couleur

	            // Remplacer l'ancien bouton (ou le placeholder) par le nouveau bouton
	            grille.add(bouton, colonneCase, ligneCase);
	            break;
	        }
	    }
	}
*/

	public void generationMazeCompleteParfait(int[][] values, GridPane grid) {
	    Random rand = new Random();

	    while (endCondition(values)) {
	        int row = (rand.nextInt(18) * 2) + 1; // valeurs impaires entre 1 et 35
	        int col = (rand.nextInt(32) * 2) + 1; // valeurs impaires entre 1 et 63

	        List<int[]> directions = new ArrayList<>(Arrays.asList(
	        	    new int[]{0, 2},
	        	    new int[]{0, -2},
	        	    new int[]{2, 0},
	        	    new int[]{-2, 0}
	        	));


	        Collections.shuffle(directions);

	        for (int[] dir : directions) {
	            int newRow = row + dir[0];
	            int newCol = col + dir[1];

	            if (newRow > 0 && newRow < 37 && newCol > 0 && newCol < 65) {
	                if (values[row][col] != values[newRow][newCol]) {
	                    // casser le mur 
	                    int wallRow = row + dir[0] / 2;
	                    int wallCol = col + dir[1] / 2;

	                    int oldValue = values[newRow][newCol];
	                    int newValue = values[row][col];


	                    values[wallRow][wallCol] = newValue;
	                    changerCouleurDeCase(grid, wallRow, wallCol, "blue");

	                    // changer tous les oldValue en newValue
	                    for (int i = 0; i < 37; i++) {
	                        for (int j = 0; j < 65; j++) {
	                            if (values[i][j] == oldValue) {
	                                values[i][j] = newValue;
	                                changerCouleurDeCase(grid, i, j, "blue");
	                            }
	                        }
	                    }

	                    break; // on casse un mur à la fois
	                }
	            }
	        }
	    }
	}
	
	public void generationMazeCompletePasParfait(int[][] values, GridPane grid) {
	    Random rand = new Random();

	    while (endCondition(values)) {
	        int row = (rand.nextInt(18) * 2) + 1; // valeurs impaires entre 1 et 35
	        int col = (rand.nextInt(32) * 2) + 1; // valeurs impaires entre 1 et 63

	        List<int[]> directions = new ArrayList<>(Arrays.asList(
	        	    new int[]{0, 2},
	        	    new int[]{0, -2},
	        	    new int[]{2, 0},
	        	    new int[]{-2, 0}
	        	));


	        Collections.shuffle(directions);

	        for (int[] dir : directions) {
	            int newRow = row + dir[0];
	            int newCol = col + dir[1];

	            if (newRow > 0 && newRow < 37 && newCol > 0 && newCol < 65) {
	                if (values[row][col] != values[newRow][newCol]) {
	                    // casser le mur 
	                    int wallRow = row + dir[0] / 2;
	                    int wallCol = col + dir[1] / 2;

	                    int oldValue = values[newRow][newCol];
	                    int newValue = values[row][col];


	                    values[wallRow][wallCol] = newValue;
	                    changerCouleurDeCase(grid, wallRow, wallCol, "blue");

	                    // changer tous les oldValue en newValue
	                    for (int i = 0; i < 37; i++) {
	                        for (int j = 0; j < 65; j++) {
	                            if (values[i][j] == oldValue) {
	                                values[i][j] = newValue;
	                                changerCouleurDeCase(grid, i, j, "blue");
	                            }
	                        }
	                    }

	                    break; // on casse un mur à la fois
	                }
	            }
	        }
	    }

	    int value=values[1][1];
	    
	    int c = 0;
	    ArrayList<int[]> tabIndex = new ArrayList<>();

	    // Ajouter les indices des cases avec la valeur -1
	    for (int i = 1; i < 35; i++) {
	        for (int j = 1; j < 63; j++) {
	            if (values[i][j] == -1) {
	                c += 1;
	                tabIndex.add(new int[]{i, j});
	            }
	        }
	    }

	    // Si on a des cases à changer
	    if (c > 0) {
	        Collections.shuffle(tabIndex); // Mélanger les murs disponibles

	        int mursACasser;
	        if (c >= 30) {
	            mursACasser = 30;  // On limite à 10 si on en a assez
	        } else {
	            mursACasser = c;   // Sinon, on prend ce qu'on a
	        }

	        for (int k = 0; k < mursACasser; k++) {
	            int newRow = tabIndex.get(k)[0];
	            int newCol = tabIndex.get(k)[1];

	            changerCouleurDeCase(grid, newRow, newCol, "blue");
	            values[newRow][newCol] = value;
	        }
	    }
	}
	
	
	
	public void generationMazeCompleteWithoutSol(int[][] values, GridPane grid) {
	    Random rand = new Random();

	    while (endCondition(values)) {
	        int row = (rand.nextInt(18) * 2) + 1; // valeurs impaires entre 1 et 35
	        int col = (rand.nextInt(32) * 2) + 1; // valeurs impaires entre 1 et 63

	        List<int[]> directions = new ArrayList<>(Arrays.asList(
	        	    new int[]{0, 2},
	        	    new int[]{0, -2},
	        	    new int[]{2, 0},
	        	    new int[]{-2, 0}
	        	));


	        Collections.shuffle(directions);

	        for (int[] dir : directions) {
	            int newRow = row + dir[0];
	            int newCol = col + dir[1];

	            if (newRow > 0 && newRow < 37 && newCol > 0 && newCol < 65) {
	                if (values[row][col] != values[newRow][newCol]) {
	                    // casser le mur 
	                    int wallRow = row + dir[0] / 2;
	                    int wallCol = col + dir[1] / 2;

	                    int oldValue = values[newRow][newCol];
	                    int newValue = values[row][col];


	                    values[wallRow][wallCol] = newValue;
	                    changerCouleurDeCase(grid, wallRow, wallCol, "blue");

	                    // changer tous les oldValue en newValue
	                    for (int i = 0; i < 37; i++) {
	                        for (int j = 0; j < 65; j++) {
	                            if (values[i][j] == oldValue) {
	                                values[i][j] = newValue;
	                                changerCouleurDeCase(grid, i, j, "blue");
	                            }
	                        }
	                    }

	                    break; // on casse un mur à la fois
	                }
	            }
	        }
	    }

	    int value=values[1][1];
	    
	    int c = 0;
	    ArrayList<int[]> tabIndex = new ArrayList<>();

	    // Ajouter les indices des cases avec la valeur -1
	    for (int i = 1; i < 35; i++) {
	        for (int j = 1; j < 63; j++) {
	            if (values[i][j] == value) {
	                c += 1;
	                tabIndex.add(new int[]{i, j});
	            }
	        }
	    }

	    // Si on a des cases à changer
	    if (c > 0) {
	        Collections.shuffle(tabIndex); // Mélanger les murs disponibles

	        int mursAajouter;
	        if (c >= 30) {
	        	mursAajouter = 30;  // On limite à 10 si on en a assez
	        } else {
	        	mursAajouter = c;   // Sinon, on prend ce qu'on a
	        }

	        for (int k = 0; k < mursAajouter; k++) {
	            int newRow = tabIndex.get(k)[0];
	            int newCol = tabIndex.get(k)[1];

	            changerCouleurDeCase(grid, newRow, newCol, "black");
	            values[newRow][newCol] = -1;
	        }
	    }
	}
	    
	

	/*
	public void generationMaze(int[][] values) {
		for (int row = 1; row < 36; row++) {
		    for (int col = 1; col < 64; col++) {
		    	while(endCondition(values)) {
			    	Random rand = new Random();
			    	int n1 = (rand.nextInt(35/2)*2)+2; 
			    	int n2 = (rand.nextInt(63/2)*2)+2; 
			    	if (values[n1][n2]!= -1) {
			    		if (values[n1+1][n2]== -1) {
			    			values[n1][n2]=values[n1][n2+1];
			    			values[n1][n2-1]=values[n1][n2+1];
			    		}
			    		else {
			    			values[n1][n2]=values[n1][n2+1];
			    			values[n1][n2-1]=values[n1][n2+1];
			    		}
			    	}
			    }
		    }
		}
		
	}
	*/
	public static void main(String[] args) {
		launch(args);
	}
}
