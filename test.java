public static void resDistanceSlow(MazeGenerator maze) {
	    Timeline timeline = new Timeline();
	    int height = maze.getHeight();
	    int width = maze.getWidth();
	    int[][] grid = maze.getMazeGrid();
	    initDistance(maze);
	    Button[][] buttonGrid = maze.getButtonGrid();

	    int[] distance = {1};
	    List<int[]> dejavu = new ArrayList<>();
	    List<int[]> atraiter = new ArrayList<>();

	    int[] position = {height - 2, width - 1}; // cellule de sortie
	    grid[position[0]][position[1]] = distance[0];
	    atraiter.add(position);
	    dejavu.add(position);

	    KeyFrame keyFrame = new KeyFrame(Duration.millis(30), event -> {
	        if (grid[1][1] != 0 || atraiter.isEmpty()) {
	            timeline.stop(); // fin si on atteint l'entrée ou plus rien à traiter
	            return;
	        }

	        int size = atraiter.size();
	        distance[0]++;

	        for (int k = 0; k < size; k++) {
	            int[] courant = atraiter.remove(0);
	            Button btn1 = buttonGrid[courant[0]][courant[1]];
	            btn1.setStyle("-fx-background-color: red;");

	            for (int[] neighbor : searchNeighbor(courant, maze)) {
	                int a = neighbor[0];
	                int b = neighbor[1];

	                boolean dejaVu = false;
	                for (int[] vu : dejavu) {
	                    if (vu[0] == a && vu[1] == b) {
	                        dejaVu = true;
	                        break;
	                    }
	                }

	                if (!dejaVu) {
	                    grid[a][b] = distance[0];
	                    atraiter.add(new int[]{a, b});
	                    dejavu.add(new int[]{a, b});
	                }
	            }
	        }
	    });

	    timeline.getKeyFrames().add(keyFrame);
	    timeline.setCycleCount(Animation.INDEFINITE); // boucle infinie jusqu’à `stop()`
	    timeline.play();
	}

List<int[]> dejavu=new ArrayList<>();
    List<int[]> atraiter=new ArrayList<>();
    
    private void DFS(MazeGenerator maze,List<int[]>atraiter,List<int[]> dejavu, int[] depart) {
    	int[][] grid= maze.getMazeGrid();
    	
    	dejavu.add(depart);
    	atraiter.add(depart);
    	while (!atraiter.isEmpty()) {
    		//for (int[] :)
    	}
    }
