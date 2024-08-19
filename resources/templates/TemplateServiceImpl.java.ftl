package ${packageName}.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ${packageName}.common.ErrorCode;
import ${packageName}.constant.CommonConstant;
import ${packageName}.exception.ThrowUtils;
import ${packageName}.mapper.${caseTableName}Mapper;
import ${packageName}.model.dto.${tableName}.${caseTableName}QueryRequest;
import ${packageName}.model.entity.${caseTableName};
import ${packageName}.model.entity.${caseTableName}Favour;
import ${packageName}.model.entity.${caseTableName}Thumb;
import ${packageName}.model.entity.User;
import ${packageName}.model.vo.${caseTableName}VO;
import ${packageName}.model.vo.UserVO;
import ${packageName}.service.${caseTableName}Service;
import ${packageName}.service.UserService;
import ${packageName}.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ${classComment}服务实现
 *
 * @author GenCodeByDDLPlugins

 */
@Service
@Slf4j
public class ${caseTableName}ServiceImpl extends ServiceImpl<${caseTableName}Mapper, ${caseTableName}> implements ${caseTableName}Service {

    @Resource
    private UserService userService;

    /**
     * 校验数据
     *
     * @param ${tableName}
     * @param add      对创建的数据进行校验
     */
    @Override
    public void valid${caseTableName}(${caseTableName} ${tableName}, boolean add) {
        ThrowUtils.throwIf(${tableName} == null, ErrorCode.PARAMS_ERROR);
        // todo 从对象中取值
        String title = ${tableName}.getTitle();
        // 创建数据时，参数不能为空
        if (add) {
            // todo 补充校验规则
            ThrowUtils.throwIf(StringUtils.isBlank(title), ErrorCode.PARAMS_ERROR);
        }
        // 修改数据时，有参数则校验
        // todo 补充校验规则
        if (StringUtils.isNotBlank(title)) {
            ThrowUtils.throwIf(title.length() > 80, ErrorCode.PARAMS_ERROR, "标题过长");
        }
    }

    /**
     * 获取查询条件
     *
     * @param ${tableName}QueryRequest
     * @return
     */
    @Override
    public QueryWrapper<${caseTableName}> getQueryWrapper(${caseTableName}QueryRequest ${tableName}QueryRequest) {
        QueryWrapper<${caseTableName}> queryWrapper = new QueryWrapper<>();
        if (${tableName}QueryRequest == null) {
            return queryWrapper;
        }
        // todo 从对象中取值
        Long id = ${tableName}QueryRequest.getId();
        Long notId = ${tableName}QueryRequest.getNotId();
        String title = ${tableName}QueryRequest.getTitle();
        String content = ${tableName}QueryRequest.getContent();
        String searchText = ${tableName}QueryRequest.getSearchText();
        String sortField = ${tableName}QueryRequest.getSortField();
        String sortOrder = ${tableName}QueryRequest.getSortOrder();
        List<String> tagList = ${tableName}QueryRequest.getTags();
        Long userId = ${tableName}QueryRequest.getUserId();
        // todo 补充需要的查询条件
        // 从多字段中搜索
        if (StringUtils.isNotBlank(searchText)) {
            // 需要拼接查询条件
            queryWrapper.and(qw -> qw.like("title", searchText).or().like("content", searchText));
        }
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        // JSON 数组查询
        if (CollUtil.isNotEmpty(tagList)) {
            for (String tag : tagList) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取${classComment}封装
     *
     * @param ${tableName}
     * @return
     */
    @Override
    public ${caseTableName}VO get${caseTableName}VO(${caseTableName} ${tableName}) {
        // 对象转封装类
        ${caseTableName}VO ${tableName}VO = ${caseTableName}VO.objToVo(${tableName});

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Long userId = ${tableName}.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        ${tableName}VO.setUser(userVO);
        // 2. 已登录，获取用户点赞、收藏状态
        long ${tableName}Id = ${tableName}.getId();
        User loginUser = userService.getLoginUserPermitNull();
        if (loginUser != null) {
            // 获取点赞
            QueryWrapper<${caseTableName}Thumb> ${tableName}ThumbQueryWrapper = new QueryWrapper<>();
            ${tableName}ThumbQueryWrapper.in("${tableName}Id", ${tableName}Id);
            ${tableName}ThumbQueryWrapper.eq("userId", loginUser.getId());
            ${caseTableName}Thumb ${tableName}Thumb = ${tableName}ThumbMapper.selectOne(${tableName}ThumbQueryWrapper);
            ${tableName}VO.setHasThumb(${tableName}Thumb != null);
            // 获取收藏
            QueryWrapper<${caseTableName}Favour> ${tableName}FavourQueryWrapper = new QueryWrapper<>();
            ${tableName}FavourQueryWrapper.in("${tableName}Id", ${tableName}Id);
            ${tableName}FavourQueryWrapper.eq("userId", loginUser.getId());
            ${caseTableName}Favour ${tableName}Favour = ${tableName}FavourMapper.selectOne(${tableName}FavourQueryWrapper);
            ${tableName}VO.setHasFavour(${tableName}Favour != null);
        }
        // endregion

        return ${tableName}VO;
    }

    /**
     * 分页获取${classComment}封装
     *
     * @param ${tableName}Page 分页对象
     * @return 分页对象
     */
    @Override
    public Page<${caseTableName}VO> get${caseTableName}VOPage(Page<${caseTableName}> ${tableName}Page) {
        List<${caseTableName}> ${tableName}List = ${tableName}Page.getRecords();
        Page<${caseTableName}VO> ${tableName}VOPage = new Page<>(${tableName}Page.getCurrent(), ${tableName}Page.getSize(), ${tableName}Page.getTotal());
        if (CollUtil.isEmpty(${tableName}List)) {
            return ${tableName}VOPage;
        }
        // 对象列表 => 封装对象列表
        List<${caseTableName}VO> ${tableName}VOList = ${tableName}List.stream().map(${tableName} -> {
            return ${caseTableName}VO.objToVo(${tableName});
        }).collect(Collectors.toList());

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = ${tableName}List.stream().map(${caseTableName}::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 2. 已登录，获取用户点赞、收藏状态
        Map<Long, Boolean> ${tableName}IdHasThumbMap = new HashMap<>();
        Map<Long, Boolean> ${tableName}IdHasFavourMap = new HashMap<>();
        User loginUser = userService.getLoginUserPermitNull();
        if (loginUser != null) {
            Set<Long> ${tableName}IdSet = ${tableName}List.stream().map(${caseTableName}::getId).collect(Collectors.toSet());
            loginUser = userService.getLoginUser();
            // 获取点赞
            QueryWrapper<${caseTableName}Thumb> ${tableName}ThumbQueryWrapper = new QueryWrapper<>();
            ${tableName}ThumbQueryWrapper.in("${tableName}Id", ${tableName}IdSet);
            ${tableName}ThumbQueryWrapper.eq("userId", loginUser.getId());
            List<${caseTableName}Thumb> ${tableName}${caseTableName}ThumbList = ${tableName}ThumbMapper.selectList(${tableName}ThumbQueryWrapper);
            ${tableName}${caseTableName}ThumbList.forEach(${tableName}${caseTableName}Thumb -> ${tableName}IdHasThumbMap.put(${tableName}${caseTableName}Thumb.get${caseTableName}Id(), true));
            // 获取收藏
            QueryWrapper<${caseTableName}Favour> ${tableName}FavourQueryWrapper = new QueryWrapper<>();
            ${tableName}FavourQueryWrapper.in("${tableName}Id", ${tableName}IdSet);
            ${tableName}FavourQueryWrapper.eq("userId", loginUser.getId());
            List<${caseTableName}Favour> ${tableName}FavourList = ${tableName}FavourMapper.selectList(${tableName}FavourQueryWrapper);
            ${tableName}FavourList.forEach(${tableName}Favour -> ${tableName}IdHasFavourMap.put(${tableName}Favour.get${caseTableName}Id(), true));
        }
        // 填充信息
        ${tableName}VOList.forEach(${tableName}VO -> {
            Long userId = ${tableName}VO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            ${tableName}VO.setUser(userService.getUserVO(user));
            ${tableName}VO.setHasThumb(${tableName}IdHasThumbMap.getOrDefault(${tableName}VO.getId(), false));
            ${tableName}VO.setHasFavour(${tableName}IdHasFavourMap.getOrDefault(${tableName}VO.getId(), false));
        });
        // endregion

        ${tableName}VOPage.setRecords(${tableName}VOList);
        return ${tableName}VOPage;
    }

}
