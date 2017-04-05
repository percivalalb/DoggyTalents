package doggytalents.block;

import java.util.List;

import doggytalents.DoggyTalentsMod;
import doggytalents.api.DoggyTalentsAPI;
import doggytalents.entity.EntityDog;
import doggytalents.proxy.CommonProxy;
import doggytalents.tileentity.TileEntityFoodBowl;
import jline.internal.Nullable;
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
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ProPercivalalb
 **/
public class BlockFoodBowl extends BlockContainer {
	
    public BlockFoodBowl() {
        super(Material.iron);
        this.setHardness(5.0F);
        this.setTickRandomly(true);
        this.setCreativeTab(DoggyTalentsAPI.CREATIVE_TAB);
        this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 1.0F - 0.0625F, 0.5F, 1.0F - 0.0625F);
		this.setResistance(5.0F);
    }
    
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
		
	@Override
	public boolean isFullBlock() {
		return false;
	}
		
	@Override
	public boolean isFullCube() {
	    return false;
	}
		
	@Override
	public int getRenderType() {
		return 3;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        else
        {
            TileEntityFoodBowl tileentitydogfoodbowl = (TileEntityFoodBowl)worldIn.getTileEntity(pos);
            playerIn.openGui(DoggyTalentsMod.instance, CommonProxy.GUI_ID_FOOD_BOWL, worldIn, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
    }
    
    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
    	TileEntityFoodBowl foodBowl = (TileEntityFoodBowl) worldIn.getTileEntity(pos);
        List list = null;
        list = worldIn.getEntitiesWithinAABB(EntityDog.class, new AxisAlignedBB(pos.getX(), pos.getY() + 0.5D, pos.getZ(), pos.getX() + 1, pos.getY() + 0.5D + 0.05000000074505806D, pos.getZ() + 1));

        if (list != null && list.size() > 0)
        {
            for (int l = 0; l < list.size(); l++)
            {
            	EntityDog dog = (EntityDog)list.get(l);
            	dog.coords.setBowlPos(pos);
            }
        }
        
        if (entityIn instanceof EntityItem) {
            EntityItem entityItem = (EntityItem)entityIn;
            

            if(TileEntityHopper.putDropInInventoryAllSlots(foodBowl, entityItem)) {
            	worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getX() + 0.5D, pos.getZ() + 0.5D, "random.pop", 0.25F, ((worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            }
        }

        List list2 = null;
        list2 = worldIn.getEntitiesWithinAABB(EntityDog.class, new AxisAlignedBB(pos.getX(), pos.getY() + 0.5D, pos.getZ(), pos.getX() + 1, pos.getY() + 0.5D + 0.05000000074505806D, pos.getZ() + 1));

        if (list2 != null && list2.size() > 0)
        {
            TileEntity tileentity1 = worldIn.getTileEntity(pos);

            if (!(tileentity1 instanceof TileEntityFoodBowl))
            {
                return;
            }

            TileEntityFoodBowl tileentitydogfoodbowl1 = (TileEntityFoodBowl)tileentity1;

            for (int j1 = 0; j1 < list2.size(); j1++)
            {
            	EntityDog entitydtdoggy1 = (EntityDog)list2.get(j1);

                if (entitydtdoggy1.getDogHunger() <= 60 && tileentitydogfoodbowl1.getFirstDogFoodStack(entitydtdoggy1) >= 0)
                {
                    tileentitydogfoodbowl1.feedDog(entitydtdoggy1, tileentitydogfoodbowl1.getFirstDogFoodStack(entitydtdoggy1), 1);
                }
            }
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityFoodBowl();
    }

    @Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
	    return super.canPlaceBlockAt(worldIn, pos) ? this.canBlockStay(worldIn, pos) : false;
	}

	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
		if(!this.canBlockStay(world, pos)) {
			this.dropBlockAsItem((World)world, pos, world.getBlockState(pos), 0);
			((World)world).setBlockToAir(pos);
		}
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if(tileentity instanceof TileEntityFoodBowl) {
			InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityFoodBowl)tileentity);
			worldIn.updateComparatorOutputLevel(pos, this);
		}

		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public boolean hasComparatorInputOverride() {
        return true;
    }

	@Override
    public int getComparatorInputOverride(World worldIn, BlockPos pos) {
        return Container.calcRedstone(worldIn.getTileEntity(pos));
    }

	public boolean canBlockStay(IBlockAccess world, BlockPos pos) {
		IBlockState blockstate = world.getBlockState(pos.down());
		return blockstate.getBlock().isSideSolid(world, pos.down(), EnumFacing.UP);
	}
}
