package doggytalents.api.feature;

import java.util.Objects;

public record DogLevel(int level, int direLevel) {

    public enum Type {
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

    public int getLevel(Type type) {
        return type == Type.DIRE ? this.direLevel : this.level;
    }

    public boolean canIncrease(Type type) {
        return type == Type.DIRE ? this.level >= 60 : true;
    }

    public DogLevel setLevel(Type type, int level) {
        if (type == Type.DIRE) {
            return new DogLevel(this.level, level);
        } else {
            return new DogLevel(level, this.direLevel);
        }
    }

    public DogLevel incrementLevel(Type type) {
        return this.setLevel(type, this.getLevel(type) + 1);
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

    public boolean isDireDog() {
        return this.direLevel >= 30;
    }
}
