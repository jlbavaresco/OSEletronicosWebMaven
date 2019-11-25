package br.edu.ifsul.dao;

import java.io.Serializable;

/**
 * @author Prof. Me. Jorge Luis Boeira Bavaresco
 * @email jorge.bavaresco@passofundo.ifsul.edu.br
 * @organization IFSUL - Campus Passo Fundo
 */
public class Ordem implements Serializable {
    private String atributo;
    private String label;
    private String operador;

    /**
     *
     * @param atributo Atributo da classe será usado na consulta
     * @param label Chave de internacionalização para o valor do atributo que será exibido na tela
     * @param operador Operador usado na consulta =,like,<,>
     */    
    public Ordem(String atributo, String label, String operador) {
        this.atributo = atributo;
        this.label = label;
        this.operador = operador;
    }

    public String getAtributo() {
        return atributo;
    }

    public void setAtributo(String atributo) {
        this.atributo = atributo;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    @Override
    public String toString() {
        return label;
    }
}
