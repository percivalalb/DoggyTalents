package doggytalents.client.screen.DogNewInfoScreen.element.view.StyleView.view.SkinView;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import doggytalents.client.DogTextureManager;
import doggytalents.client.screen.DogNewInfoScreen.element.AbstractElement;
import doggytalents.client.screen.DogNewInfoScreen.element.ElementPosition.ChildDirection;
import doggytalents.client.screen.DogNewInfoScreen.element.ElementPosition.PosType;
import doggytalents.common.entity.DogEntity;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;

public class SkinView extends AbstractElement {

    DogEntity dog;
    List<ResourceLocation> textureList;
    
    public SkinView(AbstractElement parent, Screen screen, DogEntity dog) {
        super(parent, screen);
        this.dog = dog;
    }

    @Override
    public AbstractElement init() {
        this.textureList = DogTextureManager.INSTANCE.getAll();
        this.getPosition().setChildDirection(ChildDirection.COL);

        var dogSkinPreview = new DogSkinElement(this, getScreen(), this.dog, textureList);
        dogSkinPreview
            .setPosition(PosType.RELATIVE, 0, 0)
            .setSize(1f, 0.75f)
            .setBackgroundColor(0x87363636)
            .init();
        this.addChildren(dogSkinPreview);
        
        var skinSelect = new SkinButtonElement(this, getScreen(), this.dog, textureList);
        skinSelect
            .setPosition(PosType.RELATIVE, 0, 0)
            .setSize(1f, 0.25f)
            .setBackgroundColor(0xAA595858)
            .init();
        this.addChildren(skinSelect);
        return this;
    }

    @Override
    public void renderElement(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        // int pX = this.getRealX() + 40;
        // int pY = this.getRealY() + 40;
        // InventoryScreen.renderEntityInInventory(pX, pY + 32, 50, 
        // pX - mouseX, pY - mouseY, this.dog);
        // var oldHash = dog.getSkinHash();
        // var manifestHash = DogTextureManager.INSTANCE.getTextureHash(this.textureList.get(2));
        // dog.setSkinHash(manifestHash);
        // pX += 60;
        // InventoryScreen.renderEntityInInventory(pX, pY + 32, 50, 
        // pX - mouseX, pY - mouseY, this.dog);
        // dog.setSkinHash(oldHash);
    }
    
}
