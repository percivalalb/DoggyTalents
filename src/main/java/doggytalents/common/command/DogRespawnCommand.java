package doggytalents.common.command;

import static net.minecraft.command.Commands.*;

import java.util.Set;
import java.util.UUID;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import doggytalents.common.storage.DogRespawnData;
import doggytalents.common.storage.DogRespawnStorage;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.server.ServerWorld;

public class DogRespawnCommand {

    public static void register(final CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                literal("dogrespawn")
                    .requires(s -> s.hasPermissionLevel(2))
                    .executes(c -> respawn(c.getSource()))
        );
    }

    private static int respawn(final CommandSource source) throws CommandSyntaxException {
        //TODO
        ServerWorld world = source.getWorld();
        PlayerEntity player = source.asPlayer();
        Set<UUID> set = DogRespawnStorage.get(world).getAllUUID();
        if (!set.isEmpty()) {
            UUID uuid = set.iterator().next();
            DogRespawnData respawnData = DogRespawnStorage.get(world).getData(uuid);
            respawnData.respawn(world, player, player.getPosition().up());
        }
        return 1;
    }
}