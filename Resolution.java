package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import java.util.*;

/**
 * Classe abstraite représentant une stratégie de résolution de labyrinthe.
 * Les classes qui en héritent doivent implémenter les méthodes nécessaires à
 * l'initialisation, la visualisation, et la logique du chemin.
 * @author Jawad Harizi, @author Cherf Noam
 */
public abstract class Resolution {

    /**
     * Initialise les distances ou valeurs des cases dans le labyrinthe.
     * Cette méthode est utile avant de lancer un algorithme de résolution.
     *
     * @param maze Le labyrinthe à initialiser
     */
    public abstract void initDistance(MazeGenerator maze);

    /**
     * Colore ou met en évidence le chemin trouvé dans le labyrinthe.
     *
     * @param maze Le labyrinthe dans lequel afficher le chemin
     */
    public abstract void highlightPath(MazeGenerator maze);

    /**
     * Réinitialise l'état du labyrinthe à son état initial,
     * y compris l'affichage des boutons.
     *
     * @param maze Le labyrinthe à réinitialiser
     */
    public abstract void reset(MazeGenerator maze);

    /**
     * Vérifie si une position (tableau de deux entiers) est déjà présente dans une liste.
     *
     * @param liste Liste de positions
     * @param element Élément recherché
     * @return true si présent, false sinon
     */
    public abstract boolean contains(List<int[]> liste, int[] element);
}
