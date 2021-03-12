package com.zanclus.vertx.db;

import com.zanclus.vertx.models.Todo;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.UUID;

public class TodoRepository extends Repository<Todo, UUID> {

	public TodoRepository(Mutiny.SessionFactory sessionFactory) {
		super(Todo.class, sessionFactory);
	}
}
