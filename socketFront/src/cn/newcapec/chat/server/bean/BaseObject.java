package cn.newcapec.chat.server.bean;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BaseObject implements Serializable {

    private static final long serialVersionUID = 4278222239697366603L;

    public BaseObject() {
    }

    public List<? extends BaseObject> fromJsonArray(String jsons) {
        List<BaseObject> es = new ArrayList<BaseObject>();
        jsons = jsons.trim();
        if (jsons.startsWith("{")) {
            jsons = "[" + jsons + "]";
        }
        JSONArray jsonArray = JSONArray.parseArray(jsons);
        for (int i = 0; i < jsonArray.size(); i++) {
            BaseObject e = BaseObject.formJson(jsonArray.get(i).toString(), this.getClass());
            es.add(e);
        }
        return es;
    }

    public JSONObject toJsonObject() {
        return JSONObject.parseObject(this.toString());
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    public static <T> T formJson(String json, Class<T> clazz) {
        return JSONObject.parseObject(json, clazz);
    }

}
