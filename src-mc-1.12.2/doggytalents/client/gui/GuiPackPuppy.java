package doggytalents.client.gui;

import doggytalents.ModTalents;
import doggytalents.entity.EntityDog;
import doggytalents.inventory.ContainerPackPuppy;
import doggytalents.inventory.InventoryPackPuppy;
import doggytalents.lib.ResourceLib;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiPackPuppy extends GuiContainer {
    
    private EntityDog dog;
    private InventoryPackPuppy inventory;

    public GuiPackPuppy(EntityPlayer player, EntityDog dog) {
        super(new ContainerPackPuppy(player, dog));
        this.dog = dog;
        this.inventory = (InventoryPackPuppy)this.dog.objects.get("packpuppyinventory");
        this.allowUserInput = false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = this.inventory.getDisplayName().getUnformattedText();
        this.fontRenderer.drawString(s, this.xSize / 2 - 10, 14, 4210752);
        this.fontRenderer.drawString(I18n.format("container.inventory"), 8, this.ySize - 95 + 2, 4210752);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(ResourceLib.GUI_PACK_PUPPY);
        int l = (this.width - this.xSize) / 2;
        int i1 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(l, i1, 0, 0, this.xSize, this.ySize);

        for (int j1 = 0; j1 < 3; j1++)
            for (int k1 = 0; k1 < MathHelper.clamp(this.dog.TALENTS.getLevel(ModTalents.PACK_PUPPY), 0, 5); k1++)
                this.drawTexturedModalRect(l + 78 + 18 * k1, i1 + 9 + 18 * j1 + 15, 197, 2, 18, 18);

        GuiInventory.drawEntityOnScreen(l + 42, i1 + 51, 30, (float)(l + 51) - mouseX, (float)((i1 + 75) - 50) - mouseY, this.dog);
    }
}
