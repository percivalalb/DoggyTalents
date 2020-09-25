package doggytalents.client.screen;

import java.util.Optional;

import com.mojang.blaze3d.systems.RenderSystem;

import doggytalents.DoggyAccessories;
import doggytalents.api.registry.AccessoryInstance;
import doggytalents.client.screen.widget.SmallButton;
import doggytalents.common.entity.accessory.DyeableAccessory.DyeableAccessoryInstance;
import doggytalents.common.inventory.container.DogInventoriesContainer;
import doggytalents.common.inventory.container.slot.DogInventorySlot;
import doggytalents.common.lib.Resources;
import doggytalents.common.network.PacketHandler;
import doggytalents.common.network.packet.data.DogInventoryPageData;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.network.PacketDistributor;

public class DogInventoriesScreen extends ContainerScreen<DogInventoriesContainer> {

    private Button left, right;

    public DogInventoriesScreen(DogInventoriesContainer packPuppy, PlayerInventory playerInventory, ITextComponent displayName) {
        super(packPuppy, playerInventory, displayName);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    public void init() {
        super.init();
        this.left = new SmallButton(this.guiLeft + this.xSize - 29, this.guiTop + 4, "<", (btn) -> {
            int page = this.getContainer().position.get();

            if (page > 0) {
                PacketHandler.send(PacketDistributor.SERVER.noArg(), new DogInventoryPageData(--page));
            }

            btn.active = page > 0;
            this.right.active = page < this.getContainer().possibleSlots - 9;
        });
        this.right = new SmallButton(this.guiLeft + this.xSize - 26 + 9, this.guiTop + 4, ">", (btn) -> {
            int page = this.getContainer().position.get();

            if (page < this.getContainer().possibleSlots - 9) {
                PacketHandler.send(PacketDistributor.SERVER.noArg(), new DogInventoryPageData(++page));
            }

            btn.active = page < this.getContainer().possibleSlots - 9;
            this.left.active = page > 0;

        });
        if (this.getContainer().possibleSlots > 9) {
            this.left.active = false;
            this.right.active = true;
        } else {
            this.left.visible = false;
            this.right.visible = false;
        }

        this.addButton(this.left);
        this.addButton(this.right);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        this.font.drawString(this.title.getFormattedText(), 8, 6, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int xMouse, int yMouse) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(Resources.DOG_INVENTORY);
        int l = (this.width - this.xSize) / 2;
        int i1 = (this.height - this.ySize) / 2;
        this.blit(l, i1, 0, 0, this.xSize, this.ySize);

        for (DogInventorySlot slot : this.getContainer().dogSlots) {
            if (!slot.isEnabled()) {
                continue;
            }

            Optional<AccessoryInstance> inst = slot.getDog().getAccessory(DoggyAccessories.DYEABLE_COLLAR.get());
            if (inst.isPresent()) {
                float[] color = inst.get().cast(DyeableAccessoryInstance.class).getFloatArray();
                RenderSystem.color3f(color[0], color[1], color[2]);
            } else {
                RenderSystem.color3f(1, 1, 1);
            }

            this.blit(l + slot.xPos - 1, i1 + slot.yPos - 1, 197, 2, 18, 18);
        }
//        for (int row = 0; row < 3; row++) {
//            for (int col = 0; col < MathHelper.clamp(this.getContainer().dogSlots.size() / 3D, 0, 9); col++) {
//               this.blit(l + 8 - 1 + 18 * col, i1 + 9 + 18 * row + 15, 197, 2, 18, 18);
//            }
//        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
       InputMappings.Input mouseKey = InputMappings.getInputByCode(keyCode, scanCode);
       if (this.minecraft.gameSettings.keyBindInventory.isActiveAndMatches(mouseKey)) {
           if (this.playerInventory.player.abilities.isCreativeMode) {
               this.minecraft.displayGuiScreen(new CreativeScreen(this.playerInventory.player));
           } else {
               this.minecraft.displayGuiScreen(new InventoryScreen(this.playerInventory.player));
           }
           return true;
       }

       return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    protected void renderHoveredToolTip(int mouseX, int mouseY) {
        if (this.minecraft.player.inventory.getItemStack().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.getHasStack()) {
//            if (this.hoveredSlot instanceof DogInventorySlot) {
//                renderTooltip(Arrays.asList(TextFormatting.RED + "Test"), mouseX, mouseY);
//            } else {
                this.renderTooltip(this.hoveredSlot.getStack(), mouseX, mouseY);
            //}
        }

    }
}
