package doggytalents.client.render;

import static net.minecraftforge.client.IItemRenderer.ItemRenderType.FIRST_PERSON_MAP;

import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.common.FMLLog;
import doggytalents.ModBlocks;
import doggytalents.api.DogBedManager;
import doggytalents.entity.EntityDTDoggy;

/**
 * @author ProPercivalalb
 */
public class RenderItemRadar implements IItemRenderer {

	private static final ResourceLocation field_148253_a = new ResourceLocation("textures/map/map_icons.png");
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack itemstack, ItemRendererHelper helper) {
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		if(type == ItemRenderType.EQUIPPED) {
	      
		}
		else if(type == ItemRenderType.FIRST_PERSON_MAP) {
			EntityPlayer player = (EntityPlayer)data[0];
			TextureManager textureManager = (TextureManager)data[1];
			MapData mapData = (MapData)data[2];
			World world = player.worldObj;
	
			byte b0 = 0;
            byte b1 = 0;
            Tessellator tessellator = Tessellator.instance;
            float f = 0.0F;
            textureManager.bindTexture(this.field_148253_a);
            int i = 0;
            GL11.glPushMatrix();
            GL11.glTranslatef(64F, 64F, -0.02F);


            GL11.glRotatef((player.rotationYaw % 360), 0.0F, 0.0F, -1.0F);
            GL11.glTranslatef(-64F, -64F, 0F);
 
			for(Object entity : world.loadedEntityList) {
				if(entity instanceof EntityDTDoggy) {
					EntityDTDoggy dog = (EntityDTDoggy)entity;

					if(dog.hasRadarCollar() && dog.canInteract(player)) {
						int icon = 6;
						if(dog.getOwner() != player)
							GL11.glColor3f(0.0F, 1.0F, 1.0F);
						
						int rotation = (int) dog.rotationYaw ;
						int centerX = (int) (player.posX - dog.posX);
						int centerZ = (int) (player.posZ - dog.posZ);
	                    GL11.glPushMatrix();
	                    GL11.glTranslatef((float)b0 + (float)centerX / 2.0F + 64.0F, (float)b1 + (float)centerZ / 2.0F + 64.0F, -0.02F);
	                    GL11.glRotatef((dog.rotationYaw % 360), 0.0F, 0.0F, 1.0F);
	                    GL11.glScalef(4.0F, 4.0F, 3.0F);
	                    GL11.glTranslatef(-0.125F, 0.125F, 0.0F);
	                    float f1 = (float)(icon % 4 + 0) / 4.0F;
	                    float f2 = (float)(icon / 4 + 0) / 4.0F;
	                    float f3 = (float)(icon % 4 + 1) / 4.0F;
	                    float f4 = (float)(icon / 4 + 1) / 4.0F;
	                    tessellator.startDrawingQuads();
	                    tessellator.addVertexWithUV(-1.0D, 1.0D, (double)((float)i * 0.001F), (double)f1, (double)f2);
	                    tessellator.addVertexWithUV(1.0D, 1.0D, (double)((float)i * 0.001F), (double)f3, (double)f2);
	                    tessellator.addVertexWithUV(1.0D, -1.0D, (double)((float)i * 0.001F), (double)f3, (double)f4);
	                    tessellator.addVertexWithUV(-1.0D, -1.0D, (double)((float)i * 0.001F), (double)f1, (double)f4);
	                    tessellator.draw();
	                    GL11.glPopMatrix();
	                    ++i;
					}
                }
            }
			GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslatef((float)b0 + 64.0F, (float)b1 + 64.0F, -0.02F);
            //GL11.glRotatef(dog.rotationPitch, 0.0F, 0.0F, 1.0F);
            GL11.glScalef(4.0F, 4.0F, 3.0F);
            GL11.glTranslatef(-0.125F, 0.125F, 0.0F);
            float f1 = (float)(5 % 4 + 0) / 4.0F;
            float f2 = (float)(5 / 4 + 0) / 4.0F;
            float f3 = (float)(5 % 4 + 1) / 4.0F;
            float f4 = (float)(5 / 4 + 1) / 4.0F;
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(-1.0D, 1.0D, (double)((float)i * 0.001F), (double)f1, (double)f2);
            tessellator.addVertexWithUV(1.0D, 1.0D, (double)((float)i * 0.001F), (double)f3, (double)f2);
            tessellator.addVertexWithUV(1.0D, -1.0D, (double)((float)i * 0.001F), (double)f3, (double)f4);
            tessellator.addVertexWithUV(-1.0D, -1.0D, (double)((float)i * 0.001F), (double)f1, (double)f4);
            tessellator.draw();
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 0.0F, -0.04F);
            GL11.glScalef(1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
            
			//Minecraft.getMinecraft().entityRenderer.getMapItemRenderer().func_148250_a(mapData, false);
		}
		
		else if(type == ItemRenderType.ENTITY) {
			RenderBlocks renderBlocks = (RenderBlocks)data[0];
			EntityItem entityItem = (EntityItem)data[1];
			
		}
		
		else if(type == ItemRenderType.INVENTORY) {
			RenderBlocks renderBlocks = (RenderBlocks)data[0];
		
		}
		
	}
}
