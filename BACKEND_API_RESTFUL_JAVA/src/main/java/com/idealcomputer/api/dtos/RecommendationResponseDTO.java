package com.idealcomputer.api.dtos;

import com.idealcomputer.api.models.*;
import lombok.Data;

// Objeto de resposta contendo a sugestão completa de hardware.
// Resolve o problema de entrega da configuração sugerida, agrupando todas as
// entidades de hardware em um único pacote para exibição detalhada no frontend.
@Data
public class RecommendationResponseDTO {

    // Componentes selecionados pelo algoritmo de recomendação.
    // O retorno das Models completas permite que o frontend exiba fichas técnicas
    // detalhadas sem a necessidade de novas chamadas à API.
    private CpuModel cpu;
    private PlacaMaeModel placaMae;
    private GpuModel gpu;
    private MemoriaRamModel memoriaRam;
    private ArmazenamentoModel armazenamento;
    private FonteModel fonte;
    private GabineteModel gabinete;
    private RefrigeracaoModel refrigeracao;
}