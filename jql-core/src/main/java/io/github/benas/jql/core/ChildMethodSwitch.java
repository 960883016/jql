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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author lan
 */
public class ChildMethodSwitch {



    public ChildMethodSwitch() { }


    /**
     * childMethod进行类型映射
     * 校验顺序：一阶方法形参 -> 一阶方法局部变量 -> 全局变量
     * Order:  Parameter   -> ChildField     -> Field
     * childMethodType: childMethod 表中的name字段
     */
    public String typeMapping(String childMethodType, String name) {
        char initial = childMethodType.charAt(0);

        // childMethodType为特殊情况下的处理逻辑
        if (childMethodType.equals("this") || childMethodType.contains("this.")) {
            return CompilationUnitIndexer.className;
        }
        if (childMethodType.contains("new ")) {
            String substr = childMethodType.substring(childMethodType.indexOf("new ") + "new ".length());
            return substr.substring(0 , substr.indexOf("("));
        }

        // 映射关系建立
        if (Character.isUpperCase(initial)) {
            // 首字符大写
            return childMethodType;
        } else if (Character.isLowerCase(initial)) {
            // 首字符为小写
            Map<String, String> parameterMap = CompilationUnitIndexer.parameterMap;
            Map<String, String> childFieldMap = CompilationUnitIndexer.childFieldMap;
            Map<String, String> fieldMap = CompilationUnitIndexer.fieldMap;
            // 数组格式处理
            if (childMethodType.contains("[") && childMethodType.contains("]")) {
                childMethodType = childMethodType.substring(0, childMethodType.indexOf("["));
            }
            // 开始映射
            if (!parameterMap.isEmpty() && parameterMap.containsKey(childMethodType)) {
                return parameterMap.get(childMethodType);
            } else if (!childFieldMap.isEmpty() && childFieldMap.containsKey(childMethodType)) {
                return childFieldMap.get(childMethodType);
            } else if (!fieldMap.isEmpty() && fieldMap.containsKey(childMethodType)) {
                return fieldMap.get(childMethodType);
            } else {
                return childMethodType;
            }
        } else if (initial == '\"') {
            // 首字符为'"'
            return "String";
        } else if ("((".equals(childMethodType.substring(0, 2))) {
            // 前两个字符为"(("，为强制转换类型
            return childMethodType.substring(2, childMethodType.indexOf(")") - 1);
        } else {
            // 其他场景
            return childMethodType;
        }
    }

    /**
     * 过滤规则表-常规基础数据
     */
    public List<String> excludeRuleTable() {
        return Arrays.asList("Boolean", "Collectors", "Date", "Double", "Exception", "File", "Float", "IOException", "Integer", "List", "Long",
                "Map", "Object", "OutputStream", "Process", "Runtime", "Session", "Set", "Stream", "String", "StringBuilder", "System", "Thread",
                "Byte", "Character", "Class", "Enum", "Error", "Short", "Random");
    }
}