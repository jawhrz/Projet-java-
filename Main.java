package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        TextField widthField = new TextField();
        
        TextField heightField = new TextField();
        

        Button perfectFast = new Button("Parfait - Complet");
        Button perfectSlow = new Button("Parfait - Pas à pas");
        Button imperfectFast = new Button("Imparfait - Complet");
        Button imperfectSlow = new Button("Imparfait - Pas à pas");

        HBox controls = new HBox(10);
        controls.getChildren().addAll(
                new Label("Largeur :"), widthField,
                new Label("Hauteur :"), heightField,
                perfectFast, perfectSlow, imperfectFast, imperfectSlow
        );
        root.setTop(controls);


        perfectFast.setOnAction(e -> generateMaze(root, widthField, heightField, true, false));
        perfectSlow.setOnAction(e -> generateMaze(root, widthField, heightField, true, true));
        imperfectFast.setOnAction(e -> generateMaze(root, widthField, heightField, false, false));
        imperfectSlow.setOnAction(e -> generateMaze(root, widthField, heightField, false, true));

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Génération de labyrinthe");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void generateMaze(BorderPane root, TextField widthField, TextField heightField, boolean perfect, boolean progressive) {
        try {
            int width = Integer.parseInt(widthField.getText());
            int height = Integer.parseInt(heightField.getText());
            MazeGenerator newMaze = new MazeGenerator(height, width, perfect, progressive);
            root.setCenter(newMaze.getGridPane());
        } catch (NumberFormatException ex) {
            System.err.println("Dimensions non valides.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
