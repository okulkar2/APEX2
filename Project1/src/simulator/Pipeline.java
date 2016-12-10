package simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class Pipeline {
	

	private static HashMap<String, Instruction> stages = new HashMap<String, Instruction>();
	private static HashMap<String, Register> registerFile= new HashMap<String, Register>();
	private static HashMap<String, PipelineRegister> pipelinestages = new HashMap<String, PipelineRegister>(5);
	private static HashMap<String, Integer> specialRegister = new HashMap<String, Integer>(2);
	private static List<Instruction> selectInstruction = new ArrayList<Instruction>();
	private static Integer [] memory = new Integer[4000];
	private int min_cycle=9999;
	private static int programCounter;
	private String fetchInst;
	boolean stall = false;
	private IHelper helper;
	private ALUFU aluFU;
	private MultiplyFU multiplyFU;
	private LoadStoreFU loadStoreFU;
	private BranchFU branchFU;
	private ROB rob;
	private UnifiedRegisterFile urf;
	private IssueQueue issueQueue;
	private PrepareInstruction prepareInstruction;
	
	public void initialize(int urfSize) {
		
		stages.put(Constants.FETCH, null);
		stages.put(Constants.DECODE_R1, null);
		stages.put(Constants.R2_DISPATCH, null);
		stages.put(Constants.ALU1, null);
		stages.put(Constants.ALU2, null);
		stages.put(Constants.MUL1, null);
		stages.put(Constants.MUL2, null);
		stages.put(Constants.MUL3, null);
		stages.put(Constants.MUL4, null);
		stages.put(Constants.BRANCH, null);
		stages.put(Constants.LSFU1, null);
		stages.put(Constants.LSFU2, null);
		
		programCounter = 4000;
		
		for (int i = 0; i < 4000; i++) {
			memory[i] = 0;
		}

		urf=new UnifiedRegisterFile(urfSize);
		urf.initRegisters();
		issueQueue=new IssueQueue(12);
		rob=new ROB(40, urf);
		prepareInstruction=new PrepareInstruction(urf,rob,issueQueue);
		
		aluFU=new ALUFU(Pipeline.this);
		multiplyFU=new MultiplyFU(Pipeline.this);
		loadStoreFU=new LoadStoreFU(Pipeline.this);
		
		Flag.setINTFUAvailable(true);
		Flag.setMULFUAvailable(true);
		Flag.setBRANCHFUAvailable(true);
		Flag.setLSFUAvailable(true);
		Flag.setBranchFlag(false);
	}
	
	
	public void execute() {
	
		System.out.println();
		writeback();
		executeStage();
		rename2Dispatch();
		decodeRename1();
		fetch();
		printStages();
		urf.displayFrontEndRAT();
		urf.displayPhysicalRegisters();
		urf.displayFreeList();
		System.out.println("Issue queue size : "+issueQueue.getSize());
		//issueQueue.display();
	}

	private void writeback() {
		
		if(stages.get(Constants.ALU2)!=null) {
			
			Instruction aluInstruction=stages.get(Constants.ALU2);
			rob.getROBEntry(aluInstruction.getRobIndex()).setResult(aluInstruction.getDestValue());
			urf.getPhysicalRegisters().get(aluInstruction.getDest_physical()).setValue(aluInstruction.getDestValue());
			urf.getPhysicalRegisters().get(aluInstruction.getDest_physical()).setValid(true);
			stages.put(Constants.ALU2, null);
			
		}
		
		if(stages.get(Constants.MUL4)!=null) {
			
			Instruction mulInstruction=stages.get(Constants.MUL4);
			rob.getROBEntry(mulInstruction.getRobIndex()).setResult(mulInstruction.getDestValue());
			stages.put(Constants.MUL4, null);
			stages.put(Constants.MUL1, null);
			Flag.setMULFUAvailable(true);
			// setMUL flag available
		}
		
		if(stages.get(Constants.LSFU2)!=null) {
			
			Instruction lsInstruction=stages.get(Constants.LSFU2);
			rob.getROBEntry(lsInstruction.getRobIndex()).setResult(lsInstruction.getDestValue());
			stages.put(Constants.LSFU2, null);
		}
	}
	
	
	public Instruction selectionIntruction() {
		Instruction selectedInstruction=null;
		List<Instruction> LSInstructions = new ArrayList<Instruction>(3);
		List<Instruction> BranchInstructions = new ArrayList<Instruction>(3);
		min_cycle = 9999;
		int minimum =9999;
		int size = issueQueue.getSize();
		selectInstruction.clear();
		
		for(int i=0; i<size; i++){
			selectedInstruction = issueQueue.getInstruction(i);
			switch(selectedInstruction.getFunction_unit()){
			case Constants.INTFU:
				if(selectedInstruction.isSrc1Valid()==true && selectedInstruction.isSrc2Valid()==true && Flag.isINTFUAvailable()){
					selectInstruction.add(selectedInstruction);
				}
				break;
			case Constants.MULFU:
				if(selectedInstruction.isSrc1Valid()==true && selectedInstruction.isSrc2Valid()==true && Flag.isMULFUAvailable()){
					selectInstruction.add(selectedInstruction);
				}
				break;
			case Constants.LSFU:
				LSInstructions.add(selectedInstruction);
				break;
			case Constants.BRANCHFU:
				BranchInstructions.add(selectedInstruction);
				break;
			}
		}
		
		if(LSInstructions.size()!=0){
			size = LSInstructions.size();
			for(int i=0; i<size; i++){
				if(LSInstructions.get(i).getCycle() < minimum){
					minimum = LSInstructions.get(i).getCycle();
					selectedInstruction = LSInstructions.get(i);
				}
			}
			if(selectedInstruction.isSrc1Valid()==true && selectedInstruction.isSrc2Valid()==true && Flag.isLSFUAvailable()){
				selectInstruction.add(selectedInstruction);
			}
		}
		
		if(BranchInstructions.size()!=0){
			minimum = 9999;
			size = BranchInstructions.size();
			for(int i=0; i<size; i++){
				if(BranchInstructions.get(i).getCycle() < minimum){
					minimum = BranchInstructions.get(i).getCycle();
					selectedInstruction = BranchInstructions.get(i);
				}
			}
			if(selectedInstruction.isSrc1Valid()==true && selectedInstruction.isSrc2Valid()==true && Flag.isBRANCHFUAvailable()){
				selectInstruction.add(selectedInstruction);
			}
		}
		
		if(selectInstruction.size()!=0){
			size = selectInstruction.size();
			for(int i=0;i<size;i++){
				if(selectInstruction.get(i).getCycle() < min_cycle){
					min_cycle = selectInstruction.get(i).getCycle();
					selectedInstruction = selectInstruction.get(i);
				}
			}
		}
		else{
			selectedInstruction = null;
		}
		
		return selectedInstruction;
		
	}
	
	private void executeStage() {
		
		aluFU.performOperation();
		multiplyFU.performOperation();
		loadStoreFU.performOperation();
		
		//Instruction currentInstruction=selectionIntruction();
		Instruction currentInstruction=stages.get(Constants.R2_DISPATCH);
		if(currentInstruction!=null && !currentInstruction.getOperand().equalsIgnoreCase(Constants.HALT)) {
		
			int result;
			switch(currentInstruction.getFunction_unit()) {
			
				case Constants.INTFU:
									if(stages.get(Constants.ALU1)==null) {
										
										stages.put(Constants.ALU1, currentInstruction);
										Flag.setINTFUAvailable(false);
									}
									break;
						
				case Constants.MULFU:
									if(stages.get(Constants.MUL1)==null) {
										
										stages.put(Constants.MUL1, currentInstruction);
										Flag.setMULFUAvailable(false);
										result=FunctionUnit.executeIntruction(currentInstruction);
										currentInstruction.setDestValue(result);
									}
									break;
				
				case Constants.BRANCHFU:
									if(stages.get(Constants.BRANCHFU)==null) {
											
										stages.put(Constants.BRANCH, currentInstruction);
										result=FunctionUnit.executeIntruction(currentInstruction);
										currentInstruction.setDestValue(result);
									}
									break;
				
				case Constants.LSFU:
					
									if(stages.get(Constants.LSFU1)==null) {
						
										stages.put(Constants.LSFU1, currentInstruction);
										Flag.setLSFUAvailable(false);
										result=FunctionUnit.executeIntruction(currentInstruction);
										currentInstruction.setDestValue(result);
									}
									break;
	
					default:
							break;
			}
		}
	}
	
	public void flushFrontEndStages() {
		
		stages.put(Constants.FETCH, null);
		stages.put(Constants.DECODE_R1, null);
		stages.put(Constants.R2_DISPATCH, null);
	}
	
	private void rename2Dispatch() {
		
		if(issueQueue.isIQFull() && urf.isPhysicalRegisterAvailable() && rob.isFreeSlotAvailable()) {

			if(stages.get(Constants.DECODE_R1) != null) {
				
				Instruction instruction=stages.get(Constants.DECODE_R1);
				prepareInstruction.addEntries(instruction);
				stages.put(Constants.R2_DISPATCH, instruction);
				stages.put(Constants.DECODE_R1, null);
				Flag.setStallFlag(false);
			}  
		} else
			Flag.setStallFlag(true);
	}
	
	private void decodeRename1() {
			
		if (!Flag.isStallFlagSet() && stages.get(Constants.FETCH) != null) {
			
			Instruction instruction=stages.get(Constants.FETCH);
			String[] tokens = instruction.getInstruction().split("[ ,#]+");
			prepareInstruction.prepare(instruction, tokens);
			stages.put(Constants.DECODE_R1, instruction);
			stages.put(Constants.FETCH, null);			
		} 
	}
	
	public void readSourceValues(Instruction instruction) {
		
		switch(instruction.getOperand()) {
		
			case Constants.ADD:
			case Constants.SUB:
			case Constants.AND:
			case Constants.OR:
			case Constants.EXOR:
			case Constants.MUL:
			case Constants.STORE:
								instruction.setSrc1Value(urf.getPhysicalRegisters().get(instruction.getSource1()).getValue());
								instruction.setSrc2Value(urf.getPhysicalRegisters().get(instruction.getSource2()).getValue());
								break;
			case Constants.LOAD:
								instruction.setSrc1Value(urf.getPhysicalRegisters().get(instruction.getSource1()).getValue());
								break;
		
		}
	}
	
	public void fetch() {

		if(!Flag.isStallFlagSet() && stages.get(Constants.FETCH) == null && !Flag.isBranchFlagSet()) {
			
			if((fetchInst = Cache.getInstruction()) != null) {
				
				Instruction newInstruction = new Instruction();
				newInstruction.setInstruction(fetchInst);
				newInstruction.setPc_value(programCounter);
				stages.put(Constants.FETCH, newInstruction);
				programCounter = programCounter + 4;
			
			} else {

				stages.put(Constants.FETCH, null);
			}
		}
		else if(Flag.isBranchFlagSet()==true){
			int counter=0;
			if(specialRegister.get("Fetch")!=null){
				counter = specialRegister.get("Fetch");
				//System.out.println("Counter Value"+counter);
			}
			int result1 = counter - 4000;
			int result = result1/4;
			//System.out.println("Instruction at"+result);
			Cache.setInstCount(result);
			programCounter = counter;
			Flag.setBranchFlag(false);
			stages.put(Constants.FETCH, null);
		}
		else{
			//System.out.println("Fetch Instruction:"+fetchInst);
		}
	}
	
	private void dependencyCheck(Instruction input){
		
		if(stages.get("IntALU1")!=null){
			Instruction exInput = stages.get("IntALU1");
			if(exInput.getDestination()!=null){
				if(input.getSource1()!=null && exInput.getDestination().equalsIgnoreCase(input.getSource1())){
					input.setSrc1Valid(false);
					Flag.setStallFlag(true);
				} 
				if(input.getSource2()!=null && exInput.getDestination().equalsIgnoreCase(input.getSource2())){
					input.setSrc2Valid(false);
					Flag.setStallFlag(true);
				}
			}
		}
		
		if(stages.get("IntALU2")!=null){
			Instruction exInput = stages.get("IntALU2");
			if(exInput.getDestination()!=null){
				if(input.getSource1()!=null && exInput.getDestination().equalsIgnoreCase(input.getSource1())){
					input.setSrc1Valid(false);
					Flag.setStallFlag(true);
				} 
				if(input.getSource2()!=null && exInput.getDestination().equalsIgnoreCase(input.getSource2())){
					input.setSrc2Valid(false);
					Flag.setStallFlag(true);
				}
			}
		}
		
		if(stages.get("MEM")!=null){
			Instruction exInput = stages.get("MEM");
			if(exInput.getDestination()!=null){
				if(input.getSource1()!=null && exInput.getDestination().equalsIgnoreCase(input.getSource1())){
					input.setSrc1Valid(false);
					Flag.setStallFlag(true);
				} 
				if(input.getSource2()!=null && exInput.getDestination().equalsIgnoreCase(input.getSource2())){
					input.setSrc2Valid(false);
					Flag.setStallFlag(true);
				}
			}
		}
	}
	
	private void stall(Instruction inputInst){
		
		Instruction MemInst = null;
		boolean [] sources =  new boolean[2];
		
		sources[0] = inputInst.isSrc1Valid();
		sources[1] = inputInst.isSrc2Valid();
		
		if(stages.get("MEM") != null){
			MemInst = stages.get("MEM");
		}
		
		if(inputInst.getSource1()!=null && inputInst.isSrc1Valid()==false){
			Register result = registerFile.get(inputInst.getSource1());
			if(MemInst!=null && !MemInst.getOperand().equalsIgnoreCase("LOAD") && pipelinestages.get("MEM").getRegister()!=null && pipelinestages.get("MEM").getRegister().equalsIgnoreCase(inputInst.getSource1())){
				Flag.setForwardEXFlag(true);
				inputInst.setSrc1Valid(true);
			}
			else{
				if(MemInst!=null && MemInst.getOperand().equalsIgnoreCase("LOAD") && pipelinestages.get("MEM").getRegister()!=null && pipelinestages.get("MEM").getRegister().equalsIgnoreCase(inputInst.getSource1())){
					if(inputInst.getOperand().equalsIgnoreCase("STORE")){
						
						//Flag.setForwardStoreFlag(true);
						inputInst.setSrc1Valid(true);
					}
				}
				else{
					if(pipelinestages.get("WB").getRegister()!= null && pipelinestages.get("WB").getRegister().equalsIgnoreCase(inputInst.getSource1())){
						Flag.setForwardMEMFlag(true);
						inputInst.setSrc1Valid(true);
					}
					else{
						if(result.isValid() == true){
							inputInst.setSrc1Valid(true);
						}	
					}
				}
			}
		}
		
		if(inputInst.getSource2()!=null && inputInst.isSrc2Valid()==false){
			Register result = registerFile.get(inputInst.getSource2());
			if(MemInst!=null && !MemInst.getOperand().equalsIgnoreCase("LOAD") && pipelinestages.get("MEM").getRegister()!=null && pipelinestages.get("MEM").getRegister().equalsIgnoreCase(inputInst.getSource2())){
				Flag.setForwardEXFlag(true);
				inputInst.setSrc2Valid(true);
			}
			else{
				if(pipelinestages.get("WB").getRegister()!=null && pipelinestages.get("WB").getRegister().equalsIgnoreCase(inputInst.getSource2())){
					Flag.setForwardMEMFlag(true);
					inputInst.setSrc2Valid(true);
				}
				else{
					if(result.isValid() == true){
						inputInst.setSrc2Valid(true);
					}
				}
			}
		}
		
		if(stages.get("IntALU2")!=null){
			Instruction exInput = stages.get("IntALU2");
			if(exInput.getDestination()!=null){
				if(inputInst.getSource1()!=null && exInput.getDestination().equalsIgnoreCase(inputInst.getSource1())){
					inputInst.setSrc1Valid(false);
				} 
				if(inputInst.getSource2()!=null && exInput.getDestination().equalsIgnoreCase(inputInst.getSource2())){
					inputInst.setSrc2Valid(false);
				}
			}
		}
		
		if(inputInst.isSrc1Valid()==true && inputInst.isSrc2Valid()==true){
			Flag.setStallFlag(false);
		}
		
		if(Flag.isStallFlagSet()==true){
			inputInst.setSrc1Valid(sources[0]);
			inputInst.setSrc2Valid(sources[1]);
		}
		
	}
	
	private void stallBranch(Instruction inputInst){
		
		Instruction MemInst=null;
		
		if(stages.get("MEM") != null){
			MemInst = stages.get("MEM");
		}
		
		if(inputInst.getSource1()!=null && inputInst.isSrc1Valid()==false){
			Register result = registerFile.get(inputInst.getSource1());
			if(MemInst!=null && !MemInst.getOperand().equalsIgnoreCase("LOAD") && pipelinestages.get("MEM").getRegister()!=null && pipelinestages.get("MEM").getRegister().equalsIgnoreCase(inputInst.getSource1())){
				Flag.setForwardEXFlag(true);
				inputInst.setSrc1Valid(true);
			}
			else{
				if(MemInst!=null && MemInst.getOperand().equalsIgnoreCase("LOAD") && pipelinestages.get("MEM").getRegister()!=null && pipelinestages.get("MEM").getRegister().equalsIgnoreCase(inputInst.getSource1())){
					Flag.setForwardMEMFlag(true);;
					inputInst.setSrc1Valid(true);
				}
				else{
					if(result.isValid() == true){
						inputInst.setSrc1Valid(true);
					}
				}
			}
		}
		
		if(inputInst.isSrc1Valid()==true){
			Flag.setStallFlag(false);
		}
		
	}
	
	public static HashMap<String, Instruction> getStages() {
		return stages;
	}
	
	public static HashMap<String, Integer> getSpecialRegister() {
		return specialRegister;
	}

	public static void setSpecialRegister(String result, int value) {
		
		if(result.equalsIgnoreCase("Fetch")){
			specialRegister.put("Fetch", value);
		}
		else{
			specialRegister.put("X", value);
		}
	}
	
	public static int getMemory(int index) {
		return memory[index];
	}

	public static void setMemory(int index, int value) {
		memory[index] = value;
	}

	public void printRegisterFile(){
	    for(Entry<String, Register> entry: registerFile.entrySet()) {
	        System.out.println(entry.getKey()+":"+entry.getValue().getValue());
	    }
	}
	
	public void printMemory(){
		for(int i=0;i<20;i++){
			System.out.println(i+" : "+memory[i]+"\t\t"+(i+20)+" : "+memory[i+20]+"\t\t"+(i+40)+" : "+memory[i+40]+"\t\t"+(i+60)+" : "+memory[i+60]+"\t\t"+(i+80)+" : "+memory[i+80]);
		}
	}
	
	
	public void printStages(){
		if(stages.get(Constants.FETCH)!=null){
			System.out.println("Fetch Stage:"+stages.get(Constants.FETCH).getInstruction());
		}
		else{
			System.out.println("Fetch Stage: NOP");
		}
		if(stages.get(Constants.DECODE_R1)!=null){
			System.out.println("Decode/R1 Stage:"+stages.get(Constants.DECODE_R1).getInstruction());
		}
		else{
			System.out.println("Decode/Rename1 Stage: NOP");
		}
		
		if(stages.get(Constants.R2_DISPATCH)!=null){
			System.out.println("R2/Dispatch Stage:"+stages.get(Constants.R2_DISPATCH).getInstruction());
		}
		else{
			System.out.println("R2/Dispatch Stage: NOP");
		}
		
		if(stages.get(Constants.ALU1)!=null){
			System.out.println("Integer ALU1 Stage:"+stages.get(Constants.ALU1).getInstruction());
		}
		else{
			System.out.println("Integer ALU1 Stage: NOP");
		}
		
		if(stages.get(Constants.ALU2)!=null){
			System.out.println("Integer ALU2 Stage:"+stages.get(Constants.ALU2).getInstruction());
		}
		else{
			System.out.println("Integer ALU2 Stage: NOP");
		}
		
		if(stages.get(Constants.MUL1)!=null){
			System.out.println("MUL Stage:"+stages.get(Constants.MUL1).getInstruction());
		}
		else{
			System.out.println("MUL Stage: NOP");
		}
		if(stages.get(Constants.BRANCH)!=null){
			System.out.println("Branch FU Stage:"+stages.get(Constants.BRANCH).getInstruction());
		}
		else{
			System.out.println("Branch FU Stage: NOP");
		}
		if(stages.get(Constants.LSFU1)!=null){
			System.out.println("LSFU1 Stage:"+stages.get(Constants.LSFU1).getInstruction());
		}
		else{
			System.out.println("LSFU1 Stage: NOP");
		}
		if(stages.get(Constants.LSFU2)!=null){
			System.out.println("LSFU2 Stage:"+stages.get(Constants.LSFU2).getInstruction());
		}
		else{
			System.out.println("LSFU2 Stage: NOP");
		}
	}
}


