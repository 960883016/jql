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
public class ChildField {

    /**
     * childMethodId: 二阶方法id
     * parentMethodId: 一阶方法id
     */
    private int id, parentMethodId;

    private String name, type;

    private boolean isFinal;

    public ChildField(String name, String type, boolean isFinal, int parentMethodId) {
        this.name = name;
        this.type = type;
        this.isFinal = isFinal;
        this.parentMethodId = parentMethodId;
    }

    public ChildField(int id, String name, String type, boolean isFinal, int parentMethodId) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.isFinal = isFinal;
        this.parentMethodId = parentMethodId;
    }

    public int getId() {
        return id;
    }

    public int getParentMethodId() {
        return parentMethodId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isFinal() {
        return isFinal;
    }
}
