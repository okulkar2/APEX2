package simulator;

public class ROBEntry {

	private int pcValue;
	private Register destArch, destPhysical;
	private int result;
	private boolean status;
	private String instructionType;

	public int getPcValue() {
		return pcValue;
	}
	public void setPcValue(int pcValue) {
		this.pcValue = pcValue;
	}
	public Register getDestArch() {
		return destArch;
	}
	public void setDestArch(Register destArch) {
		this.destArch = destArch;
	}
	public Register getDestPhysical() {
		return destPhysical;
	}
	public void setDestPhysical(Register destPhysical) {
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
