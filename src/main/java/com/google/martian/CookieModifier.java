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
import org.joda.time.DateTime;

import java.io.IOException;

/**
 * Generates Martian Proxy JSON configuration meessages to modify
 * HTTP request and response cookies. Portions of the cookie that are not
 * explicitly modified will retain their values.
 **/
public class CookieModifier implements Modifier {
    static final String NAME = "cookie.Modifier";

    private Scope scope;
    private String name;
    private String value;
    private String path;
    private String domain;
    private DateTime expires;
    private Boolean secure;
    private Boolean httpOnly;
    private Integer maxAge;

    /**
     * Class constructor.
     **/
    public CookieModifier() {
        this.scope = Scope.DEFAULT;
    }

    /**
     * Writes a Cookie Modifier JSON configuration message for
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
        if (this.name != null) {
            writer.name("name").value(this.name);
        }
        if (this.value != null) {
            writer.name("value").value(this.value);
        }
        if (this.path != null) {
            writer.name("path").value(this.path);
        }
        if (this.domain != null) {
            writer.name("domain").value(this.domain);
        }
        if (this.expires != null) {
            writer.name("expires").value(this.expires.toString());
        }
        if (this.secure != null) {
            writer.name("secure").value(this.secure);
        }
        if (this.httpOnly != null) {
            writer.name("httpOnly").value(this.httpOnly);
        }
        if (this.maxAge != null) {
            writer.name("maxAge").value(this.maxAge);
        }
        writer.endObject();
        writer.endObject();
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setExpires(DateTime expires) {
        this.expires = expires;
    }

    public void setSecure(Boolean secure) {
        this.secure = secure;
    }

    public void setHttpOnly(Boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }
}
