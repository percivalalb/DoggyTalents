package doggytalents.block;

import doggytalents.DoggyTalents;
import doggytalents.tileentity.TileEntityDogBath;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**;
 * @author ProPercivalalb
 */
public abstract class BlockDogBath extends BlockContainer {

	public BlockDogBath() {
		super(Material.IRON);
		this.setHardness(3.0F);
		this.setResistance(5.0F);
		this.setCreativeTab(DoggyTalents.CREATIVE_TAB);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityDogBath();
	}
}
