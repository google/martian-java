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
public class BodyModifierTest {
    @Test
    public void testWriteJson() throws Exception {
        BodyModifier modifier = new BodyModifier();
        modifier.setBody("base64encoded=");
        modifier.setContentType("text/plain");
        modifier.setScope(Scope.REQUEST_AND_RESPONSE);

        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        modifier.writeJson(jsonWriter);

        String actualJson = stringWriter.toString();

        JsonParser parser = new JsonParser();
        JsonElement actual = parser.parse(actualJson);

        String expectedJson = "{" +
                                "\"body.Modifier\": {" +
                                  "\"scope\": ["+
                                    "\"request\","+
                                    "\"response\""+
                                  "],"+
                                  "\"body\": \"base64encoded=\","+
                                  "\"contentType\": \"text/plain\""+
                                "}"+
                              "}";

        JsonElement expected = parser.parse(expectedJson);

        assertEquals(expected, actual);
    }
}
