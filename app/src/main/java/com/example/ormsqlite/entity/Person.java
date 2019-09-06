package com.example.ormsqlite.entity;

import com.llj.ormsqlite.annotation.DbFiled;
import com.llj.ormsqlite.annotation.DbTable;

/**
 * @author: lilinjie
 * @date: 2019-09-06 09:35
 * @description:
 */
@DbTable("tb_person")
public class Person {
    public long id;
    @DbFiled("tb_name")
    public String name;
    @DbFiled("tb_age")
    public int age;
}
