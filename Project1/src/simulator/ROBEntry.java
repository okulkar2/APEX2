package simulator;

public class ROBEntry {

	private int pcValue;
	private String destArch, destPhysical;
	private String savedRATEntry;
	private int result;
	private boolean status;
	private String instructionType;

	public ROBEntry() {

		status=false;
		result=-1;
	}
	
	public int getPcValue() {
		return pcValue;
	}
	public void setPcValue(int pcValue) {
		this.pcValue = pcValue;
	}
	public String getDestArch() {
		return destArch;
	}
	public void setDestArch(String destArch) {
		this.destArch = destArch;
	}
	public String getDestPhysical() {
		return destPhysical;
	}
	public void setDestPhysical(String destPhysical) {
		this.destPhysical = destPhysical;
	}
	public String getSavedRATEntry() {
		return savedRATEntry;
	}
	public void setSavedRATEntry(String savedRATEntry) {
		this.savedRATEntry = savedRATEntry;
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
	
	public String toString(){
		return "Architectural Dest: "+destArch+" Physical Dest: "+destPhysical+" Saved RAT Entry: "+savedRATEntry+" Result: "+result;
	}
}
