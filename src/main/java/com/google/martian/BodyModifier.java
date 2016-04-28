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
 * BodyModifier generates Martian Proxy JSON configuration meessages to modify
 * HTTP bodies. Martian will replace the body and content-type of an HTTP
 * request and/or response.
 **/
public class BodyModifier implements Modifier {
    static final String NAME = "body.Modifier";

    private String body;
    private String contentType;
    private Scope scope;

    /**
     * Class constructor.
     **/
    public BodyModifier() {
        this.scope = Scope.DEFAULT;
    }

    /**
     * Writes a JSON configuration message for Martian Proxy to writer.
     *
     * @param writer a GSON JsonWriter that JSON configurations are written to.
     **/
    @Override
    public void writeJson(JsonWriter writer) throws IOException {
        writer.setSerializeNulls(false);

        writer.beginObject();
        writer.name(NAME);

        writer.beginObject();
        scope.writeJson(writer);
        writer.name("body").value(body);
        writer.name("contentType").value(contentType);
        writer.endObject();

        writer.endObject();
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }
}
