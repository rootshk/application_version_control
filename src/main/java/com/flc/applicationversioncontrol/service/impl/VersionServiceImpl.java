package com.flc.applicationversioncontrol.service.impl;

import com.flc.applicationversioncontrol.entity.Version;
import com.flc.applicationversioncontrol.repository.VersionRepository;
import com.flc.applicationversioncontrol.service.VersionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service(value="versionService")
public class VersionServiceImpl implements VersionService {

    @Resource
    private VersionRepository versionRepository;

    @Override
    public Page<Version> findPage(Integer pageNumber, Integer pageSize, String keyword, String searchKey) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.DESC, "createdDate");
        return versionRepository.findAll(pageable);
    }
}
