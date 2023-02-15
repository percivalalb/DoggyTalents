package doggytalents.client.screen.DogNewInfoScreen.element.view.StyleView;

import com.mojang.blaze3d.vertex.PoseStack;

import doggytalents.client.screen.DogNewInfoScreen.element.AbstractElement;
import doggytalents.client.screen.DogNewInfoScreen.element.DivElement;
import doggytalents.client.screen.DogNewInfoScreen.element.ElementPosition.PosType;
import doggytalents.client.screen.DogNewInfoScreen.element.view.StyleView.view.AccessoryView.AccessoryView;
import doggytalents.client.screen.DogNewInfoScreen.element.view.StyleView.view.SkinView.SkinView;
import doggytalents.client.screen.DogNewInfoScreen.store.Store;
import doggytalents.client.screen.DogNewInfoScreen.store.slice.StyleViewPanelSlice;
import doggytalents.client.screen.DogNewInfoScreen.store.slice.StyleViewPanelSlice.StyleViewPanelTab;
import doggytalents.common.entity.DogEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

public class StyleView extends AbstractElement {

    DogEntity dog;

    public StyleView(AbstractElement parent, Screen screen, DogEntity dog) {
        super(parent, screen);
        this.dog = dog;
        
    }

    //TODO print no accessories when no accessories
    //TODO shrink the div box.
    @Override
    public AbstractElement init() {

        var tab = Store.get(getScreen())
            .getStateOrDefault(StyleViewPanelSlice.class, 
                StyleViewPanelTab.class, StyleViewPanelTab.ACCESSORIES);

        int sizeX = this.getSizeX();
        int sizeY = this.getSizeY();

        int mX = sizeX/2;
        int mY = sizeY/2;

        int styleViewBoxSizeX = sizeX > 507 ? 448 : sizeX;
        int styleViewBoxSizeY = sizeY > 337 ? 320 : sizeY;
        
        var styleViewBoxDiv = new DivElement(this, getScreen())
            .setPosition(PosType.ABSOLUTE, mX - styleViewBoxSizeX/2, 
            mY - styleViewBoxSizeY/2 + (sizeY > 337 ? 10 : 0)) //+10 if detached to center it.
            .setSize(styleViewBoxSizeX, styleViewBoxSizeY);
            //.setBackgroundColor(0xffff05de);
        this.addChildren(styleViewBoxDiv);

        var styleListDiv = new StyleTabListPanel(styleViewBoxDiv, getScreen())
            .setPosition(PosType.RELATIVE, 0, 0)
            .setSize(120, 1f)
            .setBackgroundColor(0x87363636)
            .init();

        styleViewBoxDiv.addChildren(styleListDiv);

        AbstractElement rightView;
        switch (tab) {
            case SKINS:
                rightView = new SkinView(styleViewBoxDiv, getScreen(), dog);
                break;
            default:
                rightView = new AccessoryView(styleViewBoxDiv, getScreen(), dog);
                break;
        }
        
        rightView
            .setPosition(PosType.RELATIVE, 0, 0)
            .setSize(styleViewBoxDiv.getSizeX() - 120, 1f)
            .setBackgroundColor(0x57595858)
            .init();
        styleViewBoxDiv.addChildren(rightView);
        
        // var InventoryAccessoryList = 
        //     new InventoryAccessoryListElement(
        //         styleViewBoxDiv, getScreen(),
        //         Minecraft.getInstance().player, dog
        //     );
        // InventoryAccessoryList
        //     .setPosition(PosType.ABSOLUTE, 0, 0)
        //     .setSize(100, 40)
        //     .setBackgroundColor(0xffff05de)
        //     .init();

        // styleViewBoxDiv.addChildren(InventoryAccessoryList);

        

        return this;
    }

    @Override
    public void renderElement(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        // var font = getScreen().getMinecraft().font;
        // font.draw(stack, "style", this.getRealX()+3, this.getRealY() + 40, 0xffffffff);
        
        
    }
    
}
