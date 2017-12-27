package br.com.lasa.notificacao.domain;

import java.util.Date;

public enum TimeUnit {

    DIARIO {
        @Override
        public String getCronTrigger() {
            return String.format("* * * 0/%s * *", 1);
        }
    },
    SEGUNDO {
        @Override
        public String getCronTrigger() {
            return String.format("0/%s * * * * *", 1) ;
        }
    },
    MINUTO {
        @Override
        public String getCronTrigger() {
            return String.format("* 0/%s * * * *", 1) ;
        }
    },
    HORA  {
        @Override
        public String getCronTrigger() {
            return String.format("* * 0/%s * * *", 1) ;
        }
    };

    private Date date;

    TimeUnit(Date date) {
        this.date = date;
    }


    TimeUnit() {

    }

    public abstract String getCronTrigger();
}
