package com.yunpower.system.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yunpower.common.log.annotation.Log;
import com.yunpower.common.log.enums.BusinessType;
import com.yunpower.common.security.annotation.RequiresPermissions;
import com.yunpower.system.domain.SysFiles;
import com.yunpower.system.service.ISysFilesService;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.common.core.utils.poi.ExcelUtil;
import com.yunpower.common.core.web.page.TableDataInfo;

/**
 * 上传文件管理
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Tag(name = "W 上传文件管理表", description = "W 上传文件管理表")
@RestController
@RequestMapping("/files")
public class SysFilesController extends BaseController {
    @Autowired
    private ISysFilesService sysFilesService;

    /**
     * 查询上传文件管理列表
     */
    @Operation(summary = "查询上传文件管理列表")
    @RequiresPermissions("system:files:list")
    @GetMapping("/list")
    public TableDataInfo list(SysFiles sysFiles) {
        startPage();
        List<SysFiles> list = sysFilesService.selectSysFilesList(sysFiles);
        return getDataTable(list);
    }

    /**
     * 导出上传文件管理列表
     */
    @Operation(summary = "导出上传文件管理列表")
    @RequiresPermissions("system:files:export")
    @Log(title = "上传文件管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysFiles sysFiles) {
        List<SysFiles> list = sysFilesService.selectSysFilesList(sysFiles);
        ExcelUtil<SysFiles> util = new ExcelUtil<SysFiles>(SysFiles.class);
        util.exportExcel(response, list, "上传文件管理数据");
    }

    /**
     * 获取上传文件管理详细信息
     */
    @Operation(summary = "获取上传文件管理详细信息")
    @RequiresPermissions("system:files:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(sysFilesService.selectSysFilesById(id));
    }

    /**
     * 新增上传文件管理
     */
    @Operation(summary = "新增上传文件管理")
    @RequiresPermissions("system:files:add")
    @Log(title = "上传文件管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysFiles sysFiles) {
        return toAjax(sysFilesService.insertSysFiles(sysFiles));
    }

    /**
     * 修改上传文件管理
     */
    @Operation(summary = "修改上传文件管理")
    @RequiresPermissions("system:files:edit")
    @Log(title = "上传文件管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysFiles sysFiles) {
        return toAjax(sysFilesService.updateSysFiles(sysFiles));
    }

    /**
     * 修改上传文件管理状态
     */
    @Operation(summary = "修改上传文件管理状态")
    @RequiresPermissions("system:files:state")
    @Log(title = "上传文件管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus/{id}/{state}")
    public AjaxResult changeStatus(@PathVariable Long id, @PathVariable Integer state) {
        return toAjax(sysFilesService.updateSysFilesState(new SysFiles(), id, state));
    }

    /**
     * 删除上传文件管理
     */
    @Operation(summary = "删除上传文件管理")
    @RequiresPermissions("system:files:remove")
    @Log(title = "上传文件管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sysFilesService.deleteSysFilesByIds(new SysFiles(), ids));
    }
}
