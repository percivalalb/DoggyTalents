package doggytalents.test.doggytalents.common.util;

import static org.junit.jupiter.api.Assertions.*;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.IRegistryDelegate;
import org.junit.jupiter.api.Test;

import static doggytalents.common.util.Util.*;
import static org.junit.jupiter.api.Assertions.*;

public class UtilTest {

    @Test
    public void testGetRegistryId() {
        ResourceLocation dirtRL = new ResourceLocation("minecraft:dirt");
        assertEquals(dirtRL, getRegistryId("minecraft:dirt"));
        assertEquals(dirtRL, getRegistryId(dirtRL));
        assertEquals(dirtRL, getRegistryId(RegistryObject.of(dirtRL, Block.class, "dummy_mod")));

        assertEquals(dirtRL, getRegistryId(new IRegistryDelegate<Block>() {
            @Override public ResourceLocation name() { return dirtRL; }
            // Methods should never be called
            @Override public Class type() { return Block.class; }
            @Override public Block get() { return null; }
        }));

        assertEquals(dirtRL, getRegistryId(new IForgeRegistryEntry<Block>() {
            @Override public ResourceLocation getRegistryName() { return dirtRL;  }
            // Methods should never be called
            @Override public Class<Block> getRegistryType() { return Block.class; }
            @Override public Block setRegistryName(ResourceLocation name) { return null; }
        }));

        assertNull(getRegistryId("name:space:invalid")); // invalid rl's should return null
        assertNull(getRegistryId(Integer.valueOf(0))); // other object types too
    }
}
