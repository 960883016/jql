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

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import io.github.benas.jql.domain.ExtendsDao;
import io.github.benas.jql.domain.TypeDao;
import io.github.benas.jql.model.Extends;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ExtendsRelationCalculator {

    private TypeDao typeDao;

    private ExtendsDao extendsDao;

    public ExtendsRelationCalculator(TypeDao typeDao, ExtendsDao extendsDao) {
        this.typeDao = typeDao;
        this.extendsDao = extendsDao;
    }

    public void calculate(ClassOrInterfaceDeclaration classOrInterfaceDeclaration, CompilationUnit compilationUnit) {
        List<ClassOrInterfaceType> extendedTypes = classOrInterfaceDeclaration.getExtendedTypes();
        for (ClassOrInterfaceType extendedType : extendedTypes) {
            String extendedTypeName = extendedType.getNameAsString();
            // 类与其继承类在同一package下
            String extendedTypePackageName = extendedType
                    .findCompilationUnit()
                    .flatMap(CompilationUnit::getPackageDeclaration)
                    .flatMap(pkg -> Optional.of(pkg.getNameAsString())).orElse("???");
            // 不在同一package下（不考虑String、Exception等原生类的继承）
            String extendedTypePackageName2 = extendedType
                    .findRootNode()
                    .findAll(ImportDeclaration.class)
                    .stream()
                    .filter(f -> (f.getNameAsString().split("\\.")[f.getNameAsString().split("\\.").length - 1]).equals(extendedTypeName))
                    .map(m -> m.getNameAsString().substring(0, m.getNameAsString().lastIndexOf(".")))
                    .collect(Collectors.joining());

            if (typeDao.exist(extendedTypeName, extendedTypePackageName)) { // JDK interfaces are not indexed
                int extendedInterfaceId = typeDao.getId(extendedTypeName, extendedTypePackageName);
                int interfaceId = typeDao.getId(classOrInterfaceDeclaration.getNameAsString(), compilationUnit.getPackageDeclaration().get().getNameAsString());
                extendsDao.save(new Extends(interfaceId, extendedInterfaceId));
            } else if (typeDao.exist(extendedTypeName, extendedTypePackageName2)) {
                int extendedInterfaceId = typeDao.getId(extendedTypeName, extendedTypePackageName2);
                int interfaceId = typeDao.getId(classOrInterfaceDeclaration.getNameAsString(), compilationUnit.getPackageDeclaration().get().getNameAsString());
                extendsDao.save(new Extends(interfaceId, extendedInterfaceId));
            }
        }
    }
}
