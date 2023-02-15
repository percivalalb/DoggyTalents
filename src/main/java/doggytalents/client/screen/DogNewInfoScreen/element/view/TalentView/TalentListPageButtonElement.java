package doggytalents.client.screen.DogNewInfoScreen.element.view.TalentView;

import com.mojang.blaze3d.vertex.PoseStack;

import doggytalents.client.screen.DogNewInfoScreen.element.AbstractElement;
import doggytalents.client.screen.DogNewInfoScreen.store.Store;
import doggytalents.client.screen.DogNewInfoScreen.store.UIAction;
import doggytalents.client.screen.DogNewInfoScreen.store.UIActionTypes;
import doggytalents.client.screen.DogNewInfoScreen.store.slice.TalentListPageCounterSlice;
import doggytalents.client.screen.DogNewInfoScreen.widget.TextOnlyButton;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class TalentListPageButtonElement extends AbstractElement {

    static final int BUTTON_SIZE = 20;
    static final int BUTTON_MIDDLE_SPACING = 40;

    int curPage;
    int maxPage;

    Font font;

    public TalentListPageButtonElement(AbstractElement parent, Screen screen,
        int curPage, int maxPage) {
        super(parent, screen);
        this.curPage = curPage;
        this.maxPage = maxPage;
        var mc = this.getScreen().getMinecraft();
        this.font = mc.font;
    }

    @Override
    public AbstractElement init() {
        int mX = this.getSizeX()/2;
        int mY = this.getSizeY()/2;
        var nextButton = new TextOnlyButton(0, 0, BUTTON_SIZE, BUTTON_SIZE, 
            Component.literal(">"), b -> {
                Store.get(getScreen()).dispatch(TalentListPageCounterSlice.class, 
                new UIAction(UIActionTypes.Talents.LIST_INC, null));
            }, font
        ); 
        nextButton.active = curPage < maxPage;
        var backButton = new TextOnlyButton(0, 0, BUTTON_SIZE, BUTTON_SIZE, 
            Component.literal("<"), b -> {
                Store.get(getScreen()).dispatch(TalentListPageCounterSlice.class, 
                new UIAction(UIActionTypes.Talents.LIST_DEC, null));
            }, font
        ); 
        backButton.active = 1 < curPage;
        
        int aX = mX - BUTTON_MIDDLE_SPACING/2 - backButton.getWidth();
        int aY = mY - backButton.getHeight()/2;
        backButton.setX(this.getRealX() + aX);
        backButton.setY(this.getRealY() + aY);
        aX = mX + BUTTON_MIDDLE_SPACING/2;
        nextButton.setX(this.getRealX() + aX);
        nextButton.setY(this.getRealY() + aY);
        this.addChildren(backButton);
        this.addChildren(nextButton);
        

        return this;
    }

    @Override
    public void renderElement(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        int mX = this.getSizeX()/2;
        int mY = this.getSizeY()/2;
        var c0 = Component.literal(this.curPage + "/" + this.maxPage);
        int tX = this.getRealX() + mX - font.width(c0)/2;
        int tY = this.getRealY() + mY - font.lineHeight/2;
        font.draw(stack, c0, tX, tY, 0xffffffff);
        
    }
    
}
