package doggytalents.client.screen.DogNewInfoScreen.store;

public class UIActionTypes {
    public static String RESIZE = "resize";
    public static String CHANGE_TAB = "change_tab";
    public static String CHANGE_TAB_NEXT = "change_tab_next";
    public static String CHANGE_TAB_PREV = "change_tab_prev";
    public static String CHANGE_PANEL_TAB = "change_panel_tab";
    public static class Talents {
        public static String OPEN_DESC = "talents.desc";
        
        public static String LIST_INC = "talents.list.increment";
        public static String LIST_DEC = "talents.list.decrement";
    }

    public static class Skins {
        public static String ACTIVE_INC = "skins.active.increment";
        public static String ACTIVE_DEC = "skins.active.decrement";
    }
}
