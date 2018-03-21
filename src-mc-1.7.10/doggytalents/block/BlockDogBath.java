package doggytalents.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import doggytalents.DoggyTalents;
import doggytalents.entity.EntityDog;
import doggytalents.tileentity.TileEntityDogBath;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**;
 * @author ProPercivalalb
 */
public class BlockDogBath extends BlockContainer {

	protected static final AxisAlignedBB AABB = AxisAlignedBB.getBoundingBox(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
	
	public BlockDogBath() {
		super(Material.iron);
		this.setHardness(3.0F);
		this.setResistance(5.0F);
		this.setStepSound(Block.soundTypeMetal);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
		this.setCreativeTab(DoggyTalents.CREATIVE_TAB);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityDogBath();
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack stack) {
	    int facingDirection = MathHelper.floor_double((double)(entityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
	    world.setBlockMetadataWithNotify(x, y, z, facingDirection, 2);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int x, int y, int z, int side) {
        if(side == 0) {
        	Block block = par1IBlockAccess.getBlock(x, y - 1, z);
        	if(par1IBlockAccess instanceof World)
        		return block.isSideSolid((World)par1IBlockAccess, x, y - 1, z, ForgeDirection.UP);
        	else
        		return block.isBlockSolid(par1IBlockAccess, x, y - 1, z, 1);
        }
		return true;
    }

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return Blocks.iron_block.getIcon(0, 0);
	}
	
	@Override
	public int getRenderType() {
	    return DoggyTalents.PROXY.RENDER_ID_DOG_BATH;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {}
	
	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		if(entity instanceof EntityDog) {
			EntityDog dog = (EntityDog)entity;
			dog.isShaking = true;
		}
	}
	
	@Override
	public boolean isOpaqueCube() {
        return false;
    }
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, int x, int y, int z) {
	    return super.canPlaceBlockAt(worldIn, x, y, z) ? this.canBlockStay(worldIn, x, y, z) : false;
	}

	@Override
	public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block block) {
		if(!this.canBlockStay(worldIn, x, y, z)) {
			this.dropBlockAsItem(worldIn, x, y, z, worldIn.getBlockMetadata(x, y, z), 0);
			worldIn.setBlockToAir(x, y, z);
		}
	}
	
	@Override
	public boolean canBlockStay(World world, int x, int y, int z) {
		Block block = world.getBlock(x, y - 1, z);
		return block != null && block.isSideSolid(world, x, y - 1, z, ForgeDirection.UP);
	}
}
