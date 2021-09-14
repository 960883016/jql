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

import io.github.benas.jql.domain.ClassTradeCodeDao;
import io.github.benas.jql.model.ClassTradeCode;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author lan
 */
public class ClassTradeCodeIndexer {

    private ClassTradeCodeDao classTradeCodeDao;

    public ClassTradeCodeIndexer(ClassTradeCodeDao classTradeCodeDao) {
        this.classTradeCodeDao = classTradeCodeDao;
    }

    public void index(ClassTradeCode classTradeCode) {
        classTradeCodeDao.save(classTradeCode);
    }

    /**
     * 解析xml,类与交易映射信息
     * @param sourceCodeDirectory
     * @throws DocumentException
     */
    public void resolveClassTradeCode(File sourceCodeDirectory) throws DocumentException {
        String  pathStr= sourceCodeDirectory.toString().split("src\\\\main")[0] + "src\\main\\resources\\SYSDATA\\SERVICEREGCFG";
        //String sourceCodeDir = "D:\\zzzworkspace\\spring-boot-demo\\src\\main\\java";
        File directory = new File(pathStr);
        List<ClassTradeCode> tradeCodes = new ArrayList<>();
        if(directory.exists() && directory.isDirectory()){
            List<File> files = Arrays.asList(directory.listFiles());
            SAXReader reader = new SAXReader();
            for(File file : files){
                parseElement(reader.read(file).getRootElement(),tradeCodes);
            }
        }
        tradeCodes.forEach(b->{
            classTradeCodeDao.save(b);
        });
    }

    private void parseElement(Element element , List<ClassTradeCode> tradeCodes){
        if("section".equals(element.getName())){
            ClassTradeCode classTradeCode = new ClassTradeCode(element.attributeValue("name"),
                    element.elementText("SVC_NAM"),
                    element.elementText("CLS_NAM") + ".java",
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
            );
            tradeCodes.add(classTradeCode);
        }
        Iterator iterator = element.elementIterator();
        while (iterator.hasNext()){
            Element childEle = (Element) iterator.next();
            parseElement(childEle,tradeCodes);
        }
    }

    public void generateThreeLinkRoad(){
        //先组装查询出三阶链路数据,在插入三阶链路表
        List<Map<String, Object>> mapList = classTradeCodeDao.generateThreeLinkRoadList();
        mapList.forEach(map->{
            System.out.println("三阶链路数据:" + map.toString());
            classTradeCodeDao.saveThreeLinkRoad(map);
        });
    }


}