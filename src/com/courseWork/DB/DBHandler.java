package com.courseWork.DB;

import java.sql.*;

public class DBHandler {


    private static DBHandler instance = null;

    public static synchronized DBHandler getInstance() throws SQLException {
        if (instance == null)
            instance = new DBHandler();
        return instance;
    }
}
