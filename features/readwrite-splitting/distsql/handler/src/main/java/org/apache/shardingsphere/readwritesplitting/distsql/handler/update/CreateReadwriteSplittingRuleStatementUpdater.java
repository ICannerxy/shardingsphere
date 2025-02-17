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

package org.apache.shardingsphere.readwritesplitting.distsql.handler.update;

import org.apache.shardingsphere.distsql.handler.update.RuleDefinitionCreateUpdater;
import org.apache.shardingsphere.infra.metadata.database.ShardingSphereDatabase;
import org.apache.shardingsphere.readwritesplitting.api.ReadwriteSplittingRuleConfiguration;
import org.apache.shardingsphere.readwritesplitting.distsql.handler.checker.ReadwriteSplittingRuleStatementChecker;
import org.apache.shardingsphere.readwritesplitting.distsql.handler.converter.ReadwriteSplittingRuleStatementConverter;
import org.apache.shardingsphere.readwritesplitting.distsql.parser.segment.ReadwriteSplittingRuleSegment;
import org.apache.shardingsphere.readwritesplitting.distsql.parser.statement.CreateReadwriteSplittingRuleStatement;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Create readwrite-splitting rule statement updater.
 */
public final class CreateReadwriteSplittingRuleStatementUpdater implements RuleDefinitionCreateUpdater<CreateReadwriteSplittingRuleStatement, ReadwriteSplittingRuleConfiguration> {
    
    private Collection<String> duplicatedRuleNames = new LinkedList<>();
    
    @Override
    public void checkSQLStatement(final ShardingSphereDatabase database, final CreateReadwriteSplittingRuleStatement sqlStatement, final ReadwriteSplittingRuleConfiguration currentRuleConfig) {
        Collection<String> duplicatedRuleNames = new LinkedList<>();
        ReadwriteSplittingRuleStatementChecker.checkCreation(database, sqlStatement.getRules(), currentRuleConfig, duplicatedRuleNames, sqlStatement.isIfNotExists());
        this.duplicatedRuleNames = duplicatedRuleNames;
    }
    
    @Override
    public ReadwriteSplittingRuleConfiguration buildToBeCreatedRuleConfiguration(final CreateReadwriteSplittingRuleStatement sqlStatement) {
        Collection<ReadwriteSplittingRuleSegment> segments = sqlStatement.getRules();
        if (!duplicatedRuleNames.isEmpty()) {
            segments.removeIf(each -> duplicatedRuleNames.contains(each.getName()));
        }
        return ReadwriteSplittingRuleStatementConverter.convert(segments);
    }
    
    @Override
    public void updateCurrentRuleConfiguration(final ReadwriteSplittingRuleConfiguration currentRuleConfig, final ReadwriteSplittingRuleConfiguration toBeCreatedRuleConfig) {
        if (null != currentRuleConfig) {
            currentRuleConfig.getDataSources().addAll(toBeCreatedRuleConfig.getDataSources());
            currentRuleConfig.getLoadBalancers().putAll(toBeCreatedRuleConfig.getLoadBalancers());
        }
    }
    
    @Override
    public Class<ReadwriteSplittingRuleConfiguration> getRuleConfigurationClass() {
        return ReadwriteSplittingRuleConfiguration.class;
    }
    
    @Override
    public String getType() {
        return CreateReadwriteSplittingRuleStatement.class.getName();
    }
}
