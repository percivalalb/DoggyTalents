package doggytalents.client.screen.DogNewInfoScreen.store.slice;

import doggytalents.client.screen.DogNewInfoScreen.store.UIAction;

public abstract interface AbstractSlice {
    
    public Object getInitalState();

    public Object reducer(Object oldData, UIAction action);

}
