package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import java.util.*;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe abstraite pour les algorithmes de résolution de labyrinthe de type
 * parcours en largeur (BFS) ou en profondeur (DFS), héritée de {@link Resolution}.
 * @author Jawad Harizi, @author Cherf Noam
 */
public abstract class ResolutionFS extends Resolution {

    /**
     * Recherche les voisins atteignables depuis une position donnée dans le labyrinthe.
     *
     * @param position La position actuelle sous forme [ligne, colonne]
     * @param maze Le labyrinthe dans lequel chercher les voisins
     * @return Liste des positions voisines accessibles
     */
    public abstract List<int[]> searchNeighbor(int[] position, MazeGenerator maze); 

    /**
     * Met en évidence de manière animée le chemin trouvé dans le labyrinthe.
     *
     * @param maze Le labyrinthe dans lequel afficher le chemin avec animation
     */
    public abstract void highlightPathAnimation(MazeGenerator maze); 
}
