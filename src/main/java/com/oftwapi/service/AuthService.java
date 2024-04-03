package com.oftwapi.service;

import com.oftwapi.dto.request.auth.CheckCertificationRequestDto;
import com.oftwapi.dto.request.auth.EmailCertificationRequestDto;
import com.oftwapi.dto.request.auth.IdCheckRequestDto;
import com.oftwapi.dto.request.auth.SignUpRequestDto;
import com.oftwapi.dto.response.auth.CheckCertificationResponseDto;
import com.oftwapi.dto.response.auth.EmailCertificationResponseDto;
import com.oftwapi.dto.response.auth.IdCheckResponseDto;
import com.oftwapi.dto.response.auth.SignUpResponseDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<? super IdCheckResponseDto> idCheck(IdCheckRequestDto dto);
    ResponseEntity<? super EmailCertificationResponseDto> emailCertification(EmailCertificationRequestDto dto);
    ResponseEntity<? super CheckCertificationResponseDto> checkCertification(CheckCertificationRequestDto dto);
    ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto dto);
}
