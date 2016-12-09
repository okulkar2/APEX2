package simulator;

import java.util.AbstractMap.SimpleEntry;

public class IHelper {

	
	
	
	public IHelper() {
		// TODO Auto-generated constructor stub
	}
	
	public String getRequiredFU(String opcode) {
	
		String result=null;
		switch (opcode) {

			case Constants.ADD:
			case Constants.SUB:
			case Constants.AND:
			case Constants.OR:
			case Constants.EXOR:
			case Constants.MOVC:
								result=Constants.INTFU;
								break;
			case Constants.LOAD:
			case Constants.STORE:
								result=Constants.LSFU;
								break;
				
			case Constants.MUL:
								result=Constants.MUL;
								break;
								
			case Constants.BAL:
			case Constants.JUMP:
			case Constants.BNZ:
			case Constants.BZ:
								result=Constants.BRANCHFU;
								break;
					
			default:
				break;
		}
		
		return result;
	}
	
	
	public void performExecution(Instruction instruction) {
		
		String opcode=instruction.getOperand();
		int result;
		
		switch(opcode) {
		
			case Constants.ADD:
								result=instruction.getSrc1Value()+instruction.getSrc2Value();
								instruction.setDestValue(result);
								break;
			case Constants.SUB:
								result=instruction.getSrc1Value()-instruction.getSrc2Value();
								instruction.setDestValue(result);
								break;
			case Constants.MUL:
								result=instruction.getSrc1Value()*instruction.getSrc2Value();
								instruction.setDestValue(result);
								break;
			case Constants.AND:
								result=instruction.getSrc1Value()&instruction.getSrc2Value();
								instruction.setDestValue(result);
								break;
			case Constants.OR:
								result=instruction.getSrc1Value()|instruction.getSrc2Value();
								instruction.setDestValue(result);
								break;
					
			case Constants.EXOR:
								result=instruction.getSrc1Value()^instruction.getSrc2Value();
								instruction.setDestValue(result);
								break;
			
			case Constants.MOVC:								
								instruction.setDestValue(instruction.getLiteral());
								break;
			
			case Constants.LOAD:
								result=instruction.getSrc1Value()+instruction.getLiteral();
								instruction.setSrc1Value(result);
								break;
						
			case Constants.STORE:
								result=instruction.getSrc2Value()+instruction.getLiteral();
								instruction.setSrc2Value(result);
								break;
								
								
				default:
						break;
				
								
		}
	}
	
}
