package simulator;

public final class Constants {

	// Pipeline stages
		public static final String FETCH="Fetch";
		public static final String DECODE_R1="Decode/Rename1";
		public static final String R2_DISPATCH="Rename2/Dispatch";
		public static final String ISSUE="Issue";
		public static final String ALU1="ALU1";
		public static final String ALU2="ALU2";
		public static final String MUL1="MUL1";
		public static final String MUL2="MUL2";
		public static final String MUL3="MUL3";
		public static final String MUL4="MUL4";
		public static final String BRANCH="Branch";
		public static final String LSFU1="LSFU1";
		public static final String LSFU2="LSFU2";
		public static final String MEM="Mem";
		public static final String WB="Writeback";
		
		// Instruction opcodes
		public static final String ADD="ADD";
		public static final String SUB="SUB";
		public static final String MUL="MUL";
		public static final String AND="AND";
		public static final String OR="OR";
		public static final String EXOR="EX-OR";
		public static final String MOVC="MOVC";
		public static final String LOAD="LOAD";
		public static final String STORE="STORE";
		public static final String BZ="BZ";
		public static final String BNZ="BNZ";
		public static final String JUMP="JUMP";
		public static final String BAL="BAL";
		public static final String HALT="HALT";
		
		// Function units.
		public static final String INTFU="IntegerFU";
		public static final String MULFU="MultiplyFU";
		public static final String BRANCHFU="BranchFU";
		public static final String LSFU="LOADSTOREFU";	
		
}
