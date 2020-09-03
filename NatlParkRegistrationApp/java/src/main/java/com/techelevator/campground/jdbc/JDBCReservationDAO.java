package com.techelevator.campground.jdbc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Reservation;
import com.techelevator.campground.model.ReservationDAO;
import com.techelevator.campground.model.Site;

public class JDBCReservationDAO implements ReservationDAO {
	
	private JdbcTemplate jdbcTemplate;
	
	public JDBCReservationDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public boolean isReserved(Site site, LocalDate fromDate, LocalDate toDate) {
		String sqlFindReservationById = "SELECT * FROM reservation WHERE site_id = ? AND from_date BETWEEN ? AND ? "
										+ "OR site_id = ? AND to_date BETWEEN ? AND ? "
										+ "OR site_id = ? AND ? BETWEEN from_date AND to_date "
										+ "OR site_id = ? AND ? BETWEEN from_date AND to_date";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindReservationById, site.getSiteId(), fromDate, toDate,
																				site.getSiteId(), fromDate, toDate,
																				site.getSiteId(), fromDate,
																				site.getSiteId(), toDate);
		List<Reservation> reservationList = new ArrayList<>();
		while (results.next()) {
			Reservation theReservation = mapRowToReservation(results);
			reservationList.add(theReservation);
		}
		if (reservationList.size() > 0) {
			return true;
		} 
			return false;
		
	}
	
	@Override
	public int createReservation(int siteId, String name, LocalDate fromDate, LocalDate toDate) {
		
		String sqlCreateReservation = "INSERT INTO reservation (site_id, name, from_date, to_date, create_date) "
				+ "VALUES (?,?,?,?,?) "
				+ "RETURNING reservation_id";	
		
		int reservationId = jdbcTemplate.queryForObject(sqlCreateReservation, 
				Integer.class,  
				siteId, name, fromDate, toDate, LocalDate.now());

		return reservationId;
	}
	
	private Reservation mapRowToReservation(SqlRowSet results) {
		Reservation theReservation = new Reservation();
		theReservation.setReservationId(results.getInt("reservation_id"));
		theReservation.setSiteId(results.getInt("site_id"));
		theReservation.setName(results.getString("name"));
		theReservation.setFromDate(results.getDate("from_date").toLocalDate());
		theReservation.setToDate(results.getDate("to_date").toLocalDate());
		theReservation.setCreateDate(results.getDate("create_date").toLocalDate());
		
		
		return theReservation;
	}

	

}
