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
			do {	
				System.out.println("\nAPEX Simulator");
				System.out.println("1.Initialize");
				System.out.println("2.Simulate");
				System.out.println("3.Print Map Tables");
				System.out.println("4.Print Issue Queue");
				System.out.println("5.Print ROB");
				System.out.println("6.Print URF");
				System.out.println("7.Print Memory");
				System.out.println("8.Print Stats");
				System.out.println("9.Display");
				System.out.println("10.Exit");
				System.out.println("Enter your choice : ");
				in = new Scanner(System.in);
				String input = in.nextLine();
				choice = Integer.parseInt(input);
				
				switch (choice) {
					
					case 1:
							cache = new Cache(file_name);
							cache.execute();
							System.out.println("\nEnter URF size : ");
							input = in.nextLine();
							simulator = new Pipeline();
							simulator.initialize(Integer.parseInt(input));
							break;
					case 2:
							int i=1;
							System.out.println("\nEnter no. of cycles : ");
							input = in.nextLine();
							specifiedNoOfCycles = Integer.parseInt(input);
							while(i <= specifiedNoOfCycles){
								simulator.execute(i);
								i++;
							}
							break;
		
					case 3:
							simulator.printStages();
							simulator.printRegisterFile();
							simulator.printMemory();
							break;
					
					case 10:
							System.exit(0);
							break;
					default:
							break;
				}
			} while(choice != 4);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
