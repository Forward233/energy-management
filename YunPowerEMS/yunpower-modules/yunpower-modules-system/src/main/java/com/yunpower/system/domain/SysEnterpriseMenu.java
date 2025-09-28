package com.yunpower.system.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.web.domain.BaseEntity;

/**
 * 企业专属菜单对象 sys_enterprise_menu
 *
 * @author JUNFU.WANG
 * @date 2023-09-27
 */
@Schema(description = "企业专属菜单对象")
public class SysEnterpriseMenu extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号ID */
    @Schema(description = "编号ID")
    private Long id;

    /** 企业ID */
    @Schema(description = "企业ID")
    @Excel(name = "企业ID")
    private Long entId;

    /** 菜单ID */
    @Schema(description = "菜单ID")
    @Excel(name = "菜单ID")
    private Long menuId;

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

    public void setMenuId(Long menuId)
    {
        this.menuId = menuId;
    }

    public Long getMenuId()
    {
        return menuId;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("entId", getEntId())
                .append("menuId", getMenuId())
                .toString();
    }
}
