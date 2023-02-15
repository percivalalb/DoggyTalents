package doggytalents.client.screen.DogNewInfoScreen.store.slice;

import java.util.List;

import doggytalents.client.DogTextureManager;
import doggytalents.client.screen.DogNewInfoScreen.store.UIAction;
import doggytalents.client.screen.DogNewInfoScreen.store.UIActionTypes;
import doggytalents.client.screen.DogNewInfoScreen.store.payload.InitSkinIndexPayload;
import doggytalents.client.screen.DogNewInfoScreen.store.payload.interfaces.TabChange;
import doggytalents.client.screen.DogNewInfoScreen.store.slice.ActiveTabSlice.Tab;
import net.minecraft.resources.ResourceLocation;

public class ActiveSkinSlice implements AbstractSlice {

    public static List<ResourceLocation> locList;

    public int activeSkinId;

    @Override
    public Object getInitalState() {
        var ret = new ActiveSkinSlice();
        ret.activeSkinId = 0;
        return ret;
    }

    @Override
    public Object reducer(Object oldData, UIAction action) {
        if (locList == null) return oldData;
        if (locList.isEmpty()) return oldData;
        if (oldData instanceof ActiveSkinSlice oldActiveSkin) {
            if (action.type == UIActionTypes.Skins.ACTIVE_INC) {
                var ret = new ActiveSkinSlice();
                    ret.activeSkinId = oldActiveSkin.activeSkinId;
                    ++ret.activeSkinId;
                    if (ret.activeSkinId >= locList.size()) {
                        ret.activeSkinId = locList.size() - 1;
                    }
                    return ret;
            } else if (action.type == UIActionTypes.Skins.ACTIVE_DEC) {
                var ret = new ActiveSkinSlice();
                    ret.activeSkinId = oldActiveSkin.activeSkinId;
                    --ret.activeSkinId;
                    if (ret.activeSkinId < 0) {
                        ret.activeSkinId = 0;
                    }
                    return ret;  
            } else if (action.type == UIActionTypes.CHANGE_TAB) {
                if (action.payload instanceof InitSkinIndexPayload initSkin && initSkin.getTab() == Tab.STYLE) {
                    return initSkin.getInitSkinIndex();
                }
            }
        }
        return oldData;
    }

    public static void initLocList() {
        locList = DogTextureManager.INSTANCE.getAll();
    }
    
}
