package com.llj.ormsqlite;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

/**
 * @author: lilinjie
 * @date: 2019-09-06 13:18
 * @description:
 */
public class BaseDaoFactory {
    private SQLiteDatabase database;
    private static final BaseDaoFactory sInstance = new BaseDaoFactory();

    public static BaseDaoFactory getInstance() {
        return sInstance;
    }

    private BaseDaoFactory() {
        String databasePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.db";
        database = SQLiteDatabase.openOrCreateDatabase(databasePath, null);
    }

    public <T> IBaseDao<T> getBaseDao(Class<T> entityClass) {
        BaseDao<T> baseDao = new BaseDao<>();
        baseDao.init(entityClass, database);
        return baseDao;
    }
}
