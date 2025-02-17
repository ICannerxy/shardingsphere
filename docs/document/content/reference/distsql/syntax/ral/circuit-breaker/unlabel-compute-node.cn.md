+++
title = "UNLABEL COMPUTE NODE"
weight = 7
+++

### 描述

`UNLABEL COMPUTE NODE` 语法用于为 `PROXY` 实例去除指定标签

### 语法

{{< tabs >}}
{{% tab name="语法" %}}
```sql
UnlabelComputeNode ::=
  'UNLABEL' 'COMPUTE' 'NODE' instance_id 'WITH' labelName

instance_id ::=
  string

labelName ::=
  identifier
```
{{% /tab %}}
{{% tab name="铁路图" %}}
<iframe frameborder="0" name="diagram" id="diagram" width="100%" height="100%"></iframe>
{{% /tab %}}
{{< /tabs >}}

### 补充说明

- `instance_id` 需要通过 [SHOW COMPUTE NODES](/cn/reference/distsql/syntax/ral/circuit-breaker/show-compute-nodes/) 语法查询获得

### 示例

- 为 `PROXY` 实例去除指定标签

```sql
UNLABEL COMPUTE NODE "0699e636-ade9-4681-b37a-65240c584bb3" WITH label_1;
```

### 保留字

`UNLABEL`、`COMPUTE`、`NODE`、`WITH`

### 相关链接

- [保留字](/cn/reference/distsql/syntax/reserved-word/)
- [SHOW COMPUTE NODES](/cn/reference/distsql/syntax/ral/circuit-breaker/show-compute-nodes/)