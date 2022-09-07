package com.igrium.videolib.util;

import java.net.URI;
import java.net.URISyntaxException;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.minecraft.text.TranslatableText;

public class UriArgumentType implements ArgumentType<URI> {

    SimpleCommandExceptionType COMMAND_EXCEPTION = new SimpleCommandExceptionType(new TranslatableText("commands.videolib.arguments.invalid_uri"));

    @Override
    public URI parse(StringReader reader) throws CommandSyntaxException {
        int i = reader.getCursor();
        while (reader.canRead() && !Character.isWhitespace(reader.peek())) {
            reader.skip();
        }
        String str = reader.getString().substring(i, reader.getCursor());
        try {
            return new URI(str);
        } catch (URISyntaxException e) {
            reader.setCursor(i);
            throw COMMAND_EXCEPTION.createWithContext(reader);
        }
    }

    public static UriArgumentType uri() {
        return new UriArgumentType();
    }

    public static URI getUri(CommandContext<?> context, String name) {
        return context.getArgument(name, URI.class);
    }
    
}
