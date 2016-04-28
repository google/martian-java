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

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Client acts against a known, running instance of a Martian Proxy
 * (https://github.com/google/martian). Client allows configuration of modifiers
 * and traffic verification.
 * <p/>
 * Communication with Martian is handled over HTTP.
 */
public class Client {
  public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
  private OkHttpClient client;
  private String resetVerificationsPath;
  private String checkVerificationsPath;
  private String configurePath;
  private String proxyHost;
  private Hashtable<String, Logger> loggers;

  /**
   * Class constructor.
   *
   * @param uri host:port of running instance of Maritan Proxy to act upon
   * @throws URISyntaxException    if uri does not parse successfully
   * @throws MalformedURLException if uri is malformed and cannot be parsed
   **/
  public Client(String uri) throws URISyntaxException, MalformedURLException {
    URI proxy = URI.create("http://" + uri);
    InetSocketAddress addr = InetSocketAddress.createUnresolved(proxy.getHost(), proxy.getPort());
    this.client = new OkHttpClient();
    this.client.setProxy(new Proxy(Proxy.Type.HTTP, addr));
    this.proxyHost = "martian.proxy";
    this.resetVerificationsPath = "/verify/reset";
    this.checkVerificationsPath = "/verify";
    this.configurePath = "/configure";
    this.loggers = new Hashtable<String, Logger>();
  }

  /**
   * Sets the timeout for sending configuration messages to Martian.
   *
   * @param timeout number of time units to wait before timing out
   * @param units   units of time measurement
   **/
  public void setTimeout(long timeout, TimeUnit units) {
    this.client.setConnectTimeout(timeout, units);
    this.client.setReadTimeout(timeout, units);
    this.client.setWriteTimeout(timeout, units);
  }

  /**
   * Clears any in-memory traffic verification logs.
   *
   * @throws IOException if an error occurs during input or output
   **/
  public void resetVerifications() throws IOException {
    Request request =
        new Request.Builder()
            .url(getMartianUrl(this.resetVerificationsPath))
            .post(RequestBody.create(JSON, ""))
            .build();

    Response response = this.client.newCall(request).execute();
    response.body().close();
  }

  /**
   * Returns the in-memory list of traffic verification messages as a
   * List<String>.
   *
   * @return all in-memory traffic verification messages
   * @throws IOException if an error occurs during input or output
   **/
  public List<String> checkVerifications() throws IOException {
    Request request = new Request.Builder().url(getMartianUrl(this.checkVerificationsPath)).build();

    Response response = this.client.newCall(request).execute();
    if (!response.isSuccessful()) {
      throw new IOException("Error on GET " + this.checkVerificationsPath + ": " + response);
    }

    String json = response.body().string();
    response.body().close();

    VerificationParser parser = new VerificationParser();
    return parser.fromJson(json);
  }

  /**
   * Configures a running instance of Martian with a modifier. Subsequent
   * calls wil overwrite any previous configurations.
   *
   * @param  modifier    Martian request or response modifier
   * @throws IOException if an error occurs during input or output
   **/
  public void configure(Modifier modifier) throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter writer = new JsonWriter(stringWriter);
    modifier.writeJson(writer);

    RequestBody body = RequestBody.create(JSON, stringWriter.toString());
    Request request =
        new Request.Builder().url(getMartianUrl(this.configurePath)).post(body).build();

    Response response = this.client.newCall(request).execute();
    response.body().close();
    if (!response.isSuccessful()) {
      throw new IOException("Error on POST " + this.configurePath + ": " + response);
    }
  }

  /**
   * Returns the in-memory traffic logs in HAR format, which is a JSON message returned as a
   * String
   *
   * @param name unique name of log
   * @return traffic logs in HAR format
   * @throws IOException if an error occurs during input or output
   **/
  public String retrieveLogs(String name) throws IOException {
    return this.loggers.get(name).retrieveLogs();
  }

  /**
   * Clears in-memory traffic logs based on name.
   *
   * @param name unique name of log.
   * @throws IOException if an error occurs during input or output
   **/
  public void resetLogs(String name) throws IOException {
    this.loggers.get(name).resetLogs();
  }


  /**
   * Registers a logger with the client with the paths for retrieval and clearing the logs.
   *
   * @param name name of the logger which is unique to the client
   * @param resetPath path to send a POST request to that clears in-memory logs
   * @param retrievePath path to send a GET request to that retrieves HAR logs
   **/
  public void registerLogger(String name, String resetPath, String retrievePath)
      throws MalformedURLException {
    Logger logger = new Logger(getMartianUrl(resetPath), getMartianUrl(retrievePath));
    this.loggers.put(name, logger);
  }

  /**
   * Removes the logger from the client's map of loggers.
   *
   * @param name name of the logger to deregister
   **/
  public void deregisterLogger(String name) {
    this.loggers.remove(name);
    }

  /**
   * Sets the path that requests to retrieve in-memory verification messages will be sent to.
   * This only sets the path for the client, it does not change the path within Martian; that
   * setup is handled wherever Martian itself is started.
   *
   * @param path path to send request to retrieve in-memory logss from Martian
   **/
  public void setCheckVerificationsPath(String path) {
    this.checkVerificationsPath = path;
  }

  /**
   * Sets the path that requests to configure Martian will be sent to. This only
   * sets the path for the client, it does not change the path within Martian; that
   * setup is handled wherever Martian itself is started.
   *
   * @param path path to send request to configure Martian
   **/
  public void setConfigurePath(String path) {
    this.configurePath = path;
  }

  /**
   * Sets the path that requests to clear in-memory verification messages should
   * be sent to. This only sets the path for the client, it does not change the
   * path within Martian; that setup is handled wherever Martian itself is started.
   *
   * @param path path to send reset verification log requests to Martian
   **/
  public void setResetVerificationsPath(String path) {
    this.resetVerificationsPath = path;
  }

  /**
   * Returns a canonicalized URL as a string that can be used for Martian system requests. These
   * URLs will be of the form http://martian.proxy/{path}. The provided path should contian a
   * leading slash. Any forward slashes in the provided path will be handled as path delimeters.
   *
   * @param path Path portion of the Martian system request URL
   **/
  public String getMartianUrl(String path) {
    HttpUrl.Builder urlBuilder = new HttpUrl.Builder().scheme("http").host(this.proxyHost);
    String[] parts = path.split("/");
    for (String part : parts) {
      urlBuilder.addPathSegment(part);
    }

    return urlBuilder.build().toString();
  }
}
