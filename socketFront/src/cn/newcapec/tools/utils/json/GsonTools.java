package cn.newcapec.tools.utils.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/10 0010.
 */
public class GsonTools {
    public GsonTools() {
    }

    public static <T> T getObject(String jsonString, Class<T> cls) {
        T t = null;
        try {
            Gson gson = new Gson();
            t = gson.fromJson(jsonString, cls);
        } catch (Exception var4) {

        }

        return t;
    }

    public static <T> List<T> getList(String jsonString, Class<T> cls) {
        Object list = new ArrayList();

        try {
            Gson e = new Gson();
            list = (List)e.fromJson(jsonString, (new TypeToken() {
            }).getType());
        } catch (Exception var5) {
            String str = var5.toString();
            System.out.println(str);
        }

        return (List)list;
    }

    public static List<String> getList(String jsonString) {
        Object list = new ArrayList();

        try {
            Gson gson = new Gson();
            list = (List)gson.fromJson(jsonString, (new TypeToken() {
            }).getType());
        } catch (Exception var3) {
            ;
        }

        return (List)list;
    }

    public static List<Map<String, Object>> getlistMap(String jsonString) {
        Object listmap = new ArrayList();

        try {
            Gson gson = new Gson();
            listmap = (List)gson.fromJson(jsonString, (new TypeToken() {
            }).getType());
        } catch (Exception var3) {
            ;
        }

        return (List)listmap;
    }
}
