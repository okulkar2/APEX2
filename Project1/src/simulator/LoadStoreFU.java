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
					instruction.setSrc1Value(instruction.getSrc1Value()+instruction.getLiteral());
					int result= tlb.readFromMemory(instruction);
					instruction.setDestValue(result);
					break;
			
				case Constants.STORE:
					
					
					if(Pipeline.getStages().get(Constants.WBLSFU)!=null) {
						
						if(instruction.getSource1().equalsIgnoreCase(Pipeline.getStages().get(Constants.WBALU).getDestination()))
							instruction.setSrc1Value(Pipeline.getStages().get(Constants.WBALU).getDestValue());
						
						if(instruction.getSource1().equalsIgnoreCase(Pipeline.getStages().get(Constants.WBMUL).getDestination()))
							instruction.setSrc1Value(Pipeline.getStages().get(Constants.WBMUL).getDestValue());
						
						
						if(instruction.getSource1().equalsIgnoreCase(Pipeline.getStages().get(Constants.WBLSFU).getDestination()))
							instruction.setSrc1Value(Pipeline.getStages().get(Constants.WBLSFU).getDestValue());
					}
					
					System.out.println("src1 value : "+instruction.getSrc1Value());
					System.out.println("src2 value : "+instruction.getSrc2Value());
					instruction.setSrc2Value(instruction.getSrc2Value()+instruction.getLiteral());
					tlb.writeToMemory(instruction);
					break;
			}
			
			Flag.setLSDone(true);
		}
	}
}
