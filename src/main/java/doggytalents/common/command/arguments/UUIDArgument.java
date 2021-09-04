package doggytalents.common.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.UUID;

public class UUIDArgument implements ArgumentType<UUID> {

    public static final DynamicCommandExceptionType UUID_SECTION_INVALID = new DynamicCommandExceptionType((arg) -> {
        return new TranslatableComponent("argument.doggytalents.uuid.section.invalid", arg);
    });

    public static final DynamicCommandExceptionType UUID_FORMAT_INVALID = new DynamicCommandExceptionType((arg) -> {
        return new TranslatableComponent("argument.doggytalents.uuid.format.invalid", arg);
    });

    public static UUIDArgument uuid() {
        return new UUIDArgument();
    }

    @Override
    public UUID parse(StringReader reader) throws CommandSyntaxException {
        String str = reader.readUnquotedString();
        try {
            return UUID.fromString(str);
        } catch (NumberFormatException e) {
            throw UUID_SECTION_INVALID.create(str);
        } catch (IllegalArgumentException e) {
            throw UUID_FORMAT_INVALID.create(str);
        }
    }
}
