package doggytalents.client.screen;

import java.util.Optional;

import com.mojang.blaze3d.matrix.MatrixStack;
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
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.PacketDistributor;

public class DogInventoriesScreen extends ContainerScreen<DogInventoriesContainer> {

    private Button left, right;

    public DogInventoriesScreen(DogInventoriesContainer packPuppy, PlayerInventory playerInventory, ITextComponent displayName) {
        super(packPuppy, playerInventory, displayName);
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        this.renderTooltip(stack, mouseX, mouseY);
    }

    @Override
    public void init() {
        super.init();
        this.left = new SmallButton(this.leftPos + this.imageWidth - 29, this.topPos + 4, new StringTextComponent("<"), (btn) -> {
            int page = this.getMenu().getPage();

            if (page > 0) {
                PacketHandler.send(PacketDistributor.SERVER.noArg(), new DogInventoryPageData(--page));
            }

            btn.active = page > 0;
            this.right.active = page < this.getMenu().getTotalNumColumns() - 9;
        });
        this.right = new SmallButton(this.leftPos + this.imageWidth - 26 + 9, this.topPos + 4, new StringTextComponent(">"), (btn) -> {
            int page = this.getMenu().getPage();

            if (page < this.getMenu().getTotalNumColumns() - 9) {
                PacketHandler.send(PacketDistributor.SERVER.noArg(), new DogInventoryPageData(++page));
            }

            btn.active = page < this.getMenu().getTotalNumColumns() - 9;
            this.left.active = page > 0;

        });
        if (this.getMenu().getTotalNumColumns() > 9) {
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
    protected void renderLabels(MatrixStack stack, int par1, int par2) {
        this.font.draw(stack, this.title.getString(), 8, 6, 4210752);
        this.font.draw(stack, this.inventory.getDisplayName().getString(), 8.0F, this.imageHeight - 96 + 2, 4210752);
    }

    @Override
    protected void renderBg(MatrixStack stack, float partialTicks, int xMouse, int yMouse) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(Resources.DOG_INVENTORY);
        int l = (this.width - this.imageWidth) / 2;
        int i1 = (this.height - this.imageHeight) / 2;
        this.blit(stack, l, i1, 0, 0, this.imageWidth, this.imageHeight);

        for (DogInventorySlot slot : this.getMenu().getSlots()) {
            if (!slot.isActive()) {
                continue;
            }

            Optional<AccessoryInstance> inst = slot.getDog().getAccessory(DoggyAccessories.DYEABLE_COLLAR.get());
            if (inst.isPresent()) {
                float[] color = inst.get().cast(DyeableAccessoryInstance.class).getFloatArray();
                RenderSystem.color3f(color[0], color[1], color[2]);
            } else {
                RenderSystem.color3f(1, 1, 1);
            }

            this.blit(stack, l + slot.x - 1, i1 + slot.y - 1, 197, 2, 18, 18);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
       InputMappings.Input mouseKey = InputMappings.getKey(keyCode, scanCode);
       if (this.minecraft.options.keyInventory.isActiveAndMatches(mouseKey)) {
           if (this.inventory.player.abilities.instabuild) {
               this.minecraft.setScreen(new CreativeScreen(this.inventory.player));
           } else {
               this.minecraft.setScreen(new InventoryScreen(this.inventory.player));
           }
           return true;
       }

       return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    protected void renderTooltip(MatrixStack stack, int mouseX, int mouseY) {
        if (this.minecraft.player.inventory.getCarried().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
//            if (this.hoveredSlot instanceof DogInventorySlot) {
//                this.renderTooltip(Arrays.asList(new TranslationTextComponent("test").applyTextStyle(TextFormatting.RED).getFormattedText()), mouseX, mouseY);
//            } else {
                this.renderTooltip(stack, this.hoveredSlot.getItem(), mouseX, mouseY);
//            }
        }

    }
}
