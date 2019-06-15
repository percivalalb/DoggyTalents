package doggytalents.item;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import doggytalents.entity.ai.DogLocationManager;
import doggytalents.entity.ai.DogLocationManager.DogLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ProPercivalalb
 **/
public class ItemRadarCreative extends ItemDT {
	
	public ItemRadarCreative() {
		super();
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if(!worldIn.isRemote) {
			int dimCurr = playerIn.dimension;
			
			playerIn.sendMessage(new TextComponentString(""));

			DogLocationManager locationManager = DogLocationManager.getHandler(worldIn);
			List<DogLocation> ownDogs = locationManager.getList(dimCurr, loc -> loc.getOwner(worldIn) == playerIn);
			
			if(ownDogs.isEmpty()) {
				playerIn.sendMessage(new TextComponentTranslation("dogradar.errornull", String.valueOf(dimCurr)));
			} else {
				for(DogLocation loc : ownDogs) {
						
					String translateStr = ItemRadar.getDirectionTranslationKey(loc, playerIn);
						
					playerIn.sendMessage(new TextComponentTranslation(translateStr, loc.getName(worldIn), (int)Math.ceil(playerIn.getDistance(loc.x, loc.y, loc.z))));
				}
			}

			
			Set<Integer> otherDogs = new HashSet<>();
			List<DogLocation> allDogs = locationManager.getAll(loc -> loc.getOwner(worldIn) == playerIn);
			
			for(DogLocation loc : allDogs) {
				if(dimCurr == loc.dim) continue;

				otherDogs.add(loc.dim);
			}
			
			if(otherDogs.size() > 0)
				playerIn.sendMessage(new TextComponentTranslation("dogradar.notindim", otherDogs.stream().map(ItemRadarCreative::getDimensionName).collect(Collectors.joining(", "))));
			
		}
		
		return new ActionResult<ItemStack>(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));
	}
	
	public static String getDimensionName(int dim) {
		DimensionType type = DimensionManager.getProviderType(dim);
		if(type != null) {
			return type.getName();
		} else {
			return "ID: " + dim;
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		super.addInformation(stack, playerIn, tooltip, advanced);
		
		tooltip.add(new TextComponentTranslation(this.getUnlocalizedName() + ".tooltip").getFormattedText());
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }
}
