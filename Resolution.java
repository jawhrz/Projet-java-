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

public abstract class Resolution {
	  	
	
	public abstract void initDistance(MazeGenerator maze); 

	public abstract void highlightPath(MazeGenerator maze); 
		
	public abstract void reset(MazeGenerator maze); 
    
    public abstract boolean contains(List<int[]> liste, int[] element);
    

   }
	