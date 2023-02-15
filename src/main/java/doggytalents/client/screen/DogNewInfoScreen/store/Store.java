package doggytalents.client.screen.DogNewInfoScreen.store;

import java.util.Map;

import com.google.common.collect.Maps;

import doggytalents.client.screen.DogNewInfoScreen.store.slice.AbstractSlice;
import doggytalents.client.screen.DogNewInfoScreen.store.slice.ActiveSkinSlice;
import doggytalents.client.screen.DogNewInfoScreen.store.slice.ActiveTabSlice;
import doggytalents.client.screen.DogNewInfoScreen.store.slice.ActiveTalentDescSlice;
import doggytalents.client.screen.DogNewInfoScreen.store.slice.CleanableSlice;
import doggytalents.client.screen.DogNewInfoScreen.store.slice.MainPanelSlice;
import doggytalents.client.screen.DogNewInfoScreen.store.slice.StatsViewPanelSlice;
import doggytalents.client.screen.DogNewInfoScreen.store.slice.StyleViewPanelSlice;
import doggytalents.client.screen.DogNewInfoScreen.store.slice.TalentListPageCounterSlice;
import doggytalents.client.screen.DogNewInfoScreen.store.slice.TalentListSlice;
import net.minecraft.client.gui.screens.Screen;

public class Store {
    
    private static Store INSTANCE;

    //Re-render listener.
    private Screen screen;

    private final Map<Class<? extends AbstractSlice>, StoreValue> applicationStates
        = Maps.newConcurrentMap();

    private Store() {
        this.registerSlices();
    }

    private <T extends AbstractSlice> void registerSlice(Class<T> slice) {
        try {   
            var worker = slice.getConstructor().newInstance();
            var storeValue = new StoreValue(worker, worker.getInitalState());
            this.applicationStates.computeIfAbsent(slice, $ -> storeValue);
        } catch (Exception e) {
        }
    }

    private void registerSlices() {
        registerSlice(ActiveTabSlice.class);
        registerSlice(TalentListSlice.class);
        registerSlice(TalentListPageCounterSlice.class);
        registerSlice(ActiveTalentDescSlice.class);
        registerSlice(StyleViewPanelSlice.class);
        registerSlice(ActiveSkinSlice.class);
        registerSlice(StatsViewPanelSlice.class);
        registerSlice(MainPanelSlice.class);
    }

    private void init(Screen screen) {
        for (var x : this.applicationStates.entrySet()) {
            var initState = x.getValue().worker.getInitalState();
            x.getValue().state = initState;
        }
        this.screen = screen;
    }

    /**
     * A "dispatch" only happens with user interactions.
     * @param <T>
     * @param slice
     * @param action
     */
    public <T extends AbstractSlice> void dispatch(Class<T> slice, UIAction action, 
        int widthAfter, int heightAfter) {
        var storeValue = this.applicationStates.get(slice);
        if (storeValue == null) return;
        storeValue.state = storeValue.worker.reducer(storeValue.state, action);
        //ChopinLogger.l("Dispatched action: [" + action.type  + "] to ["
        //    + slice.getSimpleName() + "] with payload [" + action.payload +"]."); 
        this.screen.init(
            this.screen.getMinecraft(), 
            widthAfter, 
            heightAfter
        );
    }

    public <T extends AbstractSlice> void dispatch(Class<T> slice, UIAction action) {
        dispatch(slice, action, this.screen.width, this.screen.height);
    }

    public <T extends AbstractSlice> void dispatchAll(UIAction action, 
        int widthAfter, int heightAfter) {
        for (var entry : this.applicationStates.entrySet()) {
            var storeValue = entry.getValue();
            if (storeValue == null) return;
            storeValue.state = storeValue.worker.reducer(storeValue.state, action);
        }
        //ChopinLogger.l("Dispatched action: [" + action.type  + "] to all slices with payload [" + action.payload +"]."); 
        
        this.screen.init(
            this.screen.getMinecraft(), 
            widthAfter, 
            heightAfter
        );
    }

    public <T extends AbstractSlice> void dispatchAll(UIAction action) {
        dispatchAll(action, this.screen.width, this.screen.height);
    } 

    public <T extends Object, S extends AbstractSlice> T getStateOrDefault(
        Class<S> slice, Class<T> cast, T defaultState) {
        var storeValue = this.applicationStates.get(slice);
        if (storeValue == null) return defaultState;
        if (cast.isInstance(storeValue.state)) {
            return cast.cast(storeValue.state);
        }
        return defaultState;
    }

    public static Store get(Screen screen) {
        if (INSTANCE == null) {
            INSTANCE = new Store();
            INSTANCE.init(screen);
        } else if (screen != INSTANCE.screen) {
            INSTANCE.screen = screen;
        }
        return INSTANCE;
    }

    private static void cleanUpStaticCache() {
        if (INSTANCE == null) return;
        for (var entry : INSTANCE.applicationStates.entrySet()) {
            var slice = entry.getValue().worker;
            if (slice instanceof CleanableSlice cSlice) {
                cSlice.cleanUpSlice();
            }
        }
    }

    public static void finish() {
        cleanUpStaticCache();
        INSTANCE = null;
        ToolTipOverlayManager.finish();
    }

    private static class StoreValue {
        public AbstractSlice worker;
        public Object state;

        public StoreValue(AbstractSlice worker, Object state) {
            this.worker = worker;
            this.state = state;
        }
    }

}
