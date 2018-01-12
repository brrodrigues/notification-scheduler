package br.com.lasa.notificacao.repository.exception;

public class NoDataFoundException extends Exception {

    public NoDataFoundException() {
    }

    public NoDataFoundException(String message) {
        super(message);
    }

    public NoDataFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoDataFoundException(Throwable cause) {
        super(cause);
    }

    public NoDataFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
