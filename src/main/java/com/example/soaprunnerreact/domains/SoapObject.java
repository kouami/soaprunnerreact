package com.example.soaprunnerreact.domains;

import lombok.Data;

@Data
public class SoapObject {

  private String address;
  private String messageRequest;
  private String messageResponse;
  //private String error;
}
