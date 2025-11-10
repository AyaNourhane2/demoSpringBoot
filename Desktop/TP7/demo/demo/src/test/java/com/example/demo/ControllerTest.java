package com.example.demo;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

// Pour les assertions fluides
import static org.assertj.core.api.Assertions.assertThat;

// Entité et Repository simples pour le test
import jakarta.persistence.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// Indique que c'est un test Spring Boot qui charge le contexte complet
@SpringBootTest

// Spécifie l'ordre d'exécution des méthodes de test
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ControllerTest {

    // Injecte le bean StudentRepository du contexte Spring
    @Autowired
    private StudentRepository studentRepository;

    // Premier test : vérifie qu'un étudiant peut être sauvegardé dans le repository
    @Test
    @Order(1) // Garantit que ce test s'exécute en premier
    void shouldSaveStudent() {  
        // Crée un nouvel objet étudiant
        Student student = new Student();
        student.setName("Charlie");
        student.setAddress("Algeria");

        // Sauvegarde l'étudiant dans la base de données H2 en mémoire
        studentRepository.save(student);

        // Vérifie que le repository contient maintenant exactement un enregistrement
        assertThat(studentRepository.count()).isEqualTo(1);  
    }

    // Deuxième test : vérifie que tous les étudiants peuvent être récupérés correctement
    @Test
    @Order(2) // Garantit que ce test s'exécute après shouldSaveStudent()
    void shouldFindAllStudents() {  
        // Récupère tous les étudiants du repository
        List<Student> students = studentRepository.findAll();

        // Vérifie qu'il y a exactement un étudiant dans la liste
        assertThat(students).hasSize(1);

        // Vérifie que le nom de l'étudiant est "Charlie"
        assertThat(students.get(0).getName()).isEqualTo("Charlie");
    }
}

// === CLASSES INTERNES POUR LE TEST SEULEMENT ===

// Entité simple pour les tests
@Entity
@Table(name = "student")
class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String address;
    
    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}

// Repository simple pour les tests
interface StudentRepository extends JpaRepository<Student, Long> {
}