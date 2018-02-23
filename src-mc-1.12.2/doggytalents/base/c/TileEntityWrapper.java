package doggytalents.base.c;

import doggytalents.tileentity.TileEntityDogBed;
import doggytalents.tileentity.TileEntityFoodBowl;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;

public class TileEntityWrapper {

	public static class TileEntityDogBedWrapper extends TileEntityDogBed {
		
		@Override
		public NBTTagCompound writeToNBT(NBTTagCompound tag) {
			return this.writeToNBTGENERAL(tag);
		}
		
		@Override
		public SPacketUpdateTileEntity getUpdatePacket() {
			return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
		}
		
		@Override
		public void handleUpdateTag(NBTTagCompound tag) {
			super.handleUpdateTag(tag);
		}


		@Override
		public NBTTagCompound getUpdateTag() {
			return writeToNBT(new NBTTagCompound());
		}
		
		@Override
		public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
			this.readFromNBT(pkt.getNbtCompound());
		}
	}
	
	public static class TileEntityFoodBowlWrapper extends TileEntityFoodBowl {
		
		@Override
		public NBTTagCompound writeToNBT(NBTTagCompound tag) {
			return this.writeToNBTGENERAL(tag);
		}
	}
}
