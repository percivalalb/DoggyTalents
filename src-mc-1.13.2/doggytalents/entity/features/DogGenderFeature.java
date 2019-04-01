package doggytalents.entity.features;

import doggytalents.configuration.ConfigHandler;
import doggytalents.entity.EntityDog;
import net.minecraft.util.text.TextComponentTranslation;

public class DogGenderFeature extends DogFeature {
	
	public DogGenderFeature(EntityDog dogIn) {
		super(dogIn);
	}
	
	public TextComponentTranslation getGenderSubjectFromString(String gender) {
		if(ConfigHandler.COMMON.dogGender() && gender == "male") {
			return new TextComponentTranslation("doggui.gender.male.subject");
		}else if (ConfigHandler.COMMON.dogGender() && gender == "female"){
			return new TextComponentTranslation("doggui.gender.female.subject");
		}else{
			return new TextComponentTranslation("doggui.gender.neuter.subject");
		}	
	}
	
	public boolean checkGender(EntityDog matedog) {
		if(ConfigHandler.COMMON.dogGender() && dog.getGender() == matedog.getGender())
			return false;
		else if(ConfigHandler.COMMON.dogGender() && dog.getGender() != matedog.getGender() && (matedog.getGender().isEmpty() || dog.getGender().isEmpty()))
			return false;
		else if(ConfigHandler.COMMON.dogGender() && dog.getGender() != matedog.getGender())
			return true;
		else
			return true;
	}
	
	public TextComponentTranslation getGenderPronoun() {
		if (ConfigHandler.COMMON.dogGender() && dog.getGender().equalsIgnoreCase("male")) {
			return new TextComponentTranslation("doggui.gender.male.pronoun");
		}
		else if(ConfigHandler.COMMON.dogGender() && dog.getGender().equalsIgnoreCase("female")){
			return new TextComponentTranslation("doggui.gender.female.pronoun");
		}else {
			return new TextComponentTranslation("doggui.gender.neuter.pronoun");
		}
	}

	public TextComponentTranslation getGenderSubject() {
		if (ConfigHandler.COMMON.dogGender() && dog.getGender().equalsIgnoreCase("male")) {
			return new TextComponentTranslation("doggui.gender.male.subject");
		}
		else if(ConfigHandler.COMMON.dogGender() && dog.getGender().equalsIgnoreCase("female")){
			return new TextComponentTranslation("doggui.gender.female.subject");
		}else{
			return new TextComponentTranslation("doggui.gender.neuter.subject");
		}
	}
	
	public TextComponentTranslation getGenderTitle() {
		if (ConfigHandler.COMMON.dogGender() && dog.getGender().equalsIgnoreCase("male")) {
			return new TextComponentTranslation("doggui.gender.male.title");
		}
		else if(ConfigHandler.COMMON.dogGender() && dog.getGender().equalsIgnoreCase("female")){
			return new TextComponentTranslation("doggui.gender.female.title");
		}else {
			return new TextComponentTranslation("doggui.gender.neuter.title");
		}
	}
	
	public String getGenderTip() {
		if(ConfigHandler.COMMON.dogGender() && dog.getGender().equalsIgnoreCase("male")) {
			return "doggui.gendertip.male";
		}else if(ConfigHandler.COMMON.dogGender() && dog.getGender().equalsIgnoreCase("female")) {
			return "doggui.gendertip.female";
		}else{
			return "";
		}
	}
	
	public String getGenderName() {
		if(ConfigHandler.COMMON.dogGender() && dog.getGender().equalsIgnoreCase("male")) {
			return "doggui.gender.male";
		}else if(ConfigHandler.COMMON.dogGender() && dog.getGender().equalsIgnoreCase("female")) {
			return "doggui.gender.female";
		}else{
			return "";
		}
	}
}
