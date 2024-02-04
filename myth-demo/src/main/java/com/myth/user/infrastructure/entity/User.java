package com.myth.user.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author MyBatis Plus Generator
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tz_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "user_id")
    private String userId;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 证件号码
     */
    private String idCardNum;

    /**
     * 用户邮箱
     */
    private String userMail;

    /**
     * 登录密码
     */
    private String loginPassword;

    /**
     * 支付密码
     */
    private String payPassword;

    /**
     * 账号登陆使用的账号
     */
    private String userName;

    /**
     * 手机号码
     */
    private String userMobile;

    /**
     * 修改时间
     */
    private LocalDateTime modifyTime;

    /**
     * 注册时间
     */
    private LocalDateTime userRegtime;

    /**
     * 注册IP
     */
    private String userRegip;

    /**
     * 备注
     */
    private String userMemo;

    /**
     * 性别：0 未知；1 男性；2 女性
     */
    private String sex;

    /**
     * 例如：2009-11-27
     */
    private String birthDate;

    /**
     * 头像图片路径
     */
    private String pic;

    /**
     * 状态 1 正常 0 无效
     */
    private Integer status;

    /**
     * 会员等级（冗余字段）
     */
    private Long level;
}
