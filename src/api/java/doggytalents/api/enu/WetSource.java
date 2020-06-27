package doggytalents.api.enu;

public enum WetSource {

    WATER(true),
    BUBBLE_COLUMN(true),
    RAIN(false);

    boolean isWater;

    WetSource(boolean isWaterIn) {
        this.isWater = isWaterIn;
    }

    public boolean isWaterBlock() {
        return this.isWater;
    }

    public static WetSource of(boolean inWater, boolean inBubbleColumn, boolean inRain) {
        return inWater ? WetSource.WATER : (inBubbleColumn ? WetSource.BUBBLE_COLUMN : WetSource.RAIN);
    }
}
