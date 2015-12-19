package com.isn.services.repo;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import com.isn.services.po.User;

//@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

	User findByMobile(@Param("mobile") String mobile);
	
	User findByEmail(@Param("email") String email);
	
}