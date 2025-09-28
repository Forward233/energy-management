package com.yunpower.system.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunpower.common.core.xss.Xss;
import com.yunpower.system.api.domain.SysStation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.web.domain.BaseEntity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 企业信息对象 sys_common_enterprise
 *
 * @author JUNFU.WANG
 * @date 2023-09-27
 */
@Schema(description = "企业信息对象")
public class SysCommonEnterprise extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号ID */
    @Schema(description = "编号ID")
    private Long id;

    /** 企业名称 */
    @Schema(description = "企业名称")
    @Excel(name = "企业名称")
    private String entName;

    /** 企业编码 */
    @Schema(description = "企业编码")
    @Excel(name = "企业编码")
    private String entSn;

    /** 企业类型 */
    @Schema(description = "企业类型")
    @Excel(name = "企业类型")
    private Integer entType;

    /** 企业Logo */
    @Schema(description = "企业Logo")
    @Excel(name = "企业Logo")
    private String entLogo;

    /** 企业简介 */
    @Schema(description = "企业简介")
    @Excel(name = "企业简介")
    private String entIntro;

    /** 统一社会信用代码 */
    @Schema(description = "统一社会信用代码")
    @Excel(name = "统一社会信用代码")
    private String creditCode;

    /** 企业法人 */
    @Schema(description = "企业法人")
    @Excel(name = "企业法人")
    private String legalPerson;

    /** 联系人 */
    @Schema(description = "联系人")
    @Excel(name = "联系人")
    private String linkName;

    /** 联系电话 */
    @Schema(description = "联系电话")
    @Excel(name = "联系电话")
    private String linkMobile;

    /** 联系地址 */
    @Schema(description = "联系地址")
    @Excel(name = "联系地址")
    private String linkAddress;

    /** 邮箱 */
    @Schema(description = "邮箱")
    @Excel(name = "邮箱")
    private String email;

    /** 经营范围 */
    @Schema(description = "经营范围")
    @Excel(name = "经营范围")
    private String businessScope;

    /** 注册资本 */
    @Schema(description = "注册资本")
    @Excel(name = "注册资本")
    private String registeredCapital;

    /** 成立日期 */
    @Schema(description = "成立日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "成立日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date foundDate;

    /** 营业期限开始 */
    @Schema(description = "营业期限开始")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "营业期限开始", width = 30, dateFormat = "yyyy-MM-dd")
    private Date openStartDate;

    /** 营业期限结束 */
    @Schema(description = "营业期限结束")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "营业期限结束", width = 30, dateFormat = "yyyy-MM-dd")
    private Date openEndDate;

    /** 住所 */
    @Schema(description = "住所")
    @Excel(name = "住所")
    private String address;

    /** 登记机关 */
    @Schema(description = "登记机关")
    @Excel(name = "登记机关")
    private String regOffice;

    /** 发证日期 */
    @Schema(description = "发证日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发证日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date issueDate;

    /** 逻辑代码 */
    @Schema(description = "逻辑代码")
    @Excel(name = "逻辑代码")
    private String logicCode;

    /** 是否停用（0正常 1停用） */
    @Schema(description = "是否停用（0正常 1停用）")
    @Excel(name = "是否停用", readConverterExp = "0=正常,1=停用")
    private Integer stopFlag;

    /** 是否删除（0正常 1删除） */
    @Schema(description = "是否删除（0正常 1删除）")
    private Integer deleteFlag;

    /** 站点对象 */
    private List<SysStation> stations;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public void setEntName(String entName)
    {
        this.entName = entName;
    }

    @Xss(message = "企业名称不能包含脚本字符")
    @NotBlank(message = "企业名称不能为空")
    @Size(min = 0, max = 30, message = "企业名称长度不能超过30个字符")
    public String getEntName()
    {
        return entName;
    }

    public void setEntSn(String entSn)
    {
        this.entSn = entSn;
    }

    @NotBlank(message = "企业编码不能为空")
    public String getEntSn()
    {
        return entSn;
    }

    public void setEntType(Integer entType)
    {
        this.entType = entType;
    }

    public Integer getEntType()
    {
        return entType;
    }

    public void setEntLogo(String entLogo)
    {
        this.entLogo = entLogo;
    }

    public String getEntLogo()
    {
        return entLogo;
    }

    public void setEntIntro(String entIntro)
    {
        this.entIntro = entIntro;
    }

    public String getEntIntro()
    {
        return entIntro;
    }

    public void setCreditCode(String creditCode)
    {
        this.creditCode = creditCode;
    }

    @NotBlank(message = "统一社会信用代码不能为空")
    @Size(min = 0, max = 50, message = "统一社会信用代码长度不能超过50个字符")
    public String getCreditCode()
    {
        return creditCode;
    }

    public void setLegalPerson(String legalPerson)
    {
        this.legalPerson = legalPerson;
    }

    public String getLegalPerson()
    {
        return legalPerson;
    }

    public void setLinkName(String linkName)
    {
        this.linkName = linkName;
    }

    public String getLinkName()
    {
        return linkName;
    }

    public void setLinkMobile(String linkMobile)
    {
        this.linkMobile = linkMobile;
    }

    public String getLinkMobile()
    {
        return linkMobile;
    }

    public void setLinkAddress(String linkAddress)
    {
        this.linkAddress = linkAddress;
    }

    public String getLinkAddress()
    {
        return linkAddress;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getEmail()
    {
        return email;
    }

    public void setBusinessScope(String businessScope)
    {
        this.businessScope = businessScope;
    }

    public String getBusinessScope()
    {
        return businessScope;
    }

    public void setRegisteredCapital(String registeredCapital)
    {
        this.registeredCapital = registeredCapital;
    }

    public String getRegisteredCapital()
    {
        return registeredCapital;
    }

    public void setFoundDate(Date foundDate)
    {
        this.foundDate = foundDate;
    }

    public Date getFoundDate()
    {
        return foundDate;
    }

    public void setOpenStartDate(Date openStartDate)
    {
        this.openStartDate = openStartDate;
    }

    public Date getOpenStartDate()
    {
        return openStartDate;
    }

    public void setOpenEndDate(Date openEndDate)
    {
        this.openEndDate = openEndDate;
    }

    public Date getOpenEndDate()
    {
        return openEndDate;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddress()
    {
        return address;
    }

    public void setRegOffice(String regOffice)
    {
        this.regOffice = regOffice;
    }

    public String getRegOffice()
    {
        return regOffice;
    }

    public void setIssueDate(Date issueDate)
    {
        this.issueDate = issueDate;
    }

    public Date getIssueDate()
    {
        return issueDate;
    }

    public void setLogicCode(String logicCode)
    {
        this.logicCode = logicCode;
    }

    public String getLogicCode()
    {
        return logicCode;
    }

    public void setStopFlag(Integer stopFlag)
    {
        this.stopFlag = stopFlag;
    }

    public Integer getStopFlag()
    {
        return stopFlag;
    }

    public void setDeleteFlag(Integer deleteFlag)
    {
        this.deleteFlag = deleteFlag;
    }

    public Integer getDeleteFlag()
    {
        return deleteFlag;
    }

    public List<SysStation> getStations() {
        return stations;
    }

    public void setStations(List<SysStation> stations) {
        this.stations = stations;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("entName", getEntName())
                .append("entSn", getEntSn())
                .append("entType", getEntType())
                .append("entLogo", getEntLogo())
                .append("entIntro", getEntIntro())
                .append("creditCode", getCreditCode())
                .append("legalPerson", getLegalPerson())
                .append("linkName", getLinkName())
                .append("linkMobile", getLinkMobile())
                .append("linkAddress", getLinkAddress())
                .append("email", getEmail())
                .append("businessScope", getBusinessScope())
                .append("registeredCapital", getRegisteredCapital())
                .append("foundDate", getFoundDate())
                .append("openStartDate", getOpenStartDate())
                .append("openEndDate", getOpenEndDate())
                .append("address", getAddress())
                .append("regOffice", getRegOffice())
                .append("issueDate", getIssueDate())
                .append("logicCode", getLogicCode())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("stopFlag", getStopFlag())
                .append("deleteFlag", getDeleteFlag())
                .toString();
    }
}