/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gebze.endulus;

import com.mongodb.BasicDBObject;
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
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.MediaType;
import org.bson.types.ObjectId;
import org.codehaus.jackson.map.ObjectMapper;

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

    public List<FileModel> getAllFiles(String collectionName) {
        String str = null;
        FileModel newFile;
        boolean sonuncuMu;
        boolean flag = false;
        List<FileModel> list = new ArrayList<>();
        DBCollection coll = db.getCollection(collectionName);
        
        DBCursor  cursor = coll.find();
        cursor= cursor.limit(cursorLimit);
        
        while (cursor.hasNext()) {
            BasicDBObject result = (BasicDBObject) cursor.next();
            newFile = new FileModel();
            flag = false;
            if (result.getString("isim", null) != null) {
                str = result.getString("isim");
                newFile.setFileName(str);
                try {
                    newFile.setDosyaTuru(displayContentType(str));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                flag = true;
            }
            if (result.getString("fileId", null) != null) {
                str = result.getString("fileId");
                newFile.setId(str);
            } else {
                continue;
            }
            if (result.getString("klasorPath", null) != null) {
                str = result.getString("klasorPath");
                newFile.setKlasorPath(str);
            } else {
                continue;
            }
            if (result.getString("kaydedenUser_username", null) != null) {
                str = result.getString("kaydedenUser_username");
                newFile.setKaydedenKullaniciAdi(str);
            } else {
                continue;
            }
            if (result.getBoolean("sonuncu", false) != false) {
                sonuncuMu = result.getBoolean("sonuncu", false);
                newFile.setSonuncuMu(sonuncuMu);
            } else {
                continue;
            }

            if (result.getString("table", null) != null) {
                str = result.getString("table");
                newFile.setTable(str);
            } else {
                continue;
            }
            if (result.getInt("boyut", -1) != -1) {
                newFile.setBoyut(result.getInt("boyut", -1));
            } else {
                continue;
            }
            if (result.getString("fileId", null) != null) {
                str = result.getString("fileId");
                newFile.setFileId(str);
            } else {
                continue;
            }

            if (flag) {
                list.add(newFile);
                flag = false;
            }

        }
        flag = true;
        return list;
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

}
