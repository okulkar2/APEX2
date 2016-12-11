package simulator;

public class LoadStoreFU {

	private ROB rob;
	private TLB tlb;
	
	public LoadStoreFU(ROB robIn) {
		rob = robIn;
		tlb = new TLB();
	}
	
	
	public void performOperation() {
		
		if(Pipeline.getStages().get(Constants.LSFU2)!=null) {
			performCheck(Pipeline.getStages().get(Constants.LSFU2));
		}
		else {
			
			if(Pipeline.getStages().get(Constants.LSFU1)!=null) {
				Instruction instruction=Pipeline.getStages().get(Constants.LSFU1);
				Pipeline.getStages().put(Constants.LSFU2, instruction);
				Pipeline.getStages().put(Constants.LSFU1, null);
				Flag.setLSFUAvailable(true);
				performCheck(instruction);
			}
		}
	}	
	
	private void performCheck(Instruction instruction) {
		
		if(instruction.getPc_value() == rob.getROBHead().getPcValue()){
			switch (instruction.getOperand()) {
				case Constants.LOAD:
					int result= tlb.readFromMemory(instruction);
					instruction.setDestValue(result);
					break;
			
				case Constants.STORE:
					tlb.writeToMemory(instruction);
					break;
			}
			
			Flag.setLSDone(true);
		}
	}
}
