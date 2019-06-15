package doggytalents.client.renderer.entity;

import doggytalents.entity.EntityDoggyBeam;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.init.Items;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderDogBeam extends RenderSnowball<EntityDoggyBeam> {

	public RenderDogBeam(RenderManager renderManagerIn) {
		super(renderManagerIn, Items.SNOWBALL, Minecraft.getMinecraft().getRenderItem());
	}

}
