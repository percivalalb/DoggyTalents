package doggytalents.api.feature;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public enum EnumGender {

    MALE(1, "male"),
    FEMALE(2, "female"),
    UNISEX(0, "unisex");

    private int index;
    private String saveName;
    private String unlocalisedTip;
    private String unlocalisedName;
    private String unlocalisedPronoun;
    private String unlocalisedSubject;
    private String unlocalisedTitle;

    public static final EnumGender[] VALUES = Arrays.stream(EnumGender.values()).sorted(Comparator.comparingInt(EnumGender::getIndex)).toArray(size -> {
        return new EnumGender[size];
    });

    private EnumGender(int index, String name) {
        this.index = index;
        this.saveName = name;
        this.unlocalisedName = "dog.gender." + name;
        this.unlocalisedTip = this.unlocalisedName + ".indicator";
        this.unlocalisedPronoun = this.unlocalisedName + ".pronoun";
        this.unlocalisedSubject = this.unlocalisedName + ".subject";
        this.unlocalisedTitle = this.unlocalisedName + ".title";
    }

    public int getIndex() {
        return this.index;
    }

    public String getSaveName() {
        return this.saveName;
    }

    public String getUnlocalisedTip() {
        return this.unlocalisedTip;
    }

    public String getUnlocalisedName() {
        return this.unlocalisedName;
    }

    public String getUnlocalisedPronoun() {
        return this.unlocalisedPronoun;
    }

    public String getUnlocalisedSubject() {
        return this.unlocalisedSubject;
    }

    public String getUnlocalisedTitle() {
        return this.unlocalisedTitle;
    }

    public boolean canMateWith(EnumGender gender) {
        boolean equalGenders = this == gender;
        return (equalGenders && this == EnumGender.UNISEX) || !equalGenders;
    }

    public static EnumGender byIndex(int i) {
        if (i < 0 | i >= VALUES.length) {
            i = EnumGender.UNISEX.getIndex();
        }
        return VALUES[i];
    }

    public static EnumGender bySaveName(String saveName) {
        for (EnumGender gender : EnumGender.values()) {
            if (gender.getSaveName().equals(saveName)) {
                return gender;
            }
        }

        return UNISEX;
    }

    public static EnumGender random(Random rng) {
        return rng.nextBoolean() ? MALE : FEMALE;
    }

}