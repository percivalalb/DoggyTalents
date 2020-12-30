package doggytalents.common.command;

import static net.minecraft.command.Commands.*;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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
import doggytalents.common.entity.DogEntity;
import doggytalents.common.item.RadarItem;
import doggytalents.common.storage.DogLocationData;
import doggytalents.common.storage.DogLocationStorage;
import doggytalents.common.storage.DogRespawnData;
import doggytalents.common.storage.DogRespawnStorage;
import doggytalents.common.storage.IDogData;
import doggytalents.common.util.Util;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.ArgumentSerializer;
import net.minecraft.command.arguments.ArgumentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.dimension.DimensionType;
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
                literal("dog")
                    .requires(s -> s.hasPermissionLevel(2))
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
        ArgumentTypes.register(Util.getResourcePath("uuid"), UUIDArgument.class, new ArgumentSerializer<>(UUIDArgument::uuid));
    }

    private static <S extends ISuggestionProvider> SuggestionProvider<S> getOwnerIdSuggestionsLocate() {
        return (context, builder) -> getOwnerIdSuggestions(DogLocationStorage.get(((CommandSource)context.getSource()).getWorld()).getAll(), context, builder);
    }

    private static <S extends ISuggestionProvider> SuggestionProvider<S> getOwnerIdSuggestionsRevive() {
        return (context, builder) -> getOwnerIdSuggestions(DogRespawnStorage.get(((CommandSource)context.getSource()).getWorld()).getAll(), context, builder);
    }

    private static <S extends ISuggestionProvider> CompletableFuture<Suggestions> getOwnerIdSuggestions(Collection<? extends IDogData> possibilities, final CommandContext<S> context, final SuggestionsBuilder builder) {
        if (context.getSource() instanceof CommandSource) {

            return ISuggestionProvider.suggest(possibilities.stream()
                    .map(IDogData::getOwnerId)
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .collect(Collectors.toSet()),
                   builder);

        } else if (context.getSource() instanceof ISuggestionProvider) {
            return context.getSource().getSuggestionsFromServer((CommandContext<ISuggestionProvider>) context, builder);
        } else {
            return Suggestions.empty();
        }
    }

    private static <S extends ISuggestionProvider> SuggestionProvider<S> getDogIdSuggestionsLocate() {
        return (context, builder) -> getDogIdSuggestions(DogLocationStorage.get(((CommandSource)context.getSource()).getWorld()).getAll(), context, builder);
    }

    private static <S extends ISuggestionProvider> SuggestionProvider<S> getDogIdSuggestionsRevive() {
        return (context, builder) -> getDogIdSuggestions(DogRespawnStorage.get(((CommandSource)context.getSource()).getWorld()).getAll(), context, builder);
    }

    private static <S extends ISuggestionProvider> CompletableFuture<Suggestions> getDogIdSuggestions(Collection<? extends IDogData> possibilities, final CommandContext<S> context, final SuggestionsBuilder builder) {
        if (context.getSource() instanceof CommandSource) {
            UUID ownerId = context.getArgument("dog_owner", UUID.class);
            if (ownerId == null) {
                return Suggestions.empty();
            }

            return ISuggestionProvider.suggest(possibilities.stream()
                     .filter(data -> ownerId.equals(data.getOwnerId()))
                     .map(IDogData::getDogId)
                     .map(Object::toString)
                     .collect(Collectors.toSet()),
                    builder);
        } else if (context.getSource() instanceof ISuggestionProvider) {
             return context.getSource().getSuggestionsFromServer((CommandContext<ISuggestionProvider>) context, builder);
        } else {
             return Suggestions.empty();
        }
    }


    private static <S extends ISuggestionProvider> SuggestionProvider<S> getOwnerNameSuggestionsLocate() {
        return (context, builder) -> getOwnerNameSuggestions(DogLocationStorage.get(((CommandSource)context.getSource()).getWorld()).getAll(), context, builder);
    }

    private static <S extends ISuggestionProvider> SuggestionProvider<S> getOwnerNameSuggestionsRevive() {
        return (context, builder) -> getOwnerNameSuggestions(DogRespawnStorage.get(((CommandSource)context.getSource()).getWorld()).getAll(), context, builder);
    }

    public static <S extends ISuggestionProvider> CompletableFuture<Suggestions> getOwnerNameSuggestions(Collection<? extends IDogData> possibilities, final CommandContext<S> context, final SuggestionsBuilder builder) {
        if (context.getSource() instanceof CommandSource) {
            return ISuggestionProvider.suggest(possibilities.stream()
                    .map(IDogData::getOwnerName)
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .collect(Collectors.toSet()),
                   builder);

        } else if (context.getSource() instanceof ISuggestionProvider) {
            return context.getSource().getSuggestionsFromServer((CommandContext<ISuggestionProvider>) context, builder);
        } else {
            return Suggestions.empty();
        }
    }

    private static <S extends ISuggestionProvider> SuggestionProvider<S> getDogNameSuggestionsLocate() {
        return (context, builder) -> getDogNameSuggestions(DogLocationStorage.get(((CommandSource)context.getSource()).getWorld()).getAll(), context, builder);
    }

    private static <S extends ISuggestionProvider> SuggestionProvider<S> getDogNameSuggestionsRevive() {
        return (context, builder) -> getDogNameSuggestions(DogRespawnStorage.get(((CommandSource)context.getSource()).getWorld()).getAll(), context, builder);
    }

    public static <S extends ISuggestionProvider> CompletableFuture<Suggestions> getDogNameSuggestions(Collection<? extends IDogData> possibilities, final CommandContext<S> context, final SuggestionsBuilder builder) {
        if (context.getSource() instanceof CommandSource) {
            String ownerName = context.getArgument("owner_name", String.class);

            if (ownerName == null) {
                return Suggestions.empty();
            }

            return ISuggestionProvider.suggest(possibilities.stream()
                    .filter(data -> ownerName.equals(data.getOwnerName()))
                    .map(IDogData::getDogName)
                    .filter((str) -> !Strings.isNullOrEmpty(str))
                     .collect(Collectors.toList()),
                     builder);

        } else if (context.getSource() instanceof ISuggestionProvider) {
             return context.getSource().getSuggestionsFromServer((CommandContext<ISuggestionProvider>)context, builder);
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

    private static int respawn(DogRespawnStorage respawnStorage, DogRespawnData respawnData, final CommandSource source) throws CommandSyntaxException {
        DogEntity dog = respawnData.respawn(source.getWorld(), source.asPlayer(), source.asPlayer().getPosition().up());

        if (dog != null) {
            respawnStorage.remove(respawnData.getDogId());
            source.sendFeedback(new TranslationTextComponent("commands.dogrespawn.uuid.success", respawnData.getDogName()), false);
            return 1;
        }

        source.sendFeedback(new TranslationTextComponent("commands.dogrespawn.uuid.failure", respawnData.getDogName()), false);
        return 0;
    }

    private static int locate(final CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        CommandSource source = ctx.getSource();
        source.asPlayer(); // Check source is a player
        ServerWorld world = source.getWorld();

        UUID ownerUuid = ctx.getArgument("dog_owner", UUID.class);

        UUID uuid = ctx.getArgument("dog_uuid", UUID.class);

        DogLocationStorage locationStorage = DogLocationStorage.get(world);
        DogLocationData locationData = locationStorage.getData(uuid);

        if (locationData == null) {
            throw COLOR_INVALID.create(uuid.toString());
        }

        return locate(locationStorage, locationData, source);
    }

    private static int locate2(final CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        CommandSource source = ctx.getSource();
        source.asPlayer(); // Check source is a player
        ServerWorld world = source.getWorld();

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

    private static int locate(DogLocationStorage respawnStorage, DogLocationData locationData, final CommandSource source) throws CommandSyntaxException {
        PlayerEntity player = source.asPlayer();

        if (locationData.getDimension() == player.dimension) {
            String translateStr = RadarItem.getDirectionTranslationKey(locationData, player);
            int distance = MathHelper.ceil(locationData.getPos() != null ? locationData.getPos().distanceTo(player.getPositionVector()) : -1);

            source.sendFeedback(new TranslationTextComponent(translateStr, locationData.getName(player.world), distance), false);
        } else {
            source.sendFeedback(new TranslationTextComponent("dogradar.notindim", DimensionType.getKey(locationData.getDimension())), false); // TODO change message
        }
        return 1;

    }
}