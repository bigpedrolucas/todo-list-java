package com.taskmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicação Spring Boot.
 * 
 * EQUIVALENTE DJANGO:
 * - Similar a manage.py (ponto de entrada)
 * - @SpringBootApplication = settings.py + urls.py combinados
 * 
 * COMO FUNCIONA:
 * 1. SpringApplication.run() inicia o embedded Tomcat
 * 2. Auto-configuração baseada nas dependências (pom.xml)
 * 3. Scan por componentes (@Component, @Service, @Repository, @Controller)
 * 
 * COMANDO:
 * mvn spring-boot:run  (equivalente a: python manage.py runserver)
 */
@SpringBootApplication
public class TaskManagerApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(TaskManagerApplication.class, args);
    }
}
