package br.com.lasa.notificacao.repository.exception;

public class MysqlNoDataFoundException extends Exception {

    public MysqlNoDataFoundException() {
    }

    public MysqlNoDataFoundException(String message) {
        super(message);
    }

    public MysqlNoDataFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MysqlNoDataFoundException(Throwable cause) {
        super(cause);
    }

    public MysqlNoDataFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
