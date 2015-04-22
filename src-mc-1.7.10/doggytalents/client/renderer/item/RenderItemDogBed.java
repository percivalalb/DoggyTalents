package doggytalents.client.renderer.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import doggytalents.ModBlocks;
import doggytalents.api.registry.DogBedRegistry;

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
    	String casingId = "";
    	String beddingId = "";
    	
		if (item.hasTagCompound() && item.stackTagCompound.hasKey("doggytalents")) {
	    	NBTTagCompound tag = item.stackTagCompound.getCompoundTag("doggytalents");
	    	casingId = tag.getString("casingId");
	    	beddingId = tag.getString("beddingId");
	    }
		
		if(type == ItemRenderType.EQUIPPED) {
			RenderBlocks renderBlocks = (RenderBlocks)data[0];
			EntityLivingBase entity = (EntityLivingBase)data[1];
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			this.renderDogBed(renderBlocks, casingId, beddingId);
		}
		else if(type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
			RenderBlocks renderBlocks = (RenderBlocks)data[0];
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			this.renderDogBed(renderBlocks, casingId, beddingId);
		}
		
		else if(type == ItemRenderType.ENTITY) {
			RenderBlocks renderBlocks = (RenderBlocks)data[0];
			EntityItem entityItem = (EntityItem)data[1];
			GL11.glScalef(1.1F, 1.1F, 1.1F);
			this.renderDogBed(renderBlocks, casingId, beddingId);
		}
		
		else if(type == ItemRenderType.INVENTORY) {
			RenderBlocks renderBlocks = (RenderBlocks)data[0];
			this.renderDogBed(renderBlocks, casingId, beddingId);
		}
	}
	
	public void renderDogBed(RenderBlocks renderBlocks, String casingId, String beddingId) {
		//Bottom Wood
		renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.2D, 1.0D);
		this.renderInvBlockCasing(renderBlocks, ModBlocks.dogBed, casingId);
		
		//Back
		renderBlocks.setRenderBounds(0.0D, 0.2D, 0.0D, 0.1D, 0.6D, 1.0D);
		this.renderInvBlockCasing(renderBlocks, ModBlocks.dogBed, casingId);
		
		//Right
		renderBlocks.setRenderBounds(0.0D, 0.2D, 0.0D, 1.0D, 0.6D, 0.1D);
		this.renderInvBlockCasing(renderBlocks, ModBlocks.dogBed, casingId);
		
		//Left
		renderBlocks.setRenderBounds(0.0D, 0.2D, 0.9D, 1.0D, 0.6D, 1.0D);
		this.renderInvBlockCasing(renderBlocks, ModBlocks.dogBed, casingId);
		
		//Front
		renderBlocks.setRenderBounds(0.9D, 0.2D, 0.7D, 1.0D, 0.6D, 1.0D);
		this.renderInvBlockCasing(renderBlocks, ModBlocks.dogBed, casingId);
		renderBlocks.setRenderBounds(0.9D, 0.2D, 0.0D, 1.0D, 0.6D, 0.3D);
		this.renderInvBlockCasing(renderBlocks, ModBlocks.dogBed, casingId);
		
		//Wool
		renderBlocks.setRenderBounds(0.1D, 0.2D, 0.1D, 0.9D, 0.4D, 0.9D);
		this.renderInvBlockBedding(renderBlocks, ModBlocks.dogBed, beddingId);
	}

	public void renderInvBlockBedding(RenderBlocks renderer, Block block, String beddingId) {
		Tessellator tessellator = Tessellator.instance;
	    GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
	    tessellator.startDrawingQuads();
	    tessellator.setNormal(0.0F, -1.0F, 0.0F);
	    renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getIconSafe(DogBedRegistry.BEDDINGS.getIcon(beddingId, 0)));
	    tessellator.draw();
	    tessellator.startDrawingQuads();
	    tessellator.setNormal(0.0F, 1.0F, 0.0F);
	    renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getIconSafe(DogBedRegistry.BEDDINGS.getIcon(beddingId, 1)));
	    tessellator.draw();
	    tessellator.startDrawingQuads();
	    tessellator.setNormal(0.0F, 0.0F, -1.0F);
	    renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getIconSafe(DogBedRegistry.BEDDINGS.getIcon(beddingId, 2)));
	    tessellator.draw();
	    tessellator.startDrawingQuads();
	    tessellator.setNormal(0.0F, 0.0F, 1.0F);
	    renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getIconSafe(DogBedRegistry.BEDDINGS.getIcon(beddingId, 3)));
	    tessellator.draw();
	    tessellator.startDrawingQuads();
	    tessellator.setNormal(-1.0F, 0.0F, 0.0F);
	    renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getIconSafe(DogBedRegistry.BEDDINGS.getIcon(beddingId, 4)));
	    tessellator.draw();
	    tessellator.startDrawingQuads();
	    tessellator.setNormal(1.0F, 0.0F, 0.0F);
	    renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getIconSafe(DogBedRegistry.BEDDINGS.getIcon(beddingId, 5)));
	    tessellator.draw();
	    GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}
	
	public void renderInvBlockCasing(RenderBlocks renderer, Block block, String casingId) {
		Tessellator tessellator = Tessellator.instance;
	    GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
	    tessellator.startDrawingQuads();
	    tessellator.setNormal(0.0F, -1.0F, 0.0F);
	    renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getIconSafe(DogBedRegistry.CASINGS.getIcon(casingId, 0)));
	    tessellator.draw();
	    tessellator.startDrawingQuads();
	    tessellator.setNormal(0.0F, 1.0F, 0.0F);
	    renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getIconSafe(DogBedRegistry.CASINGS.getIcon(casingId, 1)));
	    tessellator.draw();
	    tessellator.startDrawingQuads();
	    tessellator.setNormal(0.0F, 0.0F, -1.0F);
	    renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getIconSafe(DogBedRegistry.CASINGS.getIcon(casingId, 2)));
	    tessellator.draw();
	    tessellator.startDrawingQuads();
	    tessellator.setNormal(0.0F, 0.0F, 1.0F);
	    renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getIconSafe(DogBedRegistry.CASINGS.getIcon(casingId, 3)));
	    tessellator.draw();
	    tessellator.startDrawingQuads();
	    tessellator.setNormal(-1.0F, 0.0F, 0.0F);
	    renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getIconSafe(DogBedRegistry.CASINGS.getIcon(casingId, 4)));
	    tessellator.draw();
	    tessellator.startDrawingQuads();
	    tessellator.setNormal(1.0F, 0.0F, 0.0F);
	    renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getIconSafe(DogBedRegistry.CASINGS.getIcon(casingId, 5)));
	    tessellator.draw();
	    GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}
}
