<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.${packageName}.mapper.${caseTableName}Mapper">

    <resultMap id="BaseResultMap" type="com.${packageName}.entity.${caseTableName}">
        <#list fieldList as field>
            <#if field.isPrimaryKey>
            <id property="${field.fieldName}" column="${field.fieldName}" jdbcType="${field.jdbcType}"/>
            <#else>
            <result property="${field.fieldName}" column="${field.fieldName}" jdbcType="${field.jdbcType}"/>
            </#if>
        </#list>
    </resultMap>

    <sql id="Base_Column_List">
        <#list fieldList as field>
            ${field.fieldName}<#if field_has_next>,</#if>
        </#list>
    </sql>

</mapper>
