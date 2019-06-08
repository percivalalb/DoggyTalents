package doggytalents.item;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import doggytalents.entity.ai.DogLocationManager;
import doggytalents.entity.ai.DogLocationManager.DogLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.ServerWorld;
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
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if(!worldIn.isRemote) {
			DimensionType dimCurr = playerIn.dimension;
			
			playerIn.sendMessage(new StringTextComponent(""));

			DogLocationManager locationManager = DogLocationManager.getHandler((ServerWorld)worldIn);
			List<DogLocation> ownDogs = locationManager.getList(dimCurr, loc -> loc.getOwner(worldIn) == playerIn);
			
			if(ownDogs.isEmpty()) {
				playerIn.sendMessage(new TranslationTextComponent("dogradar.errornull", String.valueOf(DimensionType.getKey(dimCurr))));
			} else {
				boolean noRadioCollars = true;
				
				for(DogLocation loc : ownDogs) {
					if(loc.hasRadioCollar(worldIn)) {
						noRadioCollars = false;
						
						String translateStr = ItemRadar.getDirectionTranslationKey(loc, playerIn);
						
						playerIn.sendMessage(new TranslationTextComponent(translateStr, loc.getName(worldIn), MathHelper.ceil(Math.sqrt(playerIn.getDistanceSq(loc)))));
					}
				}
				
				if(noRadioCollars) {
					playerIn.sendMessage(new TranslationTextComponent("dogradar.errornoradio"));
				}
			}

			
			List<DimensionType> otherDogs = new ArrayList<>();
			for(DimensionType dimType : DimensionType.getAll()) {
				if(dimCurr == dimType) continue;
				locationManager = DogLocationManager.getHandler((ServerWorld)worldIn);
				ownDogs = locationManager.getList(dimType, loc -> loc.getOwner(worldIn) == playerIn && loc.hasRadioCollar(worldIn));
				
				if(ownDogs.size() > 0) {
					otherDogs.add(dimType);
				}
			}
			
			if(otherDogs.size() > 0)
				playerIn.sendMessage(new TranslationTextComponent("dogradar.notindim", otherDogs.stream().map(dim -> String.valueOf(DimensionType.getKey(dim))).collect(Collectors.joining(", "))));
		}
		return new ActionResult<ItemStack>(ActionResultType.FAIL, playerIn.getHeldItem(handIn));
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
