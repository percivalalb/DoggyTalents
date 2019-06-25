package doggytalents.api.registry;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import doggytalents.DoggyTalents;
import doggytalents.ModBlocks;
import doggytalents.api.inferface.IBedMaterial;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

/**
 * @author ProPercivalalb
 */
public class DogBedRegistry {

    public final static DogBedRegistry CASINGS = new DogBedRegistry("casing");
    public final static DogBedRegistry BEDDINGS = new DogBedRegistry("bedding");
    
    private final List<IBedMaterial> REGISTRY = new ArrayList<IBedMaterial>();
    private final String key;
    
    public DogBedRegistry(String key) {
        this.key = key;
    }
    
    public IBedMaterial registerMaterial(@Nonnull Block block, int meta, ResourceLocation textureLocation) {
        return this.registerMaterial(new BedMaterial(block, meta, textureLocation, Ingredient.fromStacks(new ItemStack(block, 1, meta))));
    }
    
    public BedMaterial registerMaterial(BedMaterial material) {
        if(this.REGISTRY.contains(material)) {
            DoggyTalents.LOGGER.warn("Tried to register a dog bed material with the id {} more that once", material); 
            return null;
        }
        else {
            this.REGISTRY.add(material.setRegName(this.key));
            DoggyTalents.LOGGER.debug("Register dog bed {} under the key {}", this.key, material);
            return material;
        }
    }
    
    public List<IBedMaterial> getKeys() {
        return this.REGISTRY;
    }
    
    public IBedMaterial get(String saveId) {
        if(saveId.equals("missing"))
            return IBedMaterial.NULL;
        
        // Try find a registered material
        for(IBedMaterial thing : this.REGISTRY) {
            if(thing.getSaveId().equals(saveId)) {
                return thing;
            }
        }
        
        // Gets a holders so saveId is preserved
        return IBedMaterial.getHolder(saveId);
    }
    
    public IBedMaterial getFromStack(ItemStack stack) {
        for(IBedMaterial m : this.REGISTRY) {
            if(m.getIngredients().test(stack))
                return m;
        }
        return IBedMaterial.NULL;
    }
    
    public static ItemStack createItemStack(IBedMaterial casingId, IBedMaterial beddingId) {
        ItemStack stack = new ItemStack(ModBlocks.DOG_BED, 1);
        stack.setTagCompound(new NBTTagCompound());
        
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("casingId", casingId.getSaveId());
        tag.setString("beddingId", beddingId.getSaveId());
        stack.getTagCompound().setTag("doggytalents", tag);
        return stack;
    }
}
