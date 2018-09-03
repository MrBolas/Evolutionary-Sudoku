import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.text.Position;


public class subGrid {
	ArrayList<numberSubGrid> alphaSubGrid = new ArrayList<numberSubGrid>();
	int numberOfNumbersInSB;
	
	//Builder
	subGrid(int sizeSubGrid){
		numberOfNumbersInSB=sizeSubGrid;
		this.clonesFrom(this.setupPossible());
	}

	subGrid(int sizeSubGrid,int full){
		numberOfNumbersInSB=sizeSubGrid;
		for(int i=0;i<sizeSubGrid;i++){
			numberSubGrid value = new numberSubGrid(full, true);
			alphaSubGrid.add(value);
		}
	}
	
	
	//Functions
	void startUp(){
		ArrayList<Integer> buffer = new ArrayList<Integer>();
		for(int i=0;i<numberOfNumbersInSB;i++){
			buffer.add(i,i+1);
		}

		for(int j=0;j<numberOfNumbersInSB;j++){
			for(int i=0;i<buffer.size();i++){
				if(this.alphaSubGrid.get(j).getNumber()==buffer.get(i)){
					buffer.remove(i);
				}
			}
		}
		
		for(int i=0;i<numberOfNumbersInSB;i++){
			Random rand = new Random();
			int choosenIndex;
			if(buffer.size()>0){
			choosenIndex=rand.nextInt(buffer.size());
			}else{choosenIndex=0;}
			if(this.alphaSubGrid.get(i).getNumber()==0){
			this.replaceInGrid(buffer.get(choosenIndex), i, true);
			buffer.remove(choosenIndex);
			}
		}
	}

	void mutation(double mutationProbability){
		Random rand = new Random();
		
		if(rand.nextDouble()<mutationProbability){
			int position1,position2;
			
			do{
				position1=rand.nextInt(numberOfNumbersInSB);
				position2=rand.nextInt(numberOfNumbersInSB);
			}while(!((this.alphaSubGrid.get(position1).getChangeableValue()==true)&&(this.alphaSubGrid.get(position2).getChangeableValue()==true)));

			numberSubGrid temp = new numberSubGrid();
			temp.setNumber(this.getNumberSubGrid(position1).getNumber(), true);
			this.replaceInGrid(this.getNumberSubGrid(position2).getNumber(), position1, true);
			this.replaceInGrid(temp.getNumber(), position2, true);
		}	
	}
	
	numberSubGrid getNumberSubGrid(int position){
		numberSubGrid value = new numberSubGrid(this.alphaSubGrid.get(position).getNumber(),this.alphaSubGrid.get(position).getChangeableValue());
		return value; 
	}
	
	void setupSubGrid(ArrayList<numberSubGrid> setupArrayList){
		int i;		
		for(i=0;i<setupArrayList.size();i++){
			alphaSubGrid.get(i).setNumber(setupArrayList.get(i).getNumber(), false);
		}
	}
	
	void clonesFrom(subGrid tempGrid){
		int i;
		this.alphaSubGrid.clear();
		for(i=0;i<numberOfNumbersInSB;i++){
			this.alphaSubGrid.add(i,tempGrid.getNumberSubGrid(i));
		}	
	}
	
	subGrid inputSetup(){
		Scanner scan = new Scanner(System.in);
		subGrid tempGrid = new subGrid(numberOfNumbersInSB,0);
		int i;		

		for(i=0;i<numberOfNumbersInSB;i++){
			int newScannedValue=scan.nextInt();

			if(newScannedValue==0){
				tempGrid.replaceInGrid(0, i, true);
			}else{
				tempGrid.replaceInGrid(newScannedValue, i, false);
			}
			System.out.println("Inputed value"+i);
		}
		return tempGrid;
	}
	
	subGrid setupPossible(){
		subGrid tempGrid = new subGrid(numberOfNumbersInSB,0);
		int i,evalutatedNumber,evaluatedPosition;		

		for(i=0;i<Math.sqrt(numberOfNumbersInSB)-Math.sqrt(numberOfNumbersInSB)+1;i++){
			Random rand = new Random();
			evalutatedNumber=rand.nextInt(numberOfNumbersInSB)+1;
			evaluatedPosition=rand.nextInt(numberOfNumbersInSB);
			if(tempGrid.contains(evalutatedNumber)){
				i--;
			}else{
				tempGrid.replaceInGrid(evalutatedNumber, evaluatedPosition, false);
			}
		}
		return tempGrid;
	}

	void replaceInGrid(int value,int position,boolean changeable){
		numberSubGrid tempNumber = new numberSubGrid(value,changeable);
		this.alphaSubGrid.set(position, tempNumber);
	}
	
	boolean contains(int evalNumber){
		int i;
		for(i=0;i<numberOfNumbersInSB;i++){
			if(this.alphaSubGrid.get(i).getNumber()==evalNumber){
				return true;
			}
		}
		return false;
	}
	
	void printSubGrid(int line){
		int i;
		for(i=0;i<Math.sqrt(numberOfNumbersInSB);i++){
			System.out.print(this.alphaSubGrid.get((int) (i+Math.sqrt(numberOfNumbersInSB)*(line-1))).getNumber()+" ");	
			}
		System.out.print(" ");
	}
}
