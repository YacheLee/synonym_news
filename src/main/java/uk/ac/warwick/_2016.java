package uk.ac.warwick;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

import static uk.ac.warwick.DBUtils.getDataSource;

public class _2016 {
    public static void main(String[] args) {
        DataSource dataSource = getDataSource();
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        int year = 2016;
        UpdateNewsHtml updateNewsHtml = new UpdateNewsHtml(namedParameterJdbcTemplate, year);
        updateNewsHtml.updateDatabaseHtml();
    }
}