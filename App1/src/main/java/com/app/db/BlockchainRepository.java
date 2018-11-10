package com.app.db;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.app.dto.BlockchainDTO;

public interface BlockchainRepository extends MongoRepository<BlockchainDTO, String>{

}
