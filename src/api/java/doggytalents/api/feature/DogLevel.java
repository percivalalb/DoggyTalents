package doggytalents.api.feature;

public class DogLevel {

    private int level;
    private int direLevel;

    public static enum Type {
        NORMAL("normal_treat"),
        DIRE("dire_treat");

        String n;

        Type(String n) {
            this.n = n;
        }

        public String getName() {
            return this.n;
        }
    }

    public DogLevel(int level, int direLevel) {
        this.level = level;
        this.direLevel = direLevel;
    }

    public int getLevel(Type type) {
        return type == Type.DIRE ? this.direLevel : this.level;
    }

    public boolean canIncrease(Type type) {
        return type == Type.DIRE ? this.level >= 60 : true;
    }

    @Deprecated
    public void setLevel(Type type, int level) {
        if (type == Type.DIRE) {
            this.direLevel = level;
        } else {
            this.level = level;
        }
    }

    @Deprecated
    public void incrementLevel(Type type) {
        this.setLevel(type, this.getLevel(type) + 1);
    }

    public DogLevel copy() {
        return new DogLevel(this.level, this.direLevel);
    }

    /**
     * Combines parents levels together
     */
    public DogLevel combine(DogLevel levelIn) {
        int combinedLevel = this.getLevel(Type.NORMAL) + levelIn.getLevel(Type.NORMAL);
        combinedLevel /= 2;
        combinedLevel = Math.min(combinedLevel, 20);
        return new DogLevel(combinedLevel, 0);
    }

    public final boolean isDireDog() {
        return this.direLevel >= 30;
    }

}
