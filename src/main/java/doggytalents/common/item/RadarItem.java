package doggytalents.common.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import doggytalents.DoggyItems;
import doggytalents.common.storage.DogLocationData;
import doggytalents.common.storage.DogLocationStorage;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceKey;
import net.minecraft.Util;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;

import net.minecraft.world.item.Item.Properties;

public class RadarItem extends Item {

    public RadarItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
//        if (worldIn.isRemote) {
//            DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> RadarScreen.open(playerIn));
//        }

        if (!worldIn.isClientSide) {
            if (playerIn.isShiftKeyDown()) {
                DogLocationStorage locationManager = DogLocationStorage.get(worldIn);
                for (UUID uuid : locationManager.getAllUUID()) {
                    playerIn.sendMessage(new TextComponent(locationManager.getData(uuid).toString()), Util.NIL_UUID);
                }
                return new InteractionResultHolder<ItemStack>(InteractionResult.FAIL, playerIn.getItemInHand(handIn));
            }

            ResourceKey<Level> dimCurr = playerIn.level.dimension();

            playerIn.sendMessage(new TextComponent(""), Util.NIL_UUID);

            DogLocationStorage locationManager = DogLocationStorage.get(worldIn);
            List<DogLocationData> ownDogs = locationManager.getDogs(playerIn, dimCurr).collect(Collectors.toList());

            if (ownDogs.isEmpty()) {
                playerIn.sendMessage(new TranslatableComponent("dogradar.errornull", dimCurr), Util.NIL_UUID);
            } else {
                boolean flag = false;

                for (DogLocationData loc : ownDogs) {
                    if (loc.shouldDisplay(worldIn, playerIn, handIn)) {
                        flag = true;

                        String translateStr = RadarItem.getDirectionTranslationKey(loc, playerIn);
                        int distance = Mth.ceil(loc.getPos() != null ? loc.getPos().distanceTo(playerIn.position()) : -1);

                        playerIn.sendMessage(new TranslatableComponent(translateStr, loc.getName(worldIn), distance), Util.NIL_UUID);
                    }
                }

                if (!flag) {
                    playerIn.sendMessage(new TranslatableComponent("dogradar.errornoradio"), Util.NIL_UUID);
                }
            }

            List<ResourceKey<Level>> otherDogs = new ArrayList<>();
            List<ResourceKey<Level>> noDogs = new ArrayList<>();
            for (ResourceKey<Level> worldkey : worldIn.getServer().levelKeys()) {
                if (worldkey.equals(worldIn.dimension()))  continue;
                ownDogs = locationManager.getDogs(playerIn, worldkey).collect(Collectors.toList()); // Check if radio collar is on

                (ownDogs.size() > 0 ? otherDogs : noDogs).add(worldkey);
            }

            if (otherDogs.size() > 0) {
                playerIn.sendMessage(new TranslatableComponent("dogradar.notindim", otherDogs.stream().map(ResourceKey::location).map(Objects::toString).collect(Collectors.joining(", "))), Util.NIL_UUID);
            }

            if (noDogs.size() > 0 && stack.getItem() == DoggyItems.CREATIVE_RADAR.get()) {
                playerIn.sendMessage(new TranslatableComponent("dogradar.errornull", noDogs.stream().map(ResourceKey::location).map(Objects::toString).collect(Collectors.joining(", "))), Util.NIL_UUID);
            }
        }
        return new InteractionResultHolder<ItemStack>(InteractionResult.FAIL, stack);
    }

    public static String getDirectionTranslationKey(DogLocationData loc, Entity entity) {
        if (loc.getPos() == null) {
            return "dogradar.unknown";
        }
        Vec3 diff = loc.getPos().add(entity.position().reverse());
        double angle = Mth.atan2(diff.x(), diff.z());

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
