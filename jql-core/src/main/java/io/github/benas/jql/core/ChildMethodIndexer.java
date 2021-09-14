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

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import io.github.benas.jql.domain.ChildMethodDao;
import io.github.benas.jql.model.ChildMethod;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @author lan
 */
public class ChildMethodIndexer {

    private ChildMethodDao childMethodDao;
    private ChildParameterIndexer childParameterIndexer;

    public ChildMethodIndexer(ChildMethodDao childMethodDao, ChildParameterIndexer childParameterIndexer) {
        this.childMethodDao = childMethodDao;
        this.childParameterIndexer = childParameterIndexer;
    }

    /**
     *
     * @param methodCallExpr 方法对象
     * @param parentMethodId 二阶方法所属的一阶方法的ID
     * @param typeId 二阶方法所属的当前类ID
     * @param isLambda 0:属于lambda表达式中的方法 1:普通方法
     */
    public void index(MethodCallExpr methodCallExpr, int parentMethodId, int typeId, int isLambda) {
        NodeList<Expression> childParameters = methodCallExpr.getArguments();
        String name = methodCallExpr.getNameAsString();
        String message = scopeToString(methodCallExpr, 0);
        String type = null;
        int isChain = chainMethodJudge(message);
        int isReflection = reflectionMethodJduge(message);
        // originTypeId字段预留，在Calculator阶段进行更新
        int originTypeId = 0;
        int isExclude = 0;
        if (isChain == 1 || isReflection == 1) {
            type = message;
            isExclude = 1;
        } else {
            type = scopeToString(methodCallExpr, 1);
            isExclude = excludeMethodJudge(type);
        }
        int childMethodId = childMethodDao.save(new ChildMethod(name, type, message, isExclude, isChain, isLambda, isReflection, parentMethodId, typeId, originTypeId));
        for (Expression childParameter : childParameters) {
            childParameterIndexer.index(childParameter, childMethodId);
        }
    }

    /**
     * 判断childMethod的类型
     * 如果为当前类方法调用，输出当前类名 或 Optional.empty
     * 否则，输出正常解析结果
     * mode:0 输出message。未转化的原始数据
     * mode:1 转化后的type。根据typeMapping方法计算后的真实返回类型
     */
    public String scopeToString(MethodCallExpr methodCallExpr, int mode) {
        Optional<Expression> scope = methodCallExpr.getScope();
        if (scope.isPresent()) {
            switch (mode) {
                case 0 : return scope.get().toString();
                case 1 : {
                    ChildMethodSwitch childMethodSwitch = new ChildMethodSwitch();
                    return childMethodSwitch.typeMapping(scope.get().toString(), methodCallExpr.getNameAsString());
                }
                default: return "Optional.empty";
            }
        } else {
            // Optional<ClassOrInterfaceDeclaration> className = methodCallExpr.findRootNode().findFirst(ClassOrInterfaceDeclaration.class); // 另一种获取className的方案
            String className = CompilationUnitIndexer.className;
            if (!className.isEmpty()) {
                return className;
            } else {
                return "Optional.empty";
            }
        }
    }

    /**
     * exclude判断
     */
    public int excludeMethodJudge(String type) {
        // 数据降噪，包含<>和.
        if (type.contains("<") && type.contains(">")) {
            type = type.substring(0, type.indexOf("<"));
        }
        if (type.contains(".")) {
            type = type.substring(0, type.indexOf("."));
        }
        ChildMethodSwitch childMethodSwitch = new ChildMethodSwitch();
        // 过滤规则表中的类型 || 过滤全小写内容
        if (childMethodSwitch.excludeRuleTable().contains(type) || type.toLowerCase().equals(type)) {
            return 1;
        } else {
            return 0;
        }
    }
    /**
     * chain判断
     */
    public int chainMethodJudge(String message) {
        // eg. ast.getNameAsString().subString(0, 2)
        String regular = "^[\\w\\[\\]]*?\\.\\w*?\\(";
        // eg. ast.CHAIN.getNameString().subString(0, 2)
        String regular2 = "^[\\w\\[\\]]*?\\.\\w*?\\.\\w*?\\(";
        if (Pattern.compile(regular).matcher(message).find() || Pattern.compile(regular2).matcher(message).find()) {
            return 1;
        } else {
            return 0;
        }
    }
    /**
     * reflection判断
     */
    public int reflectionMethodJduge(String message) {
        String regular = "^\\w*?\\.class";
        if (Pattern.compile(regular).matcher(message).find()) {
            return 1;
        } else {
            return 0;
        }
    }
}