package cn.newcapec.tools.utils.json;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/11 0011.
 */
public class JsonParse {
    static JsonParse jsonParse = new JsonParse();

    private JsonParse() {
    }

    public static JsonParse getJsonParse() {
        return jsonParse;
    }

    public ArrayList<Object> parseWebDataToList(String strArray, Class<?> cla) {
        ArrayList alObjects = null;

        try {
            if(strArray != null && !"".equals(strArray)) {
                alObjects = new ArrayList();
                JSONArray e = JSONArray.parseArray(strArray);
                if(e.size() <= 0) {
                    return alObjects;
                }

                for(int i = 0; i < e.size(); ++i) {
                    JSONObject jsonObject = e.getJSONObject(i);
                    String parstValue = jsonObject.toString();
                    if(parstValue!=null) {
                        Object object = this.parseWebDataToObject(parstValue, cla);
                        alObjects.add(object);
                    }
                }
            }
        } catch (Exception var9) {
            var9.printStackTrace();
        }

        return alObjects;
    }

    public Object parseWebDataToObject(String strArray, Class<?> cla) {
        Object object = null;

        try {
            Field[] e = cla.getDeclaredFields();
            JSONObject jsonObject = JSONObject.parseObject(strArray);
            object = cla.newInstance();

            for(int j = 0; j < e.length; ++j) {
                Field field = e[j];
                String fieldName = field.getName();
                String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Method method = cla.getMethod(methodName, new Class[]{field.getType()});
                Object filedValue = null;

                try {
                    filedValue = jsonObject.get(fieldName);
                    if(filedValue != null && !"".equals(filedValue)) {
                        method.invoke(object, new Object[]{filedValue});
                    }
                } catch (Exception var13) {
                    ;
                }
            }
        } catch (Exception var14) {
            System.out.println(var14.getMessage());
        }

        return object;
    }

    public Object parseWebDataToObjectInCludeList(String strArray, Class<?> cla, Class<?> includeClass, String listKey) {
        try {
            Field[] e = cla.getDeclaredFields();
            JSONObject jsonObject = JSONObject.parseObject(strArray);
            Object object = cla.newInstance();

            for(int j = 0; j < e.length; ++j) {
                Field field = e[j];
                String fieldName = field.getName();
                String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Method method = cla.getMethod(methodName, new Class[]{field.getType()});
                Object filedValue = null;

                try {
                    if(!listKey.equals(fieldName)) {
                        filedValue = jsonObject.get(fieldName);
                        if(filedValue != null && !"".equals(filedValue)) {
                            method.invoke(object, new Object[]{filedValue});
                        }
                    } else {
                        ArrayList objectlist = this.parseWebDataToList(jsonObject.get(listKey).toString(), includeClass);
                        if(objectlist != null && objectlist.size() > 0) {
                            method.invoke(object, new Object[]{objectlist});
                        }
                    }
                } catch (Exception var16) {
                    ;
                }
            }

            return object;
        } catch (Exception var17) {
            var17.printStackTrace();
            return null;
        }
    }

    public ArrayList<Object> parseWebDataToList(String parentKey, String strArray, Class<?> parentcla, Class<?> childcla, String childKey) {
        ArrayList alObjects = new ArrayList();

        try {
            if(parentKey != null && !"".equals(parentKey)) {
                JSONObject e = JSONObject.parseObject(strArray);
                strArray = e.get(parentKey).toString().toString();
                strArray = strArray.trim().replace(" ", "").toString();
            }

            if(strArray != null && !"".equals(strArray)) {
                Field[] var22 = parentcla.getDeclaredFields();
                JSONArray array = JSONArray.parseArray(strArray);
                if(array.size() <= 0) {
                    return alObjects;
                }

                for(int i = 0; i < array.size(); ++i) {
                    Object object = parentcla.newInstance();
                    JSONObject jsonObject = array.getJSONObject(i);

                    for(int j = 0; j < var22.length; ++j) {
                        Field field = var22[j];
                        String fieldName = field.getName();
                        String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                        Method method = parentcla.getMethod(methodName, new Class[]{field.getType()});
                        Object filedValue = null;

                        try {
                            if(!childKey.equals(fieldName)) {
                                filedValue = jsonObject.get(fieldName);
                                if(filedValue != null && !"".equals(filedValue)) {
                                    method.invoke(object, new Object[]{filedValue});
                                }
                            } else {
                                ArrayList objectlist = this.parseWebDataToList(jsonObject.get(childKey).toString(), childcla);
                                if(objectlist != null && objectlist.size() > 0) {
                                    method.invoke(object, new Object[]{objectlist});
                                }
                            }
                        } catch (Exception var20) {
                            ;
                        }
                    }

                    alObjects.add(object);
                }
            }
        } catch (Exception var21) {
            System.out.println(var21.getMessage());
        }

        return alObjects;
    }

    public boolean successFilter(String strArray) {
        if(strArray!=null) {
            JSONObject jsonObject = JSONObject.parseObject(strArray);
            String state = jsonObject.get("status").toString();
            return state!=null && "success".equalsIgnoreCase(state.trim());
        }
        return false;
    }
}
