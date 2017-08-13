package doggytalents.base.b;

import doggytalents.DoggyTalents;
import doggytalents.ModItems;
import doggytalents.api.registry.DogBedRegistry;
import doggytalents.block.BlockDogBath;
import doggytalents.block.BlockDogBed;
import doggytalents.block.BlockFoodBowl;
import doggytalents.block.PropertyString;
import doggytalents.entity.EntityDog;
import doggytalents.helper.DogUtil;
import doggytalents.inventory.InventoryTreatBag;
import doggytalents.proxy.CommonProxy;
import doggytalents.tileentity.TileEntityDogBed;
import doggytalents.tileentity.TileEntityFoodBowl;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockWrapper {

	public static final PropertyStringListed VERSION = PropertyStringListed.create("version");
	
	public static class BlockDogBathWrapper extends BlockDogBath {
		
		public BlockDogBathWrapper() {
			super();
			this.setStepSound(Block.soundTypeMetal);
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
			this.setDefaultState(this.blockState.getBaseState().withProperty(VERSION, "1.8"));
		}
		
		@Override
		protected BlockState createBlockState() {
	        return new BlockState(this, new IProperty[] {VERSION});
	    }
		
		@Override
		public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
			return state.withProperty(VERSION, "1.8");
		}
		
		@Override
		public int getMetaFromState(IBlockState state) {
	        return 0;
	    }
		
		@Override
		public int getRenderType() {
		    return 3;
		}
		
		@Override
		public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
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
		public boolean isFullCube() {
		    return false;
		}
		
		@Override
		public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		    return super.canPlaceBlockAt(worldIn, pos) ? this.canBlockStay(worldIn, pos) : false;
		}

		@Override
		public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
			if(!this.canBlockStay((World)world, pos)) {
				this.dropBlockAsItem((World)world, pos, world.getBlockState(pos), 0);
				((World)world).setBlockToAir(pos);
			}
		}
		
		public boolean canBlockStay(World world, BlockPos pos) {
			IBlockState blockstate = world.getBlockState(pos.down());
			return blockstate.getBlock().isSideSolid(world, pos.down(), EnumFacing.UP);
		}
	}
	
	public static class BlockDogBedWrapper extends BlockDogBed {
		
		public BlockDogBedWrapper() {
			super();
			this.setStepSound(Block.soundTypeWood);
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.6F, 1.0F);
			this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(VERSION, "1.8"));
		}
		
		@Override
		public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
			return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
		}
		
		@Override
	    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
	        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
		    
	        if(stack != null && stack.hasTagCompound() && stack.getTagCompound().hasKey("doggytalents")) {
		    	NBTTagCompound tag = stack.getTagCompound().getCompoundTag("doggytalents");
		    	
		    	String casingId = tag.getString("casingId");
		    	if(DogBedRegistry.CASINGS.isValidId(casingId)) 
		    		((TileEntityDogBed)worldIn.getTileEntity(pos)).setCasingId(casingId);
		    	
		    	String beddingId = tag.getString("beddingId");
		    	if(DogBedRegistry.BEDDINGS.isValidId(beddingId))
		    		((TileEntityDogBed)worldIn.getTileEntity(pos)).setBeddingId(beddingId);
		    }
		}
		
		@SideOnly(Side.CLIENT)
		public IBlockState getStateForEntityRender(IBlockState state) {
		    return this.getDefaultState().withProperty(FACING, EnumFacing.SOUTH);
		}

		@Override
		public IBlockState getStateFromMeta(int meta) {
			EnumFacing enumfacing = EnumFacing.getFront(meta);

		    if (enumfacing.getAxis() == EnumFacing.Axis.Y)
		        enumfacing = EnumFacing.NORTH;

		    return this.getDefaultState().withProperty(FACING, enumfacing);
		}

		@Override
		public int getMetaFromState(IBlockState state) {
		    return ((EnumFacing)state.getValue(FACING)).getIndex();
		}
		
		@Override
		protected BlockState createBlockState() {
			return new ExtendedBlockState(this, new IProperty[] {FACING, VERSION}, new IUnlistedProperty[] {CASING, BEDDING});
		}
		
		@Override
	    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
	        TileEntity te = world.getTileEntity(pos);
	        if(te instanceof TileEntityDogBed && state instanceof IExtendedBlockState) {
	        	IExtendedBlockState stateExtended = (IExtendedBlockState)state;
	        	TileEntityDogBed dogBed = (TileEntityDogBed) te;
	            return stateExtended.withProperty(CASING, dogBed.getCasingId()).withProperty(BEDDING, dogBed.getBeddingId());
	        }
	        return super.getExtendedState(state, world, pos);
	    }
		
		@Override
		public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
			return state.withProperty(VERSION, "1.8");
		}
		
		@SideOnly(Side.CLIENT)
	    public int getMixedBrightnessForBlock(IBlockAccess worldIn, BlockPos pos) {
	        return worldIn.getCombinedLight(pos, 0);
	    }
		
		@Override
		public int getRenderType() {
			return 3;
		}
		
		@Override
		public boolean isFullCube() {
		    return false;
		}

		@Override
		public boolean isOpaqueCube() {
		    return false;
		}
		
		public final ThreadLocal<ItemStack> drops = new ThreadLocal<>();

		@Override
		public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn) {
			TileEntity tileentity = worldIn.getTileEntity(pos);

			if(tileentity instanceof TileEntityDogBed) {
				TileEntityDogBed dogBed = (TileEntityDogBed)tileentity;
				if(!playerIn.capabilities.isCreativeMode)
					this.drops.set(DogBedRegistry.createItemStack(dogBed.getCasingId(), dogBed.getBeddingId()));
			}
		}
		
		@Override
		public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos) {
	        TileEntity tile = world.getTileEntity(pos);
			
			if(!(tile instanceof TileEntityDogBed))
				return null;
			TileEntityDogBed dogBed = (TileEntityDogBed)tile;
			
			return DogBedRegistry.createItemStack(dogBed.getCasingId(), dogBed.getBeddingId());
		}
		
		@Override
		public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		    return super.canPlaceBlockAt(worldIn, pos) ? this.canBlockStay(worldIn, pos) : false;
		}

		@Override
		public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
			if(!this.canBlockStay((World)world, pos)) {
			    TileEntity tile = world.getTileEntity(pos);
				if(tile instanceof TileEntityDogBed) {
						
					TileEntityDogBed dogBed = (TileEntityDogBed)tile;
					
			        this.spawnAsEntity((World)world, pos, DogBedRegistry.createItemStack(dogBed.getCasingId(), dogBed.getBeddingId()));
			        ((World) world).setBlockToAir(pos);
				}
			}
		}

		public boolean canBlockStay(World world, BlockPos pos) {
			IBlockState blockstate = world.getBlockState(pos.down());
			return blockstate.getBlock().isSideSolid(world, pos.down(), EnumFacing.UP);
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public boolean addDestroyEffects(World world, BlockPos pos, EffectRenderer manager) {
			IBlockState state = world.getBlockState(pos);
			IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelFromBlockState(state, world, pos);
				TextureAtlasSprite sprite = model.getParticleTexture();
				if(sprite != null) {
					for(int j = 0; j < 4; ++j) {
						for(int k = 0; k < 4; ++k) {
							for(int l = 0; l < 4; ++l) {
								double d0 = ((double)j + 0.5D) / 4.0D;
								double d1 = ((double)k + 0.5D) / 4.0D;
								double d2 = ((double)l + 0.5D) / 4.0D;
								manager.addEffect(new ParticleCustomDigging(world, (double)pos.getX() + d0, (double)pos.getY() + d1, (double)pos.getZ() + d2, d0 - 0.5D, d1 - 0.5D, d2 - 0.5D, state, pos, sprite));
							}
						}
					}

					return true;
				}
			

			return false;
		}

		@Override
		@SideOnly(Side.CLIENT)
		public boolean addHitEffects(World world, MovingObjectPosition target, EffectRenderer manager) {
			IBlockState state = world.getBlockState(target.getBlockPos());
			IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelFromBlockState(state, world, target.getBlockPos());
				BlockPos pos = target.getBlockPos();
				EnumFacing side = target.sideHit;
				
				TextureAtlasSprite sprite = model.getParticleTexture();
				if(sprite != null) {
					int x = pos.getX();
					int y = pos.getY();
					int z = pos.getZ();
					 float f = 0.1F;
					double d0 = (double)x + RANDOM.nextDouble() * (this.getBlockBoundsMaxX() - this.getBlockBoundsMinX() - (double)(f * 2.0F)) + (double)f + this.getBlockBoundsMinX();
		            double d1 = (double)y + RANDOM.nextDouble() * (this.getBlockBoundsMaxY() - this.getBlockBoundsMinY() - (double)(f * 2.0F)) + (double)f + this.getBlockBoundsMinY();
		            double d2 = (double)z + RANDOM.nextDouble() * (this.getBlockBoundsMaxZ() - this.getBlockBoundsMinZ() - (double)(f * 2.0F)) + (double)f + this.getBlockBoundsMinZ();

		            if (side == EnumFacing.DOWN)
		            {
		                d1 = (double)y + this.getBlockBoundsMinY() - (double)f;
		            }

		            if (side == EnumFacing.UP)
		            {
		                d1 = (double)y + this.getBlockBoundsMaxY() + (double)f;
		            }

		            if (side == EnumFacing.NORTH)
		            {
		                d2 = (double)z + this.getBlockBoundsMinZ() - (double)f;
		            }

		            if (side == EnumFacing.SOUTH)
		            {
		                d2 = (double)z + this.getBlockBoundsMaxZ() + (double)f;
		            }

		            if (side == EnumFacing.WEST)
		            {
		                d0 = (double)x + this.getBlockBoundsMinX() - (double)f;
		            }

		            if (side == EnumFacing.EAST)
		            {
		                d0 = (double)x + this.getBlockBoundsMaxX() + (double)f;
		            }

					EntityFX particle = new ParticleCustomDigging(world, d0, d1, d2, 0.0D, 0.0D, 0.0D, state, pos, sprite).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F);
					manager.addEffect(particle);

					return true;
				}
			

			return false;
		}
	}
	
	public static class BlockFoodBowlWrapper extends BlockFoodBowl {
		
	    public BlockFoodBowlWrapper() {
	        super();
			this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 1.0F - 0.0625F, 0.5F, 1.0F - 0.0625F);
			this.setDefaultState(this.blockState.getBaseState().withProperty(VERSION, "1.8"));
	    }
	    
		@Override
		protected BlockState createBlockState() {
	        return new BlockState(this, new IProperty[] {VERSION});
	    }
	    
	    @Override
		public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
			return state.withProperty(VERSION, "1.8");
		}
	    
	    @Override
		public int getMetaFromState(IBlockState state) {
	        return 0;
	    }
	    
	    @Override
	    public int getRenderType() {
	        return 3;
	    }
	    
		@Override
		public boolean isFullCube() {
		    return false;
		}

		@Override
		public boolean isOpaqueCube() {
		    return false;
		}

		@Override
	    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing facing, float hitX, float hitY, float hitZ) {
	        if(worldIn.isRemote) {
	            return true;
	        }
	        else {
	        	ItemStack stack = playerIn.getHeldItem();
	        	
	        	if(stack != null && stack.getItem() == ModItems.TREAT_BAG) {
	        		TileEntityFoodBowl tileentitydogfoodbowl = (TileEntityFoodBowl)worldIn.getTileEntity(pos);
	        		InventoryTreatBag treatBag = new InventoryTreatBag(playerIn, playerIn.inventory.currentItem, stack);
	        		treatBag.openInventory(playerIn);
	        		
	        		for(int i = 0; i < treatBag.getSizeInventory(); i++)
	        			treatBag.setInventorySlotContents(i, this.addItem(tileentitydogfoodbowl.inventory, treatBag.getStackInSlot(i)));
	        		
	        		treatBag.closeInventory(playerIn);
	        		
	        		return true;
	        	}
	        	else {
		            TileEntityFoodBowl tileentitydogfoodbowl = (TileEntityFoodBowl)worldIn.getTileEntity(pos);
		            playerIn.openGui(DoggyTalents.INSTANCE, CommonProxy.GUI_ID_FOOD_BOWL, worldIn, pos.getX(), pos.getY(), pos.getZ());
		            return true;
	        	}
	        }
	    }
	    
	    @Override
	    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
	    	TileEntityFoodBowl foodBowl = (TileEntityFoodBowl) worldIn.getTileEntity(pos);
	        
	        if(entityIn instanceof EntityItem) {
	            EntityItem entityItem = (EntityItem)entityIn;
	            ItemStack itemstack = entityItem.getItem().copy();
	            ItemStack itemstack1 = this.addItem(foodBowl.inventory, entityItem.getItem());

	            if(itemstack1 != null && itemstack1.stackSize != 0)
	            	entityItem.setItem(itemstack1);
	            else {
	                entityItem.setDead();
	                worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, "random.pop", 0.25F, ((worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
	            }
	        }
	    }

	    @Override
		public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		    return super.canPlaceBlockAt(worldIn, pos) ? this.canBlockStay(worldIn, pos) : false;
		}

		@Override
		public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
			if(!this.canBlockStay((World)world, pos)) {
				this.dropBlockAsItem((World)world, pos, world.getBlockState(pos), 0);
				((World)world).setBlockToAir(pos);
			}
		}

		public boolean canBlockStay(World world, BlockPos pos) {
			IBlockState blockstate = world.getBlockState(pos.down());
			return blockstate.getBlock().isSideSolid(world, pos.down(), EnumFacing.UP);
		}
		
		@Override
		public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
			TileEntity tileentity = worldIn.getTileEntity(pos);

			if(tileentity instanceof TileEntityFoodBowl) {
				InventoryHelper.dropInventoryItems(worldIn, pos, ((TileEntityFoodBowl)tileentity).inventory);
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
			return Container.calcRedstoneFromInventory(((TileEntityFoodBowl)worldIn.getTileEntity(pos)).inventory);
		}
		
		public ItemStack addItem(IInventory inventory, ItemStack stack) {
	    	if(stack == null) return null;
	    	
	        ItemStack itemstack = stack.copy();
	        
	        

	        for (int i = 0; i < inventory.getSizeInventory(); ++i) {
	            ItemStack itemstack1 = inventory.getStackInSlot(i);

	            if (itemstack1 == null) {
	            	inventory.setInventorySlotContents(i, itemstack);
	            	inventory.markDirty();
	                return null;
	            }

	            if (ItemStack.areItemStacksEqual(itemstack1, itemstack)) {
	                int j = Math.min(inventory.getInventoryStackLimit(), itemstack1.getMaxStackSize());
	                int k = Math.min(itemstack.stackSize, j - itemstack1.stackSize);

	                if (k > 0) {
	                    itemstack1.stackSize += k;
	                    itemstack.stackSize -= k;

	                    if (itemstack.stackSize == 0) {
	                    	inventory.markDirty();
	                        return null;
	                    }
	                }
	            }
	        }

	        if(itemstack.stackSize != stack.stackSize)
	        	inventory.markDirty();

	        return itemstack;
	    }
	}
}
