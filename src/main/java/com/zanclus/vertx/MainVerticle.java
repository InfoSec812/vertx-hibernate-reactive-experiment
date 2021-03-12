package com.zanclus.vertx;

import com.zanclus.vertx.models.Todo;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.hibernate.reactive.mutiny.Mutiny;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

public class MainVerticle extends AbstractVerticle {

    EntityManagerFactory emf;
    private Mutiny.SessionFactory sessionFactory;

    @Override
    public void start(Promise<Void> start) {
        Map<String, String> dbSettings = new HashMap<>();
        dbSettings.put("javax.persistence.jdbc.url","jdbc:postgresql://localhost:5432/todo");
        dbSettings.put("javax.persistence.jdbc.user","todo");
        dbSettings.put("javax.persistence.jdbc.password","todo");
        dbSettings.put("hibernate.connection.pool_size","10");
        dbSettings.put("javax.persistence.schema-generation.database.action","drop-and-create");
        dbSettings.put("javax.persistence.create-database-schemas","true");
        dbSettings.put("hibernate.show_sql","true");
        dbSettings.put("hibernate.format_sql","true");
        dbSettings.put("hibernate.highlight_sql","true");

        try {
            emf = Persistence.createEntityManagerFactory("postgresql", dbSettings);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            sessionFactory = emf.unwrap(Mutiny.SessionFactory.class);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        vertx.setPeriodic(5000, this::insertRecord);
        start.complete();
    }

    private void insertRecord(Long aLong) {
        System.out.println("Preparing to add a new Todo");
        Todo todo = new Todo();
        todo.setTitle("Example");
        todo.setDescription("Example description");
        todo.setCreated(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));
        sessionFactory
            .withTransaction((session, tx) -> session.persist(todo))
            .onFailure(this::logError)
                .recoverWithNull()
            .subscribe()
                .with(this::logAction);
    }

    private void logAction(Void unused) {
        System.out.println("Persisted Todo item");
    }

    private void logAction() {
        System.out.println("Persisted Todo item");
    }

    private boolean logError(Throwable throwable) {
        throwable.printStackTrace();
        return false;
    }
}
