package simulator;

public class ALUFU {

	private Pipeline pipeline;
	
	public ALUFU(Pipeline pipelineIn) {

		pipeline=pipelineIn;
	}
	
	
	
	public void performOperation() {
		
		// code to perform ALU2 operation
		
		if(pipeline.getStages().get(Constants.ALU1)!=null) {
			
			Instruction instruction=pipeline.getStages().get(Constants.ALU1);
			pipeline.getStages().put(Constants.ALU2, instruction);
			int result=FunctionUnit.executeIntruction(instruction);
			instruction.setDestValue(result);
			pipeline.getStages().put(Constants.ALU1, null);
			Flag.setINTFUAvailable(true);
		}
	}
	
}
