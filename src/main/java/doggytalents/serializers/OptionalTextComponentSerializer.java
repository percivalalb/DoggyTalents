package doggytalents.serializers;

import java.io.IOException;

import com.google.common.base.Optional;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.util.text.ITextComponent;

public class OptionalTextComponentSerializer implements DataSerializer<Optional<ITextComponent>> {
    
    @Override
    public void write(PacketBuffer buf, Optional<ITextComponent> value) {
        if(value.isPresent()) {
            buf.writeBoolean(true);
            buf.writeTextComponent(value.get());
        } else {
            buf.writeBoolean(false);
        }
    }

    @Override
    public Optional<ITextComponent> read(PacketBuffer buf) throws IOException {
         return buf.readBoolean() ? Optional.of(buf.readTextComponent()) : Optional.absent();
      }

    @Override
    public DataParameter<Optional<ITextComponent>> createKey(int id) {
        return new DataParameter<>(id, this);
    }

    @Override
    public Optional<ITextComponent> copyValue(Optional<ITextComponent> value) {
        return value.isPresent() ? Optional.of(value.get().createCopy()) : Optional.absent();
    }
}
