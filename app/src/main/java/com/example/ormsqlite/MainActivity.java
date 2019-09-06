package com.example.ormsqlite;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.ormsqlite.entity.Person;
import com.llj.ormsqlite.BaseDaoFactory;
import com.llj.ormsqlite.IBaseDao;
import com.llj.ormsqlite.log.OrmSqliteLog;

public class MainActivity extends AppCompatActivity {

    private IBaseDao<Person> baseDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OrmSqliteLog.enableLog();
        baseDao = BaseDaoFactory.getInstance().getBaseDao(Person.class);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Person person = new Person();
                person.name = "张三";
                person.age = 26;
                long id = baseDao.insert(person);
                Log.i("id", id + "");
            }
        });
    }
}
