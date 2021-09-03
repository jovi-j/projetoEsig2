package br.com.esig.controller;
import br.com.esig.model.Tarefa;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
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
	private String tituloOuDesc = "";
	private Long id;
	private String responsavel = "";
	private boolean status = false;

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
		String pid = getParam("id");
		this.tarefa = em.find(Tarefa.class, Long.parseLong(pid));
		if(this.tarefa.getStatus()) {
			enviarMensagem("A tarefa já foi concluida", "A tarefa já está concluida.", FacesMessage.SEVERITY_INFO);
			this.tarefa = new Tarefa();
			return;
		}
		this.tarefa.setStatus(true);
		enviarMensagem("Tarefa concluida.", "Tarefa modificada com sucesso.", FacesMessage.SEVERITY_INFO);
		adicionarTarefa();
	}

	public String removerTarefa(){
		String pid = getParam("id");
		this.tarefa = em.find(Tarefa.class, Long.parseLong(pid));
		em.getTransaction().begin();
		tarefa = em.merge(tarefa);
		em.remove(tarefa);
		em.getTransaction().commit();
		this.tarefa = new Tarefa();
		buscarTodasAsTarefas();
		return "listaDeTarefas";
	}

	public String buscarTarefas(){
		//Só realizar uma busca intensiva caso o id não for passado
		if(this.id == null){
        TypedQuery<Tarefa> tQuery = em.createQuery("from Tarefa t where " + 
        		"t.titulo like :titulo and " + 
        		"t.descricao like :descricao and " +
        		"t.responsavel like :responsavel and " + 
        		"t.status = :status", Tarefa.class);
        // "%"s necessários para o "LIKE" funcionar
		tQuery.setParameter("titulo", "%" + this.tituloOuDesc + "%");
		tQuery.setParameter("descricao", "%" + this.tituloOuDesc + "%");
        tQuery.setParameter("responsavel", "%" + this.responsavel + "%");
		tQuery.setParameter("status", this.status);
		setTarefas(tQuery.getResultList());
		// Aviso de busca vazia
		if(this.tarefas.isEmpty()) {	
			enviarMensagem("Nenhum Resultado", "A busca não retornou resultados", FacesMessage.SEVERITY_ERROR);
		} 
		}
		// Se for, realizar uma busca única por id
		else{
			Tarefa t = em.find(Tarefa.class, this.id);
			if(t == null) {	
				enviarMensagem("Nenhum Resultado", "Id não existe no banco de dados.", FacesMessage.SEVERITY_ERROR);
				setTarefas(new ArrayList<Tarefa>());
			}else {
				setTarefas(new ArrayList<Tarefa>());
				tarefas.add(t);
			}
		}
		return "listaDeTarefas";
	}

	public void buscarTodasAsTarefas(){
		TypedQuery<Tarefa> tQuery = em.createQuery("select t from Tarefa t", Tarefa.class);
		this.tarefas = tQuery.getResultList();
	}
	
	// Função simples para pegar um único parâmetro vindo da requisição
	public String getParam(String param) {
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
		return params.get(param);
	}
	
	public void enviarMensagem(String mensagemResumida, String mensagemInteira, FacesMessage.Severity severidade) {
		FacesMessage fm = new FacesMessage(severidade, mensagemResumida, mensagemInteira);
		FacesContext ctx = FacesContext.getCurrentInstance();
		ctx.addMessage("messageOutput", fm);
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

	public String getTituloOuDesc() {
		return tituloOuDesc;
	}

	public void setTituloOuDesc(String tituloOuDesc) {
		this.tituloOuDesc = tituloOuDesc;
	}

	public String getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean getStatus() {
		return this.status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
}
