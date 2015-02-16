package doggytalents.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import doggytalents.DoggyTalentsMod;
import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.registry.DogBedRegistry;
import doggytalents.entity.EntityDog;
import doggytalents.proxy.CommonProxy;
import doggytalents.tileentity.TileEntityDogBed;
import doggytalents.tileentity.TileEntityFoodBowl;

/**
 * @author ProPercivalalb
 **/
public class BlockFoodBowl extends BlockContainer {
	
    public BlockFoodBowl() {
        super(Material.iron);
        this.setTickRandomly(true);
        this.setCreativeTab(DoggyTalentsAPI.CREATIVE_TAB);
        this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 1.0F - 0.0625F, 0.5F, 1.0F - 0.0625F);
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
	public boolean isFullCube()
    {
        return false;
    }
	
	@Override
	public int getRenderType() {
	    return 3;
	}

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        else
        {
            TileEntityFoodBowl tileentitydogfoodbowl = (TileEntityFoodBowl)worldIn.getTileEntity(pos);
            player.openGui(DoggyTalentsMod.instance, CommonProxy.GUI_ID_FOOD_BOWL, worldIn, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
    }
    
    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, Entity entity) {
    	TileEntityFoodBowl foodBowl = (TileEntityFoodBowl) world.getTileEntity(pos);
        List list = null;
        list = world.getEntitiesWithinAABB(EntityDog.class, new AxisAlignedBB(pos.getX(), pos.getY() + 0.5D, pos.getZ(), pos.getX() + 1, pos.getY() + 0.5D + 0.05000000074505806D, pos.getZ() + 1));

        if (list != null && list.size() > 0)
        {
            for (int l = 0; l < list.size(); l++)
            {
            	EntityDog entitydtdoggy = (EntityDog)list.get(l);
                //TODO entitydtdoggy.saveposition.setBowlX(x);
            	//TODO entitydtdoggy.saveposition.setBowlY(y);
            	//TODO entitydtdoggy.saveposition.setBowlZ(z);
            }
        }
        
        if (entity instanceof EntityItem) {
            EntityItem entityItem = (EntityItem)entity;
            

            if(TileEntityHopper.func_145898_a(foodBowl, entityItem))
                world.playSoundEffect(pos.getX() + 0.5D, pos.getX() + 0.5D, pos.getZ() + 0.5D, "random.pop", 0.25F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            
        }

        List list2 = null;
        list2 = world.getEntitiesWithinAABB(EntityDog.class, new AxisAlignedBB(pos.getX(), pos.getY() + 0.5D, pos.getZ(), pos.getX() + 1, pos.getY() + 0.5D + 0.05000000074505806D, pos.getZ() + 1));

        if (list2 != null && list2.size() > 0)
        {
            TileEntity tileentity1 = world.getTileEntity(pos);

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
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
		if(!this.canBlockStay(worldIn, pos)) {
		    TileEntity tile = worldIn.getTileEntity(pos);
			if(tile instanceof TileEntityDogBed) {
					
				TileEntityDogBed dogBed = (TileEntityDogBed)tile;
				
		        this.spawnAsEntity(worldIn, pos, DogBedRegistry.createItemStack(dogBed.getCasingId(), dogBed.getBeddingId()));
		        worldIn.setBlockToAir(pos);
			}
		}
	}

	public boolean canBlockStay(World world, BlockPos pos) {
		IBlockState blockstate = world.getBlockState(pos.down());
		return blockstate.getBlock().isSideSolid(world, pos.down(), EnumFacing.UP);
	}
}
