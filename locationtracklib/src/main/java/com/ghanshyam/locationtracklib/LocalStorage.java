package com.ghanshyam.locationtracklib;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ghanshyam.
 */
public class LocalStorage {
    private static LocalStorage instance;
    private SharedPreferences sharedPreferences;
    Context ctx;

    public static synchronized LocalStorage getInstance() {
        if (instance == null) {
            instance = new LocalStorage();
        }
        return instance;
    }

    public void createPreference(Context ctx, String preferenceName) {
        this.ctx = ctx;
        sharedPreferences = ctx.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
    }

    public void saveData(Data data) {
       save("channelId", data.getChannelId());
       save("channelName", data.getChannelName());
       save("notificationId",data.getNotificationId());
       save("smallIcon", data.getSmallIcon());
       save("isAlertOnce", data.isAlertOnce());
       save("isOnGoing", data.isOnGoing());            ;
       save("priority", data.getPriority());
       save("category", data.getCategory());
       save("visibility", data.getVisibility());
       save("number", data.getNumber());
       save("UPDATE_INTERVAL_IN_MILLISECONDS", data.getUPDATE_INTERVAL_IN_MILLISECONDS());
       save("FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS", data.getFASTEST_UPDATE_INTERVAL_IN_MILLISECONDS());
       save("DISPLACEMENT", data.getDISPLACEMENT());
    }

    public Data getData() {
        Data data = new Data();
        try {
            String channelId = get("channelId", "");
            String channelName = get("channelName", "");
            int notificationId = get("notificationId", 0);
            int smallIcon = get("smallIcon", 0);
            boolean isAlertOnce = get("isAlertOnce", true);
            boolean isOnGoing = get("isOnGoing", true);            ;
            int priority = get("priority", 0);
            String category = get("category", "");
            int visibility = get("visibility", 0);
            int number = get("number", 0);
            long UPDATE_INTERVAL_IN_MILLISECONDS = get("UPDATE_INTERVAL_IN_MILLISECONDS", 0l);
            long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = get("FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS", 0l);
            int DISPLACEMENT = get("DISPLACEMENT", 0);
            data.setAlertOnce(isAlertOnce);
            data.setCategory(category);
            data.setChannelId(channelId);
            data.setChannelName(channelName);
            data.setDISPLACEMENT(DISPLACEMENT);
            data.setFASTEST_UPDATE_INTERVAL_IN_MILLISECONDS(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
            data.setUPDATE_INTERVAL_IN_MILLISECONDS(UPDATE_INTERVAL_IN_MILLISECONDS);
            data.setNotificationId(notificationId);
            data.setNumber(number);
            data.setOnGoing(isOnGoing);
            data.setSmallIcon(smallIcon);
            data.setVisibility(visibility);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public void save(String key, Object value) {
        try {
            SharedPreferences.Editor editor = getEditor();
            if (value instanceof Boolean) {
                editor.putBoolean(key, (Boolean) value);
            } else if (value instanceof Integer) {
                editor.putInt(key, (Integer) value);
            } else if (value instanceof Float) {
                editor.putFloat(key, (Float) value);
            } else if (value instanceof Long) {
                editor.putLong(key, (Long) value);
            } else if (value instanceof String) {
                editor.putString(key, (String) value);
            } else if (value instanceof Enum) {
                editor.putString(key, value.toString());
            } else if (value != null) {
                throw new RuntimeException("Attempting to save non-supported preference");
            }

            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, T defValue) {
        T returnValue = (T) sharedPreferences.getAll().get(key);
        return returnValue == null ? defValue : returnValue;
    }

    public boolean has(String key) {
        return sharedPreferences.contains(key);
    }

    private SharedPreferences.Editor getEditor() {
        return sharedPreferences.edit();
    }

    public void clearData() {
        try {
            SharedPreferences.Editor editor = getEditor();
            editor.clear();
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
