package ModelEwakuacji;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Random;

import GUI.Interface;

public class CellGrid {

	Interface i;
	public int width;
	public int height;
	public int stage;
	public Boolean solved;
	int totalNumberOfMovesToSolve;
	public int totalNumberOfPedestrians;
	public int numberOfPedestriansOnGrid;			//ilu przechodniów jest na planszy
	int numberOfExits;
	public int mooreRadius=1;
	
	public Cell[][] cellGridTable;
	public ArrayList<Pedestrian> pedestriansList;
	public ArrayList<Exit> exitList;
	
	public int solveXtimes=1;
	public int[] evacuationResults;
	
	public CellGrid(int a, int b, Interface I){
		
		this.width = a;
		height = b;
		i=I;
		stage = 0;
		solved=false;
		totalNumberOfPedestrians=0;
		numberOfPedestriansOnGrid=0;
		numberOfExits=0;
		
		cellGridTable = new Cell[width][height];
		pedestriansList = new ArrayList<Pedestrian>();
		exitList = new ArrayList<Exit>();
		
		//tworzymy ściany na brzegach pomieszczenia
		for (int i=0;i<width;i++){
			for (int j=0;j<height;j++){
				cellGridTable[i][j] = new Cell(this, i,j);
				if (i==0 || j==0 || i ==(width-1) || j==(height-1)){
					cellGridTable[i][j].setState(cellState.WALL);
				}
			}
		}
	}

	public void simulateEvacuationOnce(){
		Boolean evacuationFailed=false;
		solveXtimes=1;
		evacuationResults = new int[solveXtimes];
		totalNumberOfPedestrians = numberOfPedestriansOnGrid;
		
		if (stage==0){
			createInitialFiles();
			createOutputFile();
			calculateExitValues();
			
			for (int x=0;x<pedestriansList.size();x++){
				pedestriansList.get(x).distanceValues = new double[exitList.size()];
				pedestriansList.get(x).crowdDensityValues = new double[exitList.size()];
				pedestriansList.get(x).changingCrowdDensityValues = new double[exitList.size()];
			}
		}	
		
		while (numberOfPedestriansOnGrid!=0){
			
			double percentR = ((double)numberOfPedestriansOnGrid/totalNumberOfPedestrians);
			percentR = percentR*100;
			percentR = Math.round(percentR*100)/100;
			int progress = 100-(int)percentR;

			i.n1.setText("Simulation: 1"+" of: "+"1");
			i.n2.setText("Progress: "+progress+"%");
			i.pBar.setValue(progress);
			
			try {
			         Thread.sleep(50);
			} catch (InterruptedException ignore) {};
			
			if (checkIfAllBlocked()==true){
				System.out.println("Evacuation failed");
				evacuationFailed=true;
				break;
			}
			
			stage++;
		
			checkIfPedestriansAreOnExits();
			defineExitDecisionForPedestrians();
			sortPedestriansList();
			makeMove();
			calculateExitValues();

			createOutputFile();
			//createValuesFile();
		}
		
		solved=true;
		stage++;
		i.pBar.setValue(100);
		i.n2.setText("Progress: "+"100%");
		
		if (evacuationFailed==true){
			evacuationResults[0]=0;
			evacuationFailed=false;
		}
		else
			evacuationResults[0]=getStageNumber();
		
		createOutputFile();
		createStatisticFile();
	}
	
	public void simulateEvacuationXTimes(int numberOfSimulation, int numberOfPedestrians, Boolean randomStrategies, pedestrianStrategy strategyForAll){
		Boolean evacuationFailed=false;
		solveXtimes=numberOfSimulation;
		evacuationResults = new int[solveXtimes];
		totalNumberOfPedestrians = numberOfPedestrians;
		
		for (int a=0;a<pedestriansList.size();a++){
			cellGridTable[pedestriansList.get(a).width][pedestriansList.get(a).height].setState(cellState.CLEAR);
		}
		
		for (int a=0;a<solveXtimes;a++){
			System.out.println("Simulation: "+a);
			stage=0;
			solved=false;
			
			for (int z=0;z<pedestriansList.size();z++){
				if (pedestriansList.get(z).onGrid==true)
					cellGridTable[pedestriansList.get(z).width][pedestriansList.get(z).height].setState(cellState.CLEAR);
				if (pedestriansList.get(z).onGrid==true && pedestriansList.get(z).onExit==true)
					cellGridTable[pedestriansList.get(z).width][pedestriansList.get(z).height].setState(cellState.EXIT);
			}
			pedestriansList.clear();
			
			numberOfPedestriansOnGrid=0;
			generateRandomPedestirans(numberOfPedestrians, randomStrategies, strategyForAll);
			
			if (stage==0){
				createInitialFiles();
				createOutputFile();
				calculateExitValues();
				
				for (int x=0;x<pedestriansList.size();x++){
					pedestriansList.get(x).distanceValues = new double[exitList.size()];
					pedestriansList.get(x).crowdDensityValues = new double[exitList.size()];
					pedestriansList.get(x).changingCrowdDensityValues = new double[exitList.size()];
				}
			}
			
			while (numberOfPedestriansOnGrid!=0){
				
				double percentR = ((double)numberOfPedestriansOnGrid/totalNumberOfPedestrians);
				percentR = percentR*100;
				percentR = Math.round(percentR*100)/100;
				int progress = 100-(int)percentR;

				i.n1.setText("Simulation: "+(a+1)+" of: "+solveXtimes);
				i.n2.setText("Progress: "+progress+"%");
				i.pBar.setValue(progress);
				i.n1.repaint();
				
				
				if (checkIfAllBlocked()==true){
					System.out.println("Evacuation failed");
					evacuationFailed=true;
					break;
				}
				
				stage++;
			
				checkIfPedestriansAreOnExits();
				defineExitDecisionForPedestrians();
				sortPedestriansList();
				//System.out.println("Sorting: "+stage);
				makeMove();
				calculateExitValues();					//obliczamy akturalny stan wyjść, komórki wyświetlają prawidłowe dane

				createOutputFile();
				//createValuesFile();
				
			}
			solved=true;
			stage++;
			i.pBar.setValue(100);
			i.n2.setText("Progress: "+"100%");
			
			if (evacuationFailed==true){
				evacuationResults[a]=0;
				evacuationFailed=false;
			}
			else
				evacuationResults[a]=getStageNumber();
			
			createOutputFile();
		}
		createStatisticFile();
	}
	
	public void checkIfPedestriansAreOnExits(){
		
		for (int a=0;a<pedestriansList.size();a++){
			if (cellGridTable[pedestriansList.get(a).width][pedestriansList.get(a).height].getState()==cellState.PEDESTRIANONEXIT && pedestriansList.get(a).onGrid==true){
				cellGridTable[pedestriansList.get(a).width][pedestriansList.get(a).height].setState(cellState.EXIT);
				pedestriansList.get(a).onGrid=false;
				pedestriansList.get(a).width=0;
				pedestriansList.get(a).oldwidth=0;
				pedestriansList.get(a).height=0;
				pedestriansList.get(a).oldheight=0;				
				decreaseNumberOfPedestrians();
			}
		}
	}
	
	//Najpierw ustawiamy koszt funkcji kazdego przechodnia na 1.
	//Status blocked ustawiamy na false;
	//Wyznaczamy również dystans do najbardziej oddalonego wyjścia
	//Nowy koszt funkcji liczmy jedynie dla tych przechdniów którzy są jeszcze w poimeszczeniu
	//Obliczamy koszt funkcji dla kazdego wyjścia i wybieramy najniższą wartość.
	public void defineExitDecisionForPedestrians(){
		
		for (int a=0;a<pedestriansList.size();a++){
			if (pedestriansList.get(a).strategy!=pedestrianStrategy.ONECHOICE){
				pedestriansList.get(a).costFunction=1;
				pedestriansList.get(a).blocked=false;
				pedestriansList.get(a).distanceToMostDistantExit=0;
			} else {
				pedestriansList.get(a).blocked=false;
				if (pedestriansList.get(a).onGrid==false)
					pedestriansList.get(a).costFunction=1;
			}
		}
		
		for (int a=0;a<pedestriansList.size();a++){
			if (pedestriansList.get(a).onGrid==true){
				if (pedestriansList.get(a).strategy!=pedestrianStrategy.ONECHOICE || stage==1){
					for (int b=0;b<exitList.size();b++){
						//Wyznaczamy najdalsze wyjscie
						double distanceWidth = Math.abs(((pedestriansList.get(a).width)-(exitList.get(b).width)));
						double distanceHeight = Math.abs(((pedestriansList.get(a).height)-(exitList.get(b).height)));
						double distanceToExitB;
						
						// Z twierdzenia Pitagorasa
						distanceToExitB = Math.sqrt((Math.pow(distanceWidth, 2))+(Math.pow(distanceHeight, 2)));		

						if (distanceToExitB>pedestriansList.get(a).distanceToMostDistantExit)
							pedestriansList.get(a).distanceToMostDistantExit=distanceToExitB;
					}
					for (int b=0;b<exitList.size();b++){
						//
						double distanceWidth = Math.abs(((pedestriansList.get(a).width)-(exitList.get(b).width)));
						double distanceHeight = Math.abs(((pedestriansList.get(a).height)-(exitList.get(b).height)));
						double distanceToExitB;
						
						double normalizedDistanceValueForExitB;
						double crowdDensityValueForExitB;
						double changingCrowdDensityRateValueForExitB;
						double costFunctionForExitB;
			
						// Z twierdzenia Pitagorasa
						distanceToExitB = Math.sqrt((Math.pow(distanceWidth, 2))+(Math.pow(distanceHeight, 2)));
						
						

							pedestriansList.get(a).distanceValues[b] = (double)distanceToExitB/(double)pedestriansList.get(a).distanceToMostDistantExit;
							pedestriansList.get(a).crowdDensityValues[b] = exitList.get(b).crowdDensity;
							pedestriansList.get(a).changingCrowdDensityValues[b] = exitList.get(b).changingCrowdDensityRate;

							
							
						normalizedDistanceValueForExitB = ((double)distanceToExitB/(double)pedestriansList.get(a).distanceToMostDistantExit)*(pedestriansList.get(a).distanceWeight);
						crowdDensityValueForExitB = (exitList.get(b).crowdDensity)*(pedestriansList.get(a).crowdDensityWeight);
						changingCrowdDensityRateValueForExitB = (exitList.get(b).changingCrowdDensityRate)*(pedestriansList.get(a).changingCrowdDensityWeight);

						costFunctionForExitB = normalizedDistanceValueForExitB + crowdDensityValueForExitB + changingCrowdDensityRateValueForExitB;
						
						costFunctionForExitB = Math.floor(costFunctionForExitB * 1000000)/1000000;
						
						if (costFunctionForExitB<pedestriansList.get(a).costFunction){
							pedestriansList.get(a).costFunction=costFunctionForExitB;
							pedestriansList.get(a).distanceToDesiredExit=distanceToExitB;
							pedestriansList.get(a).indexOfDesiredExit=b;
						} else
						if (costFunctionForExitB==pedestriansList.get(a).costFunction){
							Random r = new Random();
							double randomValue = r.nextDouble();
							if (randomValue>0.5){
								pedestriansList.get(a).costFunction=costFunctionForExitB;
								pedestriansList.get(a).distanceToDesiredExit=distanceToExitB;
								pedestriansList.get(a).indexOfDesiredExit=b;
							}
						}
					}
				}
			}
		}	
	}
	
	//Ustalamy kolejność ruchu według kosztu funkcji
	public void sortPedestriansList(){
		for (int a=0;a<pedestriansList.size();a++){
			pedestriansList.get(a).setmovementOrder(a+1);  //Pierwszy, drugi, trzeci, czwarty
		}
		
		int x;
		
		for (int z=0;z<pedestriansList.size();z++){
		for (int a=0;a<pedestriansList.size();a++){
			for (int b=0;b<pedestriansList.size();b++){
				
				if ((pedestriansList.get(a).costFunction)>(pedestriansList.get(b).costFunction)){
					if ((pedestriansList.get(a).movementOrder)<(pedestriansList.get(b).movementOrder)){
						x=pedestriansList.get(a).movementOrder;
						pedestriansList.get(a).movementOrder=pedestriansList.get(b).movementOrder;
						pedestriansList.get(b).movementOrder=x;
					}
				}
				
				if ((pedestriansList.get(a).costFunction)==(pedestriansList.get(b).costFunction) && pedestriansList.get(a).costFunction!=1){
					if ((pedestriansList.get(a).distanceToDesiredExit)>(pedestriansList.get(b).distanceToDesiredExit)){
						if ((pedestriansList.get(a).movementOrder)<(pedestriansList.get(b).movementOrder)){
							x=pedestriansList.get(a).movementOrder;
							pedestriansList.get(a).movementOrder=pedestriansList.get(b).movementOrder;
							pedestriansList.get(b).movementOrder=x;
						}
					} else
					if ((pedestriansList.get(a).distanceToDesiredExit)==(pedestriansList.get(b).distanceToDesiredExit)){
						Random r = new Random();
						double randomValue = r.nextDouble();
						if (randomValue>0.5){
							x=pedestriansList.get(a).movementOrder;
							pedestriansList.get(a).movementOrder=pedestriansList.get(b).movementOrder;
							pedestriansList.get(b).movementOrder=x;
						}
					}	
				}
			}
		}
		}
	}
	
	// random wieksze od pol - przechodzień woli isc w SWOJE lewo; mniejsze - przechodzien woli isc w SWOJE prawo
	public void makeMove(){
		
		int order=1;
		
		for (int a=0;a<getNumberOfPedestrians();a++){
			for (int b=0;b<pedestriansList.size();b++){
				if (pedestriansList.get(b).movementOrder==order){
					if (pedestriansList.get(b).onGrid==true){
						totalNumberOfMovesToSolve++;
						int xP = pedestriansList.get(b).width;												//width Pedestrian
						int yP = pedestriansList.get(b).height;												//height Pedestrian
						int xE = exitList.get((pedestriansList.get(b).indexOfDesiredExit)).width;			//width Exit
						int yE = exitList.get((pedestriansList.get(b).indexOfDesiredExit)).height;			//height Exit
						
						Random r = new Random();
						double randomValue = r.nextDouble();
						
						//idź na północ
						if (xP==xE && yP>yE)
							{
								if 	(cellGridTable[xP][yP-1].occupied==false){
									movePedestiran(xP, yP, xP, (yP-1), b);
								} else if 	(cellGridTable[xP-1][yP-1].occupied==false && randomValue>0.5){
									movePedestiran(xP, yP, (xP-1), (yP-1), b);
								} else if 	(cellGridTable[xP+1][yP-1].occupied==false && randomValue<0.5){
									movePedestiran(xP, yP, (xP+1), (yP-1), b);
								} else if 	(cellGridTable[xP-1][yP-1].occupied==false && cellGridTable[xP+1][yP-1].occupied==true){
									movePedestiran(xP, yP, (xP-1), (yP-1), b);
								} else if 	(cellGridTable[xP-1][yP-1].occupied==true && cellGridTable[xP+1][yP-1].occupied==false){
									movePedestiran(xP, yP, (xP+1), (yP-1), b);
								} else {
									pedestriansList.get(getPedestrianIndexFromList(xP,yP)).blocked=true;
								}
							}
						//idź na południe
						if (xP==xE && yP<yE)
						{
							if 	(cellGridTable[xP][yP+1].occupied==false){
								movePedestiran(xP, yP, xP, (yP+1), b);
							} else if 	(cellGridTable[xP+1][yP+1].occupied==false && randomValue>0.5){
								movePedestiran(xP, yP, (xP+1), (yP+1), b);
							} else if 	(cellGridTable[xP-1][yP+1].occupied==false && randomValue<0.5){
								movePedestiran(xP, yP, (xP-1), (yP+1), b);
							} else if 	(cellGridTable[xP+1][yP+1].occupied==false && cellGridTable[xP-1][yP+1].occupied==true){
								movePedestiran(xP, yP, (xP+1), (yP+1), b);
							} else if 	(cellGridTable[xP+1][yP+1].occupied==true && cellGridTable[xP-1][yP+1].occupied==false){
								movePedestiran(xP, yP, (xP-1), (yP+1), b);
							} else {
								pedestriansList.get(getPedestrianIndexFromList(xP,yP)).blocked=true;
							}
						}
						//idź na zachód
						if (xP>xE && yP==yE)
							if 	(cellGridTable[xP-1][yP].occupied==false){
								movePedestiran(xP, yP, (xP-1), yP, b);
							} else if 	(cellGridTable[xP-1][yP+1].occupied==false && randomValue>0.5){
								movePedestiran(xP, yP, (xP-1), (yP+1), b);
							} else if 	(cellGridTable[xP-1][yP-1].occupied==false && randomValue<0.5){
								movePedestiran(xP, yP, (xP-1), (yP-1), b);
							} else if 	(cellGridTable[xP-1][yP+1].occupied==false && cellGridTable[xP-1][yP-1].occupied==true){
								movePedestiran(xP, yP, (xP-1), (yP+1), b);
							} else if 	(cellGridTable[xP-1][yP+1].occupied==true && cellGridTable[xP-1][yP-1].occupied==false){
								movePedestiran(xP, yP, (xP-1), (yP-1), b);
							} else {
								pedestriansList.get(getPedestrianIndexFromList(xP,yP)).blocked=true;
							}
						//idź na wschód
						if (xP<xE && yP==yE)
							if 	(cellGridTable[xP+1][yP].occupied==false){
								movePedestiran(xP, yP, (xP+1), yP, b);
							} else if 	(cellGridTable[xP+1][yP-1].occupied==false && randomValue>0.5){
								movePedestiran(xP, yP, (xP+1), (yP-1), b);
							} else if 	(cellGridTable[xP+1][yP+1].occupied==false && randomValue<0.5){
								movePedestiran(xP, yP, (xP+1), (yP+1), b);
							} else if 	(cellGridTable[xP+1][yP-1].occupied==false && cellGridTable[xP+1][yP+1].occupied==true){
								movePedestiran(xP, yP, (xP+1), (yP-1), b);
							} else if 	(cellGridTable[xP+1][yP-1].occupied==true && cellGridTable[xP+1][yP+1].occupied==false){
								movePedestiran(xP, yP, (xP+1), (yP+1), b);
							} else {
								pedestriansList.get(getPedestrianIndexFromList(xP,yP)).blocked=true;
							}
						//idź na północny-zachód
						if (xP>xE && yP>yE)
							if 	(cellGridTable[xP-1][yP-1].occupied==false){
								movePedestiran(xP, yP, (xP-1), (yP-1), b);
							} else if 	(cellGridTable[xP-1][yP].occupied==false && randomValue>0.5){
								movePedestiran(xP, yP, (xP-1), yP, b);
							} else if 	(cellGridTable[xP][yP-1].occupied==false && randomValue<0.5){
								movePedestiran(xP, yP, xP, (yP-1), b);
							} else if 	(cellGridTable[xP-1][yP].occupied==false && cellGridTable[xP][yP-1].occupied==true){
								movePedestiran(xP, yP, (xP-1), yP, b);
							} else if 	(cellGridTable[xP-1][yP].occupied==true && cellGridTable[xP][yP-1].occupied==false){
								movePedestiran(xP, yP, (xP), (yP-1), b);
							} else {
								pedestriansList.get(getPedestrianIndexFromList(xP,yP)).blocked=true;
							}
						//idź na południowy-zachód
						if (xP>xE && yP<yE)
							if 	(cellGridTable[xP-1][yP+1].occupied==false){
								movePedestiran(xP, yP, (xP-1), (yP+1), b);
							} else if 	(cellGridTable[xP][yP+1].occupied==false && randomValue>0.5){
								movePedestiran(xP, yP, (xP), yP+1, b);
							} else if 	(cellGridTable[xP-1][yP].occupied==false && randomValue<0.5){
								movePedestiran(xP, yP, xP-1, yP, b);
							} else if 	(cellGridTable[xP][yP+1].occupied==false && cellGridTable[xP-1][yP].occupied==true){
								movePedestiran(xP, yP, xP, yP+1, b);
							} else if 	(cellGridTable[xP][yP+1].occupied==true && cellGridTable[xP-1][yP].occupied==false){
								movePedestiran(xP, yP, (xP-1), yP, b);
							} else {
								pedestriansList.get(getPedestrianIndexFromList(xP,yP)).blocked=true;
							}
						//idź na północny-wschód
						if (xP<xE && yP>yE)
							if 	(cellGridTable[xP+1][yP-1].occupied==false){
								movePedestiran(xP, yP, (xP+1), (yP-1), b);
							} else if 	(cellGridTable[xP][yP-1].occupied==false && randomValue>0.5){
								movePedestiran(xP, yP, (xP), yP-1, b);
							} else if 	(cellGridTable[xP+1][yP].occupied==false && randomValue<0.5){
								movePedestiran(xP, yP, xP+1, yP, b);
							} else if 	(cellGridTable[xP][yP-1].occupied==false && cellGridTable[xP+1][yP].occupied==true){
								movePedestiran(xP, yP, xP, yP-1, b);
							} else if 	(cellGridTable[xP][yP-1].occupied==true && cellGridTable[xP+1][yP].occupied==false){
								movePedestiran(xP, yP, (xP+1), yP, b);
							} else {
								pedestriansList.get(getPedestrianIndexFromList(xP,yP)).blocked=true;
							}
						//idź na południowy-wschód
						if (xP<xE && yP<yE)
							if 	(cellGridTable[xP+1][yP+1].occupied==false){
								movePedestiran(xP, yP, (xP+1), (yP+1), b);
							} else if 	(cellGridTable[xP+1][yP].occupied==false && randomValue>0.5){
								movePedestiran(xP, yP, (xP+1), yP, b);
							} else if 	(cellGridTable[xP][yP+1].occupied==false && randomValue<0.5){
								movePedestiran(xP, yP, xP, yP+1, b);
							} else if 	(cellGridTable[xP+1][yP].occupied==false && cellGridTable[xP][yP+1].occupied==true){
								movePedestiran(xP, yP, (xP+1), yP, b);
							} else if 	(cellGridTable[xP+1][yP].occupied==true && cellGridTable[xP][yP+1].occupied==false){
								movePedestiran(xP, yP, xP, yP+1, b);
							} else {
								pedestriansList.get(getPedestrianIndexFromList(xP,yP)).blocked=true;
							}
					}
else System.out.println("Tu jest blad"+pedestriansList.get(b).costFunction);
				}		
			}
			order++;
		}		
	}

	//zerujemy informacje o komorkach przy wyjsciach i liczymy na nowo
	//Liczenie komórek w zależności od położenia komórki - aby nie wyjść poza tablice
	//Dla stage 0 wykonywany jest po każdej zmianie na macierzy komórek. Potem wykonywany na koncu kazdego kroku

	public void calculateExitValues(){		
		
		for (int a=0;a<exitList.size();a++){
			exitList.get(a).numberOfCellsInNeighborhood=0;
			exitList.get(a).ocupiedCellsInNeighborhood=0;
		}
				
		for (int a=0;a<exitList.size();a++){
			if (exitList.get(a).width==0 && exitList.get(a).height==0){
				for(int x=exitList.get(a).width;x<(exitList.get(a).width+mooreRadius+1);x++)
					for(int y=exitList.get(a).height;y<(exitList.get(a).height+mooreRadius+1);y++){
						exitList.get(a).checkIfCellIsOcupied(cellGridTable[x][y].state);
						if (cellGridTable[x][y].state==cellState.PEDESTRIAN)
							if (pedestriansList.get(getPedestrianIndexFromList(x,y)).strategy==pedestrianStrategy.BUSINESSMAN || pedestriansList.get(getPedestrianIndexFromList(x,y)).strategy==pedestrianStrategy.RANDOM || pedestriansList.get(getPedestrianIndexFromList(x,y)).strategy==pedestrianStrategy.SPRINTER)
								pedestriansList.get(getPedestrianIndexFromList(x,y)).setStrategy(pedestrianStrategy.ANT);
					}
				
				} else
			if (exitList.get(a).width==(this.width-1) && exitList.get(a).height==0){
				for(int x=exitList.get(a).width-mooreRadius;x<(exitList.get(a).width+1);x++)
					for(int y=exitList.get(a).height;y<(exitList.get(a).height+mooreRadius+1);y++){
						exitList.get(a).checkIfCellIsOcupied(cellGridTable[x][y].state);
						if (cellGridTable[x][y].state==cellState.PEDESTRIAN)
							if (pedestriansList.get(getPedestrianIndexFromList(x,y)).strategy==pedestrianStrategy.BUSINESSMAN || pedestriansList.get(getPedestrianIndexFromList(x,y)).strategy==pedestrianStrategy.RANDOM || pedestriansList.get(getPedestrianIndexFromList(x,y)).strategy==pedestrianStrategy.SPRINTER)
								pedestriansList.get(getPedestrianIndexFromList(x,y)).setStrategy(pedestrianStrategy.ANT);
					}
			} else
			if (exitList.get(a).width==0 && exitList.get(a).height==(this.height-1)){
				for(int x=exitList.get(a).width;x<(exitList.get(a).width+mooreRadius+1);x++)
					for(int y=exitList.get(a).height-mooreRadius;y<(exitList.get(a).height+1);y++){
						exitList.get(a).checkIfCellIsOcupied(cellGridTable[x][y].state);
						if (cellGridTable[x][y].state==cellState.PEDESTRIAN)
							if (pedestriansList.get(getPedestrianIndexFromList(x,y)).strategy==pedestrianStrategy.BUSINESSMAN || pedestriansList.get(getPedestrianIndexFromList(x,y)).strategy==pedestrianStrategy.RANDOM || pedestriansList.get(getPedestrianIndexFromList(x,y)).strategy==pedestrianStrategy.SPRINTER)
								pedestriansList.get(getPedestrianIndexFromList(x,y)).setStrategy(pedestrianStrategy.ANT);
					}
			} else
			if (exitList.get(a).width==(this.width-1) && exitList.get(a).height==(this.height-1)){
				for(int x=(exitList.get(a).width-mooreRadius);x<(exitList.get(a).width+1);x++)
					for(int y=(exitList.get(a).height-mooreRadius);y<(exitList.get(a).height+1);y++){
						exitList.get(a).checkIfCellIsOcupied(cellGridTable[x][y].state);
						if (cellGridTable[x][y].state==cellState.PEDESTRIAN)
							if (pedestriansList.get(getPedestrianIndexFromList(x,y)).strategy==pedestrianStrategy.BUSINESSMAN || pedestriansList.get(getPedestrianIndexFromList(x,y)).strategy==pedestrianStrategy.RANDOM || pedestriansList.get(getPedestrianIndexFromList(x,y)).strategy==pedestrianStrategy.SPRINTER)
								pedestriansList.get(getPedestrianIndexFromList(x,y)).setStrategy(pedestrianStrategy.ANT);
					}
			} else
			if (exitList.get(a).width==0 && exitList.get(a).height!=0 && exitList.get(a).height!=(this.height-1)){
				for(int x=exitList.get(a).width;x<(exitList.get(a).width+mooreRadius+1);x++)
					for(int y=exitList.get(a).height-mooreRadius;y<(exitList.get(a).height+mooreRadius+1);y++){
						exitList.get(a).checkIfCellIsOcupied(cellGridTable[x][y].state);
						if (cellGridTable[x][y].state==cellState.PEDESTRIAN)
							if (pedestriansList.get(getPedestrianIndexFromList(x,y)).strategy==pedestrianStrategy.BUSINESSMAN || pedestriansList.get(getPedestrianIndexFromList(x,y)).strategy==pedestrianStrategy.RANDOM || pedestriansList.get(getPedestrianIndexFromList(x,y)).strategy==pedestrianStrategy.SPRINTER)
								pedestriansList.get(getPedestrianIndexFromList(x,y)).setStrategy(pedestrianStrategy.ANT);
					}
			} else
			if (exitList.get(a).width==(this.width-1) && exitList.get(a).height!=0 && exitList.get(a).height!=(this.height-1)){
				for(int x=exitList.get(a).width-mooreRadius;x<(exitList.get(a).width+1);x++)
					for(int y=exitList.get(a).height-mooreRadius;y<(exitList.get(a).height+mooreRadius+1);y++){
						exitList.get(a).checkIfCellIsOcupied(cellGridTable[x][y].state);
						if (cellGridTable[x][y].state==cellState.PEDESTRIAN)
							if (pedestriansList.get(getPedestrianIndexFromList(x,y)).strategy==pedestrianStrategy.BUSINESSMAN || pedestriansList.get(getPedestrianIndexFromList(x,y)).strategy==pedestrianStrategy.RANDOM || pedestriansList.get(getPedestrianIndexFromList(x,y)).strategy==pedestrianStrategy.SPRINTER)
								pedestriansList.get(getPedestrianIndexFromList(x,y)).setStrategy(pedestrianStrategy.ANT);
					}
			} else
			if (exitList.get(a).width!=0 && exitList.get(a).width!=(this.width-1) && exitList.get(a).height==0){
				for(int x=exitList.get(a).width-mooreRadius;x<(exitList.get(a).width+mooreRadius+1);x++)
					for(int y=exitList.get(a).height;y<(exitList.get(a).height+mooreRadius+1);y++){
						exitList.get(a).checkIfCellIsOcupied(cellGridTable[x][y].state);
						if (cellGridTable[x][y].state==cellState.PEDESTRIAN)
							if (pedestriansList.get(getPedestrianIndexFromList(x,y)).strategy==pedestrianStrategy.BUSINESSMAN || pedestriansList.get(getPedestrianIndexFromList(x,y)).strategy==pedestrianStrategy.RANDOM || pedestriansList.get(getPedestrianIndexFromList(x,y)).strategy==pedestrianStrategy.SPRINTER)
								pedestriansList.get(getPedestrianIndexFromList(x,y)).setStrategy(pedestrianStrategy.ANT);
					}
			} else
			if (exitList.get(a).width!=0 && exitList.get(a).width!=(this.width-1) && exitList.get(a).height==(this.height-1)){
				for(int x=exitList.get(a).width-mooreRadius;x<(exitList.get(a).width+mooreRadius+1);x++)
					for(int y=exitList.get(a).height-mooreRadius;y<(exitList.get(a).height+1);y++){
						exitList.get(a).checkIfCellIsOcupied(cellGridTable[x][y].state);
						if (cellGridTable[x][y].state==cellState.PEDESTRIAN)
							if (pedestriansList.get(getPedestrianIndexFromList(x,y)).strategy==pedestrianStrategy.BUSINESSMAN || pedestriansList.get(getPedestrianIndexFromList(x,y)).strategy==pedestrianStrategy.RANDOM || pedestriansList.get(getPedestrianIndexFromList(x,y)).strategy==pedestrianStrategy.SPRINTER)
								pedestriansList.get(getPedestrianIndexFromList(x,y)).setStrategy(pedestrianStrategy.ANT);
					}
			} else{
				for(int x=(exitList.get(a).width-mooreRadius);x<(exitList.get(a).width+mooreRadius+1);x++)
					for(int y=(exitList.get(a).height-mooreRadius);y<(exitList.get(a).height+mooreRadius+1);y++){
						exitList.get(a).checkIfCellIsOcupied(cellGridTable[x][y].state);
						if (cellGridTable[x][y].state==cellState.PEDESTRIAN)
							if (pedestriansList.get(getPedestrianIndexFromList(x,y)).strategy==pedestrianStrategy.BUSINESSMAN || pedestriansList.get(getPedestrianIndexFromList(x,y)).strategy==pedestrianStrategy.RANDOM || pedestriansList.get(getPedestrianIndexFromList(x,y)).strategy==pedestrianStrategy.SPRINTER)
								pedestriansList.get(getPedestrianIndexFromList(x,y)).setStrategy(pedestrianStrategy.ANT);
					}
			}
			
			if (stage==0)
				exitList.get(a).calculateCrowdDensityForStage0();
			else
				exitList.get(a).calculateCrowdDensity();
		}
	}
	
	public Boolean checkIfAllBlocked(){
		Boolean retValue=false;
		int blockedCounter=0;
		int stageCounter=0;
		
		if (totalNumberOfPedestrians<10)
			stageCounter=15*totalNumberOfPedestrians;
		else if (totalNumberOfPedestrians>=10 && totalNumberOfPedestrians<100)
			stageCounter=10*totalNumberOfPedestrians;
		else if (totalNumberOfPedestrians>=100)
			stageCounter=5*totalNumberOfPedestrians;

		
		for(int a=0;a<pedestriansList.size();a++){
			if (pedestriansList.get(a).blocked==true)
				blockedCounter++;
		}
		
		if (blockedCounter==numberOfPedestriansOnGrid)
			retValue=true;
		
		if (stage==stageCounter)
			retValue=true;
		
		return retValue;
	}
	
	
	
	
	
	
	
	
	//Funkcja używana jednie dla stage=0 przy tworzeniu macierzy komórek, list przechodnów oraz wyjść.
	public void changeCell(int width, int height, cellState newState){		
		if (newState==cellState.CLEAR){		
			if (cellGridTable[width][height].getState()==cellState.WALL){
				cellGridTable[width][height].setState(cellState.CLEAR);
			}		
			if (cellGridTable[width][height].getState()==cellState.PEDESTRIAN){
				decreaseNumberOfPedestrians();
				removePedestrianFromList(width, height);
				cellGridTable[width][height].setState(cellState.CLEAR);
			}
			if (cellGridTable[width][height].getState()==cellState.EXIT){
				decreaseNumberOfExits();
				removeExitFromList(width, height);
				cellGridTable[width][height].setState(cellState.CLEAR);
			}
		}
		if (newState==cellState.WALL){		
			if (cellGridTable[width][height].getState()==cellState.CLEAR){
				cellGridTable[width][height].setState(cellState.WALL);
			}		
			if (cellGridTable[width][height].getState()==cellState.PEDESTRIAN){
				decreaseNumberOfPedestrians();
				removePedestrianFromList(width, height);
				cellGridTable[width][height].setState(cellState.WALL);
			}
			if (cellGridTable[width][height].getState()==cellState.EXIT){
				decreaseNumberOfExits();
				removeExitFromList(width, height);
				cellGridTable[width][height].setState(cellState.WALL);
			}
		}
		if (newState==cellState.PEDESTRIAN){		
			if (cellGridTable[width][height].getState()==cellState.CLEAR){
				//increaseNumberOfPedestrians();
				//cellGridTable[width][height].setState(cellState.PEDESTRIAN);
				addPedestrianToList(width, height);
			}		
			if (cellGridTable[width][height].getState()==cellState.WALL){
				//increaseNumberOfPedestrians();
				//cellGridTable[width][height].setState(cellState.PEDESTRIAN);
				addPedestrianToList(width, height);
			}
			if (cellGridTable[width][height].getState()==cellState.EXIT){
				decreaseNumberOfExits();
				removeExitFromList(width, height);
				//increaseNumberOfPedestrians();
				//cellGridTable[width][height].setState(cellState.PEDESTRIAN);
				addPedestrianToList(width, height);
			}
		}
		if (newState==cellState.EXIT){		
			if (cellGridTable[width][height].getState()==cellState.CLEAR){
				//increaseNumberOfExits();
				//cellGridTable[width][height].setState(cellState.EXIT);
				addExitToList(width, height);
			}		
			if (cellGridTable[width][height].getState()==cellState.WALL){
				//increaseNumberOfExits();
				//cellGridTable[width][height].setState(cellState.EXIT);
				addExitToList(width, height);
			}
			if (cellGridTable[width][height].getState()==cellState.PEDESTRIAN){
				decreaseNumberOfPedestrians();
				removePedestrianFromList(width, height);
				//increaseNumberOfExits();
				//cellGridTable[width][height].setState(cellState.EXIT);
				addExitToList(width, height);
			}
		}
		calculateExitValues();
	}
	
	public void createInitialFiles(){
		
		PrintWriter writer;
		int numberOfInnerWalls=0;
		try {
			writer = new PrintWriter("in_people.txt");
			writer.println(numberOfPedestriansOnGrid);
			for (int x=0;x<numberOfPedestriansOnGrid;x++){
				writer.println(pedestriansList.get(x).width+" "+pedestriansList.get(x).height);
			}		
			writer.close();
			
			writer = new PrintWriter("in_data.txt");
			writer.println(this.width+" "+this.height);
			writer.println();
			writer.println(numberOfExits);
			for (int x=0;x<numberOfExits;x++){
				writer.println("1 "+exitList.get(x).width+" "+exitList.get(x).height);
			}
			writer.println();
			for (int x=1;x<this.width-1;x++){
				for (int y=1;y<this.height-1;y++){
					if (cellGridTable[x][y].state==cellState.WALL)
						numberOfInnerWalls++;
				}
			}
			writer.println(numberOfInnerWalls);
			for (int x=1;x<this.width-1;x++){
				for (int y=1;y<this.height-1;y++){
					if (cellGridTable[x][y].state==cellState.WALL)
						writer.println("1 "+x+" "+y);
				}
			}
				
			writer.close();						
			
		} catch (Exception e) {
			System.out.println("Blad");
		}
	}
	
	public void createOutputFile(){
		PrintWriter writer, writer2;
		BufferedReader reader;
		String s;
		StringBuilder sBuilder;
		

		//dla stage = 0
		if (stage==0){
			try {
				writer = new PrintWriter("out_moves.txt");
				writer.close();
				writer = new PrintWriter(new BufferedWriter(new FileWriter("out_moves.txt", true)));
				for (int x=0;x<pedestriansList.size();x++){
					writer.println(x+" 0 "
								+pedestriansList.get(x).width+" "+pedestriansList.get(x).height+" "
								+pedestriansList.get(x).oldwidth+" "+pedestriansList.get(x).oldheight+" "
								+stage+" "+numberOfPedestriansOnGrid);
				}
				writer.println();
				writer.println();
				writer.close();
				
			} catch (Exception e) {
				System.out.println("makeOutputFile() file error");
			}
		}
		else{
			try {
				writer = new PrintWriter(new BufferedWriter(new FileWriter("out_moves.txt", true)));
				if (solved==true){
					
					writer.println(600+" 0 0 0 0 "+stage+" "+numberOfPedestriansOnGrid);
					writer.close();
					
					reader = new BufferedReader(new FileReader("out_moves.txt"));
					sBuilder = new StringBuilder();
				    String ls = System.getProperty("line.separator");
				    
					sBuilder.append(totalNumberOfMovesToSolve+ls+pedestriansList.size()+ls+stage+ls+ls);
					
					while (( s=reader.readLine() ) != null)
					{
						sBuilder.append(s);
						sBuilder.append( ls );
					}
					reader.close();

					writer2 = new PrintWriter("out_moves.txt");
					writer2.write(sBuilder.toString());
					writer2.close();	

					
				} else {
					for (int x=0;x<pedestriansList.size();x++){
						writer.print(x);
						if (pedestriansList.get(x).onGrid==true)
							writer.print(" 0 ");
						else
							writer.print(" 1 ");
						writer.print(pedestriansList.get(x).width+" "+pedestriansList.get(x).height+" ");
						writer.print(pedestriansList.get(x).oldwidth+" "+pedestriansList.get(x).oldheight+" ");
						writer.println(stage+" "+numberOfPedestriansOnGrid);
						writer.println();
					}
				writer.close();
				}
			} catch (Exception e) {
				System.out.println("makeOutputFile() file error");
			}	
		}
		
		//dla stage = 0
		if (stage==0){
			try {
				writer = new PrintWriter("out_info.txt");
				writer.close();
				writer = new PrintWriter(new BufferedWriter(new FileWriter("out_info.txt", true)));
				for (int x=0;x<pedestriansList.size();x++){
					writer.print(x);
					
					if (pedestriansList.get(x).onExit==true)
						writer.print(" 1 ");
					else
						writer.print(" 0 ");
					
					if (pedestriansList.get(x).blocked==true)
						writer.print("1 ");
					else
						writer.print("0 ");
					
					writer.print(pedestriansList.get(x).indexOfDesiredExit+" "+pedestriansList.get(x).movementOrder);
					
					if (pedestriansList.get(x).strategy==pedestrianStrategy.SPRINTER)
						writer.print(" 0 ");
					else if (pedestriansList.get(x).strategy==pedestrianStrategy.BALANCE)
						writer.print(" 1 ");
					else if (pedestriansList.get(x).strategy==pedestrianStrategy.BUSINESSMAN)
						writer.print(" 2 ");
					else if (pedestriansList.get(x).strategy==pedestrianStrategy.ANT)
						writer.print(" 3 ");
					else if (pedestriansList.get(x).strategy==pedestrianStrategy.SAMPLING)
						writer.print(" 4 ");
					else if (pedestriansList.get(x).strategy==pedestrianStrategy.LOOKANDGO)
						writer.print(" 5 ");
					else if (pedestriansList.get(x).strategy==pedestrianStrategy.ONECHOICE)
						writer.print(" 6 ");
					else if (pedestriansList.get(x).strategy==pedestrianStrategy.RANDOM)
						writer.print(" 7 ");
					
					writer.println(pedestriansList.get(x).distanceWeight+" "+pedestriansList.get(x).crowdDensityWeight+" "
							+pedestriansList.get(x).changingCrowdDensityWeight+" "+pedestriansList.get(x).costFunction);
				}
				writer.println();
				writer.println();
				writer.close();
				
			} catch (Exception e) {
				System.out.println("makeOutputFile() file error");
			}
		}
		else{
			try {
				writer = new PrintWriter(new BufferedWriter(new FileWriter("out_info.txt", true)));
				if (solved==true){;}
				else {
					for (int x=0;x<pedestriansList.size();x++){
						writer.print(x);
						
						if (pedestriansList.get(x).onExit==true)
							writer.print(" 1 ");
						else
							writer.print(" 0 ");
						
						if (pedestriansList.get(x).blocked==true)
							writer.print("1 ");
						else
							writer.print("0 ");
						writer.print(pedestriansList.get(x).indexOfDesiredExit+" "+pedestriansList.get(x).movementOrder);
						
						if (pedestriansList.get(x).strategy==pedestrianStrategy.SPRINTER)
							writer.print(" 0 ");
						else if (pedestriansList.get(x).strategy==pedestrianStrategy.BALANCE)
							writer.print(" 1 ");
						else if (pedestriansList.get(x).strategy==pedestrianStrategy.BUSINESSMAN)
							writer.print(" 2 ");
						else if (pedestriansList.get(x).strategy==pedestrianStrategy.ANT)
							writer.print(" 3 ");
						else if (pedestriansList.get(x).strategy==pedestrianStrategy.SAMPLING)
							writer.print(" 4 ");
						else if (pedestriansList.get(x).strategy==pedestrianStrategy.LOOKANDGO)
							writer.print(" 5 ");
						else if (pedestriansList.get(x).strategy==pedestrianStrategy.ONECHOICE)
							writer.print(" 6 ");
						else if (pedestriansList.get(x).strategy==pedestrianStrategy.RANDOM)
							writer.print(" 7 ");
						
						writer.println(pedestriansList.get(x).distanceWeight+" "+pedestriansList.get(x).crowdDensityWeight+" "
								+pedestriansList.get(x).changingCrowdDensityWeight+" "+pedestriansList.get(x).costFunction);
						writer.println();
					}
				}
				writer.close();
				
				
			} catch (Exception e) {
				System.out.println("makeOutputFile() file error");
			}	
		}	
	}
	
	/*
	public void createValuesFile(){
		PrintWriter writer;
		
		if (stage==1){
			try {
				writer = new PrintWriter("out_values.txt");
				writer.close();
				writer = new PrintWriter(new BufferedWriter(new FileWriter("out_values.txt", true)));
				
				writer.println("Step: "+(stage-1));
				for (int x=0;x<pedestriansList.size();x++){				
					for (int y=0;y<exitList.size();y++){
						
						writer.println(x+"   "+y+"   "+pedestriansList.get(x).distanceValues[y]+
										   "   "+pedestriansList.get(x).crowdDensityValues[y]+
										   "   "+pedestriansList.get(x).changingCrowdDensityValues[y]
									);
					}
					writer.println();
				}
				writer.println();
				writer.println();
				
				writer.close();
			} catch (Exception e) {
				System.out.println("makeOutputFile() file error - out_values");
			}
		} else {
			try {
				writer = new PrintWriter(new BufferedWriter(new FileWriter("out_values.txt", true)));
				writer.println("Step: "+(stage-1));
				for (int x=0;x<pedestriansList.size();x++){				
					for (int y=0;y<exitList.size();y++){
						
						writer.println(x+"   "+y+"   "+pedestriansList.get(x).distanceValues[y]+
										   "   "+pedestriansList.get(x).crowdDensityValues[y]+
										   "   "+pedestriansList.get(x).changingCrowdDensityValues[y]
									);
					}
					writer.println();
				}
				writer.println();
				writer.println();
				
				writer.close();
			} catch (Exception e) {
				System.out.println("makeOutputFile() file error - out_values");
			}
			
		}
		
	}
	*/
	
	public void createStatisticFile(){
		
		PrintWriter writer;
		int sum=0;
		int succesfullEvacuations=0;
		int failedEvacuations=0;
		double arithmeticalAverage;

			try {
				writer = new PrintWriter("out_statistic.txt");
				writer.close();
				writer = new PrintWriter(new BufferedWriter(new FileWriter("out_statistic.txt", true)));
				
				for (int x=0;x<solveXtimes;x++){
					if (evacuationResults[x]==0)
						failedEvacuations++;
					else
						succesfullEvacuations++;
				}
				
				for (int x=0;x<solveXtimes;x++){
					if (evacuationResults[x]!=0)
						sum = sum + evacuationResults[x];
				}
				
				arithmeticalAverage=(double)sum/(double)succesfullEvacuations;
				arithmeticalAverage = Math.floor(arithmeticalAverage * 10000)/10000;
				
				writer.println("Number of evacuation simulations: "+this.solveXtimes);
				writer.println("Number of pedestrians: "+this.totalNumberOfPedestrians);
				writer.println("Room width: "+this.width);
				writer.println("Room heigth: "+this.height);
				writer.println("Moore radius: "+this.mooreRadius);
				writer.println();
				writer.println("Arithmetical Average of succesfull evacuations: "+arithmeticalAverage);
				writer.println();
				writer.println("Succesfull evacuations: "+succesfullEvacuations);
				writer.println("Failed evacuations: "+failedEvacuations);
				writer.println();
				for (int x=0;x<solveXtimes;x++){
					writer.println("Simulation "+x+": "+evacuationResults[x]);
				}
				writer.close();
			} catch (Exception e) {
				System.out.println("createStatisticFile() file error");
			}
					
	}
	
	public void goToStage(int stageNumber, String Path1, String Path2, String Path3){
	    try {
			BufferedReader br1 = new BufferedReader(new FileReader(Path1));
			BufferedReader br2 = new BufferedReader(new FileReader(Path2));
			BufferedReader br3 = new BufferedReader(new FileReader(Path3));
	        StringBuilder sb1 = new StringBuilder();
	        StringBuilder sb2 = new StringBuilder();
	        StringBuilder sb3 = new StringBuilder();
	        String line;
	        String[] stringTable;
	        int x=0;
	        
			while ((line=br1.readLine()) != null)
			{
				sb1.append(line+"\n");
			}
			br1.close();
			
			BufferedReader text=new BufferedReader( new StringReader(sb1.toString()));
			
			while ((line=br2.readLine()) != null)
			{
				sb2.append(line+"\n");
			}
			br2.close();
			
			BufferedReader text2=new BufferedReader( new StringReader(sb2.toString()));
			
			while ((line=br3.readLine()) != null)
			{
				sb3.append(line+"\n");
			}
			br3.close();
			
			BufferedReader text3=new BufferedReader( new StringReader(sb3.toString()));
	        
			for (int a=0;a<width;a++){
				for (int b=0;b<height;b++){
					if (cellGridTable[a][b].state==cellState.PEDESTRIAN)
						cellGridTable[a][b].setState(cellState.CLEAR);
					if (cellGridTable[a][b].state==cellState.PEDESTRIANONEXIT)
						cellGridTable[a][b].setState(cellState.EXIT);					
				}
			}
			
			for (int a=0;a<pedestriansList.size();a++){
				pedestriansList.get(a).blocked=false;
				pedestriansList.get(a).onExit=false;
			}
			
			
			text2.readLine();
			text2.readLine();
			line = text2.readLine();
			x = Integer.parseInt(line);
			for (int a=0;a<x;a++){
				line = text2.readLine();
				stringTable = line.split(" ");
				
				int b = Integer.parseInt(stringTable[1]);
				int c = Integer.parseInt(stringTable[2]);
				exitList.get(a).width= b;
				exitList.get(a).height= c;
				cellGridTable[b][c].setState(cellState.EXIT);
			}
			text2.readLine();
			line = text2.readLine();
			x = Integer.parseInt(line);
			for (int a=0;a<x;a++){
				line = text2.readLine();
				stringTable = line.split(" ");
				
				int b = Integer.parseInt(stringTable[1]);
				int c = Integer.parseInt(stringTable[2]);
				cellGridTable[b][c].setState(cellState.WALL);
			}
			
			if (stageNumber==0){
				for (x=0;x<4;x++)
					text.readLine();
				while (x<(4+(pedestriansList.size())))
				{
					line = text.readLine();
					x++;
					stringTable = line.split(" ");

					if (Integer.parseInt(stringTable[1])==0){
						
						String aa = stringTable[2];
						String bb = stringTable[3];
						
						int a = Integer.parseInt(aa);
						int b = Integer.parseInt(bb);
						int index = Integer.parseInt(stringTable[0]);
						
						pedestriansList.get(index).onGrid=true;
						pedestriansList.get(index).width=a;
						pedestriansList.get(index).height=b;		
						
						cellGridTable[a][b].setState(cellState.PEDESTRIAN);
						
					}
					else {
						pedestriansList.get(Integer.parseInt(stringTable[0])).onGrid=false;
					}
				}
			} else{
				for (x=0;x<6+pedestriansList.size()+(pedestriansList.size()*2*(stageNumber-1));x++)
					text.readLine();
				while (x<(6+pedestriansList.size()+(pedestriansList.size()*2*stageNumber)))
				{
					x++;x++;
					line = text.readLine();
					stringTable = line.split(" ");

					if (Integer.parseInt(stringTable[1])==0){
						
						int a = Integer.parseInt(stringTable[2]);
						int b = Integer.parseInt(stringTable[3]);
						int index = Integer.parseInt(stringTable[0]);
						
						pedestriansList.get(index).onGrid=true;
						pedestriansList.get(index).width=a;
						pedestriansList.get(index).height=b;
						cellGridTable[a][b].setState(cellState.PEDESTRIAN);
					}
					else {
						pedestriansList.get(Integer.parseInt(stringTable[0])).onGrid=false;
						
						
					}
					
					line = text.readLine();
				}
			}
			
			
			x=0;
			if (stageNumber==0){
				while (x<pedestriansList.size())
				{
					line = text3.readLine();
					x++;
					stringTable = line.split(" ");
					int index = Integer.parseInt(stringTable[0]);

					if (pedestriansList.get(index).onGrid==true){
						
						int a = Integer.parseInt(stringTable[1]);
						int b = Integer.parseInt(stringTable[2]);
						int c = Integer.parseInt(stringTable[3]);
						int d = Integer.parseInt(stringTable[4]);
						int e = Integer.parseInt(stringTable[5]);
						double f = Double.parseDouble(stringTable[6]);
						double g = Double.parseDouble(stringTable[7]);
						double h = Double.parseDouble(stringTable[8]);
						double i = Double.parseDouble(stringTable[9]);
						
						if (a==1){
							pedestriansList.get(index).onExit=true;
							cellGridTable[pedestriansList.get(index).width][pedestriansList.get(index).height].setState(cellState.PEDESTRIANONEXIT);
						}
						if (b==1){
							pedestriansList.get(index).blocked=true;
						}
						pedestriansList.get(index).indexOfDesiredExit=c;
						pedestriansList.get(index).movementOrder=d;
						
						if (e==0)
							pedestriansList.get(index).strategy=pedestrianStrategy.SPRINTER;
						else if (e==1)
							pedestriansList.get(index).strategy=pedestrianStrategy.BALANCE;
						else if (e==2)
							pedestriansList.get(index).strategy=pedestrianStrategy.BUSINESSMAN;
						else if (e==3)
							pedestriansList.get(index).strategy=pedestrianStrategy.ANT;
						else if (e==4)
							pedestriansList.get(index).strategy=pedestrianStrategy.SAMPLING;
						else if (e==5)
							pedestriansList.get(index).strategy=pedestrianStrategy.LOOKANDGO;
						else if (e==6)
							pedestriansList.get(index).strategy=pedestrianStrategy.ONECHOICE;
						else if (e==7)
							pedestriansList.get(index).strategy=pedestrianStrategy.RANDOM;
						
						pedestriansList.get(index).distanceWeight=f;
						pedestriansList.get(index).crowdDensityWeight=g;
						pedestriansList.get(index).changingCrowdDensityWeight=h;
						pedestriansList.get(index).costFunction=i;	
					}
				}
			} else{
				for (x=0;x<2+pedestriansList.size()+(pedestriansList.size()*2*(stageNumber-1));x++)
					text3.readLine();
				while (x<(2+pedestriansList.size()+(pedestriansList.size()*2*stageNumber)))
				{
					line = text3.readLine();
					x++;x++;
					stringTable = line.split(" ");
					int index = Integer.parseInt(stringTable[0]);

					if (pedestriansList.get(index).onGrid==true){
						
						int a = Integer.parseInt(stringTable[1]);
						int b = Integer.parseInt(stringTable[2]);
						int c = Integer.parseInt(stringTable[3]);
						int d = Integer.parseInt(stringTable[4]);
						int e = Integer.parseInt(stringTable[5]);
						double f = Double.parseDouble(stringTable[6]);
						double g = Double.parseDouble(stringTable[7]);
						double h = Double.parseDouble(stringTable[8]);
						double i = Double.parseDouble(stringTable[9]);
						
						if (a==1){
							pedestriansList.get(index).onExit=true;
							cellGridTable[pedestriansList.get(index).width][pedestriansList.get(index).height].setState(cellState.PEDESTRIANONEXIT);
						}
						if (b==1){
							pedestriansList.get(index).blocked=true;
						}
						pedestriansList.get(index).indexOfDesiredExit=c;
						pedestriansList.get(index).movementOrder=d;
						
						if (e==0)
							pedestriansList.get(index).strategy=pedestrianStrategy.SPRINTER;
						else if (e==1)
							pedestriansList.get(index).strategy=pedestrianStrategy.BALANCE;
						else if (e==2)
							pedestriansList.get(index).strategy=pedestrianStrategy.BUSINESSMAN;
						else if (e==3)
							pedestriansList.get(index).strategy=pedestrianStrategy.ANT;
						else if (e==4)
							pedestriansList.get(index).strategy=pedestrianStrategy.SAMPLING;
						else if (e==5)
							pedestriansList.get(index).strategy=pedestrianStrategy.LOOKANDGO;
						else if (e==6)
							pedestriansList.get(index).strategy=pedestrianStrategy.ONECHOICE;
						else if (e==7)
							pedestriansList.get(index).strategy=pedestrianStrategy.RANDOM;
						
						pedestriansList.get(index).distanceWeight=f;
						pedestriansList.get(index).crowdDensityWeight=g;
						pedestriansList.get(index).changingCrowdDensityWeight=h;
						pedestriansList.get(index).costFunction=i;	
					}
					line = text3.readLine();
				}
			}

		} catch (Exception e) {
			System.out.println("goToStage Error");
		}
	}
	
	
	
	
	
	public void generateRandomPedestirans(int numberOfNewPedestrians, Boolean randomStrategies, pedestrianStrategy strategyForAll){
		Random r = new Random();
		int int1, int2;
		
		while (numberOfNewPedestrians!=0){
			
			int1 = r.nextInt(width);
			int2 = r.nextInt(height);
			
			if (cellGridTable[int1][int2].getState()==cellState.CLEAR)
			{
				addPedestrianToList(int1, int2);
				
				if (randomStrategies==true){
					int1 = r.nextInt(8);
					
					switch (int1){
						case 0:	pedestriansList.get(pedestriansList.size()-1).setStrategy(pedestrianStrategy.SPRINTER);
								break;
						case 1:	pedestriansList.get(pedestriansList.size()-1).setStrategy(pedestrianStrategy.BALANCE);
								break;
						case 2:	pedestriansList.get(pedestriansList.size()-1).setStrategy(pedestrianStrategy.BUSINESSMAN);
								break;
						case 3:	pedestriansList.get(pedestriansList.size()-1).setStrategy(pedestrianStrategy.ANT);
								break;
						case 4:	pedestriansList.get(pedestriansList.size()-1).setStrategy(pedestrianStrategy.SAMPLING);
								break;
						case 5:	pedestriansList.get(pedestriansList.size()-1).setStrategy(pedestrianStrategy.LOOKANDGO);
								break;
						case 6:	pedestriansList.get(pedestriansList.size()-1).setStrategy(pedestrianStrategy.ONECHOICE);
								break;
						case 7:	pedestriansList.get(pedestriansList.size()-1).setStrategy(pedestrianStrategy.RANDOM);
								break;
						}
				}else{
					pedestriansList.get(pedestriansList.size()-1).setStrategy(strategyForAll);
				}
				numberOfNewPedestrians--;
			}
		}
		
	}
	
	public void addPedestrianToList(int width, int height){
		increaseNumberOfPedestrians();
		cellGridTable[width][height].setState(cellState.PEDESTRIAN);
		Pedestrian a = new Pedestrian (width, height);
		pedestriansList.add(a);
	}
	
	public void removePedestrianFromList(int width, int height){
		
		for (int i=0;i<pedestriansList.size();i++){
			if (pedestriansList.get(i).width==width && pedestriansList.get(i).height==height )
				pedestriansList.remove(i);		
		}		
	}
	
	public int getPedestrianIndexFromList(int width, int height){
		int returnedvalue=0;
		for (int i=0;i<pedestriansList.size();i++){
			if (pedestriansList.get(i).width==width && pedestriansList.get(i).height==height)
				if (pedestriansList.get(i).onGrid==true)
					returnedvalue=i;
		}
		
		return returnedvalue;
	}
	
	public void movePedestiran(int x, int y, int newX, int newY, int pedestrianIndex){
		if (cellGridTable[newX][newY].getState()==cellState.CLEAR){
			cellGridTable[x][y].setState(cellState.CLEAR);
			cellGridTable[newX][newY].setState(cellState.PEDESTRIAN);
			pedestriansList.get(pedestrianIndex).oldwidth=x;
			pedestriansList.get(pedestrianIndex).oldheight=y;
			pedestriansList.get(pedestrianIndex).width=newX;
			pedestriansList.get(pedestrianIndex).height=newY;
		}
		if (cellGridTable[newX][newY].getState()==cellState.EXIT){
			cellGridTable[x][y].setState(cellState.CLEAR);
			cellGridTable[newX][newY].setState(cellState.PEDESTRIANONEXIT);
			pedestriansList.get(pedestrianIndex).onExit=true;
			pedestriansList.get(pedestrianIndex).oldwidth=x;
			pedestriansList.get(pedestrianIndex).oldheight=y;
			pedestriansList.get(pedestrianIndex).width=newX;
			pedestriansList.get(pedestrianIndex).height=newY;
		}	
	}
	
	public void addExitToList(int width, int height){
		increaseNumberOfExits();
		cellGridTable[width][height].setState(cellState.EXIT);
		Exit a = new Exit(width, height);
		exitList.add(a);
	}
	
	public void removeExitFromList(int width, int height){
		
		for (int i=0;i<exitList.size();i++){
			if (exitList.get(i).width==width && exitList.get(i).height==height )
				exitList.remove(i);		
		}	
	}
	
	public int getExitIndexFromList(int width, int height){
		int returnedvalue=0;
		for (int i=0;i<exitList.size();i++){
			if (exitList.get(i).width==width && exitList.get(i).height==height )
				returnedvalue=i;
		}
		
		return returnedvalue;
	}
	
	public void increaseNumberOfPedestrians(){
		numberOfPedestriansOnGrid++;
	}

	public void decreaseNumberOfPedestrians(){
		numberOfPedestriansOnGrid--;
	}
	
	public int getNumberOfPedestrians(){
		return numberOfPedestriansOnGrid;
	}
	
	public void increaseNumberOfExits(){
		numberOfExits++;
	}
	
	public void decreaseNumberOfExits(){
		numberOfExits--;
	}

	public int getNumberOfExits(){
		return exitList.size();
	}

	public int getStageNumber(){
		return stage;
	}
}






