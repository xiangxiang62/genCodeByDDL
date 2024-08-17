<#-- Java 实体模板 -->
import lombok.Data;

/**
 * ${classComment}
 */
@Data
@Accessors(chain = true)
public class ${className} implements Serializable {

    <#-- 序列化 -->
    /**
    * 序列化 id
    */
    private static final long serialVersionUID = 1L;

<#-- 循环生成字段 ---------->
<#list fieldList as field>
    <#if field.comment!?length gt 0>
    /**
     * ${field.comment}
     */
    </#if>
    private ${field.entityType} ${field.fieldName};

</#list>
}
