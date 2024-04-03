package com.oftwapi.service.implement;

import com.oftwapi.common.CertificationNumber;
import com.oftwapi.dto.request.auth.CheckCertificationRequestDto;
import com.oftwapi.dto.request.auth.EmailCertificationRequestDto;
import com.oftwapi.dto.request.auth.IdCheckRequestDto;
import com.oftwapi.dto.request.auth.SignUpRequestDto;
import com.oftwapi.dto.response.ResponseDto;
import com.oftwapi.dto.response.auth.CheckCertificationResponseDto;
import com.oftwapi.dto.response.auth.EmailCertificationResponseDto;
import com.oftwapi.dto.response.auth.IdCheckResponseDto;
import com.oftwapi.dto.response.auth.SignUpResponseDto;
import com.oftwapi.entity.CertificationEntity;
import com.oftwapi.entity.UserEntity;
import com.oftwapi.provider.EmailProvider;
import com.oftwapi.repository.CertificationRepository;
import com.oftwapi.repository.UserRepository;
import com.oftwapi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImplement implements AuthService {
    private final UserRepository userRepository;
    private final CertificationRepository certificationRepository;
    private final PasswordEncoder passwordEncoder;

    private final EmailProvider emailProvider;

    @Override
    public ResponseEntity<? super IdCheckResponseDto> idCheck(IdCheckRequestDto dto) {
        try {
            String username = dto.getUsername();
            boolean isExist = userRepository.existsByUsername(username);
            if (isExist) return IdCheckResponseDto.duplicatedId();
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return IdCheckResponseDto.success();
    }

    @Override
    public ResponseEntity<? super EmailCertificationResponseDto> emailCertification(EmailCertificationRequestDto dto) {
        try {
            String username = dto.getUsername();
            String email = dto.getEmail();

            boolean isExistUsername = userRepository.existsByUsername(username);
            if (isExistUsername) return EmailCertificationResponseDto.duplicateId();

            String certificationNumber = CertificationNumber.getCertificationNumber();

            boolean isSucceeded = emailProvider.sendCertificationMail(email, certificationNumber);
            if (!isSucceeded) return EmailCertificationResponseDto.mailSendFail();

            CertificationEntity certificationEntity = new CertificationEntity(username, email, certificationNumber);
            certificationRepository.save(certificationEntity);
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return EmailCertificationResponseDto.success();
    }

    @Override
    public ResponseEntity<? super CheckCertificationResponseDto> checkCertification(CheckCertificationRequestDto dto) {
        try {
            String username = dto.getUsername();
            String email = dto.getEmail();
            String certificationNumber = dto.getCertificationNumber();

            CertificationEntity certificationEntity = certificationRepository.findByUsername(username);

            boolean isMatched = certificationEntity.getEmail().equals(email) && certificationEntity.getCertificationNumber().equals(certificationNumber);
            System.out.println("isMatched = " + isMatched);
            if (!isMatched) return CheckCertificationResponseDto.certificationFail();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return CheckCertificationResponseDto.success();
    }

    @Override
    public ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto dto) {
        try {
            String username = dto.getUsername();
            String email = dto.getEmail();

            boolean isExistUsername = userRepository.existsByUsername(username);
            if (isExistUsername) return SignUpResponseDto.duplicateId();

            boolean isExistEmail = userRepository.existsByEmail(email);
            if (isExistEmail) return SignUpResponseDto.duplicateId();

            String certificationNumber = dto.getCertificationNumber();
            CertificationEntity certificationEntity = certificationRepository.findByUsername(username);
            boolean isMatched = certificationEntity.getEmail().equals(email) && certificationEntity.getCertificationNumber().equals(certificationNumber);
            if (!isMatched) return SignUpResponseDto.certificationFail();

            String password = dto.getPassword();
            String password2 = dto.getPassword2();
            boolean isMatchedPasswords = password.equals(password2);
            if (!isMatchedPasswords) return ResponseDto.validationFail();

            dto.setPassword(passwordEncoder.encode(password));

            UserEntity userEntity = new UserEntity(dto);
            userRepository.save(userEntity);

            // certificationRepository.delete(certificationEntity);
            certificationRepository.deleteByUsername(username);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return SignUpResponseDto.success();
    }
}
