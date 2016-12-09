package simulator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class UnifiedRegisterFile {

	private int urfSize;
	
	private Map<String,Register> archRegisters;
	private Map<String,Register> physicalRegisters;
	private Map<String,String> frontEndRat;
	private List<Integer> freeList;
	
	public UnifiedRegisterFile() {
		
		archRegisters=new LinkedHashMap<String,Register>();
		physicalRegisters=new LinkedHashMap<String,Register>();
		frontEndRat=new LinkedHashMap<String,String>();
		freeList=new ArrayList<Integer>();
	}
	
	public void initRegisters() {
		
		int numPhysicalRegisters=urfSize-16;
		
		
		for(int i=0;i<16;i++)
			archRegisters.put("R"+i, new Register());
		
		for(int i=0;i<numPhysicalRegisters;i++) 
			physicalRegisters.put("P"+i, new Register());
		
		for(int i=0;i<16;i++)
			frontEndRat.put("R"+i, null);
		
		for(int i=0;i<numPhysicalRegisters;i++) 
			freeList.add(0);
	}
	
	public boolean isPhysicalRegisterAvailable() {
		
		boolean flag=false;
		
		for(int i=0;i<freeList.size();i++) {
			
			if(freeList.get(i)==0) {
				
				flag=true;
				break;
			}
		}
		return flag;
	}
	
	public int getFreePhysicalRegister() {
		
		int index=-1;
		
		for(int i=0;i<freeList.size();i++) {
			
			if(freeList.get(i)==0) {
				
				index=i;
				break;
			}
		}
		
		freeList.add(index, 1);
		return index;
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
	
	public void display() {
		
		for(Entry<String, String> entry : frontEndRat.entrySet()) {
			
			System.out.println(entry.getKey()+" : "+entry.getValue());
		}
		
	}
	
}
