package com.github.tokscull.shopbackend.repository;

import com.github.tokscull.shopbackend.model.TvChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TvChannelRepository extends JpaRepository<TvChannel, Long> {

    Optional<TvChannel> findById(Long id);

    Boolean existsByName(String username);
}
