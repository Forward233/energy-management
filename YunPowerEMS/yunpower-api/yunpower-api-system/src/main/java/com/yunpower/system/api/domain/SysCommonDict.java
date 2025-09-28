package com.yunpower.system.api.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.web.domain.BaseEntity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 通用字典对象 sys_common_dict
 * yunpower-common-security 中有引用，所以放到此处
 *
 * @author yunpower
 * @date 2023-09-14
 */
@Schema(description = "通用字典对象")
public class SysCommonDict extends BaseEntity {

    private static final long serialVersionUID = -7130910602591636589L;
    /**
     * 编号ID
     */
    @Schema(description = "编号ID")
    private Long id;

    /**
     * 枚举名称
     */
    @Schema(description = "枚举名称")
    @Excel(name = "枚举名称")
    private String dictName;

    /**
     * 字典编码
     */
    @Schema(description = "字典编码")
    @Excel(name = "字典编码")
    private String dictSn;

    /**
     * 是否停用（0正常 1停用）
     */
    @Schema(description = "是否停用（0正常 1停用）")
    @Excel(name = "是否停用", readConverterExp = "0=正常,1=停用")
    private Integer stopFlag;

    /**
     * 是否删除（0正常 1删除）
     */
    @Schema(description = "是否删除（0正常 1删除）")
    @Excel(name = "是否删除", readConverterExp = "0=正常,1=删除")
    private Integer deleteFlag;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    @NotBlank(message = "字典名称不能为空")
    @Size(min = 0, max = 100, message = "字典类型名称长度不能超过100个字符")
    public String getDictName() {
        return dictName;
    }

    public void setDictSn(String dictSn) {
        this.dictSn = dictSn;
    }

    @NotBlank(message = "字典编码不能为空")
    @Size(min = 0, max = 100, message = "字典编码类型长度不能超过100个字符")
    @Pattern(regexp = "^[a-z][a-z0-9_]*$", message = "字典编码必须以字母开头，且只能为（小写字母，数字，下滑线）")
    public String getDictSn() {
        return dictSn;
    }

    public void setStopFlag(Integer stopFlag) {
        this.stopFlag = stopFlag;
    }

    public Integer getStopFlag() {
        return stopFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("dictName", getDictName())
                .append("dictSn", getDictSn())
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
