package doggytalents.common.command;

import static net.minecraft.command.Commands.*;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import doggytalents.common.command.arguments.DogNameArgument;
import doggytalents.common.command.arguments.DogOwnerArgument;
import doggytalents.common.command.arguments.UUIDArgument;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.storage.DogRespawnData;
import doggytalents.common.storage.DogRespawnStorage;
import doggytalents.common.util.Util;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.ArgumentSerializer;
import net.minecraft.command.arguments.ArgumentTypes;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;

public class DogRespawnCommand {

    public static final DynamicCommandExceptionType COLOR_INVALID = new DynamicCommandExceptionType((arg) -> {
        return new TranslationTextComponent("command.dogrespawn.invalid", arg);
    });

    public static final DynamicCommandExceptionType SPAWN_EXCEPTION = new DynamicCommandExceptionType((arg) -> {
        return new TranslationTextComponent("command.dogrespawn.exception", arg);
    });

    public static final Dynamic2CommandExceptionType TOO_MANY_OPTIONS = new Dynamic2CommandExceptionType((arg1, arg2) -> {
        return new TranslationTextComponent("command.dogrespawn.imprecise", arg1, arg2);
    });

    public static void register(final CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                literal("dogrespawn")
                    .requires(s -> s.hasPermissionLevel(2))
                    .then(Commands.literal("byuuid")
                      .then(Commands.argument("dog_owner", UUIDArgument.uuid()).suggests(DogRespawnCommand::getOwnerIdSuggestions)
                      .then(Commands.argument("dog_uuid", UUIDArgument.uuid()).suggests(DogRespawnCommand::getDogIdSuggestions)
                      .executes(c -> respawn(c)))))
                    .then(Commands.literal("byname")
                       .then(Commands.argument("owner_name", DogOwnerArgument.name())
                       .then(Commands.argument("dog_name", DogNameArgument.name())
                       .executes(c -> respawn2(c)))))
        );

        ArgumentTypes.register(Util.getResourcePath("uuid"), UUIDArgument.class, new ArgumentSerializer<>(UUIDArgument::uuid));
        ArgumentTypes.register(Util.getResourcePath("owner_name"), DogOwnerArgument.class, new ArgumentSerializer<>(DogOwnerArgument::name));
        ArgumentTypes.register(Util.getResourcePath("dog_name"), DogNameArgument.class, new ArgumentSerializer<>(DogNameArgument::name));
    }

    private static <S> CompletableFuture<Suggestions> getOwnerIdSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        if (context.getSource() instanceof CommandSource) {
            DogRespawnStorage respawnStorage = DogRespawnStorage.get(((CommandSource)context.getSource()).getWorld());

            return ISuggestionProvider.suggest(respawnStorage.getAll().stream()
                    .map(DogRespawnData::getOwnerId)
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

    private static <S> CompletableFuture<Suggestions> getDogIdSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        if (context.getSource() instanceof CommandSource) {
            UUID ownerId = context.getArgument("dog_owner", UUID.class);
            if (ownerId == null) {
                return Suggestions.empty(); // TODO return all dogs
            }

            DogRespawnStorage respawnStorage = DogRespawnStorage.get(((CommandSource)context.getSource()).getWorld());

            return ISuggestionProvider.suggest(respawnStorage.getDogs(ownerId)
                     .map(DogRespawnData::getId)
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

    private static int respawn(final CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        CommandSource source = ctx.getSource();
        source.asPlayer(); // Check source is a player
        ServerWorld world = source.getWorld();

        UUID ownerUuid = ctx.getArgument("dog_owner", UUID.class);

        UUID uuid = ctx.getArgument("dog_uuid", UUID.class);

        DogRespawnStorage respawnStorage = DogRespawnStorage.get(world);
        DogRespawnData respawnData = respawnStorage.getData(uuid);

        if (respawnData == null) {
            throw COLOR_INVALID.create(uuid.toString());
        }

        return respawn(respawnStorage, respawnData, source);
    }

    private static int respawn2(final CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        CommandSource source = ctx.getSource();
        source.asPlayer(); // Check source is a player
        ServerWorld world = source.getWorld();

        String ownerName = ctx.getArgument("owner_name", String.class);

        String dogName = ctx.getArgument("dog_name", String.class);
        DogRespawnStorage respawnStorage = DogRespawnStorage.get(world);
        List<DogRespawnData> respawnData = respawnStorage.getDogs(ownerName).filter((data) -> data.getName().equalsIgnoreCase(dogName)).collect(Collectors.toList());

        if (respawnData.isEmpty()) {
            throw COLOR_INVALID.create(dogName);
        }

        if (respawnData.size() > 1) {
            StringJoiner joiner = new StringJoiner(", ");
            for (DogRespawnData data : respawnData) {
                joiner.add(Objects.toString(data.getId()));
            }
            throw TOO_MANY_OPTIONS.create(joiner.toString(), respawnData.size());
        }

        return respawn(respawnStorage, respawnData.get(0), source);
    }

    private static int respawn(DogRespawnStorage respawnStorage, DogRespawnData respawnData, final CommandSource source) throws CommandSyntaxException {
        DogEntity dog = respawnData.respawn(source.getWorld(), source.asPlayer(), source.asPlayer().getPosition().up());

        if (dog != null) {
            respawnStorage.remove(respawnData.getId());
            source.sendFeedback(new TranslationTextComponent("commands.dogrespawn.uuid.success", respawnData.getName()), false);
            return 1;
        }

        source.sendFeedback(new TranslationTextComponent("commands.dogrespawn.uuid.failure", respawnData.getName()), false);
        return 0;
    }
}