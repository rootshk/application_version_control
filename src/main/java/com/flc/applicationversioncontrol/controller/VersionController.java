package com.flc.applicationversioncontrol.controller;

import com.alibaba.fastjson.JSONObject;
import com.flc.applicationversioncontrol.entity.Version;
import com.flc.applicationversioncontrol.repository.VersionRepository;
import com.flc.applicationversioncontrol.utils.JsonUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController("versionController")
@RequestMapping("/version")
public class VersionController {

    @Resource
    private VersionRepository versionRepository;

    /**
     * 获取最新的版本号
     *
     * @param platform
     * @param versionChannel
     * @return
     */
    @GetMapping("/latest")
    @CrossOrigin(methods = { RequestMethod.GET }, origins = "*")
    public JSONObject get(Version.Platform platform, Version.VersionChannel versionChannel) {
        if (null == platform || null == versionChannel) return JsonUtils.getRoot(400, "error", "参数 platform或versionChannel 为空");
        return JsonUtils.getRoot(10000, "success",
                versionRepository.findFirstByPlatformAndVersionChannelAndEnableOrderByCreatedDateDesc(platform, versionChannel, true));
    }

}
