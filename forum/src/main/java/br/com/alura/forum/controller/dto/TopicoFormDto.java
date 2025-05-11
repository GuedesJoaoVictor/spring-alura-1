package br.com.alura.forum.controller.dto;

import br.com.alura.forum.modelo.Curso;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TopicoFormDto {
    @NotNull @NotEmpty @Size(min = 5)
    private String titulo;
    @NotNull @NotEmpty @Size(min = 10)
    private String mensagem;
    @NotNull @NotEmpty
    private String nomeCurso;

    public TopicoFormDto(String titulo, String mensagem, String nomeCurso) {
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.nomeCurso = nomeCurso;
    }

    public Topico converter(CursoRepository cursoRepository) {
        Curso curso = cursoRepository.findByNome(nomeCurso);
        return new Topico(titulo, mensagem, curso);
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getNomeCurso() {
        return nomeCurso;
    }

    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }
}
