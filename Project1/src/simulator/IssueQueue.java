package simulator;

import java.util.ArrayList;
import java.util.List;

public class IssueQueue {
	int currentInstruction = 1;
	int iqCapacity = 12;
	List<Instruction> issueQueueInstructions = new ArrayList<>();
	Boolean[] status = new Boolean[iqCapacity];

	/**
	 * @return the iqCapacity
	 */
	public int getIqCapacity() {
		return iqCapacity;
	}

	/**
	 * @param iqCapacity
	 *            the iqCapacity to set
	 */
	public void setIqCapacity(int iqCapacity) {
		this.iqCapacity = iqCapacity;
	}

	/**
	 * @return the issueQueueInstruction
	 */
	public List<Instruction> getIssueQueueInstruction() {
		return issueQueueInstructions;
	}

	/**
	 * @param issueQueueInstruction
	 *            the issueQueueInstruction to set
	 */
	public void setIssueQueueInstruction(List<Instruction> issueQueueInstruction) {
		this.issueQueueInstructions = issueQueueInstruction;
	}

	public void setIssueQueueInstructionList(Instruction issueQueueInstruction) {
		issueQueueInstructions.add(issueQueueInstruction);
		status[currentInstruction] = true;
		currentInstruction++;

	}

	public Instruction getIssueQueueInstructionatIndex(int index) {
		return issueQueueInstructions.get(index);
	}

	public int checkIfIQIsFull() {
		int i = 0;
		while (i < iqCapacity) {
			if (status[currentInstruction].equals(false)) {
				return i;
			}
			i++;
		}
		return -1;

	}
}
