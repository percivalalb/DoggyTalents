package doggytalents.common.command.arguments;

import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import doggytalents.common.storage.DogRespawnData;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class DogRespawnArgument implements ArgumentType<DogRespawnData> {

    public static final DynamicCommandExceptionType COLOR_INVALID = new DynamicCommandExceptionType((arg) -> {
        return new TranslationTextComponent("argument.dogrespawn.invalid", arg);
    });

    private DogRespawnArgument() {
    }

    public static DogRespawnArgument respawn() {
        return new DogRespawnArgument();
    }

    public static TextFormatting getColor(CommandContext<CommandSource> context, String name) {
        return context.getArgument(name, TextFormatting.class);
    }

    @Override
    public DogRespawnData parse(StringReader reader) throws CommandSyntaxException {
        String s = reader.readUnquotedString();
        int i = Integer.valueOf(s);

        return null;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder suggestionBuilder) {
        //TODO return ISuggestionProvider.suggest(DogRespawnStorage.get(context), suggestionBuilder);
        return Suggestions.empty();
    }
}