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
 * modifiers to be applied conditionally. UrlFilter allows the application of a
 * modifier based on conditional matching of the url of the request or response.
 **/
public class UrlFilter implements Modifier {
    private static final String NAME = "url.Filter";

    private Modifier modifier;
    private Scope scope;
    private String scheme;
    private String host;
    private String path;
    private String query;

    /**
     * Class constructor.
     **/
    public UrlFilter() {
        this.scope = Scope.DEFAULT;
    }

    /**
     * Writes URL Fitler JSON configuration message as well as the configuration
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
        if (this.scheme != null) {
            writer.name("scheme").value(this.scheme);
        }
        if (this.host != null) {
            writer.name("host").value(this.host);
        }
        if (this.path != null) {
            writer.name("path").value(this.path);
        }
        if (this.query != null) {
            writer.name("query").value(this.query);
        }
        writer.name("modifier");
        this.modifier.writeJson(writer);
        writer.endObject();
        writer.endObject();
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public void setModifier(Modifier modifier) {
        this.modifier = modifier;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
