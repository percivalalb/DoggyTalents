package doggytalents.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.base.Strings;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import doggytalents.DoggyTalentsMod;
import doggytalents.ModBlocks;
import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.registry.DogBedRegistry;
import doggytalents.entity.EntityDog;
import doggytalents.tileentity.TileEntityDogBath;
import doggytalents.tileentity.TileEntityDogBed;

/**
 * @author ProPercivalalb
 */
public class BlockDogBath extends BlockContainer {

	public BlockDogBath() {
		super(Material.wood);
		this.setHardness(2.0F);
		this.setResistance(5.0F);
		this.setStepSound(Block.soundTypeMetal);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
		this.setCreativeTab(DoggyTalentsAPI.CREATIVE_TAB);
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
	public Item getItemDropped(int meta, Random par2Random, int fortune) {
	    return Item.getItemById(-1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return Blocks.iron_block.getIcon(0, 0);
	}
	
	@Override
	public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {
		if(par7Entity instanceof EntityDog) {
		 
		}
		else {
			super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
		}
    }
	
	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		if(entity instanceof EntityDog) {
			EntityDog dog = (EntityDog)entity;
			dog.forceShake = true;
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {}

	@Override
	public boolean isOpaqueCube() {
        return false;
    }
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getRenderType() {
	    return DoggyTalentsMod.proxy.RENDER_ID_DOG_BATH;
	}
	
	@Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        if (!super.canPlaceBlockAt(world, x, y, z))
            return false;
        else
            return canBlockStay(world, x, y, z);
    }
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		if (!this.canBlockStay(world, x, y, z))
			this.removedByPlayer(world, null, x, y, z, false);
	}

	@Override
	public boolean canBlockStay(World world, int x, int y, int z) {
		Block block = world.getBlock(x, y - 1, z);
		return block != null && block.isSideSolid(world, x, y - 1, z, ForgeDirection.UP);
	}
}
