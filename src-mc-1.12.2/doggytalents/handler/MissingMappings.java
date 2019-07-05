package doggytalents.handler;

import java.util.Objects;

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
         if(event.getAllMappings() == null) return; // Prevent crash
         
         ImmutableList<Mapping<Item>> mappings = event.getAllMappings();
         mappings.forEach(mapping -> {
             if(Objects.equals(mapping.key, Compatibility.COMMAND_EMBLEM)) {
                 mapping.remap(ModItems.WHISTLE);
                 DoggyTalents.LOGGER.debug("Remapped Command Emblem to Whistle");
             } else if(Objects.equals(mapping.key, Compatibility.CREATIVE_RADAR)) {
                 mapping.remap(ModItems.CREATIVE_RADAR);
             } else if(Objects.equals(mapping.key, Compatibility.FANCY_COLLAR)) {
                 mapping.remap(ModItems.MULTICOLOURED_COLLAR);
                 DoggyTalents.LOGGER.debug("Remapped Fancy Collar to Mutlicoloured Collar");
             }
         });
     }
}
