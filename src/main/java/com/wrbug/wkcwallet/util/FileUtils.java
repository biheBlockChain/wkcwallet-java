package com.wrbug.wkcwallet.util;

import java.io.*;

/**
 * Created by wrbug on 2017/8/23.
 */
public class FileUtils {
    public static void inputstreamtofile(InputStream ins, File file) throws IOException {
        if (file.exists()) {
            file.createNewFile();
        }
        OutputStream os = new FileOutputStream(file);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        ins.close();
    }

    public static String readFile(File file) {
        StringBuilder builder = new StringBuilder();
        try {
            FileReader fr = new FileReader(file);
            int ch = 0;
            while ((ch = fr.read()) != -1) {
                builder.append((char) ch);
            }
        } catch (IOException e) {

        }
        return builder.toString();
    }

    public static String readFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            return readFile(file);
        }
        return "";
    }

    public static void whiteFile(File file, String data) {
        try {
            FileWriter fw = new FileWriter(file);
            fw.write(data);
            fw.flush();
        } catch (IOException e) {
        }
    }
}
