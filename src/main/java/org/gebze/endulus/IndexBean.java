/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gebze.endulus;

import com.mongodb.BasicDBObject;
import java.io.InputStream;
import java.util.List;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Ömer
 */
@ManagedBean
@ApplicationScoped
public class IndexBean implements Serializable {

    private List<FileModel> files;
    private FileModel selectedFile;
    private TreeNode root;
    private SdtpTreeNode selectedNode;
    private boolean connected = false;
    private MongoDbService mydb;
    private String console;
    private Date firstDate;
    private Date lastDate;

    private SdtpService sdtpService;
    private String searchText;
    private String searchDosyaIsmi;
    private String searchKonusu;
    private String searchKodu;
    private boolean searchSonmuTik = true;
    private String searchDosyaturu;
    private String searchKaydedenKullanici;
    private String searchTablo;

    public int buttonConnectDb() {
        try {
            mydb.connectToMongoDB("mydb", 27017, "192.168.77.25", "nico", "nico");
            //files = mydb.getAllFiles("veriler");
            addMessage("MongoDB'ye bağlanıldı");
            addSdtpNodes();
            connected = true;
        } catch (Exception e) {
            addMessage("Bağlantı Kurulamadı !");
        }

        return 0;
    }

    public int buttonLogout() {
        try {
            files.clear();
            root = new DefaultTreeNode("Root", null);
            connected = false;
            addMessage("Çıkış yapıldı");
        } catch (Exception e) {
            addMessage("Çıkış yapılamadı !");
        }

        return 0;
    }

    public void buttonAction() {
        addMessage("button action!");
    }

    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    @PostConstruct
    public void init() {
        Set<String> collectionNameList = null;
        List<String> list = new ArrayList();
        mydb = new MongoDbService();
        mydb.setCursorLimit(100);
        files = new ArrayList<>();
        sdtpService = new SdtpService();
        root = new DefaultTreeNode("Root", null);
    }

    public void searchByName() {
        if (!connected) {
            addMessage("Önce DB'ye bağlanın !");
            return;
        }
        files = mydb.getFilesByName("veriler", getSearchText());
        addMessage("" + files.size() + " Dosya Bulundu.");
    }

    public void searchAdvanced() {
        if (!connected) {
            addMessage("Önce DB'ye bağlanın !");
            return;
        }
        files = mydb.getFilesAdvanced("veriler", getAdvancedSearchValues(),firstDate,lastDate);
        addMessage("" + files.size() + " Dosya Bulundu.");
    }

    public BasicDBObject getAdvancedSearchValues() {
        BasicDBObject obj = new BasicDBObject();

        List<JSdtpModel> list = null;
        if (getSearchDosyaIsmi().length() > 0) {
            obj.append("isim", Pattern.compile(getSearchDosyaIsmi(), Pattern.CASE_INSENSITIVE));
        }
        if (getSearchKaydedenKullanici().length() > 0) {
            obj.append("kaydedenUser_username", Pattern.compile(getSearchKaydedenKullanici(), Pattern.CASE_INSENSITIVE));
        }
        if (!getSearchDosyaturu().matches("ALL")) {
            obj.append("dosyaTuru", Pattern.compile(getSearchDosyaturu(), Pattern.CASE_INSENSITIVE));
        }
        if (isSearchSonmuTik()) {
            obj.append("sonuncu", isSearchSonmuTik());
        }
        if (getSearchTablo().length() > 0) {
            obj.append("table", Pattern.compile(getSearchTablo(), Pattern.CASE_INSENSITIVE));
        }
        if (getSearchKodu().length() > 0) {
            obj.append("klasorPath", Pattern.compile(getSearchKodu(), Pattern.CASE_INSENSITIVE));
        }
        if (getSearchKonusu().length() > 0) {
            list = sdtpService.findWithKonu(getSearchKonusu());
        }
        if (list != null) {
            for (JSdtpModel list1 : list) {
                obj.append("klasorPath", Pattern.compile("" + list1.getStdpKodu(), Pattern.CASE_INSENSITIVE));
            }
        }
        return obj;
    }

    private void addSdtpNodes() {

        List<JSdtpModel> list = sdtpService.getSdtp();

        if (list == null) {
            addMessage("SDTP Servise Bağlanılamadı !");

            return;
        }

        for (JSdtpModel list1 : list) {
            root.getChildren().add(new SdtpTreeNode(list1, list1.getStdpKodu()+" "+list1.getKonu()));
        }

    }

    public StreamedContent getDownloadFile(FileModel file) {
        InputStream input;
        String str = null;
        try {
            input = mydb.getFileInputStream(file.getId(), file.getTable());
            str = MongoDbService.displayContentType(file.getFileName());
        } catch (Exception e) {
            e.printStackTrace();
            addMessage("Dosya Indirilemedi !");
            return null;
        }
        return new DefaultStreamedContent(input, str, file.getFileName());
    }
    

    public void onNodeSelect(NodeSelectEvent event) {

        List<JSdtpModel> list;
        List<Long> llist;
        String str;
        SdtpTreeNode newJTModel;
        String kodu;
        //sdtp turu bakılcak

        if (selectedNode.isDizin()) {

            files.clear();
            System.out.println("dizin_id: " + selectedNode.getDizin_id());
            files = mydb.getFilesByDizinId("veriler", selectedNode.getDizin_id());
            return;
        }

        if (selectedNode.getSdtpmodel().getSdtpTuru() != 2) {
            files.clear();
            if (selectedNode.isOpened()) {
                return;
            }
            System.out.println("" + selectedNode.getSdtpmodel().getSdtpTuru() + "  " + selectedNode.getSdtpmodel().getStdpKodu());

            list = sdtpService.findSdtpWithId("" + selectedNode.getSdtpmodel().getId());

            if (list == null) {
                addMessage("SDTP Servise Bağlanılamadı !");
                return;
            }
            for (JSdtpModel list1 : list) {
                selectedNode.getChildren().add(new SdtpTreeNode(list1, list1.getKonu()));
            }
            selectedNode.setOpened(true);
        } else {
            System.out.println("" + selectedNode.getSdtpmodel().getSdtpTuru() + "  " + selectedNode.getSdtpmodel().getStdpKodu());
            System.out.flush();
            files.clear();
            llist = sdtpService.getEbysDizin("" + selectedNode.getSdtpmodel().getId());
            if (llist == null) {
                addMessage("SDTP Servise Bağlanılamadı !");
                return;
            }
            List<FileModel> modelList;

            if (!mydb.isConnected()) {
                try {
                    mydb.connectToMongoDB("mydb", 27017, "192.168.77.25", "nico", "nico");
                    connected = true;
                } catch (UnknownHostException ex) {
                    addMessage("MongoDB'ye Bağlanılamadı !");
                    Logger.getLogger(IndexBean.class.getName()).log(Level.SEVERE, null, ex);
                    return;
                }
            }
            for (Long llist1 : llist) {
                modelList = mydb.getFilesByDizinId("veriler", llist1.intValue());
                for (FileModel modelList1 : modelList) {
                    files.add(modelList1);
                }
                if (selectedNode.isOpened() || modelList.isEmpty()) {
                    continue;
                }

                str = files.get(files.size() - 1).getKlasorPath();
                kodu = selectedNode.getSdtpmodel().getStdpKodu().replace('.', '/');
                str = str.replaceFirst(kodu, "");
                newJTModel = new SdtpTreeNode(files.get(files.size() - 1).getDizinId(), str);
                newJTModel.setIsDizin(true);
                newJTModel.setDizin_id(llist1.intValue());
                selectedNode.getChildren().add(newJTModel);
            }
            selectedNode.setOpened(true);
        }

    }

    public List<FileModel> getFiles() {
        return files;
    }

    public FileModel getSelectedFile() {
        return selectedFile;
    }

    public void setSelectedFile(FileModel selectedFile) {
        this.selectedFile = selectedFile;
    }

    public void save() {
        addMessage("Success Data saved");
    }

    public void update() {
        addMessage("Data updated");
    }

    public void delete() {
        addMessage("Data deleted");
    }

    public SdtpTreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(SdtpTreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public TreeNode getRoot() {
        return root;
    }

    public String getConsole() {
        return console;
    }

    public void setConsole(String console) {
        this.console = console;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String getSearchKonusu() {
        return searchKonusu;
    }

    public void setSearchKonusu(String searchKonusu) {
        this.searchKonusu = searchKonusu;
    }

    public String getSearchKodu() {
        return searchKodu;
    }

    public void setSearchKodu(String searchKodu) {
        this.searchKodu = searchKodu;
    }

    public boolean isSearchSonmuTik() {
        return searchSonmuTik;
    }

    public void setSearchSonmuTik(boolean searchSonmuTik) {
        this.searchSonmuTik = searchSonmuTik;
    }

    public String getSearchDosyaturu() {
        return searchDosyaturu;
    }

    public void setSearchDosyaturu(String searchDosyaturu) {
        this.searchDosyaturu = searchDosyaturu;
    }

    public String getSearchKaydedenKullanici() {
        return searchKaydedenKullanici;
    }

    public void setSearchKaydedenKullanici(String searchKaydedenKullanici) {
        this.searchKaydedenKullanici = searchKaydedenKullanici;
    }

    public String getSearchTablo() {
        return searchTablo;
    }

    public void setSearchTablo(String searchTablo) {
        this.searchTablo = searchTablo;
    }

    public String getSearchDosyaIsmi() {
        return searchDosyaIsmi;
    }

    public void setSearchDosyaIsmi(String searchDosyaIsmi) {
        this.searchDosyaIsmi = searchDosyaIsmi;
    }

    public Date getFirstDate() {
        return firstDate;
    }

    public void setFirstDate(Date firstDate) {
        this.firstDate = firstDate;
    }

    public Date getLastDate() {
        return lastDate;
    }

    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }

}
