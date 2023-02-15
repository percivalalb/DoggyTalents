package doggytalents.client.screen.DogNewInfoScreen.element.view.StyleView.view.SkinView;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import doggytalents.client.DogTextureManager;
import doggytalents.client.screen.DogNewInfoScreen.element.AbstractElement;
import doggytalents.client.screen.DogNewInfoScreen.store.Store;
import doggytalents.client.screen.DogNewInfoScreen.store.UIAction;
import doggytalents.client.screen.DogNewInfoScreen.store.UIActionTypes;
import doggytalents.client.screen.DogNewInfoScreen.store.slice.ActiveSkinSlice;
import doggytalents.client.screen.DogNewInfoScreen.widget.TextOnlyButton;
import doggytalents.client.screen.widget.CustomButton;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.network.PacketHandler;
import doggytalents.common.network.packet.data.DogTextureData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.PacketDistributor;

public class SkinButtonElement extends AbstractElement {

    DogEntity dog;
    Font font;
    List<ResourceLocation> locList;
    Button applyButton;
    int activeSkinId;

    public SkinButtonElement(AbstractElement parent, Screen screen, DogEntity dog, List<ResourceLocation> locList) {
        super(parent, screen);
        this.dog = dog;
        var mc = Minecraft.getInstance();
        this.font = mc.font;
        this.locList = locList;
    }

    @Override
    public AbstractElement init() {

        activeSkinId = Store.get(getScreen())
            .getStateOrDefault(ActiveSkinSlice.class,
            ActiveSkinSlice.class, new ActiveSkinSlice())
            .activeSkinId;
        
        
        int mX = this.getSizeX()/2;
        int mY = this.getSizeY()/2;
        
        if (locList == null) return this;
        if (locList.isEmpty()) return this;
        var prevSkinButton = new TextOnlyButton(
            this.getRealX() + 10, this.getRealY() + mY - 9,
            18, 18, Component.literal("<"), 
            b -> {
                Store.get(getScreen()).dispatch(ActiveSkinSlice.class, 
                    new UIAction(
                        UIActionTypes.Skins.ACTIVE_DEC,
                        new ActiveSkinSlice()
                    ) 
                );
            }, this.font);
        prevSkinButton.active = activeSkinId > 0;
        
        

        var nextSkinButton = new TextOnlyButton(
            this.getRealX() + 75, this.getRealY() + mY - 9,
            18, 18, Component.literal(">"), 
            b -> {
                Store.get(getScreen()).dispatch(ActiveSkinSlice.class, 
                    new UIAction(
                        UIActionTypes.Skins.ACTIVE_INC,
                        
                        new ActiveSkinSlice()
                    ) 
                );
            }, this.font);
            nextSkinButton.active = activeSkinId < this.locList.size() - 1;

        applyButton = new CustomButton(
            this.getRealX() + this.getSizeX() - 30 - 30,
            this.getRealY() + mY - 10, 40, 20,
            Component.literal("Apply"),
            b -> {
                applyAndRequestSkinChange(activeSkinId);
            }  
        );
        applyButton.active = !(
            (activeSkinId == 0 && dog.getSkinHash().equals("")) 
            || DogTextureManager.INSTANCE
            .getTextureHash((locList.get(activeSkinId))).equals(
                dog.getSkinHash()
            )
       );

        this.addChildren(prevSkinButton);
        this.addChildren(nextSkinButton);
        this.addChildren(applyButton);
        
        return this;
    }

    @Override
    public void renderElement(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        
        int tX = this.getRealX() + 38;
        int tY = this.getRealY() + this.getSizeY()/2 - font.lineHeight/2;
        if (this.activeSkinId+1 < 10) tX += 2;  
        String str = (this.activeSkinId+1) + "/" + this.locList.size();
        this.font.draw(stack, str, tX, tY, 0xffffffff);
        
    }

    public void applyAndRequestSkinChange(int id) {
        int size = locList.size();
        if (id >= size || id < 0) return;
        String requestHash = null;
        if (id == 0) requestHash = "";
        else {
            requestHash = DogTextureManager.INSTANCE.getTextureHash((locList.get(id)));
        }

        applyButton.active = false;

        PacketHandler.send(PacketDistributor.SERVER.noArg(),
            new DogTextureData(this.dog.getId(), requestHash));

    }
    
}
