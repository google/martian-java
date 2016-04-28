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
 * Generates Martian Proxy JSON configuration messages that configures the proxy to skip the HTTP
 * roundtrip.
 **/
public class SkipRoundTripModifier implements Modifier {
  static final String NAME = "skip.RoundTrip";

  private Scope scope;

  // Class constructor.
  public SkipRoundTripModifier() {
    this.scope = Scope.REQUEST;
  }

  /**
   * Writes a Status Modifier JSON configuration message for
   * Martian Proxy to writer.
   *
   * @throws IOException if an error occurs during input or output
   **/
  @Override
  public void writeJson(JsonWriter writer) throws IOException {
      writer.beginObject();
      writer.name(NAME);
      writer.beginObject();
      this.scope.writeJson(writer);
      writer.endObject();
      writer.endObject();
  }
}
