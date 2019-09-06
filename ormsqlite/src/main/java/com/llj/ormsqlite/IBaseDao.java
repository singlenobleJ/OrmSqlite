package com.llj.ormsqlite;

/**
 * @author: lilinjie
 * @date: 2019-09-06 09:21
 * @description:
 */
public interface IBaseDao<T> {

    long insert(T entity);

}
