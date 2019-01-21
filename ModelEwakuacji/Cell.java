package ModelEwakuacji;

public class Cell {
	cellState state;
	public int cellWidthValue;
	public int cellHeightValue;
	Boolean occupied;
	
	public Cell(CellGrid cellGrid, int width, int height){
		state = cellState.CLEAR;
		
		cellWidthValue = width;
		cellHeightValue = height;
		occupied=false;
	}
		
	public void setState(cellState newState) {
		this.state = newState;
		
		if (newState==cellState.WALL || newState==cellState.PEDESTRIAN || newState==cellState.PEDESTRIANONEXIT)
			occupied=true;
		//if (newState==cellState.CLEAR || newState==cellState.EXIT)
		else
			occupied=false;
	}

	public cellState getState() {
		return state;
	}

	public String toString(CellGrid cellGrid, int WasOrWarras) {
		String message="";
		if (WasOrWarras==0){
			if (state==cellState.CLEAR)
				message = getState().toString()+" [" +cellHeightValue+","+cellWidthValue+"] ";
			if (state==cellState.WALL)
				message = getState().toString()+" [" +cellHeightValue+","+cellWidthValue+"] ";
			if (state==cellState.PEDESTRIAN)
				message = getState().toString()+" [" +cellHeightValue+","+cellWidthValue+"] Index: " + cellGrid.getPedestrianIndexFromList(cellWidthValue, cellHeightValue)
						  +" '"+cellGrid.pedestriansList.get(cellGrid.getPedestrianIndexFromList(cellWidthValue, cellHeightValue)).strategy+"'"
						  +" Desired Exit: "+cellGrid.pedestriansList.get(cellGrid.getPedestrianIndexFromList(cellWidthValue, cellHeightValue)).indexOfDesiredExit
						  +" Movement order: "+cellGrid.pedestriansList.get(cellGrid.getPedestrianIndexFromList(cellWidthValue, cellHeightValue)).movementOrder
						  +" Cost funtion value: "+cellGrid.pedestriansList.get(cellGrid.getPedestrianIndexFromList(cellWidthValue, cellHeightValue)).costFunction;

			if (state==cellState.EXIT)
				message = getState().toString()+" [" +cellHeightValue+","+cellWidthValue+"] Index: " + cellGrid.getExitIndexFromList(cellWidthValue, cellHeightValue);
			if (state==cellState.PEDESTRIANONEXIT)
				message = getState().toString()+" [" +cellHeightValue+","+cellWidthValue+"] Index: " + cellGrid.getPedestrianIndexFromList(cellWidthValue, cellHeightValue)
						  +" '"+cellGrid.pedestriansList.get(cellGrid.getPedestrianIndexFromList(cellWidthValue, cellHeightValue)).strategy+"'"
						  +" Desired Exit: "+cellGrid.pedestriansList.get(cellGrid.getPedestrianIndexFromList(cellWidthValue, cellHeightValue)).indexOfDesiredExit
						  +" Movement order: "+cellGrid.pedestriansList.get(cellGrid.getPedestrianIndexFromList(cellWidthValue, cellHeightValue)).movementOrder
						  +" Cost funtion value: "+cellGrid.pedestriansList.get(cellGrid.getPedestrianIndexFromList(cellWidthValue, cellHeightValue)).costFunction
						  +"   //   Exit Index: " + cellGrid.getExitIndexFromList(cellWidthValue, cellHeightValue);
		}
		
		if (WasOrWarras==1){
			if (state==cellState.CLEAR)
				message = "Warras method not yet implemented.";
			if (state==cellState.WALL)
				message = "Warras method not yet implemented.";
			if (state==cellState.PEDESTRIAN)
				message = "Warras method not yet implemented.";			
			if (state==cellState.EXIT)
				message = "Warras method not yet implemented.";
			if (state==cellState.PEDESTRIANONEXIT)
				message = "Warras method not yet implemented.";
		}
		
		return message;
	}
}
