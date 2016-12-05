package simulator;

import java.util.Scanner;
/*Driver Class */
public class Driver {

	private static Scanner in;

	public static void main(String[] args) {
		 
		
		String file_name = args[0];
		int specifiedNoOfCycles;
		Cache cache = null; 
		Pipeline simulator = null;
		int choice;
		
		
		try {
			do{
				System.out.println("\nAPEX Simulator");
				System.out.println("1.Initialize");
				System.out.println("2.Simulate");
				System.out.println("3.Display");
				System.out.println("4.Exit");
				System.out.println("Enter your choice : ");
				in = new Scanner(System.in);
				String input = in.nextLine();
				choice = Integer.parseInt(input);
				
				switch (choice) {
				case 1:
					cache = new Cache(file_name);
					cache.execute();
					simulator = new Pipeline();
					Pipeline.initialize();
					break;
				case 2:
					int i=0;
					System.out.println("\nEnter no. of cycles : ");
					input = in.nextLine();
					specifiedNoOfCycles = Integer.parseInt(input);
					//System.out.println("No of Cycles:"+specifiedNoOfCycles);
					if(simulator == null){
						System.err.println("Please initialize the Pipeline and then try");
						break;
					}
					while(i < specifiedNoOfCycles){
						//System.out.println("Cycle:"+(i+1));
						simulator.execute();
						i++;
					}
				break;
	
				case 3:
					simulator.printStages();
					simulator.printRegisterFile();
					simulator.printMemory();
				break;
				default:
				break;
				}
			}while(choice != 4);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}

}
