package com.pickle.pickle_BE.service;

import com.pickle.pickle_BE.dto.request.RegisterUserRequest;
import com.pickle.pickle_BE.dto.response.RegisterUserResponse;
import com.pickle.pickle_BE.entity.Preference;
import com.pickle.pickle_BE.entity.Review;
import com.pickle.pickle_BE.entity.User;
import com.pickle.pickle_BE.repository.PreferenceRepository;
import com.pickle.pickle_BE.repository.RefreshTokenRepository;
import com.pickle.pickle_BE.repository.ReviewRepository;
import com.pickle.pickle_BE.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PreferenceRepository preferenceRepository;
    private final ReviewRepository reviewRepository;


    //회원가입
    public RegisterUserResponse registerUser(RegisterUserRequest request) {
        User user = new User(
                request.getName(),
                request.getEmail(),
                bCryptPasswordEncoder.encode(request.getPassword()),
                request.getPhoneNumber()
        );
        userRepository.save(user);
        return new RegisterUserResponse(user.getUserId(), user.getEmail(), user.getName());
    }

    public User findById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }


    //이메일중복 확인
    public Optional<String> getEmailByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(User::getEmail);
    }

    //이메일찾기
    public Optional<String> getEmailByPhoneNumber(String phoneNumber){
        Optional<User> user = userRepository.findEmailByPhoneNumber(phoneNumber);
        return user.map(User::getEmail);
    }

    //유저 조회
    public Optional<User> getUserByUserId(String userId){
        //return userRepository.findUserByUserId(userId);
        return userRepository.findById(userId);
    }


    //유저 수정
    public User updateUserById(String Id, User userDetails){
        User user = userRepository.findById(Id)
                .orElseThrow(() -> new RuntimeException("User not found with id"+Id));
        user.setEmail(userDetails.getEmail());
        user.setPassword(userDetails.getPassword());
        user.setPhoneNumber(userDetails.getPhoneNumber());
        return userRepository.save(user);
    }

    public String getPhoneNumberByUserId(String userId) {
        Optional<User> user = userRepository.findPhoneNumberByUserId(userId);
        return user.map(User::getPhoneNumber).toString().substring(9,20);
    }

    public User getUserByPhoneNumberAndEmail(String phoneNumber, String email) {
        return userRepository.findByPhoneNumberAndEmail(phoneNumber,email);
    }

    public Optional<String> getPasswordByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(User::getPassword);
    }

    @Transactional
    public void deleteUser(String userId) {
        // 유저의 리프레시 토큰 삭제
        refreshTokenRepository.deleteByUserId(userId);
        // 유저 삭제
        userRepository.deleteById(userId);
    }

    // 비밀번호 해시를 업데이트하는 메서드
    @Transactional
    public void updatePassword(String email, String newPasswordHash) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setPassword(newPasswordHash);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
    // 임시 비밀번호를 검증하는 메서드
    public boolean validateTemporaryPassword(String email, String tempPassword) {
        Optional<String> existingPasswordHash = getPasswordByEmail(email);
        // 여기서 bcrypt 해시와 직접 비교하지 않고 문자열로 비교합니다.
        return existingPasswordHash.isPresent() && tempPassword.equals(existingPasswordHash.get());
    }

    public List<Preference> getPreferencesByUserId(String userId) {
        return preferenceRepository.findListByUserId(userId);
    }

    public List<Review> getReviewsByUserId(String userId) {
        return reviewRepository.findByUserId(userId);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}