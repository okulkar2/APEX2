package simulator;

public class ROB {
	
	ROBEntry[] queue;
	int front;
	int rear;
	int maxsize;
	
	public ROB(int maxSizeIn){
		maxsize = maxSizeIn;
		queue = new ROBEntry[maxsize];
		front=-1;
		rear=-1;
	}
	
	public void enqueue(ROBEntry entry){
		
		if(rear<(maxsize-1)){
			//System.out.println("Code Here Rear:"+rear);
			rear++;
			queue[rear]=entry;
			if(front==-1){
				front=0;
			}
			//System.out.println("Queue Element:"+queue[rear]+" Rear: "+rear);
		}
		else{
			System.err.println("The Queue is full");
		}
	}
	
	public ROBEntry dequeue(){
		ROBEntry entry = null;
		if(front==rear && rear==-1){
			System.err.println("The Queue is empty");
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
	
	/*public void printQueue(){
		
		if(rear==-1 && front==-1){
			System.out.println("Queue is Empty");
			return;
		}
		System.out.print("Queue Elements:");
		for(int i=front;i<=rear;i++){
			System.out.print(queue[i]+" ");
		}
		System.out.print("\n");
	}*/
}
