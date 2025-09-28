package com.yunpower.system.controller;

import com.yunpower.common.core.utils.poi.ExcelUtil;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.common.core.web.page.TableDataInfo;
import com.yunpower.common.log.annotation.Log;
import com.yunpower.common.log.enums.BusinessType;
import com.yunpower.common.security.annotation.InnerAuth;
import com.yunpower.common.security.annotation.RequiresPermissions;
import com.yunpower.system.api.domain.ReportTemplates;
import com.yunpower.system.service.IReportTemplatesService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 报表模版
 *
 * @author yunpower
 * @date 2024-06-06
 */
@Tag(name = "B 报表模版表", description = "B 报表模版表")
@RestController
@RequestMapping("/report-templates")
public class ReportTemplatesController extends BaseController
{
    @Autowired
    private IReportTemplatesService reportTemplatesService;

    /**
     * 查询报表模版列表
     */
    @Operation(summary = "查询报表模版列表")
    @RequiresPermissions("system:report-templates:list")
    @GetMapping("/list")
    public TableDataInfo list(ReportTemplates reportTemplates)
    {
        startPage();
        List<ReportTemplates> list = reportTemplatesService.selectReportTemplatesList(reportTemplates);
        return getDataTable(list);
    }

    /**
     * 查询报表模版列表-不分页
     */
    @Operation(summary = "查询报表模版列表-不分页")
    @RequiresPermissions("system:report-templates:list")
    @GetMapping("/listAll")
    public AjaxResult listAll(ReportTemplates reportTemplates)
    {
        List<ReportTemplates> list = reportTemplatesService.selectReportTemplatesList(reportTemplates);
        return success(list);
    }

    /**
     * 获取报表模版详细信息
     */
    @Operation(summary = "获取报表模版详细信息")
    @RequiresPermissions("system:report-templates:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(reportTemplatesService.selectReportTemplatesById(id));
    }

    /**
     * 获取报表模板配置
     */
    @Operation(summary = "获取报表模板配置")
    @InnerAuth
    @GetMapping("/inner/{templateId}")
    public ReportTemplates innerGetInfo(@PathVariable("templateId") Long templateId) {
        return reportTemplatesService.selectReportTemplatesById(templateId);
    }
}
