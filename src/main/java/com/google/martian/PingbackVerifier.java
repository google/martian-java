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
 * Generates Martain Proxy JSON configuration messages to verify
 * pingbacks to a URL. In the case that only some parts of the URL to verify
 * are set, the verification will check that a pingpack to a URL that matches
 * the provided parts of the URL occurred. For instance, to verify that all
 * pingbacks are over HTTPS, only set the scheme to "HTTPS".
 **/
public class PingbackVerifier implements Modifier {
    public static final String NAME = "pingback.Verifier";

    private Scope scope;
    private String scheme;
    private String host;
    private String path;
    private String query;

    /*
     * Class constructor.
     **/
    public PingbackVerifier() {
        this.scope = Scope.DEFAULT;
    }

    /**
     * Writes a Method Verifier JSON configuration message for
     * Martian Proxy to writer
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
        writer.endObject();
        writer.endObject();
    }

    public void setScope(Scope scope) {
        this.scope = scope;
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
