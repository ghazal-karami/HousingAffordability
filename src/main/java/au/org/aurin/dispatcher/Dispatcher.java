
// http://dyutiman.wordpress.com/2011/04/13/rest-template-using-apache-httpclient/

package au.org.aurin.dispatcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

import java.net.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.impl.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.client.params.*;
import org.apache.http.entity.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;

import java.util.*;
import java.net.*;
import java.util.regex.*;
import com.fasterxml.jackson.databind.*;


public class Dispatcher extends HttpServlet {
  public void doGet(  HttpServletRequest request,
                      HttpServletResponse response)
    throws ServletException, IOException {

    PrintWriter out = response.getWriter();

    if (!isLoggedIn(request)) { // only logged in user can use this service
      response.setStatus(500);
      out.print("{ \"error\": \"Not logged in\"}");
      out.flush();
      out.close();
      return;
    }

    String url = request.getParameter("url");

    if (!isValidUrl(url)) {
      response.setStatus(500);
      out.print("{ \"error\": \"Untrusted Url\"}");
      out.flush();
      out.close();
      return;
    }

    Enumeration qenum = request.getParameterNames();
    String qs = "";

    while (qenum.hasMoreElements()) {
      String name = (String) qenum.nextElement();
      if (name.equals("url")) continue;
      if (name.equals("callback")) continue;
      String value = request.getParameter(name); // assuming a single value
      qs += URLEncoder.encode(name + "=" + value, "utf-8");
      if (qenum.hasMoreElements()) {
        qs += "&";
      }
    }
    url += "?"+qs;
    System.out.println(url);

    HttpClient httpClient = new DefaultHttpClient();

    try {
      String callback = request.getParameter("callback");
      Boolean isCallback = (callback != null && !callback.isEmpty());
      HttpGet httpGet = new HttpGet(url);

      HttpResponse httpRes = httpClient.execute(httpGet);
      int statusCode = httpRes.getStatusLine().getStatusCode();

      response.setStatus(statusCode);
      if (statusCode != HttpStatus.SC_OK) {
        out.print("{ \"Method failed\": \"" + httpRes.getStatusLine() + "\"}");
      }
      if (isCallback) { out.print(callback + "("); }
      out.print(httpResRead(httpRes));
      if (isCallback) { out.print(");"); }
    } catch (IOException e) {
      e.printStackTrace();
      response.setStatus(500);
      out.print("{ \"error\": \"IOException\"}");
    } finally {
      if (httpClient != null) httpClient.getConnectionManager().shutdown();
    }
    out.flush();
    out.close();
  }

  public void doPost(  HttpServletRequest request,
                      HttpServletResponse response)
    throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    response.setContentType("text/plain");

    if (!isLoggedIn(request)) { // only logged in user can use this service
      response.setStatus(500);
      out.print("{ \"error\": \"Not logged in\"}");
      out.flush();
      out.close();
      return;
    }

    DataInputStream inputStream = new DataInputStream(request.getInputStream());
    int inputLength = request.getContentLength();
    byte dataBytes[] = new byte[inputLength];
    int byteRead = 0;
    int totalBytesRead = 0;
    while (totalBytesRead < inputLength) {
      byteRead = inputStream.read(dataBytes, totalBytesRead, inputLength);
      totalBytesRead += byteRead;
    }
    String postData = new String(dataBytes);
    ObjectMapper mapper = new ObjectMapper(); // create once, reuse
    HttpClient httpClient = null;

    try {

      Map<String, Object> jsonData = mapper.readValue(postData, Map.class);

      /* Request body is converted to jsonData
       *
       * { "url": API service endpoint,
       *   "method": "get" or "post" or "put" or "delete",
       *   "params": {params to be sent to API},
       *   "headers": {additional request headers}
       * }
       */

      String url = (String)jsonData.get("url");

      if (!isValidUrl(url)) {
        response.setStatus(500);
        out.print("{ \"error\": \"Untrusted Url\"}");
        out.flush();
        out.close();
        return;
      }
      String method = ((String)jsonData.get("method")).toLowerCase();
      String params = mapper.writeValueAsString(jsonData.get("params"));

      HttpRequestBase httpMeth;
      Boolean entityRequest = false;

      if (method.equalsIgnoreCase("post")) {
        httpMeth = new HttpPost(url);
        entityRequest = true;
      } else if (method.equalsIgnoreCase("put")) {
        httpMeth = new HttpPut(url);
        entityRequest = true;
      } else if (method.equalsIgnoreCase("delete")) {
        httpMeth = new HttpDelete(url);
      } else {
        httpMeth = new HttpGet(url);
      }

      httpClient = new DefaultHttpClient();
      HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 10000);

      if (   params != null
          && params.length() != 0
          && !params.equals("{}")
          && entityRequest
        ) {
        System.out.println(params);
        StringEntity entity = new StringEntity(params, "UTF-8");
        BasicHeader basicHeader = new BasicHeader(HTTP.CONTENT_TYPE, "application/json");
        HttpEntityEnclosingRequestBase er = (HttpEntityEnclosingRequestBase)httpMeth;
        entity.setContentType(basicHeader);
        er.setEntity(entity);
      }

      Map <String, String> headers = (Map <String, String>)jsonData.get("headers");
      if (headers != null && headers.size() != 0) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
          httpMeth.setHeader(entry.getKey(), entry.getValue());
        }
      }

      httpMeth.getParams().setBooleanParameter("http.protocol.expect-continue", false);
      HttpResponse httpRes = httpClient.execute(httpMeth);
      out.print(httpResRead(httpRes));
    } catch (IOException e) {
      e.printStackTrace();
      response.setStatus(500);
      out.print("{ \"error\": \"IOException\"}");
    } finally {
      if (httpClient != null) httpClient.getConnectionManager().shutdown();
    }
    out.flush();
    out.close();
  }

  public Boolean isLoggedIn(HttpServletRequest request) {
    HttpSession session = request.getSession();
    String userId = (String)session.getAttribute("userId");
    if (   userId  == null
        || userId.length() == 0
       ) {
      return false;
    } else {
      return true;
    }
  }

  public Boolean isValidUrl(String url) {

    String[] trustedPaths = {
      "/node"
    , "/data_registration/datasets"
    , "/datastore"
    , "/aurin-data-provider"
    , "/mservices"
    , "/R/call"
    , "/workflow-api"
    };

    String[] trustedHosts = {
      "dev-api.aurin.org.au"
    , "api.aurin.org.au"
    , "beta.opencpu.org"

    };

    int i = 0, j;
    if (url != null && !url.isEmpty()) {
      try {
        URL parseUrl = new URL(url);
        String urlHost = parseUrl.getHost();
        for (; i < trustedHosts.length; i++) {
          String urlPath = parseUrl.getPath();
          if (trustedHosts[i].equalsIgnoreCase(urlHost)) {
            for (j = 0; j < trustedPaths.length; j++){
              System.out.println(urlPath + ':' + trustedPaths[j]);
              if (urlPath.startsWith(trustedPaths[j])) {
                return true;
              }
            }
          }
        }
      } catch(MalformedURLException e) {
        return false;
      }
    }
    return false;
  }

  private String httpResRead(HttpResponse response) throws IOException {

    HttpEntity entity = response.getEntity();
    if (entity == null) return "";

    InputStream inputStream = entity.getContent();
    StringBuilder sb = new StringBuilder();
    BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
    for (String line = rd.readLine(); line != null; line = rd.readLine()) {
      sb.append(line);
    }
    inputStream.close();

    return sb.toString();
  }
}