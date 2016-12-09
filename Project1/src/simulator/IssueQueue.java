package simulator;

import java.util.ArrayList;
import java.util.List;

public class IssueQueue {
	int iqCapacity;
	List<Instruction> issueQueueInstructions = new ArrayList<>();
	
	
	public IssueQueue(int iqCapacityIn){
		iqCapacity = iqCapacityIn;
	}
	
	

	/**
	 * @return the iqCapacity
	 */
	public int getIqCapacity() {
		return iqCapacity;
	}



	/**
	 * @param iqCapacity the iqCapacity to set
	 */
	public void setIqCapacity(int iqCapacity) {
		this.iqCapacity = iqCapacity;
	}



	/**
	 * @return the issueQueueInstructions
	 */
	public List<Instruction> getIssueQueueInstructions() {
		return issueQueueInstructions;
	}



	/**
	 * @param issueQueueInstructions the issueQueueInstructions to set
	 */
	public void setIssueQueueInstructions(List<Instruction> issueQueueInstructions) {
		this.issueQueueInstructions = issueQueueInstructions;
	}

	public int getSize(){
		return issueQueueInstructions.size();
	}

	public Instruction getInstruction(int index){
		return issueQueueInstructions.get(index);
	}
	
	public void removeInstruction(int index){
		issueQueueInstructions.remove(index);
	}
	
	public void putInstruction(Instruction issueQueueInstruction) {
		issueQueueInstructions.add(issueQueueInstruction);
	}

	public Instruction getIssueQueueInstructionatIndex(int index) {
		return issueQueueInstructions.get(index);
	}

	public boolean isIQFull() {
		if(issueQueueInstructions.size() <= iqCapacity){
			return true;
		}
		return false;
	}
	
	public void display() {
		
		System.out.println("Contents of Issue Queue");
		for(Instruction instruction : issueQueueInstructions) {
			
			System.out.println(instruction.getInstruction());
		}
	}
}
