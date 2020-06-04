package doggytalents.common.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import doggytalents.DoggyTalents2;
import doggytalents.api.inferface.IBedMaterial;
import doggytalents.api.inferface.IDogBedRegistry;
import doggytalents.common.util.BackwardsComp;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

/**
 * @author ProPercivalalb
 */
public class DogBedRegistry implements IDogBedRegistry {

    public final static DogBedRegistry CASINGS = new DogBedRegistry("casing");
    public final static DogBedRegistry BEDDINGS = new DogBedRegistry("bedding");

    private final List<IBedMaterial> REGISTRY = new ArrayList<>();
    private final String key;

    public DogBedRegistry(String key) {
        this.key = key;
    }

    @Override
    public IBedMaterial registerMaterial(@Nonnull Block block, ResourceLocation textureLocation) {
        return this.registerMaterial(new BedMaterial(block, textureLocation, Ingredient.fromItems(block)));
    }

    @Override
    public IBedMaterial registerMaterial(IBedMaterial material) {
        if(this.REGISTRY.contains(material)) {
            DoggyTalents2.LOGGER.warn("Tried to register a dog bed material with the id {} more that once", material);
            return null;
        }
        else {
            this.REGISTRY.add(material.setRegName(this.key));
            DoggyTalents2.LOGGER.debug("Register dog bed {} under the key {}", this.key, material);
            return material;
        }
    }

    public List<IBedMaterial> getKeys() {
        return this.REGISTRY;
    }

    public IBedMaterial get(String saveId) {
        if(saveId == null || saveId.equals("missing")) {
            return IBedMaterial.NULL;
        }

        // Keep things when updating from 1.12
        Optional<String> newId = BackwardsComp.getBedMaterialMapping(saveId);
        if (newId.isPresent()) {
            saveId = newId.get();
        }

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
            if(m.getIngredient().test(stack)) {
                return m;
            }
        }
        return IBedMaterial.NULL;
    }
}
