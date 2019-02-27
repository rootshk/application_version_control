package com.flc.applicationversioncontrol.service;

import com.flc.applicationversioncontrol.entity.Version;
import org.springframework.data.domain.Page;

public interface VersionService {

    /**
     * 分页查询
     * @param pageNumber    页码
     * @param pageSize      页数
     * @param keyword       搜索值
     * @param searchKey     搜索key
     * @return
     */
    Page<Version> findPage(Integer pageNumber, Integer pageSize, String keyword, String searchKey);

}
