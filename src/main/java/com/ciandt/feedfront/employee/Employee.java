package com.ciandt.feedfront.employee;

import com.ciandt.feedfront.excecoes.ArquivoException;
import com.ciandt.feedfront.excecoes.ComprimentoInvalidoException;
import com.ciandt.feedfront.excecoes.EmailInvalidoException;
import com.ciandt.feedfront.excecoes.EmployeeNaoEncontradoException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Employee {
    private String id;
    private String nome;
    private String sobrenome;
    private String email;
    private String arquivo = "arquivo.extensao"; //TODO: alterar de acordo com a sua implementação
    private static final String repositorioPath = "src/main/resources/data/employee/"; //

    private static final long serialVersionID = 1;

    public Employee(String nome, String sobrenome, String email) throws ComprimentoInvalidoException {
        this.id = UUID.randomUUID().toString();
        this.arquivo = repositorioPath + getId() + ".byte";

        setNome(nome);
        setEmail(email);
        setSobrenome(sobrenome);
    }

    private static ObjectOutputStream getOutputStream(String arquivo) throws IOException{
        return new ObjectOutputStream(new FileOutputStream(arquivo));

    }

    private static ObjectInputStream getInputStream(String arquivo) throws IOException{
        return new ObjectInputStream(new FileInputStream(arquivo));
    }

    public static Employee salvarEmployee(Employee employee) throws ArquivoException, EmailInvalidoException {
        ObjectOutputStream outputStream = null;

        try{
            List<Employee> employees = listarEmployees();

            boolean emailExistente = false;
            for(Employee employeeSalvo : employees){
                if(!employeeSalvo.getId().equals((employee.getId())) && employeeSalvo.getEmail().equals(employee.getEmail())){
                    emailExistente = true;
                    break;
                }
            }
            if(emailExistente){
                throw new EmailInvalidoException("E-mail já cadastrado no arquivo");
            }
            outputStream = getOutputStream(employee.arquivo);
            outputStream.writeObject(employee);

        } catch (IOException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new ArquivoException("");
        }

        return employee;
    }


    public static Employee atualizarEmployee(Employee employee) throws ArquivoException, EmailInvalidoException, EmployeeNaoEncontradoException {
        buscarEmployee(employee.getId());

        return salvarEmployee(employee);

    }

    public static List<Employee> listarEmployees() throws ArquivoException {
       List<Employee> employees = new ArrayList<>();

       try {
           Stream<Path> paths = Files.walk(Paths.get(repositorioPath));

           List<String> files = paths
                   .map(p -> p.getFileName().toString())
                   .filter(p -> p.endsWith(".byte"))
                   .map(p -> p.replace(".byte", ""))
                   .collect(Collectors.toList());

           for (String file : files) {
               try {
                   employees.add(buscarEmployee(file));
               } catch (EmployeeNaoEncontradoException e) {
                   // Exception silenciada porque não chegara aqui
               }
           }
           paths.close();
       } catch (IOException e){
           throw new ArquivoException("erro ao processar arquivos");
       }

        return employees;
    }

    public static Employee buscarEmployee(String id) throws ArquivoException, EmployeeNaoEncontradoException {
        return null;
    }

    public static void apagarEmployee(String id) throws ArquivoException, EmployeeNaoEncontradoException {
    }

    public String getNome() {
        return null;
    }

    public void setNome(String nome) throws ComprimentoInvalidoException {
    }

    public String getSobrenome() {
        return null;
    }

    public void setSobrenome(String sobrenome) throws ComprimentoInvalidoException {
    }

    public String getEmail() {
        return null;
    }

    public void setEmail(String email) {
    }

    public String getId() {
        return id;
    }

}
