package doggytalents.handler;

import com.google.common.collect.ImmutableList;

import doggytalents.DoggyTalentsMod;
import doggytalents.ModEntities;
import doggytalents.helper.Compatibility;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.RegistryEvent.MissingMappings.Mapping;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MissingMappings {

	 @SubscribeEvent
	 public void remapMissingEntities(final RegistryEvent.MissingMappings<EntityType<?>> event) {
		 ImmutableList<Mapping<EntityType<?>>> mappings = event.getMappings();
		 for(Mapping<EntityType<?>> mapping : mappings) {
			 if(mapping.key.equals(Compatibility.DOGGY_BEAM)) {
				 mapping.remap(ModEntities.DOG_BEAM);
			     DoggyTalentsMod.LOGGER.info("Remapped Dog Beam id");
			 }
		 }
	 }
	 
	 @SubscribeEvent
	 public void remapMissingBlocks(final RegistryEvent.MissingMappings<Block> event) {
//		 ImmutableList<Mapping<Block>> mappings = event.getMappings();
//		 for(Mapping<Block> mapping : mappings) {
//			 DoggyTalentsMod.LOGGER.info("MISSING MAPPING !!!!!!!!!!!!!!!!!!!!!!!! " + mapping.key);
//			 if(mapping.key.equals(Compatibility.DOGGY_BEAM)) {
//				 mapping.remap(ModEntities.DOG_BEAM);
//			     DoggyTalentsMod.LOGGER.info("Remapped Dog Beam id");
//			 }
//		 }
	 }
}
