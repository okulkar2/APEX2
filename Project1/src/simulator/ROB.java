package simulator;

public class ROB {
	
	Instruction [] queue;
	int front;
	int rear;
	int maxsize;
	
	public ROB(int maxSizeIn){
		maxsize = maxSizeIn;
		queue = new Instruction[maxsize];
		front=-1;
		rear=-1;
	}
	
	public void enqueue(Instruction inputInst){
		
		if(rear<(maxsize-1)){
			//System.out.println("Code Here Rear:"+rear);
			rear++;
			queue[rear]=inputInst;
			if(front==-1){
				front=0;
			}
			//System.out.println("Queue Element:"+queue[rear]+" Rear: "+rear);
		}
		else{
			System.err.println("The Queue is full");
		}
	}
	
	public Instruction dequeue(){
		Instruction currentInst = null;
		if(front==rear && rear==-1){
			System.err.println("The Queue is empty");
		}
		else{
			currentInst = queue[front];
			if(front==rear){
				front=-1;
				rear=-1;
			}
			else
				front++;
		}
		return currentInst;
		
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
