package com.devabit.takestock.data.source.local.dao;

/**
 * Created by Victor Artemyev on 11/07/2016.
 */
abstract class AbstractDao {
    /* Clearing right realm with right DAO is important,
     * test this method to avoid any misplaced clear */
    abstract public void clearDatabase();
}
