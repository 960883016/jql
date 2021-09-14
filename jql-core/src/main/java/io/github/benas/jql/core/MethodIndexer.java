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

import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.VoidType;
import io.github.benas.jql.domain.MethodDao;
import io.github.benas.jql.model.Method;

import java.util.ArrayList;
import java.util.List;

import static java.lang.reflect.Modifier.*;

public class MethodIndexer {

    private MethodDao methodDao;
    private ParameterIndexer parameterIndexer;
    private ChildMethodIndexer childMethodIndexer;
    private ChildFieldIndexer childFieldIndexer;

    public MethodIndexer(MethodDao methodDao, ParameterIndexer parameterIndexer, ChildMethodIndexer childMethodIndexer, ChildFieldIndexer childFieldIndexer) {
        this.methodDao = methodDao;
        this.parameterIndexer = parameterIndexer;
        this.childMethodIndexer = childMethodIndexer;
        this.childFieldIndexer = childFieldIndexer;
    }

    public void index(MethodDeclaration methodDeclaration, int typeId) {
        List<Parameter> parameters = methodDeclaration.getParameters();
        String name = methodDeclaration.getNameAsString();
        String returnType = methodDeclaration.getType().asString();

        int methodId = methodDao.save(new Method(name, returnType,
                methodDeclaration.isPublic(), methodDeclaration.isStatic(), methodDeclaration.isFinal(), methodDeclaration.isAbstract(), false, typeId));
        for (Parameter parameter : parameters) {
            parameterIndexer.index(parameter, methodId);
        }

        // 增加childField、childMethod(childParameter)判断
        List<MethodCallExpr> methodCallExprList = methodDeclaration.findAll(MethodCallExpr.class);
        List<VariableDeclarationExpr> variableDeclarationExprList = methodDeclaration.findAll(VariableDeclarationExpr.class);
        List<CatchClause> catchClauseList = methodDeclaration.findAll(CatchClause.class);

        // catch中的形参获取
        if (!catchClauseList.isEmpty()) {
            for (CatchClause catchClause : catchClauseList) {
                childFieldIndexer.index(catchClause);
            }
        }
        // 二阶局部变量获取
        if (!variableDeclarationExprList.isEmpty()) {
            for (VariableDeclarationExpr variableDeclarationExpr : variableDeclarationExprList) {
                childFieldIndexer.index(variableDeclarationExpr, methodId);
            }
        }

        // 二阶方法获取
        if (!methodCallExprList.isEmpty()) {
            // lambda方法获取，标记，统计完成后移除
            List<MethodCallExpr> methodCallExprListLambda = new ArrayList<MethodCallExpr>();
            for (MethodCallExpr methodCallExpr : methodCallExprList) {
                List<LambdaExpr> lambdaExprList = methodCallExpr.findAll(LambdaExpr.class);
                if (!lambdaExprList.isEmpty()) {
                    for (LambdaExpr lambdaExpr : lambdaExprList) {
                        List<MethodCallExpr> methodCallExprListLambda2 = lambdaExpr.findAll(MethodCallExpr.class);
                        if (!methodCallExprListLambda2.isEmpty()) {
                            for (MethodCallExpr methodCallExprLambda : methodCallExprListLambda2) {
                                childMethodIndexer.index(methodCallExprLambda, methodId, typeId, 1);
                                methodCallExprListLambda.add(methodCallExprLambda);
                            }
                        }
                    }
                }
            }
            methodCallExprList.removeAll(methodCallExprListLambda);
            // 剩余方法计算
            for (MethodCallExpr methodCallExpr : methodCallExprList) {
                childMethodIndexer.index(methodCallExpr, methodId, typeId, 0);
            }
        }
    }
}
