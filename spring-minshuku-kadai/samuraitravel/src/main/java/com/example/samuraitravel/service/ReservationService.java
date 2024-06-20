package com.example.samuraitravel.service;

// import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Reservation;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReservationRepository;
import com.example.samuraitravel.repository.UserRepository;

@Service public class ReservationService {
	private final ReservationRepository	reservationRepository;
	private final HouseRepository				houseRepository;
	private final UserRepository				userRepository;


	public ReservationService(ReservationRepository reservationRepository, HouseRepository houseRepository,
			UserRepository userRepository) {
		this.reservationRepository = reservationRepository;
		this.houseRepository = houseRepository;
		this.userRepository = userRepository;
	}

	@Transactional
	public void create(Map<String, String> paymentIntentObject) {
		// reserve
		Reservation reservation = new Reservation();
		// id
		Integer houseId = Integer.valueOf(paymentIntentObject.get("houseId"));
		Integer userId = Integer.valueOf(paymentIntentObject.get("userId"));
		// data
		House h = houseRepository.getReferenceById(houseId);
		User u = userRepository.getReferenceById(userId);
		LocalDate cid = LocalDate.parse(paymentIntentObject.get("checkinDate"));
		LocalDate cod = LocalDate.parse(paymentIntentObject.get("checkoutDate"));
		Integer numberOfPeople = Integer.valueOf(paymentIntentObject.get("numberOfPeople"));
		Integer amount = Integer.valueOf(paymentIntentObject.get("amount"));
		// set
		reservation.setHouse(h);
		reservation.setUser(u);
		reservation.setCheckinDate(cid);
		reservation.setCheckoutDate(cod);
		reservation.setNumberOfPeople(numberOfPeople);
		reservation.setAmount(amount);
		// save
		reservationRepository.save(reservation);
	}

	// 宿泊人数が定員以下かどうかをチェックする
	public boolean isWithinCapacity(Integer numberOfPeople, Integer capacity) {
		return numberOfPeople<=capacity;
	}

	// 宿泊料金を計算する
	public Integer calculateAmount(LocalDate checkinDate, LocalDate checkoutDate, Integer price) {
		long numberOfNights = ChronoUnit.DAYS.between(checkinDate, checkoutDate);
		int amount = price *(int)numberOfNights;
		return amount;
	}
}
