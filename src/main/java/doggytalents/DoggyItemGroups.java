package doggytalents;

import java.util.function.Supplier;

import doggytalents.common.util.DogBedUtil;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class DoggyItemGroups {

    public static final ItemGroup GENERAL = new CustomItemGroup("doggytalents", () -> new ItemStack(DoggyItems.TRAINING_TREAT.get()));
    public static final ItemGroup DOG_BED = new CustomItemGroup("doggytalents.dogbed", DogBedUtil::createRandomBed);

    public static class CustomItemGroup extends ItemGroup {

        private Supplier<ItemStack> icon;

        public CustomItemGroup(String label, Supplier<ItemStack> iconIn) {
            super(label);
            this.icon = iconIn;
        }

        @Override
        public ItemStack createIcon() {
            return this.icon.get();
        }
    }
}
