package doggytalents.item;

import doggytalents.entity.EntityDog;
import net.minecraft.entity.player.EntityPlayer;

public abstract class ItemChewStick extends ItemDT {
	
	public abstract void addChewStickEffects(EntityPlayer player, EntityDog dog);
}
