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

import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class VerificationParser {
    public List<String> fromJson(String json) throws IOException {
        if (json == null || json.isEmpty()) {
            return new ArrayList<String>();
        }

        StringReader stringReader = new StringReader(json);
        JsonReader reader = new JsonReader(stringReader);
        List<String> failures = new ArrayList<String>();
        reader.beginObject();
        String n = reader.nextName();
        if (!"errors".equals(n)) {
            throw new JsonParseException("expected property: errors, got: " + n);
        }
        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();
            n = reader.nextName();
            if (!"message".equals(n)) {
                throw new JsonParseException("expected property: message");
            }
            String message = reader.nextString();
            failures.add(message);
            reader.endObject();
        }
        reader.endArray();
        reader.endObject();

        reader.close();
        stringReader.close();

        return failures;
    }
}
