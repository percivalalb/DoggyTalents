package doggytalents.client.screen.DogNewInfoScreen.element.view.StyleView.view.SkinView;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import doggytalents.client.DogTextureManager;
import doggytalents.client.screen.DogNewInfoScreen.element.AbstractElement;
import doggytalents.client.screen.DogNewInfoScreen.store.Store;
import doggytalents.client.screen.DogNewInfoScreen.store.slice.ActiveSkinSlice;
import doggytalents.common.entity.DogEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

public class DogSkinElement extends AbstractElement {

    DogEntity dog;
    List<ResourceLocation> locList;
    int activeSkinId;

    public DogSkinElement(AbstractElement parent, Screen screen, DogEntity dog, List<ResourceLocation> locList) {
        super(parent, screen);
        this.dog = dog;
        this.locList = locList;
    }

    @Override
    public AbstractElement init() {
        this.activeSkinId = 
            Store.get(getScreen()).getStateOrDefault(
                ActiveSkinSlice.class, ActiveSkinSlice.class, 
                new ActiveSkinSlice()).activeSkinId;
            
        return this;
    }

    @Override
    public void renderElement(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        int mX = this.getSizeX()/2;
        int mY = this.getSizeY()/2;
        int e_mX = this.getRealX() + mX;
        int e_mY = this.getRealY() + mY;

        {
            var oldHash = dog.getSkinHash();
            var manifestHash = DogTextureManager.INSTANCE.getTextureHash(this.locList.get(activeSkinId));
            dog.setSkinHash(manifestHash);
            InventoryScreen.renderEntityInInventory(e_mX, e_mY + 36, 64, 
            e_mX - mouseX, e_mY - mouseY, this.dog);
            dog.setSkinHash(oldHash);

            if (
                (oldHash.equals("") && this.activeSkinId == 0) 
                || manifestHash.equals(oldHash)
            ) {
                var font = Minecraft.getInstance().font;
                var c1 = Component.translatable("doggui.style.skins.selected");
                c1.setStyle(
                    Style.EMPTY
                        .withBold(true)
                        .withColor(0xff1fa800)   
                );
                
                var c1_len = font.width(c1);
                int tX = e_mX - c1_len/2;
                int tY = e_mY - font.lineHeight/2 + 75;
                
                font.draw(stack, c1, tX, tY, 0xffffffff);
            }
        }
        

        if (this.locList == null) return;
        if (this.locList.isEmpty()) return;
        int prevId = this.activeSkinId - 1;
        int nextId = this.activeSkinId + 1;

        if (nextId < locList.size()) {

            

            var oldHash = dog.getSkinHash();
            var manifestHash = DogTextureManager.INSTANCE.getTextureHash(this.locList.get(nextId));
            var oldSittingPose = dog.isInSittingPose();
            dog.setInSittingPose(false);
            dog.setSkinHash(manifestHash);
            
            InventoryScreen.renderEntityInInventory(e_mX + 32 + 25 + 25, e_mY + 32, 50, 
            -64, -64, this.dog);
            dog.setSkinHash(oldHash);
            dog.setInSittingPose(oldSittingPose);

            if (
                manifestHash.equals(oldHash)
            ) {
                var font = Minecraft.getInstance().font;
                var c1 = Component.literal("Selected");
                c1.setStyle(
                    Style.EMPTY
                        .withBold(true)
                        .withColor(0xff1fa800)   
                );
                
                var c1_len = font.width(c1);
                int tX = e_mX + 32 + 25 + 25 - c1_len/2;
                int tY = e_mY - font.lineHeight/2 + 50;
                
                font.draw(stack, c1, tX, tY, 0xffffffff);
            }
        }

        if (prevId >= 0) {
            var oldHash = dog.getSkinHash();
            var manifestHash = DogTextureManager.INSTANCE.getTextureHash(this.locList.get(prevId));
            var oldSittingPose = dog.isInSittingPose();
            dog.setInSittingPose(false);
            dog.setSkinHash(manifestHash);
            InventoryScreen.renderEntityInInventory(e_mX - 32 - 25 - 25, e_mY + 32, 50, 
            -64, -64, this.dog);
            dog.setSkinHash(oldHash);
            dog.setInSittingPose(oldSittingPose);

            if (
                (oldHash.equals("") && this.activeSkinId == 1) 
                || manifestHash.equals(oldHash)
            ) {
                var font = Minecraft.getInstance().font;
                var c1 = Component.literal("Selected");
                c1.setStyle(
                    Style.EMPTY
                        .withBold(true)
                        .withColor(0xff1fa800)   
                );
                
                var c1_len = font.width(c1);
                int tX = e_mX - 32 - 25 - 25 - c1_len/2;
                int tY = e_mY - font.lineHeight/2 + 50;
                
                font.draw(stack, c1, tX, tY, 0xffffffff);
            }
        }


         
    }
    
}
