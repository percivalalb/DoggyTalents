package doggytalents.entity.doggyhelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import doggytalents.entity.EntityDTDoggy;

/**
 * @author ProPercivalalb
 */
public class ModelHelper {

    @SideOnly(Side.CLIENT)
    public static float getBegAngle(EntityDTDoggy dog, float par1) {
        return (dog.BEG_VAR_2 + (dog.BEG_VAR_1 - dog.BEG_VAR_2) * par1) * 0.15F * (float)Math.PI;
    }
}
