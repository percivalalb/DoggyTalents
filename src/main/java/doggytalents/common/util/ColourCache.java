package doggytalents.common.util;

public class ColourCache {

    public static final ColourCache WHITE = new ColourCache(16632255);

    private final int color;
    private float[] floatArray;

    protected ColourCache(int color) {
        this.color = color;
    }

    public int get() {
        return this.color;
    }

    public float[] getFloatArray() {
        if (this.floatArray == null) {
            this.floatArray = Util.rgbIntToFloatArray(this.color);
        }

        return this.floatArray;
    }

    public static ColourCache make(int color) {
        return new ColourCache(color);
    }

    @Override
    public int hashCode() {
        return this.color;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj.getClass() == ColourCache.class)) {
            return false;
        }

        if (!(obj instanceof ColourCache)) {
            return false;
        }

        ColourCache other = (ColourCache) obj;
        return other.color == this.color;
    }

    public boolean is(int colorIn) {
        return this.color == colorIn;
    }
}
