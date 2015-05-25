package savosh.soundcloudsaver;


import android.content.Context;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DiskLruCache {
    private static DiskLruCache diskLruCache;

    private Context context;
    private Map<String, File> map;
    private DiskLruCache(Context context){
        this.context = context;
    }

    public static void init(Context context){
        diskLruCache = new DiskLruCache(context);
        File cacheDir = diskLruCache.context.getCacheDir();
        File[] files = cacheDir.listFiles();
        diskLruCache.map = new HashMap<>();
        for(File file : files){
            if(file.canRead()) {
                diskLruCache.map.put(file.getName(), file);
            }
        }
    }

    public static Serializable get(String key){
        if(diskLruCache != null){
            if(diskLruCache.map.containsKey(key)){
                File file = diskLruCache.map.get(key);
                if(file.canRead()){

                    try {
                        Serializable serializable = (Serializable) new ObjectInputStream(new FileInputStream(file))
                                .readObject();
                        return serializable;
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
            return null;
        }
        throw new IllegalArgumentException("DiskLruCache is not init.");
    }

    public static void put(String key, Serializable value){

    }

    public static boolean contains(String key){
        if(diskLruCache != null){
            return diskLruCache.map.containsKey(key);
        }
        throw new IllegalArgumentException("DiskLruCache is not init.");
    }
}
