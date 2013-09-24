package doggytalents.client.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import doggytalents.DoggyTalentsMod;
import doggytalents.ModBlocks;
import doggytalents.api.DogBedManager;
import doggytalents.core.proxy.CommonProxy;
import doggytalents.tileentity.TileEntityDogBed;

/**
 * @author ProPercivalalb
 */
public class RenderWorldDogBed implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		TileEntity target = world.getBlockTileEntity(x, y, z);
		int meta = world.getBlockMetadata(x, y, z);

		GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);

		//Bottom Wood
		renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.2D, 1.0D);
		renderer.renderStandardBlock(block, x, y, z);
		
		if(meta == 0) {
			//Back
			renderer.setRenderBounds(0.0D, 0.2D, 0.9D, 1.0D, 0.6D, 1.0D);
			renderer.renderStandardBlock(block, x, y, z);
			
			//Right
			renderer.setRenderBounds(0.9D, 0.2D, 0.1D, 1.0D, 0.6D, 0.9D);
			renderer.renderStandardBlock(block, x, y, z);
			
			//Left
			renderer.setRenderBounds(0.0D, 0.2D, 0.1D, 0.1D, 0.6D, 0.9D);
			renderer.renderStandardBlock(block, x, y, z);
			
			//Front
			renderer.setRenderBounds(0.7D, 0.2D, 0.0D, 1.0D, 0.6D, 0.1D);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(0.0D, 0.2D, 0.0D, 0.3D, 0.6D, 0.1D);
			renderer.renderStandardBlock(block, x, y, z);
		}
		else if(meta == 1) {
			//Back
			renderer.setRenderBounds(0.0D, 0.2D, 0.0D, 0.1D, 0.6D, 1.0D);
			renderer.renderStandardBlock(block, x, y, z);
			
			//Right
			renderer.setRenderBounds(0.1D, 0.2D, 0.0D, 0.9D, 0.6D, 0.1D);
			renderer.renderStandardBlock(block, x, y, z);
			
			//Left
			renderer.setRenderBounds(0.1D, 0.2D, 0.9D, 0.9D, 0.6D, 1.0D);
			renderer.renderStandardBlock(block, x, y, z);
			
			//Front
			renderer.setRenderBounds(0.9D, 0.2D, 0.7D, 1.0D, 0.6D, 1.0D);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(0.9D, 0.2D, 0.0D, 1.0D, 0.6D, 0.3D);
			renderer.renderStandardBlock(block, x, y, z);
		}
		else if(meta == 2) {
			//Back
			renderer.setRenderBounds(0.0D, 0.2D, 0.0D, 1.0D, 0.6D, 0.1D);
			renderer.renderStandardBlock(block, x, y, z);
			
			//Right
			renderer.setRenderBounds(0.9D, 0.2D, 0.1D, 1.0D, 0.6D, 0.9D);
			renderer.renderStandardBlock(block, x, y, z);
			
			//Left
			renderer.setRenderBounds(0.0D, 0.2D, 0.1D, 0.1D, 0.6D, 0.9D);
			renderer.renderStandardBlock(block, x, y, z);
			
			//Front
			renderer.setRenderBounds(0.7D, 0.2D, 0.9D, 1.0D, 0.6D, 1.0D);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(0.0D, 0.2D, 0.9D, 0.3D, 0.6D, 1.0D);
			renderer.renderStandardBlock(block, x, y, z);
		}
		else if(meta == 3) {
			//Back
			renderer.setRenderBounds(0.9D, 0.2D, 0.0D, 1.0D, 0.6D, 1.0D);
			renderer.renderStandardBlock(block, x, y, z);
			
			//Right
			renderer.setRenderBounds(0.1D, 0.2D, 0.9D, 0.9D, 0.6D, 1.0D);
			renderer.renderStandardBlock(block, x, y, z);
			
			//Left
			renderer.setRenderBounds(0.1D, 0.2D, 0.0D, 0.9D, 0.6D, 0.1D);
			renderer.renderStandardBlock(block, x, y, z);
			
			//Front
			renderer.setRenderBounds(0.0D, 0.2D, 0.0D, 0.1D, 0.6D, 0.3D);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(0.0D, 0.2D, 0.7D, 0.1D, 0.6D, 1.0D);
			renderer.renderStandardBlock(block, x, y, z);
		}
	    
	    
	    if(target instanceof TileEntityDogBed) {
			TileEntityDogBed dogBed = (TileEntityDogBed)target;
			renderer.setOverrideBlockTexture(renderer.getIconSafe(DogBedManager.getWoolIcon(dogBed.getWoolId(), 1)));
		}
	    
	    renderer.setRenderBounds(0.1D, 0.2D, 0.1D, 0.9D, 0.4D, 0.9D);
	    renderer.renderStandardBlock(block, x, y, z);
	    
	    renderer.clearOverrideBlockTexture();
	    
	    GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
        return true;
	}

	@Override
	public boolean shouldRender3DInInventory() {
		return false;
	}

	@Override
	public int getRenderId() {
		return DoggyTalentsMod.proxy.RENDER_ID_DOG_BED;
	}
}
