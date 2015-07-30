/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gebze.endulus;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;


/**
 *
 * @author Omer
 */
public class FileModel {
    
    private String id;
    private String klasorPath;
    private int boyut;
    private String fileName;
    private String kaydedenKullaniciAdi;
    private int kaydedenKullaniciId;
    private int dizinId;
    private String dosyaTuru;
    private boolean sonuncuMu;
    private String table;
    private String fileId;
    private String uuid;
    private String groupId;
    
    

    public String getFileName() {
        return fileName;
    }


    public String getTable() {
        return table;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKlasorPath() {
        return klasorPath;
    }

    public void setKlasorPath(String klasorPath) {
        this.klasorPath = klasorPath;
    }

    public int getBoyut() {
        return boyut;
    }

    public void setBoyut(int boyut) {
        this.boyut = boyut;
    }

    public String getKaydedenKullaniciAdi() {
        return kaydedenKullaniciAdi;
    }

    public void setKaydedenKullaniciAdi(String kaydedenKullaniciAdi) {
        this.kaydedenKullaniciAdi = kaydedenKullaniciAdi;
    }

    public int getKaydedenKullaniciId() {
        return kaydedenKullaniciId;
    }

    public void setKaydedenKullaniciId(int kaydedenKullaniciId) {
        this.kaydedenKullaniciId = kaydedenKullaniciId;
    }

    public int getDizinId() {
        return dizinId;
    }

    public void setDizinId(int diziId) {
        this.dizinId = diziId;
    }

    public String getDosyaTuru() {
        return dosyaTuru;
    }

    public void setDosyaTuru(String dosyaTuru) {
        this.dosyaTuru = dosyaTuru;
    }

    public boolean isSonuncuMu() {
        return sonuncuMu;
    }

    public void setSonuncuMu(boolean sonuncuMu) {
        this.sonuncuMu = sonuncuMu;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setTable(String table) {
        this.table = table;
    }


    public FileModel(){
        
    }
    
    public FileModel( String fileName ) {
        this.id = getRandomId();
        this.fileName = fileName;
    }
    
    private String getRandomId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
    public String getId(){
        return id;
    }
    public StreamedContent getImage()  {
        
        try {
            FacesContext context = FacesContext.getCurrentInstance();

            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            InputStream input;
            if (dosyaTuru.contentEquals("PDF")) {

                input = externalContext.getResourceAsStream("/resources/images/pdf.png");
            } else {
                input = externalContext.getResourceAsStream("/resources/images/any.jpg");
            }
            return new DefaultStreamedContent(input);
        } catch (Exception e) {
            return null;
        }
    }
    
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
    

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FileModel other = (FileModel) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

    
    
    
    
}
