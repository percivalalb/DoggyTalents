package doggytalents.entity;

/**
 * @author ProPercivalalb
 * You may look at this file to gain knowledge of javascript
 * but must not copy any features or code directly.
 **/
public class DoggySkills {

	public EntityDTDoggy doggy;
	
	protected DoggySkills(EntityDTDoggy par1EntityDTDoggy) {
		this.doggy = par1EntityDTDoggy;
	}
	
	public int getTalentLevel(EnumSkills par1EnumSkills) {
		int ID = par1EnumSkills.getID();
		try{return new Integer(this.getSkillsLevels()[ID]);}
    	catch(Exception e){e.printStackTrace(); return 0;}
	}
	
	protected int getTalentLevel(int par1) {
		try{return new Integer(this.getSkillsLevels()[par1]);}
    	catch(Exception e){e.printStackTrace(); return 0;}
	}
	
	public void increaseTalentLevel(EnumSkills par1EnumSkills, int factor) {
		this.setTalentLevel(par1EnumSkills, this.getTalentLevel(par1EnumSkills) + factor);
	}
	
	public void resetTalents() {
		this.doggy.getDataWatcher().updateObject(20, "0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0");
	}
	
	public void setTalentLevel(EnumSkills par1EnumSkills, int newLevel) {
		int var1;
		String structure = "";
		for(var1 = 0; var1 < EnumSkills.getUpperLimit(); ++var1) {
			int level = this.getTalentLevel(var1);
			
			if(par1EnumSkills.getID() == var1) {
				structure = structure + newLevel + (var1 == EnumSkills.getUpperLimit() - 1 ? "" : ":");
			}
			else {
				structure = structure + level + (var1 == EnumSkills.getUpperLimit() - 1 ? "" : ":");	
			}
		}
		
		this.doggy.getDataWatcher().updateObject(20, structure);
	}
	
	protected String writeAllTalents() {
		int var1;
		String structure = "";
		for(var1 = 0; var1 < EnumSkills.getUpperLimit(); ++var1) {
			int level = this.getTalentLevel(var1);
			
			structure = structure + level + (var1 == EnumSkills.getUpperLimit() - 1 ? "" : ":");
		}
		
		this.doggy.getDataWatcher().updateObject(20, structure);
		return structure;
	}
	
	private String[] getSkillsLevels() {
		String data = this.getLevelString();
		String[] tokens = data.split(":");
		return tokens;
	}
	
	private String getLevelString() {
		return doggy.getDataWatcher().getWatchableObjectString(20);
	}
}
