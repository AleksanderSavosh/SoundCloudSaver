package savosh.soundcloudsaver.cache;

import android.content.Context;
import android.util.Log;
import com.google.common.cache.CacheBuilder;

import java.io.Serializable;

public class Cache {

    public static void init(Context context) {
        Log.d(Cache.class.getName(), "Init!");
        DiskCache.init(context);
    }

    public static Serializable get(String key) {
        Log.d(Cache.class.getName(), "Get key: " + key);
        Serializable serializable = MemCache.get(key);
        if(serializable == null){
            serializable = DiskCache.get(key);
        }
        Log.d(Cache.class.getName(), "Result: " + serializable);
        return serializable;
    }

    public static void put(String key, Serializable value) {
        Log.d(Cache.class.getName(), "Put key: " + key + " value: " + value);
        MemCache.put(key, value);
        DiskCache.put(key, value);
    }

    public static boolean contains(String key) {
        Log.d(Cache.class.getName(), "Is contains key: " + key);
        boolean result = MemCache.contains(key);
        if(!result){
            result = DiskCache.contains(key);
        }
        Log.d(Cache.class.getName(), "Result: " + result);
        return result;
    }

}
