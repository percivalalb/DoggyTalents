package doggytalents.client.screen.DogNewInfoScreen.element.view.TalentView;

import com.mojang.blaze3d.vertex.PoseStack;

import doggytalents.client.screen.DogNewInfoScreen.element.AbstractElement;
import doggytalents.client.screen.DogNewInfoScreen.element.DivElement;
import doggytalents.client.screen.DogNewInfoScreen.element.ElementPosition.PosType;
import doggytalents.common.entity.DogEntity;
import net.minecraft.client.gui.screens.Screen;

public class TalentView extends AbstractElement {

    DogEntity dog;

    public TalentView(AbstractElement parent, Screen screen, DogEntity dog) {
        super(parent, screen);
        this.dog = dog;
    }

    @Override
    public AbstractElement init() {

        int sizeX = this.getSizeX();
        int sizeY = this.getSizeY();

        int mX = sizeX/2;
        int mY = sizeY/2;

        int talentViewBoxSizeX = sizeX > 600 ? 560 : sizeX;
        int talentViewBoxSizeY = sizeY > 440 ? 400 : sizeY;
        
        var talentViewBoxDiv = new DivElement(this, getScreen())
            .setPosition(PosType.ABSOLUTE, mX - talentViewBoxSizeX/2,  
            mY - talentViewBoxSizeY/2 + (sizeY > 440 ? 10 : 0)) //+10 if detached to center it.
            .setSize(talentViewBoxSizeX, talentViewBoxSizeY);
            //.setBackgroundColor(0xffff05de);
        this.addChildren(talentViewBoxDiv);

        var talentListDiv = new TalentListPanel(talentViewBoxDiv, getScreen(), dog)
            .setPosition(PosType.RELATIVE, 0, 0)
            .setSize(120, 1f)
            .setBackgroundColor(0x87363636)
            .init();

        talentViewBoxDiv.addChildren(talentListDiv);
        // var talentLisEntriesDiv = new DivElement(talentListDiv, getScreen())
        //     .setPosition(PosType.ABSOLUTE, 0, 0)
        //     .setSize(1f, talentListDiv.getSizeY() - 30)
        //     .setBackgroundColor(0xffcfa73c);
        // talentListDiv.addChildren(talentLisEntriesDiv);
        // talentLisEntriesDiv.addChildren(
        //     new TalentButtonEntryElement(talentLisEntriesDiv, getScreen(), dog, 0)
        //     .setPosition(PosType.ABSOLUTE, 0, 0)
        //     .setSize(1f, 1f)
        //     .init()
        // );


        var talentInfoDiv = new TalentInfoViewElement(talentViewBoxDiv, getScreen(), dog)
            .setPosition(PosType.RELATIVE, 
            0, 0)
            .setSize(talentViewBoxSizeX - 120, 1f)
            .setBackgroundColor(0x57595858)
            .init();
        talentViewBoxDiv.addChildren(talentInfoDiv);
        // var talentInfoCenterDiv = new DivElement(talentInfoDiv, getScreen())
        //     .setPosition(
        //         PosType.ABSOLUTE, 
        //         talentInfoDiv.getSizeX()/2-50, talentInfoDiv.getSizeY()/2-50
        //     )
        //     .setSize(100 , 100)
        //     .setBackgroundColor(0xffcfa73c);
        // talentInfoDiv.addChildren(talentInfoCenterDiv);

        return this;
    }

    @Override
    public void renderElement(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        // var font = getScreen().getMinecraft().font;
        // font.draw(stack, "talents", this.getRealX()+3, this.getRealY() + 40, 0xffffffff);
        
    }
    
}
