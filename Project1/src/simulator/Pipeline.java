package simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class Pipeline {

	private static HashMap<String, Instruction> stages = new HashMap<String, Instruction>();
	private static HashMap<String, Register> registerFile = new HashMap<String, Register>();
	private static HashMap<String, PipelineRegister> pipelinestages = new HashMap<String, PipelineRegister>(
			5);
	private static HashMap<String, Integer> specialRegister = new HashMap<String, Integer>(
			2);
	private static Integer[] memory = new Integer[4000];
	private static int programCounter;
	private String fetchInst;
	boolean stall = false;
	private static List<Boolean> freePhyRegisters = new ArrayList<Boolean>();
	static final int noOfRegisters = 16;
	private static ROB rob = new ROB(noOfRegisters);
	private static IssueQueue issueQ = new IssueQueue();

	public Pipeline() {
	}

	public static void initialize() {
		stages.put("F", null);
		stages.put("D/RF", null);
		stages.put("IntALU1", null);
		stages.put("IntALU2", null);
		stages.put("BranchALU", null);
		stages.put("Delay", null);
		stages.put("MEM", null);
		stages.put("WB", null);
		Flag.setStallFlag(false);
		Flag.setZeroFlag(false);
		Flag.setForwardEXFlag(false);
		Flag.setForwardMEMFlag(false);
		Flag.setBranchFlag(false);

		for (int i = 0; i < 16; i++) {
			registerFile.put("R" + i, new Register());
		}

		for (int i = 0; i < 4000; i++) {
			memory[i] = 0;
		}
		pipelinestages.put("Branch", new PipelineRegister());
		pipelinestages.put("EX2", new PipelineRegister());
		pipelinestages.put("MEM", new PipelineRegister());
		pipelinestages.put("WB", new PipelineRegister());
		pipelinestages.put("WB1", new PipelineRegister());

		specialRegister.put("X", 0);
		specialRegister.put("Fetch", 0);

		programCounter = 4000;
		for (int i = 0; i < noOfRegisters; i++) {

			freePhyRegisters.add(false);

		}

	}

	public void execute() {

		write();
		writeBack();
		memStage();
		executer();
		issueQueue();
		decode();
		fetch();

	}

	public void write() {

		if (stages.get("WB") != null) {
			Instruction currentInst = stages.get("WB");
			if (currentInst.getOperand().equalsIgnoreCase("HALT")) {
				stages.put("WB", null);
				return;
			}
			if (pipelinestages.get("WB").getRegister() != null) {
				PipelineRegister pipeResult = pipelinestages.get("WB");
				PipelineRegister pipeMem = pipelinestages.get("WB1");
				pipeMem.setPipelineRegister(pipelinestages.get("WB")
						.getRegister(), pipelinestages.get("WB").getValue());
				pipelinestages.put("WB1", pipeMem);
				if (pipeResult.getRegister() != null
						&& pipeResult.getRegister().equalsIgnoreCase(
								currentInst.getDestination())) {
					Register result = registerFile
							.get(pipeResult.getRegister());
					result.setValue(pipeResult.getValue());
					result.setValid(true);
					registerFile.put(pipeResult.getRegister(), result);
				}
			} else {
				PipelineRegister pipeMem = pipelinestages.get("WB1");
				pipeMem.setPipelineRegister(null, pipelinestages.get("WB1")
						.getValue());
				pipelinestages.put("WB1", pipeMem);
			}
			stages.put("WB", null);
		}
	}

	public void writeBack() {

		if (stages.get("MEM") != null) {
			Instruction currentInst = stages.get("MEM");
			// System.out.println("WB Stage:"+currentInst);
			if (currentInst.getOperand().equalsIgnoreCase("HALT")) {
				stages.put("WB", currentInst);
				stages.put("MEM", null);
				return;
			}
			PipelineRegister pipeResult = pipelinestages.get("WB");
			if (pipelinestages.get("MEM").getRegister() != null) {
				pipeResult.setPipelineRegister(pipelinestages.get("MEM")
						.getRegister(), pipelinestages.get("MEM").getValue());
				// System.out.println("WB Register:"+pipeResult.getRegister()+" Value:"+pipeResult.getValue());
				pipelinestages.put("WB", pipeResult);
			} else {
				pipeResult.setPipelineRegister(null, pipelinestages.get("MEM")
						.getValue());
				// ystem.out.println("WB Register:null Value:"+pipeResult.getValue());
				pipelinestages.put("WB", pipeResult);
			}
			stages.put("WB", currentInst);
			stages.put("MEM", null);
		} else {
			// System.out.println("WB Stage:NOP");
		}
	}

	public void memStage() {

		if (stages.get("IntALU2") != null) {
			Instruction currentInst = stages.get("IntALU2");
			// System.out.println("MEM Stage:"+currentInst);
			if (currentInst.getOperand().equalsIgnoreCase("HALT")) {
				stages.put("MEM", currentInst);
				stages.put("IntALU2", null);
				return;
			}
			PipelineRegister pipeResult = pipelinestages.get("MEM");
			if (currentInst.getOperand().equals("LOAD")
					|| currentInst.getOperand().equalsIgnoreCase("STORE")) {
				if (currentInst.getOperand().equalsIgnoreCase("STORE")) {
					if (Flag.isForwardStoreFlagSet() == true
							&& pipelinestages.get("WB1").getRegister() != null
							&& pipelinestages.get("WB1").getRegister()
									.equalsIgnoreCase(currentInst.getSource1())) {
						currentInst.setSrc1Value(pipelinestages.get("WB1")
								.getValue());
						Flag.setForwardStoreFlag(false);
					}
				}
				int result = MEMStage.executeIntructionMEM(currentInst,
						pipelinestages.get("EX2").getValue());
				if (currentInst.getOperand().equals("LOAD")) {
					pipeResult.setPipelineRegister(
							currentInst.getDestination(), result);
				} else {
					pipeResult.setPipelineRegister(null, result);
				}
			} else {
				if (pipelinestages.get("EX2").getRegister() != null) {
					pipeResult.setPipelineRegister(pipelinestages.get("EX2")
							.getRegister(), pipelinestages.get("EX2")
							.getValue());
					// System.out.println("MEM Register:"+pipeResult.getRegister()+" Value:"+pipeResult.getValue());
				} else {
					pipeResult.setPipelineRegister(null,
							pipelinestages.get("EX2").getValue());
					// System.out.println("MEM Register:null Value:"+pipeResult.getValue());
				}
			}
			pipelinestages.put("MEM", pipeResult);
			stages.put("MEM", currentInst);
			stages.put("IntALU2", null);
		}
		if (stages.get("Delay") != null) {
			Instruction currentInst = stages.get("Delay");
			// System.out.println("MEM Stage:"+currentInst);
			if (currentInst.getDestination() == null) {
				PipelineRegister pipeResult = pipelinestages.get("MEM");
				pipeResult.setPipelineRegister(null, pipelinestages.get("EX2")
						.getValue());
				// System.out.println("MEM Register:null Value:"+pipeResult.getValue());
				pipelinestages.put("MEM", pipeResult);
			}
			stages.put("MEM", currentInst);
			stages.put("Delay", null);
		} else {
			// System.out.println("MEMDelay Stage:NOP");
		}
	}

	public void executer() {

		execute2();
		execute1();
	}

	private void execute2() {

		if (stages.get("IntALU1") != null) {
			Instruction currentInst = stages.get("IntALU1");
			if (currentInst.getOperand().equalsIgnoreCase("HALT")) {
				stages.put("IntALU2", currentInst);
				stages.put("IntALU1", null);
				return;
			}
			// System.out.println("Execute Stage2:"+currentInst);
			int result = IntegerFU.executeIntructionIntALU(currentInst);
			PipelineRegister pipeResult = pipelinestages.get("EX2");
			if (currentInst.getDestination() != null) {
				pipeResult.setPipelineRegister(currentInst.getDestination(),
						result);
				// System.out.println("EX2 Register:"+pipeResult.getRegister()+" Value:"+pipeResult.getValue());
				pipelinestages.put("EX2", pipeResult);
			} else {
				pipeResult.setPipelineRegister(null, result);
				// System.out.println("EX2 Register:null Value:"+pipeResult.getValue());
				pipelinestages.put("EX2", pipeResult);
			}
			stages.put("IntALU2", currentInst);
			stages.put("Delay", null);
			stages.put("IntALU1", null);
		} else {
			// System.out.println("Execute Stage2:NOP");
		}
		if (stages.get("BranchALU") != null) {
			Instruction currentInst = stages.get("BranchALU");
			// System.out.println("Delay Stage:"+currentInst);
			if (currentInst.getDestination() == null) {
				PipelineRegister pipeResult = pipelinestages.get("EX2");
				pipeResult.setPipelineRegister(null,
						pipelinestages.get("Branch").getValue());
				// System.out.println("EX2 Register:null Value:"+pipeResult.getValue());
				pipelinestages.put("EX2", pipeResult);
			}
			stages.put("Delay", currentInst);
			stages.put("IntALU2", null);
			stages.put("BranchALU", null);
		} else {
			// System.out.println("Delay Stage:NOP");
		}
	}

	private void execute1() {

		if (stages.get("D/RF") != null) {
			Instruction currentInst = stages.get("D/RF");
			if (currentInst.getFunction_unit().equalsIgnoreCase("IntALU1")) {
				stall(currentInst);
				if (Flag.isStallFlagSet() == false) {
					if (currentInst.getOperand().equalsIgnoreCase("HALT")) {
						Instruction current = stages.get("D/RF");
						stages.put("IntALU1", current);
						stages.put("D/RF", null);
						return;
					}

					if (currentInst.getSource1() != null) {
						if (Flag.isForwardEXFlagSet() == true && pipelinestages.get("MEM").getRegister() != null && pipelinestages.get("MEM").getRegister().equalsIgnoreCase(currentInst.getSource1())) {
							currentInst.setSrc1Value(pipelinestages.get("MEM").getValue());
							Flag.setForwardEXFlag(false);
						} else {
							if (Flag.isForwardMEMFlagSet() == true && pipelinestages.get("WB").getRegister() != null && pipelinestages.get("WB").getRegister().equalsIgnoreCase(currentInst.getSource1())) {
								currentInst.setSrc1Value(pipelinestages.get("WB").getValue());
								Flag.setForwardMEMFlag(false);
							} else {
								currentInst.setSrc1Value(registerFile.get(currentInst.getSource1()).getValue());
							}
						}
					}

					if (currentInst.getSource2() != null) {
						if (Flag.isForwardEXFlagSet() == true && pipelinestages.get("MEM").getRegister() != null && pipelinestages.get("MEM").getRegister().equalsIgnoreCase(currentInst.getSource2())) {
							currentInst.setSrc2Value(pipelinestages.get("MEM").getValue());
							Flag.setForwardEXFlag(false);
						} else {
							if (Flag.isForwardMEMFlagSet() == true && pipelinestages.get("WB").getRegister() != null
									&& pipelinestages
											.get("WB")
											.getRegister()
											.equalsIgnoreCase(
													currentInst.getSource2())) {
								currentInst.setSrc2Value(pipelinestages.get(
										"WB").getValue());
								Flag.setForwardMEMFlag(false);
							} else {
								currentInst.setSrc2Value(registerFile.get(
										currentInst.getSource2()).getValue());
							}
						}
					}

					if (currentInst.getDestination() != null) {
						Register result = registerFile.get(currentInst
								.getDestination());
						result.setValid(false);
					}
					// System.out.println("Execute Stage1:"+currentInst);
					stages.put("IntALU1", currentInst);
					stages.put("BranchALU", null);
					// stages.put("D/RF", null);
				} else {
					// System.out.println("Execute Stage1:NOP");
				}
			} else if (currentInst.getFunction_unit().equalsIgnoreCase(
					"BranchALU")) {
				stallBranch(currentInst);
				if (Flag.isStallFlagSet() == false) {
					int result = 0;
					if (currentInst.getSource1() != null) {
						if (Flag.isForwardEXFlagSet() == true && pipelinestages.get("MEM").getRegister().equalsIgnoreCase(currentInst.getSource1())) {
							currentInst.setSrc1Value(pipelinestages.get("MEM").getValue());
							result = BranchFU.executeIntructionBranchALU(currentInst);
							if (currentInst.getDestination() == null) {
								PipelineRegister pipeResult = pipelinestages.get("Branch");
								pipeResult.setPipelineRegister(null, 0);
								// System.out.println("Branch Register:"+pipeResult.getRegister()+" Value:"+pipeResult.getValue());
								pipelinestages.put("Branch", pipeResult);
							}
							Flag.setForwardEXFlag(false);
						} else {
							if (Flag.isForwardMEMFlagSet() == true && pipelinestages.get("WB").getRegister().equalsIgnoreCase(currentInst.getSource1())) {
								currentInst.setSrc1Value(pipelinestages.get("WB").getValue());
								result = BranchFU.executeIntructionBranchALU(currentInst);
								if (currentInst.getDestination() == null) {
									PipelineRegister pipeResult = pipelinestages.get("Branch");
									pipeResult.setPipelineRegister(null, 0);
									// System.out.println("Branch Register:"+pipeResult.getRegister()+" Value:"+pipeResult.getValue());
									pipelinestages.put("Branch", pipeResult);
								}
								Flag.setForwardEXFlag(false);
							} else {
								if (currentInst.getSource1() != null && !currentInst.getSource1().equalsIgnoreCase("X")) {
									currentInst.setSrc1Value(registerFile.get(currentInst.getSource1()).getValue());
								}
								result = BranchFU.executeIntructionBranchALU(currentInst);
								if (currentInst.getDestination() == null) {
									PipelineRegister pipeResult = pipelinestages.get("Branch");
									pipeResult.setPipelineRegister(null, 0);
									// System.out.println("Branch Register:"+pipeResult.getRegister()+" Value:"+pipeResult.getValue());
									pipelinestages.put("Branch", pipeResult);
								}
							}
						}
					}
					// System.out.println("Branch Stage:"+currentInst);
					specialRegister.put("Fetch", result);
					stages.put("BranchALU", currentInst);
					stages.put("IntALU1", null);
					// stages.put("D/RF", null);
				} else {
					// System.out.println("Branch Stage:NOP");
				}
			}
		}

		else {
			// System.out.println("Execute Stage1:NOP");
			// System.out.println("Branch Stage:NOP");
		}
	}

	public void issueQueue() {
		ROBEntry queue = new ROBEntry();
		/*
		 * Check ROB Entry, IQ Entry, Free Physical Register
		 */
		if (!(rob.checkIfROBIsFull()) && issueQ.checkIfIQIsFull() != -1
				&& checkIfFreePhyRegIsAvailable() != -1) {
			/*
			 * If ROB, IQ, PHY REG is unavailable set stallflag to true
			 */
			if (stages.get("D/RF") != null) {
				Instruction currentInst = stages.get("D/RF");

				if (Flag.isStallFlagSet() == false) {

					issueQ.setIssueQueueInstructionList(currentInst);
					queue.setDestArch(currentInst.getDestination());
					queue.setDestPhysical(currentInst.getDestination());
					queue.setPcValue(programCounter);
					rob.enqueue(queue);
					/* To Do: Allocate destination in RAT */

					stages.put("IQ", currentInst);
					stages.put("D/RF", null);

				} else {
					// Instruction current = stages.get("D/RF");
					// System.out.println("Decode Stage:"+current);
				}
			} else if (Flag.isBranchFlagSet() == true) {
				// System.out.println("D/RF Stage: Branchtaken");
				stages.put("IQ", null);
			} else {
				// System.out.println("Decode Stage:NOP");
			}

		} else {
			System.out.println("Stall ");
		}

	}

	public int checkIfFreePhyRegIsAvailable() {
		for (int i = 0; i < noOfRegisters; i++) {
			if (freePhyRegisters.get(i).equals(false)) {
				return i;
			}
		}
		return -1;
	}

	public void decode() {

		if (stages.get("F") != null && Flag.isBranchFlagSet() == false) {
			Instruction currentInst = stages.get("F");
			if (Flag.isStallFlagSet() == false) {
				String[] tokens = fetchInst.split("[ ,#]+");
				PrepareInstruction.prepare(currentInst, tokens);
				// System.out.println("Decode Stage:"+currentInst);
				stages.put("D/RF", currentInst);
				dependencyCheck(currentInst);
				stages.put("F", null);
			} else {
				// Instruction current = stages.get("D/RF");
				// System.out.println("Decode Stage:"+current);
			}
		} else if (Flag.isBranchFlagSet() == true) {
			// System.out.println("D/RF Stage: Branchtaken");
			stages.put("D/RF", null);
		} else {
			// System.out.println("Decode Stage:NOP");
		}
	}

	public void fetch() {

		if (stages.get("F") == null && Flag.isBranchFlagSet() == false) {
			if ((fetchInst = Cache.getInstruction()) != null) {
				Instruction newInstruction = new Instruction();
				newInstruction.setInstruction(fetchInst);
				newInstruction.setPc_value(programCounter);
				stages.put("F", newInstruction);
				// System.out.println("Fetched Instruction: " + programCounter +
				// " " + fetchInst);
				programCounter = programCounter + 4;
			} else {
				// System.out.println("Fetch Stage:NOP");
				stages.put("F", null);
			}
		} else if (Flag.isBranchFlagSet() == true) {
			int counter = 0;
			if (specialRegister.get("Fetch") != null) {
				counter = specialRegister.get("Fetch");
				// System.out.println("Counter Value"+counter);
			}
			int result1 = counter - 4000;
			int result = result1 / 4;
			// System.out.println("Instruction at"+result);
			Cache.setInstCount(result);
			programCounter = counter;
			Flag.setBranchFlag(false);
			stages.put("F", null);
		} else {
			// System.out.println("Fetch Instruction:"+fetchInst);
		}
	}

	private void dependencyCheck(Instruction input) {

		if (stages.get("IntALU1") != null) {
			Instruction exInput = stages.get("IntALU1");
			if (exInput.getDestination() != null) {
				if (input.getSource1() != null
						&& exInput.getDestination().equalsIgnoreCase(
								input.getSource1())) {
					input.setSrc1Valid(false);
					Flag.setStallFlag(true);
				}
				if (input.getSource2() != null
						&& exInput.getDestination().equalsIgnoreCase(
								input.getSource2())) {
					input.setSrc2Valid(false);
					Flag.setStallFlag(true);
				}
			}
		}

		if (stages.get("IntALU2") != null) {
			Instruction exInput = stages.get("IntALU2");
			if (exInput.getDestination() != null) {
				if (input.getSource1() != null
						&& exInput.getDestination().equalsIgnoreCase(
								input.getSource1())) {
					input.setSrc1Valid(false);
					Flag.setStallFlag(true);
				}
				if (input.getSource2() != null
						&& exInput.getDestination().equalsIgnoreCase(
								input.getSource2())) {
					input.setSrc2Valid(false);
					Flag.setStallFlag(true);
				}
			}
		}

		if (stages.get("MEM") != null) {
			Instruction exInput = stages.get("MEM");
			if (exInput.getDestination() != null) {
				if (input.getSource1() != null
						&& exInput.getDestination().equalsIgnoreCase(
								input.getSource1())) {
					input.setSrc1Valid(false);
					Flag.setStallFlag(true);
				}
				if (input.getSource2() != null
						&& exInput.getDestination().equalsIgnoreCase(
								input.getSource2())) {
					input.setSrc2Valid(false);
					Flag.setStallFlag(true);
				}
			}
		}
	}

	private void stall(Instruction inputInst) {

		Instruction MemInst = null;
		boolean[] sources = new boolean[2];

		sources[0] = inputInst.isSrc1Valid();
		sources[1] = inputInst.isSrc2Valid();

		if (stages.get("MEM") != null) {
			MemInst = stages.get("MEM");
		}

		if (inputInst.getSource1() != null && inputInst.isSrc1Valid() == false) {
			Register result = registerFile.get(inputInst.getSource1());
			if (MemInst != null
					&& !MemInst.getOperand().equalsIgnoreCase("LOAD")
					&& pipelinestages.get("MEM").getRegister() != null
					&& pipelinestages.get("MEM").getRegister()
							.equalsIgnoreCase(inputInst.getSource1())) {
				Flag.setForwardEXFlag(true);
				inputInst.setSrc1Valid(true);
			} else {
				if (MemInst != null
						&& MemInst.getOperand().equalsIgnoreCase("LOAD")
						&& pipelinestages.get("MEM").getRegister() != null
						&& pipelinestages.get("MEM").getRegister()
								.equalsIgnoreCase(inputInst.getSource1())) {
					if (inputInst.getOperand().equalsIgnoreCase("STORE")) {
						Flag.setForwardStoreFlag(true);
						inputInst.setSrc1Valid(true);
					}
				} else {
					if (pipelinestages.get("WB").getRegister() != null
							&& pipelinestages.get("WB").getRegister()
									.equalsIgnoreCase(inputInst.getSource1())) {
						Flag.setForwardMEMFlag(true);
						inputInst.setSrc1Valid(true);
					} else {
						if (result.isValid() == true) {
							inputInst.setSrc1Valid(true);
						}
					}
				}
			}
		}

		if (inputInst.getSource2() != null && inputInst.isSrc2Valid() == false) {
			Register result = registerFile.get(inputInst.getSource2());
			if (MemInst != null
					&& !MemInst.getOperand().equalsIgnoreCase("LOAD")
					&& pipelinestages.get("MEM").getRegister() != null
					&& pipelinestages.get("MEM").getRegister()
							.equalsIgnoreCase(inputInst.getSource2())) {
				Flag.setForwardEXFlag(true);
				inputInst.setSrc2Valid(true);
			} else {
				if (pipelinestages.get("WB").getRegister() != null
						&& pipelinestages.get("WB").getRegister()
								.equalsIgnoreCase(inputInst.getSource2())) {
					Flag.setForwardMEMFlag(true);
					inputInst.setSrc2Valid(true);
				} else {
					if (result.isValid() == true) {
						inputInst.setSrc2Valid(true);
					}
				}
			}
		}

		if (stages.get("IntALU2") != null) {
			Instruction exInput = stages.get("IntALU2");
			if (exInput.getDestination() != null) {
				if (inputInst.getSource1() != null
						&& exInput.getDestination().equalsIgnoreCase(
								inputInst.getSource1())) {
					inputInst.setSrc1Valid(false);
				}
				if (inputInst.getSource2() != null
						&& exInput.getDestination().equalsIgnoreCase(
								inputInst.getSource2())) {
					inputInst.setSrc2Valid(false);
				}
			}
		}

		if (inputInst.isSrc1Valid() == true && inputInst.isSrc2Valid() == true) {
			Flag.setStallFlag(false);
		}

		if (Flag.isStallFlagSet() == true) {
			inputInst.setSrc1Valid(sources[0]);
			inputInst.setSrc2Valid(sources[1]);
		}

	}

	private void stallBranch(Instruction inputInst) {

		Instruction MemInst = null;

		if (stages.get("MEM") != null) {
			MemInst = stages.get("MEM");
		}

		if (inputInst.getSource1() != null && inputInst.isSrc1Valid() == false) {
			Register result = registerFile.get(inputInst.getSource1());
			if (MemInst != null
					&& !MemInst.getOperand().equalsIgnoreCase("LOAD")
					&& pipelinestages.get("MEM").getRegister() != null
					&& pipelinestages.get("MEM").getRegister()
							.equalsIgnoreCase(inputInst.getSource1())) {
				Flag.setForwardEXFlag(true);
				inputInst.setSrc1Valid(true);
			} else {
				if (MemInst != null
						&& MemInst.getOperand().equalsIgnoreCase("LOAD")
						&& pipelinestages.get("MEM").getRegister() != null
						&& pipelinestages.get("MEM").getRegister()
								.equalsIgnoreCase(inputInst.getSource1())) {
					Flag.setForwardMEMFlag(true);
					;
					inputInst.setSrc1Valid(true);
				} else {
					if (result.isValid() == true) {
						inputInst.setSrc1Valid(true);
					}
				}
			}
		}

		if (inputInst.isSrc1Valid() == true) {
			Flag.setStallFlag(false);
		}

	}

	public static HashMap<String, Instruction> getStages() {
		return stages;
	}

	public static HashMap<String, Integer> getSpecialRegister() {
		return specialRegister;
	}

	public static void setSpecialRegister(String result, int value) {

		if (result.equalsIgnoreCase("Fetch")) {
			specialRegister.put("Fetch", value);
		} else {
			specialRegister.put("X", value);
		}
	}

	public static int getMemory(int index) {
		return memory[index];
	}

	public static void setMemory(int index, int value) {
		memory[index] = value;
	}

	public void printRegisterFile() {
		for (Entry<String, Register> entry : registerFile.entrySet()) {
			System.out.println(entry.getKey() + ":"
					+ entry.getValue().getValue());
		}
	}

	public void printMemory() {
		for (int i = 0; i < 20; i++) {
			System.out.println(i + " : " + memory[i] + "\t\t" + (i + 20)
					+ " : " + memory[i + 20] + "\t\t" + (i + 40) + " : "
					+ memory[i + 40] + "\t\t" + (i + 60) + " : "
					+ memory[i + 60] + "\t\t" + (i + 80) + " : "
					+ memory[i + 80]);
		}
	}

	public void printStages() {
		if (stages.get("F") != null) {
			System.out.println("Fetch Stage:"
					+ stages.get("F").getInstruction());
		} else {
			System.out.println("Fetch Stage: NOP");
		}
		if (stages.get("D/RF") != null) {
			System.out.println("Decode Stage:"
					+ stages.get("D/RF").getInstruction());
		} else {
			System.out.println("Decode Stage: NOP");
		}
		if (stages.get("IntALU1") != null) {
			System.out.println("Integer ALU1 Stage:"
					+ stages.get("IntALU1").getInstruction());
		} else {
			System.out.println("Integer ALU1 Stage: NOP");
		}
		if (stages.get("BranchALU") != null) {
			System.out.println("Branch FU Stage:"
					+ stages.get("BranchALU").getInstruction());
		} else {
			System.out.println("Branch FU Stage: NOP");
		}
		if (stages.get("IntALU2") != null) {
			System.out.println("IntALU2 Stage:"
					+ stages.get("IntALU2").getInstruction());
		} else {
			System.out.println("IntALU2 Stage: NOP");
		}
		if (stages.get("Delay") != null) {
			System.out.println("Delay Stage:"
					+ stages.get("Delay").getInstruction());
		} else {
			System.out.println("Delay Stage: NOP");
		}
		if (stages.get("MEM") != null) {
			System.out.println("MEM Stage:"
					+ stages.get("MEM").getInstruction());
		} else {
			System.out.println("MEM Stage: NOP");
		}
		if (stages.get("WB") != null) {
			System.out.println("WB Stage:" + stages.get("WB").getInstruction());
		} else {
			System.out.println("WB4 Stage: NOP");
		}
	}

}
