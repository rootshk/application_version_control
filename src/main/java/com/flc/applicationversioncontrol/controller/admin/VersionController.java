package com.flc.applicationversioncontrol.controller.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.flc.applicationversioncontrol.entity.Version;
import com.flc.applicationversioncontrol.repository.VersionRepository;
import com.flc.applicationversioncontrol.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@RestController("adminVersionController")
@RequestMapping("/admin/version")
public class VersionController {

    @Resource
    private VersionRepository versionRepository;

    /**
     * 分页列表查询
     * @param pageNumber        分页页数
     * @param pageSize          查询数量
     * @param sort              排序方式
     * @param sortKey           排序字段
     * @param name              查询名称
     * @param version           查询版本
     * @param enable            是否开启
     * @param forcedUpdate      是否强制更新
     * @param platform          平台
     * @param versionChannel    版本渠道
     * @return
     */
    @GetMapping
    @CrossOrigin(methods = { RequestMethod.GET }, origins = "*")
    public JSONObject getPage(@RequestParam(defaultValue = "0", required = false) Integer pageNumber,
                              @RequestParam(defaultValue = "10", required = false) Integer pageSize,
                              @RequestParam(defaultValue = "DESC", required = false) Sort.Direction sort,
                              @RequestParam(defaultValue = "createdDate", required = false) String sortKey,
                              String name, String version, Boolean enable, Boolean forcedUpdate,
                              Version.Platform platform, Version.VersionChannel versionChannel) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort, sortKey);

        if (StringUtils.isEmpty(name) && StringUtils.isEmpty(version) && null == enable && null == forcedUpdate && null == platform && null == versionChannel) {
            return JsonUtils.getRoot(10000, "success", versionRepository.findAll(pageable));
        } else {
            Page<Version> page = versionRepository.findAll((Specification<Version>) (root, query, cb) -> {
                List<Predicate> list = new ArrayList<Predicate>();

                if (StringUtils.isNoneBlank(name)) {
                    list.add(cb.like(root.get("name").as(
                            String.class), "%" + name + "%"));
                }
                if (StringUtils.isNoneBlank(version)) {
                    list.add(cb.like(root.get("version").as(String.class), "%" + version + "%"));
                }

                if (platform != null) {
                    list.add(cb.equal(root.get("platform").as(Version.Platform.class), platform));
                }
                if (versionChannel != null) {
                    list.add(cb.equal(root.get("versionChannel").as(Version.VersionChannel.class), versionChannel));
                }
                if (enable != null) {
                    list.add(cb.equal(root.get("enable").as(Boolean.class), enable));
                }
                if (forcedUpdate != null) {
                    list.add(cb.equal(root.get("forcedUpdate").as(Boolean.class), forcedUpdate));
                }

                Predicate[] p = new Predicate[list.size()];
                return cb.and(list.toArray(p));
            }, pageable);

//            return JsonUtils.getRoot(10000, "success", versionRepository.findAllByVersionLike("%" + versionValue + "%", pageable));
            return JsonUtils.getRoot(10000, "success", page);
        }
    }

    /**
     * 查看详情
     * @param id
     * @return
     */
    @GetMapping("/id")
    @CrossOrigin(methods = { RequestMethod.GET }, origins = "*")
    public JSONObject get(@PathVariable Long id) {
        Optional<Version> versionOptional = versionRepository.findById(id);

        if (versionOptional.isPresent())
            return JsonUtils.getRoot(10000, "success", versionOptional.get());
        else
            return JsonUtils.getRoot(404, "error", "未查询到该版本");
    }

    /**
     * 保存版本
     * @param rec
     * @return
     */
    @PostMapping
    @CrossOrigin(methods = { RequestMethod.POST }, origins = "*")
    public JSONObject save(@RequestBody JSONObject rec) {
        if (!rec.containsKey("name")) return JsonUtils.getRoot(404, "error", "name为空");
        if (!rec.containsKey("enable")) return JsonUtils.getRoot(404, "error", "enable为空");
        if (!rec.containsKey("version")) return JsonUtils.getRoot(404, "error", "version为空");
        if (!rec.containsKey("platform")) return JsonUtils.getRoot(404, "error", "platform为空");
        if (!rec.containsKey("downloadLink")) return JsonUtils.getRoot(404, "error", "downloadLink为空");
        if (!rec.containsKey("forcedUpdate")) return JsonUtils.getRoot(404, "error", "forcedUpdate为空");
        if (!rec.containsKey("versionChannel")) return JsonUtils.getRoot(404, "error", "versionChannel为空");

        Version.VersionChannel versionChannel;
        Version.Platform platform;

        try {
            versionChannel = Version.VersionChannel.valueOf(rec.getString("versionChannel"));
            platform = Version.Platform.valueOf(rec.getString("platform"));
        } catch (Exception e) {
            return JsonUtils.getRoot(404, "error", "versionChannel或platform转换错误");
        }

        Version version = new Version();
        version.setCreatedDate(new Date());
        version.setLastModifiedDate(new Date());
        version.setName(rec.getString("name"));
        version.setEnable(rec.getBoolean("enable"));
        version.setVersion(rec.getString("version"));
        version.setPlatform(platform);
        version.setDownloadLink(rec.getString("downloadLink"));
        version.setForcedUpdate(rec.getBoolean("forcedUpdate"));
        version.setVersionChannel(versionChannel);

        if (rec.containsKey("image"))
            version.setImage(rec.getString("image"));
        if (rec.containsKey("introduction"))
            version.setIntroduction(rec.getString("introduction"));

        versionRepository.save(version);

        return JsonUtils.getRoot(10000, "success", null);
    }

    /**
     * 更新版本
     * @param rec
     * @return
     */
    @PutMapping
    @CrossOrigin(methods = { RequestMethod.PUT }, origins = "*")
    public JSONObject update(@RequestBody JSONObject rec) {
        if (!rec.containsKey("id")) return JsonUtils.getRoot(404, "error", "id为空");
        if (!rec.containsKey("name")) return JsonUtils.getRoot(404, "error", "name为空");
        if (!rec.containsKey("enable")) return JsonUtils.getRoot(404, "error", "enable为空");
        if (!rec.containsKey("version")) return JsonUtils.getRoot(404, "error", "version为空");
        if (!rec.containsKey("platform")) return JsonUtils.getRoot(404, "error", "platform为空");
        if (!rec.containsKey("downloadLink")) return JsonUtils.getRoot(404, "error", "downloadLink为空");
        if (!rec.containsKey("forcedUpdate")) return JsonUtils.getRoot(404, "error", "forcedUpdate为空");
        if (!rec.containsKey("versionChannel")) return JsonUtils.getRoot(404, "error", "versionChannel为空");

        Version.VersionChannel versionChannel;
        Version.Platform platform;

        try {
            versionChannel = Version.VersionChannel.valueOf(rec.getString("versionChannel"));
            platform = Version.Platform.valueOf(rec.getString("platform"));
        } catch (Exception e) {
            return JsonUtils.getRoot(404, "error", "versionChannel或platform转换错误");
        }

        Optional<Version> versionOptional = versionRepository.findById(rec.getLong("id"));
        if (versionOptional.isEmpty()) return JsonUtils.getRoot(404, "error", "未找到该version");

        Version version = versionOptional.get();
        version.setLastModifiedDate(new Date());
        version.setName(rec.getString("name"));
        version.setEnable(rec.getBoolean("enable"));
        version.setVersion(rec.getString("version"));
        version.setPlatform(platform);
        version.setDownloadLink(rec.getString("downloadLink"));
        version.setForcedUpdate(rec.getBoolean("forcedUpdate"));
        version.setVersionChannel(versionChannel);

        if (rec.containsKey("image"))
            version.setImage(rec.getString("image"));
        if (rec.containsKey("introduction"))
            version.setIntroduction(rec.getString("introduction"));
        versionRepository.save(version);

        return JsonUtils.getRoot(10000, "success", null);
    }

    /**
     * 删除版本
     * @param rec
     * @return
     */
    @DeleteMapping
    @CrossOrigin(methods = { RequestMethod.DELETE }, origins = "*")
    public JSONObject delete(@RequestBody JSONArray rec) {
        if (null != rec) {
            for (int i = 0; i < rec.size(); i++)
                versionRepository.deleteById(rec.getLong(i));
        } else
            return JsonUtils.getRoot(10000, "success", "未接收到需要删除的id");

        return JsonUtils.getRoot(10000, "success", null);
    }

}
