package com.isn.services.repo;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import com.isn.services.po.User;

//@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

	List<User> findByMobile(@Param("mobile") String mobile);
	
	List<User> findByEmail(@Param("email") String email);
	
}