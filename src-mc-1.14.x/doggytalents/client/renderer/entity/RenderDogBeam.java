package doggytalents.client.renderer.entity;

import doggytalents.entity.EntityDoggyBeam;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSprite;
import net.minecraft.init.Items;

public class RenderDogBeam extends RenderSprite<EntityDoggyBeam> {

	public RenderDogBeam(RenderManager renderManagerIn) {
		super(renderManagerIn, Items.SNOWBALL, Minecraft.getInstance().getItemRenderer());
	}

}
