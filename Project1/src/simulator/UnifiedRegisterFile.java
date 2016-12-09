package simulator;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class UnifiedRegisterFile {

	private int urfSize;
	
	private Map<String,Register> archRegisters;
	private Map<String,Register> physicalRegisters;
	private Map<String,String> frontEndRat;
	
	public UnifiedRegisterFile() {
		
		archRegisters=new LinkedHashMap<String,Register>();
		physicalRegisters=new LinkedHashMap<String,Register>();
		frontEndRat=new LinkedHashMap<String,String>();
	}
	
	public void initRegisters() {
		
		int numPhysicalRegisters=urfSize-16;
		
		
		for(int i=0;i<16;i++)
			archRegisters.put("R"+i, new Register());
		
		for(int i=0;i<numPhysicalRegisters;i++) 
			physicalRegisters.put("P"+i, new Register());
		
		for(int i=0;i<16;i++)
			frontEndRat.put("R"+i, null);
	}
	
	public boolean isPhysicalRegisterAvailable() {
		
		boolean flag=false;
		
		for(Entry<String, Register> entry : physicalRegisters.entrySet()) {
			if(entry.getValue().isValid()) {
				flag=true;
				break;
			}
		}
		return flag;
	}
	
	public String getFreePhysicalRegister() {
		
		String physicalRegister=null;
		
		for(Entry<String, Register> entry : physicalRegisters.entrySet()) {	
			if(entry.getValue().isValid()) {
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
	
	public void display() {
		
		for(Entry<String, String> entry : frontEndRat.entrySet()) {
			
			System.out.println(entry.getKey()+" : "+entry.getValue());
		}
		
	}
	
}
