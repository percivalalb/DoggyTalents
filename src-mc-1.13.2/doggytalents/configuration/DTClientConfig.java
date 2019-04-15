package doggytalents.configuration;

import net.minecraftforge.common.ForgeConfigSpec;

public class DTClientConfig {

	public ForgeConfigSpec.BooleanValue DIRE_PARTICLES;
	public ForgeConfigSpec.BooleanValue RENDER_BLOOD;
	public ForgeConfigSpec.BooleanValue DOGGY_WINGS;
	public ForgeConfigSpec.BooleanValue DOGGY_CHEST;
	public ForgeConfigSpec.BooleanValue DOGGY_SADDLE;
	public ForgeConfigSpec.BooleanValue USE_DT_TEXTURES;
	public ForgeConfigSpec.BooleanValue	DOGGY_ARMOUR;
	
	public DTClientConfig(ForgeConfigSpec.Builder builder) {
		builder.push("General");
		
		builder.pop();
		builder.push("Dog Settings");

		DIRE_PARTICLES = builder
				.comment("Enables the particle effect on Dire Level 30 dogs.")
				.define("direParticles", true);
		RENDER_BLOOD = builder
				.comment("When enabled, Dogs will show blood texture while incapacitated.")
				.define("bloodWhenIncapacitated", true);
		DOGGY_WINGS = builder
				.comment("When enabled, Dogs will have wings when at level 5 pillow paw.")
				.define("doggyWings", true);
		DOGGY_CHEST = builder
				.comment("When enabled, dogs with points in pack puppy will have chests on their side.")
				.define("doggyChest", true);
		DOGGY_SADDLE = builder
				.comment("When enabled, dogs with points in wolf mount will have a saddle on.")
				.define("doggySaddle", true);
		USE_DT_TEXTURES = builder
				.comment("If disabled will use the default minecraft wolf skin for all dog textures.")
				.define("useDTTextures", true);
		DOGGY_ARMOUR = builder
				.comment("When enabled, dogs with points in guard dog will have armour.")
				.define("doggyArmour", false);
		
		builder.pop();
	}
	
	public boolean direParticles() {
		return this.DIRE_PARTICLES.get().booleanValue();
	}

	public boolean renderBlood() {
		return this.RENDER_BLOOD.get().booleanValue();
	}
	
	public boolean doggyWings() {
		return this.DOGGY_WINGS.get().booleanValue();
	}
	
	public boolean doggyChest() {
		return this.DOGGY_CHEST.get().booleanValue();
	}
	
	public boolean doggySaddle() {
		return this.DOGGY_SADDLE.get().booleanValue();
	}
	
	public boolean useDTTextures() {
		return this.USE_DT_TEXTURES.get().booleanValue();
	}

	public boolean doggyArmour() {
		return this.DOGGY_ARMOUR.get().booleanValue();
	}
}
