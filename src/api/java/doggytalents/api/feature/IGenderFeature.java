package doggytalents.api.feature;

import doggytalents.api.inferface.IDogEntity;
import net.minecraft.util.text.TextComponentTranslation;

public interface IGenderFeature {

    /**
     * Gets the dog's gender
     * @return The dog's gender
     */
    public EnumGender getGender();

    /**
     * Sets the dog's gender
     * @param gender The gender
     */
    public void setGender(EnumGender gender);

    /**
     * Returns true if this dog can mate with the given dog.
     *
     * If gender is disabled in the config it will always
     * return true unless this dog equals other
     *
     * A dog can not mate with the same gender unless both the genders
     * are {@link EnumGender#UNISEX}}
     *
     * @param other The other dog
     * @return If breeding can take place
     */
    public boolean canMateWith(IDogEntity other);

    /**
     * @return The title used when referring the the dog
     * with respect to it's gender e.g boy, girl, unisex
     */
    public TextComponentTranslation getGenderTitle();

    /**
     * @return The pronoun used when referring the the dog
     * with respect to it's gender e.g he, him, her, them
     */
    public TextComponentTranslation getGenderPronoun();

    /**
     * @return The subject used when referring the the dog
     * with respect to it's gender e.g he, she, it
     */
    public TextComponentTranslation getGenderSubject();

    /**
     * @return The tip used when referring the the dog
     * with respect to it's gender e.g M, F, U.
     */
    public TextComponentTranslation getGenderTip();

    /**
     * @return The title used when referring the the dog
     * with respect to it's gender e.g boy, girl, unisex
     */
    public TextComponentTranslation getGenderName();
}
