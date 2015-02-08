package doggytalents.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import doggytalents.entity.EntityDTDoggy;
import doggytalents.entity.data.EnumTalents;
import doggytalents.inventory.ContainerPackPuppy;
import doggytalents.lib.ResourceReference;

public class GuiPackPuppy extends GuiContainer
{
    private float xSize_wolf;
    private float ySize_wolf;
    private EntityDTDoggy dog;
    private boolean mouseWasDown;

    public GuiPackPuppy(InventoryPlayer inventoryplayer, EntityDTDoggy entitydtdoggy) {
        super(new ContainerPackPuppy(inventoryplayer, entitydtdoggy));
        this.mouseWasDown = false;
        this.dog = entitydtdoggy;
        this.allowUserInput = false;
        this.ySize = ySize;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
    	String s = this.dog.inventory.hasCustomInventoryName() ? this.dog.inventory.getInventoryName() : StatCollector.translateToLocal(this.dog.inventory.getInventoryName());
        this.fontRendererObj.drawString(s, this.xSize / 2 - 10, 14, 4210752);
        this.fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 95 + 2, 4210752);
    }

    @Override
    public void drawScreen(int i, int j, float f) {
        super.drawScreen(i, j, f);
        this.xSize_wolf = i;
        this.ySize_wolf = j;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(ResourceReference.packPuppy);
        int l = (width - xSize) / 2;
        int i1 = (height - ySize) / 2;
        this.drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);

        for (int j1 = 0; j1 < 3; j1++)
            for (int k1 = 0; k1 < dog.talents.getTalentLevel(EnumTalents.PACKPUPPY); k1++)
            	this.drawTexturedModalRect(l + 78 + 18 * k1, i1 + 9 + 18 * j1 + 15, 197, 2, 18, 18);

        GuiInventory.func_147046_a(l + 42, i1 + 51, 30, (float)(l + 51) - xSize_wolf, (float)((i1 + 75) - 50) - ySize_wolf, this.dog);
    }
}
