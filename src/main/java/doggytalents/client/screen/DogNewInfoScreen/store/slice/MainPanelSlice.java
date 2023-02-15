package doggytalents.client.screen.DogNewInfoScreen.store.slice;

import doggytalents.client.screen.DogNewInfoScreen.store.UIAction;
import doggytalents.client.screen.DogNewInfoScreen.store.UIActionTypes;
import doggytalents.client.screen.DogNewInfoScreen.store.payload.ChangeTabPayload;
import doggytalents.client.screen.DogNewInfoScreen.store.slice.ActiveTabSlice.Tab;

public class MainPanelSlice implements AbstractSlice {

    @Override
    public Object getInitalState() {
        // TODO Auto-generated method stub
        return MainTab.MAIN;
    }

    @Override
    public Object reducer(Object oldData, UIAction action) {
        if (action.type == UIActionTypes.CHANGE_TAB) {
            if (action.payload instanceof ChangeTabPayload tabPayload) {
                if (tabPayload.getTab() == Tab.HOME) {
                    return MainTab.MAIN;
                }
            } 
        }
        if (action.payload instanceof MainTab) {
            return action.payload;
        }
        return oldData;
    }

    public static enum MainTab {
        MAIN("doggui.common.go_back"), EDIT_INFO("doggui.home.edit_info");

        public final String unLocalisedTitle;

        MainTab(String title) {
            this.unLocalisedTitle = title;
        }
    }
    
}
