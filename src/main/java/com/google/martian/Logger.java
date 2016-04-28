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

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.URL;

/**
 * Logger represents a single logging point within a running Martian Proxy. A
 * logger's name must be unique within the Client, and the resetLogsUrl and the
 * retrieveLogsUrl are the log reset and retrieval handlers defined within the
 * Martian Proxy.
 */
public class Logger {
  private String resetLogsUrl;
  private String retrieveLogsUrl;
  private OkHttpClient httpClient;

  public Logger(String resetLogsUrl, String retrieveLogsUrl) {
    this.resetLogsUrl = resetLogsUrl;
    this.retrieveLogsUrl = retrieveLogsUrl;
    this.httpClient = new OkHttpClient();
  }

  /**
   * Clears in-memory logs.
   *
   * @throws IOException if an error occurs during HTTP POST to clear logs.
   **/
  public void resetLogs() throws IOException {
    Request request =
        new Request.Builder()
            .url(this.resetLogsUrl)
            .post(RequestBody.create(null, new byte[0]))
            .build();

    this.httpClient.newCall(request).execute();
  }

  /**
   * Returns in-memory logs as String.
   *
   * @throws IOException if an error occurs during HTTP GET to retrieve logs.
   **/
  public String retrieveLogs() throws IOException {
    Request request = new Request.Builder().url(this.retrieveLogsUrl).build();

    Response response = this.httpClient.newCall(request).execute();
    if (!response.isSuccessful()) {
      throw new IOException("Error on GET " + this.retrieveLogsUrl + ": " + response);
    }

    String logs = response.body().string();
    response.body().close();

    return logs;
  }
}
