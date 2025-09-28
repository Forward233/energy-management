package com.yunpower.system.api.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.web.domain.BaseEntity;

/**
 * 用户登录日志对象 sys_log_login
 * 
 * @author yunpower
 * @date 2023-09-14
 */
@Schema(description = "用户登录日志对象")
public class SysLogLogin extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号ID */
    @Schema(description = "编号ID")
    private Long id;

    /** 用户名称 */
    @Schema(description = "用户名称")
    @Excel(name = "用户名称")
    private String userName;

    /** 部门名称 */
    @Schema(description = "部门名称")
    @Excel(name = "部门名称")
    private String deptName;

    /** 登录IP地址 */
    @Schema(description = "登录IP地址")
    @Excel(name = "登录IP地址")
    private String ipaddr;

    /** 登录地点 */
    @Schema(description = "登录地点")
    @Excel(name = "登录地点")
    private String loginLocation;

    /** 浏览器类型 */
    @Schema(description = "浏览器类型")
    @Excel(name = "浏览器类型")
    private String browser;

    /** 操作系统 */
    @Schema(description = "操作系统")
    @Excel(name = "操作系统")
    private String os;

    /** 登录状态（0成功 1失败） */
    @Schema(description = "登录状态（0成功 1失败）")
    @Excel(name = "登录状态", readConverterExp = "0=成功,1=失败")
    private Integer status;

    /** 提示信息 */
    @Schema(description = "提示信息")
    @Excel(name = "提示信息")
    private String msg;

    /** 访问时间 */
    @Schema(description = "访问时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "访问时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date loginTime;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setUserName(String userName) 
    {
        this.userName = userName;
    }

    public String getUserName() 
    {
        return userName;
    }
    public void setDeptName(String deptName) 
    {
        this.deptName = deptName;
    }

    public String getDeptName() 
    {
        return deptName;
    }
    public void setIpaddr(String ipaddr) 
    {
        this.ipaddr = ipaddr;
    }

    public String getIpaddr() 
    {
        return ipaddr;
    }
    public void setLoginLocation(String loginLocation) 
    {
        this.loginLocation = loginLocation;
    }

    public String getLoginLocation() 
    {
        return loginLocation;
    }
    public void setBrowser(String browser) 
    {
        this.browser = browser;
    }

    public String getBrowser() 
    {
        return browser;
    }
    public void setOs(String os) 
    {
        this.os = os;
    }

    public String getOs() 
    {
        return os;
    }
    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }
    public void setMsg(String msg) 
    {
        this.msg = msg;
    }

    public String getMsg() 
    {
        return msg;
    }
    public void setLoginTime(Date loginTime) 
    {
        this.loginTime = loginTime;
    }

    public Date getLoginTime() 
    {
        return loginTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userName", getUserName())
            .append("deptName", getDeptName())
            .append("ipaddr", getIpaddr())
            .append("loginLocation", getLoginLocation())
            .append("browser", getBrowser())
            .append("os", getOs())
            .append("status", getStatus())
            .append("msg", getMsg())
            .append("loginTime", getLoginTime())
            .toString();
    }
}
