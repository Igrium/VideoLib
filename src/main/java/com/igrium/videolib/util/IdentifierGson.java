package com.igrium.videolib.util;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import net.minecraft.util.Identifier;

/**
 * A simple Gson type adapter for identifiers. (I don't get why Mojang doesn't
 * have a default one.)
 */
public class IdentifierGson extends TypeAdapter<Identifier> {

    @Override
    public Identifier read(JsonReader reader) throws IOException {
        return Identifier.tryParse(reader.nextString());
    }

    @Override
    public void write(JsonWriter writer, Identifier val) throws IOException {
        writer.value(val.toString());
    }
    
}
