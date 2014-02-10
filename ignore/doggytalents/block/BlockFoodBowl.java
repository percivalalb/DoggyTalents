package doggytalents.block;

import java.util.List;
import java.util.Random;

import doggytalents.DoggyTalentsMod;
import doggytalents.entity.EntityDTDoggy;
import doggytalents.tileentity.TileEntityFoodBowl;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 * You may look at this file to gain knowledge of javascript
 * but must not copy any features or code directly.
 **/
public class BlockFoodBowl extends BlockContainer {
	
    Icon top;
    Icon side;
    Icon bottom;
	
    public BlockFoodBowl(int i) {
        super(i, Material.iron);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabFood);
    }

    /**
     * Updates the blocks bounds based on its current state. Args: par1World, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int i, int j, int k) {
        int var1 = par1IBlockAccess.getBlockMetadata(i, j, k);
        float var2 = 0.0625F;
        float var3 = (float)(1 + var1 * 2) / 16F;
        float var4 = 0.5F;
        setBlockBounds(var3, 0.0F, var2, 1.0F - var2, var4, 1.0F - var2);
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    public void setBlockBoundsForItemRender() {
        float var1 = 0.0625F;
        float var2 = 0.5F;
        this.setBlockBounds(var1, 0.0F, var1, 1.0F - var1, var2, 1.0F - var1);
    }

    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        List list = par1World.getEntitiesWithinAABB(EntityDTDoggy.class, AxisAlignedBB.getBoundingBox((float)par2, (double)(float)par3 + 0.5D, (float)par4, (float)(par2 + 1), (double)(float)par3 + 0.5D + 0.05000000074505806D, (float)(par4 + 1)).expand(5, 5, 5));

        if (list != null && list.size() > 0)
        {
            for (int l = 0; l < list.size(); l++)
            {
                EntityDTDoggy entitydtdoggy = (EntityDTDoggy)list.get(l);
                entitydtdoggy.bowlLocationX = par2;
                entitydtdoggy.bowlLocationY = par3;
                entitydtdoggy.bowlLocationZ = par4;
            }
        }
    }
    
    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int i, int j, int k)
    {
        int l = par1World.getBlockMetadata(i, j, k);
        float f = 0.0625F;
        float f1 = (float)(1 + l * 2) / 16F;
        float f2 = 0.5F;
        return AxisAlignedBB.getBoundingBox((float)i + f1, j, (float)k + f, (float)(i + 1) - f, ((float)j + f2) - f, (float)(k + 1) - f);
    }

    /**
     * Returns the bounding box of the wired rectangular prism to render.
     */
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        int var1 = par1World.getBlockMetadata(par2, par3, par4);
        float var2 = 0.0625F;
        float var3 = (float)(1 + var1 * 2) / 16F;
        float var4 = 0.5F;
        return AxisAlignedBB.getBoundingBox((float)par2 + var3, par3, (float)par4 + var2, (float)(par2 + 1) - var2, (float)par3 + var4, (float)(par4 + 1) - var2);
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
    	return par1 == 1 ? this.top : par1 == 0 ? this.bottom : this.side;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * Called upon block activation (left or right click on the block.). The three integers represent x,y,z of the
     * block.
     */
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        if (par1World.isRemote)
        {
            return true;
        }
        else
        {
            TileEntityFoodBowl tileentitydogfoodbowl = (TileEntityFoodBowl)par1World.getBlockTileEntity(par2, par3, par4);
            par5EntityPlayer.openGui(DoggyTalentsMod.instance, 3, par1World, par2, par3, par4);
            return true;
        }
    }
    
    /**
     * Triggered whenever an entity collides with this block (enters into the block). Args: par1World, x, y, z, entity
     */
    public void onEntityCollidedWithBlock(World par1World, int i, int j, int k, Entity entity)
    {
        List list = null;
        list = par1World.getEntitiesWithinAABB(EntityDTDoggy.class, AxisAlignedBB.getBoundingBox((float)i, (double)(float)j + 0.5D, (float)k, (float)(i + 1), (double)(float)j + 0.5D + 0.05000000074505806D, (float)(k + 1)));

        if (list != null && list.size() > 0)
        {
            for (int l = 0; l < list.size(); l++)
            {
                EntityDTDoggy entitydtdoggy = (EntityDTDoggy)list.get(l);
                entitydtdoggy.bowlLocationX = i;
                entitydtdoggy.bowlLocationY = j;
                entitydtdoggy.bowlLocationZ = k;
            }
        }
        
        if (entity != null && entity instanceof EntityItem)
        {
            TileEntity tileentity = par1World.getBlockTileEntity(i, j, k);

            if (!(tileentity instanceof TileEntityFoodBowl))
            {
                return;
            }

            TileEntityFoodBowl tileentitydogfoodbowl = (TileEntityFoodBowl)tileentity;

                EntityItem entityitem = (EntityItem)entity;

                if (entityitem.isDead)
                {
                    return;
                }

                if (tileentitydogfoodbowl.AddItemStackToInventory(entityitem.getEntityItem()))
                {
                    par1World.playSoundEffect((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.pop", 0.25F, ((par1World.rand.nextFloat() - par1World.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    entityitem.setDead();
                    return;
                }

                int k1 = par1World.getBlockId(i, j + 1, k);

                if (k1 != Block.waterMoving.blockID && k1 != Block.waterStill.blockID)
                {
                    return;
                }

                double d = (double)j + 1.05D;

                if (entityitem.boundingBox.minY < d)
                {
                    double d1 = d - entityitem.boundingBox.minY;
                    entityitem.setPosition(entityitem.posX, entityitem.posY + d1, entityitem.posZ);
                }
            
        }

        List list2 = null;
        list2 = par1World.getEntitiesWithinAABB(EntityDTDoggy.class, AxisAlignedBB.getBoundingBox((float)i, (double)(float)j + 0.5D, (float)k, (float)(i + 1), (double)(float)j + 0.5D + 0.05000000074505806D, (float)(k + 1)));

        if (list2 != null && list2.size() > 0)
        {
            TileEntity tileentity1 = par1World.getBlockTileEntity(i, j, k);

            if (!(tileentity1 instanceof TileEntityFoodBowl))
            {
                return;
            }

            TileEntityFoodBowl tileentitydogfoodbowl1 = (TileEntityFoodBowl)tileentity1;

            for (int j1 = 0; j1 < list2.size(); j1++)
            {
                EntityDTDoggy entitydtdoggy1 = (EntityDTDoggy)list2.get(j1);

                if (entitydtdoggy1.getWolfTummy() <= 60 && tileentitydogfoodbowl1.GetFirstDogFoodStack(entitydtdoggy1) >= 0)
                {
                    tileentitydogfoodbowl1.FeedDog(entitydtdoggy1, tileentitydogfoodbowl1.GetFirstDogFoodStack(entitydtdoggy1), 1);
                }
            }
        }
    }

    /**
     * Returns the TileEntity used by this block.
     */
    public TileEntity createNewTileEntity(World var1)
    {
        return new TileEntityFoodBowl();
    }
    
    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: par1World, x, y, z
     */
    public boolean canPlaceBlockAt(World par1World, int i, int j, int k)
    {
        if (!super.canPlaceBlockAt(par1World, i, j, k))
        {
            return false;
        }
        else
        {
            return canBlockStay(par1World, i, j, k);
        }
    }

    /**
     * Called whenever the block is removed.
     */
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        TileEntityFoodBowl tileentitydogfoodbowl = (TileEntityFoodBowl)par1World.getBlockTileEntity(par2, par3, par4);
        tileentitydogfoodbowl.DropContents(par1World, par2, par3, par4);
        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }
    
    public void func_94332_a(IconRegister par1IconRegister) {
        this.top = par1IconRegister.func_94245_a("dt_foodTop");
        this.bottom = par1IconRegister.func_94245_a("dt_foodBottom");
        this.side = par1IconRegister.func_94245_a("dt_foodSide");
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        if (!this.canBlockStay(par1World, par2, par3, par4))
        {
            par1World.func_94571_i(par2, par3, par4);
        }
    }

    /**
     * Can this block stay at this position.  Similar to canPlaceBlockAt except gets checked often with plants.
     */
    public boolean canBlockStay(World par1World, int par2, int par3, int par4)
    {
        return par1World.getBlockMaterial(par2, par3 - 1, par4).isSolid();
    }
}
