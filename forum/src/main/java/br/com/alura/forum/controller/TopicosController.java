package br.com.alura.forum.controller;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import br.com.alura.forum.controller.dto.AtualizarTopicoFormDto;
import br.com.alura.forum.controller.dto.DetalhesTopicoDto;
import br.com.alura.forum.controller.dto.TopicoFormDto;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.modelo.Topico;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

	@Autowired
	private TopicoRepository repository;

	@Autowired
	private CursoRepository cursoRepository;

	@GetMapping
	@Cacheable(value = "listaTopicos")
	public Page<TopicoDto> lista(@RequestParam(required = false) String nomeCurso,
		@PageableDefault(sort = "id", direction = Sort.Direction.ASC, size = 10) Pageable pageable) {

		if (Objects.isNull(nomeCurso)) {
			Page<Topico> topicos = repository.findAll(pageable);
			return TopicoDto.converter(topicos);
		}
		Page<Topico> topicos = repository.findByCursoNome(nomeCurso, pageable);
		return TopicoDto.converter(topicos);
	}

	@PostMapping
	@CacheEvict(value = "listaTopicos", allEntries = true)
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoFormDto topicoFormDto, UriComponentsBuilder uriBuilder) {
		Topico topico = repository.save(topicoFormDto.converter(cursoRepository));
		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
	}

	@GetMapping("/{id}")
	public DetalhesTopicoDto detalhar(@PathVariable Long id) {
		Optional<Topico> topico = repository.findById(id);
        return topico.map(DetalhesTopicoDto::new).orElse(null);
    }

	@PatchMapping("/{id}")
	@Transactional
	public 	ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizarTopicoFormDto dto) {
		Topico topico = dto.atualizar(id, repository);

		return ResponseEntity.status(HttpStatus.OK).body(new TopicoDto(topico));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Boolean> deletar(@PathVariable Long id) {
		Optional<Topico> topico = repository.findById(id);
		if(topico.isPresent()) {
			repository.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).body(Boolean.TRUE);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Boolean.FALSE);
	}
}
