package com.zanclus.vertx;

import com.zanclus.vertx.models.Todo;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.hibernate.reactive.stage.Stage;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class MainVerticle extends AbstractVerticle {

    EntityManagerFactory emf;
    private Stage.SessionFactory sessionFactory;

    @Override
    public void start(Promise<Void> start) {
        try {
            emf = Persistence.createEntityManagerFactory("postgresql");
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            sessionFactory = emf.unwrap(Stage.SessionFactory.class);
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
            .exceptionally(this::logError)
            .thenRun(this::logAction);
    }

    private Void logError(Throwable throwable) {
        throwable.printStackTrace();
        return null;
    }

    private void logAction() {
        System.out.println("Added Todo");
    }
}
