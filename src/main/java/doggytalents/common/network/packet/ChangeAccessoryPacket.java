package doggytalents.common.network.packet;

import java.util.function.Supplier;

import doggytalents.api.registry.Accessory;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.item.AccessoryItem;
import doggytalents.common.network.packet.data.ChangeAccessoriesData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent.Context;

public class ChangeAccessoryPacket extends DogPacket<ChangeAccessoriesData> {

    @Override
    public void encode(ChangeAccessoriesData data, FriendlyByteBuf buf) {
        buf.writeInt(data.entityId);
        buf.writeBoolean(data.add);
        buf.writeInt(data.slotId);
    }

    @Override
    public ChangeAccessoriesData decode(FriendlyByteBuf buf) {
        int dog_id = buf.readInt();
        boolean add =buf.readBoolean();
        int slot_id = buf.readInt();
        return new ChangeAccessoriesData(dog_id, add, slot_id);
    }

    @Override
    public void handleDog(DogEntity dog, ChangeAccessoriesData data, Supplier<Context> ctx) {
        var sender = ctx.get().getSender();
        if (data.add) {
            var inventory = sender.getInventory();
            var items = inventory.items;
            if (data.slotId >= items.size()) return;
            var item = items.get(data.slotId);
            if (item.getItem() instanceof AccessoryItem accessoryItem) {
                var inst = accessoryItem.createInstance(dog, item, sender);
                if (inst == null) return;
                if (dog.addAccessory(inst)) {
                    dog.consumeItemFromStack(dog, item);
                }
                
                
            }
        } else {
            var accessories = dog.getAccessories();
            if (data.slotId >= accessories.size()) return;
            var toRemove = accessories.get(data.slotId);
            if (toRemove == null) return;
            var inventory = sender.getInventory();
            var retItem = toRemove.getReturnItem();
            if (retItem == null) return;
            if (inventory.getFreeSlot() < 0) return;

            inventory.add(toRemove.getReturnItem());
            accessories.remove(toRemove);
            dog.markAccessoriesDirty();
            
        }
    }
    
}
