package br.org.rfcc.saudefem.repository;

import br.org.rfcc.saudefem.domain.entity.*;
import br.org.rfcc.saudefem.domain.enums.PeriodoTurno;
import br.org.rfcc.saudefem.domain.enums.RolePerfil;
import br.org.rfcc.saudefem.domain.enums.StatusAgendamento;
import br.org.rfcc.saudefem.domain.enums.TipoTelefone;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class PersistenciaJpaTest {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired private TestEntityManager em;
    @Autowired private PacienteRepository pacienteRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private ProcedimentoRepository procedimentoRepository;
    @Autowired private AgendamentoRepository agendamentoRepository;
    @Autowired private TelefoneContatoRepository telefoneContatoRepository;
    @Autowired private AnamneseRepository anamneseRepository;

    private Usuario novaMedica() {
        return usuarioRepository.save(Usuario.builder()
                .nomeCompleto("Dra. Ana Beatriz")
                .email("ana.beatriz@rfcc.org.br")
                .senhaHash(encoder.encode("senha-teste"))
                .perfil(RolePerfil.MEDICA)
                .build());
    }

    private Paciente novaPaciente(String cpf) {
        Paciente paciente = Paciente.builder()
                .nomeCompleto("Maria Aparecida da Silva")
                .dataNascimento(LocalDate.of(1980, 5, 12))
                .cpf(cpf)
                .build();
        paciente.definirEndereco(Endereco.builder()
                .cep("89251-000").logradouro("Rua Exemplo").numero("100")
                .bairro("Centro").cidade("Jaraguá do Sul").estado("SC").build());
        paciente.adicionarTelefone(TelefoneContato.builder()
                .numero("(47) 9 9123-4567").tipo(TipoTelefone.CELULAR).build());
        paciente.definirDadosRecepcao(DadosRecepcao.builder().planoSaude("SUS").build());
        paciente.definirAnamnese(Anamnese.builder()
                .partosNormais(2).partosCesaria(1).teveAborto(true).build());
        return pacienteRepository.save(paciente);
    }

    private Procedimento novoProcedimento(String codigo) {
        return procedimentoRepository.save(Procedimento.builder()
                .codigo(codigo).nome("Exame de Mama e Preventivo").build());
    }

    private Agendamento novoAgendamento(String protocolo, Paciente paciente, Usuario medica, Procedimento proc) {
        Agendamento agendamento = Agendamento.builder()
                .protocolo(protocolo)
                .dataAtendimento(LocalDate.of(2026, 10, 27))
                .horario(LocalTime.of(7, 40))
                .periodo(PeriodoTurno.MANHA)
                .paciente(paciente)
                .profissionalResponsavel(medica)
                .build();
        if (proc != null) {
            agendamento.adicionarProcedimento(proc);
        }
        return agendamento;
    }

    @Test
    void devePersistirAgregadoDaPacienteComCascata() {
        Paciente salva = novaPaciente("123.456.789-00");
        em.flush();
        em.clear();

        Paciente carregada = pacienteRepository.findByCpf("123.456.789-00").orElseThrow();
        assertThat(carregada.getEndereco().getCidade()).isEqualTo("Jaraguá do Sul");
        assertThat(carregada.getTelefones()).hasSize(1);
        assertThat(carregada.getDadosRecepcao().isAtendimentoSus()).isTrue();
        assertThat(carregada.getAnamnese().calcularTotalPartos()).isEqualTo(3);
        assertThat(carregada.getAnamnese().getDataAtualizacao()).isNotNull();
        assertThat(telefoneContatoRepository.findFirstByPacienteIdAndPossuiWhatsappTrue(salva.getId())).isPresent();
        assertThat(anamneseRepository.findByTeveAbortoTrue()).hasSize(1);
    }

    @Test
    void devePersistirAgendamentoComRelacionamentosNM() {
        Paciente paciente = novaPaciente("111.222.333-44");
        Usuario medica = novaMedica();
        Procedimento proc = novoProcedimento("PROC-001");

        agendamentoRepository.save(novoAgendamento("PROT-2026-0001", paciente, medica, proc));
        em.flush();
        em.clear();

        List<Agendamento> agenda = agendamentoRepository.filtrarAgendaDoDia(
                LocalDate.of(2026, 10, 27), StatusAgendamento.MARCADO, "maria");
        assertThat(agenda).hasSize(1);
        Agendamento carregado = agenda.get(0);
        assertThat(carregado.getProcedimentos()).extracting(Procedimento::getCodigo).containsExactly("PROC-001");
        assertThat(carregado.getProfissionalResponsavel().getNomeCompleto()).isEqualTo("Dra. Ana Beatriz");
        assertThat(carregado.gerarMensagemConfirmacaoWhatsApp()).contains("Maria", "PROT-2026-0001");
    }

    @Test
    void naoDevePermitirAgendamentoSemProcedimento() {
        Paciente paciente = novaPaciente("555.666.777-88");
        Usuario medica = novaMedica();

        assertThatThrownBy(() -> {
            agendamentoRepository.save(novoAgendamento("PROT-2026-0002", paciente, medica, null));
            em.flush();
        }).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void naoDevePermitirProtocoloDuplicado() {
        Paciente paciente = novaPaciente("999.888.777-66");
        Usuario medica = novaMedica();
        Procedimento proc = novoProcedimento("PROC-002");

        agendamentoRepository.save(novoAgendamento("PROT-2026-0003", paciente, medica, proc));
        assertThatThrownBy(() -> agendamentoRepository.saveAndFlush(
                novoAgendamento("PROT-2026-0003", paciente, medica, proc)))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void deveAutenticarComBCrypt() {
        Usuario medica = novaMedica();

        assertThat(medica.autenticar("senha-teste", encoder)).isTrue();
        assertThat(medica.autenticar("senha-errada", encoder)).isFalse();
        medica.setAtivo(false);
        assertThat(medica.autenticar("senha-teste", encoder)).isFalse();
    }
}
