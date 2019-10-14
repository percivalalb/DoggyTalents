package doggytalents.api;

import doggytalents.api.inferface.IDogBedRegistry;
import net.minecraftforge.fml.common.eventhandler.Event;

public class BeddingRegistryEvent extends Event {

    private IDogBedRegistry casingRegistry;
    private IDogBedRegistry beddingRegistry;
    
    public BeddingRegistryEvent(IDogBedRegistry casingRegistry, IDogBedRegistry beddingRegistry) {
        this.casingRegistry = casingRegistry;
        this.beddingRegistry = beddingRegistry;
    }
    
    public IDogBedRegistry getBeddingRegistry() {
        return this.beddingRegistry;
    }
    
    public IDogBedRegistry getCasingRegistry() {
        return this.casingRegistry;
    }
}
