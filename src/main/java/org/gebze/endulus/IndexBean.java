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
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
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
    
    private SdtpService sdtpService;
    private String searchText;
    private String searchDosyaIsmi;
    private String searchKonusu;
    private String searchKodu;
    private boolean searchSonmuTik;
    private String searchDosyaturu;
    private String searchKaydedenKullanici;
    private String searchTablo;


    public int buttonConnectDb() {
        try {
            mydb.connectToMongoDB("mydb", 27017, "192.168.77.25", "nico", "nico");
            files = mydb.getAllFiles("veriler");
            addMessage("Connected");
            connected = true;
        } catch (Exception e) {
            addMessage("Connection Failed");
        }

        return 0;
    }

    public int buttonLogout() {
        try {

            addMessage("Log out");
        } catch (Exception e) {
            addMessage("Log out Failed");
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
        mydb.setCursorLimit(1500);
        files = new ArrayList<>();
        sdtpService = new SdtpService();
        root = new DefaultTreeNode("Root", null);
        addSdtpNodes();
    }
    
    public void searchByName(){
        if(!connected){
            addMessage("Connect DB First");
            return;
        }
        files = mydb.getFilesByName("veriler", getSearchText());
        addMessage(""+files.size()+" Files Found");
    }
    public void searchAdvanced(){
        if(!connected){
            addMessage("Connect DB First");
            return;
        }
        files = mydb.getFilesAdvanced("veriler", getAdvancedSearchValues());
        addMessage(""+files.size()+" Files Found");
    }
    public BasicDBObject getAdvancedSearchValues(){
         BasicDBObject obj = new BasicDBObject();

        obj.append("isim", Pattern.compile(getSearchDosyaIsmi(), Pattern.CASE_INSENSITIVE));
        obj.append("kaydedenUser_username", Pattern.compile(getSearchKaydedenKullanici(), Pattern.CASE_INSENSITIVE));
        if (!getSearchDosyaturu().contentEquals("ALL")) {
            obj.append("dosyaTuru", Pattern.compile(getSearchDosyaturu(), Pattern.CASE_INSENSITIVE));
        }
        if (isSearchSonmuTik()) {
            obj.append("sonuncu", isSearchSonmuTik());
        }
        obj.append("table", Pattern.compile(getSearchTablo(), Pattern.CASE_INSENSITIVE));
        obj.append("klasorPath", Pattern.compile(getSearchKodu(), Pattern.CASE_INSENSITIVE));
        List<JSdtpModel> list = sdtpService.findWithKonu(getSearchKonusu());
        for (JSdtpModel list1 : list) {
            obj.append("klasorPath", Pattern.compile("" + list1.getStdpKodu(), Pattern.CASE_INSENSITIVE));
        }

        return obj;
    }

    private void addSdtpNodes() {

        List<JSdtpModel> list = sdtpService.getSdtp();

        if (list == null) {
            addMessage("Cannot Get SDTP Files");
            
            return;
        }

        for (JSdtpModel list1 : list) {
            root.getChildren().add(new SdtpTreeNode(list1, list1.getKonu()));
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
            addMessage("File cannot Download");
            return null;
        }
        return new DefaultStreamedContent(input, str, file.getFileName());
    }


    public void onNodeSelect(NodeSelectEvent event) {

        if(selectedNode.isOpened())
            return;
        List<JSdtpModel> list;
        List<Long> llist;
        //sdtp turu bakılcak
        if(selectedNode.getSdtpmodel().getSdtpTuru() != 2){
            System.out.println(""+selectedNode.getSdtpmodel().getSdtpTuru()+"  "+selectedNode.getSdtpmodel().getStdpKodu());
            list = sdtpService.findSdtpWithId("" + selectedNode.getSdtpmodel().getId());

            if (list == null) {
                addMessage("Cannot Get SDTP Files");
                return;
            }
            for (JSdtpModel list1 : list) {
                selectedNode.getChildren().add(new SdtpTreeNode(list1, list1.getKonu()));
            }
            selectedNode.setOpened(true);
        } else {
            System.out.println(""+selectedNode.getSdtpmodel().getSdtpTuru()+"  "+selectedNode.getSdtpmodel().getStdpKodu());
            llist = sdtpService.getEbysDizin("" + selectedNode.getSdtpmodel().getId());
            if(llist==null){
                addMessage("Cannot Get SDTP Files");
                return;
            }
            List<FileModel> modelList;
            files.clear();
            if (!mydb.isConnected()) {
                try {
                    mydb.connectToMongoDB("mydb", 27017, "192.168.77.25", "nico", "nico");
                    connected = true;
                } catch (UnknownHostException ex) {
                    Logger.getLogger(IndexBean.class.getName()).log(Level.SEVERE, null, ex);
                    addMessage("Cannot Connect to DB");
                }
            }
            for (int i = 0; i < llist.size(); ++i) {

                modelList = mydb.getFilesByDizinId("veriler", llist.get(i).intValue());
                for (int j = 0; j < modelList.size(); j++) {
                    files.add(modelList.get(i));
                }
            }
            for (FileModel file : files) {
                String str;
                str = file.getKlasorPath();
                String kodu=selectedNode.getSdtpmodel().getStdpKodu().replace('.', '/');      
                str=str.replaceFirst(kodu, "");
                selectedNode.getChildren().add(new DefaultTreeNode(str));
                
            }
           //selectedNode.setOpened(true);
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

    

}
