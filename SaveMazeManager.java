package application;

import java.io.File;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class SaveMazeManager {
	
    public static void saveMaze(File file, MazeGenerator mazeToSave) {
    	int height = mazeToSave.getHeight();
    	int width = mazeToSave.getWidth();
    	int[][] mazeGrid = mazeToSave.getMazeGrid();
    	
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
        	writer.write(height+" "+width);
        	writer.println();
        	for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    writer.write(mazeGrid[i][j]+" ");
                }
                writer.println();
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde : " + e.getMessage());
        }
    }

    public static MazeGenerator loadMaze(File file) {
        try (Scanner scanner = new Scanner(file)) {
           int height = scanner.nextInt();
           int width = scanner.nextInt();
           int[][] mazeGrid = new int[height][width];
           //ArrayList
           for (int i = 0; i < height; i++) {
               for (int j = 0; j < width; j++) {
            	   mazeGrid[i][j] = scanner.nextInt();
               }   
           }
           return new MazeGenerator(height,width,true, false, 0, true, mazeGrid);
           //return new MazeGenerator(mazeGrid);
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement : " + e.getMessage());
            return null;
        }
    }
}