package doggytalents;

import java.util.List;
import java.util.Random;

import doggytalents.api.inferface.IBedMaterial;
import doggytalents.block.DogBedRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ModCreativeTabs {

    public static final ItemGroup GENERAL = new ItemGroup("doggytalents") {
        @Override
        @OnlyIn(Dist.CLIENT)
        public ItemStack createIcon() {
            return new ItemStack(ModItems.TRAINING_TREAT);
        }
    };
    
    public static final ItemGroup DOG_BED = new ItemGroup("doggytalents.dogbed") {
        private Random random = new Random();
        
        @Override
        @OnlyIn(Dist.CLIENT)
        public ItemStack createIcon() {
            return DogBedRegistry.createItemStack(this.pickRandomString(DogBedRegistry.CASINGS.getKeys()), this.pickRandomString(DogBedRegistry.BEDDINGS.getKeys()));
        }
        
        public IBedMaterial pickRandomString(List<IBedMaterial> strs) {
            return strs.get(this.random.nextInt(strs.size()));
        }
    };
}
