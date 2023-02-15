package doggytalents.client.screen.DogNewInfoScreen.store.payload;

import doggytalents.client.DogTextureManager;
import doggytalents.client.screen.DogNewInfoScreen.store.payload.interfaces.InitSkinIndex;
import doggytalents.client.screen.DogNewInfoScreen.store.slice.ActiveSkinSlice;
import doggytalents.client.screen.DogNewInfoScreen.store.slice.ActiveTabSlice.Tab;
import doggytalents.common.entity.DogEntity;

public class InitSkinIndexPayload extends ChangeTabPayload implements InitSkinIndex {

    DogEntity dog;
    int index;

    public InitSkinIndexPayload(Tab tab, DogEntity dog) {
        super(tab);
        this.dog = dog;
        var textureRl = DogTextureManager.INSTANCE.getTexture(dog);
        index = Math.max(0, ActiveSkinSlice.locList.indexOf(textureRl)); 
    }

    @Override
    public ActiveSkinSlice getInitSkinIndex() {
        var ret = new ActiveSkinSlice();
        ret.activeSkinId = index;
        return ret;
    }
    
}
