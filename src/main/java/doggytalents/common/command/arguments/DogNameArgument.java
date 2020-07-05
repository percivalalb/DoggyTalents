package doggytalents.common.command.arguments;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import doggytalents.DoggyTalents2;
import doggytalents.common.storage.DogRespawnData;
import doggytalents.common.storage.DogRespawnStorage;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;

public class DogNameArgument implements ArgumentType<String> {

    public static DogNameArgument name() {
        return new DogNameArgument();
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return reader.readUnquotedString();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        if (context.getSource() instanceof CommandSource) {
            String ownerName = context.getArgument("owner_name", String.class);
            DoggyTalents2.LOGGER.debug("{}", ownerName);
            if (ownerName == null) {
                return Suggestions.empty(); // TODO return all dogs
            }

            DogRespawnStorage respawnStorage = DogRespawnStorage.get(((CommandSource)context.getSource()).getWorld());

            return ISuggestionProvider.suggest(respawnStorage.getDogs(ownerName)
                    .map(DogRespawnData::getName)
                    .filter((str) -> !Strings.isNullOrEmpty(str))
                     .collect(Collectors.toList()),
                     builder);

        } else if (context.getSource() instanceof ISuggestionProvider) {
             ISuggestionProvider isuggestionprovider = (ISuggestionProvider)context.getSource();
             return isuggestionprovider.getSuggestionsFromServer((CommandContext<ISuggestionProvider>)context, builder);
        } else {
             return Suggestions.empty();
        }
    }
}