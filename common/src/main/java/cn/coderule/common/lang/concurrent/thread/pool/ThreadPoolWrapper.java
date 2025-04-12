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

package cn.coderule.common.lang.concurrent.thread.pool;

import cn.coderule.common.lang.concurrent.thread.monitor.ThreadPoolMonitor;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThreadPoolWrapper {
    private String name;
    private ThreadPoolExecutor executor;
    private List<ThreadPoolMonitor> monitors;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ThreadPoolWrapper wrapper = (ThreadPoolWrapper) o;
        return Objects.equal(name, wrapper.name) && Objects.equal(executor, wrapper.executor) && Objects.equal(monitors, wrapper.monitors);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, executor, monitors);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("name", name)
            .add("threadPoolExecutor", executor)
            .add("statusPrinters", monitors)
            .toString();
    }
}
