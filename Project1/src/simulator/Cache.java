package simulator;

import java.util.ArrayList;

public class Cache{
	
	private static ArrayList<String> memory = new ArrayList<String>();
	private FileOperations processFile;
	private static int instCount;
	private static String line1 = null; 
	
	public Cache(String fileName){
		processFile = new FileOperations(fileName);
		instCount = 0;
	}

	public void execute() {
		
		String line = null;
		
		while((line = processFile.readLineFromFile()) != null){
			memory.add(line);
		}
		processFile.closeFile();
	}

	public static int getInstCount() {
		return instCount;
	}
	
	public static void setInstCount(int instCountIn) {
		instCount = instCountIn;
	}

	public static String getInstruction(){
		
		try{
			if((line1=memory.get(instCount)) != null){
				instCount++;
			}
		}
		catch(IndexOutOfBoundsException e){
			line1 = null;
		}
		return line1;
	}
	
	public static int getSize(){
		//System.out.println("Size: "+memory.size());
		return memory.size();
	}
	
	public static void printCache(){
		
		for(int i=0;i<memory.size();i++){
			System.out.println(memory.get(i));
		}
		
	}
	
}
