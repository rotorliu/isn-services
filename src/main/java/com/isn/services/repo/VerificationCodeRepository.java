package com.isn.services.repo;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.isn.services.po.VerificationCode;

public interface VerificationCodeRepository extends PagingAndSortingRepository<VerificationCode, Long> {
	VerificationCode findByMobile(@Param("mobile") String mobile);
	void deleteByMobile(@Param("mobile") String mobile);
}
