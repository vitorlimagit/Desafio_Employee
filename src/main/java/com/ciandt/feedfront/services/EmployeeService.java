package com.ciandt.feedfront.services;

import com.ciandt.feedfront.contracts.DAO;
import com.ciandt.feedfront.contracts.Service;
import com.ciandt.feedfront.daos.EmployeeDAO;
import com.ciandt.feedfront.excecoes.EmailInvalidoException;
import com.ciandt.feedfront.excecoes.EntidadeNaoEncontradaException;
import com.ciandt.feedfront.models.Employee;
import com.ciandt.feedfront.excecoes.ArquivoException;
import com.ciandt.feedfront.excecoes.BusinessException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

public class EmployeeService implements Service<Employee> {
    private DAO<Employee> dao;

    public EmployeeService() {
        dao = new EmployeeDAO();
    }

    private boolean emailJaCadastrado(Employee employee) throws ArquivoException {
        Predicate<Employee> comparaEmailComId = e -> e.getEmail().equals(employee.getEmail()) &&
                !e.getId().equals(employee.getId());

        return listar().stream().anyMatch(comparaEmailComId);
    }

    @Override
    public List<Employee> listar() throws ArquivoException {
        List<Employee> employees;

        try {
            employees = this.dao.listar();
        } catch (IOException e) {
            throw new ArquivoException(e.getMessage());
        }

        return employees;
    }

    @Override
    public Employee buscar(String id) throws ArquivoException, BusinessException {
        Employee employee;

        try {
            employee = this.dao.buscar(id);

        } catch (IOException e) {
            if (e instanceof FileNotFoundException) {
                throw new EntidadeNaoEncontradaException("não foi possível encontrar o employee");
            }

            throw new ArquivoException(e.getMessage());
        }

        return employee;
    }

    @Override
    public Employee salvar(Employee employee) throws ArquivoException, BusinessException {
        if  (employee == null) {
            throw new IllegalArgumentException("employee inválido");
        }

        if (emailJaCadastrado(employee)) {
            throw new EmailInvalidoException("já existe um employee cadastrado com esse e-mail");
        }

        Employee employeeSalvo;

        try {
            employeeSalvo = this.dao.salvar(employee);
        } catch (IOException e) {
            throw new ArquivoException(e.getMessage());
        }

        return employeeSalvo;
    }

    @Override
    public Employee atualizar(Employee employee) throws ArquivoException, BusinessException {
        if (emailJaCadastrado(employee)) {
            throw new EmailInvalidoException("E-mail ja cadastrado no repositorio");
        }

        buscar(employee.getId());
        Employee employeeAtualizado;

        try {
            employeeAtualizado = this.dao.salvar(employee);
        } catch (IOException e) {
            throw new ArquivoException(e.getMessage());
        }

        return employeeAtualizado;
    }

    @Override
    public void apagar(String id) throws ArquivoException, BusinessException {
        try {
            this.dao.apagar(id);
        } catch (IOException e) {
            if (e instanceof FileNotFoundException) {
                throw new EntidadeNaoEncontradaException("não foi possível encontrar o employee");
            }

            throw new ArquivoException(e.getMessage());
        }
    }

    @Override
    public void setDAO(DAO<Employee> dao) {
        this.dao = dao;
    }
}
