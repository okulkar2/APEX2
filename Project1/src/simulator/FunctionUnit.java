package simulator;

public class FunctionUnit {

	public static int executeIntruction(Instruction inputInst){
		int result=0;
		switch (inputInst.getOperand()) {
			case "ADD":
				result = inputInst.getSrc1Value()+inputInst.getSrc2Value();
			break;

			case "SUB":
				//System.out.println("Source1:"+inputInst.getSrc1Value()+" Source2:"+inputInst.getSrc2Value());
				result = inputInst.getSrc1Value()-inputInst.getSrc2Value();
			break;
		
			case "MOVC":
				result = inputInst.getLiteral()+0;
			break;
		
			case "MUL":
				result = inputInst.getSrc1Value()*inputInst.getSrc2Value();
			break;
		
			case "AND":
				result = inputInst.getSrc1Value() & inputInst.getSrc2Value();
			break;
		
			case "OR":
				result = inputInst.getSrc1Value() | inputInst.getSrc2Value();
			break;
		
			case "EX-OR":
				result = inputInst.getSrc1Value() ^ inputInst.getSrc2Value();
			break;
		
			case "LOAD":
				result = inputInst.getSrc1Value() + inputInst.getLiteral();
			break;
		
			case "STORE":
				result = inputInst.getSrc2Value() + inputInst.getLiteral();
			break;
		}
		return result;
	}
}
