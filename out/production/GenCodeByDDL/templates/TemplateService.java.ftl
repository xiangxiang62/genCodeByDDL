package ${packageName}.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import ${packageName}.model.dto.${tableName}.${caseTableName}QueryRequest;
import ${packageName}.model.entity.${caseTableName};
import ${packageName}.model.vo.${caseTableName}VO;

import jakarta.servlet.http.HttpServletRequest;

/**
 * ${classComment}服务
 *
 * @author GenCodeByDDLPlugins
 */
public interface ${caseTableName}Service extends IService<${caseTableName}> {

    /**
     * 校验数据
     *
     * @param ${tableName} 数据
     * @param add 对创建的数据进行校验
     */
    void valid${caseTableName}(${caseTableName} ${tableName}, boolean add);

    /**
     * 获取查询条件
     *
     * @param ${tableName}QueryRequest 查询条件
     * @return QueryWrapper
     */
    QueryWrapper<${caseTableName}> getQueryWrapper(${caseTableName}QueryRequest ${tableName}QueryRequest);
    
    /**
     * 获取${classComment}封装
     *
     * @param ${tableName} ${classComment}实体
     * @return ${caseTableName}VO
     */
    ${caseTableName}VO get${caseTableName}VO(${caseTableName} ${tableName});

    /**
     * 分页获取${classComment}封装
     *
     * @param ${tableName}Page 分页数据
     * @return Page<${caseTableName}VO>
     */
    Page<${caseTableName}VO> get${caseTableName}VOPage(Page<${caseTableName}> ${tableName}Page);
}
