package service;

import client.SolicitacaoClient;
import dto.*;
import entity.ListaEspera;
import entity.MatriculaTurma;
import entity.Turma;
import entity.enums.StatusMatricula;
import entity.enums.StatusSolicitacao;
import entity.enums.StatusTurma;
import entity.enums.TurnoEnum;
import exception.ResourceNotFoundException;
import repository.ListaEsperaRepository;
import repository.MatriculaTurmaRepository;
import repository.TurmaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TurmaService {

    private final TurmaRepository turmaRepository;
    private final MatriculaTurmaRepository matriculaTurmaRepository;
    private final ListaEsperaRepository listaEsperaRepository;
    private final SolicitacaoClient solicitacaoClient;

    public TurmaService(TurmaRepository turmaRepository, 
                        MatriculaTurmaRepository matriculaTurmaRepository, 
                        ListaEsperaRepository listaEsperaRepository, 
                        SolicitacaoClient solicitacaoClient) {
        this.turmaRepository = turmaRepository;
        this.matriculaTurmaRepository = matriculaTurmaRepository;
        this.listaEsperaRepository = listaEsperaRepository;
        this.solicitacaoClient = solicitacaoClient;
    }

    @Transactional
    public void processarFormacaoTurmas() {
    	List<SolicitacaoResponseDTO> solicitacoesGerais =
    	        solicitacaoClient.obterPendentes(
    	                "ADMINISTRADOR",
    	                1L
    	        );

        if (solicitacoesGerais == null || solicitacoesGerais.isEmpty()) {
            return;
        }

        List<FlatSolicitacaoDTO> demandasMapeadas = new ArrayList<>();
        for (SolicitacaoResponseDTO sol : solicitacoesGerais) {
            if (sol.disciplinas() != null) {
                for (ItemSolicitacaoResponseDTO item : sol.disciplinas()) {
                    demandasMapeadas.add(new FlatSolicitacaoDTO(
                            sol.id(),
                            sol.alunoId(),
                            item.disciplinaId(),
                            item.prioridade(),
                            sol.dataSolicitacao(),
                            item.turnos(),
                            sol.semestre()
                    ));
                }
            }
        }

        Map<Long, List<FlatSolicitacaoDTO>> porDisciplina = demandasMapeadas.stream()
                .collect(Collectors.groupingBy(FlatSolicitacaoDTO::disciplinaId));

        for (Map.Entry<Long, List<FlatSolicitacaoDTO>> entry : porDisciplina.entrySet()) {
            Long disciplinaId = entry.getKey();
            List<FlatSolicitacaoDTO> demandas = entry.getValue();

            demandas.sort(new Comparator<FlatSolicitacaoDTO>() {
                @Override
                public int compare(FlatSolicitacaoDTO d1, FlatSolicitacaoDTO d2) {
                    int compPrioridade = Integer.compare(d1.prioridade(), d2.prioridade());
                    if (compPrioridade != 0) {
                        return compPrioridade;
                    }
                    return d1.dataSolicitacao().compareTo(d2.dataSolicitacao());
                }
            });

            for (FlatSolicitacaoDTO dema : demandas) {
                if (isAlunoMatriculadoOuEmEspera(dema.alunoId(), disciplinaId)) {
                    continue; 
                }

                List<Turma> turmasAbertas = turmaRepository.findByDisciplinaIdAndStatus(disciplinaId, StatusTurma.ABERTA);
                boolean alocado = false;

                for (Turma turma : turmasAbertas) {
                    // CORREÇÃO: Removido prefixo "get" do método da Entidade
                    if (turma.quantidadeAlunos() < 20) {
                    	efetuarMatricula(
                    	        turma,
                    	        dema,
                    	        dema.solicitacaoId()
                    	);
                        alocado = true;
                        break;
                    }
                }

                if (!alocado) {
                    Long professorId = encontrarProfessorDisponivel(disciplinaId);
                    if (professorId != null) {
                        TurnoEnum turnoDefinido = (dema.turnos() != null && !dema.turnos().isEmpty())
                                ? dema.turnos().iterator().next() : TurnoEnum.MATUTINO;

                        Turma novaTurma = new Turma(
                                null,
                                disciplinaId,
                                professorId,
                                1L, // espaço provisório
                                dema.semestre(),
                                turnoDefinido,
                                20,
                                0,
                                StatusTurma.ABERTA
                        );

                        novaTurma = turmaRepository.save(novaTurma);
                        efetuarMatricula(
                                novaTurma,
                                dema,
                                dema.solicitacaoId()
                        );
                    } else {
                    	rejeitarSolicitacao(
                    	        dema.solicitacaoId()
                    	);
                    }
                }
            }

            validarQuorumMinimo(disciplinaId);
        }
    }

    @Transactional
    public void cancelarMatricula(Long turmaId, Long alunoId) {
        Turma turma = turmaRepository.findById(turmaId)
                .orElseThrow(() -> new ResourceNotFoundException("Turma não localizada: ID " + turmaId));

        MatriculaTurma matricula = matriculaTurmaRepository.findByTurmaIdAndAlunoIdAndStatus(turmaId, alunoId, StatusMatricula.MATRICULADO)
                .orElseThrow(() -> new ResourceNotFoundException("Matrícula ativa não encontrada para este aluno."));

        matricula.setStatus(StatusMatricula.CANCELADO);
        matriculaTurmaRepository.save(matricula);

        // CORREÇÃO: Métodos modificados para o padrão de atribuição direta/métodos record
        turma.setQuantidadeAlunos(turma.quantidadeAlunos() - 1);
        if (turma.status() == StatusTurma.FECHADA && turma.quantidadeAlunos() < 20) {
            turma.setStatus(StatusTurma.ABERTA);
        }
        turmaRepository.save(turma);

        Optional<ListaEspera> topoFilaOpt = listaEsperaRepository.findFirstByDisciplinaIdOrderByPosicaoFilaAsc(turma.disciplinaId());

        if (topoFilaOpt.isPresent()) {
            ListaEspera proximo = topoFilaOpt.get();
            listaEsperaRepository.delete(proximo);
            reajustarFilaEspera(turma.disciplinaId());

            MatriculaTurma novaMatricula = new MatriculaTurma(
                    null,
                    turma.id(),
                    proximo.alunoId(),
                    proximo.prioridade(),
                    LocalDateTime.now(),
                    StatusMatricula.MATRICULADO
            );
            matriculaTurmaRepository.save(novaMatricula);

            turma.setQuantidadeAlunos(turma.quantidadeAlunos() + 1);
            if (turma.quantidadeAlunos() == 20) {
                turma.setStatus(StatusTurma.FECHADA);
            }
            turmaRepository.save(turma);
        } else {
            if (turma.quantidadeAlunos() < 10) {
                cancelarTurmaPorFaltaDeQuorum(turma);
            }
        }
    }

    public List<TurmaResponseDTO> listarTurmas() {
        return turmaRepository.findAll().stream()
                .map(t -> new TurmaResponseDTO(
                        t.id(),
                        t.disciplinaId(),
                        t.professorId(),
                        t.espacoId(),
                        t.semestre(),
                        t.turno(),
                        t.capacidadeMaxima(),
                        t.quantidadeAlunos(),
                        t.status()
                ))
                .collect(Collectors.toList());
    }

    public TurmaResponseDTO buscarPorId(Long id) {
        Turma t = turmaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Turma não encontrada."));
        return new TurmaResponseDTO(
                t.id(),
                t.disciplinaId(),
                t.professorId(),
                t.espacoId(),
                t.semestre(),
                t.turno(),
                t.capacidadeMaxima(),
                t.quantidadeAlunos(),
                t.status()
        );
    }

    public TurmaResponseDTO criar(TurmaResponseDTO dto) {
        Turma nova = new Turma(
                null,
                dto.disciplinaId(),
                dto.professorId(),
                dto.espacoId() != null ? dto.espacoId() : 1L,
                dto.semestre(),
                dto.turno() != null ? dto.turno() : TurnoEnum.MATUTINO,
                dto.capacidadeMaxima() != null ? dto.capacidadeMaxima() : 20,
                0,
                StatusTurma.ABERTA
        );
        nova = turmaRepository.save(nova);
        return buscarPorId(nova.id());
    }

    public TurmaResponseDTO atualizar(
            Long id,
            TurmaResponseDTO dto
    ) {

        Turma turma = turmaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Turma não encontrada."
                        ));

        turma.setProfessorId(dto.professorId());
        turma.setEspacoId(dto.espacoId());
        turma.setTurno(dto.turno());
        turma.setStatus(dto.status());

        turmaRepository.save(turma);

        return buscarPorId(id);
    }
    
    public void excluir(Long id) {

        Turma turma = turmaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Turma não encontrada."
                        ));

        turmaRepository.delete(turma);
    }
    
    
    public List<ListaEsperaResponseDTO> buscarFilaPorDisciplina(Long disciplinaId) {
        return listaEsperaRepository.findByDisciplinaIdOrderByPosicaoFilaAsc(disciplinaId).stream()
                .map(l -> new ListaEsperaResponseDTO(l.id(), l.getDisciplinaId(), l.alunoId(), l.prioridade(), l.posicaoFila(), l.dataEntrada()))
                .collect(Collectors.toList());
    }

    private void efetuarMatricula(
            Turma turma,
            FlatSolicitacaoDTO dema,
            Long solicitacaoId
    ) {

        MatriculaTurma m = new MatriculaTurma(
                null,
                turma.id(),
                dema.alunoId(),
                dema.prioridade(),
                LocalDateTime.now(),
                StatusMatricula.MATRICULADO
        );

        matriculaTurmaRepository.save(m);

        turma.setQuantidadeAlunos(
                turma.quantidadeAlunos() + 1
        );

        if (turma.quantidadeAlunos() == 20) {
            turma.setStatus(StatusTurma.FECHADA);
        }

        turmaRepository.save(turma);

        if (solicitacaoId != null) {

            solicitacaoClient.atualizarStatus(
                    solicitacaoId,
                    "ADMINISTRADOR",
                    1L,
                    new AtualizarStatusRequestDTO(
                            StatusSolicitacao.VALIDADO.name()
                    )
            );
        }
    }
    
    private void adicionarListaEspera(
            FlatSolicitacaoDTO dema,
            Long solicitacaoId
    ) {

        if (
                listaEsperaRepository.existsByDisciplinaIdAndAlunoId(
                        dema.disciplinaId(),
                        dema.alunoId()
                )
        ) {
            return;
        }

        int posicao =
                (int) listaEsperaRepository.countByDisciplinaId(
                        dema.disciplinaId()
                ) + 1;

        ListaEspera l = new ListaEspera(
                null,
                dema.disciplinaId(),
                dema.alunoId(),
                dema.prioridade(),
                posicao,
                dema.dataSolicitacao()
        );

        listaEsperaRepository.save(l);

        reajustarFilaEspera(
                dema.disciplinaId()
        );

        if (solicitacaoId != null) {

            solicitacaoClient.atualizarStatus(
                    solicitacaoId,
                    "ADMINISTRADOR",
                    1L,
                    new AtualizarStatusRequestDTO(
                            StatusSolicitacao.LISTA_DE_ESPERA.name()
                    )
            );
        }
    }
    
    private void rejeitarSolicitacao(Long solicitacaoId) {

        if (solicitacaoId == null) {
            return;
        }

        solicitacaoClient.atualizarStatus(
                solicitacaoId,
                "ADMINISTRADOR",
                1L,
                new AtualizarStatusRequestDTO(
                        StatusSolicitacao.REJEITADO.name()
                )
        );
    }
    

    private void validarQuorumMinimo(Long disciplinaId) {
        List<Turma> turmasAtivas = turmaRepository.findByDisciplinaIdAndStatus(disciplinaId, StatusTurma.ABERTA);
        for (Turma t : turmasAtivas) {
            if (t.quantidadeAlunos() < 10) {
                cancelarTurmaPorFaltaDeQuorum(t);
            }
        }
    }

    private void cancelarTurmaPorFaltaDeQuorum(Turma turma) {
        turma.setStatus(StatusTurma.CANCELADA);
        turmaRepository.save(turma);

        List<MatriculaTurma> matriculas = matriculaTurmaRepository.findByTurmaIdAndStatus(turma.id(), StatusMatricula.MATRICULADO);
        for (MatriculaTurma m : matriculas) {
            m.setStatus(StatusMatricula.CANCELADO);
            matriculaTurmaRepository.save(m);

            FlatSolicitacaoDTO reAlocacao = new FlatSolicitacaoDTO(
                    null,
                    m.alunoId(),
                    turma.disciplinaId(),
                    m.prioridade(),
                    m.dataMatricula(),
                    Set.of(turma.turno()),
                    turma.semestre()
            );
            adicionarListaEspera(
                    reAlocacao,
                    null
            );
        }
        turma.setQuantidadeAlunos(0);
        turmaRepository.save(turma);
    }

    private Long encontrarProfessorDisponivel(Long disciplinaId) {
        for (long profId = 101; profId <= 110; profId++) {
            long totalTurmasAtivasDoProf = turmaRepository.countByDisciplinaIdAndProfessorIdAndStatusNot(disciplinaId, profId, StatusTurma.CANCELADA);
            if (totalTurmasAtivasDoProf < 2) {
                return profId;
            }
        }
        return null;
    }

    private boolean isAlunoMatriculadoOuEmEspera(Long alunoId, Long disciplinaId) {
        if (listaEsperaRepository.existsByDisciplinaIdAndAlunoId(disciplinaId, alunoId)) {
            return true;
        }
        List<Turma> turmasAtivas = turmaRepository.findByDisciplinaIdAndStatus(disciplinaId, StatusTurma.ABERTA);
        turmasAtivas.addAll(turmaRepository.findByDisciplinaIdAndStatus(disciplinaId, StatusTurma.FECHADA));

        if (turmasAtivas.isEmpty()) return false;

        List<Long> turmaIds = turmasAtivas.stream().map(Turma::id).collect(Collectors.toList());
        return matriculaTurmaRepository.existsByAlunoIdAndStatusAndTurmaIdIn(alunoId, StatusMatricula.MATRICULADO, turmaIds);
    }

    private void reajustarFilaEspera(Long disciplinaId) {
        List<ListaEspera> fila = listaEsperaRepository.findByDisciplinaIdOrderByPosicaoFilaAsc(disciplinaId);
        
        fila.sort(new Comparator<ListaEspera>() {
            @Override
            public int compare(ListaEspera l1, ListaEspera l2) {
                int compPrioridade = Integer.compare(l1.prioridade(), l2.prioridade());
                if (compPrioridade != 0) {
                    return compPrioridade;
                }
                return l1.dataEntrada().compareTo(l2.dataEntrada());
            }
        });

        for (int i = 0; i < fila.size(); i++) {
            fila.get(i).setPosicaoFila(i + 1);
        }
        listaEsperaRepository.saveAll(fila);
    }
}