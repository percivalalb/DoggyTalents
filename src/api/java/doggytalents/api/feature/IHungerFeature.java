package doggytalents.api.feature;

public interface IHungerFeature {

    /**
     * @return True if the dog is incapacicated
     */
    public boolean isIncapacicated();

    /**
     * @return The current hunger value
     */
    public int getDogHunger();

    /**
     * @param value The hunger value to set, will be bound between
     * 0 and {@link #getMaxHunger()}
     */
    public void setDogHunger(int value);

    /**
     * The max hunger the dog can have
     */
    public int getMaxHunger();
}
