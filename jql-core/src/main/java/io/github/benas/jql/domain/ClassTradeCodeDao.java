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
package io.github.benas.jql.domain;

import io.github.benas.jql.model.ClassTradeCode;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

public class ClassTradeCodeDao extends BaseDao {

    public ClassTradeCodeDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    public int save(ClassTradeCode classTradeCode) {
        int classTradeCodeId = getNextId("class_tradecode");
        jdbcTemplate.update("insert into class_tradecode values (?,?,?,?,?)", classTradeCodeId, classTradeCode.getCode(),
                classTradeCode.getName(),
                classTradeCode.getQualifiedName(),
                classTradeCode.getCreateTime());
        return classTradeCodeId;
    }

    public List<Map<String, Object>> generateThreeLinkRoadList() {
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from \n" +
                "    (SELECT onecode.code as code,onec.id AS oneclassid,onec.PACKAGE||'.'||onec.name AS oneclassName,onem.name AS onemethodname,\n" +
                "\t\t    twoc.id AS twoclassid, twoc.PACKAGE||'.'||twoc.name AS twoclassname, twom.name AS twomethodname,\n" +
                "\t        threec.id AS threeclassid, threec.PACKAGE||'.'||threec.name AS threeclassname, threecm.name AS threemethodname\n" +
                "\t\tFROM COMPILATION_UNIT onec\n" +
                "\t      LEFT join CLASS_TRADECODE onecode ON onecode.QUALIFIED_NAME = onec.PACKAGE||'.'||onec.name\n" +
                "\t      LEFT JOIN METHOD onem ON onec.id = onem.type_ID\n" +
                "\t\t  LEFT JOIN CHILD_METHOD twom ON onem.id = twom.PARENT_METHOD_ID\n" +
                "\t\t  LEFT JOIN COMPILATION_UNIT twoc ON twom.ORIGIN_TYPE_ID = twoc.ID\n" +
                "\t\t  LEFT JOIN METHOD threem ON twoc.id = threem.type_ID\n" +
                "\t\t  LEFT JOIN CHILD_METHOD threecm ON threem.id = threecm.PARENT_METHOD_ID\n" +
                "\t\t  LEFT JOIN COMPILATION_UNIT threec ON threecm.ORIGIN_TYPE_ID = threec.ID and twoc.id != threec.id\n" +
                "\t\twhere onecode.code NOTNULL)a  \n" +
                "\tgroup by a.onemethodname,a.oneclassName,a.twoclassname,a.twomethodname,a.threeclassname,a.threemethodname \n" +
                "\torder by a.oneclassName");
        //System.out.println("三阶组装数据:"+ maps.toString());
        return maps;
    }

    public int saveThreeLinkRoad(Map<String,Object> map) {
        int classTradeCodeId = getNextId("THREE_LINKROAD");
        jdbcTemplate.update("insert into THREE_LINKROAD values (?,?,?,?,?,?,?,?,?,?,?)", classTradeCodeId, (String)map.get("code"),
                (Integer)map.get("oneclassid"),(String)map.get("oneclassName"), (String)map.get("onemethodname"),
                (Integer)map.get("twoclassid"),(String)map.get("twoclassName"), (String)map.get("twomethodname"),
                (Integer)map.get("threeclassid"),(String)map.get("threeclassName"),(String)map.get("threemethodname")
        );
        return classTradeCodeId;
    }

}
