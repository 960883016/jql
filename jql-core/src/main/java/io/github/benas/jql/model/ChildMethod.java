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
package io.github.benas.jql.model;

/**
 * @author lan
 */
public class ChildMethod {

    private int id;
    private String name;
    private String type;
    private String message;
    private int isExclude;
    private int isChain;
    private int isLambda;
    private int isReflection;
    private int parentMethodId;
    private int typeId;
    private int originTypeId;

    public ChildMethod(String name, String type, String message, int isExclude, int isChain, int isLambda, int isReflection, int parentMethodId, int typeId, int originTypeId) {
        this.name = name;
        this.type = type;
        this.message = message;
        this.isExclude = isExclude;
        this.isChain = isChain;
        this.isLambda = isLambda;
        this.isReflection = isReflection;
        this.parentMethodId = parentMethodId;
        this.typeId = typeId;
        this.originTypeId = originTypeId;
    }

    public ChildMethod(int id, String name, String type, String message, int isExclude, int isChain, int isLambda, int isReflection, int parentMethodId, int typeId, int originTypeId) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.message = message;
        this.isExclude = isExclude;
        this.isChain = isChain;
        this.isLambda = isLambda;
        this.isReflection = isReflection;
        this.parentMethodId = parentMethodId;
        this.typeId = typeId;
        this.originTypeId = originTypeId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() { return type; }

    public String getMessage() { return message; }

    public int getIsExclude() { return isExclude; }

    public int getIsChain() { return isChain; }

    public int getIsLambda() { return isLambda; }

    public int getIsReflection() { return isReflection; }

    public int getTypeId() {
        return typeId;
    }

    public int getOriginTypeId() { return originTypeId; }

    public int getParentMethodId() { return parentMethodId; }

}
