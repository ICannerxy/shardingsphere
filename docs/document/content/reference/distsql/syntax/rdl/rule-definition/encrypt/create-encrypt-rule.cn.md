+++
title = "CREATE ENCRYPT RULE"
weight = 2
+++

## 描述

`CREATE ENCRYPT RULE` 语法用于创建数据加密规则

### 语法定义

{{< tabs >}}
{{% tab name="语法" %}}
```sql
CreateEncryptRule ::=
  'CREATE' 'ENCRYPT' 'RULE' ifNotExists? encryptDefinition (',' encryptDefinition)*

ifNotExists ::=
  'IF' 'NOT' 'EXISTS'

encryptDefinition ::=
  ruleName '(' 'COLUMNS' '(' columnDefinition (',' columnDefinition)*  ')' (',' 'QUERY_WITH_CIPHER_COLUMN' '=' ('TRUE' | 'FALSE'))? ')'

columnDefinition ::=
  '(' 'NAME' '=' columnName (',' 'PLAIN' '=' plainColumnName)? ',' 'CIPHER' '=' cipherColumnName (',' 'ASSISTED_QUERY_COLUMN' '=' assistedQueryColumnName)? (',' 'LIKE_QUERY_COLUMN' '=' likeQueryColumnName)? ',' encryptAlgorithmDefinition (',' assistedQueryAlgorithmDefinition)? (',' likeQueryAlgorithmDefinition)? ')' 

encryptAlgorithmDefinition ::=
  'ENCRYPT_ALGORITHM' '(' 'TYPE' '(' 'NAME' '=' encryptAlgorithmType (',' propertiesDefinition)? ')'

assistedQueryAlgorithmDefinition ::=
  'ASSISTED_QUERY_ALGORITHM' '(' 'TYPE' '(' 'NAME' '=' encryptAlgorithmType (',' propertiesDefinition)? ')'

likeQueryAlgorithmDefinition ::=
  'LIKE_QUERY_ALGORITHM' '(' 'TYPE' '(' 'NAME' '=' encryptAlgorithmType (',' propertiesDefinition)? ')'

propertiesDefinition ::=
  'PROPERTIES' '(' key '=' value (',' key '=' value)* ')'

tableName ::=
  identifier

columnName ::=
  identifier

plainColumnName ::=
  identifier

cipherColumnName ::=
  identifier

assistedQueryColumnName ::=
  identifier

likeQueryColumnName ::=
  identifier

encryptAlgorithmType ::=
  string

key ::=
  string

value ::=
  literal
```
{{% /tab %}}
{{% tab name="铁路图" %}}
<iframe frameborder="0" name="diagram" id="diagram" width="100%" height="100%"></iframe>
{{% /tab %}}
{{< /tabs >}}

### 补充说明

- `PLAIN` 指定明文数据列，`CIPHER` 指定密文数据列，`ASSISTED_QUERY_COLUMN` 指定辅助查询列，`LIKE_QUERY_COLUMN` 指定模糊查询列；
- `encryptAlgorithmType` 指定加密算法类型，请参考 [加密算法](/cn/user-manual/common-config/builtin-algorithm/encrypt/)；
- 重复的 `ruleName` 将无法被创建；
- `ifNotExists` 子句用于避免出现 `Duplicate encrypt rule` 错误。

### 示例

#### 创建数据加密规则

```sql
CREATE ENCRYPT RULE t_encrypt (
COLUMNS(
(NAME=user_id,PLAIN=user_plain,CIPHER=user_cipher,ENCRYPT_ALGORITHM(TYPE(NAME='AES',PROPERTIES('aes-key-value'='123456abc')))),
(NAME=order_id, CIPHER =order_cipher,ENCRYPT_ALGORITHM(TYPE(NAME='MD5')))
),QUERY_WITH_CIPHER_COLUMN=true),
t_encrypt_2 (
COLUMNS(
(NAME=user_id,PLAIN=user_plain,CIPHER=user_cipher,ENCRYPT_ALGORITHM(TYPE(NAME='AES',PROPERTIES('aes-key-value'='123456abc')))),
(NAME=order_id, CIPHER=order_cipher,ENCRYPT_ALGORITHM(TYPE(NAME='MD5')))
), QUERY_WITH_CIPHER_COLUMN=FALSE);
```

#### 使用 `ifNotExists` 子句创建数据加密规则

```sql
CREATE ENCRYPT RULE IF NOT EXISTS t_encrypt (
COLUMNS(
(NAME=user_id,PLAIN=user_plain,CIPHER=user_cipher,ENCRYPT_ALGORITHM(TYPE(NAME='AES',PROPERTIES('aes-key-value'='123456abc')))),
(NAME=order_id, CIPHER =order_cipher,ENCRYPT_ALGORITHM(TYPE(NAME='MD5')))
),QUERY_WITH_CIPHER_COLUMN=true),
t_encrypt_2 (
COLUMNS(
(NAME=user_id,PLAIN=user_plain,CIPHER=user_cipher,ENCRYPT_ALGORITHM(TYPE(NAME='AES',PROPERTIES('aes-key-value'='123456abc')))),
(NAME=order_id, CIPHER=order_cipher,ENCRYPT_ALGORITHM(TYPE(NAME='MD5')))
), QUERY_WITH_CIPHER_COLUMN=FALSE);
```

### 保留字

`CREATE`、`ENCRYPT`、`RULE`、`COLUMNS`、`NAME`、`CIPHER`、`PLAIN`、`QUERY_WITH_CIPHER_COLUMN`、`TYPE`、`TRUE`、`FALSE`

### 相关链接

- [保留字](/cn/reference/distsql/syntax/reserved-word/)
- [加密算法](/cn/user-manual/common-config/builtin-algorithm/encrypt/)