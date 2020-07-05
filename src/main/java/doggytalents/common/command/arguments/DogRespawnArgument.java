package doggytalents.common.command.arguments;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import doggytalents.common.storage.DogRespawnData;
import doggytalents.common.storage.DogRespawnStorage;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;

public class DogRespawnArgument implements ArgumentType<UUID> {

    public static DogRespawnArgument respawn() {
        return new DogRespawnArgument();
    }

    @Override
    public UUID parse(StringReader reader) throws CommandSyntaxException {
        return UUID.fromString(reader.readUnquotedString());
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder suggestionBuilder) {
        if (context.getSource() instanceof CommandSource) {
            UUID ownerId = context.getArgument("dog_owner", UUID.class);
            if (ownerId == null) {
                return Suggestions.empty(); // TODO return all dogs
            }

            DogRespawnStorage respawnStorage = DogRespawnStorage.get(((CommandSource)context.getSource()).getWorld());

            return ISuggestionProvider.suggest(respawnStorage.getDogs(ownerId)
                     .map(DogRespawnData::getName)
                     .map(Object::toString)
                     .collect(Collectors.toSet()),
                   suggestionBuilder);

         } else if (context.getSource() instanceof ISuggestionProvider) {
            ISuggestionProvider isuggestionprovider = (ISuggestionProvider)context.getSource();
            return isuggestionprovider.getSuggestionsFromServer((CommandContext<ISuggestionProvider>)context, suggestionBuilder);
         } else {
            return Suggestions.empty();
         }

    }
}