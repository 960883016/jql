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

import io.github.benas.jql.domain.ParameterDao;

import java.util.HashMap;
import java.util.Map;

public class ParameterIndexer {

    private ParameterDao parameterDao;

    public ParameterIndexer(ParameterDao parameterDao) {
        this.parameterDao = parameterDao;
    }

    public void index(com.github.javaparser.ast.body.Parameter parameter, int methodId) {
        io.github.benas.jql.model.Parameter p = new io.github.benas.jql.model.Parameter(parameter.getNameAsString(), parameter.getType().asString(), methodId);
        //parameterMap = new HashMap<String, String>(10);
        CompilationUnitIndexer.parameterMap.put(parameter.getNameAsString(), parameter.getType().asString());
        parameterDao.save(p);
    }
}
