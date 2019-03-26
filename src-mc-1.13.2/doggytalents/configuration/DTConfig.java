package doggytalents.configuration;

import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.common.ForgeConfigSpec;

public class DTConfig {

	public ForgeConfigSpec.BooleanValue DEBUG_MODE;
	
	public ForgeConfigSpec.BooleanValue DOGS_IMMORTAL;
	public ForgeConfigSpec.BooleanValue TEN_DAY_PUPS;
	public ForgeConfigSpec.BooleanValue IS_HUNGER_ON;
	public ForgeConfigSpec.BooleanValue DIRE_PARTICLES;
	public ForgeConfigSpec.BooleanValue STARTING_ITEMS;
	public ForgeConfigSpec.BooleanValue RENDER_BLOOD;
	public ForgeConfigSpec.BooleanValue DOGGY_WINGS;
	public ForgeConfigSpec.BooleanValue DOG_GENDER;
	public ForgeConfigSpec.BooleanValue DOG_WHINE_WHEN_HUNGER_LOW;
	
	public Map<String, ForgeConfigSpec.BooleanValue> DISABLED_TALENTS;
	
	public DTConfig(ForgeConfigSpec.Builder builder) {
		builder.push("General");
		  
		DEBUG_MODE = builder
				.comment("Enables debugging mode, which would output values for the sake of finding issues in the mod.")
				.define("debugMode", false);	
		
		builder.pop();
		builder.push("Dog Settings");
		  
		DOGS_IMMORTAL = builder
				.comment("Determines if dogs die when their health reaches zero. If true, dogs will not die, and will instead become incapacitated.")
				.define("isDogImmortal", true);	  
		TEN_DAY_PUPS = builder
				.comment("Determines if pups take 10 days to mature.")
				.define("tenDayPuppies", true);
		IS_HUNGER_ON = builder
				.comment("Enables hunger mode for the dog")
				.define("isHungerOn", true);
		DIRE_PARTICLES = builder
				.comment("Enables the particle effect on Dire Level 30 dogs.")
				.define("direParticles", true);
		STARTING_ITEMS = builder
				.comment("When enabled you will spawn with a guide, Doggy Charm and Command Emblem.")
				.define("isStartingItemsEnabled", true);
		RENDER_BLOOD = builder
				.comment("When enabled, Dogs will bleed while incapacitated.")
				.define("bloodWhenIncapacitated", true);
		DOGGY_WINGS = builder
				.comment("When enabled, Dogs will have wings when at level 5 pillow paw.")
				.define("doggyWings", true);
		DOG_GENDER = builder
				.comment("When enabled, dogs will be randomly assigned genders and will only mate and produce children with the opposite gender.")
				.define("dogGender", true);
		DOG_WHINE_WHEN_HUNGER_LOW = builder
				.comment("Determines if dogs should whine when hunger reaches below 20 DP.")
				.define("shouldWhineWhenStarving", true);
		
		builder.pop();
		  
		builder.push("Talents");
		String[] talentIds = new String[] {"bedfinder", "blackpelt", "creepersweeper", "doggydash", "fisherdog", "guarddog", "happyeater", "hellhound", 
				"hunterdog", "packpuppy", "pestfighter", "pillowpaw", "poisonfang", "puppyeyes", "quickhealer", 
				"rescuedog", "roaringgale", "shepherddog", "swimmerdog", "wolfmount"}; 
		
		DISABLED_TALENTS = new HashMap<String, ForgeConfigSpec.BooleanValue>();
		
		for(String talentId : talentIds) {
			DISABLED_TALENTS.put(talentId, builder.define(talentId, true));
		}
		
		builder.pop();
	}
	
	public boolean debugMode() {
		return this.DEBUG_MODE.get().booleanValue();
	}
	
	public boolean dogsImmortal() {
		return this.DOGS_IMMORTAL.get().booleanValue();
	}
	
	public boolean tenDayPups() {
		return this.TEN_DAY_PUPS.get().booleanValue();
	}
	
	public boolean hungerOn() {
		return this.IS_HUNGER_ON.get().booleanValue();
	}
	
	public boolean direParticles() {
		return this.DIRE_PARTICLES.get().booleanValue();
	}
	
	public boolean startingItems() {
		return this.STARTING_ITEMS.get().booleanValue();
	}
	
	public boolean renderBlood() {
		return this.RENDER_BLOOD.get().booleanValue();
	}
	
	public boolean doggyWings() {
		return this.DOGGY_WINGS.get().booleanValue();
	}
	
	public boolean dogGender() {
		return this.DOG_GENDER.get().booleanValue();
	}
	
	public boolean whineWhenHungerLow() {
		return this.DOG_WHINE_WHEN_HUNGER_LOW.get().booleanValue();
	}
	
	public boolean isTalentDisabled(String talentId) {
		ForgeConfigSpec.BooleanValue entry = this.DISABLED_TALENTS.get(talentId);
		return entry != null && !entry.get().booleanValue();
	}
}
