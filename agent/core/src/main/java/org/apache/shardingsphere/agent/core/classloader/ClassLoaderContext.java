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

package org.apache.shardingsphere.agent.core.classloader;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.shardingsphere.agent.core.plugin.PluginJar;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class loader context.
 */
@RequiredArgsConstructor
@Getter
public final class ClassLoaderContext {
    
    private static final Map<ClassLoader, AgentClassLoader> AGENT_CLASS_LOADERS = new ConcurrentHashMap<>();
    
    private final ClassLoader appClassLoader;
    
    private final Collection<PluginJar> pluginJars;
    
    /**
     * Get agent class loader.
     *
     * @return agent class loader
     */
    public AgentClassLoader getAgentClassLoader() {
        return AGENT_CLASS_LOADERS.computeIfAbsent(appClassLoader, key -> new AgentClassLoader(key, pluginJars));
    }
}
