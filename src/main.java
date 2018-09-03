import java.awt.Toolkit;
import java.io.Console;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class main {

	public static void main(String[] args) {

		/* Sudoku Solver *******************************Joao Bolas n40651******************************************
		 * 
		 * Number of parallel solutions can increase convergence time trading for computational power
		 * sizeOfSudoku variable is the designated result of NxN, which means is the total number of subGrids and it's inner elements
		 *
		 ***********************The Algorithm itself is ready to start just compile and run the program*****************************
		 *SizeOfSudoku can have values of perfect squares
		 */
		ArrayList<ArrayList<sudokuGrid>> generations = new ArrayList<ArrayList<sudokuGrid>>();
		ArrayList<sudokuGrid> populationOfSudokus = new ArrayList<sudokuGrid>();
		boolean sound=true;
		int sizeOfPopulation=1000,i,k,g=0,sizeOfSudoku=9,numberOfGens=99000,numberOfElites=(int) (sizeOfPopulation*0.15),genThreshold=5000,startingFitness=sizeOfSudoku*2,genCounter=0,bestScore=startingFitness;
		double probCross=1,probMuta=1;
		
		//Game ON (Making the solvable game)
		sudokuGrid elementSudoku = new sudokuGrid(sizeOfSudoku);
		sudokuGrid modelSudoku = new sudokuGrid(sizeOfSudoku, true);
		modelSudoku.clonesFrom(elementSudoku);
		populationOfSudokus.add(0,elementSudoku);
		populationOfSudokus.get(0).printSudoku();
		
		//Setting up Generations ArrayList
		for(i=1;i<numberOfGens;i++){
			ArrayList<sudokuGrid> tempArraylist = new ArrayList<sudokuGrid>();
			generations.add(tempArraylist);
		}
		
		//Creating the population for GA
		for(i=1;i<sizeOfPopulation;i++){
			sudokuGrid tempElementSudoku = new sudokuGrid(sizeOfSudoku,true);
			tempElementSudoku.clonesFrom(populationOfSudokus.get(0));
			populationOfSudokus.add(i, tempElementSudoku);
		}
		
		//Fire Up Random Game Solutions
		for(i=0;i<sizeOfPopulation;i++){
			populationOfSudokus.get(i).startUp();
		}
		
		//Defining Generations and setting up the GA
		generations.add(0,populationOfSudokus);
		Collections.sort(generations.get(g));

		//Compute Fitness of first gen
		for(i=0;i<sizeOfPopulation;i++){
			generations.get(0).get(i).computeFitness();
		}
		
		
		
		//Selection
		//GOD Function (Generation Orientator Designer)
		for( g=0;g<numberOfGens-1;g++){

			//CrossOver
			for(i=0;i<(sizeOfPopulation)/2;i=i+2){
				Random rand = new Random();
				if(rand.nextDouble()<probCross){
					generations.get(g+1).add(generations.get(g).get(rand.nextInt(sizeOfPopulation)).crossOverMatrixReturningNewMatrix(probCross, generations.get(g).get(rand.nextInt(sizeOfPopulation))));
				}
			}

			//Adding the best parents to the next Generation
			k=0;
			for(i=generations.get(g+1).size();i<sizeOfPopulation;i++){
				sudokuGrid tempBGrid = new sudokuGrid(sizeOfSudoku,true);
				tempBGrid.clonesFrom(generations.get(g).get(k++));
				generations.get(g+1).add(tempBGrid);
			}
			
			//Compute Fitness of next gen
			for(i=0;i<sizeOfPopulation;i++){
				generations.get(g+1).get(i).computeFitness();
			}
			
			//Sorting next Gen
			Collections.sort(generations.get(g+1));
			
			//Mutation
			for(i=0;i<sizeOfPopulation;i++){
				Random rand = new Random();
				if(rand.nextDouble()<probMuta){
					int numberMutations=0,numberOfInvolvedMatrix=0;
					double evaltuatedRandMatrix=rand.nextDouble(),evaltuatedRandMutation=rand.nextDouble();
					
					for(int j=0;j<sizeOfSudoku;j++){
						if(evaltuatedRandMatrix<((double)(Math.pow(2, j)/Math.pow(2, sizeOfSudoku)))){
							numberOfInvolvedMatrix=sizeOfSudoku-j;
							break;
						}	
					}
					
					for(int j=0;j<sizeOfSudoku;j++){
						if(evaltuatedRandMutation<((double)(Math.pow(2, j)/Math.pow(2, sizeOfSudoku)))){
							numberMutations=sizeOfSudoku-j+1;
							break;
						}	
					}

					for(int k2=0;k2<numberOfInvolvedMatrix;k2++){
						int j = rand.nextInt(sizeOfSudoku);
						
						//Elite Mutation
						if(i<numberOfElites){
							for(int k1=0;k1<numberMutations;k1++){
								sudokuGrid Gtemp = new sudokuGrid(sizeOfSudoku, true);
								subGrid temp = new subGrid(sizeOfSudoku, 0);
								Gtemp.clonesFrom(generations.get(g+1).get(i));
								temp.clonesFrom(generations.get(g+1).get(i).getSubGrid(j));
								temp.mutation(1);
								Gtemp.setSubGrid(temp, j);
								Gtemp.computeFitness();
								if(generations.get(g+1).get(i).getRealFitness()>Gtemp.getRealFitness()){
									generations.get(g+1).get(i).clonesFrom(Gtemp);
								}
							}
						}else if(i>=numberOfElites){
							// Pleb Mutation
							for(int k1=0;k1<numberMutations+1;k1++){
								subGrid temp = new subGrid(sizeOfSudoku, 0);
								temp.clonesFrom(generations.get(g+1).get(i).getSubGrid(j));
								temp.mutation(1);
								generations.get(g+1).get(i).setSubGrid(temp,j);
							}
						}
					}
				}
			}

			//Compute Fitness of next gen
			for(i=0;i<sizeOfPopulation;i++){
				generations.get(g+1).get(i).computeFitness();
			}
			
			//Sorting next Gen
			Collections.sort(generations.get(g+1));
			
			//Print Game and real fitness
			double fitnessAverage=0,previewsFitnessAverage=0;
			for(i=0;i<sizeOfPopulation;i++){
				fitnessAverage=fitnessAverage+(int) generations.get(g).get(i).getRealFitness();
			}
			
			if(g>1){
				generations.get(g-1).clear();
				if(previewsFitnessAverage>fitnessAverage/sizeOfPopulation){
				}
				previewsFitnessAverage=fitnessAverage;
			}
			
			if(generations.get(g).get(0).getRealFitness()<startingFitness){
				startingFitness=(int) generations.get(g).get(0).getRealFitness();
				bestScore=startingFitness;
				System.out.println("Generation: "+g+" With the current Fitness of: "+generations.get(g).get(0).getRealFitness()+" And Average Fitness: "+((double)fitnessAverage/sizeOfPopulation)+" The best Score till now was: "+bestScore);
				Toolkit.getDefaultToolkit().beep();
				genCounter=0;
			}else if(generations.get(g).get(0).getRealFitness()>startingFitness){
				genCounter=0;
			}
			else{
				genCounter++;
			}

			if(genCounter>genThreshold){
				//Dive in the Gene Pool Quick Restart
				for(i=0;i<sizeOfPopulation;i++){
					sudokuGrid tempGrid= new sudokuGrid(sizeOfSudoku, true);
					tempGrid.clonesFrom(modelSudoku);
					tempGrid.startUp();
					generations.get(g+1).get(i).clonesFrom(tempGrid);
				}
				genCounter=0;
			}

			if(generations.get(g).get(0).getRealFitness()<(sizeOfSudoku/2)){
				numberOfElites=(int) (sizeOfPopulation*0.5);
			}
			
			if(generations.get(g).get(0).getRealFitness()==0){
				if(sound){
					Toolkit.getDefaultToolkit().beep();
				}
				//generations.get(g).get(0).printSudoku();
				System.out.println("The Solution for the Starting "+sizeOfSudoku+"by"+sizeOfSudoku+" is the following, achieved in :"+g+" Generations");
				generations.get(g).get(0).printSudoku();
				break;
			}
		}

	}

}
