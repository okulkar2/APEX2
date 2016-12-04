package simulator;

public class PrepareInstruction {
	
	public static void prepare(Instruction inputInst,String[] input) {
		
		inputInst.setOperand(input[0].toUpperCase());
		
		switch (inputInst.getOperand()) {
			case "ADD":
				inputInst.setDestination(input[1].toUpperCase());
				inputInst.setSource1(input[2].toUpperCase());
				inputInst.setSource2(input[3].toUpperCase());
				inputInst.setFunction_unit("IntALU1");
			break;

			case "SUB":
				inputInst.setDestination(input[1].toUpperCase());
				inputInst.setSource1(input[2].toUpperCase());
				inputInst.setSource2(input[3].toUpperCase());
				inputInst.setFunction_unit("IntALU1");
			break;
			
			case "MOVC":
				inputInst.setDestination(input[1].toUpperCase());
				inputInst.setLiteral(Integer.parseInt(input[2]));
				inputInst.setFunction_unit("IntALU1");
			break;
			
			case "MUL":
				inputInst.setDestination(input[1].toUpperCase());
				inputInst.setSource1(input[2].toUpperCase());
				inputInst.setSource2(input[3].toUpperCase());
				inputInst.setFunction_unit("IntALU1");
			break;
			
			case "AND":
				inputInst.setDestination(input[1].toUpperCase());
				inputInst.setSource1(input[2].toUpperCase());
				inputInst.setSource2(input[3].toUpperCase());
				inputInst.setFunction_unit("IntALU1");
			break;
			
			case "OR":
				inputInst.setDestination(input[1].toUpperCase());
				inputInst.setSource1(input[2].toUpperCase());
				inputInst.setSource2(input[3].toUpperCase());
				inputInst.setFunction_unit("IntALU1");
			break;
			
			case "EX-OR":
				inputInst.setDestination(input[1].toUpperCase());
				inputInst.setSource1(input[2].toUpperCase());
				inputInst.setSource2(input[3].toUpperCase());
				inputInst.setFunction_unit("IntALU1");
			break;
			
			case "LOAD":
				inputInst.setDestination(input[1].toUpperCase());
				inputInst.setSource1(input[2].toUpperCase());
				inputInst.setLiteral(Integer.parseInt(input[3]));				
				inputInst.setFunction_unit("IntALU1");
			break;
			
			case "STORE":
				inputInst.setSource1(input[1].toUpperCase());
				inputInst.setSource2(input[2].toUpperCase());
				inputInst.setLiteral(Integer.parseInt(input[3]));
				inputInst.setFunction_unit("IntALU1");
			break;
			
			case "BZ":
				if(Pipeline.getStages().get("IntALU1")!= null){
					Instruction aluInstruction = Pipeline.getStages().get("IntALU1");
					inputInst.setSource1(aluInstruction.getDestination());
				}
				inputInst.setLiteral(Integer.parseInt(input[1]));
				inputInst.setFunction_unit("BranchALU");
			break;
			
			case "BNZ":
				if(Pipeline.getStages().get("IntALU1")!= null){
					Instruction aluInstruction = Pipeline.getStages().get("IntALU1");
					inputInst.setSource1(aluInstruction.getDestination());
				}
				inputInst.setLiteral(Integer.parseInt(input[1]));
				inputInst.setFunction_unit("BranchALU");
			break;
			
			case "JUMP":
				inputInst.setSource1(input[1]);
				inputInst.setLiteral(Integer.parseInt(input[2]));
				inputInst.setFunction_unit("BranchALU");
			break;
			
			case "BAL":
				inputInst.setSource1(input[1]);
				inputInst.setLiteral(Integer.parseInt(input[2]));
				inputInst.setFunction_unit("BranchALU");
			break;
			
			case "HALT":
				inputInst.setFunction_unit("IntALU1");
			break;
		}

	}

}
