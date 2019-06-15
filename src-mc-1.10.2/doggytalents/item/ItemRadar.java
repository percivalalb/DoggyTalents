package doggytalents.item;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import doggytalents.entity.ai.DogLocationManager;
import doggytalents.entity.ai.DogLocationManager.DogLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 **/
public class ItemRadar extends ItemDT {
	
	public ItemRadar() {
		super();
		this.setMaxStackSize(1);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if(!worldIn.isRemote) {
			int dimCurr = playerIn.dimension;
			
			playerIn.sendMessage(new TextComponentString(""));

			DogLocationManager locationManager = DogLocationManager.getHandler(worldIn);
			List<DogLocation> ownDogs = locationManager.getList(dimCurr, loc -> loc.getOwner(worldIn) == playerIn);
			
			if(ownDogs.isEmpty()) {
				playerIn.sendMessage(new TextComponentTranslation("dogradar.errornull", String.valueOf(dimCurr)));
			} else {
				boolean noRadioCollars = true;
				
				for(DogLocation loc : ownDogs) {
					if(loc.hasRadioCollar(worldIn)) {
						noRadioCollars = false;
						
						String translateStr = ItemRadar.getDirectionTranslationKey(loc, playerIn);
						
						playerIn.sendMessage(new TextComponentTranslation(translateStr, loc.getName(worldIn), (int)Math.ceil(playerIn.getDistance(loc.x, loc.y, loc.z))));
					}
				}
				
				if(noRadioCollars) {
					playerIn.sendMessage(new TextComponentTranslation("dogradar.errornoradio"));
				}
			}

			
			Set<Integer> otherDogs = new HashSet<>();
			List<DogLocation> allDogs = locationManager.getAll(loc -> loc.getOwner(worldIn) == playerIn && loc.hasRadioCollar(worldIn));
			
			for(DogLocation loc : allDogs) {
				if(dimCurr == loc.dim) continue;

				otherDogs.add(loc.dim);
			}
			
			if(otherDogs.size() > 0)
				playerIn.sendMessage(new TextComponentTranslation("dogradar.notindim", otherDogs.stream().map(ItemRadarCreative::getDimensionName).collect(Collectors.joining(", "))));
		}
		return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStackIn);
	}

	public static String getDirectionTranslationKey(DogLocation loc, Entity entity) {
		double angle = MathHelper.atan2(loc.x - entity.posX, loc.z - entity.posZ);
		
		if(angle < -Math.PI + Math.PI / 8)
			return "dogradar.north";
		else if(angle < -Math.PI + 3 * Math.PI / 8)
			return "dogradar.north.west";
		else if(angle < -Math.PI + 5 * Math.PI / 8)
			return "dogradar.west";
		else if(angle < -Math.PI + 7 * Math.PI / 8)
			return "dogradar.south.west";
		else if(angle < -Math.PI + 9 * Math.PI / 8)
			return "dogradar.south";
		else if(angle < -Math.PI + 11 * Math.PI / 8)
			return "dogradar.south.east";
		else if(angle < -Math.PI + 13 * Math.PI / 8)
			return "dogradar.east";
		else if(angle < -Math.PI + 15 * Math.PI / 8)
			return "dogradar.north.east";
		else
			return "dogradar.north";
	}
}
