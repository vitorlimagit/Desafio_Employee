package com.ciandt.feedfront.daos;

import com.ciandt.feedfront.contracts.DAO;
import com.ciandt.feedfront.excecoes.EntidadeNaoSerializavelException;
import com.ciandt.feedfront.models.Feedback;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FeedbackDAO implements DAO<Feedback> {
    private String repositorioPath = "src/main/resources/data/feedback/";

    private static ObjectOutputStream getOutputStream(String arquivo) throws IOException {
        return new ObjectOutputStream(new FileOutputStream(arquivo));
    }

    private static ObjectInputStream getInputStream(String arquivo) throws IOException {
        return new ObjectInputStream(new FileInputStream(arquivo));
    }

    @Override
    public boolean tipoImplementaSerializable() {
        return Feedback.class instanceof Serializable;
    }

    @Override
    public List<Feedback> listar() throws IOException, EntidadeNaoSerializavelException {
        if (!this.tipoImplementaSerializable()) {
            throw new EntidadeNaoSerializavelException("Feedback não é serializável");
        }

        List<Feedback> feedbacks = new ArrayList<>();

        Stream<Path> paths = Files.walk(Paths.get(this.repositorioPath));

        List<String> filesNames = paths
                .map(p -> p.getFileName().toString())
                .filter(p -> p.endsWith(".byte"))
                .map(p -> p.replace(".byte", ""))
                .collect(Collectors.toList());

        paths.close();

        for (String fileName: filesNames) {
            feedbacks.add(buscar(fileName));
        }

        return feedbacks;
    }

    @Override
    public Feedback buscar(String id) throws IOException, EntidadeNaoSerializavelException {
        if (!this.tipoImplementaSerializable()) {
            throw new EntidadeNaoSerializavelException("Employee não é serializável");
        }

        Feedback feedback = null;

        String pathCompleto = this.repositorioPath + id + ".byte";
        ObjectInputStream inputStream = getInputStream(pathCompleto);

        try {
            feedback = (Feedback) inputStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        } finally {
            inputStream.close();
        }

        return feedback;
    }

    @Override
    public Feedback salvar(Feedback feedback) throws IOException, EntidadeNaoSerializavelException {
        if (!this.tipoImplementaSerializable()) {
            throw new EntidadeNaoSerializavelException("Employee não é serializável");
        }

        ObjectOutputStream outputStream = getOutputStream(this.repositorioPath + feedback.getArquivo());
        outputStream.writeObject(feedback);

        outputStream.close();

        return feedback;
    }

    @Override
    public boolean apagar(String id) throws IOException, EntidadeNaoSerializavelException {
        throw new UnsupportedOperationException();
    }
}
