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
 * Effective scope of a modifier. In general, a modifier can be applied on an
 * HTTP request, response or both. By default, most modifiers are scoped to
 * both request and response.
 **/
public enum Scope {
    // modifier's default scope as defined in Martian
    DEFAULT,
    // modifier applies to HTTP requests
    REQUEST,
    // modifier applies to HTTP responses
    RESPONSE,
    // modifier applies to HTTP requests and responses
    REQUEST_AND_RESPONSE;

    /**
     * writes scope portion of JSON configuration message to writer
     *
     * @param writer a GSON JsonWriter that JSON configurations are written to.
     **/
    public void writeJson(JsonWriter writer) throws IOException {
        if (this == Scope.DEFAULT) {
            return;
        }

        writer.name("scope");
        writer.beginArray();
        switch (this) {
            case REQUEST:
                writer.value("request");
                break;
            case RESPONSE:
                writer.value("response");
                break;
            case REQUEST_AND_RESPONSE:
                writer.value("request");
                writer.value("response");
                break;
        }
        writer.endArray();
    }

    /**
     * @return a string representation of the scope
     **/
    public String toString() {
        return this.name().toLowerCase();
    }
}
