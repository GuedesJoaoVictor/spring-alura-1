package br.com.alura.forum.controller;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import br.com.alura.forum.controller.dto.TopicoFormDto;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.modelo.Curso;
import br.com.alura.forum.modelo.Topico;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

@Controller
@RequestMapping("/topicos")
public class TopicosController {

	@Autowired
	private TopicoRepository repository;

	@Autowired
	private CursoRepository cursoRepository;

	@GetMapping
	public List<TopicoDto> lista(String nomeCurso) {
		List<Topico> topicos = repository.findAll();
		if (Objects.isNull(nomeCurso)) {
			return TopicoDto.converter(topicos);
		}
		topicos = repository.findByCursoNome(nomeCurso);
		return TopicoDto.converter(topicos);
	}

	@PostMapping
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoFormDto topicoFormDto, UriComponentsBuilder uriBuilder) {
		Topico topico = repository.save(topicoFormDto.converter(cursoRepository));
		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
	}

}
