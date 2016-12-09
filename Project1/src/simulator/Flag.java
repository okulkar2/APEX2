package simulator;

public class Flag {
	
	private static boolean forwardEXFlag;
	private static boolean forwardMEMFlag;
	private static boolean zeroFlag;
	private static boolean stallFlag;
	private static boolean branchFlag;
	private static boolean INTFUAvailable;
	private static boolean MULFUAvailable;
	private static boolean LSFUAvailable;
	private static boolean BRANCHFUAvailable;
	
	public static void initializeFlags(){
		stallFlag = false;
		zeroFlag = false;
		forwardEXFlag = false;
		forwardMEMFlag = false;
		branchFlag = false;
		INTFUAvailable = true;
		MULFUAvailable = true;
		LSFUAvailable = true;
		BRANCHFUAvailable = true;
	}

	

	/**
	 * @return the iNTFUAvailable
	 */
	public static boolean isINTFUAvailable() {
		return INTFUAvailable;
	}



	/**
	 * @param iNTFUAvailable the iNTFUAvailable to set
	 */
	public static void setINTFUAvailable(boolean iNTFUAvailable) {
		INTFUAvailable = iNTFUAvailable;
	}



	/**
	 * @return the mULFUAvailable
	 */
	public static boolean isMULFUAvailable() {
		return MULFUAvailable;
	}



	/**
	 * @param mULFUAvailable the mULFUAvailable to set
	 */
	public static void setMULFUAvailable(boolean mULFUAvailable) {
		MULFUAvailable = mULFUAvailable;
	}



	/**
	 * @return the lSFUAvailable
	 */
	public static boolean isLSFUAvailable() {
		return LSFUAvailable;
	}



	/**
	 * @param lSFUAvailable the lSFUAvailable to set
	 */
	public static void setLSFUAvailable(boolean lSFUAvailable) {
		LSFUAvailable = lSFUAvailable;
	}



	/**
	 * @return the bRANCHFUAvailable
	 */
	public static boolean isBRANCHFUAvailable() {
		return BRANCHFUAvailable;
	}



	/**
	 * @param bRANCHFUAvailable the bRANCHFUAvailable to set
	 */
	public static void setBRANCHFUAvailable(boolean bRANCHFUAvailable) {
		BRANCHFUAvailable = bRANCHFUAvailable;
	}



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
