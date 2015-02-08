package doggytalents.client.renderer.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import doggytalents.ModBlocks;

/**
 * @author ProPercivalalb
 */
public class RenderItemDogBath implements IItemRenderer {

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
		
		if(type == ItemRenderType.EQUIPPED) {
			RenderBlocks renderBlocks = (RenderBlocks)data[0];
			EntityLivingBase entity = (EntityLivingBase)data[1];
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			this.renderdogBath(renderBlocks);
		}
		else if(type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
			RenderBlocks renderBlocks = (RenderBlocks)data[0];
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			this.renderdogBath(renderBlocks);
		}
		
		else if(type == ItemRenderType.ENTITY) {
			RenderBlocks renderBlocks = (RenderBlocks)data[0];
			EntityItem entityItem = (EntityItem)data[1];
			GL11.glScalef(1.1F, 1.1F, 1.1F);
			this.renderdogBath(renderBlocks);
		}
		
		else if(type == ItemRenderType.INVENTORY) {
			RenderBlocks renderBlocks = (RenderBlocks)data[0];
			this.renderdogBath(renderBlocks);
		}
	}
	
	public void renderdogBath(RenderBlocks renderBlocks) {
		//Bottom Wood
		renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.2D, 1.0D);
		this.renderStandardInvBlock(renderBlocks, ModBlocks.dogBath, ModBlocks.dogBath.getIcon(1, 0));
		
		//Back
		renderBlocks.setRenderBounds(0.0D, 0.2D, 0.0D, 0.1D, 0.6D, 1.0D);
		this.renderStandardInvBlock(renderBlocks, ModBlocks.dogBath, ModBlocks.dogBath.getIcon(1, 0));
		
		//Right
		renderBlocks.setRenderBounds(0.0D, 0.2D, 0.0D, 1.0D, 0.6D, 0.1D);
		this.renderStandardInvBlock(renderBlocks, ModBlocks.dogBath, ModBlocks.dogBath.getIcon(1, 0));
		
		//Left
		renderBlocks.setRenderBounds(0.0D, 0.2D, 0.9D, 1.0D, 0.6D, 1.0D);
		this.renderStandardInvBlock(renderBlocks, ModBlocks.dogBath, ModBlocks.dogBath.getIcon(1, 0));
		
		//Front
		renderBlocks.setRenderBounds(0.9D, 0.2D, 0.0D, 1.0D, 0.6D, 1.0D);
		this.renderStandardInvBlock(renderBlocks, ModBlocks.dogBath, ModBlocks.dogBath.getIcon(1, 0));
		
		//Wool
		renderBlocks.setRenderBounds(0.1D, 0.2D, 0.1D, 0.9D, 0.4D, 0.9D);
		this.renderStandardInvBlock(renderBlocks, ModBlocks.dogBath, Blocks.water.getBlockTextureFromSide(1));
	}

	public void renderStandardInvBlock(RenderBlocks renderer, Block block, IIcon icon) {
		Tessellator tessellator = Tessellator.instance;
	    GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
	    tessellator.startDrawingQuads();
	    tessellator.setNormal(0.0F, -1.0F, 0.0F);
	    renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getIconSafe(icon));
	    tessellator.draw();
	    tessellator.startDrawingQuads();
	    tessellator.setNormal(0.0F, 1.0F, 0.0F);
	    renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getIconSafe(icon));
	    tessellator.draw();
	    tessellator.startDrawingQuads();
	    tessellator.setNormal(0.0F, 0.0F, -1.0F);
	    renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getIconSafe(icon));
	    tessellator.draw();
	    tessellator.startDrawingQuads();
	    tessellator.setNormal(0.0F, 0.0F, 1.0F);
	    renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getIconSafe(icon));
	    tessellator.draw();
	    tessellator.startDrawingQuads();
	    tessellator.setNormal(-1.0F, 0.0F, 0.0F);
	    renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getIconSafe(icon));
	    tessellator.draw();
	    tessellator.startDrawingQuads();
	    tessellator.setNormal(1.0F, 0.0F, 0.0F);
	    renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getIconSafe(icon));
	    tessellator.draw();
	    GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}
}
