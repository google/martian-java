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
 * Generates Martian Proxy JSON configuration messages to verify the
 * HTTP request method.
 **/
public class MethodVerifier implements Modifier {
    public static final String NAME = "method.Verifier";

    private Scope scope;
    private String method;

    /**
     * Class constructor.
     **/
    public MethodVerifier() {
        this.scope = Scope.REQUEST;
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
        writer.name("method").value(this.method);
        writer.endObject();
        writer.endObject();
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
