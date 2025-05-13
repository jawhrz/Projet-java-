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

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            BorderPane root = new BorderPane();

            // Barre supérieure : Saisie des dimensions
            HBox sizeInput = new HBox(10);
            TextField widthField = new TextField();
            widthField.setPromptText("Largeur");
            TextField heightField = new TextField();
            heightField.setPromptText("Hauteur");

            Button perfectButton = new Button("Labyrinthe parfait");
            Button imperfectButton = new Button("Labyrinthe non parfait");

            sizeInput.getChildren().addAll(new Label("Largeur : "), widthField, new Label("Hauteur : "), heightField, perfectButton, imperfectButton);
            root.setTop(sizeInput);

            // Conteneur du labyrinthe (initialement vide)
            MazeGenerator mazeGen = new MazeGenerator(10, 10, true); // Dimensions par défaut
            root.setCenter(mazeGen.getGridPane());

            // Action du bouton "Labyrinthe parfait"
            perfectButton.setOnAction(e -> {
                try {
                    int width = Integer.parseInt(widthField.getText());
                    int height = Integer.parseInt(heightField.getText());
                    MazeGenerator newMaze = new MazeGenerator(width, height, true);
                    root.setCenter(newMaze.getGridPane());
                } catch (NumberFormatException ex) {
                    System.err.println("Veuillez entrer des dimensions valides.");
                }
            });

            // Action du bouton "Labyrinthe non parfait"
            imperfectButton.setOnAction(e -> {
                try {
                    int width = Integer.parseInt(widthField.getText());
                    int height = Integer.parseInt(heightField.getText());
                    MazeGenerator newMaze = new MazeGenerator(width, height, false);
                    root.setCenter(newMaze.getGridPane());
                } catch (NumberFormatException ex) {
                    System.err.println("Veuillez entrer des dimensions valides.");
                }
            });

            Scene scene = new Scene(root, 800, 600);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Génération de labyrinthe");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
