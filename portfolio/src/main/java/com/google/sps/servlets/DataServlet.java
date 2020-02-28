// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.ArrayList;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  ArrayList<String> values = new ArrayList<String>();

  /*@Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String json = convertToJson(values);
    
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }*/

  /**
   * Converts a ServerStats instance into a JSON string using manual String concatentation.
   */
  /*private String convertToJson(ArrayList<String> values) {
    String json = "{";
    json += "\"CommentOne\": ";
    json += "\"" + values.get(0) + "\"";
    json += ", ";
    json += "\"CommentTwo\": ";
    json += "\"" + values.get(1) + "\"";
    json += ", ";
    json += "\"CommentThree\": ";
    json += "\"" + values.get(2) + "\"";
    json += "}";
    return json;
  }*/

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the input from the form.
    String text = getParameter(request, "Comment-Input", "");
    boolean upperCase = Boolean.parseBoolean(getParameter(request, "upper-case", "false"));
    boolean lowerCase = Boolean.parseBoolean(getParameter(request, "lower-case", "false"));

    // Convert the text to upper case.
    if (upperCase) {
      text = text.toUpperCase();
    }

    // Convert the text to lower case.
    if (lowerCase) {
      text = text.toLowerCase();
    }

    // Break the text into individual words.
    String[] words = text.split("\\s*,\\s*");

    // Respond with the result.
    response.setContentType("text/html;");
    response.getWriter().println(Arrays.toString(words));
  }

  /**
   * @return the request parameter, or the default value if the parameter
   *         was not specified by the client
   */
  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
}
