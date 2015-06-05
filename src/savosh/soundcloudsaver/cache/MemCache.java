package savosh.soundcloudsaver.cache;

import android.util.Log;
import com.google.common.cache.*;

import java.io.*;

public class MemCache {
    public static com.google.common.cache.Cache<String, Serializable> memCache =
            CacheBuilder.newBuilder().maximumSize(100).build();

    public static Serializable get(String key) {
        Log.d(MemCache.class.getName(), "Get key: " + key);
        Serializable result = memCache.getIfPresent(key);
        Log.d(MemCache.class.getName(), "Result: " + result);
        return result;
    }

    public static void put(String key, Serializable value) {
        Log.d(MemCache.class.getName(), "Put key: " + key + " value: " + value);
        memCache.put(key, value);
    }

    public static boolean contains(String key) {
        Log.d(MemCache.class.getName(), "Is contains key: " + key);
        boolean result = memCache.getIfPresent(key) != null;
        Log.d(MemCache.class.getName(), "Result: " + result);
        return result;
    }
}
