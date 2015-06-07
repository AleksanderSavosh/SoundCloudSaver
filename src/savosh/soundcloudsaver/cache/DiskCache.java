package savosh.soundcloudsaver.cache;

import android.content.Context;
import android.util.Log;
import savosh.soundcloudsaver.util.FileUtil;

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
            return FileUtil.get(map.get(key));
        }
        return null;
    }

    public static void put(String key, Serializable value) {
        File file = FileUtil.put(cacheDirectory.getAbsolutePath() + "/" + key, value);
        if(file != null) {
            map.put(key, file);
        }
    }

    public static boolean contains(String key) {
        return map.containsKey(key);
    }

    private static void toLog(Exception e, String message) {
        Log.d(DiskCache.class.getName(), message, e);
        Log.e(DiskCache.class.getName(), message);
    }
}
