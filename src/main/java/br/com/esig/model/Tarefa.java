package br.com.esig.model;

import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "tarefas")
@SessionScoped
@ManagedBean
public class Tarefa {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String titulo;
	private String descricao;
	private String responsavel;
	private int prioridade;
	private boolean status = false;

	@Temporal(TemporalType.DATE)
	private Date deadline;


	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return this.titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getResponsavel() {
		return this.responsavel;
	}

	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}

	public int getPrioridade() {
		return this.prioridade;
	}

	public void setPrioridade(int prioridade) {
		this.prioridade = prioridade;
	}

	public boolean isStatus() {
		return this.status;
	}

	public boolean getStatus() {
		return this.status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Date getDeadline() {
		return this.deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	@Override
	public String toString() {
		return "{" +
			" id='" + getId() + "'" +
			", titulo='" + getTitulo() + "'" +
			", descricao='" + getDescricao() + "'" +
			", responsavel='" + getResponsavel() + "'" +
			", prioridade='" + getPrioridade() + "'" +
			", status='" + isStatus() + "'" +
			", deadline='" + getDeadline() + "'" +
			"}";
	}
}
