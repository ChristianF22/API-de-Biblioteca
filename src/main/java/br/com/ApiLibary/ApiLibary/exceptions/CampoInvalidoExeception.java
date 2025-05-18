package br.com.ApiLibary.ApiLibary.exceptions;

import lombok.Getter;

public class CampoInvalidoExeception extends RuntimeException {

    @Getter
    private String campo;

    public CampoInvalidoExeception(String campo, String mensagem){
        super(mensagem);
        this.campo = campo;
    }

}
