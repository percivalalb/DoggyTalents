package doggytalents.base.b;

import doggytalents.tileentity.TileEntityDogBed;
import doggytalents.tileentity.TileEntityFoodBowl;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;

public class TileEntityWrapper {

	public static class TileEntityDogBedWrapper extends TileEntityDogBed {
		
		@Override
		public void writeToNBT(NBTTagCompound tag) {
			this.writeToNBTGENERAL(tag);
		}
		
		@Override
		public Packet getDescriptionPacket() {
			NBTTagCompound tagCompound = new NBTTagCompound();
			this.writeToNBT(tagCompound);
			return new S35PacketUpdateTileEntity(this.pos, 0, tagCompound);
		}

		@Override
		public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
			this.readFromNBT(pkt.getNbtCompound());
		}
	}
	
	public static class TileEntityFoodBowlWrapper extends TileEntityFoodBowl {
		
		@Override
		public void writeToNBT(NBTTagCompound tag) {
			this.writeToNBTGENERAL(tag);
		}
	}
}
