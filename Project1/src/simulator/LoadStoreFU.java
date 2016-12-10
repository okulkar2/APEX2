package simulator;

public class LoadStoreFU {

	private Pipeline pipeline;
	
	public LoadStoreFU(Pipeline pipelineIn) {

		pipeline=pipelineIn;
	}
	
	
	public void performOperation() {
		
		// code for TLB access to be done
		
		if(pipeline.getStages().get(Constants.LSFU1)!=null) {
			
			pipeline.getStages().put(Constants.LSFU2, pipeline.getStages().get(Constants.LSFU1));
			pipeline.getStages().put(Constants.LSFU1, null);
			Flag.setLSFUAvailable(true);
		}
	}
}
