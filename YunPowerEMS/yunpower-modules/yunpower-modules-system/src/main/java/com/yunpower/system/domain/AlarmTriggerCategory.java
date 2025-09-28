package com.yunpower.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.yunpower.system.domain.handler.JsonCommonTypeHandler;
import com.yunpower.system.domain.jsonvo.JsonCommonVo;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.web.domain.BaseEntity;

import java.util.List;

/**
 * 报警类型对象 alarm_trigger_category
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Schema(description = "报警类型对象")
public class AlarmTriggerCategory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号ID */
    @Schema(description = "编号ID")
    private Long id;

    /** 企业ID */
    @Schema(description = "企业ID")
    @Excel(name = "企业ID")
    private Long entId;

    /** 部门ID */
    @Schema(description = "部门ID")
    @Excel(name = "部门ID")
    private Long deptId;

    /** 类型名称 */
    @Schema(description = "类型名称")
    @Excel(name = "类型名称")
    private String triggerName;

    /** 类型编码 */
    @Schema(description = "类型编码")
    @Excel(name = "类型编码")
    private String triggerSn;

    /** 报警级别 */
    @Schema(description = "报警级别")
    @Excel(name = "报警级别")
    private Integer triggerLevel;

    /** 触发类型 */
    @Schema(description = "触发类型")
    @Excel(name = "触发类型")
    private String triggerType;

    /** 适用类型（1模拟量 2状态量） */
    @Schema(description = "适用类型（1模拟量 2状态量）")
    @Excel(name = "适用类型", readConverterExp = "1=模拟量,2=状态量")
    private Integer suitType;

    /** 报警提示 */
    @Schema(description = "报警提示")
    @Excel(name = "报警提示")
    private Integer isAlarm;

    /** 报警方式（JSON格式） */
    @Schema(description = "报警方式（JSON格式）")
    @Excel(name = "报警方式", readConverterExp = "JSON格式")
    @TableField(typeHandler = JsonCommonTypeHandler.class)
    private List<JsonCommonVo> alarmMethod;

    /** 是否停用（0正常 1停用） */
    @Schema(description = "是否停用（0正常 1停用）")
    @Excel(name = "是否停用", readConverterExp = "0=正常,1=停用")
    private Integer stopFlag;

    /** 是否删除（0正常 1删除） */
    @Schema(description = "是否删除（0正常 1删除）")
    @Excel(name = "是否删除", readConverterExp = "0=正常,1=删除")
    private Integer deleteFlag;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public void setEntId(Long entId)
    {
        this.entId = entId;
    }

    public Long getEntId()
    {
        return entId;
    }

    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    public Long getDeptId()
    {
        return deptId;
    }

    public void setTriggerName(String triggerName)
    {
        this.triggerName = triggerName;
    }

    public String getTriggerName()
    {
        return triggerName;
    }

    public void setTriggerSn(String triggerSn)
    {
        this.triggerSn = triggerSn;
    }

    public String getTriggerSn()
    {
        return triggerSn;
    }

    public void setTriggerLevel(Integer triggerLevel)
    {
        this.triggerLevel = triggerLevel;
    }

    public Integer getTriggerLevel()
    {
        return triggerLevel;
    }

    public void setTriggerType(String triggerType)
    {
        this.triggerType = triggerType;
    }

    public String getTriggerType()
    {
        return triggerType;
    }

    public void setSuitType(Integer suitType)
    {
        this.suitType = suitType;
    }

    public Integer getSuitType()
    {
        return suitType;
    }

    public void setIsAlarm(Integer isAlarm)
    {
        this.isAlarm = isAlarm;
    }

    public Integer getIsAlarm()
    {
        return isAlarm;
    }

    public List<JsonCommonVo> getAlarmMethod() {
        return alarmMethod;
    }

    public void setAlarmMethod(List<JsonCommonVo> alarmMethod) {
        this.alarmMethod = alarmMethod;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("entId", getEntId())
                .append("deptId", getDeptId())
                .append("triggerName", getTriggerName())
                .append("triggerSn", getTriggerSn())
                .append("triggerLevel", getTriggerLevel())
                .append("triggerType", getTriggerType())
                .append("suitType", getSuitType())
                .append("isAlarm", getIsAlarm())
                .append("alarmMethod", getAlarmMethod())
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
