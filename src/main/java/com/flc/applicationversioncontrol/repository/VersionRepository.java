package com.flc.applicationversioncontrol.repository;

import com.flc.applicationversioncontrol.entity.Version;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository("versionRepository")
public interface VersionRepository extends JpaRepository<Version,Long>, JpaSpecificationExecutor<Version> {

    Page<Version> findAllByVersionLike(String version, Pageable pageable);

    Version findFirstByPlatformAndVersionChannelAndEnableOrderByCreatedDateDesc(Version.Platform platform, Version.VersionChannel versionChannel, Boolean enable);

}