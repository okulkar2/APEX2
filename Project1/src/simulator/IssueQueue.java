package simulator;

import java.util.ArrayList;
import java.util.List;

public class IssueQueue {
	int iqCapacity;
	int currentCapacity;
	List<Instruction> issueQueueInstructions = new ArrayList<>();
	
	
	public IssueQueue(int iqCapacityIn){
		iqCapacity = iqCapacityIn;
		currentCapacity = 0;
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



	public Instruction getInstruction(int index){
		return issueQueueInstructions.get(index);
	}
	
	public void remove(int index){
		issueQueueInstructions.remove(index);
	}
	
	public void put(Instruction issueQueueInstruction) {
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
	
}
