package doggytalents.base.c;

import doggytalents.entity.EntityDog;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

/**
 * 1.11.2 Code
 */
public class Dog extends EntityDog {

	public Dog(World word) {
		super(word);
	}

	@Override
	protected SoundEvent getHurtSound() {
        return SoundEvents.ENTITY_WOLF_HURT;
    }
}
