package simulator;

public class ROBEntry {

	private int pcValue;
	private String destArch, destPhysical;
	private int result;
	private boolean status;
	private String instructionType;

	public int getPcValue() {
		return pcValue;
	}
	public void setPcValue(int pcValue) {
		this.pcValue = pcValue;
	}

	/**
	 * @return the destArch
	 */
	public String getDestArch() {
		return destArch;
	}
	/**
	 * @param destArch the destArch to set
	 */
	public void setDestArch(String destArch) {
		this.destArch = destArch;
	}
	/**
	 * @return the destPhysical
	 */
	public String getDestPhysical() {
		return destPhysical;
	}
	/**
	 * @param destPhysical the destPhysical to set
	 */
	public void setDestPhysical(String destPhysical) {
		this.destPhysical = destPhysical;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getInstructionType() {
		return instructionType;
	}
	public void setInstructionType(String instructionType) {
		this.instructionType = instructionType;
	}
}
