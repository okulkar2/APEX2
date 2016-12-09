package simulator;

public class ALUFU {

	private Pipeline pipeline;
	private IHelper helper;
	
	public ALUFU(Pipeline pipelineIn, IHelper helperIn) {

		pipeline=pipelineIn;
		helper=helperIn;
	}
	
	
	
	public void performOperation() {
		
		// code to perform ALU2 operation
		
		if(pipeline.getStages().get(Constants.ALU1)!=null) {
			
			pipeline.getStages().put(Constants.ALU2, pipeline.getStages().get(Constants.ALU1));
			pipeline.getStages().put(Constants.ALU1, null);
			Flag.setINTFUAvailable(true);
		}
	}
	
}
