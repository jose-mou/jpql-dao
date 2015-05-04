package com.diwa.dao;

import com.diwa.dao.domain.Profile;
import com.diwa.dao.domain.Role;
import com.diwa.dao.domain.User;
import com.diwa.dao.shared.aggregate.Aggregate;
import com.diwa.dao.shared.criteria.Criteria;
import com.diwa.dao.shared.criteria.PersonalCriteria;
import com.diwa.dao.shared.criteria.conditional.*;
import com.diwa.dao.shared.criteria.logical.GroupLogical;
import com.diwa.dao.shared.criteria.logical.NotLogical;
import com.diwa.dao.shared.entity.DomainEntity;
import com.diwa.dao.shared.entity.FetchJoin;
import com.diwa.dao.shared.entity.JoinEntity;
import com.diwa.dao.shared.order.OrderBy;
import com.diwa.dao.shared.search.SearchInfo;
import junit.framework.TestCase;
import org.junit.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JPQLGeneratorTest extends TestCase {

    public void testUpdate () throws Exception {
        JPQLGenerator generator = JPQLGenerator.getInstance();
        Map<String, Serializable> atts = new HashMap<String, Serializable>();
        String p1 = generator.generateVarName("fieldName1", "fieldValue1").toString();
        String p2 = generator.generateVarName("fieldName2", 2).toString();
        String p3 = generator.generateVarName("fieldName3", "fieldValue3").toString();
        atts.put("fieldName1", "fieldValue1");
        atts.put("fieldName2", 2);
        SearchInfo searchInfo = new SearchInfo();
        ValueComparison condition1 = (ValueComparison) ValueComparison.eq("fieldName3", "fieldValue3");
        searchInfo.addCriteria(condition1);
        JPQLResult query = generator.update(User.class, atts, searchInfo);
        Assert.assertEquals(String.format("UPDATE entityUser e SET e.fieldName1 = :%s,e.fieldName2 = :%s WHERE e.fieldName3 = :%s",p1,p2,p3), query.getQuery());
        Assert.assertEquals(3, query.getParameters().size());
        Assert.assertEquals("fieldValue1", query.getParameters().get(p1));
        Assert.assertEquals(2, query.getParameters().get(p2));
        Assert.assertEquals("fieldValue3", query.getParameters().get(p3));
    }

    public void testCount () throws Exception {
        JPQLGenerator generator = JPQLGenerator.getInstance();
        SearchInfo search = new SearchInfo();
        FetchJoin fetchJoin = FetchJoin.leftJoinFetch("roleFieldList");
        ValueComparison condition1 = (ValueComparison) ValueComparison.eq("fieldName", "fieldValue");
        search.addCriteria(condition1);
        search.addFetch(fetchJoin);
        String parameterName1 = generator.generateVarName("fieldName", "fieldValue").toString();
        JPQLResult jpqlResult = generator.count(User.class, search);
        // Count shouldn't include FETCH
        Assert.assertEquals(String.format("SELECT COUNT(e) FROM entityUser AS e WHERE e.fieldName = :%s", parameterName1), jpqlResult.getQuery());
    }

    public void testAggregate () throws Exception {
        JPQLGenerator generator = JPQLGenerator.getInstance();
        SearchInfo searchInfo = new SearchInfo();
        ValueComparison condition1 = (ValueComparison) ValueComparison.eq("fieldName1", "fieldValue1");
        searchInfo.addCriteria(condition1);
        String p1 = generator.generateVarName("fieldName1", "fieldValue1").toString();
        JPQLResult query = generator.aggregate(User.class, Aggregate.AVG, "amount", searchInfo);
        Assert.assertEquals(String.format("SELECT AVG(DISTINCT(e.amount)) FROM entityUser AS e WHERE e.fieldName1 = :%s",p1), query.getQuery());
        Assert.assertEquals(1, query.getParameters().size());
        Assert.assertEquals("fieldValue1", query.getParameters().get(p1));
    }

    public void testFindEntityName () throws Exception {
        JPQLGenerator generator = JPQLGenerator.getInstance();
        SearchInfo search = new SearchInfo();
        JPQLResult jpqlResult = generator.find(User.class, search);
        Assert.assertEquals("SELECT DISTINCT e FROM entityUser AS e ORDER BY e.id ASC", jpqlResult.getQuery());
        Assert.assertTrue(jpqlResult.getParameters().isEmpty());
    }

    public void testFind () throws Exception {
        JPQLGenerator generator = JPQLGenerator.getInstance();
        SearchInfo search = new SearchInfo();
        JPQLResult jpqlResult = generator.find(Role.class, search);
        Assert.assertEquals("SELECT DISTINCT e FROM Role AS e ORDER BY e.id ASC", jpqlResult.getQuery());
        Assert.assertTrue(jpqlResult.getParameters().isEmpty());
    }

    public void testGenerateFromClause () throws Exception {
        JPQLGenerator generator = JPQLGenerator.getInstance();
        SearchInfo search = new SearchInfo();
        DomainEntity entity1 = new DomainEntity(User.class, "userAlias");
        DomainEntity entity2 = new DomainEntity(Role.class, "roleAlias");
        JoinEntity joinEntity1 = JoinEntity.join("userField", entity1, "joinAlias");
        FetchJoin fetchJoin = FetchJoin.leftJoinFetch("roleFieldList", entity2);
        search.addEntity(entity1);
        search.addEntity(entity2);
        search.addEntity(joinEntity1);
        search.addFetch(fetchJoin);
        StringBuilder jpqlResult = generator.generateFromClause(Profile.class, search);
        Assert.assertEquals(" FROM Profile AS e, User userAlias, Role roleAlias INNER JOIN userAlias.userField joinAlias LEFT JOIN FETCH roleAlias.roleFieldList ", jpqlResult.toString());
    }

    public void testGenerateDomainClause () throws Exception {
        JPQLGenerator generator = JPQLGenerator.getInstance();
        DomainEntity entity = new DomainEntity(User.class, "alias");
        StringBuilder query =new StringBuilder();
        generator.generateDomainClause(entity, query);
        Assert.assertEquals(", User alias", query.toString());
    }

    public void testGenerateJoinClauseFetch () throws Exception {
        JPQLGenerator generator = JPQLGenerator.getInstance();
        StringBuilder query =new StringBuilder();
        FetchJoin join = FetchJoin.joinFetch("roles");
        generator.generateJoinClause(query,join);
        Assert.assertEquals(" JOIN FETCH e.roles ", query.toString());
    }

    public void testGenerateJoinClauseEntity () throws Exception {
        JPQLGenerator generator = JPQLGenerator.getInstance();
        StringBuilder query =new StringBuilder();
        JoinEntity join = JoinEntity.join("roles", "alias");
        generator.generateJoinClause(query,join);
        Assert.assertEquals(" INNER JOIN e.roles alias", query.toString());
    }

    public void testAppendWhereClause () throws Exception {
        JPQLGenerator generator = JPQLGenerator.getInstance();
        StringBuilder query =new StringBuilder();
        SearchInfo searchInfo = new SearchInfo();
        String fieldName1 = "fieldName1";
        String fieldName2 = "fieldName2";
        String value1 = "fieldValue1";
        String value2 = "fieldValue2";
        ValueComparison condition1 = (ValueComparison) ValueComparison.eq(fieldName1, value1);
        ValueComparison condition2 = (ValueComparison) ValueComparison.eq(fieldName2, value2);
        searchInfo.addCriteria(condition1);
        searchInfo.addCriteria(condition2);
        String parameterName1 = generator.generateVarName(fieldName1, value1).toString();
        String parameterName2 = generator.generateVarName(fieldName2, value2).toString();
        Map<String, Serializable> parameters = generator.appendWhereClause(query, searchInfo);
        Assert.assertEquals(String.format(" WHERE e.fieldName1 = :%s AND e.fieldName2 = :%s",parameterName1, parameterName2), query.toString());
        Assert.assertEquals(2, parameters.size());
        Assert.assertEquals(value1, parameters.get(parameterName1));
        Assert.assertEquals(value2, parameters.get(parameterName2));
    }

    public void testGenerateOrderClause () throws Exception {
        JPQLGenerator generator = JPQLGenerator.getInstance();
        String fieldName1 = "fieldName1";
        String fieldName2 = "fieldName2";
        DomainEntity entity2 = new DomainEntity(User.class, "user");
        SearchInfo searchInfo = new SearchInfo();
        searchInfo.addOrder(OrderBy.asc(fieldName1));
        searchInfo.addOrder(OrderBy.desc(fieldName2, entity2));
        StringBuilder query = generator.generateOrderClause(searchInfo);
        Assert.assertEquals(" ORDER BY e.fieldName1 ASC, user.fieldName2 DESC, e.id ASC", query.toString());
    }

    public void testAddOrderByIdClause () throws Exception {
        JPQLGenerator generator = JPQLGenerator.getInstance();
        StringBuilder builder =new StringBuilder();
        generator.addOrderByIdClause(builder);
        Assert.assertEquals("e.id ASC", builder.toString());
    }

    public void testGenerateCriteriaClause () throws Exception {
        JPQLGenerator generator = JPQLGenerator.getInstance();
        String fieldName = "fieldName";
        ValueComparison condition = (ValueComparison) ValueComparison.eq(fieldName, "fieldValue");
        NotLogical expression = new NotLogical(condition);
        StringBuilder query = generator.generateCriteriaClause(expression, new HashMap<String, Serializable>());
        Pattern pattern = Pattern.compile("NOT \\(e.fieldName = :fieldName_(\\d)+\\)");
        Matcher matcher = pattern.matcher(query.toString());
        Assert.assertTrue(matcher.matches());
    }
    public void testGenerateCriteriaClause2 () throws Exception {
        JPQLGenerator generator = JPQLGenerator.getInstance();
        String property = "propertyName";
        String value = "propertyValue";
        HashMap<String, Serializable> parameters = new HashMap<String, Serializable>();
        String parameterName = generator.generateVarName(property, value).toString();
        ValueComparison conditional = (ValueComparison) ValueComparison.ne(property, value);
        StringBuilder query = generator.generateCriteriaClause(conditional, parameters);
        Assert.assertEquals(String.format("e.propertyName <> :%s", parameterName), query.toString());
        Assert.assertEquals(1, parameters.entrySet().size());
        Assert.assertEquals(value, parameters.get(parameterName));
    }

    public void testGenerateConditionalClauseFieldComparison () throws Exception {
        JPQLGenerator generator = JPQLGenerator.getInstance();
        String field1 = "filed1Name";
        String field2 = "field2Name";
        DomainEntity entity1 = new DomainEntity(User.class, "user");
        DomainEntity entity2 = new DomainEntity(Role.class , "role");
        FieldComparison conditional = (FieldComparison) FieldComparison.le(entity1, field1, entity2, field2);
        HashMap<String, Serializable> parameters = new HashMap<String, Serializable>();
        StringBuilder query = generator.generateConditionalClause(conditional, parameters);
        Assert.assertEquals("user.filed1Name <= role.field2Name", query.toString());
        Assert.assertEquals(0, parameters.entrySet().size());
    }

    public void testGenerateConditionalClauseIn () throws Exception {
        JPQLGenerator generator = JPQLGenerator.getInstance();
        String property = "propertyName";
        List<Integer> propertyValue = new ArrayList<Integer>();
        propertyValue.add(1);
        propertyValue.add(9);
        DomainEntity entity = new DomainEntity(User.class, "u");
        InConditional conditional =  new InConditional(property, entity, propertyValue);
        HashMap<String, Serializable> parameters = new HashMap<String, Serializable>();
        String parameterName1 = generator.generateVarName(property, propertyValue).toString();
        StringBuilder query = generator.generateConditionalClause(conditional, parameters);
        Assert.assertEquals(String.format("u.propertyName IN (:%s)", parameterName1), query.toString());
        Assert.assertEquals(1, parameters.entrySet().size());
        Assert.assertEquals(propertyValue, parameters.get(parameterName1));
    }

    public void testGenerateConditionalClauseInJustOne () throws Exception {
        JPQLGenerator generator = JPQLGenerator.getInstance();
        String property = "propertyName";
        List<Integer> propertyValue = new ArrayList<Integer>();
        propertyValue.add(9);
        DomainEntity entity = new DomainEntity(User.class, "u");
        InConditional conditional =  new InConditional(property, entity, propertyValue);
        HashMap<String, Serializable> parameters = new HashMap<String, Serializable>();
        String parameterName1 = generator.generateVarName(property, propertyValue).toString();
        StringBuilder query = generator.generateConditionalClause(conditional, parameters);
        Assert.assertEquals(String.format("u.propertyName = :%s", parameterName1), query.toString());
        Assert.assertEquals(1, parameters.entrySet().size());
        Assert.assertEquals(9, parameters.get(parameterName1));
    }

    public void testGenerateConditionalClauseBetween () throws Exception {
        JPQLGenerator generator = JPQLGenerator.getInstance();
        String property = "propertyName";
        Integer value1 = 5;
        Integer value2 = 50;
        DomainEntity entity = new DomainEntity(User.class, "k");
        HashMap<String, Serializable> parameters = new HashMap<String, Serializable>();
        String parameterName1 = generator.generateVarName(property, value1).toString();
        String parameterName2 = generator.generateVarName(property, value2).toString();
        BetweenConditional conditional = new BetweenConditional(property, entity, value1, value2);
        StringBuilder query = generator.generateConditionalClause(conditional, parameters);
        Assert.assertEquals(String.format("k.propertyName BETWEEN :%s AND :%s", parameterName1, parameterName2), query.toString());
        Assert.assertEquals(2, parameters.entrySet().size());
        Assert.assertEquals(value1, parameters.get(parameterName1));
        Assert.assertEquals(value2, parameters.get(parameterName2));
    }

    public void testGenerateConditionalClauseComparisonEntity () throws Exception {
        JPQLGenerator generator = JPQLGenerator.getInstance();
        String property = "propertyName";
        String value = "propertyValue";
        DomainEntity entity = new DomainEntity(User.class, "k");
        HashMap<String, Serializable> parameters = new HashMap<String, Serializable>();
        String parameterName = generator.generateVarName(property, value).toString();
        ValueComparison conditional = (ValueComparison) ValueComparison.eq(property, value, entity);
        StringBuilder query = generator.generateConditionalClause(conditional, parameters);
        Assert.assertEquals(String.format("k.propertyName = :%s", parameterName), query.toString());
        Assert.assertEquals(1, parameters.entrySet().size());
        Assert.assertEquals(value, parameters.get(parameterName));
    }

    public void testGenerateConditionalClauseComparison () throws Exception {
        JPQLGenerator generator = JPQLGenerator.getInstance();
        String property = "propertyName";
        String value = "propertyValue";
        HashMap<String, Serializable> parameters = new HashMap<String, Serializable>();
        String parameterName = generator.generateVarName(property, value).toString();
        ValueComparison conditional = (ValueComparison) ValueComparison.ne(property, value);
        StringBuilder query = generator.generateConditionalClause(conditional, parameters);
        Assert.assertEquals(String.format("e.propertyName <> :%s", parameterName), query.toString());
        Assert.assertEquals(1, parameters.entrySet().size());
        Assert.assertEquals(value, parameters.get(parameterName));
    }

    public void testGenerateConditionalClauseLikeCaseSensitive () throws Exception {
        JPQLGenerator generator = JPQLGenerator.getInstance();
        String property = "propertyName";
        String value = "propertyValue";
        HashMap<String, Serializable> parameters = new HashMap<String, Serializable>();
        String parameterName = generator.generateVarName(property, value).toString();
        LikeConditional conditional = new LikeConditional(property, value);
        conditional.setCaseSensitive(true);
        StringBuilder query = generator.generateConditionalClause(conditional, parameters);
        Assert.assertEquals(String.format("e.propertyName LIKE :%s", parameterName), query.toString());
        Assert.assertEquals(1, parameters.entrySet().size());
        Assert.assertEquals("%propertyValue%", parameters.get(parameterName));
    }

    public void testGenerateConditionalClauseLike () throws Exception {
        JPQLGenerator generator = JPQLGenerator.getInstance();
        String property = "propertyName";
        String value = "propertyValue";
        HashMap<String, Serializable> parameters = new HashMap<String, Serializable>();
        String parameterName = generator.generateVarName(property, value).toString();
        LikeConditional conditional = new LikeConditional(property, value);
        StringBuilder query = generator.generateConditionalClause(conditional, parameters);
        Assert.assertEquals(String.format("UPPER(e.propertyName) LIKE :%s", parameterName), query.toString());
        Assert.assertEquals(1, parameters.entrySet().size());
        Assert.assertEquals("%PROPERTYVALUE%", parameters.get(parameterName));
    }

    public void testGeneratePersonalCriteriaClause () throws Exception {
        JPQLGenerator generator = JPQLGenerator.getInstance();
        DomainEntity entity = new DomainEntity(User.class, "k");
        PersonalCriteria criteria = new PersonalCriteria("replaceProperty > :parameterValue AND j.field2 = replaceProperty2");
        criteria.addProperty("replaceProperty", "field1");
        criteria.addProperty("replaceProperty2", "field2", entity);
        criteria.addParameter("parameterValue", 44);
        HashMap<String, Serializable> parameters = new HashMap<String, Serializable>();
        StringBuilder query = generator.generatePersonalCriteriaClause(criteria, parameters);
        Assert.assertEquals("e.field1 > :parameterValue AND j.field2 = k.field2", query.toString());
        Assert.assertEquals(1, parameters.entrySet().size());
        Assert.assertEquals(44, parameters.get("parameterValue"));
    }

    public void testGenerateLogicalClauseAnd() throws Exception {
        JPQLGenerator generator = JPQLGenerator.getInstance();
        String fieldName1 = "fieldName1";
        String fieldName2 = "fieldName2";
        String value1 = "fieldValue1";
        String value2 = "fieldValue2";
        ValueComparison condition1 = (ValueComparison) ValueComparison.eq(fieldName1, value1);
        ValueComparison condition2 = (ValueComparison) ValueComparison.eq(fieldName2, value2);
        String parameterName1 = generator.generateVarName(fieldName1, value1).toString();
        String parameterName2 = generator.generateVarName(fieldName2, value2).toString();
        ArrayList<Criteria> criterias = new ArrayList<Criteria>();
        criterias.add(condition1);
        criterias.add(condition2);
        GroupLogical expression = GroupLogical.and(criterias);
        HashMap<String, Serializable> parameters = new HashMap<String, Serializable>();
        StringBuilder query = generator.generateLogicalClause(expression, parameters);
        Assert.assertEquals(String.format("(e.fieldName1 = :%s AND e.fieldName2 = :%s)",parameterName1, parameterName2), query.toString());
        Assert.assertEquals(2, parameters.entrySet().size());
        Assert.assertEquals(value1, parameters.get(parameterName1));
        Assert.assertEquals(value2, parameters.get(parameterName2));
    }

    public void testGenerateLogicalClauseOr() throws Exception {
        JPQLGenerator generator = JPQLGenerator.getInstance();
        String fieldName1 = "fieldName1";
        String fieldName2 = "fieldName2";
        String value1 = "fieldValue1";
        String value2 = "fieldValue2";
        ValueComparison condition1 = (ValueComparison) ValueComparison.eq(fieldName1, value1);
        ValueComparison condition2 = (ValueComparison) ValueComparison.eq(fieldName2, value2);
        String parameterName1 = generator.generateVarName(fieldName1, value1).toString();
        String parameterName2 = generator.generateVarName(fieldName2, value2).toString();
        ArrayList<Criteria> criterias = new ArrayList<Criteria>();
        criterias.add(condition1);
        criterias.add(condition2);
        GroupLogical expression = GroupLogical.or(criterias);
        HashMap<String, Serializable> parameters = new HashMap<String, Serializable>();
        StringBuilder query = generator.generateLogicalClause(expression, parameters);
        Assert.assertEquals(String.format("(e.fieldName1 = :%s OR e.fieldName2 = :%s)",parameterName1, parameterName2), query.toString());
        Assert.assertEquals(2, parameters.entrySet().size());
        Assert.assertEquals(value1, parameters.get(parameterName1));
        Assert.assertEquals(value2, parameters.get(parameterName2));
    }

    public void testGenerateLogicalClauseNot() throws Exception {
        JPQLGenerator generator = JPQLGenerator.getInstance();
        String fieldName = "fieldName";
        ValueComparison condition = (ValueComparison) ValueComparison.eq(fieldName, "fieldValue");
        NotLogical expression = new NotLogical(condition);
        StringBuilder query = generator.generateLogicalClause(expression, new HashMap<String, Serializable>());
        Pattern pattern = Pattern.compile("NOT \\(e.fieldName = :fieldName_(\\d)+\\)");
        Matcher matcher = pattern.matcher(query.toString());
        Assert.assertTrue(matcher.matches());
    }

    public void testGenerateVarNameNullValue () throws Exception {
        JPQLGenerator generator = JPQLGenerator.getInstance();
        String fieldName = "fieldName";
        StringBuilder varName = generator.generateVarName(fieldName, null);
        Assert.assertEquals(fieldName + "_", varName.toString());
    }

    public void testGenerateVarName () throws Exception {
        JPQLGenerator generator = JPQLGenerator.getInstance();
        String value = "fieldValue";
        StringBuilder varName = generator.generateVarName("entity.fieldName", value);
        Assert.assertEquals("entity_fieldName_" + value.hashCode(), varName.toString());
    }
}