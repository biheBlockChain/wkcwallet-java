package com.wrbug.wkcwallet.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

/**
 * JsonHelper
 *
 * @author wrbug
 * @since 2017/9/29
 */
public class JsonHelper {
    private static Gson sGson = new Gson();

    public static String toJson(Object o) {
        if (o == null) {
            return "";
        }
        return sGson.toJson(o);
    }


    public static JsonObject fromJson(Object json) {
        return fromJson(json, JsonObject.class);
    }

    public static <T> T fromJson(Object json, Class<T> tClass) {
        try {

            if (json == null) {
                return null;
            }
            if (json instanceof JsonElement) {
                return sGson.fromJson((JsonElement) json, tClass);
            }
            return sGson.fromJson(json.toString(), tClass);
        } catch (Throwable t) {

        }
        return null;
    }

    public static <T> T fromJson(Object json, Type tClass) {
        try {
            if (json == null) {
                return null;
            }
            if (json instanceof JsonElement) {
                return sGson.fromJson((JsonElement) json, tClass);
            }
            return sGson.fromJson(json.toString(), tClass);

        } catch (Throwable t) {

        }
        return null;
    }

    public static Gson getGson() {
        return sGson;
    }
}
