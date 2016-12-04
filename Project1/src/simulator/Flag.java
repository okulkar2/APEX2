package simulator;

public class Flag {
	
	private static boolean forwardEXFlag;
	private static boolean forwardMEMFlag;
	private static boolean forwardStoreFlag;
	private static boolean zeroFlag;
	private static boolean stallFlag;
	private static boolean branchFlag;

	public static boolean isForwardEXFlagSet() {
		return forwardEXFlag;
	}

	public static void setForwardEXFlag(boolean forwardEXFlagIn) {
		forwardEXFlag = forwardEXFlagIn;
	}

	public static boolean isForwardMEMFlagSet() {
		return forwardMEMFlag;
	}

	public static void setForwardMEMFlag(boolean forwardMEMFlagIn) {
		forwardMEMFlag = forwardMEMFlagIn;
	}
	
	public static boolean isForwardStoreFlagSet() {
		return forwardStoreFlag;
	}

	public static void setForwardStoreFlag(boolean forwardStoreFlagIn) {
		forwardStoreFlag = forwardStoreFlagIn;
	}

	public static boolean isZeroFlag() {
		return zeroFlag;
	}

	public static void setZeroFlag(boolean zeroFlagIn) {
		zeroFlag = zeroFlagIn;
	}

	public static boolean isStallFlagSet() {
		return stallFlag;
	}

	public static void setStallFlag(boolean stallFlagIn) {
		Flag.stallFlag = stallFlagIn;
	}

	public static boolean isBranchFlagSet() {
		return branchFlag;
	}

	public static void setBranchFlag(boolean branchFlagIn) {
		branchFlag = branchFlagIn;
	}
	
	
	
}
