package br.org.rfcc.saudefem.domain.entity;

import br.org.rfcc.saudefem.domain.enums.TipoTelefone;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "tb_telefones_contato")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TelefoneContato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "numero", nullable = false, length = 25)
    private String numero;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 20)
    @Builder.Default
    private TipoTelefone tipo = TipoTelefone.CELULAR;

    @Column(name = "possui_whatsapp", nullable = false)
    @Builder.Default
    private Boolean possuiWhatsapp = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    public String obterNumeroSomenteDigitos() {
        if (numero == null) {
            return "";
        }
        String digitos = numero.replaceAll("\\D", "");
        return digitos.startsWith("55") ? digitos : "55" + digitos;
    }
}
