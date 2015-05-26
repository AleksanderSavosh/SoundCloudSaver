package savosh.soundcloudsaver;


import android.content.Context;
import android.util.Log;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SimpleDiskCache {
    private static SimpleDiskCache simpleDiskCache;

    private Context context;
    private Map<String, File> map;
    private SimpleDiskCache(Context context){
        this.context = context;
    }

    public static void init(Context context){
        if(simpleDiskCache == null) {
            simpleDiskCache = new SimpleDiskCache(context);
            File cacheDir = simpleDiskCache.context.getCacheDir();
            File[] files = cacheDir.listFiles();
            simpleDiskCache.map = new HashMap<>();
            for (File file : files) {
                if (file.canRead()) {
                    simpleDiskCache.map.put(file.getName(), file);
                    Log.d(SimpleDiskCache.class.getName(), "Disk cache: Key: " + file.getName());
                }
            }
        }
    }

    public static Serializable get(String key){
        Log.d(SimpleDiskCache.class.getName(), "Get key: " + key);
        if(simpleDiskCache != null){
            if(simpleDiskCache.map.containsKey(key)){
                File file = simpleDiskCache.map.get(key);
                if(file.canRead()){
                    ObjectInputStream objectInputStream = null;
                    FileInputStream fileInputStream = null;
                    try {
                        fileInputStream = new FileInputStream(file);
                        objectInputStream = new ObjectInputStream(fileInputStream);
                        Serializable serializable = (Serializable) objectInputStream.readObject();
                        Log.d(SimpleDiskCache.class.getName(),"Successfully get cached key: " + key
                                + " Value: " + serializable.toString());
                        return serializable;
                    } catch (Exception e) {
                        Log.e(SimpleDiskCache.class.getName(),
                                "Exception in get cached object by key block of code: " + e.getMessage(), e);
                    } finally {
                        try {
                            if(objectInputStream != null) {
                                objectInputStream.close();
                            }
                        } catch (IOException e) {
                            Log.e(SimpleDiskCache.class.getName(),
                                    "Exception in get cached object by key block of code when close resource: "
                                            + e.getMessage(), e);                        }
                        try {
                            if(fileInputStream != null) {
                                fileInputStream.close();
                            }
                        } catch (IOException e) {
                            Log.e(SimpleDiskCache.class.getName(),
                                    "Exception in get cached object by key block of code when close resource: "
                                            + e.getMessage(), e);
                        }
                    }
                }
            }
            return null;
        }
        throw new IllegalArgumentException("SimpleDiskCache is not init.");
    }

    /**
     *
     * @param key
     * @param value must be Serializable
     */
    public static void put(String key, Object value){
        if(simpleDiskCache != null){
            if(simpleDiskCache.map.containsKey(key)){
                throw new UnsupportedOperationException(
                        "Don't need call the method put if the key is present in the cache");
            } else {
                File cacheDir = simpleDiskCache.context.getCacheDir();
                File file = new File(cacheDir.getAbsolutePath() + "/" + key);
                Log.d(SimpleDiskCache.class.getName(), "New cache file: " + file.getAbsolutePath());
                FileOutputStream fileOutputStream = null;
                ObjectOutputStream objectOutputStream = null;
                try {
                    if(file.createNewFile()){
                        fileOutputStream = new FileOutputStream(file);
                        objectOutputStream = new ObjectOutputStream(fileOutputStream);
                        objectOutputStream.writeObject(value);
                        simpleDiskCache.map.put(key, file);
                        Log.d(SimpleDiskCache.class.getName(), "Successfully cached object");
                    }
                } catch (IOException e) {
                    Log.e(SimpleDiskCache.class.getName(),
                            "Exception in put cached object block code: " + e.getMessage(), e);
                } finally {
                    try {
                        if(fileOutputStream != null) {
                            fileOutputStream.close();
                        }
                    } catch (IOException e) {
                        Log.e(SimpleDiskCache.class.getName(),
                                "Exception in put cached object block code when close resource: " + e.getMessage(), e);
                    }
                    try {
                        if(objectOutputStream != null) {
                            objectOutputStream.close();
                        }
                    } catch (IOException e) {
                        Log.e(SimpleDiskCache.class.getName(),
                                "Exception in put cached object block code when close resource: " + e.getMessage(), e);
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("SimpleDiskCache is not init.");
        }
    }

    public static boolean contains(String key){
        if(simpleDiskCache != null){
            return simpleDiskCache.map.containsKey(key);
        }
        throw new IllegalArgumentException("SimpleDiskCache is not init.");
    }
}
