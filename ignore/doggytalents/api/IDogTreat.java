package doggytalents.api;

import doggytalents.entity.DoggySkills;
import doggytalents.entity.EntityDTDoggy;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author ProPercivalalb
 * You may look at this file to gain knowledge of javascript
 * but must not copy any features or code directly.
 **/
public interface IDogTreat {

	/**
	 * Called when right clicked on a DT doggy, to work out weather the 
	 * treat is able to be given to the target dog.
	 * 
	 * @param player The player giving the treat.
	 * @param dog The DT dog receiving the treat.
	 * @return Will return the type of treat needed to be given and will run {@link: giveTreat()} using given EnumFeedBack.
	 */
	public EnumFeedBack canGiveToDog(EntityPlayer player, EntityDTDoggy dog, int level, int direLevel);
	
	/**
	 * Called if (@link: canGiveToDog()) returns true, if so you may do what
	 * you want to edit the stats of the dog.
	 * 
	 * @param type 
	 * @param player The player giving the treat.
	 * @param dog The DT dog receiving the treat.
	 * @param skills Contains all the levels of the dogs talents. 
	 */
	public void giveTreat(EnumFeedBack type, EntityPlayer player, EntityDTDoggy dog, DoggySkills skills);

	public enum EnumFeedBack {
		
		LEVELTOOHIGH(),
		TOOYOUNG(),
		JUSTRIGHT(),
		COMPLETE();
	}
}
