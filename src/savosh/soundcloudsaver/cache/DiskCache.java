package savosh.soundcloudsaver.cache;

import android.content.Context;
import android.util.Log;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DiskCache {

    private static File cacheDirectory;
    private static Map<String, File> map;

    public static void init(Context context) {
        cacheDirectory = context.getCacheDir();
        File[] files = cacheDirectory.listFiles();
        map = new HashMap<>();
        for (File file : files) {
            if (file.canRead()) {
                map.put(file.getName(), file);
                Log.d(DiskCache.class.getName(), "Disk cache: Key: " + file.getName());
            }
        }
    }

    public static Serializable get(String key) {
        Log.d(DiskCache.class.getName(), "Get key: " + key);
        if (map.containsKey(key)) {
            File file = map.get(key);
            if (file.canRead()) {
                ObjectInputStream objectInputStream = null;
                FileInputStream fileInputStream = null;
                try {
                    fileInputStream = new FileInputStream(file);
                    objectInputStream = new ObjectInputStream(fileInputStream);
                    Serializable serializable = (Serializable) objectInputStream.readObject();
                    Log.d(DiskCache.class.getName(),
                            "Successfully get cached key: " + key +
                                    " Value: " + serializable.toString());
                    return serializable;
                } catch (Exception e) {
                    toLog(e, "Exception in get cached object by key block of code: " + e.getMessage());
                } finally {
                    closeAll(objectInputStream, fileInputStream);
                }
            }
        }
        return null;
    }

    public static void put(String key, Serializable value) {
        File file = new File(cacheDirectory.getAbsolutePath() + "/" + key);
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            if (file.exists()) {
                Log.d(DiskCache.class.getName(), "Delete cache file: " + file.getAbsolutePath());
                file.delete();
            }
            if (file.createNewFile()) {
                Log.d(DiskCache.class.getName(), "New cache file: " + file.getAbsolutePath());
                fileOutputStream = new FileOutputStream(file);
                objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(value);
                map.put(key, file);
                Log.d(DiskCache.class.getName(), "Successfully cached object");
            }
        } catch (IOException e) {
            toLog(e, "Exception in put cached object block code: " + e.getMessage());
        } finally {
            closeAll(fileOutputStream, objectOutputStream);
        }
    }

    public static boolean contains(String key) {
        return map.containsKey(key);
    }

    private static void closeAll(Closeable... resources) {
        for (Closeable closeable : resources) {
            try {
                if (closeable != null) {
                    closeable.close();
                }
            } catch (IOException e) {
                Log.e(DiskCache.class.getName(), "Exception when close resource: " + e.getMessage(), e);
            }
        }
    }

    private static void toLog(Exception e, String message) {
        Log.d(DiskCache.class.getName(), message, e);
        Log.e(DiskCache.class.getName(), message);
    }
}
