package simulator;

public class MultiplyFU {

	private Pipeline pipeline;

	public MultiplyFU(Pipeline pipelineIn) {

		pipeline=pipelineIn;
	}
	
	
	public void performOperation() {
		
		// to do mul4 stage
		
		if(pipeline.getStages().get(Constants.MUL3)!=null) {
			
			pipeline.getStages().put(Constants.MUL4, pipeline.getStages().get(Constants.MUL3));
			pipeline.getStages().put(Constants.MUL3, null);
		}
		
		if(pipeline.getStages().get(Constants.MUL2)!=null) {
			
			pipeline.getStages().put(Constants.MUL3, pipeline.getStages().get(Constants.MUL2));
			pipeline.getStages().put(Constants.MUL2, null);
		}
		
		if(pipeline.getStages().get(Constants.MUL1)!=null) {
			
			pipeline.getStages().put(Constants.MUL2, pipeline.getStages().get(Constants.MUL1));
			//pipeline.getStages().put(Constants.MUL1, null);
		}
	}
}
