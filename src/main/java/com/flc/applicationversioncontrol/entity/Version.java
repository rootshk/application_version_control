package com.flc.applicationversioncontrol.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

/**
 * 版本
 */
@Setter
@Getter
@Entity
public class Version extends IdEntity<Long> {

    /**
     * 平台
     */
    public enum Platform {

        Android,

        iOS

    }

    /**
     * 版本渠道
     */
    public enum VersionChannel {

        /**
         * Alpha是内部测试版，一般不向外部发布，通常会有很多Bug，除非你也是测试人员，否则不建议使用，alpha 就是α，是希腊字母的第一位，表示最初级的版本，beta 就是β，alpha 版就是比beta还早的测试版，一般都是内部测试的版本。
         */
        Alpha,

        /**
         * 该版本相对于α版已有了很大的改进，消除了严重的错误，但还是存在着一缺陷，需要经过多次测试来进一步消除。这个阶段的版本会一直加入新的功能。
         */
        Beta,

        /**
         * RC(Release Candidate)，发行候选版本。和Beta版最大的差别在于Beta阶段会一直加入新的功能，但是到了RC版本，几乎就不会加入新的功能了，而主要着重于除错。RC版本是最终发放给用户的最接近正式版的版本，发行后改正bug就是正式版了，就是正式版之前的最后一个测试版。
         */
        RC,

        /**
         * GA(general availability)， 正式发布的版本。 比如：MySQL Community Server 5.7.21 GA这是MySQL Community Server 5.7 第21个发行稳定的版本，GA意味着General Availability，也就是官方开始推荐广泛使用了。
         */
        GA,

        /**
         * 这个版本通常就是所谓的“最终版本”，在前面版本的一系列测试版之后，终归会有一个正式版本，是最终交付用户使用的一个版本，该版本有时也称为标准版。一般情况下，Release不会以单词形式出现在软件封面上，取而代之的是符号(R)。
         */
        Release

    }

    /**
     * 版本名称
     */
    @Column(nullable = false)
    private String name;

    /**
     * 版本号
     */
    @Column(nullable = false)
    private String version;

    /**
     * 版本平台
     */
    @Column(nullable = false)
    private Platform platform;

    /**
     * 版本渠道
     */
    @Column(nullable = false)
    private VersionChannel versionChannel;

    /**
     * 下载链接
     */
    @Lob
    @Column(nullable = false)
    private String downloadLink;

    /**
     * 版本介绍
     */
    @Lob
    @Column
    private String introduction;

    /**
     * 强制更新
     */
    @Column(nullable = false)
    private Boolean forcedUpdate;

    /**
     * 启用
     */
    @Column(nullable = false)
    private Boolean enable;

    /**
     * 图片
     */
    @Column
    private String image;

    /**
     * 厂商渠道
     */
//    @Column(nullable = false)
//    private String VendorChannel;
}
