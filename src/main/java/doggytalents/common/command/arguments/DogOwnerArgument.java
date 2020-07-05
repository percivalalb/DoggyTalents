package doggytalents.common.command.arguments;

import java.util.Objects;
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

public class DogOwnerArgument implements ArgumentType<String> {

    public static DogOwnerArgument name() {
        return new DogOwnerArgument();
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return reader.readUnquotedString();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (context.getSource() instanceof CommandSource) {
            DogRespawnStorage respawnStorage = DogRespawnStorage.get(((CommandSource)context.getSource()).getWorld());
            return ISuggestionProvider.suggest(respawnStorage.getAll().stream()
                    .map(DogRespawnData::getOwnerName)
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .collect(Collectors.toSet()),
                   builder);

        } else if (context.getSource() instanceof ISuggestionProvider) {
            ISuggestionProvider isuggestionprovider = (ISuggestionProvider)context.getSource();
            return isuggestionprovider.getSuggestionsFromServer((CommandContext<ISuggestionProvider>)context, builder);
        } else {
            return Suggestions.empty();
        }
    }
}