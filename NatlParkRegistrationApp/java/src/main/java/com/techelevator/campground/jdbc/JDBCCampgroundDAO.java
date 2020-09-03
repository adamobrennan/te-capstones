package com.techelevator.campground.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.CampgroundDAO;

public class JDBCCampgroundDAO implements CampgroundDAO {

	
	private JdbcTemplate jdbcTemplate;
	
	public JDBCCampgroundDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	
	}
	
	@Override
	public List<Campground> searchCampgroundByPark(int parkId) {
		List<Campground> campgrounds = new ArrayList<>();
		String sqlSearchCampgroundByPark = "SELECT c.* FROM campground c"
											+ " INNER JOIN park p "
											+ " ON c.park_id = p.park_id"
											+ " WHERE p.park_id = ?"
											+ " ORDER BY p.name ASC,"
											+ " c.name ASC";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSearchCampgroundByPark, parkId);
		while (results.next()) {
			Campground theCampground = mapRowToCampground(results);
			campgrounds.add(theCampground);
		}
		return campgrounds;
	}
	
	
	private Campground mapRowToCampground(SqlRowSet results) {
		Campground theCampground = new Campground();
		theCampground.setParkId(results.getInt("park_id"));
		theCampground.setCampgroundId(results.getInt("campground_id"));
		theCampground.setName(results.getString("name"));
		theCampground.setOpenFrom(results.getInt("open_from_mm"));
		theCampground.setOpenTo(results.getInt("open_to_mm"));
		theCampground.setDailyFee(results.getBigDecimal("daily_fee"));

		return theCampground;
	}
}
