package ${packageName}.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.dev33.satoken.annotation.SaCheckRole;
import ${packageName}.common.BaseResponse;
import ${packageName}.common.DeleteRequest;
import ${packageName}.common.ErrorCode;
import ${packageName}.common.ResultUtils;
import ${packageName}.constant.UserConstant;
import ${packageName}.exception.BusinessException;
import ${packageName}.exception.ThrowUtils;
import ${packageName}.model.dto.${tableName}.${caseTableName}AddRequest;
import ${packageName}.model.dto.${tableName}.${caseTableName}EditRequest;
import ${packageName}.model.dto.${tableName}.${caseTableName}QueryRequest;
import ${packageName}.model.dto.${tableName}.${caseTableName}UpdateRequest;
import ${packageName}.model.entity.${caseTableName};
import ${packageName}.model.entity.User;
import ${packageName}.model.vo.${caseTableName}VO;
import ${packageName}.service.${caseTableName}Service;
import ${packageName}.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

/**
 * ${classComment}接口
 *
 * @author <a href="https://github.com/lhccong">聪</a>

 */
@RestController
@RequestMapping("/${tableName}")
@Slf4j
@Tag(name = "${classComment}接口")
public class ${caseTableName}Controller {

    @Resource
    private ${caseTableName}Service ${tableName}Service;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建${classComment}
     *
     * @param ${tableName}AddRequest 创建${classComment}请求
     * @return {@link BaseResponse }<{@link Long }>
     */
    @PostMapping("/add")
    @Operation(summary = "创建${classComment}")
    public BaseResponse<Long> add${caseTableName}(@RequestBody ${caseTableName}AddRequest ${tableName}AddRequest) {
        ThrowUtils.throwIf(${tableName}AddRequest == null, ErrorCode.PARAMS_ERROR);
        // todo 在此处将实体类和 DTO 进行转换
        ${caseTableName} ${tableName} = new ${caseTableName}();
        BeanUtils.copyProperties(${tableName}AddRequest, ${tableName});
        // 数据校验
        ${tableName}Service.valid${caseTableName}(${tableName}, true);
        // todo 填充默认值
        User loginUser = userService.getLoginUser();
        ${tableName}.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = ${tableName}Service.save(${tableName});
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long new${caseTableName}Id = ${tableName}.getId();
        return ResultUtils.success(new${caseTableName}Id);
    }

    /**
     * 删除${classComment}
     *
     * @param deleteRequest 删除${classComment}请求
     * @return {@link BaseResponse }<{@link Boolean }>
     */
    @PostMapping("/delete")
    @Operation(summary = "删除${classComment}")
    public BaseResponse<Boolean> delete${caseTableName}(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser();
        long id = deleteRequest.getId();
        // 判断是否存在
        ${caseTableName} old${caseTableName} = ${tableName}Service.getById(id);
        ThrowUtils.throwIf(old${caseTableName} == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!old${caseTableName}.getId().equals(user.getId()) && !userService.isAdmin()) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = ${tableName}Service.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新${classComment}（仅管理员可用）
     *
     * @param ${tableName}UpdateRequest 更新${classComment}请求
     * @return {@link BaseResponse }<{@link Boolean }>
     */
    @PostMapping("/update")
    @Operation(summary = "更新${classComment}（仅管理员可用）")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> update${caseTableName}(@RequestBody ${caseTableName}UpdateRequest ${tableName}UpdateRequest) {
        if (${tableName}UpdateRequest == null || ${tableName}UpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        ${caseTableName} ${tableName} = new ${caseTableName}();
        BeanUtils.copyProperties(${tableName}UpdateRequest, ${tableName});
        // 数据校验
        ${tableName}Service.valid${caseTableName}(${tableName}, false);
        // 判断是否存在
        long id = ${tableName}UpdateRequest.getId();
        ${caseTableName} old${caseTableName} = ${tableName}Service.getById(id);
        ThrowUtils.throwIf(old${caseTableName} == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = ${tableName}Service.updateById(${tableName});
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取${classComment}（封装类）
     *
     * @param id ${classComment} id
     * @return {@link BaseResponse }<{@link ${caseTableName}VO }>
     */
    @GetMapping("/get/vo")
    @Operation(summary = "根据 id 获取${classComment}（封装类）")
    public BaseResponse<${caseTableName}VO> get${caseTableName}VOById(long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        ${caseTableName} ${tableName} = ${tableName}Service.getById(id);
        ThrowUtils.throwIf(${tableName} == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(${tableName}Service.get${caseTableName}VO(${tableName}));
    }

    /**
     * 分页获取${classComment}列表（仅管理员可用）
     *
     * @param ${tableName}QueryRequest 分页获取${classComment}列表请求
     * @return {@link BaseResponse }<{@link Page }<{@link ${caseTableName} }>>
     */
    @PostMapping("/list/page")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    @Operation(summary = "分页获取${classComment}列表（仅管理员可用）")
    public BaseResponse<Page<${caseTableName}>> list${caseTableName}ByPage(@RequestBody ${caseTableName}QueryRequest ${tableName}QueryRequest) {
        long current = ${tableName}QueryRequest.getCurrent();
        long size = ${tableName}QueryRequest.getPageSize();
        // 查询数据库
        Page<${caseTableName}> ${tableName}Page = ${tableName}Service.page(new Page<>(current, size),
                ${tableName}Service.getQueryWrapper(${tableName}QueryRequest));
        return ResultUtils.success(${tableName}Page);
    }

    /**
     * 分页获取${classComment}列表（封装类）
     *
     * @param ${tableName}QueryRequest 分页获取${classComment}列表请求
     * @return {@link BaseResponse }<{@link Page }<{@link ${caseTableName}VO }>>
     */
    @PostMapping("/list/page/vo")
    @Operation(summary = "分页获取${classComment}列表（封装类）")
    public BaseResponse<Page<${caseTableName}VO>> list${caseTableName}VOByPage(@RequestBody ${caseTableName}QueryRequest ${tableName}QueryRequest) {
        long current = ${tableName}QueryRequest.getCurrent();
        long size = ${tableName}QueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<${caseTableName}> ${tableName}Page = ${tableName}Service.page(new Page<>(current, size),
                ${tableName}Service.getQueryWrapper(${tableName}QueryRequest));
        // 获取封装类
        return ResultUtils.success(${tableName}Service.get${caseTableName}VOPage(${tableName}Page));
    }

    /**
     * 分页获取当前登录用户创建的${classComment}列表
     *
     * @param ${tableName}QueryRequest 分页获取${classComment}列表请求
     * @return {@link BaseResponse }<{@link Page }<{@link ${caseTableName}VO }>>
     */
    @PostMapping("/my/list/page/vo")
    @Operation(summary = "分页获取当前登录用户创建的${classComment}列表")
    public BaseResponse<Page<${caseTableName}VO>> listMy${caseTableName}VOByPage(@RequestBody ${caseTableName}QueryRequest ${tableName}QueryRequest) {
        ThrowUtils.throwIf(${tableName}QueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser();
        ${tableName}QueryRequest.setId(loginUser.getId());
        long current = ${tableName}QueryRequest.getCurrent();
        long size = ${tableName}QueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<${caseTableName}> ${tableName}Page = ${tableName}Service.page(new Page<>(current, size),
                ${tableName}Service.getQueryWrapper(${tableName}QueryRequest));
        // 获取封装类
        return ResultUtils.success(${tableName}Service.get${caseTableName}VOPage(${tableName}Page));
    }

    /**
     * 编辑${classComment}（给用户使用）
     *
     * @param ${tableName}EditRequest 编辑${classComment}请求
     * @return {@link BaseResponse }<{@link Boolean }>
     */
    @PostMapping("/edit")
    @Operation(summary = "编辑${classComment}（给用户使用）")
    public BaseResponse<Boolean> edit${caseTableName}(@RequestBody ${caseTableName}EditRequest ${tableName}EditRequest) {
        if (${tableName}EditRequest == null || ${tableName}EditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        ${caseTableName} ${tableName} = new ${caseTableName}();
        BeanUtils.copyProperties(${tableName}EditRequest, ${tableName});
        // 数据校验
        ${tableName}Service.valid${caseTableName}(${tableName}, false);
        User loginUser = userService.getLoginUser();
        // 判断是否存在
        long id = ${tableName}EditRequest.getId();
        ${caseTableName} old${caseTableName} = ${tableName}Service.getById(id);
        ThrowUtils.throwIf(old${caseTableName} == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!old${caseTableName}.getId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = ${tableName}Service.updateById(${tableName});
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    // endregion
}
