package com.app.db;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.app.dto.UserDTO;

public interface UserRepository extends CrudRepository<UserDTO, Long>{
	List<UserDTO> findByUsername(String username);
}
