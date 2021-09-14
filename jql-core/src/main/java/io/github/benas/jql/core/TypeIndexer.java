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

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.*;
import io.github.benas.jql.domain.TypeDao;
import io.github.benas.jql.model.CompilationUnit;
import io.github.benas.jql.model.Type;

import java.util.EnumSet;
import java.util.Map;

import static com.github.javaparser.ast.Modifier.*;

public class TypeIndexer {

    private TypeDao typeDao;

    private BodyDeclarationIndexer bodyDeclarationIndexer;

    public TypeIndexer(TypeDao typeDao, BodyDeclarationIndexer bodyDeclarationIndexer) {
        this.typeDao = typeDao;
        this.bodyDeclarationIndexer = bodyDeclarationIndexer;
    }

    public void index(TypeDeclaration<?> type, int cuId) {
        EnumSet<Modifier> modifiers = type.getModifiers();
        boolean isInterface = type instanceof ClassOrInterfaceDeclaration && ((ClassOrInterfaceDeclaration) type).isInterface();
        boolean isAnnotation = type instanceof AnnotationDeclaration;
        boolean isEnumeration = type instanceof EnumDeclaration;
        boolean isClass = !isAnnotation && !isEnumeration && !isInterface;

        CompilationUnitIndexer.className = type.getNameAsString();
        Type t = new Type(type.getNameAsString(), type.isPublic(), type.isStatic(), modifiers.contains(FINAL), modifiers.contains(ABSTRACT), isClass, isInterface, isEnumeration, isAnnotation, cuId);
        int typeId = typeDao.save(t);

        for (BodyDeclaration member : type.getMembers()) {
            bodyDeclarationIndexer.index(member, typeId);
        }
    }
}
