package doggytalents.configuration;

import net.minecraftforge.common.ForgeConfigSpec;

public class DTClientConfig {

	public ForgeConfigSpec.BooleanValue DIRE_PARTICLES;
	public ForgeConfigSpec.BooleanValue RENDER_BLOOD;
	public ForgeConfigSpec.BooleanValue DOGGY_WINGS;
	
	public DTClientConfig(ForgeConfigSpec.Builder builder) {
		builder.push("General");
		
		builder.pop();
		builder.push("Dog Settings");

		DIRE_PARTICLES = builder
				.comment("Enables the particle effect on Dire Level 30 dogs.")
				.define("direParticles", true);
		RENDER_BLOOD = builder
				.comment("When enabled, Dogs will bleed while incapacitated.")
				.define("bloodWhenIncapacitated", true);
		DOGGY_WINGS = builder
				.comment("When enabled, Dogs will have wings when at level 5 pillow paw.")
				.define("doggyWings", true);
		
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
}
