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

import io.github.benas.jql.model.ChildMethod;
import io.github.benas.jql.model.Parameter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author lan
 */
public class ChildMethodDao extends BaseDao {

    public ChildMethodDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    public int save(ChildMethod childMethod) {
        int id = getNextId("CHILD_METHOD");
        jdbcTemplate.update("insert into CHILD_METHOD values (?,?,?,?,?,?,?,?,?,?,?)", id,
                childMethod.getName(),
                childMethod.getType(),
                childMethod.getMessage(),
                childMethod.getIsExclude(),
                childMethod.getIsChain(),
                childMethod.getIsLambda(),
                childMethod.getIsReflection(),
                childMethod.getParentMethodId(),
                childMethod.getTypeId(),
                childMethod.getOriginTypeId());
        return id;
    }

    public List<String> getAllTypes() {
        return jdbcTemplate.queryForList("select c.type from CHILD_METHOD c where c.IS_EXCLUDE = 0", String.class);
    }

    public void update(String name, int id) {
        jdbcTemplate.update("update CHILD_METHOD set ORIGIN_TYPE_ID = ? where TYPE = ? and IS_EXCLUDE = 0", id, name);
    }

}
