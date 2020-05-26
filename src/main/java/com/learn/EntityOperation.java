package com.learn;


import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class EntityOperation<T> {
    private Logger log = Logger.getLogger(EntityOperation.class);
    public Class<T> entityClass = null;
    public final Map<String, PropertyMapping> mappings;
    public final RowMapper<T> rowMapper;
    public final String tableName;
    public String allColumn = "*";
    public Field pkField;

    public EntityOperation(Class<T> clazz, String pk) throws Exception{
        if(!clazz.isAnnotationPresent(Entity.class)){
            throw new Exception("在"+clazz.getName()+"中没有找到Entity注解，不能做ORM映射");
        }
        this.entityClass = clazz;
        Table table = entityClass.getAnnotation(Table.class);
        if(table!=null){
            this.tableName = table.name();
        } else{
            this.tableName = entityClass.getSimpleName();
        }
        Map<String, Method> getters = ClassMappings.findPublicGetters(entityClass);
        Map<String, Method> setters = ClassMappings.findPublicSetters(entityClass);
        Field[] fields = ClassMappings.findFields(entityClass);
        fillPkFieldAndColumn(pk, fields);
        this.mappings = getPropertyMappings(getters,setters, fields);
        this.allColumn = this.mappings.keySet().toString().replace("[","").replace("]","").replaceAll("","");
        this.rowMapper = createRowMapper();
    }

    Map<String, PropertyMapping> getPropertyMappings(Map<String, Method> getters, Map<String,Method> setters, Field[] fields){
        Map<String, PropertyMapping> mappings = new HashMap<String, PropertyMapping>();
        String name;
        for (Field field: fields) {
            if(field.isAnnotationPresent(Transient.class))
                continue;
            name = field.getName();
            if(name.startsWith("is")){
                name = name.substring(2);
            }
            name = Character.toLowerCase(name.charAt(0))+name.substring(1);
            Method setter = setters.get(name);
            Method getter = getters.get(name);
            if(setter == null || getter == null){
                continue;
            }
            Column column = field.getAnnotation(Column.class);
            if(column == null){
                mappings.put(field.getName(), new PropertyMapping(getter,setter,field));
            }else {
                mappings.put(column.name(), new PropertyMapping(getter,setter,field));
            }
        }
        return mappings;
    }
    
    RowMapper<T> createRowMapper(){
        return new RowMapper<T>() {
            @Override
            public T mapRow(ResultSet rs, int rowNum) throws SQLException {
                try {
                    T t = entityClass.newInstance();
                    ResultSetMetaData meta = rs.getMetaData();
                    int columns = meta.getColumnCount();
                    String columnName;
                    for(int i = 1; i <= columns; i++){
                        Object value = rs.getObject(i);
                        columnName = meta.getColumnName(i);
                        fillBeanFieldValue(t, columnName, value);
                    }
                    return t;
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        };
    }

    protected void fillBeanFieldValue(T t, String columnName, Object value){
        if(value != null){
            PropertyMapping pm = mappings.get(columnName);
            if(pm != null){
                try {
                    pm.set(t, value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void fillPkFieldAndColumn(String pk, Field[] fields){
        //set primary key
        try {
            if(!StringUtils.isEmpty(pk)){
                pkField = entityClass.getDeclaredField(pk);
                pkField.setAccessible(true);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < fields.length; i++){
            Field f = fields[i];
            if(StringUtils.isEmpty(pk)){
                Id id = f.getAnnotation(Id.class);
                if(id != null){
                    pkField=f;
                    break;
                }
            }
        }
    }

    public T parse(ResultSet rs){
        T t = null;
        if(null == rs){
            return null;
        }
        Object value = null;
        try {
            t = (T)entityClass.newInstance();
            for(String columnName :mappings.keySet()){
                try {
                    value = rs.getArray(columnName);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                fillBeanFieldValue(t, columnName, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    public Map<String, Object> parse(T t){
        Map<String, Object> _map = new TreeMap<String, Object>();
        try {
            for (String columnName:
                 mappings.keySet()) {
                Object value = mappings.get(columnName).getter.invoke(t);
                if(value == null)
                    continue;
                _map.put(columnName, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return _map;
    }

    public void println(T t){
        try {
            for (String columnName:mappings.keySet()
                 ) {
                Object value = mappings.get(columnName).getter.invoke(t);
                if(value == null)
                    continue;
                System.out.println(columnName + "=" + value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
    class PropertyMapping{
        final boolean isertable;
        final boolean updatable;
        final String columnName;
        final boolean id;
        final Method getter;
        final Method setter;
        final Class enumClass;
        final String fieldName;

        public PropertyMapping(Method getter, Method setter, Field field){
            this.getter = getter;
            this.setter = setter;
            this.enumClass = getter.getReturnType().isEnum()? getter.getReturnType():null;
            Column column = field.getAnnotation(Column.class);
            this.isertable = column == null || column.insertable();
            this.updatable = column ==null || column.updatable();
            this.columnName = column == null ? ClassMappings.getGetterName(getter):("".equals(column.name())?ClassMappings.getGetterName(getter):column.name());
            this.id = field.isAnnotationPresent(Id.class);
            this.fieldName = field.getName();
        }
        @SuppressWarnings("unchecked")
        Object get(Object target) throws InvocationTargetException, IllegalAccessException {
            Object r = getter.invoke(target);
            return enumClass == null? r:Enum.valueOf(enumClass, (String) r);
        }

        void set(Object target, Object value){
            if(enumClass != null&&value!=null){
                value = Enum.valueOf(enumClass, (String) value);
            }
            try {
                if(value != null){
                    setter.invoke(target, setter.getParameterTypes()[0].cast(value));
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(fieldName+"--"+value);
            }
        }
    }
