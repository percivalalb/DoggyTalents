package doggytalents.common.item;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import doggytalents.DoggyItems;
import doggytalents.client.screen.RadarScreen;
import doggytalents.common.storage.DogLocationData;
import doggytalents.common.storage.DogLocationStorage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class RadarItem extends Item {

    public RadarItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
//        if(worldIn.isRemote) {
//            DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> RadarScreen.open(playerIn));
//        }

        if(!worldIn.isRemote) {
            if (playerIn.isSneaking()) {
                DogLocationStorage locationManager = DogLocationStorage.get(worldIn);
                for (UUID uuid : locationManager.getAllUUID()) {
                    playerIn.sendMessage(new StringTextComponent(locationManager.getData(uuid).toString()));
                }
                return new ActionResult<ItemStack>(ActionResultType.FAIL, playerIn.getHeldItem(handIn));
            }

            DimensionType dimCurr = playerIn.dimension;

            playerIn.sendMessage(new StringTextComponent(""));

            DogLocationStorage locationManager = DogLocationStorage.get(worldIn);
            List<DogLocationData> ownDogs = locationManager.getDogs(playerIn, worldIn.getDimension().getType()).collect(Collectors.toList());

            if(ownDogs.isEmpty()) {
                playerIn.sendMessage(new TranslationTextComponent("dogradar.errornull", String.valueOf(DimensionType.getKey(dimCurr))));
            } else {
                boolean flag = false;

                for(DogLocationData loc : ownDogs) {
                    if(loc.shouldDisplay(worldIn, playerIn, handIn)) {
                        flag = true;

                        String translateStr = RadarItem.getDirectionTranslationKey(loc, playerIn);
                        int distance = MathHelper.ceil(loc.getPos() != null ? loc.getPos().distanceTo(playerIn.getPositionVector()) : -1);

                        playerIn.sendMessage(new TranslationTextComponent(translateStr, loc.getName(worldIn), distance));
                    }
                }

                if(!flag) {
                    playerIn.sendMessage(new TranslationTextComponent("dogradar.errornoradio"));
                }
            }


            List<DimensionType> otherDogs = Lists.newArrayList();
            List<DimensionType> noDogs = Lists.newArrayList();
            for(DimensionType dimType : DimensionType.getAll()) {
                if(dimCurr == dimType) continue;
                ownDogs = locationManager.getDogs(playerIn, dimType).collect(Collectors.toList()); // Check if radio collar is on

                (ownDogs.size() > 0 ? otherDogs : noDogs).add(dimType);
            }

            if(otherDogs.size() > 0) {
                playerIn.sendMessage(new TranslationTextComponent("dogradar.notindim", otherDogs.stream().map(dim -> String.valueOf(DimensionType.getKey(dim))).collect(Collectors.joining(", "))));
            }

            if(noDogs.size() > 0 && stack.getItem() == DoggyItems.CREATIVE_RADAR.get()) {
                playerIn.sendMessage(new TranslationTextComponent("dogradar.errornull", noDogs.stream().map(dim -> String.valueOf(DimensionType.getKey(dim))).collect(Collectors.joining(", "))));
            }
        }
        return new ActionResult<ItemStack>(ActionResultType.FAIL, stack);
    }

    public static String getDirectionTranslationKey(DogLocationData loc, Entity entity) {
        if (loc.getPos() == null) {
            return "dogradar.unknown";
        }
        Vec3d diff = loc.getPos().add(entity.getPositionVector().inverse());
        double angle = MathHelper.atan2(diff.getX(), diff.getZ());

        if(angle < -Math.PI + Math.PI / 8) {
            return "dogradar.north";
        } else if(angle < -Math.PI + 3 * Math.PI / 8) {
            return "dogradar.north.west";
        } else if(angle < -Math.PI + 5 * Math.PI / 8) {
            return "dogradar.west";
        } else if(angle < -Math.PI + 7 * Math.PI / 8) {
            return "dogradar.south.west";
        } else if(angle < -Math.PI + 9 * Math.PI / 8) {
            return "dogradar.south";
        } else if(angle < -Math.PI + 11 * Math.PI / 8) {
            return "dogradar.south.east";
        } else if(angle < -Math.PI + 13 * Math.PI / 8) {
            return "dogradar.east";
        } else if(angle < -Math.PI + 15 * Math.PI / 8) {
            return "dogradar.north.east";
        } else {
            return "dogradar.north";
        }
    }
}
