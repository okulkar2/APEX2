package simulator;

public class BranchFU {
	
	public  static int executeIntructionBranchALU(Instruction inputInst){
		int result=0;
		switch (inputInst.getOperand()) {
			case "BZ":
				if(inputInst.getSrc1Value() == 0){
					result = inputInst.getPc_value()+inputInst.getLiteral();
					Flag.setBranchFlag(true);
				}
			break;

			case "BNZ":
				if(inputInst.getSrc1Value() != 0){
					result = inputInst.getPc_value()+inputInst.getLiteral();
					Flag.setBranchFlag(true);
				}
			break;
		
			case "JUMP":
				result = Pipeline.getSpecialRegister().get(inputInst.getSource1())+inputInst.getLiteral();
				Flag.setBranchFlag(true);
			break;
		
			case "BAL":
				if(Pipeline.getStages().get("D/RF")!= null){
					Instruction aluInstruction = Pipeline.getStages().get("D/RF");
					Pipeline.setSpecialRegister("X", aluInstruction.getPc_value());
				}
				result = inputInst.getSrc1Value()+inputInst.getLiteral();
				Flag.setBranchFlag(true);
			break;
			}
		return result;
	}
	
}
