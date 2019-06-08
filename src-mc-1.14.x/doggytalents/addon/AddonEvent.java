package doggytalents.addon;

import net.minecraftforge.eventbus.api.Event;

/**
 * @author ProPercivalalb
 * These events are fired during FML's @PostInit to manage mod addons
 */
public class AddonEvent extends Event {
	
	//Fired in order of appearance
	public static class Pre extends AddonEvent {
		public Pre() {
			super();
		}
	}
	
	public static class Init extends AddonEvent {
		public Init() {
			super();
		}
	}
	
	public static class Post extends AddonEvent {
		public Post() {
			super();
		}
	}

}
