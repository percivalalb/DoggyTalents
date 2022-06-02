package doggytalents.common.command;

import com.google.common.base.Strings;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import doggytalents.common.command.arguments.UUIDArgument;
import doggytalents.common.entity.Dog;
import doggytalents.common.item.RadarItem;
import doggytalents.common.storage.*;
import doggytalents.common.util.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.ArgumentTypes;
import net.minecraft.commands.synchronization.EmptyArgumentSerializer;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static net.minecraft.commands.Commands.literal;

public class DogRespawnCommand {

    public static final DynamicCommandExceptionType COLOR_INVALID = new DynamicCommandExceptionType((arg) -> {
        return new TranslatableComponent("command.dogrespawn.invalid", arg);
    });

    public static final DynamicCommandExceptionType SPAWN_EXCEPTION = new DynamicCommandExceptionType((arg) -> {
        return new TranslatableComponent("command.dogrespawn.exception", arg);
    });

    public static final Dynamic2CommandExceptionType TOO_MANY_OPTIONS = new Dynamic2CommandExceptionType((arg1, arg2) -> {
        return new TranslatableComponent("command.dogrespawn.imprecise", arg1, arg2);
    });

    public static void register(final CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                literal("dog")
                    .requires(s -> s.hasPermission(2))
                    .then(Commands.literal("locate")
                        .then(Commands.literal("byuuid")
                          .then(Commands.argument("dog_owner", UUIDArgument.uuid()).suggests(DogRespawnCommand.getOwnerIdSuggestionsLocate())
                          .then(Commands.argument("dog_uuid", UUIDArgument.uuid()).suggests(DogRespawnCommand.getDogIdSuggestionsLocate())
                          .executes(c -> locate(c)))))
                        .then(Commands.literal("byname")
                           .then(Commands.argument("owner_name", StringArgumentType.string()).suggests(DogRespawnCommand.getOwnerNameSuggestionsLocate())
                           .then(Commands.argument("dog_name", StringArgumentType.string()).suggests(DogRespawnCommand.getDogNameSuggestionsLocate())
                           .executes(c -> locate2(c))))))
                    .then(Commands.literal("revive")
                      .then(Commands.literal("byuuid")
                        .then(Commands.argument("dog_owner", UUIDArgument.uuid()).suggests(DogRespawnCommand.getOwnerIdSuggestionsRevive())
                        .then(Commands.argument("dog_uuid", UUIDArgument.uuid()).suggests(DogRespawnCommand.getDogIdSuggestionsRevive())
                        .executes(c -> respawn(c)))))
                      .then(Commands.literal("byname")
                        .then(Commands.argument("owner_name", StringArgumentType.string()).suggests(DogRespawnCommand.getOwnerNameSuggestionsRevive())
                         .then(Commands.argument("dog_name", StringArgumentType.string()).suggests(DogRespawnCommand.getDogNameSuggestionsRevive())
                         .executes(c -> respawn2(c))))))
        );
    }

    public static void registerSerilizers() {
        ArgumentTypes.register(Util.getResourcePath("uuid"), UUIDArgument.class, new EmptyArgumentSerializer<>(UUIDArgument::uuid));
    }

    private static <S extends SharedSuggestionProvider> SuggestionProvider<S> getOwnerIdSuggestionsLocate() {
        return (context, builder) -> getOwnerIdSuggestions(DogLocationStorage.get(((CommandSourceStack)context.getSource()).getLevel()).getAll(), context, builder);
    }

    private static <S extends SharedSuggestionProvider> SuggestionProvider<S> getOwnerIdSuggestionsRevive() {
        return (context, builder) -> getOwnerIdSuggestions(DogRespawnStorage.get(((CommandSourceStack)context.getSource()).getLevel()).getAll(), context, builder);
    }

    private static <S extends SharedSuggestionProvider> CompletableFuture<Suggestions> getOwnerIdSuggestions(Collection<? extends IDogData> possibilities, final CommandContext<S> context, final SuggestionsBuilder builder) {
        if (context.getSource() instanceof CommandSourceStack) {

            return SharedSuggestionProvider.suggest(possibilities.stream()
                    .map(IDogData::getOwnerId)
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .collect(Collectors.toSet()),
                   builder);

        } else if (context.getSource() instanceof SharedSuggestionProvider) {
            return context.getSource().customSuggestion(context);
        } else {
            return Suggestions.empty();
        }
    }

    private static <S extends SharedSuggestionProvider> SuggestionProvider<S> getDogIdSuggestionsLocate() {
        return (context, builder) -> getDogIdSuggestions(DogLocationStorage.get(((CommandSourceStack)context.getSource()).getLevel()).getAll(), context, builder);
    }

    private static <S extends SharedSuggestionProvider> SuggestionProvider<S> getDogIdSuggestionsRevive() {
        return (context, builder) -> getDogIdSuggestions(DogRespawnStorage.get(((CommandSourceStack)context.getSource()).getLevel()).getAll(), context, builder);
    }

    private static <S extends SharedSuggestionProvider> CompletableFuture<Suggestions> getDogIdSuggestions(Collection<? extends IDogData> possibilities, final CommandContext<S> context, final SuggestionsBuilder builder) {
        if (context.getSource() instanceof CommandSourceStack) {
            UUID ownerId = context.getArgument("dog_owner", UUID.class);
            if (ownerId == null) {
                return Suggestions.empty();
            }

            return SharedSuggestionProvider.suggest(possibilities.stream()
                     .filter(data -> ownerId.equals(data.getOwnerId()))
                     .map(IDogData::getDogId)
                     .map(Object::toString)
                     .collect(Collectors.toSet()),
                    builder);
        } else if (context.getSource() instanceof SharedSuggestionProvider) {
             return context.getSource().customSuggestion(context);
        } else {
             return Suggestions.empty();
        }
    }


    private static <S extends SharedSuggestionProvider> SuggestionProvider<S> getOwnerNameSuggestionsLocate() {
        return (context, builder) -> getOwnerNameSuggestions(DogLocationStorage.get(((CommandSourceStack)context.getSource()).getLevel()).getAll(), context, builder);
    }

    private static <S extends SharedSuggestionProvider> SuggestionProvider<S> getOwnerNameSuggestionsRevive() {
        return (context, builder) -> getOwnerNameSuggestions(DogRespawnStorage.get(((CommandSourceStack)context.getSource()).getLevel()).getAll(), context, builder);
    }

    public static <S extends SharedSuggestionProvider> CompletableFuture<Suggestions> getOwnerNameSuggestions(Collection<? extends IDogData> possibilities, final CommandContext<S> context, final SuggestionsBuilder builder) {
        if (context.getSource() instanceof CommandSourceStack) {
            return SharedSuggestionProvider.suggest(possibilities.stream()
                    .map(IDogData::getOwnerName)
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .collect(Collectors.toSet()),
                   builder);

        } else if (context.getSource() instanceof SharedSuggestionProvider) {
            return context.getSource().customSuggestion(context);
        } else {
            return Suggestions.empty();
        }
    }

    private static <S extends SharedSuggestionProvider> SuggestionProvider<S> getDogNameSuggestionsLocate() {
        return (context, builder) -> getDogNameSuggestions(DogLocationStorage.get(((CommandSourceStack)context.getSource()).getLevel()).getAll(), context, builder);
    }

    private static <S extends SharedSuggestionProvider> SuggestionProvider<S> getDogNameSuggestionsRevive() {
        return (context, builder) -> getDogNameSuggestions(DogRespawnStorage.get(((CommandSourceStack)context.getSource()).getLevel()).getAll(), context, builder);
    }

    public static <S extends SharedSuggestionProvider> CompletableFuture<Suggestions> getDogNameSuggestions(Collection<? extends IDogData> possibilities, final CommandContext<S> context, final SuggestionsBuilder builder) {
        if (context.getSource() instanceof CommandSourceStack) {
            String ownerName = context.getArgument("owner_name", String.class);

            if (ownerName == null) {
                return Suggestions.empty();
            }

            return SharedSuggestionProvider.suggest(possibilities.stream()
                    .filter(data -> ownerName.equals(data.getOwnerName()))
                    .map(IDogData::getDogName)
                    .filter((str) -> !Strings.isNullOrEmpty(str))
                     .collect(Collectors.toList()),
                     builder);

        } else if (context.getSource() instanceof SharedSuggestionProvider) {
             return context.getSource().customSuggestion(context);
        } else {
             return Suggestions.empty();
        }
    }

    private static int respawn(final CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        CommandSourceStack source = ctx.getSource();
        source.getPlayerOrException(); // Check source is a player
        ServerLevel world = source.getLevel();

        UUID ownerUuid = ctx.getArgument("dog_owner", UUID.class);

        UUID uuid = ctx.getArgument("dog_uuid", UUID.class);

        DogRespawnStorage respawnStorage = DogRespawnStorage.get(world);
        DogRespawnData respawnData = respawnStorage.getData(uuid);

        if (respawnData == null) {
            throw COLOR_INVALID.create(uuid.toString());
        }

        return respawn(respawnStorage, respawnData, source);
    }

    private static int respawn2(final CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        CommandSourceStack source = ctx.getSource();
        source.getPlayerOrException(); // Check source is a player
        ServerLevel world = source.getLevel();

        String ownerName = ctx.getArgument("owner_name", String.class);

        String dogName = ctx.getArgument("dog_name", String.class);
        DogRespawnStorage respawnStorage = DogRespawnStorage.get(world);
        List<DogRespawnData> respawnData = respawnStorage.getDogs(ownerName).filter((data) -> data.getDogName().equalsIgnoreCase(dogName)).collect(Collectors.toList());

        if (respawnData.isEmpty()) {
            throw COLOR_INVALID.create(dogName);
        }

        if (respawnData.size() > 1) {
            StringJoiner joiner = new StringJoiner(", ");
            for (DogRespawnData data : respawnData) {
                joiner.add(Objects.toString(data.getDogId()));
            }
            throw TOO_MANY_OPTIONS.create(joiner.toString(), respawnData.size());
        }

        return respawn(respawnStorage, respawnData.get(0), source);
    }

    private static int respawn(DogRespawnStorage respawnStorage, DogRespawnData respawnData, final CommandSourceStack source) throws CommandSyntaxException {
        Dog dog = respawnData.respawn(source.getLevel(), source.getPlayerOrException(), source.getPlayerOrException().blockPosition().above());

        if (dog != null) {
            respawnStorage.remove(respawnData.getDogId());
            source.sendSuccess(new TranslatableComponent("commands.dogrespawn.uuid.success", respawnData.getDogName()), false);
            return 1;
        }

        source.sendSuccess(new TranslatableComponent("commands.dogrespawn.uuid.failure", respawnData.getDogName()), false);
        return 0;
    }

    private static int locate(final CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        CommandSourceStack source = ctx.getSource();
        source.getPlayerOrException(); // Check source is a player
        ServerLevel world = source.getLevel();

        UUID ownerUuid = ctx.getArgument("dog_owner", UUID.class);

        UUID uuid = ctx.getArgument("dog_uuid", UUID.class);

        DogLocationStorage locationStorage = DogLocationStorage.get(world);
        DogLocationData locationData = locationStorage.getData(uuid);

        if (locationData == null) {
            throw COLOR_INVALID.create(uuid.toString());
        }

        return locate(locationStorage, locationData, source);
    }

    private static int locate2(final CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        CommandSourceStack source = ctx.getSource();
        source.getPlayerOrException(); // Check source is a player
        ServerLevel world = source.getLevel();

        String ownerName = ctx.getArgument("owner_name", String.class);

        String dogName = ctx.getArgument("dog_name", String.class);
        DogLocationStorage locationStorage = DogLocationStorage.get(world);
        List<DogLocationData> locationData = locationStorage.getAll().stream()
                .filter(data -> ownerName.equals(data.getOwnerName())).filter((data) -> data.getDogName().equalsIgnoreCase(dogName)).collect(Collectors.toList());

        if (locationData.isEmpty()) {
            throw COLOR_INVALID.create(dogName);
        }

        if (locationData.size() > 1) {
            StringJoiner joiner = new StringJoiner(", ");
            for (DogLocationData data : locationData) {
                joiner.add(Objects.toString(data.getDogId()));
            }
            throw TOO_MANY_OPTIONS.create(joiner.toString(), locationData.size());
        }

        return locate(locationStorage, locationData.get(0), source);
    }

    private static int locate(DogLocationStorage respawnStorage, DogLocationData locationData, final CommandSourceStack source) throws CommandSyntaxException {
        Player player = source.getPlayerOrException();

        if (locationData.getDimension().equals(player.level.dimension())) {
            String translateStr = RadarItem.getDirectionTranslationKey(locationData, player);
            int distance = Mth.ceil(locationData.getPos() != null ? locationData.getPos().distanceTo(player.position()) : -1);

            source.sendSuccess(new TranslatableComponent(translateStr, locationData.getName(player.level), distance), false);
        } else {
            source.sendSuccess(new TranslatableComponent("dogradar.notindim", locationData.getDimension()), false); // TODO change message
        }
        return 1;

    }
}
