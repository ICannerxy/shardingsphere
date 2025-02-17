+++
title = "读写分离"
weight = 3
+++

## 语法说明

```sql
CREATE READWRITE_SPLITTING RULE ifNotExistsClause? readwriteSplittingRuleDefinition [, readwriteSplittingRuleDefinition] ...

ALTER READWRITE_SPLITTING RULE readwriteSplittingRuleDefinition [, readwriteSplittingRuleDefinition] ...

DROP READWRITE_SPLITTING RULE ruleName [, ruleName] ...

ifNotExistsClause:
    IF NOT EXISTS

readwriteSplittingRuleDefinition:
    ruleName ([staticReadwriteSplittingRuleDefinition | dynamicReadwriteSplittingRuleDefinition] 
              [, loadBalancerDefinition])

staticReadwriteSplittingRuleDefinition:
    WRITE_STORAGE_UNIT=storageUnitName, READ_STORAGE_UNITS(storageUnitName [, storageUnitName] ... )

dynamicReadwriteSplittingRuleDefinition:
    AUTO_AWARE_RESOURCE=autoAwareResourceName [, WRITE_DATA_SOURCE_QUERY_ENABLED=writeDataSourceQueryEnabled]

loadBalancerDefinition:
    TYPE(NAME=loadBalancerType [, PROPERTIES([algorithmProperties] )] )

algorithmProperties:
    algorithmProperty [, algorithmProperty] ...

algorithmProperty:
    key=value

writeDataSourceQueryEnabled:
    TRUE | FALSE
```

### 参数解释
| 名称                            | 数据类型      | 说明                         |
|:-------------------------------|:-------------|:----------------------------|
| ruleName                       | IDENTIFIER   | 规则名称                      |
| storageUnitName                | IDENTIFIER   | 已注册的数据源名称              |
| autoAwareResourceName          | IDENTIFIER   | 数据库发现的逻辑数据源名称       |
| writeDataSourceQueryEnabled    | BOOLEAN      | 读库全部下线，主库是否承担读流量  |
| loadBalancerType               | STRING       | 负载均衡算法类型               |

### 注意事项

- 支持创建静态读写分离规则和动态读写分离规则；
- 动态读写分离规则依赖于数据库发现规则；
- `loadBalancerType` 指定负载均衡算法类型，请参考 [负载均衡算法](/cn/user-manual/common-config/builtin-algorithm/load-balance/)；
- 重复的 `ruleName` 将无法被创建。

## 示例

```sql
// Static
CREATE READWRITE_SPLITTING RULE IF NOT EXISTS ms_group_0 (
WRITE_STORAGE_UNIT=write_ds,
READ_STORAGE_UNITS(read_ds_0,read_ds_1),
TYPE(NAME="random")
);

// Dynamic
CREATE READWRITE_SPLITTING RULE IF NOT EXISTS ms_group_1 (
AUTO_AWARE_RESOURCE=group_0,
WRITE_DATA_SOURCE_QUERY_ENABLED=false,
TYPE(NAME="random")
);

ALTER READWRITE_SPLITTING RULE ms_group_1 (
WRITE_STORAGE_UNIT=write_ds,
READ_STORAGE_UNITS(read_ds_0,read_ds_1,read_ds_2),
TYPE(NAME="random")
);

DROP READWRITE_SPLITTING RULE ms_group_1;
```
