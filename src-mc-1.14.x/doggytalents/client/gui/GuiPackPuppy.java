package doggytalents.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;

import doggytalents.ModTalents;
import doggytalents.entity.EntityDog;
import doggytalents.inventory.ContainerPackPuppy;
import doggytalents.inventory.InventoryPackPuppy;
import doggytalents.lib.ResourceLib;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author ProPercivalalb
 */
@OnlyIn(Dist.CLIENT)
public class GuiPackPuppy extends ContainerScreen<ContainerPackPuppy> {
	
    private EntityDog dog;
    private InventoryPackPuppy inventory;

    public GuiPackPuppy(int windowId, PlayerEntity player, EntityDog dog) {
        super(new ContainerPackPuppy(windowId, player, dog), player.inventory, ((InventoryPackPuppy)dog.objects.get("packpuppyinventory")).getDisplayName());
        this.dog = dog;
        this.inventory = (InventoryPackPuppy)this.dog.objects.get("packpuppyinventory");
        //TODO this.field_147002_h.allowUserInput = false;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
    	this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
    	String s = this.inventory.getDisplayName().getUnformattedComponentText();
        this.font.drawString(s, this.xSize / 2 - 10, 14, 4210752);
        this.font.drawString(I18n.format("container.inventory"), 8, this.ySize - 95 + 2, 4210752);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int xMouse, int yMouse) {
    	GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(ResourceLib.GUI_PACK_PUPPY);
        int l = (this.width - this.xSize) / 2;
        int i1 = (this.height - this.ySize) / 2;
        this.blit(l, i1, 0, 0, this.xSize, this.ySize);

        for (int j1 = 0; j1 < 3; j1++)
        	for (int k1 = 0; k1 < MathHelper.clamp(this.dog.TALENTS.getLevel(ModTalents.PACK_PUPPY), 0, 5); k1++)
        		this.blit(l + 78 + 18 * k1, i1 + 9 + 18 * j1 + 15, 197, 2, 18, 18);

        InventoryScreen.drawEntityOnScreen(l + 42, i1 + 51, 30, (float)(l + 51) - xMouse, (float)((i1 + 75) - 50) - yMouse, this.dog);
    }
}
