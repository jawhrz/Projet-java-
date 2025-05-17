package application;

import java.io.File;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;


public class SaveMazeManager {
	
    public static void saveMaze(File file, MazeGenerator mazeToSave) {
    	int height = mazeToSave.getHeight();
    	int width = mazeToSave.getWidth();
    	int[][] mazeGrid = mazeToSave.getMazeGrid();
    	
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
        	for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    writer.write(mazeGrid[i][j]+";");
                }
                writer.println();
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde : " + e.getMessage());
        }
    }

    public static void loadMaze(File file, MazeGenerator mazeToLoad) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
           
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement : " + e.getMessage());
        }
    }
}
