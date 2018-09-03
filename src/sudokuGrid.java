import java.util.ArrayList;
import java.util.Random;

import javax.naming.BinaryRefAddr;

public class sudokuGrid implements Comparable<sudokuGrid> {
	int numberOfsubGrids;
	double fitness;
	ArrayList <subGrid> alphaGrid =new ArrayList<subGrid>();

	//Builders
	public sudokuGrid(int sizeGrid,boolean value){
		numberOfsubGrids=sizeGrid;
		for(int i=0;i<sizeGrid;i++){
			subGrid subValue = new subGrid(sizeGrid,0);
			this.alphaGrid.add(subValue);
		}
	}
	
	public sudokuGrid(int sizeGrid) {
		numberOfsubGrids=sizeGrid;
		boolean validation=false;
		System.out.println("Validating ... a "+numberOfsubGrids+"by"+numberOfsubGrids+" Sudoku Matrix");
		while(!validation){
			for(int i=0;i<sizeGrid;i++){
				subGrid value = new subGrid(sizeGrid);
				this.alphaGrid.add(value);
			}
			if(this.validSudokuGrid()){

				System.out.println("Validated");
				validation=true;
			}
			else{
				this.alphaGrid.clear();
			}
		}
	}
	
	//Functions
	void crossOverMatrix(double CrossProb,sudokuGrid A){
		subGrid tempGrid= new subGrid(numberOfsubGrids,0);
		Random rand = new Random();
		
		if(rand.nextDouble()<CrossProb){
			//crossOver
			int choosenIndex=rand.nextInt(numberOfsubGrids);
			System.out.println("CROSSOVER: "+choosenIndex);
			//ADD funcao de troca
			tempGrid.clonesFrom(this.getSubGrid(choosenIndex));
			this.alphaGrid.remove(choosenIndex);
			A.alphaGrid.add(choosenIndex, tempGrid);
			tempGrid.clonesFrom(A.getSubGrid(choosenIndex+1));
			A.alphaGrid.remove(choosenIndex+1);
			this.alphaGrid.add(choosenIndex, tempGrid);
			
		}
	}
	
	sudokuGrid crossOverMatrixReturningNewMatrix(double CrossProb,sudokuGrid A){
		sudokuGrid tempBGrid = new sudokuGrid(numberOfsubGrids, true);
		subGrid tempGrid= new subGrid(numberOfsubGrids,0);
		Random rand = new Random();
		int choosenIndex=rand.nextInt(numberOfsubGrids-1);

		//ADD funcao de troca
		tempGrid.clonesFrom(this.getSubGrid(choosenIndex));
		tempBGrid.clonesFrom(this);
		tempBGrid.alphaGrid.remove(choosenIndex);
		tempBGrid.alphaGrid.add(choosenIndex, tempGrid);
		return tempBGrid;
	}

	double getRealFitness(){
		return this.fitness;
	}
	
	void computeFitness(){
		this.fitness=this.getFitnessLine()+this.getFitnessCollum();
	}
	
	double getFitnessLine(){
		double finalFit=0;

		for(int i=0;i<(numberOfsubGrids);i=(int)(i+Math.sqrt(numberOfsubGrids))){
			for(int j=0;j<(numberOfsubGrids);j=(int)(j+Math.sqrt(numberOfsubGrids))){
				if(!this.validLineChecker(i, j)){
					finalFit++;
				}
			}
		}
		return finalFit;
	}
	
	double getFitnessCollum(){
		double finalFit=0;

		for(int i=0;i<Math.sqrt(numberOfsubGrids);i++){
			for(int j=0;j<Math.sqrt(numberOfsubGrids);j++){
				if(!this.validCollumChecker(i, j)){
					finalFit++;
				}
			}
		}
		return finalFit;
	}
	
	void startUp(){
		//Randomize every subgrid to allow a random but valid start up
		for(int i=0;i<numberOfsubGrids;i++){
			this.alphaGrid.get(i).startUp();
		}
	}
	
	subGrid getSubGrid(int indexOfSubGrid){
		subGrid temp = new subGrid(numberOfsubGrids,0);
		temp.clonesFrom(this.alphaGrid.get(indexOfSubGrid));
		return temp;
	}

	void setSubGrid(subGrid A,int index){
		this.alphaGrid.get(index).clonesFrom(A);
	}
	
	boolean validSudokuGrid(){
		int j,i,p,k,l,m,n,q,repeatCounter=0;
		//Search for evaluation number
		for(k=0;k<numberOfsubGrids;k=(int) (k+Math.sqrt(numberOfsubGrids))){

			for(i=0;i<Math.sqrt(numberOfsubGrids);i++){

				for(j=0;j<numberOfsubGrids;j=(int) (j+Math.sqrt(numberOfsubGrids))){

					if(!validLineChecker(k, j)){
						return false;
					}

					for(p=0;p<Math.sqrt(numberOfsubGrids);p++){
						//Search for duplicates	
						
						if(!validCollumChecker(i,p)){
							return false;
						}
						repeatCounter=0;

						if(this.alphaGrid.get(i+k).getNumberSubGrid(j+p).getNumber()==0){
							continue;
						}

						for(l=0;l<numberOfsubGrids-Math.sqrt(numberOfsubGrids);l=(int) (l+Math.sqrt(numberOfsubGrids))){

							for(n=0;n<Math.sqrt(numberOfsubGrids);n++){

								for(m=0;m<numberOfsubGrids-Math.sqrt(numberOfsubGrids);m=(int) (m+Math.sqrt(numberOfsubGrids))){

									for(q=0;q<Math.sqrt(numberOfsubGrids);q++){

										if(this.alphaGrid.get(k+i).getNumberSubGrid(j+p).getNumber()==this.alphaGrid.get(l+n).getNumberSubGrid(m+q).getNumber()){
											repeatCounter++;
										}
										if(repeatCounter>1){
											return false;
										}
									}

								}

							}

						}

					}

				}

			}			

		}
		return true;
	}

	boolean validLineChecker(int gridLine,int subGridLine){
		int repeatCounter;
		for(int h=0;h<Math.sqrt(numberOfsubGrids);h++){
			for(int k=0;k<Math.sqrt(numberOfsubGrids);k++){
			repeatCounter=0;
			
			//System.out.print("Evaluatedd Number : ");
			//System.out.println(this.alphaGrid.get(gridLine+h).getNumberSubGrid(k+subGridLine).getNumber());
			
			
				for(int i=0;i<Math.sqrt(numberOfsubGrids);i++){
					for(int j=0;j<Math.sqrt(numberOfsubGrids);j++){
						
						if(this.alphaGrid.get(gridLine+i).getNumberSubGrid(j+subGridLine).getNumber()==this.alphaGrid.get(gridLine+h).getNumberSubGrid(k+subGridLine).getNumber()){
							if(this.alphaGrid.get(gridLine+h).getNumberSubGrid(k+subGridLine).getNumber()==0){
								continue;
							}
							repeatCounter++;
						/*	System.out.print("Repeated number: ");
							System.out.println(this.alphaGrid.get(gridLine+i).getNumberSubGrid(j+subGridLine).getNumber());
							System.out.print("RepeateCounter number: ");
							System.out.println(repeatCounter);
						*/}

						if(repeatCounter>1){
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	boolean validCollumChecker(int gridCollum,int subGridCollum){
		
		int repeatCounter;
		for(int h=0;h<numberOfsubGrids;h=(int) (h+Math.sqrt(numberOfsubGrids))){
			for(int k=0;k<numberOfsubGrids;k=(int) (k+Math.sqrt(numberOfsubGrids))){
			repeatCounter=0;
			
			//System.out.print("Evaluatedd Number : ");
			//System.out.println(this.alphaGrid.get(gridLine+h).getNumberSubGrid(k+subGridLine).getNumber());
			
			
				for(int i=0;i<numberOfsubGrids;i=(int) (i+Math.sqrt(numberOfsubGrids))){
					for(int j=0;j<numberOfsubGrids;j=(int) (j+Math.sqrt(numberOfsubGrids))){
						
						if(this.alphaGrid.get(gridCollum+i).getNumberSubGrid(j+subGridCollum).getNumber()==this.alphaGrid.get(gridCollum+h).getNumberSubGrid(k+subGridCollum).getNumber()){
							if(this.alphaGrid.get(gridCollum+h).getNumberSubGrid(k+subGridCollum).getNumber()==0){
								continue;
							}
							repeatCounter++;
						/*	System.out.print("Repeated number: ");
							System.out.println(this.alphaGrid.get(gridLine+i).getNumberSubGrid(j+subGridLine).getNumber());
							System.out.print("RepeateCounter number: ");
							System.out.println(repeatCounter);
						*/}

						if(repeatCounter>1){
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	void printSudoku(){
		int j,i,k;
		for(k=0;k<numberOfsubGrids;k=(int) (k+Math.sqrt(numberOfsubGrids))){
			for( j=1;j<Math.sqrt(numberOfsubGrids)+1;j++){
				for(i=0;i<Math.sqrt(numberOfsubGrids);i++){
					this.alphaGrid.get(i+k).printSubGrid(j);
				}
				System.out.println();
			}
			System.out.println();
		}
		System.out.println("----------------------------------");

	}

	void clonesFrom(sudokuGrid Original){
		int j;
		//this.alphaGrid.clear(); //Buggy Line commented for the moment
		for(j=0;j<numberOfsubGrids;j++){			
			this.alphaGrid.get(j).clonesFrom(Original.getSubGrid(j));
		}	
	}

	@Override
	public int compareTo(sudokuGrid arg0) {
		// TODO Auto-generated method stub
		if(this.getRealFitness()>arg0.getRealFitness()){
			return 1;
		}else if(this.getRealFitness()<arg0.getRealFitness()){
			return -1;
		}else{
			return 0;
		}
	}

}
