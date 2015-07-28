/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gebze.endulus;

/**
 *
 * @author Feyyaz Toptaş
 */
public class JSdtpModel
        extends JbaseModel {

  private long id;
  private String stdpKodu;
  private String konu;
  private long ustSdtpId;
  private int sdtpTuru;

  public JSdtpModel() {
  }

  public JSdtpModel(String hata) {
    hataBilgisi = hata;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getStdpKodu() {
    return stdpKodu;
  }

  public void setStdpKodu(String stdpKodu) {
    this.stdpKodu = stdpKodu;
  }

  public String getKonu() {
    return konu;
  }

  public void setKonu(String konu) {
    this.konu = konu;
  }

  public long getUstSdtpId() {
    return ustSdtpId;
  }

  public void setUstSdtpId(long ustSdtpId) {
    this.ustSdtpId = ustSdtpId;
  }

  public int getSdtpTuru() {
    return sdtpTuru;
  }

  public void setSdtpTuru(int sdtpTuru) {
    this.sdtpTuru = sdtpTuru;
  }

}
