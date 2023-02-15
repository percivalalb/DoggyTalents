package doggytalents.client.screen.DogNewInfoScreen.store.slice;

import doggytalents.client.screen.DogNewInfoScreen.store.UIAction;

public class StyleViewPanelSlice implements AbstractSlice {

    @Override
    public Object getInitalState() {
        // TODO Auto-generated method stub
        return StyleViewPanelTab.ACCESSORIES;
    }

    @Override
    public Object reducer(Object oldData, UIAction action) {
        if (action.payload instanceof StyleViewPanelTab) {
            return action.payload;
        }
        return oldData;
    }

    public static enum StyleViewPanelTab {
        ACCESSORIES("accessories"), SKINS("skins");

        public final String unLocalizedTitle;

        StyleViewPanelTab(String title) {
            this.unLocalizedTitle = "doggui.style." +  title;
        }
    }
    
}
