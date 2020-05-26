package com.learn;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryRuleSqlBuilder {
    private int CURR_INDEX = 0;
    private List<String> properties;
    private List<Object> values;
    private List<Order> orders;
    private String whereSql = "";
    private String orderSql = "";
    private Object[] valueArr = new Object[]{};
    private Map<Object, Object> valueMap = new HashMap<Object, Object>();

    /**
     * 获得查询条件
     * @return
     */
    public String getWhereSql(){
        return this.whereSql;
    }

    public String getOrderSql(){
        return this.orderSql;
    }

    public Object[] getValues(){
        return this.valueArr;
    }

    public Map<Object, Object> getvalueMap(){
        return this.valueMap;
    }

    public QueryRuleSqlBuilder(QueryRule queryRule){
        CURR_INDEX = 0;
        properties = new ArrayList<String>();
        values = new ArrayList<Object>();
        orders = new ArrayList<Order>();
        for (QueryRule.Rule rule:queryRule.getRuleList()
             ) {
            switch (rule.getType()){
                case QueryRule.BETWEEN:
                    processBetween(rule);
                    break;
                case QueryRule.EQ:
                    processEqual(rule);
                    break;
                case QueryRule.LIKE:
                    processLike(rule);
                    break;
                case QueryRule.NOTEQ:
                    processNotEqual(rule);
                    break;
                case QueryRule.GT:
                    processGreaterThen(rule);
                    break;
                case QueryRule.GE:
                    processGreaterEqual(rule);
                    break;
                case QueryRule.LT:
                    processLessThen(rule);
                    break;
                case QueryRule.LE:
                    processLessEqual(rule);
                    break;
                case QueryRule.IN:
                    processIN(rule);
                    break;
                case QueryRule.NOTIN:
                    processNotIn(rule);
                    break;
                case QueryRule.ISNULL:
                    processIsNull(rule);
                    break;
                case QueryRule.ISNOTNULL:
                    processIsNotNull(rule);
                    break;
                case QueryRule.ISEMPYT:
                    processIsEmpty(rule);
                    break;
                case QueryRule.ISNOTEMPTY:
                    processIsNotEmpty(rule);
                    break;
                case QueryRule.ASC_ORDER:
                    processOrder(rule);
                    break;
                case QueryRule.DESC_ORDER:
                    processOrder(rule);
                    break;
                default:
                    throw new IllegalArgumentException("type " + rule.getType() + "not supported.");
            }
        }
        appendWhereSql();
        appendOrderSql();
        appendValues();
    }

    protected String removeOrders(String sql){
        Pattern p = Pattern.compile("order\\s*by[\\w|\\w|\\s|\\s]*", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(sql);
        StringBuffer sb = new StringBuffer();
        while(m.find()){
            m.appendReplacement(sb, "");
        }
        m.appendTail(sb);
        return sb.toString();
    }

    protected String removeSelect(String sql){
        if(sql.toLowerCase().matches("from\\s+")){
            int beginPos = sql.toLowerCase().indexOf("from");
            return sql.substring(beginPos);
        }else {
            return sql;
        }
    }

    private void processBetween(QueryRule.Rule rule){
        if((ArrayUtils.isEmpty(rule.getValues()))||(rule.getValues().length<2)){
            return;
        }
        add(rule.getAndOr(), rule.getPropertyName(),"","between", rule.getValues()[0], "and");
        add(0, "","","",rule.getValues()[1],"");
    }

    private void processEqual(QueryRule.Rule rule){
        if(ArrayUtils.isEmpty(rule.getValues())){
            return;
        }
        add(rule.getAndOr(), rule.getPropertyName(), "=",rule.getValues()[0]);
    }

    private void processLike(QueryRule.Rule rule){
        if(ArrayUtils.isEmpty(rule.getValues())){
            return;
        }
        Object obj = rule.getValues()[0];
        if(obj != null){
            String value = obj.toString();
            if(!StringUtils.isEmpty(value)){
                value = value.replace('*','%');
                obj = value;
            }
        }
        add(rule.getAndOr(), rule.getPropertyName(), "like", "%"+rule.getValues()[0]+"%");
    }

    private void  processNotEqual(QueryRule.Rule rule){
        if(ArrayUtils.isEmpty(rule.getValues())){
            return;
        }
        add(rule.getAndOr(),rule.getPropertyName(), "<>", rule.getValues()[0]);
    }

    /**
     * process >
     * @param rule
     */
    private void processGreaterThen(QueryRule.Rule rule){
        if(ArrayUtils.isEmpty(rule.getValues())){
            return;
        }
        add(rule.getAndOr(), rule.getPropertyName(), ">", rule.getValues()[0]);
    }

    /**
     * >=
     * @param rule
     */
    private void processGreaterEqual(QueryRule.Rule rule){
        if(ArrayUtils.isEmpty(rule.getValues())){
            return;
        }
        add(rule.getAndOr(), rule.getPropertyName(), ">=", rule.getValues()[0]);
    }

    /**
     * process <
     * @param rule
     */
    private void processLessThen(QueryRule.Rule rule){
        if(ArrayUtils.isEmpty(rule.getValues())){
            return;
        }
        add(rule.getAndOr(), rule.getPropertyName(), "<", rule.getValues()[0]);
    }

    /**
     * process <=
     * @param rule
     */
    private void processLessEqual(QueryRule.Rule rule){
        if(ArrayUtils.isEmpty(rule.getValues())){
            return;
        }
        add(rule.getAndOr(), rule.getPropertyName(), "<=", rule.getValues()[0]);
    }

    /**
     * process is null
     * @param rule
     */
    private void processIsNull(QueryRule.Rule rule){
        add(rule.getAndOr(), rule.getPropertyName(), "is null", null);
    }

    /**
     * process is not null
     * @param rule
     */
    private void processIsNotNull(QueryRule.Rule rule){
        add(rule.getAndOr(), rule.getPropertyName(), "is not null", null);
    }

    /**
     * process <>''
     * @param rule
     */
    private void processIsNotEmpty(QueryRule.Rule rule){
        add(rule.getAndOr(), rule.getPropertyName(), "<>","''");
    }

    /**
     * =''
     * @param rule
     */
    private void processIsEmpty(QueryRule.Rule rule){
        add(rule.getAndOr(), rule.getPropertyName(), "=", "''");
    }

    private void inAndNotIn(QueryRule.Rule rule, String name){
        if(ArrayUtils.isEmpty(rule.getValues())){
            return;
        }
        if((rule.getValues().length==1)&&(rule.getValues()[0]!=null)&&(rule.getValues()[0] instanceof List)){
            List<Object> list = (List)rule.getValues()[0];
            if((list != null) && (list.size() > 0)){
                for(int i = 0; i < list.size(); i++){
                    if(i == 0 && i == list.size()-1){
                        add(rule.getAndOr(), rule.getPropertyName(), "", name+ " (",list.get(i), ")");
                    }else if(i == 0 && i < list.size() - 1){
                        add(rule.getAndOr(), rule.getPropertyName(), "", name+ " (", list.get(i), "");
                    }
                    if(i > 0 && i < list.size() - 1){
                        add(0, "", ",","",list.get(i),"");
                    }
                    if(i == list.size() - 1 && i != 0){
                        add(0,"",",","",list.get(i),")");
                    }
                }
        }
        } else {
            Object[] list = rule.getValues();
            for(int i = 0; i < list.length; i++){
                if(i == 0 && i == list.length -1){
                    add(rule.getAndOr(), rule.getPropertyName(), "", name + " (", list[i], ")");
                }else if(i == 0 && i < list.length - 1){
                    add(rule.getAndOr(), rule.getPropertyName(), "", name + " (", list[i], "");
                }
                if(i > 0 && i < list.length - 1){
                    add(0,"",",","",list[i],"");
                }
                if(i == list.length -1 && i != 0){
                    add(0, "", ",","",list[i],")");
                }

            }
        }
    }

    /**
     * not in
     * @param rule
     */
    private void processNotIn(QueryRule.Rule rule){
        inAndNotIn(rule, "not in");
    }

    /**
     * in
     * @param rule
     */
    private void processIN(QueryRule.Rule rule){
        inAndNotIn(rule,"in");
    }

    private void processOrder(QueryRule.Rule rule){
        switch (rule.getType()){
            case QueryRule.ASC_ORDER:
                if(!StringUtils.isEmpty(rule.getPropertyName())){
                    orders.add(Order.asc(rule.getPropertyName()));
                }
                break;
            case QueryRule.DESC_ORDER:
                if(!StringUtils.isEmpty(rule.getPropertyName())) {
                    orders.add(Order.desc(rule.getPropertyName()));
                }
                break;
            default:
                break;
        }
    }
    private void add(int andOr, String key, String split, Object value){
        add(andOr, key, split, "", value,"");
    }

    private void add(int andOr, String key, String split, String prefix, Object value, String suffix){
        String andOrStr = (0 == andOr ? "":(QueryRule.AND==andOr? " and ":" or "));
        properties.add(CURR_INDEX, andOrStr + key + " " + split + prefix + (null != value ? " ? ":" ")+suffix);
        if(null != value){
            values.add(CURR_INDEX, value);
            CURR_INDEX ++;
        }
    }

    private void appendWhereSql(){
        StringBuffer whereSql = new StringBuffer();
        for (String p: properties
             ) {
            whereSql.append(p);
        }
        this.whereSql = removeSelect(removeOrders(whereSql.toString()));
    }

    private void appendOrderSql(){
        StringBuffer orderSql = new StringBuffer();
        for(int i = 0; i < orders.size(); i++){
            if(i > 0 && i < orders.size()){
                orderSql.append(",");
            }
            orderSql.append(orders.get(i).toString());
        }
        this.orderSql = removeSelect(orderSql.toString());
    }

    private void appendValues(){
        Object[] val = new Object[values.size()];
        for(int i = 0; i < values.size(); i ++){
            val[i] = values.get(i);
            valueMap.put(i, values.get(i));
        }
        this.valueArr = val;
    }
}
