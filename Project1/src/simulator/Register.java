package simulator;

public class Register {
	
	private int value;
	private boolean valid;
	
	public Register(){
		value=0;
		valid = false;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int valueIn) {
		value = valueIn;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean validIn) {
		valid = validIn;
	}
	
}
