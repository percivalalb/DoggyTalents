package doggytalents.client.render;

import org.lwjgl.opengl.GL11;

import com.google.common.base.Strings;

import doggytalents.ModBlocks;
import doggytalents.api.DogBedManager;
import doggytalents.tileentity.TileEntityDogBed;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraftforge.client.IItemRenderer;

/**
 * @author ProPercivalalb
 */
public class RenderItemDogBed implements IItemRenderer {

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
    	String woodId = "";
    	String woolId = "";
    	
		if (item.hasTagCompound() && item.stackTagCompound.hasKey("doggytalents")) {
	    	NBTTagCompound tag = item.stackTagCompound.getCompoundTag("doggytalents");
	    	woodId = tag.getString("woodId");
	    	woolId = tag.getString("woolId");
	    }
		
		if(type == ItemRenderType.EQUIPPED) {
			RenderBlocks renderBlocks = (RenderBlocks)data[0];
			EntityLivingBase entity = (EntityLivingBase)data[1];
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);

			//Bottom Wood
			renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.2D, 1.0D);
			this.renderInvBlockCasing(renderBlocks, ModBlocks.dogBed, woodId);
			
			//Back
			renderBlocks.setRenderBounds(0.0D, 0.2D, 0.0D, 0.1D, 0.6D, 1.0D);
			this.renderInvBlockCasing(renderBlocks, ModBlocks.dogBed, woodId);
			
			//Right
			renderBlocks.setRenderBounds(0.0D, 0.2D, 0.0D, 1.0D, 0.6D, 0.1D);
			this.renderInvBlockCasing(renderBlocks, ModBlocks.dogBed, woodId);
			
			//Left
			renderBlocks.setRenderBounds(0.0D, 0.2D, 0.9D, 1.0D, 0.6D, 1.0D);
			this.renderInvBlockCasing(renderBlocks, ModBlocks.dogBed, woodId);
			
			//Front
			renderBlocks.setRenderBounds(0.9D, 0.2D, 0.7D, 1.0D, 0.6D, 1.0D);
			this.renderInvBlockCasing(renderBlocks, ModBlocks.dogBed, woodId);
			renderBlocks.setRenderBounds(0.9D, 0.2D, 0.0D, 1.0D, 0.6D, 0.3D);
			this.renderInvBlockCasing(renderBlocks, ModBlocks.dogBed, woodId);
			
			//Wool
			renderBlocks.setRenderBounds(0.1D, 0.2D, 0.1D, 0.9D, 0.4D, 0.9D);
			this.renderInvBlockBedding(renderBlocks, ModBlocks.dogBed, woolId);
		}
		else if(type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
			RenderBlocks renderBlocks = (RenderBlocks)data[0];
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			
			//Bottom Wood
			renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.2D, 1.0D);
			this.renderInvBlockCasing(renderBlocks, ModBlocks.dogBed, woodId);
			
			//Back
			renderBlocks.setRenderBounds(0.0D, 0.2D, 0.0D, 0.1D, 0.6D, 1.0D);
			this.renderInvBlockCasing(renderBlocks, ModBlocks.dogBed, woodId);
			
			//Right
			renderBlocks.setRenderBounds(0.0D, 0.2D, 0.0D, 1.0D, 0.6D, 0.1D);
			this.renderInvBlockCasing(renderBlocks, ModBlocks.dogBed, woodId);
			
			//Left
			renderBlocks.setRenderBounds(0.0D, 0.2D, 0.9D, 1.0D, 0.6D, 1.0D);
			this.renderInvBlockCasing(renderBlocks, ModBlocks.dogBed, woodId);
			
			//Front
			renderBlocks.setRenderBounds(0.9D, 0.2D, 0.7D, 1.0D, 0.6D, 1.0D);
			this.renderInvBlockCasing(renderBlocks, ModBlocks.dogBed, woodId);
			renderBlocks.setRenderBounds(0.9D, 0.2D, 0.0D, 1.0D, 0.6D, 0.3D);
			this.renderInvBlockCasing(renderBlocks, ModBlocks.dogBed, woodId);
			
			//Wool
			renderBlocks.setRenderBounds(0.1D, 0.2D, 0.1D, 0.9D, 0.4D, 0.9D);
			this.renderInvBlockBedding(renderBlocks, ModBlocks.dogBed, woolId);
		}
		
		else if(type == ItemRenderType.ENTITY) {
			RenderBlocks renderBlocks = (RenderBlocks)data[0];
			EntityItem entityItem = (EntityItem)data[1];
			GL11.glScalef(1.1F, 1.1F, 1.1F);
			
			//Bottom Wood
			renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.2D, 1.0D);
			this.renderInvBlockCasing(renderBlocks, ModBlocks.dogBed, woodId);
			
			//Back
			renderBlocks.setRenderBounds(0.0D, 0.2D, 0.0D, 0.1D, 0.6D, 1.0D);
			this.renderInvBlockCasing(renderBlocks, ModBlocks.dogBed, woodId);
			
			//Right
			renderBlocks.setRenderBounds(0.0D, 0.2D, 0.0D, 1.0D, 0.6D, 0.1D);
			this.renderInvBlockCasing(renderBlocks, ModBlocks.dogBed, woodId);
			
			//Left
			renderBlocks.setRenderBounds(0.0D, 0.2D, 0.9D, 1.0D, 0.6D, 1.0D);
			this.renderInvBlockCasing(renderBlocks, ModBlocks.dogBed, woodId);
			
			//Front
			renderBlocks.setRenderBounds(0.9D, 0.2D, 0.7D, 1.0D, 0.6D, 1.0D);
			this.renderInvBlockCasing(renderBlocks, ModBlocks.dogBed, woodId);
			renderBlocks.setRenderBounds(0.9D, 0.2D, 0.0D, 1.0D, 0.6D, 0.3D);
			this.renderInvBlockCasing(renderBlocks, ModBlocks.dogBed, woodId);
			
			//Wool
			renderBlocks.setRenderBounds(0.1D, 0.2D, 0.1D, 0.9D, 0.4D, 0.9D);
			this.renderInvBlockBedding(renderBlocks, ModBlocks.dogBed, woolId);
		}
		
		else if(type == ItemRenderType.INVENTORY) {
			RenderBlocks renderBlocks = (RenderBlocks)data[0];
			
			//Bottom Wood
			renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.2D, 1.0D);
			this.renderInvBlockCasing(renderBlocks, ModBlocks.dogBed, woodId);
			
			//Back
			renderBlocks.setRenderBounds(0.0D, 0.2D, 0.0D, 0.1D, 0.6D, 1.0D);
			this.renderInvBlockCasing(renderBlocks, ModBlocks.dogBed, woodId);
			
			//Right
			renderBlocks.setRenderBounds(0.0D, 0.2D, 0.0D, 1.0D, 0.6D, 0.1D);
			this.renderInvBlockCasing(renderBlocks, ModBlocks.dogBed, woodId);
			
			//Left
			renderBlocks.setRenderBounds(0.0D, 0.2D, 0.9D, 1.0D, 0.6D, 1.0D);
			this.renderInvBlockCasing(renderBlocks, ModBlocks.dogBed, woodId);
			
			//Front
			renderBlocks.setRenderBounds(0.9D, 0.2D, 0.7D, 1.0D, 0.6D, 1.0D);
			this.renderInvBlockCasing(renderBlocks, ModBlocks.dogBed, woodId);
			renderBlocks.setRenderBounds(0.9D, 0.2D, 0.0D, 1.0D, 0.6D, 0.3D);
			this.renderInvBlockCasing(renderBlocks, ModBlocks.dogBed, woodId);
			
			//Wool
			renderBlocks.setRenderBounds(0.1D, 0.2D, 0.1D, 0.9D, 0.4D, 0.9D);
			this.renderInvBlockBedding(renderBlocks, ModBlocks.dogBed, woolId);
		}
	}

	public void renderInvBlockBedding(RenderBlocks renderer, Block block, String woolId) {
		Tessellator tessellator = Tessellator.instance;
	    GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
	    tessellator.startDrawingQuads();
	    tessellator.setNormal(0.0F, -1.0F, 0.0F);
	    renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getIconSafe(DogBedManager.getWoolIcon(woolId, 0)));
	    tessellator.draw();
	    tessellator.startDrawingQuads();
	    tessellator.setNormal(0.0F, 1.0F, 0.0F);
	    renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getIconSafe(DogBedManager.getWoolIcon(woolId, 1)));
	    tessellator.draw();
	    tessellator.startDrawingQuads();
	    tessellator.setNormal(0.0F, 0.0F, -1.0F);
	    renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getIconSafe(DogBedManager.getWoolIcon(woolId, 2)));
	    tessellator.draw();
	    tessellator.startDrawingQuads();
	    tessellator.setNormal(0.0F, 0.0F, 1.0F);
	    renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getIconSafe(DogBedManager.getWoolIcon(woolId, 3)));
	    tessellator.draw();
	    tessellator.startDrawingQuads();
	    tessellator.setNormal(-1.0F, 0.0F, 0.0F);
	    renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getIconSafe(DogBedManager.getWoolIcon(woolId, 4)));
	    tessellator.draw();
	    tessellator.startDrawingQuads();
	    tessellator.setNormal(1.0F, 0.0F, 0.0F);
	    renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getIconSafe(DogBedManager.getWoolIcon(woolId, 5)));
	    tessellator.draw();
	    GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}
	
	public void renderInvBlockCasing(RenderBlocks renderer, Block block, String woodId) {
		Tessellator tessellator = Tessellator.instance;
	    GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
	    tessellator.startDrawingQuads();
	    tessellator.setNormal(0.0F, -1.0F, 0.0F);
	    renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getIconSafe(DogBedManager.getWoodIcon(woodId, 0)));
	    tessellator.draw();
	    tessellator.startDrawingQuads();
	    tessellator.setNormal(0.0F, 1.0F, 0.0F);
	    renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getIconSafe(DogBedManager.getWoodIcon(woodId, 1)));
	    tessellator.draw();
	    tessellator.startDrawingQuads();
	    tessellator.setNormal(0.0F, 0.0F, -1.0F);
	    renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getIconSafe(DogBedManager.getWoodIcon(woodId, 2)));
	    tessellator.draw();
	    tessellator.startDrawingQuads();
	    tessellator.setNormal(0.0F, 0.0F, 1.0F);
	    renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getIconSafe(DogBedManager.getWoodIcon(woodId, 3)));
	    tessellator.draw();
	    tessellator.startDrawingQuads();
	    tessellator.setNormal(-1.0F, 0.0F, 0.0F);
	    renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getIconSafe(DogBedManager.getWoodIcon(woodId, 4)));
	    tessellator.draw();
	    tessellator.startDrawingQuads();
	    tessellator.setNormal(1.0F, 0.0F, 0.0F);
	    renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getIconSafe(DogBedManager.getWoodIcon(woodId, 5)));
	    tessellator.draw();
	    GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}
}
