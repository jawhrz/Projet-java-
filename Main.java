package application;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.File;
import java.util.List;

public class Main extends Application {

    Button buttonBfs = new Button("BFS");
    Button buttonBfsAnimation = new Button("BFS pas à pas");
    Button showUnderGroundBFS = new Button("Traités BFS");

    Button buttonDfs = new Button("DFS");
    Button buttonAnimationDfs = new Button("DFS pas à pas");
    Button showUnderGroundDFS = new Button("Traités DFS");

    Button buttonRight = new Button("Right");
    Button buttonRightAnimation = new Button("Right pas à pas");
    
    Button buttonLeft = new Button("Left");
    Button buttonLeftAnimation = new Button("Left pas à pas");

    Button reset = new Button("Reset");

    private BorderPane root;
    private MazeGenerator currentMaze;
    private final BFS bfs = new BFS();
    private final DFS dfs = new DFS();
    private final Right right = new Right();
    private final Left left = new Left();
    @Override
    public void start(Stage primaryStage) {
    	
        root = new BorderPane();
       
        // Top controls
        TextField widthField = new TextField();
        TextField heightField = new TextField();
        TextField speedField = new TextField();
        Button perfectFast = new Button("Parfait Complet");
        Button perfectSlow = new Button("Parfait Pas à pas");
        Button imperfectFast = new Button("Imparfait Complet");
        Button imperfectSlow = new Button("Imparfait Pas à pas");
        Button saveMaze = new Button("Sauver");
        Button loadMaze = new Button("Charger");

        HBox topControls = new HBox(10,
                new Label("Largeur:"), widthField,
                new Label("Hauteur:"), heightField,
                new Label("Temps de génération:"), speedField,
                perfectFast, perfectSlow,
                imperfectFast, imperfectSlow,
                saveMaze, loadMaze
        );
        topControls.getStyleClass().add("top-controls");
        root.setTop(topControls);
        topControls.setAlignment(Pos.CENTER);

        // Right panel: solving buttons
        Label nbCaseTraite = new Label("Cases traitées: ");
        Label nbCasePath = new Label("Cases chemin: ");
        Label timeGeneration = new Label("Temps generation: ");
        
        VBox BFS =new VBox(new Label("BFS:"),buttonBfs, buttonBfsAnimation, showUnderGroundBFS);
        BFS.getStyleClass().add("solving-panel");
        
        VBox DFS =new VBox(new Label("DFS:"),buttonDfs, buttonAnimationDfs, showUnderGroundDFS);
        DFS.getStyleClass().add("solving-panel");
        
        VBox Right =new VBox(new Label("Right:"),buttonRight, buttonRightAnimation);
        Right.getStyleClass().add("solving-panel");
        
        VBox Left =new VBox(new Label("Left:"),buttonLeft, buttonLeftAnimation);
        Left.getStyleClass().add("solving-panel");
        
        VBox solvingPanel = new VBox(
                BFS,
                DFS,
                Right,
                Left      
        );
        solvingPanel.getStyleClass().add("solving-panel");
        root.setRight(solvingPanel);
        
        Region space= new Region();
        VBox.setVgrow(space,Priority.ALWAYS);

        VBox test =new VBox(reset, nbCaseTraite, nbCasePath,timeGeneration,space,saveMaze, loadMaze);
        test.getStyleClass().add("solving-panel");
        root.setLeft(test);
        // Cacher les boutons au démarrage
        setSolvingButtonsVisible(false);

        // Gestion des actions
        perfectFast.setOnAction(e -> generateMaze(widthField, heightField, true, false, speedField));
        perfectSlow.setOnAction(e -> generateMaze(widthField, heightField, true, true, speedField));
        imperfectFast.setOnAction(e -> generateMaze(widthField, heightField, false, false, speedField));
        imperfectSlow.setOnAction(e -> generateMaze(widthField, heightField, false, true, speedField));

        saveMaze.setOnAction(e -> SelectFile(true));
        loadMaze.setOnAction(e -> SelectFile(false));

        // Résolution BFS
        buttonBfs.setOnAction(e -> {
            if (currentMaze != null) {
            	long startTime = System.nanoTime();  // début

            	bfs.bfs(currentMaze);               // ton appel à l'algorithme
            	long endTime = System.nanoTime();    // fin
            	long duration = endTime - startTime; // durée en nanosecondes

            	timeGeneration.setText("Temps d'exécution : " + duration / 1_000_000.0 + " ms");
                if (currentMaze.getMazeGrid()[1][1] > 0 ) {
                    bfs.highlightPath(currentMaze);
                    
                    nbCaseTraite.setText("Cases traitées: " + bfs.nbCase);
                    nbCasePath.setText("Cases chemin: " + bfs.nbPath);
                } else {
                    showNoSolution(bfs.nbCase);
                }
            }
        });

        buttonBfsAnimation.setOnAction(e -> {
            if (currentMaze != null) {
            	long startTime = System.nanoTime();
                bfs.bfs(currentMaze);
                bfs.highlightPathAnimation(currentMaze);
                long endTime = System.nanoTime();    // fin
            	long duration = endTime - startTime; // durée en nanosecondes

            	timeGeneration.setText("Temps d'exécution : " + duration / 1_000_000.0 + " ms");
                
                if (currentMaze.getMazeGrid()[1][1] > 0) {
                    nbCaseTraite.setText("Cases traitées: " + bfs.nbCase);
                    nbCasePath.setText("Cases chemin: " + bfs.nbPath);
                } else {
                    showNoSolution(bfs.nbCase);
                }
            }
        });

        showUnderGroundBFS.setOnAction(e -> {
            if (currentMaze != null) {
            	long startTime = System.nanoTime();
                bfs.bfsAnimation(currentMaze);
                long endTime = System.nanoTime();    // fin
            	long duration = endTime - startTime; // durée en nanosecondes

            	timeGeneration.setText("Temps d'exécution : " + duration / 1_000_000.0 + " ms");
                nbCaseTraite.setText("Cases traitées: " + bfs.nbCase);
                nbCasePath.setText("Cases chemin: " + bfs.nbPath);
            }
        });

        // Résolution DFS
        buttonDfs.setOnAction(e -> {
            if (currentMaze != null) {
            	long startTime = System.nanoTime();
                dfs.DFS(currentMaze);
                long endTime = System.nanoTime();    // fin
            	long duration = endTime - startTime; // durée en nanosecondes

            	timeGeneration.setText("Temps d'exécution : " + duration / 1_000_000.0 + " ms");
                if (currentMaze.getMazeGrid()[1][1] > 0) {
                	
                    dfs.highlightPath(currentMaze);
                    
                    nbCaseTraite.setText("Cases traitées: " + dfs.nbCase);
                    nbCasePath.setText("Cases chemin: " + dfs.nbPath);
                } else {
                    showNoSolution(dfs.nbCase);
                }
            }
        });

        buttonAnimationDfs.setOnAction(e -> {
            if (currentMaze != null) {
            	long startTime = System.nanoTime();
                dfs.DFS(currentMaze);
                dfs.highlightPathAnimation(currentMaze);
                long endTime = System.nanoTime();    // fin
            	long duration = endTime - startTime; // durée en nanosecondes

            	timeGeneration.setText("Temps d'exécution : " + duration / 1_000_000.0 + " ms");
                if (currentMaze.getMazeGrid()[1][1] > 0) {
                    nbCaseTraite.setText("Cases traitées: " + dfs.nbCase);
                    nbCasePath.setText("Cases chemin: " + dfs.nbPath);
                } else {
                    showNoSolution(dfs.nbCase);
                }
            }
        });

        showUnderGroundDFS.setOnAction(e -> {
            if (currentMaze != null) {
            	long startTime = System.nanoTime();

                dfs.resDistanceSlowDFS(currentMaze);
                long endTime = System.nanoTime();    // fin
            	long duration = endTime - startTime; // durée en nanosecondes

            	timeGeneration.setText("Temps d'exécution : " + duration / 1_000_000.0 + " ms");
                nbCaseTraite.setText("Cases traitées: " + dfs.nbCase);
                nbCasePath.setText("Cases chemin: " + dfs.nbPath);
            }
        });

        // Résolution Droite
        buttonRight.setOnAction(e -> {
        	long startTime = System.nanoTime();
            List<int[]> chemin = right.resRight(currentMaze);
            if (right.contains(chemin, new int[]{currentMaze.getHeight() - 2, currentMaze.getWidth() - 2})) {

                right.highlightPath(currentMaze);
                long endTime = System.nanoTime();    // fin
            	long duration = endTime - startTime; // durée en nanosecondes

            	timeGeneration.setText("Temps d'exécution : " + duration / 1_000_000.0 + " ms");
                nbCaseTraite.setText("Cases traitées: " + right.nbCase);
                nbCasePath.setText("Cases chemin: " + right.nbPath);
            } else {
                showNoSolution(right.nbCase);
            }
        });

        buttonRightAnimation.setOnAction(e -> {
        	long startTime = System.nanoTime();
            List<int[]> chemin = right.resRight(currentMaze);
            if (right.contains(chemin, new int[]{currentMaze.getHeight() - 2, currentMaze.getWidth() - 2})) {

                right.resRightAnimation(currentMaze);
                long endTime = System.nanoTime();    // fin
            	long duration = endTime - startTime; // durée en nanosecondes

            	timeGeneration.setText("Temps d'exécution : " + duration / 1_000_000.0 + " ms");
                nbCaseTraite.setText("Cases traitées: " + right.nbCase);
                nbCasePath.setText("Cases chemin: " + right.nbPath);
            } else {
                showNoSolution(right.nbCase);
            }
        });
        
        buttonLeft.setOnAction(e -> {
        	long startTime = System.nanoTime();
            List<int[]> chemin = left.resLeft(currentMaze);
            if (left.contains(chemin, new int[]{currentMaze.getHeight() - 2, currentMaze.getWidth() - 2}) ) {

                left.highlightPath(currentMaze);
                long endTime = System.nanoTime();    // fin
            	long duration = endTime - startTime; // durée en nanosecondes

            	timeGeneration.setText("Temps d'exécution : " + duration / 1_000_000.0 + " ms");
                nbCaseTraite.setText("Cases traitées: " + left.nbCase);
                nbCasePath.setText("Cases chemin: " + left.nbPath);
            } else {
                showNoSolution(left.nbCase);
            }
        });

        buttonLeftAnimation.setOnAction(e -> {
        	long startTime = System.nanoTime();
            List<int[]> chemin = left.resLeft(currentMaze);
            if (left.contains(chemin, new int[]{currentMaze.getHeight() - 2, currentMaze.getWidth() - 2})) {

                left.resLeftAnimation(currentMaze);
                long endTime = System.nanoTime();    // fin
            	long duration = endTime - startTime; // durée en nanosecondes

            	timeGeneration.setText("Temps d'exécution : " + duration / 1_000_000.0 + " ms");
                nbCaseTraite.setText("Cases traitées: " + left.nbCase);
                nbCasePath.setText("Cases chemin: " + left.nbPath);
            } else {
                showNoSolution(left.nbCase);
            }
        });

        reset.setOnAction(e -> {
            if (currentMaze != null) currentMaze.reset(currentMaze);
        });

        Scene scene = new Scene(root, 1000, 700);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        primaryStage.setTitle("Génération de labyrinthe");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void generateMaze(TextField widthField, TextField heightField, boolean perfect, boolean progressive, TextField speedField) {
        try {
            int width = Integer.parseInt(widthField.getText());
            int height = Integer.parseInt(heightField.getText());
            int speed = speedField.getText().isEmpty() ? 0 : Integer.parseInt(speedField.getText());

            if (width >= 100 || height >= 100) {
                showAlert("Taille trop grande !");
                return;
            }

            MazeGenerator newMaze = new MazeGenerator(height, width, perfect, progressive, speed, false, null);
            currentMaze = newMaze;
            GridPane test1=currentMaze.getGridPane();
            root.setCenter(test1);
            test1.setAlignment(Pos.CENTER);
            setSolvingButtonsVisible(true);
        } catch (NumberFormatException ex) {
            System.err.println("Dimensions non valides.");
        }
    }

    private void setSolvingButtonsVisible(boolean visible) {
        buttonBfs.setVisible(visible);
        buttonBfsAnimation.setVisible(visible);
        showUnderGroundBFS.setVisible(visible);
        buttonDfs.setVisible(visible);
        buttonAnimationDfs.setVisible(visible);
        showUnderGroundDFS.setVisible(visible);
        buttonRight.setVisible(visible);
        buttonRightAnimation.setVisible(visible);
        buttonLeft.setVisible(visible);
        buttonLeftAnimation.setVisible(visible);
        reset.setVisible(visible);
    }

    private void SelectFile(boolean isSave) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(isSave ? "Sauvegarder le labyrinthe" : "Charger un labyrinthe");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Fichiers texte", "*.txt"));
        File saveDir = new File("src/saves");
        if (saveDir.exists()) {
            fileChooser.setInitialDirectory(saveDir);
        }
        File file = isSave ? fileChooser.showSaveDialog(null) : fileChooser.showOpenDialog(null);
        if (file != null) {
            if (isSave) {
                SaveMazeManager.saveMaze(file, currentMaze);
            } else {
                currentMaze = SaveMazeManager.loadMaze(file);
                root.setCenter(currentMaze.getGridPane());
                setSolvingButtonsVisible(true);
            }
        }
    }

    private void showAlert(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

    private void showNoSolution(int nbCases) {
        showAlert("Aucune solution trouvée.");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
