// Modifications copyright (C) 2017, Baidu.com, Inc.
// Copyright 2017 The Apache Software Foundation

// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package org.apache.doris.common;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;

public class MarkedCountDownLatch extends CountDownLatch {

    private Multimap<Long, Long> marks;

    public MarkedCountDownLatch(int count) {
        super(count);
        marks = HashMultimap.create();
    }

    public void addMark(long key, long value) {
        marks.put(key, value);
    }

    public synchronized boolean markedCountDown(long key, long value) {
        if (marks.remove(key, value)) {
            super.countDown();
            return true;
        }
        return false;
    }

    public synchronized List<Entry<Long, Long>> getLeftMarks() {
        return Lists.newArrayList(marks.entries());
    }
}