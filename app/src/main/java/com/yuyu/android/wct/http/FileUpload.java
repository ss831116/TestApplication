package com.yuyu.android.wct.http;

import com.yuyu.android.wct.DreamAchieveApplication;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by jackie.sun on 2016/3/25.
 */
public class FileUpload {

    public static String post(File file) throws IOException {
        int maxBufferSize = 1 * 1024 * 1024;
        byte[] buffer;
        int bytesRead, bytesAvailable, bufferSize;
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost postRequest = new HttpPost(HttpSite.httpSite+"video/uploadFile.do"
                + DreamAchieveApplication.sharedPreferences.getString("cookies", ""));
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        reqEntity.addPart("fileName", new StringBody(file.getName()));
        reqEntity.addPart("content", new StringBody("tag1,tag2"));
        reqEntity.addPart("title", new StringBody("Rollit"));
        try {
            InputStream is = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bytesAvailable = is.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            bytesRead = is.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                bos.write(buffer, 0, bufferSize);
                bytesAvailable = is.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = is.read(buffer, 0, bufferSize);
            }
            ByteArrayBody bab = new ByteArrayBody(bos.toByteArray(), file.getName());
            reqEntity.addPart("file", bab);
        } catch (Exception e) {
            reqEntity.addPart("file", new StringBody("file error"));
        }
        postRequest.setEntity(reqEntity);
        HttpResponse response = httpClient.execute(postRequest);
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        String sResponse;
        StringBuilder s = new StringBuilder();
        while ((sResponse = reader.readLine()) != null) {
            s = s.append(sResponse);
        }
        return s.toString();
    }

}
