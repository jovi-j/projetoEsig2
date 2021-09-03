package br.com.esig.controller;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EMUtil {

	private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("bancoEsig");	

	public static EntityManager getEntityManager() {
		return emf.createEntityManager();
	}
}