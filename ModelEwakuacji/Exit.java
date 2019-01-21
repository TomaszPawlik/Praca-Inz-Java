package ModelEwakuacji;

public class Exit {
	int width;
	int height;
	double crowdDensity;
	double crowdDensityFromPreviousStep;
	double changingCrowdDensityRate;
	
	int numberOfCellsInNeighborhood;
	int ocupiedCellsInNeighborhood;
	
	Exit(int a, int b){
		width = a;
		height = b;
	}
	
	public void checkIfCellIsOcupied(cellState state){
			if (state==cellState.WALL){
				;
			} else {
				numberOfCellsInNeighborhood++;
			}
			
			if (state==cellState.PEDESTRIAN || state==cellState.PEDESTRIANONEXIT)
				ocupiedCellsInNeighborhood++;
	}
	
	//gęstośc komórek z poprzedniego kroku jest zapisana i liczmy nową gęstość komórek
	public void calculateCrowdDensity(){
		crowdDensityFromPreviousStep=crowdDensity;
		crowdDensity=((double)ocupiedCellsInNeighborhood)/((double)numberOfCellsInNeighborhood);
		changingCrowdDensityRate=0.5+((crowdDensity-crowdDensityFromPreviousStep)/2);
	}
	
	//Najpierw obliczamy gęstość komórek a potem przypisujemy tą samą wartość gęstości komórek z poprzedniego kroku
	public void calculateCrowdDensityForStage0(){
		crowdDensity=((double)ocupiedCellsInNeighborhood)/((double)numberOfCellsInNeighborhood);
		crowdDensityFromPreviousStep=crowdDensity;
		changingCrowdDensityRate=0.5+((crowdDensity-crowdDensityFromPreviousStep)/2);
	}
}
