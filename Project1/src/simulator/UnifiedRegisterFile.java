package simulator;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class UnifiedRegisterFile {

	private int urfSize;
	
	private Map<String,Register> archRegisters;
	private Map<String,Register> physicalRegisters;
	private Map<String,String> frontEndRat;
	private Map<String,Boolean> freeList;
	
	public UnifiedRegisterFile(int urfSizeIn) {
		
		urfSize=urfSizeIn;
		archRegisters=new LinkedHashMap<String,Register>();
		physicalRegisters=new LinkedHashMap<String,Register>();
		frontEndRat=new LinkedHashMap<String,String>();
		freeList=new LinkedHashMap<String,Boolean>();
	}
	
	public void initRegisters() {
		
		int numPhysicalRegisters=urfSize-16;
		
		
		for(int i=0;i<16;i++)
			archRegisters.put("R"+i, new Register());
		
		for(int i=0;i<numPhysicalRegisters;i++) 
			physicalRegisters.put("P"+i, new Register());
		
		for(int i=0;i<16;i++)
			frontEndRat.put("R"+i, "P"+i);
		
		for(int i=0;i<numPhysicalRegisters;i++) 
			freeList.put("P"+i,true);
	}
	
	public boolean isPhysicalRegisterAvailable() {
		
		boolean flag=false;
		
		for(Entry<String, Boolean> entry : freeList.entrySet()) {
			
			if(entry.getValue()) {
				
				flag=true;
				break;
			}
		}
		return flag;
	}
	
	public String getFreePhysicalRegister() {
		
		String physicalRegister=null;
		
		for(Entry<String, Boolean> entry : freeList.entrySet()) {
			
			if(entry.getValue()) {
				
				physicalRegister=entry.getKey();
				break;
				
			}
		}
		
		return physicalRegister;
	}
	
	public int getUrfSize() {
		return urfSize;
	}

	public void setUrfSize(int urfSize) {
		this.urfSize = urfSize;
	}

	public Map<String, Register> getArchRegisters() {
		return archRegisters;
	}

	public void setArchRegisters(Map<String, Register> archRegisters) {
		this.archRegisters = archRegisters;
	}

	public Map<String, Register> getPhysicalRegisters() {
		return physicalRegisters;
	}

	public void setPhysicalRegisters(Map<String, Register> physicalRegisters) {
		this.physicalRegisters = physicalRegisters;
	}

	public Map<String, String> getFrontEndRat() {
		return frontEndRat;
	}

	public void setFrontEndRat(Map<String, String> frontEndRat) {
		this.frontEndRat = frontEndRat;
	}
	
	public Map<String, Boolean> getFreeList() {
		return freeList;
	}

	public void setFreeList(Map<String, Boolean> freeList) {
		this.freeList = freeList;
	}

	public void displayFrontEndRAT() {
		
		System.out.println("FRONT END RAT : ");
		for(Entry<String, String> entry : frontEndRat.entrySet()) {
			
			System.out.print("("+entry.getKey()+","+entry.getValue()+")");
		}
		System.out.println();
	}
	
	public void displayFreeList() {
		
		System.out.println("FREE LIST : ");
		for(Entry<String, Boolean> entry : freeList.entrySet()) {
			
			System.out.print("("+entry.getKey()+","+entry.getValue()+")");
		}
		System.out.println();
	}
	
	public void displayPhysicalRegisters() {
		
		System.out.println("PHYSICAL REGISTERS: ");
		for(Entry<String, Register> entry : physicalRegisters.entrySet()) {
			
			System.out.print("("+entry.getKey()+","+entry.getValue().getValue()+")");
		}
		System.out.println();
	}
	
	public void displayArchRegisters() {
		
		System.out.println("ARCH REGISTERS: ");
		for(Entry<String, Register> entry : archRegisters.entrySet()) {
			
			System.out.print("("+entry.getKey()+","+entry.getValue().getValue()+")");
		}
		System.out.println();
	}
}
