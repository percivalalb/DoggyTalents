package doggytalents.base.c;

import doggytalents.entity.EntityDoggyBeam;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityDoggyBeamWrapper extends EntityDoggyBeam {

	public EntityDoggyBeamWrapper(World worldIn, EntityPlayer throwerIn) {
		super(worldIn, throwerIn);
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		this.onImpactGENERAL(result.entityHit);
	}

}
