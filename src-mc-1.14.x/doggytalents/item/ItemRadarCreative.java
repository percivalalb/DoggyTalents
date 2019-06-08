package doggytalents.item;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import doggytalents.entity.ai.DogLocationManager;
import doggytalents.entity.ai.DogLocationManager.DogLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author ProPercivalalb
 **/
public class ItemRadarCreative extends Item {
	
	public ItemRadarCreative(Properties properties) {
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
				for(DogLocation loc : ownDogs) {
					String translateStr = ItemRadar.getDirectionTranslationKey(loc, playerIn);
						
					playerIn.sendMessage(new TextComponentTranslation(translateStr, loc.getName(worldIn), (int)Math.ceil(playerIn.getDistance(loc.x, loc.y, loc.z))));
				}
			}

			List<DimensionType> otherDogs = new ArrayList<>();
			List<DimensionType> noDogs = new ArrayList<>();
			for(DimensionType dimType : DimensionType.getAll()) {
				if(dimCurr == dimType) continue;
				locationManager = DogLocationManager.getHandler(worldIn, dimType);
				ownDogs = locationManager.getList(loc -> loc.getOwner(worldIn) == playerIn && loc.hasRadioCollar(worldIn));
				
				if(ownDogs.size() > 0) {
					otherDogs.add(dimType);
				} else {
					noDogs.add(dimType);
				}
			}
			
			if(otherDogs.size() > 0)
				playerIn.sendMessage(new TextComponentTranslation("dogradar.notindim", otherDogs.stream().map(dim -> String.valueOf(DimensionType.getKey(dim))).collect(Collectors.joining(", "))));
			
			if(noDogs.size() > 0)
				playerIn.sendMessage(new TextComponentTranslation("dogradar.errornull", noDogs.stream().map(dim -> String.valueOf(DimensionType.getKey(dim))).collect(Collectors.joining(", "))));
			
		}
		return new ActionResult<ItemStack>(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		
		tooltip.add(new TextComponentTranslation(this.getTranslationKey() + ".tooltip"));
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }
}
