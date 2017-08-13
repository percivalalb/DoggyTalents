package doggytalents.base.b;

import doggytalents.entity.EntityDoggyBeam;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityDoggyBeamWrapper extends EntityDoggyBeam {

	public EntityDoggyBeamWrapper(World worldIn) {
		super(worldIn);
	}
	
	public EntityDoggyBeamWrapper(World worldIn, EntityPlayer throwerIn) {
		super(worldIn, throwerIn);
	}

	@Override
	protected void onImpact(MovingObjectPosition result) {
		this.onImpactGENERAL(result.entityHit);
	}
}
