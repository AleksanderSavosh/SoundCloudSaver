package savosh.soundcloudsaver.util;

import android.os.Environment;
import android.util.Log;

import java.io.*;
import java.util.*;

public class FileUtil {

    public static Serializable get(String path){
        return get(new File(path));
    }

    public static Serializable get(File file){
        if (file.canRead()) {
            ObjectInputStream objectInputStream = null;
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(file);
                objectInputStream = new ObjectInputStream(fileInputStream);
                Serializable serializable = (Serializable) objectInputStream.readObject();
                Log.d(FileUtil.class.getName(),
                        "Successfully get serializable from file: " + file.getAbsolutePath()
                                + " Value: " + serializable.toString());
                return serializable;
            } catch (Exception e) {
                toLog(e, "Exception in get serializable from file block of code: " + e.getMessage());
            } finally {
                closeAll(objectInputStream, fileInputStream);
            }
        }
        return null;
    }

    public static File put(String path, Serializable value){
        return put(new File(path), value);
    }

    public static File put(File file, Serializable value){
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            if (file.exists()) {
                Log.d(FileUtil.class.getName(), "Delete file: " + file.getAbsolutePath());
                file.delete();
            }
            if (file.createNewFile()) {
                Log.d(FileUtil.class.getName(), "New file: " + file.getAbsolutePath());
                fileOutputStream = new FileOutputStream(file);
                objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(value);
                Log.d(FileUtil.class.getName(), "Successfully wrote object");
                return file;
            }
        } catch (IOException e) {
            toLog(e, "Exception in put object to file block code: " + e.getMessage());
        } finally {
            closeAll(fileOutputStream, objectOutputStream);
        }
        return null;
    }

    public static List<File> getDownloadMp3Files(){
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File[] files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().contains(".mp3");
            }
        });
        if(files != null){
            return Arrays.asList(files);
        }
        return null;
    }

    public static File getDownloadMp3File(final String name){
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File[] files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().contains(name + ".mp3");
            }
        });
        if(files != null && files.length > 0){
            return files[0];
        }
        return null;
    }

    private static void closeAll(Closeable... resources) {
        for (Closeable closeable : resources) {
            try {
                if (closeable != null) {
                    closeable.close();
                }
            } catch (IOException e) {
                Log.e(FileUtil.class.getName(), "Exception when close resource: " + e.getMessage(), e);
            }
        }
    }

    private static void toLog(Exception e, String message) {
        Log.d(FileUtil.class.getName(), message, e);
        Log.e(FileUtil.class.getName(), message);
    }
}
