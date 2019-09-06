package com.llj.ormsqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.llj.ormsqlite.annotation.DbFiled;
import com.llj.ormsqlite.annotation.DbTable;
import com.llj.ormsqlite.log.OrmSqliteLog;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: lilinjie
 * @date: 2019-09-06 09:25
 * @description:
 */
public class BaseDao<T> implements IBaseDao<T> {

    private SQLiteDatabase database;
    private Class<T> entityClass;
    private String tableName;
    private boolean isInit = false;
    private HashMap<String, Field> cacheMap;

    protected boolean init(@NonNull Class<T> entityClass, @NonNull SQLiteDatabase database) {
        if (!isInit) {
            this.database = database;
            this.entityClass = entityClass;
            if (entityClass.getAnnotation(DbTable.class) != null) {
                tableName = entityClass.getAnnotation(DbTable.class).value();
            } else {
                tableName = entityClass.getSimpleName();
            }
            if (!database.isOpen()) {
                return false;
            }
            initCacheMap();
            if (!autoCreateTable()) {
                return false;
            }
            isInit = true;
        }
        return isInit;
    }

    private boolean autoCreateTable() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ");
        sb.append(tableName);
        sb.append(" (id INTEGER PRIMARY KEY AUTOINCREMENT,");
        for (Map.Entry<String, Field> entry : cacheMap.entrySet()) {
            sb.append(entry.getKey());
            Class<?> type = entry.getValue().getType();
            if (type == String.class) {
                sb.append(" TEXT,");
            } else if (type == int.class) {
                sb.append(" INTEGER,");
            } else if (type == long.class) {
                sb.append(" BIGINT,");
            } else if (type == byte[].class) {
                sb.append(" BLOB,");
            }
        }
        if (sb.charAt(sb.length() - 1) == ',') {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append(")");
        String sql = sb.toString();
        OrmSqliteLog.info("创建表语句:" + sql);
        try {
            database.execSQL(sql);
        } catch (Exception e) {
            OrmSqliteLog.error("execSQL异常:" + e.getMessage());
            return false;
        }
        return true;
    }

    private void initCacheMap() {
        cacheMap = new HashMap<>();
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getAnnotation(DbFiled.class) != null) {
                String key = field.getAnnotation(DbFiled.class).value();
                cacheMap.put(key, field);
            }
        }
    }

    @Override
    public long insert(T entity) {
        ContentValues contentValues = getContentValues(entity);
        return database.insert(tableName, null, contentValues);
    }

    private ContentValues getContentValues(T entity) {
        ContentValues contentValues = new ContentValues();
        String sql = "select * from " + tableName + " limit 1,0";
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor == null) {
            return contentValues;
        }
        String[] columnNames = cursor.getColumnNames();
        for (String columnName : columnNames) {
            Field columnField = null;
            for (Map.Entry<String, Field> entry : cacheMap.entrySet()) {
                if (columnName.equals(entry.getKey())) {
                    columnField = entry.getValue();
                    break;
                }
            }
            if (columnField != null) {
                try {
                    Class<?> type = columnField.getType();
                    Object obj = columnField.get(entity);
                    if (type == String.class) {
                        contentValues.put(columnName, (String) obj);
                    } else if (type == int.class) {
                        contentValues.put(columnName, (int) obj);
                    } else if (type == long.class) {
                        contentValues.put(columnName, (long) obj);
                    } else if (type == byte[].class) {
                        contentValues.put(columnName, (byte[]) obj);
                    }
                } catch (IllegalAccessException e) {
                    OrmSqliteLog.error("反射获取字段值异常:" + e.getMessage());
                }
            }

        }
        cursor.close();
        return contentValues;
    }
}
