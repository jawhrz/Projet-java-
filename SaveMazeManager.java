package application;

import java.io.File;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Classe resérvé à la sauvegarde et au chargement d'un labyrinthe.
 * @author : Elyas Mensah
 */
public class SaveMazeManager {
    /** 
     * Sauvegarde un labyrinthe dans un fichier texte.
     * @param file        Le fichier dans lequel le labyrinthe sera sauvegardé.
     * @param mazeToSave  L'objet MazeGenerator représentant le labyrinthe à sauvegarder.
     */
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
    /** 
     * Charger un labyrinthe à partir d'un fichier texte.
     * @param file        Le fichier dans lequel le labyrinthe sera sauvegardé.
     * @return Un nouveau objet MazeGenerator construit à partir du labyrinthe chargé, ou null en cas d'erreur
     */
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
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement : " + e.getMessage());
            return null;
        }
    }
}
