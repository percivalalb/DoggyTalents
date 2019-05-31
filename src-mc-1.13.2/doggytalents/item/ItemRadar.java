package doggytalents.item;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import doggytalents.entity.ai.DogLocationManager;
import doggytalents.entity.ai.DogLocationManager.DogLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

/**
 * @author ProPercivalalb
 **/
public class ItemRadar extends Item {
	
	public ItemRadar(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if(!worldIn.isRemote) {
			DimensionType dimCurr = playerIn.dimension;
			
			playerIn.sendMessage(new TextComponentString(""));

			DogLocationManager locationManager = DogLocationManager.getHandler(worldIn, dimCurr);
			List<DogLocation> ownDogs = locationManager.getList(loc -> loc.getOwner(worldIn) == playerIn);
			
			if(ownDogs.isEmpty()) {
				playerIn.sendMessage(new TextComponentTranslation("dogradar.errornull", String.valueOf(DimensionType.getKey(dimCurr))));
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

			List<DimensionType> otherDogs = new ArrayList<>();
			for(DimensionType dimType : DimensionType.getAll()) {
				if(dimCurr == dimType) continue;
				locationManager = DogLocationManager.getHandler(worldIn, dimType);
				ownDogs = locationManager.getList(loc -> loc.getOwner(worldIn) == playerIn && loc.hasRadioCollar(worldIn));
				
				if(ownDogs.size() > 0) {
					otherDogs.add(dimType);
				}
			}
			
			if(otherDogs.size() > 0)
				playerIn.sendMessage(new TextComponentTranslation("dogradar.notindim", otherDogs.stream().map(dim -> String.valueOf(DimensionType.getKey(dim))).collect(Collectors.joining(", "))));
			
		}
		return new ActionResult<ItemStack>(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));
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
