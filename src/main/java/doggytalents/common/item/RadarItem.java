package doggytalents.common.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import doggytalents.DoggyItems;
import doggytalents.common.storage.DogLocationData;
import doggytalents.common.storage.DogLocationStorage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import net.minecraft.item.Item.Properties;

public class RadarItem extends Item {

    public RadarItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
//        if (worldIn.isRemote) {
//            DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> RadarScreen.open(playerIn));
//        }

        if (!worldIn.isClientSide) {
            if (playerIn.isShiftKeyDown()) {
                DogLocationStorage locationManager = DogLocationStorage.get(worldIn);
                for (UUID uuid : locationManager.getAllUUID()) {
                    playerIn.sendMessage(new StringTextComponent(locationManager.getData(uuid).toString()), Util.NIL_UUID);
                }
                return new ActionResult<ItemStack>(ActionResultType.FAIL, playerIn.getItemInHand(handIn));
            }

            RegistryKey<World> dimCurr = playerIn.level.dimension();

            playerIn.sendMessage(new StringTextComponent(""), Util.NIL_UUID);

            DogLocationStorage locationManager = DogLocationStorage.get(worldIn);
            List<DogLocationData> ownDogs = locationManager.getDogs(playerIn, dimCurr).collect(Collectors.toList());

            if (ownDogs.isEmpty()) {
                playerIn.sendMessage(new TranslationTextComponent("dogradar.errornull", dimCurr), Util.NIL_UUID);
            } else {
                boolean flag = false;

                for (DogLocationData loc : ownDogs) {
                    if (loc.shouldDisplay(worldIn, playerIn, handIn)) {
                        flag = true;

                        String translateStr = RadarItem.getDirectionTranslationKey(loc, playerIn);
                        int distance = MathHelper.ceil(loc.getPos() != null ? loc.getPos().distanceTo(playerIn.position()) : -1);

                        playerIn.sendMessage(new TranslationTextComponent(translateStr, loc.getName(worldIn), distance), Util.NIL_UUID);
                    }
                }

                if (!flag) {
                    playerIn.sendMessage(new TranslationTextComponent("dogradar.errornoradio"), Util.NIL_UUID);
                }
            }

            List<RegistryKey<World>> otherDogs = new ArrayList<>();
            List<RegistryKey<World>> noDogs = new ArrayList<>();
            for (RegistryKey<World> worldkey : worldIn.getServer().levelKeys()) {
                if (worldkey.equals(worldIn.dimension()))  continue;
                ownDogs = locationManager.getDogs(playerIn, worldkey).collect(Collectors.toList()); // Check if radio collar is on

                (ownDogs.size() > 0 ? otherDogs : noDogs).add(worldkey);
            }

            if (otherDogs.size() > 0) {
                playerIn.sendMessage(new TranslationTextComponent("dogradar.notindim", otherDogs.stream().map(RegistryKey::location).map(Objects::toString).collect(Collectors.joining(", "))), Util.NIL_UUID);
            }

            if (noDogs.size() > 0 && stack.getItem() == DoggyItems.CREATIVE_RADAR.get()) {
                playerIn.sendMessage(new TranslationTextComponent("dogradar.errornull", noDogs.stream().map(RegistryKey::location).map(Objects::toString).collect(Collectors.joining(", "))), Util.NIL_UUID);
            }
        }
        return new ActionResult<ItemStack>(ActionResultType.FAIL, stack);
    }

    public static String getDirectionTranslationKey(DogLocationData loc, Entity entity) {
        if (loc.getPos() == null) {
            return "dogradar.unknown";
        }
        Vector3d diff = loc.getPos().add(entity.position().reverse());
        double angle = MathHelper.atan2(diff.x(), diff.z());

        if (angle < -Math.PI + Math.PI / 8) {
            return "dogradar.north";
        } else if (angle < -Math.PI + 3 * Math.PI / 8) {
            return "dogradar.north.west";
        } else if (angle < -Math.PI + 5 * Math.PI / 8) {
            return "dogradar.west";
        } else if (angle < -Math.PI + 7 * Math.PI / 8) {
            return "dogradar.south.west";
        } else if (angle < -Math.PI + 9 * Math.PI / 8) {
            return "dogradar.south";
        } else if (angle < -Math.PI + 11 * Math.PI / 8) {
            return "dogradar.south.east";
        } else if (angle < -Math.PI + 13 * Math.PI / 8) {
            return "dogradar.east";
        } else if (angle < -Math.PI + 15 * Math.PI / 8) {
            return "dogradar.north.east";
        } else {
            return "dogradar.north";
        }
    }
}
