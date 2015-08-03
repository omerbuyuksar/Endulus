/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gebze.endulus;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.sun.jersey.api.client.ClientResponse;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Pattern;
import org.bson.types.ObjectId;
import sun.nio.cs.ISO_8859_9;

/**
 *
 * @author pc
 */
public class MongoDbService {

    private DB db;
    private boolean connected = false;
    private int cursorLimit;
   

    public MongoDbService() {
        cursorLimit = 0;
    }

    /**
     *
     * @param dbName
     * @param port
     * @param ipAddress
     * @param userName
     * @param password
     * @return
     * @throws java.net.UnknownHostException
     */
    public DB connectToMongoDB(String dbName, int port, String ipAddress,
            String userName, String password) throws UnknownHostException {
        MongoClient client;
        DB database;

        client = new MongoClient(ipAddress, port);
        database = client.getDB(dbName);
        database.authenticate(userName, password.toCharArray());

        db = database;
        connected = true;
        return database;
    }
    
    public List<FileModel> getFilesAdvanced(String collectionName,BasicDBObject obj){
        FileModel model;
        List<FileModel> list = new ArrayList<>();
        DBCollection coll = db.getCollection(collectionName);
        DBCursor cursor = coll.find(obj);
        cursor = cursor.limit(cursorLimit);
        while (cursor.hasNext()) {
            BasicDBObject result = (BasicDBObject) cursor.next();
            model = BasicDBObjectToFileModel(result);
            if (model != null) {
                list.add(model);
            }
        }
        return list;
    }
    public List<FileModel> getFilesAdvanced(String collectionName,BasicDBObject obj,Date firsDate,Date secondDate){
        FileModel model;
        List<FileModel> list = new ArrayList<>();
        List<FileModel> finalList = new ArrayList<>();
        DBCollection coll = db.getCollection(collectionName);
        DBCursor cursor = coll.find(obj);
        cursor = cursor.limit(cursorLimit);
        while (cursor.hasNext()) {
            BasicDBObject result = (BasicDBObject) cursor.next();
            model = BasicDBObjectToFileModel(result);
            if (model != null) {
                list.add(model);
            }
        }
        if(firsDate==null&&secondDate==null)
            return list;
        obj = new BasicDBObject();
        BasicDBObjectBuilder obj1 = BasicDBObjectBuilder.start();
        if (firsDate != null) {
            obj1.add("$gte", firsDate);
        }
        if (secondDate != null) {
            obj1.add("$lte", secondDate);
        }
        obj.append("uploadDate", obj1.get());
       
        
        /*Lis<User> users = mongoOps.find(query(where("isActive").is(true).and("CreatedDate").lte(new java.util.Date())), User.class);*/
        boolean flag=false;
        
        for (FileModel listItem : list) {
            coll=db.getCollection(listItem.getTable()+".files");
            cursor = coll.find(obj);
            if(cursor.hasNext()){
                finalList.add(listItem);
            }
        }
        return finalList;

    }

    public List<FileModel> getAllFiles(String collectionName) {
        FileModel model;
        List<FileModel> list = new ArrayList<>();
        DBCollection coll = db.getCollection(collectionName);
        
        DBCursor  cursor = coll.find();
        cursor= cursor.limit(cursorLimit);
        while (cursor.hasNext()) {
            BasicDBObject result = (BasicDBObject) cursor.next();
            model=BasicDBObjectToFileModel(result);
            if (model!=null) {
                list.add(model);
            }
        }
        return list;
    }
    public List<FileModel> getFilesByName(String collectionName,String fileName){
        
        BasicDBObject obj = new BasicDBObject();
        if(fileName.length() > 0)
            obj.append("isim", Pattern.compile(fileName, Pattern.CASE_INSENSITIVE));
        return getFilesAdvanced(collectionName,obj);
    }
    public List<FileModel> getFilesByDizinId(String collectionName,int id){
        
        BasicDBObject obj = new BasicDBObject().append("dizin_id", id);
        obj.append("sonuncu", true);
        return getFilesAdvanced(collectionName,obj);
    }

    private FileModel BasicDBObjectToFileModel(BasicDBObject result) {
        FileModel newFile = new FileModel();
        boolean sonuncuMu;
        String str;
        if (result.getString("isim", null) != null) {
            str = result.getString("isim");
            newFile.setFileName(str);
        }
        else
            return null;
        if (result.getString("dosyaTuru", null) != null) {
            str = result.getString("dosyaTuru");
            newFile.setDosyaTuru(str);
        }
        else
            return null;     
        if (result.getString("fileId", null) != null) {
            str = result.getString("fileId");
            newFile.setId(str);
        } else {
            return null;
        }
        if (result.getString("klasorPath", null) != null) {
            str = result.getString("klasorPath");
            newFile.setKlasorPath(str);
        } else {
            return null;
        }
        if (result.getString("kaydedenUser_username", null) != null) {
            str = result.getString("kaydedenUser_username");
            newFile.setKaydedenKullaniciAdi(str);
        } else {
            return null;
        }
        if (result.getBoolean("sonuncu", false) != false) {
            sonuncuMu = result.getBoolean("sonuncu", false);
            newFile.setSonuncuMu(sonuncuMu);
        } else {
            return null;
        }

        if (result.getString("table", null) != null) {
            str = result.getString("table");
            newFile.setTable(str);
        } else {
            return null;
        }
        if (result.getInt("boyut", -1) != -1) {
            newFile.setBoyut(result.getInt("boyut", -1));
        } else {
            return null;
        }
        if (result.getString("fileId", null) != null) {
            str = result.getString("fileId");
            newFile.setFileId(str);
        } else {
            return null;
        }
        return newFile;
    }

    public InputStream getFileInputStream(String fileId, String table) {

        InputStream input = null;

        BasicDBObject query = new BasicDBObject().append("_id", new ObjectId(fileId));
        GridFS gfs = new GridFS(db, table);
        if (gfs != null) {
            GridFSDBFile gFile = gfs.findOne(query);
            input = gFile.getInputStream();
        }
        return input;
    }

    public static String displayContentType(String pathText) throws Exception {
        Path path = Paths.get(pathText);
        String type = Files.probeContentType(path);
        return type;
    }


    public DB getDb() {
        return db;
    }

    public int getCursorLimit() {
        return cursorLimit;
    }

    public void setCursorLimit(int cursorLimit) {
        this.cursorLimit = cursorLimit;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
    
}
