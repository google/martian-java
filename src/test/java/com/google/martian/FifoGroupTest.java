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

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class FifoGroupTest {

    @Test
    public void testWriteJson() throws Exception {
        HeaderModifier headerModifier = new HeaderModifier();
        headerModifier.setName("X-Name");
        headerModifier.setValue("Mod-Run");

        QueryStringModifier queryStringModifier = new QueryStringModifier();
        queryStringModifier.setName("foo");
        queryStringModifier.setValue("bar");

        FifoGroup group = new FifoGroup();
        group.setScope(Scope.REQUEST);
        group.addModifier(headerModifier);
        group.addModifier(queryStringModifier);

        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);
        group.writeJson(writer);

        JsonParser parser = new JsonParser();
        JsonElement generatedJson = parser.parse(stringWriter.toString());

        String json = "{\n" +
                "  \"fifo.Group\": {\n" +
                "    \"scope\": [\n" +
                "      \"request\"\n" +
                "    ],\n" +
                "    \"modifiers\": [\n" +
                "      {\n" +
                "        \"header.Modifier\": {\n" +
                "          \"name\": \"X-Name\",\n" +
                "          \"value\": \"Mod-Run\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"querystring.Modifier\": {\n" +
                "          \"name\": \"foo\",\n" +
                "          \"value\": \"bar\"\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JsonElement desiredJson = parser.parse(json);

        assertEquals(generatedJson, desiredJson);
    }
}
