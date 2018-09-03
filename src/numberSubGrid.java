/*NumberSubGrid class responsible for each number on the matrix
 * False= cannot be changed =starter value
 * True = Can be changed
*/

public class numberSubGrid {
	int number;
	boolean changeable;

	numberSubGrid(int newNumber,boolean changeableValue){setNumber(newNumber,changeableValue);}
	numberSubGrid() {}
	
	int getNumber(){
		return number;
	}
	
	void setNumber(int newNumber, boolean changeableValue){
		number=newNumber;
		changeable=changeableValue;
	}
	
	void setNumber(int newNumber){
		number=newNumber;
	}
	
	boolean getChangeableValue(){
		return changeable;
	}
	


}
