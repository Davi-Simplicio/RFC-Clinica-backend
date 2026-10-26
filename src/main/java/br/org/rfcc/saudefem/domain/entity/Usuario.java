package br.org.rfcc.saudefem.domain.entity;

import br.org.rfcc.saudefem.domain.enums.RolePerfil;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "nome_completo", nullable = false, length = 120)
    private String nomeCompleto;

    @Email
    @NotBlank
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @NotBlank
    @Column(name = "senha_hash", nullable = false, length = 255)
    private String senhaHash;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "perfil", nullable = false, length = 30)
    private RolePerfil perfil;

    @Column(name = "registro_profissional", length = 40)
    private String registroProfissional; // Ex: CRM/SC 12345 ou COREN/SC 54321

    @Column(name = "especialidade", length = 80)
    private String especialidade; // Ex: Ginecologista / Mastologista

    @Column(name = "ativo", nullable = false)
    @Builder.Default
    private Boolean ativo = true;

    @Column(name = "criado_em", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime criadoEm = LocalDateTime.now();

    @OneToMany(mappedBy = "profissionalResponsavel", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Agendamento> agendamentos = new ArrayList<>();

    public boolean autenticar(String senhaInformadaHash) {
        return Boolean.TRUE.equals(this.ativo) && this.senhaHash.equals(senhaInformadaHash);
    }

    public boolean possuiPermissaoClinica() {
        return this.perfil == RolePerfil.MEDICA || this.perfil == RolePerfil.ENFERMEIRA;
    }
}
