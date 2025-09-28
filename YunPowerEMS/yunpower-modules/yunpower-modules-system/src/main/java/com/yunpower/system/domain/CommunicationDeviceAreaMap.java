package com.yunpower.system.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.web.domain.BaseEntity;

/**
 * 通讯设备数据区域类型（公共）对象 communication_device_area_map
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Schema(description = "通讯设备数据区域类型（公共）对象")
public class CommunicationDeviceAreaMap extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号ID */
    @Schema(description = "编号ID")
    private Long id;

    /** 协议 */
    @Schema(description = "协议")
    @Excel(name = "协议")
    private String protocol;

    /** 寄存器区 */
    @Schema(description = "寄存器区")
    @Excel(name = "寄存器区")
    private String area;

    /** 读写（1只读 2只写 3读写） */
    @Schema(description = "读写（1只读 2只写 3读写）")
    @Excel(name = "读写", readConverterExp = "1=只读,2=只写,3=读写")
    private Integer rw;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public void setProtocol(String protocol)
    {
        this.protocol = protocol;
    }

    public String getProtocol()
    {
        return protocol;
    }

    public void setArea(String area)
    {
        this.area = area;
    }

    public String getArea()
    {
        return area;
    }

    public void setRw(Integer rw)
    {
        this.rw = rw;
    }

    public Integer getRw()
    {
        return rw;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("protocol", getProtocol())
                .append("area", getArea())
                .append("rw", getRw())
                .toString();
    }
}
