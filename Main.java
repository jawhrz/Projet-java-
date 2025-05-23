package application;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.text.Font;
import javafx.scene.text.Font;
import java.io.File;
import java.util.List;
/**
 * Classe principale de l'application JavaFX permettant de générer et résoudre des labyrinthes.
 * L'application permet de générer des labyrinthes parfaits ou imparfaits,
 * de les sauvegarder, de les charger, et de les résoudre via différents algorithmes :
 * BFS, DFS, parcours par la droite ou par la gauche, avec ou sans animation.
 * 
 * @author Cherf Noam Harizi Jawad Mensah Elyas
 */
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
    /**
     * Point d'entrée de l'application JavaFX.
     * Initialise l'interface utilisateur et configure les événements pour la génération
     * et la résolution de labyrinthes.
     *
     * @param primaryStage La fenêtre principale de l'application.
     */
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
                    showNoSolution();
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
                    showNoSolution();
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
                    showNoSolution();
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
                    showNoSolution();
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
                showNoSolution();
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
                showNoSolution();
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
                showNoSolution();
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
                showNoSolution();
            }
        });

        reset.setOnAction(e -> {
            if (currentMaze != null) currentMaze.reset(currentMaze);
        });
        
        

        Scene scene = new Scene(root, 1000, 700);
        
        scene.setOnKeyPressed(event -> {		
            if (event.getCode() == KeyCode.B) {
                buttonBfs.fire();
            } else if (event.getCode() == KeyCode.F) {
                reset.fire();
            }
            else if (event.getCode() == KeyCode.D) {
                buttonDfs.fire();
            }
            else if (event.getCode() == KeyCode.R) {
                buttonRight.fire();
            }
            else if (event.getCode() == KeyCode.L) {
                buttonLeft.fire();
            }
            else if (event.getCode() == KeyCode.S) {
                buttonLeft.fire();
            }
            else if (event.getCode() == KeyCode.C) {
                buttonLeft.fire();
            }
            else if (event.getCode() == KeyCode.I) {
            	imperfectFast.fire();
            }
            else if (event.getCode() == KeyCode.P) {
            	perfectFast.fire();
            }
        });

        
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        //Font.loadFont(getClass().getResourceAsStream("PressStart2P-Regular.ttf"), 7);
        primaryStage.setTitle("Génération de labyrinthe");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    /**
     * Génère un labyrinthe selon les paramètres spécifiés dans les champs de texte.
     *
     * @param widthField  Champ contenant la largeur du labyrinthe.
     * @param heightField Champ contenant la hauteur du labyrinthe.
     * @param perfect     Indique si le labyrinthe doit être parfait (sans boucle).
     * @param progressive Indique si la génération doit être animée pas à pas.
     * @param speedField  Champ contenant la vitesse d'animation.
     */

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
            showAlert("Dimensions non valides.");
        }
    }
    /**
     * Rend visible ou invisible les boutons de résolution.
     *
     * @param visible true pour les rendre visibles, false pour les cacher.
     */
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
    /**
     * Permet à l'utilisateur de sauvegarder ou charger un labyrinthe via un sélecteur de fichiers.
     *
     * @param isSave true pour sauvegarder, false pour charger.
     */
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
                GridPane test=currentMaze.getGridPane();
                root.setCenter(test);
                test.setAlignment(Pos.CENTER);
                setSolvingButtonsVisible(true);
            }
        }
    }
    /**
     * Affiche une alerte contenant un message d'information.
     *
     * @param text Le message à afficher.
     */
    private void showAlert(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
    /**
     * Affiche une alerte indiquant qu'aucune solution n'a été trouvée pour le labyrinthe.
     */
    private void showNoSolution() {
        showAlert("Aucune solution trouvée.");
    }
    /**
     * Méthode main classique pour lancer l'application JavaFX.
     *
     * @param args arguments de la ligne de commande.
     */
    public static void main(String[] args) {
        launch(args);
    }
}