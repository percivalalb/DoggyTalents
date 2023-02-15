package doggytalents.client.screen.DogNewInfoScreen.element.view.StatsView;

import java.util.ArrayList;

import com.mojang.blaze3d.vertex.PoseStack;

import doggytalents.client.screen.DogNewInfoScreen.element.AbstractElement;
import doggytalents.client.screen.DogNewInfoScreen.store.Store;
import doggytalents.client.screen.DogNewInfoScreen.store.slice.StatsViewPanelSlice;
import doggytalents.client.screen.DogNewInfoScreen.store.slice.StyleViewPanelSlice;
import doggytalents.client.screen.DogNewInfoScreen.store.slice.StatsViewPanelSlice.StatsViewPanelTab;
import doggytalents.client.screen.DogNewInfoScreen.store.slice.StyleViewPanelSlice.StyleViewPanelTab;
import doggytalents.client.screen.DogNewInfoScreen.widget.TabPanelButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class StatsViewEntryElement extends AbstractElement {
    static final int BUTTON_HEIGHT = 20;
    static final int BUTTON_SPACING = 2;
    static final int PADDING_TOP = 0;
    static final int PADDING_LEFT = 0;

    public StatsViewEntryElement(AbstractElement parent, Screen screen) {
        super(parent, screen);
    }

    @Override
    public AbstractElement init() {
        var activeTab =
            Store.get(getScreen()).getStateOrDefault(
                StatsViewPanelSlice.class, 
                StatsViewPanelTab.class,
                StatsViewPanelTab.GENERAL
            );
        var tabs = StatsViewPanelTab.values();
        var tabButtons = new ArrayList<TabPanelButton>();
        for (var tab : tabs) {
            tabButtons.add(
                new TabPanelButton(
                    0, 0, getSizeX(), 
                    BUTTON_HEIGHT, getScreen(),
                    activeTab == tab, 
                    Component.translatable(tab.unLocalizedTitle), 
                    StatsViewPanelSlice.class, 
                    tab
                )
            );
            // --buttonsUntilFull;
            // if (buttonsUntilFull <= 0) break;
        }
        int startX = this.getRealX() + PADDING_LEFT;
        int pY = this.getRealY() + PADDING_TOP;
        for (var b : tabButtons) {
            b.setX(startX);
            b.setY(pY);
            pY += b.getHeight() + BUTTON_SPACING;
            this.addChildren(b);
        }

        return this;
    }

    @Override
    public void renderElement(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        
    }
}
