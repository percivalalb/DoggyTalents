package doggytalents;

import java.util.List;
import java.util.Random;

import doggytalents.api.registry.DogBedRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModCreativeTabs {

    public static final CreativeTabs GENERAL = new CreativeTabs("doggytalents") {
        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
        	return ModItems.TRAINING_TREAT;
        }
    };
	
	public static final CreativeTabs DOG_BED = new CreativeTabs("doggytalents.dogbed") {
		private Random random = new Random();
		
		@SideOnly(Side.CLIENT)
		private ItemStack iconItemStack;
		
		@Override
		@SideOnly(Side.CLIENT)
		public ItemStack getIconItemStack() {
			if (this.iconItemStack == null) {
				this.iconItemStack = DogBedRegistry.createItemStack(this.pickRandomString(DogBedRegistry.CASINGS.getKeys()), this.pickRandomString(DogBedRegistry.BEDDINGS.getKeys()));
			}

			return this.iconItemStack;
		}

        public String pickRandomString(List<String> strs) {
    		return strs.get(this.random.nextInt(strs.size()));
    	}

		@Override
		public Item getTabIconItem() {
			return ModItems.TRAINING_TREAT;
		}
    };
}
