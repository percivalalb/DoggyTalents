package doggytalents;

import java.util.List;
import java.util.Random;

import doggytalents.api.registry.DogBedRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModCreativeTabs {

    public static final CreativeTabs GENERAL = new CreativeTabs("doggytalents") {
        @Override
        @SideOnly(Side.CLIENT)
        public ItemStack createIcon() {
        	return new ItemStack(ModItems.TRAINING_TREAT);
        }
    };
	
	public static final CreativeTabs DOG_BED = new CreativeTabs("doggytalents.dogbed") {
		private Random random = new Random();
		
        @Override
        @SideOnly(Side.CLIENT)
        public ItemStack createIcon() {
        	return DogBedRegistry.createItemStack(this.pickRandomString(DogBedRegistry.CASINGS.getKeys()), this.pickRandomString(DogBedRegistry.BEDDINGS.getKeys()));
        }
    	
        public String pickRandomString(List<String> strs) {
    		return strs.get(this.random.nextInt(strs.size()));
    	}
    };
}
