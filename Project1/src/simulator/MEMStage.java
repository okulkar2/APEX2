package simulator;

public class MEMStage {

	public  static int executeIntructionMEM(Instruction inputInst, int memory){
		int result=0;
		switch (inputInst.getOperand()) {
			case "LOAD":
				result = Pipeline.getMemory(memory);
			break;
				
			case "STORE":
				Pipeline.setMemory(memory, inputInst.getSrc1Value());
			break;
		}
		return result;
	}
}
