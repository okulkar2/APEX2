package simulator;

public class TLB {

	public int readFromMemory(Instruction inputInst){
		int result = 0;
		result = Pipeline.getMemory(inputInst.getSrc1Value());
		return result;
	}
	
	public void writeToMemory(Instruction inputInst){
		Pipeline.setMemory(inputInst.getSrc2Value(), inputInst.getSrc1Value());
	}
}
