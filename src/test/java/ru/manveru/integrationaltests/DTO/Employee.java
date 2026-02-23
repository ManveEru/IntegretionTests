package ru.manveru.integrationaltests.DTO;

import java.util.Objects;
import ru.manveru.integrationaltests.annotations.Column;
import ru.manveru.integrationaltests.annotations.Table;

@Table(name = "employees")
public class Employee {
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "department")
    private String department;
    @Column(name = "salary")
    private int salary;
    
    // Конструкторы
    public Employee() {}
    
    public Employee(String name, String surname, String department, int salary) {
        this.name = name;
        this.surname = surname;
        this.department = department;
        this.salary = salary;
    }
    
    // Геттеры и сеттеры
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public int getSalary() { return salary; }
    public void setSalary(int salary) { this.salary = salary; }
    
    @Override
    public String toString() {
        return String.format("Employee[id=%d, name='%s', surname='%s', department='%s', salary=%d]", 
            id, name, surname, department, salary);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id == employee.id &&
           Double.compare(employee.salary, salary) == 0 &&
           Objects.equals(name, employee.name) &&
           Objects.equals(surname, employee.surname) &&
           Objects.equals(department, employee.department);
}

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, department, salary);
    }
}
