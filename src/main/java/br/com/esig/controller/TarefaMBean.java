package br.com.esig.controller;
import br.com.esig.model.Tarefa;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

@ManagedBean
@SessionScoped
public class TarefaMBean implements Serializable{

	private static final long serialVersionUID = 1L;

	private List<Tarefa> tarefas;

	private Tarefa tarefa;

	// Propriedades para a busca
	String tituloOuDesc = "";
	String responsavel = "";
	Long id = 1L;
	int prioridade = 0;
	boolean status = false;

	// EM para busca no banco
	EntityManager em = EMUtil.getEntityManager();

	public TarefaMBean() {
		this.tarefa = new Tarefa();
		buscarTodasAsTarefas();
	}

	public String adicionarTarefa(){
		em.getTransaction().begin();
		this.tarefa = em.merge(this.tarefa);
		em.persist(this.tarefa);
		em.getTransaction().commit();
		this.tarefa = new Tarefa();
		buscarTodasAsTarefas();
		return "listaDeTarefas";
	}

	public String modificarTarefa() {
		String id = getParam("id");
		this.tarefa = em.find(Tarefa.class, Long.parseLong(id));
		return "index";
	}

	public void concluirTarefa() {
		String id = getParam("id");
		this.tarefa = em.find(Tarefa.class, Long.parseLong(id));
		this.tarefa.setStatus(true);
		adicionarTarefa();
	}

	public String removerTarefa(){
		String id = getParam("id");
		this.tarefa = em.find(Tarefa.class, Long.parseLong(id));
		em.getTransaction().begin();
		tarefa = em.merge(tarefa);
		em.remove(tarefa);
		em.getTransaction().commit();
		this.tarefa = new Tarefa();
		buscarTodasAsTarefas();
		return "listaDeTarefas";
	}

	public String buscarTarefas(){
		if(this.id == null){
        TypedQuery<Tarefa> tQuery = em.createQuery("from Tarefa t where (t.titulo like :tituloOuDesc or " + 
			"t.descricao like :tituloOuDesc or t.responsavel like :responsavel) and t.status = :status", Tarefa.class);

		tQuery.setParameter("tituloOuDesc", this.tituloOuDesc.isEmpty() ? this.tituloOuDesc : "%" + this.tituloOuDesc + "%");
		tQuery.setParameter("responsavel", this.responsavel.isEmpty() ? this.responsavel : "%" + this.responsavel + "%");
		tQuery.setParameter("status", this.status);
		this.setTarefas(tQuery.getResultList());
		}else{
			this.tarefa = em.find(Tarefa.class, this.id);
		}
		return "listaDeTarefas";
	}

	public void buscarTodasAsTarefas(){
		TypedQuery<Tarefa> tQuery = em.createQuery("select t from Tarefa t", Tarefa.class);
		this.tarefas = tQuery.getResultList();
	}
	
	public String getParam(String param) {
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
		return params.get(param);
	}
	
	public Tarefa getTarefa() {
		return tarefa;
	}

	public void setTarefa(Tarefa tarefa) {
		this.tarefa = tarefa;
	}

	public List<Tarefa> getTarefas() {
		return tarefas;
	}

	public void setTarefas(List<Tarefa> tarefas) {
		this.tarefas = tarefas;
	}

}
