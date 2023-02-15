package doggytalents.client.screen.DogNewInfoScreen.store;

public class UIAction {
    public String type;
    public Object payload;

    public UIAction (String type, Object payload) {
        this.type = type;
        this.payload = payload;
    }
}
