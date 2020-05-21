package com.example.soaprunnerreact.controllers;

import com.example.soaprunnerreact.domains.SoapObject;
import com.example.soaprunnerreact.utils.SoapRunnerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.HttpURLConnection;
import java.util.Map;

@CrossOrigin("http://localhost:3000")
@org.springframework.web.bind.annotation.RestController
public class RestController {

  private Logger logger = LoggerFactory.getLogger(RestController.class);

  @PostMapping(path = "/")
  public SoapObject test(@RequestParam Map<String, String> data) {
    SoapObject requestObject = new SoapObject();
    requestObject.setAddress(data.get("address"));
    requestObject.setMessageRequest(data.get("messageRequest"));

    HttpURLConnection connection =
        SoapRunnerUtils.prepareRequest(
            requestObject.getMessageRequest(), requestObject.getAddress());

    return SoapRunnerUtils.prepareResponse(connection, requestObject);

    //logger.info("SoapObject ::: " + data);
    //return response;
  }
}
