package simulator;

public class PrepareInstruction {
	
	private ROB rob;
	private IssueQueue issueQueue;
	private UnifiedRegisterFile urf;
	
	public PrepareInstruction(UnifiedRegisterFile urfIn, ROB robIn, IssueQueue issueQueueIn) {

		urf=urfIn;
		issueQueue=issueQueueIn;
		rob=robIn;
	}
	
	public void prepare(Instruction inputInst, String[] input) {

		inputInst.setOperand(input[0].toUpperCase());
		switch (inputInst.getOperand()) {
	
			case Constants.ADD:
			case Constants.SUB:
			case Constants.AND:
			case Constants.OR:
			case Constants.EXOR:
					inputInst.setDestination(input[1].toUpperCase());
					inputInst.setSource1(urf.getFrontEndRat().get(input[2].toUpperCase()));
					inputInst.setSource2(urf.getFrontEndRat().get(input[3].toUpperCase()));
					inputInst.setFunction_unit(Constants.INTFU);
					break;
				
			case Constants.MOVC:
					inputInst.setDestination(input[1].toUpperCase());
					inputInst.setLiteral(Integer.parseInt(input[2]));
					inputInst.setFunction_unit(Constants.INTFU);
					break;
		
			case Constants.MUL:
					inputInst.setDestination(input[1].toUpperCase());
					inputInst.setSource1(urf.getFrontEndRat().get(input[2].toUpperCase()));
					inputInst.setSource2(urf.getFrontEndRat().get(input[3].toUpperCase()));
					inputInst.setFunction_unit(Constants.MULFU);
					break;
	
			case Constants.LOAD:
					inputInst.setDestination(input[1].toUpperCase());
					inputInst.setSource1(urf.getFrontEndRat().get(input[2].toUpperCase()));
					inputInst.setLiteral(Integer.parseInt(input[3]));
					inputInst.setFunction_unit(Constants.LSFU);
					break;
	
			case Constants.STORE:
					inputInst.setDestination(null);
					inputInst.setSource1(urf.getFrontEndRat().get(input[1].toUpperCase()));
					inputInst.setSource2(urf.getFrontEndRat().get(input[2].toUpperCase()));
					inputInst.setLiteral(Integer.parseInt(input[3]));
					inputInst.setFunction_unit(Constants.LSFU);
					break;
	
			case Constants.BZ:
					inputInst.setDestination(null);
					if (Pipeline.getStages().get(Constants.R2_DISPATCH) != null) {
						Instruction dependentInstruction = Pipeline.getStages().get(Constants.R2_DISPATCH);
						inputInst.setSource1(dependentInstruction.getDest_physical());
					}
					inputInst.setLiteral(Integer.parseInt(input[1]));
					inputInst.setFunction_unit(Constants.BRANCHFU);
					break;
	
			case Constants.BNZ:
					inputInst.setDestination(null);
					if (Pipeline.getStages().get(Constants.R2_DISPATCH) != null) {
						Instruction aluInstruction = Pipeline.getStages().get(Constants.R2_DISPATCH);
						inputInst.setSource1(aluInstruction.getDest_physical());
					}
					inputInst.setLiteral(Integer.parseInt(input[1]));
					inputInst.setFunction_unit(Constants.BRANCHFU);
					break;
	
			case Constants.JUMP:
					inputInst.setDestination(null);
					inputInst.setSource1(urf.getFrontEndRat().get(input[1].toUpperCase()));
					inputInst.setLiteral(Integer.parseInt(input[2]));
					inputInst.setFunction_unit(Constants.BRANCHFU);
					break;
	
			case Constants.BAL:
					inputInst.setDestination(null);
					inputInst.setSource1(urf.getFrontEndRat().get(input[1].toUpperCase()));
					inputInst.setLiteral(Integer.parseInt(input[2]));
					inputInst.setFunction_unit(Constants.BRANCHFU);
					break;
	
			case Constants.HALT:
					inputInst.setFunction_unit(Constants.INTFU);
					break;
		}
	}
	
	
	public void addEntries(Instruction instruction) {
		
		ROBEntry entry=null;
		String physicalRegister;
		int robIndex;
		switch(instruction.getOperand()) {
		
			case Constants.ADD:
			case Constants.SUB:
			case Constants.AND:
			case Constants.OR:
			case Constants.EXOR:
			case Constants.MUL:
								instruction.setSrc1Valid(urf.getPhysicalRegisters().get(instruction.getSource1()).isValid());
								instruction.setSrc2Valid(urf.getPhysicalRegisters().get(instruction.getSource2()).isValid());
								physicalRegister=urf.getFreePhysicalRegister();
								instruction.setDest_physical(physicalRegister);
								entry=createROBEntry(instruction);
								entry.setSavedRATEntry(urf.getFrontEndRat().get(instruction.getDestination()));
								entry.setInstructionType(Constants.R2R);
								robIndex=rob.enqueue(entry);
								instruction.setRobIndex(robIndex);
								urf.getFrontEndRat().put(instruction.getDestination(), instruction.getDest_physical());
								urf.getPhysicalRegisters().get(instruction.getDest_physical()).setValid(false);
								urf.getFreeList().put(physicalRegister, false);
								issueQueue.putInstruction(instruction);
								break;
			case Constants.MOVC:
								physicalRegister=urf.getFreePhysicalRegister();
								instruction.setDest_physical(physicalRegister);
								entry=createROBEntry(instruction);
								entry.setSavedRATEntry(urf.getFrontEndRat().get(instruction.getDestination()));
								entry.setInstructionType(Constants.R2R);
								robIndex=rob.enqueue(entry);
								instruction.setRobIndex(robIndex);
								urf.getFrontEndRat().put(instruction.getDestination(), instruction.getDest_physical());
								urf.getPhysicalRegisters().get(instruction.getDest_physical()).setValid(false);
								urf.getFreeList().put(physicalRegister, false);
								issueQueue.putInstruction(instruction);
								break;
			case Constants.LOAD:
								instruction.setSrc1Valid(urf.getPhysicalRegisters().get(instruction.getSource1()).isValid());
								physicalRegister=urf.getFreePhysicalRegister();
								instruction.setDest_physical(physicalRegister);
								entry=createROBEntry(instruction);
								entry.setSavedRATEntry(urf.getFrontEndRat().get(instruction.getDestination()));
								entry.setInstructionType(Constants.LOAD);
								robIndex=rob.enqueue(entry);
								instruction.setRobIndex(robIndex);
								urf.getFrontEndRat().put(instruction.getDestination(), instruction.getDest_physical());
								urf.getPhysicalRegisters().get(instruction.getDest_physical()).setValid(false);
								urf.getFreeList().put(physicalRegister, false);
								issueQueue.putInstruction(instruction);
								break;
			case Constants.STORE:
								instruction.setSrc1Valid(urf.getPhysicalRegisters().get(instruction.getSource1()).isValid());
								instruction.setSrc2Valid(urf.getPhysicalRegisters().get(instruction.getSource2()).isValid());
								instruction.setDest_physical(null);
								entry=createROBEntry(instruction);
								entry.setInstructionType(Constants.STORE);
								robIndex=rob.enqueue(entry);
								instruction.setRobIndex(robIndex);
								issueQueue.putInstruction(instruction);
								break;
			case Constants.BZ:
			case Constants.BNZ:
								instruction.setSrc1Valid(urf.getPhysicalRegisters().get(instruction.getSource1()).isValid());
								instruction.setDest_physical(null);
								entry=createROBEntry(instruction);
								robIndex=rob.enqueue(entry);
								instruction.setRobIndex(robIndex);
								issueQueue.putInstruction(instruction);
			case Constants.BAL:
			case Constants.JUMP:
								instruction.setDest_physical(null);
								entry=createROBEntry(instruction);
								entry.setInstructionType(Constants.BRANCH);
								robIndex=rob.enqueue(entry);
								instruction.setRobIndex(robIndex);
								issueQueue.putInstruction(instruction);
								break;
		
		}
		
	}
	
	private ROBEntry createROBEntry(Instruction instruction) {
		
		ROBEntry entry=new ROBEntry();
		entry.setDestArch(instruction.getDestination());
		entry.setDestPhysical(instruction.getDest_physical());
		entry.setPcValue(instruction.getPc_value());
		entry.setStatus(false);
		return entry;
	}
}
