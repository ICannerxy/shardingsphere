+++
title = "Sharding"
weight = 1
+++

## Syntax

### Sharding Table Rule

```sql
CREATE SHARDING TABLE RULE ifNotExistsClause? shardingTableRuleDefinition [, shardingTableRuleDefinition] ...

ALTER SHARDING TABLE RULE shardingTableRuleDefinition [, shardingTableRuleDefinition] ...

DROP SHARDING TABLE RULE tableName [, tableName] ...

CREATE DEFAULT SHARDING shardingScope STRATEGY ifNotExistsClause? (shardingStrategy)

ALTER DEFAULT SHARDING shardingScope STRATEGY (shardingStrategy)

DROP DEFAULT SHARDING shardingScope STRATEGY;

DROP SHARDING ALGORITHM algorithmName [, algorithmName] ...

DROP SHARDING KEY GENERATOR [IF EXISTS] keyGeneratorName [, keyGeneratorName] ...
    
DROP SHARDING AUDITOR [IF EXISTS] auditorName [, auditorName] ...

ifNotExistsClause:
    IF NOT EXISTS

shardingTableRuleDefinition:
    shardingAutoTableRule | shardingTableRule

shardingAutoTableRule:
    tableName(storageUnits, shardingColumn, algorithmDefinition [, keyGenerateDefinition] [, auditDefinition])

shardingTableRule:
    tableName(dataNodes [, databaseStrategy] [, tableStrategy] [, keyGenerateDefinition] [, auditDefinition])

storageUnits:
    STORAGE_UNITS(storageUnit [, storageUnit] ...)

dataNodes:
    DATANODES(dataNode [, dataNode] ...)

storageUnit:
    storageUnitName | inlineExpression

dataNode:
    dataNodeName | inlineExpression

shardingColumn:
    SHARDING_COLUMN=columnName

algorithmDefinition:
    TYPE(NAME=shardingAlgorithmType [, PROPERTIES([algorithmProperties])])

keyGenerateDefinition:
    KEY_GENERATE_STRATEGY(COLUMN=columnName, strategyDefinition)

auditDefinition:
    AUDIT_STRATEGY([singleAuditDefinition, singleAuditDefinition], ALLOW_HINT_DISABLE=true)

singleAuditDefinition:
    algorithmDefinition

shardingScope:
    DATABASE | TABLE

databaseStrategy:
    DATABASE_STRATEGY(shardingStrategy)

tableStrategy:
    TABLE_STRATEGY(shardingStrategy)

shardingStrategy:
    TYPE=strategyType, shardingColumn, shardingAlgorithm

shardingAlgorithm:
    SHARDING_ALGORITHM(algorithmDefinition)

strategyDefinition:
    TYPE(NAME=keyGenerateStrategyType [, PROPERTIES([algorithmProperties])])

algorithmProperties:
    algorithmProperty [, algorithmProperty] ...

algorithmProperty:
    key=value
```
- `STORAGE_UNITS` needs to use storage units managed by RDL
- `shardingAlgorithmType` specifies the type of automatic sharding algorithm, please refer to [Auto Sharding Algorithm](/en/user-manual/common-config/builtin-algorithm/sharding/)
- `keyGenerateStrategyType` specifies the distributed primary key generation strategy, please refer to [Key Generate Algorithm](/en/user-manual/common-config/builtin-algorithm/keygen/)
- `auditorAlgorithmType` specifies the sharding audit strategy, please refer to [Sharding Audit Algorithm](/cn/user-manual/common-config/builtin-algorithm/audit/)；
- Duplicate `tableName` will not be created
- To remove `shardingAlgorithm`, please execute `DROP SHARDING ALGORITHM`
- `strategyType` specifies the sharding strategy, please refer to[Sharding Strategy](/en/features/sharding/concept/#sharding-strategy)
- `Sharding Table Rule` supports both `Auto Table` and `Table` at the same time. The two types are different in syntax. For the corresponding configuration file, please refer to [Sharding](/en/user-manual/shardingsphere-jdbc/yaml-config/rules/sharding/)
- executing `CREATE SHARDING TABLE RULE`，a new sharding algorithm will be created automatically. The algorithm naming rule is `tableName_scope_shardingAlgorithmType`，such as `t_order_database_inline`
- executing `CREATE DEFAULT SHARDING STRATEGY`，a new sharding algorithm is also created automatically，The algorithm naming rule is `default_scope_shardingAlgorithmType`，such as `default_database_inline`

### Sharding Table Reference Rule

```sql
CREATE SHARDING TABLE REFERENCE RULE ifNotExistsClause? tableReferenceRuleDefinition [, tableReferenceRuleDefinition] ...

ALTER SHARDING TABLE REFERENCE RULE tableReferenceRuleDefinition [, tableReferenceRuleDefinition] ...

DROP SHARDING TABLE REFERENCE RULE ifExists? ruleName [, ruleName] ...

ifNotExistsClause:
    IF NOT EXISTS

tableReferenceRuleDefinition:
    ruleName (tableName [, tableName] ... )
```
- A sharding table can only be associated with one sharding table reference rule

### Broadcast Table Rule

```sql
CREATE BROADCAST TABLE RULE ifNotExistsClause? tableName [, tableName] ...

DROP BROADCAST TABLE RULE tableName [, tableName] ...

ifNotExistsClause:
    IF NOT EXISTS
```

## Example

### Sharding Table Rule

*Key Generator*

```sql
DROP SHARDING KEY GENERATOR snowflake_key_generator;
```

*Auditor*

```sql
DROP SHARDING AUDITOR IF EXISTS sharding_key_required_auditor;
```

*Auto Table*
```sql
CREATE SHARDING TABLE RULE IF NOT EXISTS t_order (
STORAGE_UNITS(ds_0,ds_1),
SHARDING_COLUMN=order_id,TYPE(NAME="hash_mod",PROPERTIES("sharding-count"="4")),
KEY_GENERATE_STRATEGY(COLUMN=another_id,TYPE(NAME="snowflake")),
AUDIT_STRATEGY(TYPE(NAME="dml_sharding_conditions"),ALLOW_HINT_DISABLE=true)
);

ALTER SHARDING TABLE RULE t_order (
STORAGE_UNITS(ds_0,ds_1,ds_2,ds_3),
SHARDING_COLUMN=order_id,TYPE(NAME="hash_mod",PROPERTIES("sharding-count"="16")),
KEY_GENERATE_STRATEGY(COLUMN=another_id,TYPE(NAME="snowflake")),
AUDIT_STRATEGY(TYPE(NAME="dml_sharding_conditions"),ALLOW_HINT_DISABLE=true)
);

DROP SHARDING TABLE RULE t_order;

DROP SHARDING ALGORITHM t_order_hash_mod;
```

*Table*

```sql
CREATE SHARDING TABLE RULE IF NOT EXISTS t_order_item (
DATANODES("ds_${0..1}.t_order_item_${0..1}"),
DATABASE_STRATEGY(TYPE="standard",SHARDING_COLUMN=user_id,SHARDING_ALGORITHM(TYPE(NAME="inline",PROPERTIES("algorithm-expression"="ds_${user_id % 2}")))),
TABLE_STRATEGY(TYPE="standard",SHARDING_COLUMN=order_id,SHARDING_ALGORITHM(TYPE(NAME="inline",PROPERTIES("algorithm-expression"="t_order_item_${order_id % 2}")))),
KEY_GENERATE_STRATEGY(COLUMN=another_id,TYPE(NAME="snowflake")),
AUDIT_STRATEGY(TYPE(NAME="dml_sharding_conditions"),ALLOW_HINT_DISABLE=true)
);

ALTER SHARDING TABLE RULE t_order_item (
DATANODES("ds_${0..3}.t_order_item${0..3}"),
DATABASE_STRATEGY(TYPE="standard",SHARDING_COLUMN=user_id,SHARDING_ALGORITHM(TYPE(NAME="inline",PROPERTIES("algorithm-expression"="ds_${user_id % 4}")))),
TABLE_STRATEGY(TYPE="standard",SHARDING_COLUMN=order_id,SHARDING_ALGORITHM(TYPE(NAME="inline",PROPERTIES("algorithm-expression"="t_order_item_${order_id % 4}")))),
KEY_GENERATE_STRATEGY(COLUMN=another_id,TYPE(NAME="snowflake")),
AUDIT_STRATEGY(TYPE(NAME="dml_sharding_conditions"),ALLOW_HINT_DISABLE=true)
);

DROP SHARDING TABLE RULE t_order_item;

DROP SHARDING ALGORITHM database_inline;

CREATE DEFAULT SHARDING DATABASE STRATEGY (
TYPE="standard",SHARDING_COLUMN=order_id,SHARDING_ALGORITHM(TYPE(NAME="inline",PROPERTIES("algorithm-expression"="ds_${order_id % 2}")))
);

ALTER DEFAULT SHARDING DATABASE STRATEGY (
TYPE="standard",SHARDING_COLUMN=another_id,SHARDING_ALGORITHM(TYPE(NAME="inline",PROPERTIES("algorithm-expression"="ds_${another_id % 2}")))
);

DROP DEFAULT SHARDING DATABASE STRATEGY;
```

### Sharding Table Reference Rule

```sql
CREATE SHARDING TABLE REFERENCE RULE IF NOT EXISTS ref_0 (t_order,t_order_item), ref_1 (t_1,t_2);

ALTER SHARDING TABLE REFERENCE RULE ref_0 (t_order,t_order_item,t_user);

DROP SHARDING TABLE REFERENCE RULE ref_0, ref_1;
```

### Broadcast Table Rule

```sql
CREATE BROADCAST TABLE RULE IF NOT EXISTS t_a,t_b;

DROP BROADCAST TABLE RULE t_a;
```
