package doggytalents.common.util;

//TODO
public class RadialCoordinateIterator {

    private final int startX;
    private final int startY;
    private final int startZ;
    private final int endX;
    private final int endY;
    private final int endZ;
    private final boolean oddX;
    private final boolean oddY;
    private final boolean oddZ;

    private int centreX;
    private int centreY;
    private int centreZ;


    private int x;
    private int y;
    private int z;
    private int radiusX;
    private int radiusY;
    private int radiusZ;
    private boolean started;

    public RadialCoordinateIterator(int startXIn, int startYIn, int startZIn, int endXIn, int endYIn, int endZIn) {
        this.startX = startXIn;
        this.startY = startYIn;
        this.startZ = startZIn;
        this.endX = endXIn;
        this.endY = endYIn;
        this.endZ = endZIn;
        this.oddX = ((endXIn - startXIn) & 1) == 0;
        this.oddY = ((endYIn - startYIn) & 1) == 0;
        this.oddZ = ((endZIn - startZIn) & 1) == 0;
    }

    public boolean hasNext() {
        if (!this.started) {
            if (this.oddX) {
                this.x = this.startX + (this.endX - this.startX) / 2;
                this.radiusX = 0;
            } else {
                this.x = this.startX + (this.endX - this.startX - 1) / 2;
                this.radiusX = 1;
            }

            if (this.oddY) {
                this.y = this.startY + (this.endY - this.startY - 1) / 2;
                this.radiusY = 0;
            } else {
                this.y = this.startY + (this.endY - this.startY - 1) / 2;
                this.radiusY = 1;
            }

            if (this.oddZ) {
                this.z = this.startZ + (this.endZ - this.startZ) / 2;
                this.radiusZ = 0;
            } else {
                this.z = this.startZ + (this.endZ - this.startZ - 1) / 2;
                this.radiusZ = 1;
            }

            this.centreX = this.x;
            this.centreY = this.y;
            this.centreZ = this.z;
            this.started = true;
            return true;
        } else if (this.x == this.endX && this.y == this.endY && this.z == this.endZ) {
            return false;
        } else {
            boolean xBoundary = this.x == this.centreX + this.radiusX;
            boolean yBoundary = this.y == this.centreY + this.radiusY;
            boolean zBoundary = this.z == this.centreZ + this.radiusZ;

            if (this.x <= this.centreX + this.radiusX) {
                ++this.x;
            } else if (this.z <= this.centreZ + this.radiusZ) {
                ++this.z;
            } else if (this.x >= this.centreX - this.radiusX) {
                --this.x;
            } else if (this.z >= this.centreZ - this.radiusZ) {
                --this.z;
            } else if (this.z < this.endZ) {
                this.x = this.startX;
                this.y = this.startY;
                ++this.z;
            }

            return true;
        }
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }
}