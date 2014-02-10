package doggytalents.entity.doggyhelper;

import doggytalents.entity.EntityDTDoggy;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ProPercivalalb
 */
public class NBTDataHelper {

	public static void writeEntityToNBT(EntityDTDoggy dog, NBTTagCompound par1NBTTagCompound) {
        //par1NBTTagCompound.setByte("CollarColor", (byte)dog.getCollarColor());
    }

    public static void readEntityFromNBT(EntityDTDoggy dog, NBTTagCompound par1NBTTagCompound) {
    	 if (par1NBTTagCompound.hasKey("CollarColor")) {
             //dog.setCollarColor(par1NBTTagCompound.getByte("CollarColor"));
         }
    }
}
