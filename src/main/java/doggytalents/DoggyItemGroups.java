package doggytalents;

import doggytalents.common.util.DogBedUtil;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class DoggyItemGroups {

    public static final CreativeModeTab GENERAL = new CustomItemGroup("doggytalents", () -> new ItemStack(DoggyItems.TRAINING_TREAT.get()));
    public static final CreativeModeTab DOG_BED = new CustomItemGroup("doggytalents.dogbed", DogBedUtil::createRandomBed);

    public static class CustomItemGroup extends CreativeModeTab {

        private Supplier<ItemStack> icon;

        public CustomItemGroup(String label, Supplier<ItemStack> iconIn) {
            super(label);
            this.icon = iconIn;
        }

        @Override
        public ItemStack makeIcon() {
            return this.icon.get();
        }
    }
}
