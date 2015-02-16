package doggytalents.addon;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * @author ProPercivalalb
 * These events are fired during FML's @PostInit to manage mod addons
 */
public class AddonEvent extends Event {

	public Configuration config;
	
	public AddonEvent(Configuration config) {
		this.config = config;
	}
	
	//Fired in order of appearance
	public static class Pre extends AddonEvent {
		public Pre(Configuration config) {
			super(config);
		}
	}
	
	public static class Init extends AddonEvent {
		public Init(Configuration config) {
			super(config);
		}
	}
	
	public static class Post extends AddonEvent {
		public Post(Configuration config) {
			super(config);
		}
	}

}
