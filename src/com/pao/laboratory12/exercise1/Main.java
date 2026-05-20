package com.pao.laboratory12.exercise1;

import com.pao.laboratory12.model.Author;
import com.pao.laboratory12.model.Book;
import com.pao.laboratory12.repository.AuthorRepository;
import com.pao.laboratory12.repository.BookRepository;
import com.pao.laboratory12.util.DatabaseConnection;
import com.pao.laboratory12.util.SchemaInitializer;

public class Main {
    public static void main(String[] args) throws Exception {
        SchemaInitializer.initFromSqliteScript(DatabaseConnection.getInstance().getConnection());

        AuthorRepository authors = new AuthorRepository();
        BookRepository books = new BookRepository();

        Author a = new Author("Demo Autor", "RO");
        authors.save(a);
        Book b = new Book("Demo carte", a.getId());
        books.save(b);

        System.out.println("Exercise1 CRUD smoke: autor id=" + a.getId() + ", carte id=" + b.getId());
        System.out.println("findAll authors: " + authors.findAll().size());
        System.out.println("findAll books: " + books.findAll().size());

        DatabaseConnection.getInstance().close();
    }
}
