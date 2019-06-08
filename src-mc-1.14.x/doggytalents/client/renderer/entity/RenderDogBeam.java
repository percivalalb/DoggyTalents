package doggytalents.client.renderer.entity;

import doggytalents.entity.EntityDoggyBeam;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;

public class RenderDogBeam extends SpriteRenderer<EntityDoggyBeam> {

	public RenderDogBeam(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, Minecraft.getInstance().getItemRenderer());
	}

}
