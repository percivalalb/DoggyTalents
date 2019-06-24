package doggytalents.handler;

import java.util.Objects;

import com.google.common.collect.ImmutableList;

import doggytalents.DoggyTalentsMod;
import doggytalents.ModEntities;
import doggytalents.ModItems;
import doggytalents.helper.Compatibility;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.RegistryEvent.MissingMappings.Mapping;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MissingMappings {

	 @SubscribeEvent
	 public void remapMissingEntities(final RegistryEvent.MissingMappings<EntityType<?>> event) {
	     if(event.getAllMappings() == null) return; // Prevent crash
	     
		 ImmutableList<Mapping<EntityType<?>>> mappings = event.getMappings();
		 mappings.forEach(mapping -> {
		     if(Objects.equals(mapping.key, Compatibility.DOGGY_BEAM)) {
                 mapping.remap(ModEntities.DOG_BEAM);
                 DoggyTalentsMod.LOGGER.debug("Remapped Dog Beam id");
             }
         });
	 }
	 
	 @SubscribeEvent
	 public void remapMissingItems(final RegistryEvent.MissingMappings<Item> event) {
	     if(event.getAllMappings() == null) return;  // Prevent crash
	     
		 ImmutableList<Mapping<Item>> mappings = event.getMappings();
		 mappings.forEach(mapping -> {
		     if(Objects.equals(mapping.key, Compatibility.COMMAND_EMBLEM)) {
                 mapping.remap(ModItems.WHISTLE);
                 DoggyTalentsMod.LOGGER.debug("Remapped Command Emblem to Whistle");
             }
		 });
	 }
}
