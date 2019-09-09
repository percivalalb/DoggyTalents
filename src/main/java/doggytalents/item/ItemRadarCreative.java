package doggytalents.item;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import doggytalents.entity.ai.DogLocationManager;
import doggytalents.entity.ai.DogLocationManager.DogLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
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
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if(!worldIn.isRemote) {
            DimensionType dimCurr = playerIn.dimension;
            
            playerIn.sendMessage(new StringTextComponent(""));

            DogLocationManager locationManager = DogLocationManager.getHandler((ServerWorld)worldIn);
            List<DogLocation> ownDogs = locationManager.getList(dimCurr, loc -> loc.getOwner(worldIn) == playerIn);
            
            if(ownDogs.isEmpty()) {
                playerIn.sendMessage(new TranslationTextComponent("dogradar.errornull", String.valueOf(DimensionType.getKey(dimCurr))));
            } else {
                for(DogLocation loc : ownDogs) {
                    String translateStr = ItemRadar.getDirectionTranslationKey(loc, playerIn);
                        
                    playerIn.sendMessage(new TranslationTextComponent(translateStr, loc.getName(worldIn), MathHelper.ceil(Math.sqrt(playerIn.getDistanceSq(loc)))));
                }
            }
            
            List<DimensionType> otherDogs = new ArrayList<>();
            List<DimensionType> noDogs = new ArrayList<>();
            for(DimensionType dimType : DimensionType.getAll()) {
                if(dimCurr == dimType) continue;
                locationManager = DogLocationManager.getHandler((ServerWorld)worldIn);
                ownDogs = locationManager.getList(dimType, loc -> loc.getOwner(worldIn) == playerIn);
                
                if(ownDogs.size() > 0) {
                    otherDogs.add(dimType);
                } else {
                    noDogs.add(dimType);
                }
            }
            
            if(otherDogs.size() > 0)
                playerIn.sendMessage(new TranslationTextComponent("dogradar.notindim", otherDogs.stream().map(dim -> String.valueOf(DimensionType.getKey(dim))).collect(Collectors.joining(", "))));
            
            if(noDogs.size() > 0)
                playerIn.sendMessage(new TranslationTextComponent("dogradar.errornull", noDogs.stream().map(dim -> String.valueOf(DimensionType.getKey(dim))).collect(Collectors.joining(", "))));
        }
        return new ActionResult<ItemStack>(ActionResultType.FAIL, playerIn.getHeldItem(handIn));
    }
    
    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        
        tooltip.add(new TranslationTextComponent(this.getTranslationKey() + ".tooltip"));
    }
    
    @Override
    public Rarity getRarity(ItemStack stack) {
        return Rarity.EPIC;
    }
}
