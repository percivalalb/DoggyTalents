package doggytalents.api.feature;

import java.util.Arrays;
import java.util.Comparator;

import doggytalents.api.inferface.AbstractDog;

public enum EnumMode {

    DOCILE(0, "docile"),
    WANDERING(1, "wandering"),
    AGGRESIVE(2, "aggressive"),
    BERSERKER(3, "berserker"),
    TACTICAL(4, "tactical"),
    PATROL(5, "patrol"),
    GUARD(6, "guard");

    private int index;
    private String saveName;
    private String unlocalisedTip;
    private String unlocalisedName;
    private String unlocalisedInfo;

    public static final EnumMode[] VALUES = Arrays.stream(EnumMode.values()).sorted(Comparator.comparingInt(EnumMode::getIndex)).toArray(size -> {
        return new EnumMode[size];
    });

    private EnumMode(int index, String name) {
        this(index, name, "dog.mode." + name, "dog.mode." + name + ".indicator", "dog.mode." + name + ".description");
    }

    private EnumMode(int index, String mode, String unlocalisedName, String tip, String info) {
        this.index = index;
        this.saveName = mode;
        this.unlocalisedName = unlocalisedName;
        this.unlocalisedTip = tip;
        this.unlocalisedInfo = info;
    }

    public int getIndex() {
        return this.index;
    }

    public String getSaveName() {
        return this.saveName;
    }

    public String getTip() {
        return this.unlocalisedTip;
    }

    public String getUnlocalisedName() {
        return this.unlocalisedName;
    }

    public String getUnlocalisedInfo() {
        return this.unlocalisedInfo;
    }

    public void onModeSet(AbstractDog dog, EnumMode prev) {
        switch(prev) {
        default:
            dog.getNavigation().stop();
            dog.setTarget(null);
            dog.setLastHurtByMob(null);
            break;
        }
    }

    public EnumMode previousMode() {
        int i = this.getIndex() - 1;
        if (i < 0) {
            i = VALUES.length - 1;
        }
        return VALUES[i];
    }

    public EnumMode nextMode() {
        int i = this.getIndex() + 1;
        if (i >= VALUES.length) {
            i = 0;
        }
        return VALUES[i];
    }

    public static EnumMode byIndex(int i) {
        if (i < 0 || i >= VALUES.length) {
            i = EnumMode.DOCILE.getIndex();
        }
        return VALUES[i];
    }

    public static EnumMode bySaveName(String saveName) {
        for (EnumMode gender : EnumMode.values()) {
            if (gender.saveName.equals(saveName)) {
                return gender;
            }
        }

        return DOCILE;
    }
}