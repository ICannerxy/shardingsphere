/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.data.pipeline.api.config.ingest;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.shardingsphere.data.pipeline.spi.ratelimit.JobRateLimitAlgorithm;

/**
 * Inventory dumper configuration.
 */
@Getter
@Setter
@ToString(callSuper = true)
// TODO fields final
public final class InventoryDumperConfiguration extends DumperConfiguration {
    
    private String actualTableName;
    
    private String logicTableName;
    
    private String uniqueKey;
    
    private Integer uniqueKeyDataType;
    
    private Integer shardingItem;
    
    private int batchSize = 1000;
    
    private JobRateLimitAlgorithm rateLimitAlgorithm;
    
    public InventoryDumperConfiguration(final DumperConfiguration dumperConfig) {
        setDataSourceName(dumperConfig.getDataSourceName());
        setDataSourceConfig(dumperConfig.getDataSourceConfig());
        setTableNameMap(dumperConfig.getTableNameMap());
        setTableNameSchemaNameMapping(dumperConfig.getTableNameSchemaNameMapping());
    }
}
