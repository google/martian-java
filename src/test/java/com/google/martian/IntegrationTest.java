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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class IntegrationTest {
    private IntegrationHandler handler;
    private HttpServer httpServer;
    private CountDownLatch latch;

    @Before
    public void setupServer() throws IOException {
        // Let the OS assign a free port.
        InetSocketAddress address = new InetSocketAddress(0);
        this.httpServer = HttpServer.create(address, 0);

        // Block until the handler has been called and assertions have been
        // executed.
        this.latch = new CountDownLatch(1);
        this.handler = new IntegrationHandler(this.latch);

        this.httpServer.createContext("/martian/configure", this.handler);
        this.httpServer.createContext("/verify/reset", this.handler);
        this.httpServer.createContext("/verify", this.handler);

        this.httpServer.start();
    }

    @Test
    public void testConfigure() throws Exception {
        HeaderModifier headerModifier = new HeaderModifier();
        headerModifier.setName("Martian-Test");
        headerModifier.setValue("true");

        int port = this.httpServer.getAddress().getPort();
        String url = String.format("http://localhost:%d", port);
        Client client = new Client(url);

        client.setConfigurePath("/martian/configure");

        // Local requests should be fast.
        client.setTimeout(100, TimeUnit.MILLISECONDS);

        client.configure(headerModifier);

        assertTrue("timeout waiting for handler to run", this.latch.await(1, TimeUnit.SECONDS));
        assertEquals(this.handler.method, "POST");
        assertEquals(this.handler.headers.get("Content-Type")
                .get(0), "application/json; charset=utf-8");

        JsonObject modifier = this.handler.json.getAsJsonObject("header.Modifier");

        assertEquals(modifier.get("name").getAsString(), "Martian-Test");
        assertEquals(modifier.get("value").getAsString(), "true");
    }

    @Test
    public void testVerify() throws Exception {
        int port = httpServer.getAddress().getPort();
        String url = String.format("http://localhost:%d", port);
        Client client = new Client(url);

        client.setTimeout(100, TimeUnit.MILLISECONDS);
        client.setResetVerificationsPath("/verify/reset");
        client.resetVerifications();

        assertTrue("timeout waiting for handler to run", this.latch.await(1, TimeUnit.SECONDS));
        assertEquals(this.handler.method, "POST");

        client.setCheckVerificationsPath("/verify");
        client.checkVerifications();

        assertTrue("timeout waiting for handler to run", this.latch.await(1, TimeUnit.SECONDS));
        assertEquals(this.handler.method, "GET");
    }

    @After
    public void stopServer() {
        httpServer.stop(0);
    }

    static class IntegrationHandler implements HttpHandler {
        private Headers headers;
        private CountDownLatch latch;
        private JsonObject json;
        private String method;

        IntegrationHandler(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                this.method = exchange.getRequestMethod();
                this.headers = exchange.getRequestHeaders();

                InputStreamReader reader =
                        new InputStreamReader(exchange.getRequestBody(), "UTF-8");

                JsonParser parser = new JsonParser();
                this.json = parser.parse(reader).getAsJsonObject();
            } finally {
                exchange.sendResponseHeaders(200, 0);
                exchange.close();

                latch.countDown();
            }
        }
    }
}
