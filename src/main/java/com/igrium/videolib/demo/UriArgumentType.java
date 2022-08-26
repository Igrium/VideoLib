package com.igrium.videolib.demo;

import java.net.URI;
import java.net.URISyntaxException;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.minecraft.text.Text;

public class UriArgumentType implements ArgumentType<URI> {

    SimpleCommandExceptionType COMMAND_EXCEPTION = new SimpleCommandExceptionType(Text.of("Invalid URI"));

    @Override
    public URI parse(StringReader reader) throws CommandSyntaxException {
        StringBuilder builder = new StringBuilder();
        char c;
        while (reader.canRead() && !Character.isWhitespace(c = reader.read())) {
            builder.append(c);
        }

        try {
            return new URI(builder.toString());
        } catch (URISyntaxException e) {
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
