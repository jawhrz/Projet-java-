package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import java.io.File;
import java.util.*;

public class Main extends Application {
			
	Button buttonBfs = new Button("BFS");
	Button buttonBfsAnimation = new Button("BFS pas à pas");
	Button showUnderGroundBFS = new Button("Bouttons traités");
	
	Button buttonDfs = new Button("DFS");
	Button buttonAnimationDfs = new Button("DFS pas à pas");
	Button showUnderGroundDFS = new Button("Bouttons traités DFS");

	Button buttonRight=new Button("réso par droite");
	Button buttonRightAnimation= new Button ("réso droite animé");
	
	Button reset= new Button("reset");
	BorderPane root;
	private MazeGenerator currentMaze;
	private BFS bfs = new BFS();
	private DFS dfs = new DFS();
	private Right right = new Right();
	
    @Override
    public void start(Stage primaryStage) {
        root = new BorderPane();

        Label nbCasetraite= new Label("nombre case traitée:");
        Label nbCasePath= new Label("nombre case chemin" );
        
        TextField widthField = new TextField();
        TextField heightField = new TextField();
        TextField speed = new TextField();

        Button perfectFast = new Button("Parfait - Complet");
        Button perfectSlow = new Button("Parfait - Pas à pas");
        Button imperfectFast = new Button("Imparfait - Complet");
        Button imperfectSlow = new Button("Imparfait - Pas à pas");
        Button saveMaze = new Button("Sauvegarder");
        Button loadMaze = new Button("Charger");
        HBox hiddenButton = new HBox(10);


        buttonRightAnimation.setVisible(false);
        buttonRight.setVisible(false);
        buttonBfs.setVisible(false); // bouton caché
        buttonBfsAnimation.setVisible(false);
        showUnderGroundBFS.setVisible(false);
        buttonBfs.setOnAction(e -> {
        	if (currentMaze != null) {
                bfs.bfs(currentMaze); 

                // on vérifie la case départ (1,1) 
                if (currentMaze.getMazeGrid()[1][1] != 0) {
                    bfs.highlightPath(currentMaze);
                    nbCasetraite.setText("nombre case traitée:" + bfs.nbCase);
                    nbCasePath.setText("nombre case chemin" + bfs.nbPath);
                } else {
                    // Pas de solution trouvée => affichage message
                    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                    alert.setTitle("Résultat");
                    alert.setHeaderText(null);
                    alert.setContentText("Aucune solution trouvée pour ce labyrinthe.");
                    alert.showAndWait();
                }
            }
        });
        buttonBfsAnimation.setOnAction(e -> {
            if (currentMaze != null) {
                bfs.bfs(currentMaze);
                bfs.highlightPathAnimation(currentMaze);
                nbCasetraite.setText("nombre case traitée:"+bfs.nbCase);
                nbCasePath.setText("nombre case chemin" +bfs.nbPath);
                if (currentMaze.getMazeGrid()[1][1] != 0) {
                    bfs.highlightPath(currentMaze);
                    nbCasetraite.setText("nombre case traitée:" + bfs.nbCase);
                    nbCasePath.setText("nombre case chemin" + bfs.nbPath);
                } else {
                    // Pas de solution trouvée => affichage message
                    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                    alert.setTitle("Résultat");
                    alert.setHeaderText(null);
                    alert.setContentText("Aucune solution trouvée pour ce labyrinthe.");
                    alert.showAndWait();
                }
            }
        });
        
        showUnderGroundBFS.setOnAction(e -> {
            if (currentMaze != null) {
                bfs.bfsAnimation(currentMaze);
                nbCasetraite.setText("nombre case traitée:"+bfs.nbCase);
                nbCasePath.setText("nombre case chemin" +bfs.nbPath);
            }
        });
        
        buttonDfs.setVisible(false); // bouton caché
        buttonAnimationDfs.setVisible(false);
        showUnderGroundDFS.setVisible(false);
        
        buttonDfs.setOnAction(e -> {
            if (currentMaze != null) {
                dfs.DFS(currentMaze);
                if (currentMaze.getMazeGrid()[1][1] != 0) {
                    dfs.highlightPath(currentMaze);
                    nbCasetraite.setText("nombre case traitée:" + dfs.nbCase);
                    nbCasePath.setText("nombre case chemin" + bfs.nbPath);
                } else {
                    // Pas de solution trouvée => affichage message
                    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                    alert.setTitle("Résultat");
                    alert.setHeaderText(null);
                    alert.setContentText("Aucune solution trouvée pour ce labyrinthe.");
                    alert.showAndWait();
                }
            }
        });
        buttonAnimationDfs.setOnAction(e -> {
            if (currentMaze != null) {
                dfs.DFS(currentMaze);
                if (currentMaze.getMazeGrid()[1][1] != 0) {
                	dfs.highlightPathAnimation(currentMaze);
                    nbCasetraite.setText("nombre case traitée:" + dfs.nbCase);
                    nbCasePath.setText("nombre case chemin" + dfs.nbPath);
                } else {
                    // Pas de solution trouvée => affichage message
                    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                    alert.setTitle("Résultat");
                    alert.setHeaderText(null);
                    alert.setContentText("Aucune solution trouvée pour ce labyrinthe.");
                    alert.showAndWait();
                }
            }
        });
        
        showUnderGroundDFS.setOnAction(e -> {
            if (currentMaze != null) {
                dfs.resDistanceSlowDFS(currentMaze);
                nbCasetraite.setText("nombre case traitée:"+dfs.nbCase);
                nbCasePath.setText("nombre case chemin" +dfs.nbPath);
            }
        });

        buttonRight.setOnAction(e->{
        	List<int[]> chemin = right.resRight(currentMaze);
            if (right.contains(chemin, new int[]{currentMaze.getHeight() - 2, currentMaze.getWidth() - 2})) {
                right.highlightPath(currentMaze);
                nbCasetraite.setText("nombre case traitée:" + right.nbCase);
                nbCasePath.setText("nombre case chemin" + right.nbPath);
            } else {
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                alert.setTitle("Résultat");
                alert.setHeaderText(null);
                alert.setContentText("Aucune solution trouvée avec la méthode droite.");
                alert.showAndWait();
            }
        });
        
        buttonRightAnimation.setOnAction(e->{
        	List<int[]> chemin = right.resRight(currentMaze);
            if (right.contains(chemin, new int[]{currentMaze.getHeight() - 2, currentMaze.getWidth() - 2})) {
                right.resRightAnimation(currentMaze);
                nbCasetraite.setText("nombre case traitée:" + right.nbCase);
                nbCasePath.setText("nombre case chemin" + right.nbPath);
            } else {
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                alert.setTitle("Résultat");
                alert.setHeaderText(null);
                alert.setContentText("Aucune solution trouvée avec la méthode droite.");
                alert.showAndWait();
            }
        });
        
        reset.setVisible(false);
        reset.setOnAction(e->{
        	if(currentMaze!=null) {
        		currentMaze.reset(currentMaze);
        	}
        });
        
        hiddenButton.getChildren().addAll(buttonBfs,buttonBfsAnimation,showUnderGroundBFS,buttonDfs,buttonAnimationDfs,showUnderGroundDFS,buttonRight,buttonRightAnimation,reset,nbCasetraite,nbCasePath);
        root.setBottom(hiddenButton);

        
        HBox controls = new HBox(10);
        controls.getChildren().addAll(
                new Label("Largeur :"), widthField,
                new Label("Hauteur :"), heightField,
                new Label("Vitesse :"), speed,
                perfectFast, perfectSlow, imperfectFast, imperfectSlow,saveMaze,loadMaze
        );
        root.setTop(controls);
        

        
        perfectFast.setOnAction(e -> generateMaze(root, widthField, heightField, true, false,speed));
        perfectSlow.setOnAction(e -> generateMaze(root, widthField, heightField, true, true,speed));
        imperfectFast.setOnAction(e -> generateMaze(root, widthField, heightField, false, false,speed));
        imperfectSlow.setOnAction(e -> generateMaze(root, widthField, heightField, false, true,speed));
        saveMaze.setOnAction(e -> SelectFile(true));
        loadMaze.setOnAction(e -> SelectFile(false));
        
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Génération de labyrinthe");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void generateMaze(BorderPane root, TextField widthField, TextField heightField, boolean perfect, boolean progressive, TextField speed) {
        try {
        	String speedText = speed.getText();
            int Speed = 0;
            if (!speedText.isEmpty()) {
                Speed = Integer.parseInt(speedText);}
            int width = Integer.parseInt(widthField.getText());
            int height = Integer.parseInt(heightField.getText());
            MazeGenerator newMaze = new MazeGenerator(height, width, perfect, progressive,Speed,false,null);
            currentMaze = newMaze;
            root.setCenter(currentMaze.getGridPane());
            buttonBfs.setVisible(true); // le bouton devient visible après génération
            buttonBfsAnimation.setVisible(true);
            showUnderGroundBFS.setVisible(true);
            buttonDfs.setVisible(true); // le bouton devient visible après génération
            buttonAnimationDfs.setVisible(true);
            showUnderGroundDFS.setVisible(true);
            buttonRight.setVisible(true);
            buttonRightAnimation.setVisible(true);
            reset.setVisible(true);
        } catch (NumberFormatException ex) {
            System.err.println("Dimensions non valides.");
        }
    }
 private void SelectFile(boolean isSave) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(isSave ? "Sauvegarder le labyrinthe" : "Charger un labyrinthe");
        fileChooser.getExtensionFilters().add(
            new ExtensionFilter("Fichiers texte", "*.txt")
        );
        File saveDir = new File("src/saves");
        if (saveDir.exists()) {
            fileChooser.setInitialDirectory(saveDir.getAbsoluteFile());
        }
        File file = isSave ? fileChooser.showSaveDialog(null) : fileChooser.showOpenDialog(null);
        if (file != null) {
            if (isSave) {
                SaveMazeManager.saveMaze(file,currentMaze);
            } else {
               	currentMaze =  SaveMazeManager.loadMaze(file);
		root.setCenter(currentMaze.getGridPane());
                buttonBfs.setVisible(true); // le bouton devient visible après génération
                buttonBfsAnimation.setVisible(true);
                showUnderGroundBFS.setVisible(true);
            }
        }
    }
  
    
    public static void main(String[] args) {
        launch(args);
    }
}
