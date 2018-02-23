package doggytalents.block;

import doggytalents.DoggyTalents;
import doggytalents.base.ObjectLib;
import doggytalents.base.VersionControl;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 **/
public abstract class BlockFoodBowl extends BlockContainer {
	
    public BlockFoodBowl() {
        super(Material.IRON);
        this.setHardness(5.0F);
        this.setTickRandomly(true);
        this.setCreativeTab(DoggyTalents.CREATIVE_TAB);
		this.setResistance(5.0F);
    }
    
    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
    	return VersionControl.createObject(ObjectLib.TILE_FOOD_BOWL_CLASS);
    }
}
