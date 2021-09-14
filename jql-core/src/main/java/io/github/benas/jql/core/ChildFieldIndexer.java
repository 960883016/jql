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

import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.CatchClause;
import io.github.benas.jql.domain.ChildFieldDao;
import io.github.benas.jql.model.ChildField;

import java.util.List;

/**
 * @author lan
 */
public class ChildFieldIndexer {

    private ChildFieldDao childFieldDao;

    public ChildFieldIndexer(ChildFieldDao childFieldDao) {
        this.childFieldDao = childFieldDao;
    }

    /**
     * childFiled逻辑修改
     */
    public void index(VariableDeclarationExpr variableDeclarationExpr, int parentMethodId) {
        List<VariableDeclarator> variables = variableDeclarationExpr.getVariables();
        if (!variables.isEmpty()) {
            for (VariableDeclarator variable : variables) {
                String name = variable.getNameAsString();
                CompilationUnitIndexer.childFieldMap.put(name, variableDeclarationExpr.getElementType().asString());
                childFieldDao.save(new ChildField(name, variableDeclarationExpr.getElementType().asString(),
                        variableDeclarationExpr.isFinal(), parentMethodId));
            }
        } else {
            System.out.println("[INFO]:There is no childField in " + variableDeclarationExpr.toString());
        }
    }

    public void index(CatchClause catchClause) {
        Parameter parameter = catchClause.getParameter();
        CompilationUnitIndexer.childFieldMap.put(parameter.getNameAsString(), parameter.getType().asString());
    }
}
