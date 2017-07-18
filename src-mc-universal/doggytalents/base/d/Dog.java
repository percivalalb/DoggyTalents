package doggytalents.base.d;

import doggytalents.entity.EntityDog;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

/**
 * 1.12 Code
 */
public class Dog extends EntityDog {

	public Dog(World word) {
		super(word);
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_WOLF_HURT;
    }
}
