package com.isn.services.repo;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.isn.services.po.MessageBox;

public interface MessageBoxRepository extends PagingAndSortingRepository<MessageBox, Long> {

}
