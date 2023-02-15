package doggytalents.client.screen.DogNewInfoScreen.element;

import javax.annotation.Nonnull;

public class ElementPosition {

    private AbstractElement element;
    private int x;
    private int y;
    private int originAbsoluteX;
    private int originAbsoluteY;
    private ChildDirection dir = ChildDirection.ROW;
    private PosType type = PosType.FIXED;

    public ElementPosition(@Nonnull AbstractElement element, int x, int y, PosType type) {
        this.element = element;
        this.x = x;
        this.y = y;
        this.type = type;
        if (this.type == PosType.RELATIVE) {
            computeOriginAbsolute();
        }
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getRealX() {

        if (this.type == PosType.FIXED) {
            return this.x;
        } 

        int realX = (this.type == PosType.RELATIVE) ?
            this.originAbsoluteX + this.x : 
            this.x;

        var p = this.element.getParent();

        while(p != null) {
            var pPos = p.getPosition();
            if (pPos.type == PosType.FIXED) {
                realX += pPos.x;
                break;
            }
            realX += (pPos.type == PosType.RELATIVE) ?
                pPos.originAbsoluteX + pPos.x
                : pPos.x;
            p = p.getParent();
        }
        return realX;
        
    }

    public int getRealY() {
        if (this.type == PosType.FIXED) {
            return this.y;
        } 

        int realY = (this.type == PosType.RELATIVE) ?
            this.originAbsoluteY + this.y : 
            this.y;

        var p = this.element.getParent();

        while(p != null) {
            var pPos = p.getPosition();
            if (pPos.type == PosType.FIXED) {
                realY += pPos.y;
                break;
            }
            realY += (pPos.type == PosType.RELATIVE) ?
                pPos.originAbsoluteY + pPos.y
                : pPos.y;
            p = p.getParent();
        }
        return realY;
    }

    private void computeOriginAbsolute() {
        var p = this.element.getParent();
        if (p == null) {
            this.originAbsoluteX = 0; this.originAbsoluteY = 0;
            return;
        }
        var dir = p.getPosition().getChildDirection();
        var pChilds = p.children();
        if (pChilds == null || pChilds.isEmpty()) {
            //WTF
            this.originAbsoluteX = 0; this.originAbsoluteY = 0;
            return;
        }
        int indx = pChilds.size() -1;
        var lastChild = pChilds.get(indx);
        if (lastChild == this) {
            --indx;
        }
        for (int i = 0; i <= indx; ++i) {
            var guiEvLis = pChilds.get(i);
            if (guiEvLis instanceof AbstractElement e && e.getPosition().type == PosType.RELATIVE) {
                if (dir == ChildDirection.COL) {
                    this.originAbsoluteY +=
                        e.getPosition().originAbsoluteY 
                        + e.getSizeY();
                } else {
                    this.originAbsoluteX +=
                        e.getPosition().originAbsoluteX
                        + e.getSizeX();
                }
            }
        }
    }

    public ChildDirection getChildDirection() {
        return this.dir;
    }

    public void setChildDirection(ChildDirection dir) {
        this.dir = dir;
    }
    
    public static enum PosType {
        RELATIVE,
        ABSOLUTE,
        FIXED
    }

    public static enum ChildDirection {
        COL,
        ROW
    }

    public static ElementPosition getDefault(@Nonnull AbstractElement element) {
        return new ElementPosition(element, 0, 0, PosType.RELATIVE);
    }
}
