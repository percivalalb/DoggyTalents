package doggytalents;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.registry.IBeddingMaterial;
import doggytalents.api.registry.ICasingMaterial;
import doggytalents.common.item.IDyeableArmorItem;
import doggytalents.common.util.DogBedUtil;
import doggytalents.common.util.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;

import java.util.Collections;
import java.util.List;

public class DoggyItemGroups {

    public static CreativeModeTab GENERAL;
    public static CreativeModeTab DOG_BED;

    public static void creativeModeTabRegisterEvent(final CreativeModeTabEvent.Register event) {
        GENERAL = event.registerCreativeModeTab(Util.getResource("main"), (builder) -> builder.title(Component.translatable("itemGroup.doggytalents")).icon(() -> new ItemStack(DoggyItems.TRAINING_TREAT.get())));
        DOG_BED = event.registerCreativeModeTab(Util.getResource("dogbed"), Collections.emptyList(), List.of(GENERAL), (builder) -> builder.title(Component.translatable("itemGroup.doggytalents.dogbed")).icon(DogBedUtil::createRandomBed));
    }

    public static void creativeModeTabBuildEvent(final CreativeModeTabEvent.BuildContents event) {
        if (event.getTab() == GENERAL) {
            // Adds items in the order of definition in DoggyItems. This matches order
            // from previous version of MC prior to 1.19.3.
            for (var item : DoggyItems.ITEMS.getEntries()) {
                if (item.get() instanceof IDyeableArmorItem dyeableItem) {
                    ItemStack stack = new ItemStack(item.get());

                    dyeableItem.setColor(stack, dyeableItem.getDefaultColor(stack));
                    event.accept(stack);
                    continue;
                }

                event.accept(item);
            }

            // Add the block items, missing the dog bed out as that has it's own tab.
            for (var blockItem : DoggyBlocks.ITEMS.getEntries()) {
                if (blockItem.getId().getPath() == "dog_bed") {
                    continue;
                }

                event.accept(blockItem);
            }
        }

        if (event.getTab() == DOG_BED) {
            for (IBeddingMaterial beddingId : DoggyTalentsAPI.BEDDING_MATERIAL.get().getValues()) {
                for (ICasingMaterial casingId : DoggyTalentsAPI.CASING_MATERIAL.get().getValues()) {
                    event.accept(DogBedUtil.createItemStack(casingId, beddingId), CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
                }
            }
        }
    }
}
