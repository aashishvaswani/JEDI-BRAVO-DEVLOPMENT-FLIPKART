package com.flipfit.business.impl;

import com.flipfit.business.BookingService;
import com.flipfit.bean.Booking;
import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymSlot;
import com.flipfit.bean.GymUser;
import com.flipfit.enums.BookingStatus;
import com.flipfit.helper.DataStore;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.Optional;

public class BookingServiceImpl implements BookingService {

	@Override
	public String createBooking(String userId, String centreId, LocalTime startTime, LocalTime endTime, Date date) {
		GymUser user = DataStore.getUser(userId);
		if (user == null) {
			System.out.println("User not found.");
			return null;
		}

		GymCenter center = DataStore.getCenter(centreId);
		if (center == null) {
			System.out.println("Center not found.");
			return null;
		}

		GymSlot slot = findSlot(center, startTime, endTime);
		if (slot == null) {
			System.out.println("No matching slot found.");
			return null;
		}

		if (!slot.isAvailable()) {
			System.out.println("Slot is fully booked.");
			return null;
		}

		// Remove older booking if user already has one for the same start time on same date
		LocalDate localDate = new java.sql.Date(date.getTime()).toLocalDate();
		checkOverlapAndRemove(userId, localDate, startTime);

		String bookingId = DataStore.nextBookingId();
		Booking booking = new Booking();
		booking.setBookingId(bookingId);
		booking.setGymUser(user);
		booking.setGymSlot(slot);
		booking.setDateAndTime(localDate.toString() + " " + startTime.toString());
		booking.setBookingStatus(BookingStatus.CONFIRMED);

		slot.decreaseAvailability();
		DataStore.saveBooking(booking);

		return bookingId;
	}

	@Override
	public boolean cancelBooking(String bookingId) {
		Booking booking = DataStore.removeBooking(bookingId);
		if (booking == null) {
			System.out.println("Booking not found.");
			return false;
		}
		if (booking.getGymSlot() != null) {
			booking.getGymSlot().increaseAvailability();
		}
		return true;
	}

	@Override
	public boolean modifyBooking(String userId, String oldBookingId, LocalTime startTime, LocalTime endTime, Date date) {
		String centreId = null;
		if (oldBookingId != null) {
			Booking existing = DataStore.getBooking(oldBookingId);
			if (existing != null && existing.getGymSlot() != null) {
				centreId = resolveCenterId(existing.getGymSlot());
			}
			cancelBooking(oldBookingId);
		}
		if (centreId == null) {
			centreId = "C1";
		}
		String newId = createBooking(userId, centreId, startTime, endTime, date);
		return newId != null;
	}

	private GymSlot findSlot(GymCenter center, LocalTime startTime, LocalTime endTime) {
		return center.getCenterSlot().stream()
				.filter(slot -> slot.getStartTime().equals(startTime) && slot.getEndTime().equals(endTime)).findFirst()
				.orElse(null);
	}

	private void checkOverlapAndRemove(String userId, LocalDate date, LocalTime time) {
		String key = DataStore.buildDateKey(date, time);
		Optional<Booking> existing = DataStore.getAllBookings().stream()
				.filter(b -> b.getGymUser() != null && userId.equals(b.getGymUser().getUserId()))
				.filter(b -> b.getDateAndTime() != null && b.getDateAndTime().startsWith(key)).findFirst();
		existing.ifPresent(b -> cancelBooking(b.getBookingId()));
	}

	private String resolveCenterId(GymSlot slot) {
		for (GymCenter center : DataStore.getAllCenters()) {
			if (center.getCenterSlot().contains(slot)) {
				return center.getCenterId();
			}
		}
		return null;
	}
}
