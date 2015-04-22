package doggytalents.tileentity;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import doggytalents.entity.EntityDog;
import doggytalents.network.packet.PacketDogBedUpdate;

/**
 * @author ProPercivalalb
 */
public class TileEntityDogBed extends TileEntity implements IUpdatePlayerListBox {

	private String casingId;
	private String beddingId;
	
	public TileEntityDogBed() {
		this.casingId = "";
		this.beddingId = "";
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		this.casingId = tag.getString("casingId");
		this.beddingId = tag.getString("beddingId");
    }

	@Override
    public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setString("casingId", this.casingId);
		tag.setString("beddingId", this.beddingId);
	}
	
	@Override
	public void update() {
		List dogs = this.worldObj.getEntitiesWithinAABB(EntityDog.class, new AxisAlignedBB(this.pos.getX(), this.pos.getY(), this.pos.getZ(), this.pos.getX() + 1.0D, this.pos.getY() + 1.0D, this.pos.getZ() + 1.0D).expand(3D, 2D, 3D));
		 
	    if (dogs != null && dogs.size() > 0) {
	    	for (int index = 0; index < dogs.size(); index++) {
	            EntityDog dog = (EntityDog)dogs.get(index);
	            
	            if(dog.getMaxHealth() / 2 >= dog.getHealth()) {
	            	//if(dog.bedHealTick <= 0) {
	            		dog.heal(0.5F);
	            		//dog.bedHealTick = 20 * 20;
	            	//}
	            	//dog.bedHealTick--;
        		}
	        }
	    }

	}
	
	@Override
    public Packet getDescriptionPacket() {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
	    this.writeToNBT(nbttagcompound);
	    return new S35PacketUpdateTileEntity(this.pos, this.getBlockMetadata(), nbttagcompound);
        //return new PacketDogBedUpdate(this.pos, this.casingId, this.beddingId).getPacket();
    }
	
	@Override
	public void onDataPacket(net.minecraft.network.NetworkManager net, net.minecraft.network.play.server.S35PacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.getNbtCompound());
	}
	
	public void setCasingId(String newId) {
		this.casingId = newId;
	}
	
	public void setBeddingId(String newId) {
		this.beddingId = newId;
	}
	
	public String getCasingId() {
		return this.casingId;
	}
	
	public String getBeddingId() {
		return this.beddingId;
	}
}
