package doggytalents.helper;

import doggytalents.entity.EntityAbstractDog;
import doggytalents.lib.Constants;
import net.minecraft.util.text.TextComponentTranslation;

public class DogGenderUtil {
	
	EntityAbstractDog dog;
	
	public DogGenderUtil(EntityAbstractDog entitydog) {
		this.dog = entitydog;
	}
	
	public TextComponentTranslation getGenderSubjectFromString(String gender) {
		if(Constants.DOG_GENDER == true && gender == "male") {
			return new TextComponentTranslation("doggui.gender.male.subject");
		}else if (Constants.DOG_GENDER == true && gender == "female"){
			return new TextComponentTranslation("doggui.gender.female.subject");
		}else{
			return new TextComponentTranslation("doggui.gender.neuter.subject");
		}	
	}
	
	public boolean checkGender(EntityAbstractDog matedog) {
		if(Constants.DOG_GENDER == true && dog.getGender() == matedog.getGender())
			return false;
		else if(Constants.DOG_GENDER == true && dog.getGender() != matedog.getGender() && (matedog.getGender().isEmpty() || dog.getGender().isEmpty()))
			return false;
		else if(Constants.DOG_GENDER == true && dog.getGender() != matedog.getGender())
			return true;
		else
			return true;
	}
	
	public TextComponentTranslation getGenderPronoun() {
		if (Constants.DOG_GENDER == true && dog.getGender().equalsIgnoreCase("male")) {
			return new TextComponentTranslation("doggui.gender.male.pronoun");
		}
		else if(Constants.DOG_GENDER == true && dog.getGender().equalsIgnoreCase("female")){
			return new TextComponentTranslation("doggui.gender.female.pronoun");
		}else {
			return new TextComponentTranslation("doggui.gender.neuter.pronoun");
		}
	}

	public TextComponentTranslation getGenderSubject() {
		if (Constants.DOG_GENDER == true && dog.getGender().equalsIgnoreCase("male")) {
			return new TextComponentTranslation("doggui.gender.male.subject");
		}
		else if(Constants.DOG_GENDER == true && dog.getGender().equalsIgnoreCase("female")){
			return new TextComponentTranslation("doggui.gender.female.subject");
		}else{
			return new TextComponentTranslation("doggui.gender.neuter.subject");
		}
	}
	
	public TextComponentTranslation getGenderTitle() {
		if (Constants.DOG_GENDER == true && dog.getGender().equalsIgnoreCase("male")) {
			return new TextComponentTranslation("doggui.gender.male.title");
		}
		else if(Constants.DOG_GENDER == true && dog.getGender().equalsIgnoreCase("female")){
			return new TextComponentTranslation("doggui.gender.female.title");
		}else {
			return new TextComponentTranslation("doggui.gender.neuter.title");
		}
	}
	
	public String getGenderTip() {
		if(Constants.DOG_GENDER == true && dog.getGender().equalsIgnoreCase("male")) {
			return "doggui.gendertip.male";
		}else if(Constants.DOG_GENDER == true && dog.getGender().equalsIgnoreCase("female")) {
			return "doggui.gendertip.female";
		}else{
			return "";
		}
	}
	
	public String getGenderName() {
		if(Constants.DOG_GENDER == true && dog.getGender().equalsIgnoreCase("male")) {
			return "doggui.gender.male";
		}else if(Constants.DOG_GENDER == true && dog.getGender().equalsIgnoreCase("female")) {
			return "doggui.gender.female";
		}else{
			return "";
		}
	}
}
