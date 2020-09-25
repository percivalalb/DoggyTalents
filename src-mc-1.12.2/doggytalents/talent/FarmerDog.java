package doggytalents.talent;

import java.util.List;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.inferface.ITalent;
import doggytalents.entity.EntityDog;
import doggytalents.entity.ModeUtil;
import doggytalents.helper.DogUtil;
import doggytalents.inventory.InventoryPackPuppy;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

/**
 * @author ProPercivalalb
 */
public class FarmerDog extends ITalent {
	
	protected int runDelayFarm;
	
	private ItemStack findSeed(InventoryPackPuppy inventory)
    {
        for (int i = 0; i < inventory.getSizeInventory(); ++i)
        {
            ItemStack itemstack = inventory.getStackInSlot(i);

            if (itemstack.getItem() instanceof IPlantable)
            {
                return itemstack;
            }
        }

        return ItemStack.EMPTY;
    }

	@Override
	public void onUpdate(EntityDog dog) {
		int level = dog.talents.getLevel(this);
		World world = dog.world;
		InventoryPackPuppy inventory = (InventoryPackPuppy)dog.objects.get("packpuppyinventory");
		
		if (this.runDelayFarm > 0)
        {
			--this.runDelayFarm;
        }
		
		if(dog.isTamed() && !dog.isSitting() && level > 0 && dog.getHealth() > 1 && this.runDelayFarm < 1) {
			if(dog.masterOrder() == 4 || dog.mode.isMode(ModeUtil.EnumMode.WANDERING)) {
				//checkear bloques
				for(int x = -3; x < 4; x++){
	    			for(int z = -3; z < 4; z++){
	    				if(x != -2 || x != -1 || x != 0 || x != 1 || x != 2 || z != -2 || z != -1 || z != 0 || z != 1 || z != 2){
	    					BlockPos pos = new BlockPos(dog.getPosition().getX()+x, dog.getPosition().getY(), dog.getPosition().getZ()+z);
	    					IBlockState state = world.getBlockState(pos);
	    					
	    					//romper bloques
	    					if(state.getBlock() instanceof BlockCrops && ((BlockCrops)state.getBlock()).isMaxAge(state)) {
	    						//nivel 1: solo puede romper trigo
		    					if(level < 2) {
		    						if(state.getBlock() == Blocks.WHEAT) {
		    							dog.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), 1.0D);
			    						if(dog.getPosition().getX() == pos.getX() && dog.getPosition().getY() == pos.getY() && dog.getPosition().getZ() == pos.getZ()) {
			    							world.destroyBlock(pos, true);
			    							this.runDelayFarm = 1500/level;
			    						}
		    						}    							
		    					} 
		    					//para niveles mayores a 1
		    					//nivel 2: puede romper cualquier crop
		    					else {
		    						//esta en posicion?
		    						dog.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), 1.0D);
		    						if(dog.getPosition().getX() == pos.getX() && dog.getPosition().getY() == pos.getY() && dog.getPosition().getZ() == pos.getZ()) {	    							
		    							world.destroyBlock(pos, true);
		    							this.runDelayFarm = 1500/level;
		    						}
		    					}				
	    					}
	    					//nivel 3: puede romper sandias y calabazas
	    					if(state.getBlock() == Blocks.MELON_BLOCK || state.getBlock() == Blocks.PUMPKIN) {
	    						if(level > 2) {
	    							//esta en posicion?
		    						dog.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), 1.0D);
		    						if(dog.getPosition().getX() == pos.getX() && dog.getPosition().getY() == pos.getY() && dog.getPosition().getZ() == pos.getZ()) {	    							
		    							world.destroyBlock(pos, true);
		    							this.runDelayFarm = 1500/level;
		    						}
	    						}
	    					}
	    					
	    					BlockPos pos2 = new BlockPos(dog.getPosition().getX()+x, dog.getPosition().getY()+1, dog.getPosition().getZ()+z);
	    					IBlockState state2 = world.getBlockState(pos2);
	    					
	    					//nivel 4: puede romper cañas de azucar
	    					if(state.getBlock() == Blocks.REEDS && state2.getBlock() == Blocks.REEDS) {
	    						if(level > 3) {
	    							//esta en posicion?
		    						dog.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), 1.0D);
		    						if(dog.getPosition().getX() == pos.getX() && dog.getPosition().getY() == pos.getY() && dog.getPosition().getZ() == pos.getZ()) {	    							
		    							world.destroyBlock(pos2, true);
		    							this.runDelayFarm = 1500/level;
		    						}
	    						}
	    					}    					
	    					
	    					BlockPos pos1 = new BlockPos(dog.getPosition().getX()+x, dog.getPosition().getY()-1, dog.getPosition().getZ()+z);
	    					IBlockState state1 = world.getBlockState(pos1);
	    					
	    					//poner bloques
	    					ItemStack seedStack = this.findSeed(inventory);
	    					
	    					if(state.getMaterial() == Material.AIR && state1.getBlock() == Blocks.FARMLAND && level > 4) {
								//nivel 5: si tiene semillas en el inventario va a replantar
								if(!seedStack.isEmpty()) {
		                            if(((IPlantable)seedStack.getItem()).getPlantType(world,pos) == EnumPlantType.Crop) {
		                            	//esta en posicion?
			    						dog.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), 1.0D);
			    						if(dog.getPosition().getX() == pos.getX() && dog.getPosition().getY() == pos.getY() && dog.getPosition().getZ() == pos.getZ()) {
			    							world.setBlockState(pos, ((IPlantable)seedStack.getItem()).getPlant(world,pos),3);
	    	                                seedStack.shrink(1);
	    	                                this.runDelayFarm = 1500/level;
	    	                                
	    	                                if(seedStack.isEmpty()) {
	    	                                	inventory.addItem(ItemStack.EMPTY);
	    	                                }	    	                                
		    	                        }
				                    }
	    						}
	    					}
	    					
	    				}
	    			}
				}			
    		}
		}    	
	}	
	
	@Override
	public void onLivingUpdate(EntityDog dog) {
		int level = dog.talents.getLevel(this);
		InventoryPackPuppy inventory = (InventoryPackPuppy)dog.objects.get("packpuppyinventory");
		if(dog.isTamed() && level > 2 && dog.getHealth() > 1) {
			List<EntityItem> list = dog.world.getEntitiesWithinAABB(EntityItem.class, dog.getEntityBoundingBox().grow(1D, 1D, 1D));
	        for(EntityItem entityItem : list) {
	            if(entityItem.isDead || DoggyTalentsAPI.PACKPUPPY_BLACKLIST.containsItem(entityItem.getItem())) continue;
	            
	            if(entityItem.getItem().getItem() instanceof IPlantable) {
		            ItemStack itemstack1 = DogUtil.addItem(inventory, entityItem.getItem());
		            if(!itemstack1.isEmpty())
		            	entityItem.setItem(itemstack1);
		            else {
		                entityItem.setDead();
		                dog.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.25F, ((dog.world.rand.nextFloat() - dog.world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
		            }
	            }		            
	        }
		}
	}
	
	@Override
	public String getKey() {
		return "farmerdog";
	}

}
