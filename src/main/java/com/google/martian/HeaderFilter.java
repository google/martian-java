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

/**
 * Generates Martian Proxy JSON configuration messages for the contained
 * modifiers to be applied conditionally. HeaderFilter allows the application of a
 * modifier based on conditional matching of the headers of the request or response.
 **/
public class HeaderFilter implements Modifier {
    private static final String NAME = "header.Filter";

    private Modifier modifier;
    private Scope scope;
    private String name;
    private String value;

    /**
     * class constructor.
     **/
    public HeaderFilter() {
        this.scope = Scope.DEFAULT;
    }

    /**
     * Writes Header Filter JSON configuration message as well as the configuration
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
        if (this.name != null) {
            writer.name("name").value(this.name);
        }
        if (this.value != null) {
            writer.name("value").value(this.value);
        }
        writer.name("modifier");
        this.modifier.writeJson(writer);
        writer.endObject();
        writer.endObject();
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setModifier(Modifier modifier) {
        this.modifier = modifier;
    }
}
