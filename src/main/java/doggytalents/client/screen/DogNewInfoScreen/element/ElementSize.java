package doggytalents.client.screen.DogNewInfoScreen.element;

import net.minecraft.util.Mth;

public class ElementSize {
    AbstractElement element;
    int width;
    int height;
    float widthRatio;
    float heightRatio;
    SizeType type;

    public ElementSize(AbstractElement element, int width, int height) {
        this.element = element;
        this.type = SizeType.ABSOLUTE;
        this.width = width;
        this.height = height;
    }

    public ElementSize(AbstractElement element, float ratioX, float ratioY) {
        this.element = element;
        this.type = SizeType.RELATIVE;
        this.widthRatio = ratioX;
        this.heightRatio = ratioY;
        var p = element.getParent();
        if (p != null) {
            this.width = Mth.floor(p.getSize().width * this.widthRatio);
            this.height = Mth.floor(p.getSize().height * this.heightRatio);
        }
    }

    public ElementSize(AbstractElement element, int width, float ratioY) {
        this.element = element;
        this.type = SizeType.RELATIVE;
        this.heightRatio = ratioY;
        var p = element.getParent();
        if (p != null) {
            this.height = Mth.floor(p.getSize().height * this.heightRatio);
        }
        this.width = width;
    }

    public ElementSize(AbstractElement element, float ratioX, int height) {
        this.element = element;
        this.type = SizeType.RELATIVE;
        this.widthRatio = ratioX;
        var p = element.getParent();
        if (p != null) {
            this.width = Mth.floor(p.getSize().width * this.widthRatio);
        }
        this.height = height;
    }

    public ElementSize(AbstractElement element, int size) {
        this.element = element;
        this.type = SizeType.ABSOLUTE;
        this.width = size;
        this.height = size;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public static enum SizeType {
        ABSOLUTE,
        RELATIVE,
    }

    public static ElementSize getDefault(AbstractElement element) {
        return new ElementSize(element, 0);
    }
}
