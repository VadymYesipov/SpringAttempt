package com.aimprosoft.yesipov.db;

import java.sql.ResultSet;

public interface EntityMapper<T> {

    T mapRow(ResultSet resultSet);
}
