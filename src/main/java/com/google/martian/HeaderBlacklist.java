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
 * Generates Martian Proxy JSON configuration messages to remove HTTP
 * headers from reqeusts and/or responses based on the name of the header.
 * HeaderBlacklist maintains a list of names as strings, and in the case
 * that a request and/or response contains a header with an exactly matching
 * name, that header is removed.
 **/
public class HeaderBlacklist implements Modifier {
    static final String NAME = "header.Blacklist";

    private List<String> names;
    private Scope scope;

    /**
     * Class constructor.
     **/
    public HeaderBlacklist() {
        this.scope = Scope.DEFAULT;
        this.names = new ArrayList<String>();
    }

    /**
     * Writes a header blacklist JSON configuration message for
     * Martian Proxy to writer.
     *
     * @param writer a GSON JsonWriter that JSON configurations are written to.
     * @throws IOException if an error occurs during input or output
     **/
    @Override
    public void writeJson(JsonWriter writer) throws IOException {
        writer.beginObject();
        writer.name(NAME);
        writer.beginObject();
        this.scope.writeJson(writer);
        writer.name("names");
        writer.beginArray();
        for (String name : this.names) {
            writer.value(name);
        }
        writer.endArray();
        writer.endObject();
        writer.endObject();
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    /**
     * adds a header name to the list of blacklisted headers
     **/
    public void addName(String name) {
        this.names.add(name);
    }
}
