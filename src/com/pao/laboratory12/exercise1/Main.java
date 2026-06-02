package com.pao.laboratory12.exercise1;

import com.pao.laboratory12.exercise1.model.Author;
import com.pao.laboratory12.exercise1.repository.AuthorRepository;

public class Main {
    public static void main(String[] args) {
        System.out.println("Incercam conectarea la baza de date MariaDB...");

        try {
            AuthorRepository authorRepo = new AuthorRepository();

            Author newAuthor = new Author("J.K. Rowling", "UK");
            authorRepo.save(newAuthor);
            System.out.println("✅ Autor salvat cu succes! ID-ul generat automat este: " + newAuthor.getId());

            System.out.println("\n📚 Toti autorii din baza de date:");
            authorRepo.findAll().forEach(author -> System.out.println("  -> " + author));

        } catch (Exception e) {
            System.err.println("❌ Eroare la testare:");
            e.printStackTrace();
        }
    }
}