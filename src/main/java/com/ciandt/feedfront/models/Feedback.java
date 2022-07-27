package com.ciandt.feedfront.models;

import com.ciandt.feedfront.excecoes.ComprimentoInvalidoException;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

public class Feedback implements Serializable {
    private String id;
    private String arquivo;
    private String descricao;
    private String oQueMelhora;
    private String comoMelhora;
    private LocalDate data;
    private Employee autor;
    private Employee proprietario;

    private static final long serialVersionID = 1;

    public Feedback(LocalDate data, Employee autor, Employee proprietario, String descricao) throws ComprimentoInvalidoException {
        this.id = UUID.randomUUID().toString();
        this.arquivo = getId() + ".byte";

        setData(data);
        setAutor(autor);
        setDescricao(descricao);
        setProprietario(proprietario);
    }

    @Override
    public int hashCode() {
        return 31 * 7 + ((getId() == null) ?  0 : getId().hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Feedback feedback = (Feedback) obj;

        return getId().equals(feedback.getId());
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) throws ComprimentoInvalidoException {
        if (descricao.length() < 3) {
            throw new ComprimentoInvalidoException("Comprimento da descricao deve ser maior que 2 caracteres");
        }

        this.descricao = descricao;
    }

    public String getoQueMelhora() {
        return oQueMelhora;
    }

    public void setoQueMelhora(String oQueMelhora) {
        this.oQueMelhora = oQueMelhora;
    }

    public String getComoMelhora() {
        return comoMelhora;
    }

    public void setComoMelhora(String comoMelhora) {
        this.comoMelhora = comoMelhora;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Employee getAutor() {
        return autor;
    }

    public void setAutor(Employee autor) {
        this.autor = autor;
    }

    public Employee getProprietario() {
        return proprietario;
    }

    public void setProprietario(Employee proprietario) {
        this.proprietario = proprietario;
    }

    public String getId() {
        return id;
    }

    public String getArquivo() {
        return arquivo;
    }
}
