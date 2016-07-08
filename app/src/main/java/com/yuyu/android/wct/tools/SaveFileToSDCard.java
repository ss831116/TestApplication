package com.yuyu.android.wct.tools;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by bernie.shi on 2016/3/15.
 */
public class SaveFileToSDCard {
    public static void saveToSDCard(String path, String filename, byte[] responseBody) throws Exception {
        File file = new File(filename);
        String fileName = file.getName();
        File sdFile = new File(path + fileName);
        if (!sdFile.exists()) {
            try {
                sdFile.createNewFile();
            } catch (Exception e) {
            }
        }
        FileOutputStream outStream = new FileOutputStream(sdFile);
        outStream.write(responseBody);
        outStream.close();
    }
}
