package com.example.soaprunnerreact.utils;

import com.example.soaprunnerreact.domains.SoapObject;

import javax.net.ssl.*;
import javax.xml.soap.*;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class SoapRunnerUtils {

  //https://automationrhapsody.com/send-soap-request-over-https-without-valid-certificates/

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


  public String sendSoapRequest(String endpointUrl, String request) {
    try {
      final boolean isHttps = endpointUrl.toLowerCase().startsWith("https");
      HttpsURLConnection httpsConnection = null;
      // Open HTTPS connection
      if (isHttps) {
        // Create SSL context and trust all certificates
        SSLContext sslContext = SSLContext.getInstance("SSL");
        TrustManager[] trustAll
                = new TrustManager[] {new TrustAllCertificates()};
        sslContext.init(null, trustAll, new java.security.SecureRandom());
        // Set trust all certificates context to HttpsURLConnection
        HttpsURLConnection
                .setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        // Open HTTPS connection
        URL url = new URL(endpointUrl);
        httpsConnection = (HttpsURLConnection) url.openConnection();
        // Trust all hosts
        httpsConnection.setHostnameVerifier(new TrustAllHosts());
        // Connect
        httpsConnection.connect();
      }
      // Send HTTP SOAP request and get response
      SOAPConnection soapConnection
              = SOAPConnectionFactory.newInstance().createConnection();

      SOAPMessage soapMessageRequest = getSoapMessageFromString(request);

      SOAPMessage response = soapConnection.call(soapMessageRequest, endpointUrl);
      // Close connection
      soapConnection.close();
      // Close HTTPS connection
      if (isHttps) {
        httpsConnection.disconnect();
      }
      return getXMLFromSoapMessage(response);
    } catch (SOAPException | IOException
            | NoSuchAlgorithmException | KeyManagementException ex) {
      // Do Something
    }
    return null;
  }

  private String getXMLFromSoapMessage(SOAPMessage soapMessage) {
    final StringWriter sw = new StringWriter();

    try {
      TransformerFactory.newInstance().newTransformer().transform(
              new DOMSource(soapMessage.getSOAPPart()),
              new StreamResult(sw));
    } catch (TransformerException e) {
      throw new RuntimeException(e);
    }
    System.out.println(sw.toString());
    return sw.toString();
  }

  private SOAPMessage getSoapMessageFromString(String xml) throws SOAPException, IOException {
    MessageFactory factory = MessageFactory.newInstance();
    SOAPMessage message = factory.createMessage(new MimeHeaders(), new ByteArrayInputStream(xml.getBytes(Charset.forName("UTF-8"))));
    return message;
  }

  /**
   * Dummy class implementing HostnameVerifier to trust all host names
   */
  private static class TrustAllHosts implements HostnameVerifier {
    public boolean verify(String hostname, SSLSession session) {
      return true;
    }
  }

  /**
   * Dummy class implementing X509TrustManager to trust all certificates
   */
  private static class TrustAllCertificates implements X509TrustManager {
    public void checkClientTrusted(X509Certificate[] certs, String authType) {
    }

    public void checkServerTrusted(X509Certificate[] certs, String authType) {
    }

    public X509Certificate[] getAcceptedIssuers() {
      return null;
    }
  }
}
