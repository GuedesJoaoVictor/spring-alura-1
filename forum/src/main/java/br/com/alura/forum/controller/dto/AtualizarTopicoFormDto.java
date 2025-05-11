package br.com.alura.forum.controller.dto;

import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.TopicoRepository;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AtualizarTopicoFormDto {
    @NotNull
    @NotEmpty
    @Size(min = 5)
    private String titulo;
    @NotNull
    @NotEmpty
    @Size(min = 10)
    private String mensagem;

    public AtualizarTopicoFormDto(String titulo, String mensagem) {
        this.titulo = titulo;
        this.mensagem = mensagem;
    }

    public Topico atualizar(Long id, TopicoRepository topicoRepository) {
        Topico topico = topicoRepository.getOne(id);

        topico.setTitulo(titulo);
        topico.setMensagem(mensagem);

        return topico;
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
}
