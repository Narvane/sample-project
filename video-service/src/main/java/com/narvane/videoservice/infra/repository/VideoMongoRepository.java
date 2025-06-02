package com.narvane.videoservice.infra.repository;

import com.narvane.videoservice.infra.entity.VideoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface VideoMongoRepository extends MongoRepository<VideoEntity, UUID> {}
