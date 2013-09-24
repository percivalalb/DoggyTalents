package doggytalents.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.base.Strings;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import doggytalents.DoggyTalentsMod;
import doggytalents.ModBlocks;
import doggytalents.api.DogBedManager;
import doggytalents.core.helper.LogHelper;
import doggytalents.core.proxy.CommonProxy;
import doggytalents.tileentity.TileEntityDogBed;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

/**
 * @author ProPercivalalb
 */
public class BlockDogBed extends BlockContainer {

	public BlockDogBed(int id) {
		super(id, Material.wood);
		this.setHardness(2.0F);
		this.setResistance(5.0F);
		this.setStepSound(soundWoodFootstep);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.6F, 1.0F);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityDogBed();
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack stack) {
	    int facingDirection = MathHelper.floor_double((double)(entityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

	    if (facingDirection == 0) {
	        world.setBlockMetadataWithNotify(x, y, z, 0, 2);
	    }

	    if (facingDirection == 1) {
	        world.setBlockMetadataWithNotify(x, y, z, 1, 2);
	    }

	    if (facingDirection == 2) {
	        world.setBlockMetadataWithNotify(x, y, z, 2, 2);
	    }

	    if (facingDirection == 3) {
	        world.setBlockMetadataWithNotify(x, y, z, 3, 2);
	    }

	    if (stack.hasTagCompound() && stack.stackTagCompound.hasKey("doggytalents")) {
	    	NBTTagCompound tag = stack.stackTagCompound.getCompoundTag("doggytalents");
	    	
	    	String woodId = tag.getString("woodId");
	    	if(!Strings.isNullOrEmpty(woodId) && DogBedManager.isValidWoodId(woodId)) {
	    		((TileEntityDogBed)world.getBlockTileEntity(x, y, z)).setWoodId(woodId);
	    	}
	    	
	    	String woolId = tag.getString("woolId");
	    	if(!Strings.isNullOrEmpty(woolId) && DogBedManager.isValidWoolId(woolId)) {
	    		((TileEntityDogBed)world.getBlockTileEntity(x, y, z)).setWoolId(woolId);
	    	}
	    }
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int x, int y, int z, int side) {
        if(side == 0) {
        	Block block = Block.blocksList[par1IBlockAccess.getBlockId(x, y - 1, z)];
        	if(par1IBlockAccess instanceof World) {
        		return block != null && block.isBlockSolidOnSide((World)par1IBlockAccess, x, y - 1, z, ForgeDirection.UP);
        	}
        	else {
        		return block != null && block.isBlockSolid(par1IBlockAccess, x, y - 1, z, 1);
        	}
        }
		return true;
    }
	
	@Override
	public int idDropped(int par1, Random par2Random, int par3) {
	    return 0;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean addBlockHitEffects(World worldObj, MovingObjectPosition target, EffectRenderer effectRenderer) {
		int x = target.blockX;
		int y = target.blockY;
		int z = target.blockZ;

		int sideHit = target.sideHit;
		
		TileEntity tile = worldObj.getBlockTileEntity(x, y, z);
		Icon icon = Block.planks.getIcon(0, 0);
		if(tile instanceof TileEntityDogBed) {
			TileEntityDogBed dogBed = (TileEntityDogBed)tile;
			icon = DogBedManager.getWoodIcon(dogBed.getWoodId(), sideHit);
			if(icon == null)
				icon = Block.planks.getIcon(0, 0);
		}
		
		Block block = ModBlocks.dogBed;
		float b = 0.1F;
		double px = x + worldObj.rand.nextDouble() * (block.getBlockBoundsMaxX() - block.getBlockBoundsMinX() - (b * 2.0F)) + b + block.getBlockBoundsMinX();
		double py = y + worldObj.rand.nextDouble() * (block.getBlockBoundsMaxY() - block.getBlockBoundsMinY() - (b * 2.0F)) + b + block.getBlockBoundsMinY();
		double pz = z + worldObj.rand.nextDouble() * (block.getBlockBoundsMaxZ() - block.getBlockBoundsMinZ() - (b * 2.0F)) + b + block.getBlockBoundsMinZ();

		if (sideHit == 0) {
			py = (double) y + block.getBlockBoundsMinY() - (double) b;
		}

		if (sideHit == 1) {
			py = (double) y + block.getBlockBoundsMaxY() + (double) b;
		}

		if (sideHit == 2) {
			pz = (double) z + block.getBlockBoundsMinZ() - (double) b;
		}

		if (sideHit == 3) {
			pz = (double) z + block.getBlockBoundsMaxZ() + (double) b;
		}

		if (sideHit == 4) {
			px = (double) x + block.getBlockBoundsMinX() - (double) b;
		}

		if (sideHit == 5) {
			px = (double) x + block.getBlockBoundsMaxX() + (double) b;
		}

		EntityDiggingFX fx = new EntityDiggingFX(worldObj, px, py, pz, 0.0D, 0.0D, 0.0D, block, sideHit, worldObj.getBlockMetadata(x, y, z));
		fx.setParticleIcon(icon);
		effectRenderer.addEffect(fx.applyColourMultiplier(x, y, z).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addBlockDestroyEffects(World worldObj, int x, int y, int z, int meta, EffectRenderer effectRenderer) {
		TileEntity target = worldObj.getBlockTileEntity(x, y, z);

		byte its = 4;
		for (int i = 0; i < its; ++i) {
			for (int j = 0; j < its; ++j) {
				for (int k = 0; k < its; ++k) {
					double px = x + (i + 0.5D) / (double) its;
					double py = y + (j + 0.5D) / (double) its;
					double pz = z + (k + 0.5D) / (double) its;
					int random = worldObj.rand.nextInt(6);
					Icon icon = Block.planks.getIcon(0, 0);
					if(target instanceof TileEntityDogBed) {
						TileEntityDogBed dogBed = (TileEntityDogBed)target;
						icon = DogBedManager.getWoodIcon(dogBed.getWoodId(), random);
						if(icon == null)
							icon = Block.planks.getIcon(0, 0);
					}
					EntityDiggingFX fx = new EntityDiggingFX(worldObj, px, py, pz, px - x - 0.5D, py - y - 0.5D, pz - z - 0.5D, ModBlocks.dogBed, random, meta);
					fx.setParticleIcon(icon);
					effectRenderer.addEffect(fx.applyColourMultiplier(x, y, z));
				}
			}
		}
		return true;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public Icon getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
		TileEntity tile = blockAccess.getBlockTileEntity(x, y, z);
		if(tile instanceof TileEntityDogBed) {
			TileEntityDogBed dogBed = (TileEntityDogBed)tile;
			return DogBedManager.getWoodIcon(dogBed.getWoodId(), side);
		}
		return null;
    }
	
	@Override
	public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z) {
		TileEntity target = world.getBlockTileEntity(x, y, z);
		TileEntityDogBed dogBed = (TileEntityDogBed)target;
		
		ItemStack stack = this.createItemStack(dogBed.getWoodId(), dogBed.getWoolId());
		
		boolean flag = world.setBlockToAir(x, y, z);
		if(player == null || !player.capabilities.isCreativeMode)
			this.dropBlockAsItem_do(world, x, y, z, stack);
		
		return flag;
	}
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        TileEntity tile = world.getBlockTileEntity(x, y, z);
		
		if(!(tile instanceof TileEntityDogBed))
			return null;
		TileEntityDogBed dogBed = (TileEntityDogBed)tile;
		
		return this.createItemStack(dogBed.getWoodId(), dogBed.getWoolId());
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister) {
		
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
	public int getRenderType() {
	    return DoggyTalentsMod.proxy.RENDER_ID_DOG_BED;
	}
	
	@Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        if (!super.canPlaceBlockAt(world, x, y, z)) {
            return false;
        }
        else {
            return canBlockStay(world, x, y, z);
        }
    }
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int side) {
		if (!this.canBlockStay(world, x, y, z)) {
			removeBlockByPlayer(world, null, x, y, z);
	    }
	}

	@Override
	public boolean canBlockStay(World world, int x, int y, int z) {
		Block block = Block.blocksList[world.getBlockId(x, y - 1, z)];
		return block != null && block.isBlockSolidOnSide(world, x, y - 1, z, ForgeDirection.UP);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(int id, CreativeTabs creativeTab, List stackList) {
		for(String woodId : DogBedManager.getAllWoodIds()) {
			for(String woolId : DogBedManager.getAllWoolIds()) {
				stackList.add(createItemStack(woodId, woolId));
			}
		}
    }
	
	public ItemStack createItemStack(String woodId, String woolId) {
		ItemStack stack = new ItemStack(ModBlocks.dogBed.blockID, 1, 0);
		stack.stackTagCompound = new NBTTagCompound();
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("woodId", woodId);
		tag.setString("woolId", woolId);
		stack.stackTagCompound.setCompoundTag("doggytalents", tag);
		return stack;
	}
}
