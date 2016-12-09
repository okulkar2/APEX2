
package simulator;

/**
*
*Class Instruction is used to store  
*details about a single instruction 
*line in the given instruction sequence 
*
*/
public class Instruction {
	
	private String instruction;
	private String operand;
	private String destination, dest_physical;
	private String source1,source2;
	private boolean src1,src2;
	private int src1Value,src2Value,destValue;
	private int literal;
	private int pc_value;
	private int cycle;
	private String function_unit;
	private int robIndex;
	
	public Instruction(){
		operand = null;
		destination = null;
		dest_physical=null;
		source1 = null;
		source2 = null;
		pc_value = 0;
		literal = 0;
		function_unit = null;
		src1Value = 0;
		src2Value = 0;
		cycle = 0;
		src1 = true;
		src2 = true;
		robIndex=-1;
	}
	
	
	/**
	 * @return the cycle
	 */
	public int getCycle() {
		return cycle;
	}


	/**
	 * @param cycle the cycle to set
	 */
	public void setCycle(int cycleIn) {
		cycle = cycleIn;
	}


	public String getInstruction() {
		return instruction;
	}


	public void setInstruction(String instructionIn) {
		instruction = instructionIn;
	}


	public int getPc_value() {
		return pc_value;
	}
	
	public void setPc_value(int pc_valueIn) {
		this.pc_value = pc_valueIn;
	}
	
	public String getOperand() {
		return operand;
	}
	
	public String getDestination() {
		return destination;
	}

	public void setDestination(String destinationIn) {
		destination = destinationIn;
	}
	
	public String getDest_physical() {
		return dest_physical;
	}

	public void setDest_physical(String dest_physical) {
		this.dest_physical = dest_physical;
	}

	public String getFunction_unit() {
		return function_unit;
	}

	public void setFunction_unit(String function_unitIn) {
		function_unit = function_unitIn;
	}

	public void setOperand(String operandIn) {
		operand = operandIn;
	}
	
	public String getSource1() {
		return source1;
	}
	
	public void setSource1(String source1In) {
		source1 = source1In;
	}
	
	public String getSource2() {
		return source2;
	}
	
	public void setSource2(String source2In) {
		source2 = source2In;
	}
	
	public int getLiteral() {
		return literal;
	}
	
	public void setLiteral(int literalIn) {
		literal = literalIn;
	}
	
	public boolean isSrc1Valid() {
		return src1;
	}

	public void setSrc1Valid(boolean src1In) {
		src1 = src1In;
	}

	public boolean isSrc2Valid() {
		return src2;
	}

	public void setSrc2Valid(boolean src2In) {
		this.src2 = src2In;
	}
	
	public int getSrc1Value() {
		return src1Value;
	}

	public void setSrc1Value(int src1ValueIn) {
		src1Value = src1ValueIn;
	}

	public int getSrc2Value() {
		return src2Value;
	}

	public void setSrc2Value(int src2ValueIn) {
		src2Value = src2ValueIn;
	}
	
	public int getDestValue() {
		return destValue;
	}

	public void setDestValue(int destValue) {
		this.destValue = destValue;
	}
	
	public int getRobIndex() {
		return robIndex;
	}

	public void setRobIndex(int robIndex) {
		this.robIndex = robIndex;
	}
	
	
	public String toString(){
		return "PC Value:"+pc_value+" Destination:"+destination+" Source1:"+source1+" Source2:"+source2+" literal:"+literal+" Function_Unit:"+function_unit;
	}
	
}
