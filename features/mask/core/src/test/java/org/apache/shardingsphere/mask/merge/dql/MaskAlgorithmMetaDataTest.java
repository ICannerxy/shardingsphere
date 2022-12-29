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

package org.apache.shardingsphere.mask.merge.dql;

import org.apache.shardingsphere.infra.binder.statement.dml.SelectStatementContext;
import org.apache.shardingsphere.infra.config.algorithm.AlgorithmConfiguration;
import org.apache.shardingsphere.infra.database.DefaultDatabase;
import org.apache.shardingsphere.infra.metadata.database.ShardingSphereDatabase;
import org.apache.shardingsphere.infra.metadata.database.schema.decorator.model.ShardingSphereSchema;
import org.apache.shardingsphere.mask.factory.MaskAlgorithmFactory;
import org.apache.shardingsphere.mask.rule.MaskRule;
import org.apache.shardingsphere.mask.spi.MaskAlgorithm;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class MaskAlgorithmMetaDataTest {
    
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ShardingSphereDatabase database;
    
    @Mock
    private ShardingSphereSchema schema;
    
    @Mock
    private MaskRule maskRule;
    
    @Mock
    private SelectStatementContext selectStatementContext;
    
    private MaskAlgorithm<?, ?> maskAlgorithm;
    
    @Before
    public void setUp() {
        when(database.getSchema(DefaultDatabase.LOGIC_NAME)).thenReturn(schema);
        maskAlgorithm = (MaskAlgorithm<?, ?>) MaskAlgorithmFactory.newInstance(new AlgorithmConfiguration("MD5", new Properties()));
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void assertFindMaskAlgorithm() {
        when(maskRule.findMaskAlgorithm("t_order", "id")).thenReturn(Optional.of(maskAlgorithm));
        MaskAlgorithmMetaData maskAlgorithmMetaData = new MaskAlgorithmMetaData(database, maskRule, selectStatementContext);
        Optional<MaskAlgorithm> actual = maskAlgorithmMetaData.findMaskAlgorithm("t_order", "id");
        assertTrue(actual.isPresent());
        assertThat(actual.get().getType(), is("MD5"));
    }
}
