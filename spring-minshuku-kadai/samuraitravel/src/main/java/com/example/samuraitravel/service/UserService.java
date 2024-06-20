package com.example.samuraitravel.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.samuraitravel.entity.Role;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.SignupForm;
import com.example.samuraitravel.form.UserEditForm;
import com.example.samuraitravel.repository.RoleRepository;
import com.example.samuraitravel.repository.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	public User create(SignupForm signupForm) {
		User u = new User();
		Role r = roleRepository.findByName("ROLE_GENERAL");

		u.setName(signupForm.getName());
		u.setFurigana(signupForm.getFurigana());
		u.setPostalCode(signupForm.getPostalCode());
		u.setAddress(signupForm.getAddress());
		u.setPhoneNumber(signupForm.getPhoneNumber());
		u.setEmail(signupForm.getEmail());
		u.setPassword(passwordEncoder.encode(signupForm.getPassword()));
		u.setRole(r);
		u.setEnabled(true);

		return userRepository.save(u);
	}

	@Transactional
	public void update(UserEditForm userEditForm) {
		User u = userRepository.getReferenceById(userEditForm.getId());
		u.setName(userEditForm.getName());
		u.setFurigana(userEditForm.getFurigana());
		u.setPostalCode(userEditForm.getPostalCode());
		u.setAddress(userEditForm.getAddress());
		u.setPhoneNumber(userEditForm.getPhoneNumber());
		u.setEmail(userEditForm.getEmail());
		userRepository.save(u);
	}

	// メールアドレスが登録済みかどうかをチェックする
	public boolean isEmailRegistered(String email) {
		User user = userRepository.findByEmail(email);
		return user != null;
	}

	// パスワードとパスワード（確認用）の入力値が一致するかどうかをチェックする
	public boolean isSamePassword(String password, String passwordConfirmation) {
		return password.equals(passwordConfirmation);
	}

	// メールアドレスが変更されたかどうかをチェックする
	public boolean isEmailChanged(UserEditForm userEditForm) {
		User currentUser = userRepository.getReferenceById(userEditForm.getId());
		return !userEditForm.getEmail().equals(currentUser.getEmail());
	}
}