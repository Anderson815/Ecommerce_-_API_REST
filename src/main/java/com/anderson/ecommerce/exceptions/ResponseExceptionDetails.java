package com.anderson.ecommerce.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.spi.CalendarDataProvider;

public class ResponseExceptionDetails {

    private String data;
    private int cod;
    private String erro;
    private String mensagem;

    //Construtor

    public ResponseExceptionDetails(int cod, String erro, String mensagem) {

        this.cod = cod;
        this.erro = erro;
        this.mensagem = mensagem;

        TimeZone horaBrasil = TimeZone.getTimeZone("Brazil/East");
        Calendar momentoErro = new GregorianCalendar(horaBrasil);

        SimpleDateFormat formatacaoData = new SimpleDateFormat("dd-MM-yyyy 'às' HH:mm:ss");
        this.data = formatacaoData.format(momentoErro.getTime());

    }

    //Get e Set

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public String getErro() {
        return erro;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }


}
