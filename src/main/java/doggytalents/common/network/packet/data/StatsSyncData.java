package doggytalents.common.network.packet.data;

import doggytalents.common.entity.stats.StatsTracker;

public class StatsSyncData {

    public static class Request extends DogData {

        public Request(int entityId) {
            super(entityId);
            //TODO Auto-generated constructor stub
        }
        
    }

    public static class Response extends DogData {

        public StatsTracker tracker;

        public Response(int entityId, StatsTracker tracker) {
            super(entityId);
            this.tracker = tracker;
        }
        
    }
    
}
