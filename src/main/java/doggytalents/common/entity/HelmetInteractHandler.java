package doggytalents.common.entity;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import doggytalents.DoggyAccessories;
import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.inferface.IDogItem;
import doggytalents.api.registry.Accessory;
import doggytalents.api.registry.AccessoryInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.IRegistryDelegate;

public class HelmetInteractHandler implements IDogItem {

    private static final Map<IRegistryDelegate<? extends Item>, RegistryObject<? extends Accessory>> MAPPING = new ImmutableMap.Builder<IRegistryDelegate<? extends Item>, RegistryObject<? extends Accessory>>()
        .put(Items.IRON_HELMET.delegate,      DoggyAccessories.IRON_HELMET)
        .put(Items.DIAMOND_HELMET.delegate,   DoggyAccessories.DIAMOND_HELMET)
        .put(Items.GOLDEN_HELMET.delegate,    DoggyAccessories.GOLDEN_HELMET)
        .put(Items.CHAINMAIL_HELMET.delegate, DoggyAccessories.CHAINMAIL_HELMET)
        .put(Items.TURTLE_HELMET.delegate,    DoggyAccessories.TURTLE_HELMET)
        .put(Items.NETHERITE_HELMET.delegate, DoggyAccessories.NETHERITE_HELMET)
        .put(Items.IRON_BOOTS.delegate,     DoggyAccessories.IRON_BOOTS)
        .put(Items.DIAMOND_BOOTS.delegate,     DoggyAccessories.DIAMOND_BOOTS)
        .put(Items.GOLDEN_BOOTS.delegate,     DoggyAccessories.GOLDEN_BOOTS)
        .put(Items.CHAINMAIL_BOOTS.delegate,     DoggyAccessories.CHAINMAIL_BOOTS)
        .put(Items.NETHERITE_BOOTS.delegate,     DoggyAccessories.NETHERITE_BOOTS)
        .put(Items.IRON_CHESTPLATE.delegate,  DoggyAccessories.IRON_BODY_PIECE)
        .put(Items.DIAMOND_CHESTPLATE.delegate, DoggyAccessories.DIAMOND_BODY_PIECE)
        .put(Items.GOLDEN_CHESTPLATE.delegate, DoggyAccessories.GOLDEN_BODY_PIECE)
        .put(Items.CHAINMAIL_CHESTPLATE.delegate, DoggyAccessories.CHAINMAIL_BODY_PIECE)
        .put(Items.NETHERITE_CHESTPLATE.delegate, DoggyAccessories.NETHERITE_BODY_PIECE)
        .put(Items.LEATHER_HELMET.delegate,   DoggyAccessories.LEATHER_HELMET)
        .put(Items.LEATHER_BOOTS.delegate,   DoggyAccessories.LEATHER_BOOTS)
        .put(Items.LEATHER_CHESTPLATE.delegate,   DoggyAccessories.LEATHER_BODY_PIECE)
       .build();

    @Override
    public ActionResultType processInteract(AbstractDogEntity dogIn, World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (dogIn.isTamed() && dogIn.canInteract(playerIn)) {
            ItemStack stack = playerIn.getHeldItem(handIn);

            if (!stack.isEmpty()) {
                RegistryObject<? extends Accessory> associatedAccessory = MAPPING.get(stack.getItem().delegate);

                if (associatedAccessory != null) {
                    AccessoryInstance inst = associatedAccessory.get().createFromStack(stack.copy().split(1));

                    if (dogIn.addAccessory(inst)) {
                        dogIn.consumeItemFromStack(playerIn, stack);
                        return ActionResultType.SUCCESS;
                    }
                }
            }
        }

        return ActionResultType.PASS;
    }

}
