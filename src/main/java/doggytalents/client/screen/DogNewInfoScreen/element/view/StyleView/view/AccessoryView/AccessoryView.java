package doggytalents.client.screen.DogNewInfoScreen.element.view.StyleView.view.AccessoryView;

import com.mojang.blaze3d.vertex.PoseStack;

import doggytalents.client.screen.DogNewInfoScreen.element.AbstractElement;
import doggytalents.client.screen.DogNewInfoScreen.element.ElementPosition.ChildDirection;
import doggytalents.client.screen.DogNewInfoScreen.element.ElementPosition.PosType;
import doggytalents.common.entity.DogEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

public class AccessoryView extends AbstractElement {

    DogEntity dog;

    public AccessoryView(AbstractElement parent, Screen screen, DogEntity dog) {
        super(parent, screen);
        this.dog = dog;
        //TODO Auto-generated constructor stub
    }

    @Override
    public AbstractElement init() {
        
        this.getPosition().setChildDirection(ChildDirection.COL);

        var dogAccessoriesEdit = new DogAccessoriesElement(this, getScreen(), this.dog);
        dogAccessoriesEdit
            .setPosition(PosType.RELATIVE, 0, 0)
            .setSize(1f, 0.65f)
            .setBackgroundColor(0x87363636)
            .init();
        this.addChildren(dogAccessoriesEdit);
        
        var accessoryEdit = new AccessoryEditElement(this, getScreen(), Minecraft.getInstance().player, this.dog);
        accessoryEdit
            .setPosition(PosType.RELATIVE, 0, 0)
            .setSize(1f, 0.35f)
            .setBackgroundColor(0xAA595858)
            .init();
        this.addChildren(accessoryEdit);


        return this;
    }

    @Override
    public void renderElement(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        // TODO Auto-generated method stub
        
    }
    
}
