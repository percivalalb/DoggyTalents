package doggytalents.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ReportedException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import doggytalents.common.ContainerPackPuppy;
import doggytalents.entity.EntityDTDoggy;
import doggytalents.entity.EnumSkills;

public class GuiPackPuppy extends GuiContainer
{
    private float xSize_wolf;
    private float ySize_wolf;
    private EntityDTDoggy wolf;
    private boolean mouseWasDown;

    public GuiPackPuppy(InventoryPlayer inventoryplayer, EntityDTDoggy entitydtdoggy)
    {
        super(new ContainerPackPuppy(inventoryplayer, entitydtdoggy));
        mouseWasDown = false;
        wolf = entitydtdoggy;
        allowUserInput = false;
        ySize = ySize + 60;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everythin in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int i, int j, float f)
    {
        super.drawScreen(i, j, f);
        xSize_wolf = i;
        ySize_wolf = j;
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.func_98187_b("/doggytalents/guiPackPuppy.png");
        int l = (width - xSize) / 2;
        int i1 = (height - ySize) / 2;
        drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);

        for (int j1 = 0; j1 < 3; j1++)
        {
            for (int k1 = 0; k1 < wolf.talents.getTalentLevel(EnumSkills.PACKPUPPY); k1++)
            {
                drawTexturedModalRect(l + 78 + 18 * k1, i1 + 9 + 18 * j1 + 15, 197, 2, 18, 18);
            }
        }

        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();
        GL11.glTranslatef(l + 51, i1 + 75, 50F);
        float f1 = 30F;
        GL11.glScalef(-f1, f1, f1);
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        float f2 = wolf.renderYawOffset;
        float f3 = wolf.rotationYaw;
        float f4 = wolf.rotationPitch;
        float f5 = (float)(l + 51) - xSize_wolf;
        float f6 = (float)((i1 + 75) - 50) - ySize_wolf;
        GL11.glRotatef(135F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-(float)Math.atan(f6 / 40F) * 20F, 1.0F, 0.0F, 0.0F);
        wolf.renderYawOffset = (float)Math.atan(f5 / 40F) * 20F;
        wolf.rotationYaw = (float)Math.atan(f5 / 40F) * 40F;
        wolf.rotationPitch = -(float)Math.atan(f6 / 40F) * 20F;
        wolf.rotationYawHead = wolf.rotationYaw;
        GL11.glTranslatef(0.0F, wolf.yOffset, 0.0F);
        RenderManager.instance.playerViewY = 180F;
        RenderManager.instance.renderEntityWithPosYaw(wolf, -0.29999999999999999D, 0.80000000000000004D, 0.0D, 0.0F, 1.0F);
        wolf.renderYawOffset = f2;
        wolf.rotationYaw = f3;
        wolf.rotationPitch = f4;
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
    }
}
