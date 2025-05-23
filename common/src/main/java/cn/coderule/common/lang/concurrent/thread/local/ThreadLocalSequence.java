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

package cn.coderule.common.lang.concurrent.thread.local;

import java.util.Random;

public class ThreadLocalSequence {
    private final static int POSITIVE_MASK = 0x7FFFFFFF;

    private final ThreadLocal<Integer> index;
    private final Random random;

    public ThreadLocalSequence() {
        this.random = new Random();
        this.index = ThreadLocal.withInitial(() -> {
            int index = this.random.nextInt();
           return index & POSITIVE_MASK;
        });
    }

    public int incrementAndGet() {
        Integer index = this.index.get();
        if (null == index) {
            index = random.nextInt();
        }

        index = index & POSITIVE_MASK;
        this.index.set(++index);
        return index;
    }

    public void reset() {
        int index = Math.abs(random.nextInt(Integer.MAX_VALUE));
        this.index.set(index);
    }

    @Override
    public String toString() {
        return "ThreadLocalIndex{" +
            "threadLocalIndex=" + index.get() +
            '}';
    }
}
