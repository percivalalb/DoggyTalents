package doggytalents.block;

import doggytalents.base.ObjectLib;
import doggytalents.base.VersionControl;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 */
public abstract class BlockDogBed extends BlockContainer {
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyString CASING = PropertyString.create("casing");
	public static final PropertyString BEDDING = PropertyString.create("bedding");
	
	public BlockDogBed() {
		super(Material.WOOD);
		this.setHardness(3.0F);
		this.setResistance(5.0F);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return VersionControl.createObject(ObjectLib.TILE_DOG_BED_CLASS);
	}
}
