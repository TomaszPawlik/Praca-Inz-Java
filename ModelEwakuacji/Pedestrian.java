package ModelEwakuacji;

import java.util.Random;

public class Pedestrian {
	int width;
	int height;
	int oldwidth;
	int oldheight;
	int indexOfDesiredExit=0;
	double distanceToDesiredExit;
	double distanceToMostDistantExit;
	Boolean onGrid;
	Boolean onExit;
	Boolean blocked;
	public pedestrianStrategy strategy;
	
	double costFunction;
	
	public double[] distanceValues;
	public double[] crowdDensityValues;
	public double[] changingCrowdDensityValues;
	
	public double distanceWeight;
	public double crowdDensityWeight;
	public double changingCrowdDensityWeight;
	
	int movementOrder;						//1,2,3,4,5 ... Pierwzsy, drugi, trzeci ....
	
	Pedestrian(int a, int b){
		width = a;
		height = b;
		oldwidth = a;
		oldheight = b;
		distanceToDesiredExit=0;
		distanceToMostDistantExit=0;
		onGrid=true;
		onExit=false;
		blocked=false;
		setStrategy(pedestrianStrategy.ANT);
		
		
		costFunction=1;	
	}
	
	public void setStrategy(pedestrianStrategy strat){
		strategy=strat;
		if (strat==pedestrianStrategy.SPRINTER){	
			Random r = new Random();
			
			double randomValue = r.nextDouble();
			randomValue = (Math.floor(randomValue * 100)/100)+0.05;
			distanceWeight=0.05;
			crowdDensityWeight=(randomValue-0.05);
			changingCrowdDensityWeight=1-(crowdDensityWeight+0.05);	
			
		} else
		if (strat==pedestrianStrategy.BALANCE){
			distanceWeight=0.5;
			crowdDensityWeight=0.25;
			changingCrowdDensityWeight=0.25;	
		} else
		if (strat==pedestrianStrategy.BUSINESSMAN){
			distanceWeight=0.2;
			crowdDensityWeight=0.5;
			changingCrowdDensityWeight=0.3;	
		} else
		if (strat==pedestrianStrategy.ANT){
			distanceWeight=0.9;
			crowdDensityWeight=0.05;
			changingCrowdDensityWeight=0.05;				
		} else
		if (strat==pedestrianStrategy.SAMPLING){
			distanceWeight=0.3;
			crowdDensityWeight=0.1;
			changingCrowdDensityWeight=0.6;
		} else
		if (strat==pedestrianStrategy.LOOKANDGO){
			distanceWeight=0.4;
			crowdDensityWeight=0.3;
			changingCrowdDensityWeight=0.3;
		} else
		if (strat==pedestrianStrategy.ONECHOICE){
			distanceWeight=0.5;
			crowdDensityWeight=0.5;
			changingCrowdDensityWeight=0;
		} else
		if (strat==pedestrianStrategy.RANDOM){
			Random r = new Random();
			
			double randomValue = r.nextDouble();
			randomValue = Math.floor(randomValue * 100)/100;
			distanceWeight=randomValue;
			
			randomValue=1;
			while (randomValue>(1-distanceWeight)){
			randomValue = r.nextDouble();
			}
			randomValue = Math.floor(randomValue * 10000)/10000;
			crowdDensityWeight=randomValue;
			
			randomValue = 1-(distanceWeight+crowdDensityWeight);
			randomValue = Math.floor(randomValue * 10000)/10000;
			changingCrowdDensityWeight=randomValue;
		}	
	}
	
	public void setmovementOrder(int a){
		movementOrder=a;
	}
	
	public int getmovementOrder(){
		return movementOrder;
	}
	
	public Boolean isBlocked(){
		return blocked;
	}
}
