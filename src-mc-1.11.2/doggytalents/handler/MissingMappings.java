package doggytalents.handler;

import com.google.common.collect.ImmutableList;

import doggytalents.DoggyTalents;
import doggytalents.ModItems;
import doggytalents.helper.Compatibility;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.RegistryEvent.MissingMappings.Mapping;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MissingMappings {
	 
	 @SubscribeEvent
	 public void remapMissingItems(final RegistryEvent.MissingMappings<Item> event) {
		 ImmutableList<Mapping<Item>> mappings = event.getMappings();
		 if(mappings != null) {
			 for(Mapping<Item> mapping : mappings) {
				 if(mapping.key != null) {
					 if(mapping.key.equals(Compatibility.COMMAND_EMBLEM)) {
						 mapping.remap(ModItems.WHISTLE);
					     DoggyTalents.LOGGER.info("Remapped Command Emblem to Whistle");
					 } else if(mapping.key.equals(Compatibility.CREATIVE_RADAR)) {
						 mapping.remap(ModItems.CREATIVE_RADAR);
					 } else if(mapping.key.equals(Compatibility.FANCY_COLLAR)) {
						 mapping.remap(ModItems.MULTICOLOURED_COLLAR);
					     DoggyTalents.LOGGER.info("Remapped Fancy Collar to Mutlicoloured Collar");
					 }
				 }
			 }
		 }
	 }
}
