package br.com.lasa.notificacao.util;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class DaoUtil {
    //	private static Logger logger = LogManager.getLogger();

    public static void release ( PreparedStatement ps) throws SQLException {

        if (ps != null) {
            ps.close();
        }
    }

    private static void setNull(PreparedStatement ps, int parameterIndex) throws SQLException {
        ps.setNull(parameterIndex, Types.NULL);
    }

    public static void setObject(PreparedStatement ps, int parameterIndex, Object x, int targetSqlType)
            throws SQLException {
        if(x==null){
            setNull(ps, parameterIndex);
        }else{
            ps.setObject(parameterIndex, x, targetSqlType);
        }
    }

    public void setObject(PreparedStatement ps, int parameterIndex, Object x) throws SQLException {
        if(x==null){
            setNull(ps, parameterIndex);
        }else{
            ps.setObject(parameterIndex, x);
        }
    }

    public static void setInt(PreparedStatement ps, int parameterIndex, Integer x) throws SQLException {
        if(x==null){
            setNull(ps, parameterIndex);
        }else{
            ps.setInt(parameterIndex, x);
        }

    }

    public static void setLong(PreparedStatement ps, int parameterIndex, Long x) throws SQLException {
        if(x == null){
            setNull(ps, parameterIndex);
        }else{
            ps.setLong(parameterIndex, x);
        }
    }

    public static  void setFloat(PreparedStatement ps, int parameterIndex, Float x) throws SQLException {
        if(x==null){
            setNull(ps, parameterIndex);
        }else{
            ps.setFloat(parameterIndex, x);
        }
    }

    public static void setDouble(PreparedStatement ps, int parameterIndex, Double x) throws SQLException {
        if(x==null){
            setNull(ps, parameterIndex);
        }else{
            ps.setDouble(parameterIndex, x);
        }
    }

    public static void setString(PreparedStatement ps, int parameterIndex, String x) throws SQLException {
        if(x==null){
            setNull(ps, parameterIndex);
        }else{
            ps.setString(parameterIndex, x);
        }
    }

    public static void setDate(PreparedStatement ps, int parameterIndex, Date x) throws SQLException {
        if(x==null){
            setNull(ps, parameterIndex);
        }else{
            ps.setDate(parameterIndex, new java.sql.Date(x.getTime()));
        }
    }

    public static void setTime(PreparedStatement ps, int parameterIndex, Date x) throws SQLException {
        if(x==null){
            setNull(ps, parameterIndex);
        }else{
            ps.setTime(parameterIndex, new java.sql.Time(x.getTime()));
        }
    }

    public static void setTimestamp(PreparedStatement ps, int parameterIndex, Timestamp x)
            throws SQLException {
        if(x==null){
            setNull(ps, parameterIndex);
        }else{
            ps.setTimestamp(parameterIndex, x);
        }
    }

    public static void setDate(PreparedStatement ps, int parameterIndex, Date x, Calendar cal)
            throws SQLException {
        if(x==null){
            setNull(ps, parameterIndex);
        }else{
            ps.setDate(parameterIndex, new java.sql.Date(x.getTime()), cal);
        }
    }

    public static void setTime(PreparedStatement ps, int parameterIndex, Time x, Calendar cal)
            throws SQLException {
        if(x==null){
            setNull(ps, parameterIndex);
        }else{
            ps.setTime(parameterIndex, x, cal);
        }
    }

    public static void setTimestamp(PreparedStatement ps, int parameterIndex, Timestamp x, Calendar cal)
            throws SQLException {
        if ( x==null){
            setNull(ps, parameterIndex);
        }else{
            ps.setTimestamp(parameterIndex, x, cal);
        }
    }

    public static void setBigDecimal(PreparedStatement ps, int parameterIndex, BigDecimal x) throws SQLException {
        if(x==null){
            setNull(ps, parameterIndex);
        }else{
            ps.setBigDecimal(parameterIndex, x);
        }
    }

    public static LocalDate getLocalDate(ResultSet rs, int parameterIndex) throws SQLException {
        if(rs == null){
            return null;
        }else{
            return rs.getDate(parameterIndex).toLocalDate();
        }
    }

    public static LocalDateTime getLocalDateTime(ResultSet rs, int parameterIndex) throws SQLException {
        if(rs == null){
            return null;
        }else{
            return rs.getTimestamp(parameterIndex).toLocalDateTime();
        }
    }
}
