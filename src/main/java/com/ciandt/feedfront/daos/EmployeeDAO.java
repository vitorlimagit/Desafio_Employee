package com.ciandt.feedfront.daos;

import com.ciandt.feedfront.contracts.DAO;
import com.ciandt.feedfront.models.Employee;
import com.ciandt.feedfront.excecoes.EntidadeNaoSerializavelException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EmployeeDAO implements DAO<Employee> {
    private String repositorioPath = "src/main/resources/data/employee/";

    private static ObjectOutputStream getOutputStream(String arquivo) throws IOException {
        return new ObjectOutputStream(new FileOutputStream(arquivo));
    }

    private static ObjectInputStream getInputStream(String arquivo) throws IOException {
        return new ObjectInputStream(new FileInputStream(arquivo));
    }

    @Override
    public boolean tipoImplementaSerializable() {
        return Employee.class instanceof Serializable;
    }

    @Override
    public List<Employee> listar() throws IOException, EntidadeNaoSerializavelException {
        if (!this.tipoImplementaSerializable()) {
            throw new EntidadeNaoSerializavelException("Employee não é serializável");
        }

        List<Employee> employees = new ArrayList<>();

        Stream<Path> paths = Files.walk(Paths.get(this.repositorioPath));

        List<String> fileNames = paths
                .map(p -> p.getFileName().toString())
                .filter(p -> p.endsWith(".byte"))
                .map(p -> p.replace(".byte", ""))
                .collect(Collectors.toList());

        paths.close();

        for (String fileName: fileNames) {
            employees.add(buscar(fileName));
        }

        paths.close();

        return employees;
    }

    @Override
    public Employee buscar(String id) throws IOException, EntidadeNaoSerializavelException{
        if (!this.tipoImplementaSerializable()) {
            throw new EntidadeNaoSerializavelException("Employee não é serializável");
        }

        Employee employee = null;
        String pathCompleto = this.repositorioPath + id + ".byte";
        ObjectInputStream inputStream = getInputStream(pathCompleto);

        try {
            employee = (Employee) inputStream.readObject();

        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        } finally {
            inputStream.close();
        }


        return employee;
    }

    @Override
    public Employee salvar(Employee employee) throws IOException, EntidadeNaoSerializavelException {
        if (!this.tipoImplementaSerializable()) {
            throw new EntidadeNaoSerializavelException("Employee não é serializável");
        }

        ObjectOutputStream outputStream = getOutputStream(this.repositorioPath + employee.getArquivo());
        outputStream.writeObject(employee);

        outputStream.close();

        return employee;
    }

    @Override
    public boolean apagar(String id) throws IOException, EntidadeNaoSerializavelException {
        if (!this.tipoImplementaSerializable()) {
            throw new EntidadeNaoSerializavelException("Employee não é serializável");
        }

        Employee employee = buscar(id);

        if (employee == null) {
            return false;
        }

        return new File(this.repositorioPath + employee.getArquivo()).delete();
    }
}
