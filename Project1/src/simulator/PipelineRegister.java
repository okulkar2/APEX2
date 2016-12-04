package simulator;

public class PipelineRegister {
	
	private String register;
	private int value;

	public PipelineRegister(){
		register = null;
		value=0;
	}
	
	public String getRegister() {
		return register;
	}

	public int getValue() {
		return value;
	}
	
	public void setPipelineRegister(String registerIn, int valueIn){
		register = registerIn;
		value = valueIn;
	}
	
}
