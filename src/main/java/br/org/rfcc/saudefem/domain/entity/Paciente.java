package br.org.rfcc.saudefem.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "tb_pacientes",
    indexes = {
        @Index(name = "idx_paciente_cns", columnList = "cns"),
        @Index(name = "idx_paciente_nome", columnList = "nome_completo")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "nome_completo", nullable = false, length = 150)
    private String nomeCompleto;

    @Column(name = "tratamento", length = 20)
    private String tratamento; // Sra., Srta., Dra., etc.

    @Column(name = "genero", length = 30)
    @Builder.Default
    private String genero = "Feminino";

    @NotNull
    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "estado_civil", length = 40)
    private String estadoCivil;

    @Column(name = "cutis", length = 30)
    private String cutis;

    @Column(name = "naturalidade", length = 100)
    private String naturalidade;

    @Column(name = "escolaridade", length = 60)
    private String escolaridade;

    @NotBlank
    @Column(name = "cpf", nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(name = "cns", length = 25)
    private String cns; // Cartão Nacional de Saúde (SUS)

    @Column(name = "data_cadastro", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime dataCadastro = LocalDateTime.now();

    // Relacionamento 1:1 com Endereco
    @OneToOne(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Endereco endereco;

    // Relacionamento 1:N com TelefoneContato
    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<TelefoneContato> telefones = new ArrayList<>();

    // Relacionamento 1:1 com DadosRecepcao
    @OneToOne(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private DadosRecepcao dadosRecepcao;

    // Relacionamento 1:1 com Anamnese
    @OneToOne(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Anamnese anamnese;

    // Relacionamento 1:N com Agendamento (sem REMOVE: o histórico de atendimentos é preservado)
    @OneToMany(mappedBy = "paciente", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Agendamento> agendamentos = new ArrayList<>();

    public int calcularIdade() {
        if (this.dataNascimento == null) {
            return 0;
        }
        return Period.between(this.dataNascimento, LocalDate.now()).getYears();
    }

    public void adicionarTelefone(TelefoneContato telefone) {
        telefone.setPaciente(this);
        this.telefones.add(telefone);
    }

    public void definirEndereco(Endereco novoEndereco) {
        novoEndereco.setPaciente(this);
        this.endereco = novoEndereco;
    }

    public void definirDadosRecepcao(DadosRecepcao recepcao) {
        recepcao.setPaciente(this);
        this.dadosRecepcao = recepcao;
    }

    public void definirAnamnese(Anamnese novaAnamnese) {
        novaAnamnese.setPaciente(this);
        this.anamnese = novaAnamnese;
    }
}
