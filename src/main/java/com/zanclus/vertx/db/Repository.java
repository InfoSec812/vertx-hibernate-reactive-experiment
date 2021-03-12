package com.zanclus.vertx.db;

import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.List;

public class Repository<T, K> {

	protected final Mutiny.SessionFactory sessionFactory;
	protected final Class<T> clazz;

	public Repository(Class<T> clazz, Mutiny.SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		this.clazz = clazz;
	}

	public Uni<List<T>> find() {
		return sessionFactory.withSession(session -> session.find(clazz));
	}

	public Uni<T> findById(K key) {
		return sessionFactory.withSession(session -> session.find(clazz, key));
	}

	public Uni<Void> add(final T entity) {
		return sessionFactory.withTransaction((session, tx) -> session.persist(entity));
	}

	public Uni<T> delete(final K key) {
		return sessionFactory.withSession(session -> session.find(clazz, key))
				.onItem()
					.invoke(entity -> sessionFactory.withTransaction((session, tx) -> session.remove(entity)));
	}

	public Uni<T> update(final T entity) {
		return this.sessionFactory.withTransaction((session, tx) -> session.merge(entity));
	}
}
