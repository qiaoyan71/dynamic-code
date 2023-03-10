package com.qy.common.util;//package cn.dmego.seata.common.util;
//
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.ibatis.executor.statement.StatementHandler;
//import org.apache.ibatis.mapping.BoundSql;
//import org.apache.ibatis.mapping.MappedStatement;
//import org.apache.ibatis.mapping.ParameterMapping;
//import org.apache.ibatis.mapping.ParameterMode;
//import org.apache.ibatis.plugin.*;
//import org.apache.ibatis.reflection.MetaObject;
//import org.apache.ibatis.reflection.SystemMetaObject;
//import org.apache.ibatis.session.Configuration;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.apache.ibatis.type.DateOnlyTypeHandler;
//import org.apache.ibatis.type.TimeOnlyTypeHandler;
//import org.apache.ibatis.type.TypeHandler;
//import org.apache.ibatis.type.TypeHandlerRegistry;
//
//import javax.annotation.Resource;
//import java.lang.reflect.Field;
//import java.sql.Connection;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.time.format.DateTimeFormatter;
//import java.util.Date;
//import java.util.List;
//import java.util.Properties;
//import java.util.StringTokenizer;
//
//@Slf4j
////@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
//public class MybatisSqlInterceptor {
//
////    public Object intercept(Invocation invocation) throws Throwable {
////        if (log.isDebugEnabled()) {
////            try {
////                log.debug("==>      MySQL: {}", this.replaceParameters((StatementHandler) invocation.getTarget()));
////            }catch (Exception e) {
////                log.debug("the sql is error !!!", e);
////            }
////        }
////        return invocation.proceed();
////    }
////
////    public Object plugin(Object target) {
////        if (target instanceof StatementHandler) {
////            return Plugin.wrap(this.getRealHandler(target), this);
////        }
////        return target;
////    }
//
//    @Resource
//    private SqlSessionFactory sqlSessionFactory;
//
//
//    @SneakyThrows
//    private Object getRealHandler(Object target) {
//        Class<?> clz = target.getClass();
//        if (clz.getTypeName().contains("$")) {
//            Field field = target.getClass().getSuperclass().getDeclaredField("h");
//            field.setAccessible(true);
//            Object obj = field.get(target);
//            field = obj.getClass().getDeclaredField("target");
//            field.setAccessible(true);
//            return field.get(obj);
//        }else {
//            return target;
//        }
//    }
//
//    /**
//     * ????????????
//     * @param target sql????????????
//     * @return ????????????sql
//     */
//    private String replaceParameters(StatementHandler target) {
////        Configuration configuration = sqlSessionFactory.getConfiguration();
////        sqlSessionFactory.getConfiguration().getMappedStatement(id).getBoundSql(paramMap);
//
//        MetaObject metaObject = SystemMetaObject.forObject(target);
//        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
//        BoundSql boundSql = target.getBoundSql();
//        Object parameterObject = boundSql.getParameterObject();
//        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappings();
//        String sql = this.format(boundSql.getSql());
//        StringBuilder builder = new StringBuilder();
//        int index = 0;
//        StringTokenizer tokenizer = new StringTokenizer(sql, "?");
//        boolean hasMoreTokens = tokenizer.hasMoreTokens();
//        while (hasMoreTokens) {
//            builder.append(tokenizer.nextToken());
//            hasMoreTokens = tokenizer.hasMoreTokens();
//            if (hasMoreTokens) {
//                this.setParameter(builder, mappedStatement, boundSql, parameterObject, parameterMappingList.get(index));
//            }
//            index++;
//        }
//        return builder.toString();
//    }
//
//    /**
//     * ???????????????sql
//     * @param original ??????sql
//     * @return ??????????????????sql
//     */
//    private String format(String original) {
//        StringBuilder builder = new StringBuilder();
//        StringTokenizer tokenizer = new StringTokenizer(original);
//        while (tokenizer.hasMoreTokens()) {
//            builder.append(tokenizer.nextToken());
//            builder.append(' ');
//        }
//        return builder.toString();
//    }
//
//    /**
//     * ????????????
//     * @param sqlBuilder sql?????????
//     * @param mappedStatement ????????????
//     * @param boundSql sql????????????
//     * @param parameterObject ????????????
//     * @param parameterMapping ??????????????????
//     */
//    private void setParameter(
//            StringBuilder sqlBuilder,
//            MappedStatement mappedStatement,
//            BoundSql boundSql,
//            Object parameterObject,
//            ParameterMapping parameterMapping
//    ) {
//        Configuration configuration = mappedStatement.getConfiguration();
//        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
//        if (parameterMapping.getMode() != ParameterMode.OUT) {
//            Object value;
//            String propertyName = parameterMapping.getProperty();
//            if (boundSql.hasAdditionalParameter(propertyName)) {
//                value = boundSql.getAdditionalParameter(propertyName);
//            } else if (parameterObject == null) {
//                value = null;
//            } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
//                value = parameterObject;
//            } else {
//                value = configuration.newMetaObject(parameterObject).getValue(propertyName);
//            }
//            sqlBuilder.append(ParameterHandler.getValue(value, parameterMapping.getTypeHandler()));
//        }
//    }
//
//    /**
//     * ????????????
//     */
//    private static class ParameterHandler {
//        /**
//         * ?????????????????????
//         */
//        private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        /**
//         * ???????????????
//         */
//        private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        /**
//         * ???????????????
//         */
//        private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
//
//        /**
//         * ???????????????
//         * @param value ?????????
//         * @param typeHandler ????????????
//         * @return ???????????????
//         */
//        static Object getValue(Object value, TypeHandler<?> typeHandler) {
//            if (value == null) {
//                return null;
//            } else if (value instanceof String) {
//                return "'" + value + "'";
//            } else if (value instanceof Date) {
//                return "'" + dateFormat((Date) value, typeHandler) + "'";
//            } else {
//                return value;
//            }
//        }
//
//        /**
//         * ???????????????
//         * @param date ????????????
//         * @param typeHandler ????????????
//         * @return ???????????????????????????
//         */
//        static String dateFormat(Date date, TypeHandler<?> typeHandler) {
//            LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//            if (typeHandler instanceof DateOnlyTypeHandler) {
//                return localDateTime.format(DATE_FORMATTER);
//            }
//            if (typeHandler instanceof TimeOnlyTypeHandler) {
//                return localDateTime.format(TIME_FORMATTER);
//            }
//            return localDateTime.format(DATE_TIME_FORMATTER);
//        }
//    }
//}
