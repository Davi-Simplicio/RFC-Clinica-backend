package br.org.rfcc.saudefem.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "tb_enderecos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "cep", nullable = false, length = 10)
    private String cep;

    @NotBlank
    @Column(name = "logradouro", nullable = false, length = 150)
    private String logradouro;

    @Column(name = "numero", length = 20)
    private String numero;

    @Column(name = "complemento", length = 100)
    private String complemento;

    @NotBlank
    @Column(name = "bairro", nullable = false, length = 100)
    private String bairro;

    @NotBlank
    @Column(name = "cidade", nullable = false, length = 100)
    private String cidade;

    @NotBlank
    @Column(name = "estado", nullable = false, length = 2)
    private String estado;

    @Column(name = "codigo_ibge", length = 15)
    private String codigoIbge;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false, unique = true)
    private Paciente paciente;

    public String formatarEnderecoCompleto() {
        String comp = (complemento != null && !complemento.isBlank()) ? " - " + complemento : "";
        return String.format("%s, %s%s - %s, %s/%s (CEP: %s)",
                logradouro, numero, comp, bairro, cidade, estado, cep);
    }
}
