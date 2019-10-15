package doggytalents.api.feature;

public interface ILevelFeature {


    /**
     * Gets the dog's level
     * @return The dog's level
     */
    public int getLevel();

    /**
     * Gets the dog's dire level
     * @return The dog's dire level
     */
    public int getDireLevel();


    /**
     * Simple wrapper function that takes the current level
     * and adds one and sets it
     */
    default void increaseLevel() {
        this.setLevel(this.getLevel() + 1);
    }

    /**
     * Simple wrapper function that takes the current dire level
     * and adds one and sets it
     */
    default void increaseDireLevel() {
        this.setDireLevel(this.getDireLevel() + 1);
    }

    /**
     * Sets the dogs level
     * @param level The level
     */
    public void setLevel(int level);

    /**
     * Sets the dogs level
     * @param level The level
     */
    public void setDireLevel(int level);
}
