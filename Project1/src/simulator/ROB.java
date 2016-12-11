package simulator;

public class ROB {

	private ROBEntry[] queue;
	private UnifiedRegisterFile urf;
	private int front;
	private int rear;
	private int maxsize;
	
	public ROB(int maxSizeIn, UnifiedRegisterFile urfIn){
		maxsize = maxSizeIn;
		urf=urfIn;
		queue = new ROBEntry[maxsize];
		front=-1;
		rear=-1;
	}
	
	public int enqueue(ROBEntry entry){
		
		if(rear<(maxsize-1)){
			//System.out.println("Code Here Rear:"+rear);
			//ROBEntry entry=createROBEntry(instruction);
			rear++;
			queue[rear]=entry;
			if(front==-1){
				front=0;
			}
			return rear;
			//System.out.println("Queue Element:"+queue[rear]+" Rear: "+rear);
		}
		else{
			System.err.println("ROB full");
			return -1;
		}
	}
	
	/*
	private ROBEntry createROBEntry(Instruction instruction) {
		
		ROBEntry entry=new ROBEntry();
		entry.setDestArch(instruction.getDestination());
		entry.setDestPhysical(instruction.getDest_physical());
		entry.setPcValue(instruction.getPc_value());
		
		if(instruction.getDestination()!=null)
			entry.setSavedRATEntry(urf.getFrontEndRat().get(instruction.getDestination()));
		return entry;
	}*/
	
	
	public ROBEntry dequeue(){
		ROBEntry entry = null;
		if(front==rear && rear==-1){
			System.err.println("ROB empty");
		}
		else{
			entry = queue[front];
			if(front==rear){
				front=-1;
				rear=-1;
			}
			else
				front++;
		}
		return entry;
		
	}
	
	public int removeFromTail(){
		
		int rearvalue = 0;
		if(front==rear && rear==-1){
			System.err.println("ROB empty");
		}
		else{
			if(front==rear){
				front=-1;
				rear=-1;
				rearvalue = -1;
			}
			else{
				rearvalue = rear;
				rear--;
			}
		}
		return rearvalue;
		
	}

	public ROBEntry getROBEntry(int index) {
		
		if(front==rear && rear==-1){
			System.err.println("ROB empty");
			return null;
		}
		ROBEntry entry = queue[index];
		return entry;
	}
	
	
	public boolean isFreeSlotAvailable() {
		
		if(rear<=maxsize-1)
			return true;
		else
			return false;
	}
	
	public ROBEntry getROBHead(){
		return queue[front];
	}
	
	public void retire() {
		
		ROBEntry entry=getROBEntry(front);
		if(entry!=null && entry.isStatus()) {
			
			String instructionType=entry.getInstructionType();
			
			switch(instructionType) {
			
				case Constants.R2R:
									urf.getArchRegisters().get(entry.getDestArch()).setValue(entry.getResult());
									System.out.println("Saved RAT entry : "+entry.getSavedRATEntry());
									urf.getFreeList().put(entry.getSavedRATEntry(), true);
									front++;
									break;
				case Constants.LOAD:
									urf.getArchRegisters().get(entry.getDestArch()).setValue(entry.getResult());
									System.out.println("Saved RAT entry : "+entry.getSavedRATEntry());
									urf.getFreeList().put(entry.getSavedRATEntry(), true);
									front++;
									break;
				case Constants.STORE:
									front++;
									break;
						
				case Constants.BRANCH:
									break;
			
			}
			
		}
		else
			System.out.println("Rat entry is null");
	}
	
	public void printROB(){
		
		if(rear==-1 && front==-1){
			System.out.println("ROB is Empty");
			return;
		}
		System.out.println("ROB Elements:");
		for(int i=front;i<=rear;i++){
			System.out.println(queue[i].toString()+" ");
		}
	}
}
