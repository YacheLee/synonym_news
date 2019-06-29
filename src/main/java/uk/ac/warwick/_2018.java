package uk.ac.warwick;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

import static uk.ac.warwick.DBUtils.getDataSource;

public class _2018 {
    public static void main(String[] args) {
        DataSource dataSource = getDataSource();
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        int year = 2018;
        int limit = 100;
        int n = UpdateNewsText.count(namedParameterJdbcTemplate, year);

        for (int offset = 0; offset < n; offset+=limit) {
            System.out.println("【"+offset+"/"+n+"】");
            UpdateNewsText updateNewsText = new UpdateNewsText(namedParameterJdbcTemplate, year, limit, offset);
            updateNewsText.updateDatabaseText();
        }
    }
}
