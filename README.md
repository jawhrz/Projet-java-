# Générateur et Solveur de Labyrinthe - JavaFX

## Description

Ce projet JavaFX permet de **générer** et **résoudre** des labyrinthes à l’aide de plusieurs algorithmes :

- **BFS (Breadth-First Search)** – résolution en largeur
- **DFS (Depth-First Search)** – résolution en profondeur
- **Algorithmes mur-gauche et mur-droit**
- Possibilité de choisir entre labyrinthe **parfait** (sans boucle) ou **imparfait**
- Mode **animation pas à pas** ou **exécution instantanée**
- **Sauvegarde** et **chargement** de labyrinthes au format texte

## Structure du projet

Le projet a été conçu pour être facilement exécutable dans **Eclipse** avec **JavaFX** :

- Tous les **fichiers sources Java** sont placés dans le **même package 'application'**
- Le fichier CSS de style est nommé **'application.css'** et est également dans le package 'application'
- De meme l'image "fondEcran.jpg" est au meme endroit que le main donc aussi dans le même package 'application'
- Aucun système de module (comme 'module-info.java') n'est utilisé

## Prérequis

Avant d’exécuter le projet, vous devez avoir :

- **Java JDK 8 ou supérieur** 
- **JavaFX SDK** installé et correctement lié à Eclipse (notre version est la 21.0.6, la 24 n'est pas compatible ou génère des problèmes avec java)

> 💡 Si vous utilisez Java 11 ou plus récent, vous devez ajouter manuellement les modules JavaFX dans vos paramètres d’exécution ou utiliser un JDK avec JavaFX inclus (ex. : Liberica JDK Full).

## Configuration dans Eclipse

1. **Cloner ou importer** le projet dans Eclipse
2. Placer tous les fichiers '.java' et le fichier 'application.css' dans le package 'application'
3. Ajouter JavaFX à votre configuration :
   - Clic droit sur le projet → **Build Path** → **Configure Build Path**
   - Onglet **Libraries** → Ajouter les `lib/*.jar` du SDK JavaFX
   - Onglet **Run/Debug Settings** → Arguments VM :
     ```bash
     --module-path /chemin/vers/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml
     ```

## Exécution

Lancer la classe 'Main.java', située dans le package 'application'.  
L’interface graphique vous permettra de générer et résoudre des labyrinthes dynamiquement.

## Dossier de sauvegarde

Les labyrinthes sont sauvegardés et chargés dans le dossier :
