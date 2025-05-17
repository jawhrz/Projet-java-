package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.util.*;

public class Main extends Application {
			
	Button showPathButton = new Button("BFS");
	Button showPathButtonAnimation = new Button("BFS pas à pas");
	Button showUnderGround = new Button("Bouttons traités");
	private MazeGenerator currentMaze;
	private Resolution res = new Resolution();

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        TextField widthField = new TextField();
        TextField heightField = new TextField();
        TextField speed = new TextField();
        

        Button perfectFast = new Button("Parfait - Complet");
        Button perfectSlow = new Button("Parfait - Pas à pas");
        Button imperfectFast = new Button("Imparfait - Complet");
        Button imperfectSlow = new Button("Imparfait - Pas à pas");
	Button saveMaze = new Button("Sauvegarder");
        Button loadMaze = new Button("Charger");
        HBox hiddenButton = new HBox(3);
        
        showPathButton.setVisible(false); // bouton caché
        showPathButtonAnimation.setVisible(false);
        showUnderGround.setVisible(false);
        showPathButton.setOnAction(e -> {
            if (currentMaze != null) {
                res.resDistance(currentMaze);
                res.highlightShortestPath(currentMaze);
            }
        });
        showPathButtonAnimation.setOnAction(e -> {
            if (currentMaze != null) {
                res.resDistance(currentMaze);
                res.highlightShortestPathAnimation(currentMaze);
            }
        });
        
        showUnderGround.setOnAction(e -> {
            if (currentMaze != null) {
                res.resDistanceSlow(currentMaze);
            }
        });
        
        hiddenButton.getChildren().addAll(showPathButton,showPathButtonAnimation,showUnderGround);
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
            MazeGenerator newMaze = new MazeGenerator(height, width, perfect, progressive,Speed);
            currentMaze = newMaze;
            root.setCenter(currentMaze.getGridPane());
            showPathButton.setVisible(true); // le bouton devient visible après génération
            showPathButtonAnimation.setVisible(true);
            showUnderGround.setVisible(true);
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
                SaveMazeManager.loadMaze(file,currentMaze);
            }
        }
    }
  
    
    public static void main(String[] args) {
        launch(args);
    }
}
