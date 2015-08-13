/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gebze.endulus;

/**
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

    public JSdtpModel(long id, String stdpKodu, String konu, long ustSdtpId, int sdtpTuru) {
        this.id = id;
        this.stdpKodu = stdpKodu;
        this.konu = konu;
        this.ustSdtpId = ustSdtpId;
        this.sdtpTuru = sdtpTuru;
    }
    public JSdtpModel( int sdtpTuru) {
        this.sdtpTuru = sdtpTuru;
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
