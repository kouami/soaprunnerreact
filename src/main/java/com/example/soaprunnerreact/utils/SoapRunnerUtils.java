package com.example.soaprunnerreact.utils;

import com.example.soaprunnerreact.domains.SoapObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SoapRunnerUtils {

  /**
   * @param message
   * @param url
   * @return
   */
  public static HttpURLConnection prepareRequest(String message, String url) {
    DataOutputStream wr = null;
    HttpURLConnection connection = null;
    try {
      URL endpointUrl = new URL(url); // add url validation here as a regex.
      connection = (HttpURLConnection) endpointUrl.openConnection();
      connection.setRequestMethod("POST");
      connection.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");
      connection.setDoOutput(true);
      wr = new DataOutputStream(connection.getOutputStream());
      wr.writeBytes(message);

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (wr != null) {
          wr.flush();
          wr.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return connection;
  }

  /**
   * @param connection
   * @return
   */
  public static SoapObject prepareResponse(HttpURLConnection connection, SoapObject soapRequestObject) {
    BufferedReader in;
    StringBuilder response = new StringBuilder();


    try {
      in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      in.lines().forEachOrdered(response::append);
      in.close();
    } catch (IOException e) {
      e.printStackTrace();
      soapRequestObject.setError(e.getMessage());
    }

    soapRequestObject.setMessageResponse(response.toString());
    return soapRequestObject;
  }
}
