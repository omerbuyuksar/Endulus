/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gebze.endulus;

import java.io.IOException;
import org.codehaus.jackson.map.ObjectMapper;

public class JbaseModel {

  protected String hataBilgisi = "";

  public String getHataBilgisi() {
    return hataBilgisi;
  }

  public void setHataBilgisi(String hataBilgisi) {
    this.hataBilgisi = hataBilgisi;
  }

  @Override
  public String toString() {
    try {
      return new ObjectMapper().writeValueAsString(this);
    } catch (IOException ex) {
      this.hataBilgisi = ex.getMessage();
      return "{hataBilgisi:\"" + this.hataBilgisi + "\"}";
    }
  }
}
