// Copyright 2015 Google Inc. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.google.martian;

import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Generates Martian Proxy JSON configuration messages for contained
 * modifiers sequentially. FifoGroup allows the declaration of a sequential
 * list of request and response modifiers.
 **/
public class FifoGroup implements Modifier {
    private static final String NAME = "fifo.Group";

    private List<Modifier> modifiers;
    private Scope scope;

    /**
     * Class constructor.
     **/
    public FifoGroup() {
        this.scope = Scope.DEFAULT;
        this.modifiers = new ArrayList<Modifier>();
    }

    /**
     * Writes a FifoGroup JSON configuration message as well as the configuration
     * JSON for any contained modifiers for Martian Proxy to writer.
     *
     * @param writer GSON JsonWriter that JSON configurations are written to
     * @throws IOException if an error occurs during input or output
     **/
    @Override
    public void writeJson(JsonWriter writer) throws IOException {
        writer.beginObject();
        writer.name(NAME);
        writer.beginObject();
        this.scope.writeJson(writer);
        writer.name("modifiers");
        writer.beginArray();
        for (Modifier modifier : this.modifiers) {
            modifier.writeJson(writer);
        }
        writer.endArray();
        writer.endObject();
        writer.endObject();
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    /**
     * adds a modifier to be exectued after any previously added modifiers
     *
     * @param modifier modifier to be executed after any previously added modifiers
     **/
    public void addModifier(Modifier modifier) {
        this.modifiers.add(modifier);
    }
}
