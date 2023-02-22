package doggytalents.client.screen.DogNewInfoScreen.element.view.StyleView.view.AccessoryView;

import java.util.ArrayList;

import com.mojang.blaze3d.vertex.PoseStack;

import doggytalents.client.screen.DogNewInfoScreen.element.AbstractElement;
import doggytalents.client.screen.DogNewInfoScreen.element.DivElement;
import doggytalents.client.screen.DogNewInfoScreen.element.ElementPosition.ChildDirection;
import doggytalents.client.screen.DogNewInfoScreen.element.ElementPosition.PosType;
import doggytalents.client.screen.DogNewInfoScreen.element.view.MainInfoView.DogStatusViewBoxElement;
import doggytalents.client.screen.DogNewInfoScreen.element.view.StyleView.widget.AccessoryHolder;
import doggytalents.common.entity.DogEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.item.ItemStack;

public class DogAccessoriesElement extends AbstractElement {

    private static final int BUTTON_SPACING = 4;

    DogEntity dog;
    Minecraft mc;
    final ArrayList<AccessoryHolder> accessoryHolders = new ArrayList<AccessoryHolder>(5);

    //Divs
    DivElement dogDiv;
    DivElement accessoriesDiv;

    public DogAccessoriesElement(AbstractElement parent, Screen screen, DogEntity dog) {
        super(parent, screen);
        this.dog = dog;
        mc = Minecraft.getInstance();
    }

    @Override
    public AbstractElement init() {

        dogDiv = new DivElement(this, getScreen());
        accessoriesDiv = new DivElement(this, getScreen());

        dogDiv
            .setPosition(PosType.RELATIVE, 0, 0)
            .setSize(0.5f, 1f);
            //.setBackgroundColor(0xff53b06c);
        this.addChildren(dogDiv);

        accessoriesDiv
            .setPosition(PosType.RELATIVE, 0, 0)
            .setSize(0.5f, 1f);
            //.setBackgroundColor(0x45680be0);

        //accessoriesDiv
        {
            int mX = accessoriesDiv.getSizeX()/2;
            int mY = accessoriesDiv.getSizeY()/2;

            for (int i = 0; i < 5; ++i) {
                var accessoryHolder = new AccessoryHolder(
                    0, 0,
                    this.mc.getItemRenderer(), this.dog, false);
                this.accessoryHolders.add(accessoryHolder);
            }

            int accessoryHolderTotalsSize = 5 * AccessoryHolder.WIDGET_SIZE 
                + 4 * BUTTON_SPACING;
            int startX = accessoriesDiv.getRealX() + mX - AccessoryHolder.WIDGET_SIZE/2;
            int pY = accessoriesDiv.getRealY() + mY - accessoryHolderTotalsSize/2;
            for (var holder : this.accessoryHolders) {
                holder.setX(startX);
                holder.setY(pY);
                accessoriesDiv.addChildren(holder);
                pY += AccessoryHolder.WIDGET_SIZE + BUTTON_SPACING;
            }
        }
        
        this.addChildren(accessoriesDiv);

        

        return this;
    }

    @Override
    public void renderElement(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        //dog div
        {
            int mX = dogDiv.getSizeX()/2;
            int mY = dogDiv.getSizeY()/2;
            int e_mX = dogDiv.getRealX() + mX;
            int e_mY = dogDiv.getRealY() + mY;

            DogStatusViewBoxElement.renderDogInside(stack, this.dog, e_mX, e_mY + 32, 50, 
            e_mX - mouseX, e_mY - mouseY);
        }

        //accessory div
        {
            int holderIndx = 0;
            var accessories = this.dog.getAccessories();
            for (int i = 0; i < accessories.size(); ++i) {
                var accessory = accessories.get(i); 
                if (holderIndx >= this.accessoryHolders.size()) break;
                var item = accessory.getReturnItem();
                if (item != null) {
                    var holder = this.accessoryHolders.get(holderIndx);
                    holder.setStack(item);
                    holder.setInventorySlotId(i);
                    ++holderIndx;
                }
            }
            while (holderIndx < this.accessoryHolders.size()) {
                this.accessoryHolders.get(holderIndx).setStack(ItemStack.EMPTY);
                ++holderIndx;
            }
        }
        
    }
    
}
