package doggytalents;

import java.util.List;
import java.util.Random;

import doggytalents.api.registry.DogBedRegistry;
import doggytalents.api.registry.BedMaterial;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ModCreativeTabs {

    public static final ItemGroup GENERAL = new ItemGroup("doggytalents") {
    	
        @Override
        @OnlyIn(Dist.CLIENT)
        public ItemStack createIcon()
        {
        	return new ItemStack(ModItems.TRAINING_TREAT);
        }
    };
	
	public static final ItemGroup DOG_BED = new ItemGroup("doggytalents.dogbed") {
		private Random random = new Random();
		
        @Override
        @OnlyIn(Dist.CLIENT)
        public ItemStack createIcon() {
        	return DogBedRegistry.createItemStack(pickRandomString(DogBedRegistry.CASINGS.getKeys()), pickRandomString(DogBedRegistry.BEDDINGS.getKeys()));
        }
    	
    	public BedMaterial pickRandomString(List<BedMaterial> strs) {
    		return strs.get(random.nextInt(strs.size()));
    	}
    };
}
