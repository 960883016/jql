/**
 * The MIT License
 *
 *   Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */
package io.github.benas.jql.core;

import io.github.benas.jql.domain.ChildMethodDao;
import io.github.benas.jql.domain.TypeDao;

import java.util.*;

/**
 * @author lan
 */
public class ChildMethodRelationCalculator {

    private TypeDao typeDao;
    private ChildMethodDao childMethodDao;

    public ChildMethodRelationCalculator(TypeDao typeDao, ChildMethodDao childMethodDao) {
        this.typeDao = typeDao;
        this.childMethodDao = childMethodDao;
    }

    /**
     * 粗略解析模式：默认当前项目中不存在两个类的类名完全一致的情况
     */
    public void calculate() {

        // 第一步：取数
        // 第二步：多表查询数据是否存在
        // 第三步：更新数据库表CHILD_METHOD的ORIGIN_TYPE_ID字段
        // 当前项目所有存在的类名集合
        List<String> classNames = typeDao.getAllNamesList();
        // 当前项目中所有二阶方法映射的类名集合，然后去重
        Set<String> typeNames = new HashSet(childMethodDao.getAllTypes());
        for (String name : typeNames) {
            // 数据降噪，包含<>和.
            if (name.contains("<") && name.contains(">")) {
                name = name.substring(0, name.indexOf("<"));
            }
            if (name.contains(".")) {
                name = name.substring(0, name.indexOf("."));
            }
            if (classNames.contains(name)) {
                childMethodDao.update(name, typeDao.getId(name));
            }
        }
    }
}

