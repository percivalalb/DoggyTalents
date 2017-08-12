package doggytalents.block;

import doggytalents.DoggyTalents;
import doggytalents.ModItems;
import doggytalents.base.ObjectLib;
import doggytalents.base.VersionControl;
import doggytalents.inventory.InventoryTreatBag;
import doggytalents.proxy.CommonProxy;
import doggytalents.tileentity.TileEntityFoodBowl;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
