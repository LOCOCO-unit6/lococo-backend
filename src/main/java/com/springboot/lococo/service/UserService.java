package com.springboot.lococo.service;

import com.springboot.lococo.dto.LoginRequestDto;
import com.springboot.lococo.dto.RegisterRequestDto;
import com.springboot.lococo.model.Role;
import com.springboot.lococo.model.User;
import com.springboot.lococo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {


    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    //id 중복체크
    public boolean checkLoginIdDuplicate(String identification) {
        return userRepository.existsByIdentification(identification);
    }

    //회원가입
    public void join(RegisterRequestDto registerRequestDto) {
        userRepository.save(registerRequestDto.toEntity(bCryptPasswordEncoder.encode(registerRequestDto.getPassword())));
    }

    //로그인
    public User userLogin(LoginRequestDto req) {
        Optional<User> optionalUser = userRepository.findByIdentification(req.getIdentification());

        // loginId와 일치하는 User가 없으면 null return
        if(optionalUser.isEmpty()) {
            return null;
        }

        User user = optionalUser.get();

        // 찾아온 User의 password와 입력된 password가 다르면 null return
        if (!bCryptPasswordEncoder.matches(req.getPassword(), user.getPassword())) {
            return null;
        }

        return user;
    }

    //주최자 로그인
    public User adminLogin(LoginRequestDto req) {
        Optional<User> optionalUser = userRepository.findByIdentification(req.getIdentification());

        // loginId와 일치하는 User가 없으면 null return
        if(optionalUser.isEmpty()) {
            return null;
        }

        User user = optionalUser.get();

        // 찾아온 User의 password와 입력된 password가 다르면 null return
        if (!bCryptPasswordEncoder.matches(req.getPassword(), user.getPassword())) {
            return null;
        }

        if (user.getRole() != Role.ADMIN) {
            return null;
        }

        return user;
    }

    //userId(Long)를 입력받아 User을 return 해주는 기능
    public User getLoginUserById(Long userId) {
        if(userId == null) return null;

        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()) return null;

        return optionalUser.get();
    }

    //identification(String)을 입력받아 User을 return 해주는 기능
    public User getLoginUserByLoginId(String identification) {
        if(identification == null) return null;

        Optional<User> optionalUser = userRepository.findByIdentification(identification);
        if(optionalUser.isEmpty()) return null;

        return optionalUser.get();
    }

    //
    /*
    public User register(UserRequestDto dto) {
        User user = new User();
        user.setIdentification(dto.getIdentification());
        user.setPassword(dto.getPassword()); // 실제에선 암호화해야 함
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAffiliation(dto.getAffiliation());
        user.setRole(Role.valueOf(dto.getRole()));
        return userRepository.save(user);
    }

    public User login(LoginRequestDto dto) {
        User user = userRepository.findByIdentification(dto.getIdentification())
                .orElseThrow(() -> new RuntimeException("유저 없음"));
        if (!user.getPassword().equals(dto.getPassword())) {
            throw new RuntimeException("비밀번호 틀림");
        }
        return user;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
    */
}