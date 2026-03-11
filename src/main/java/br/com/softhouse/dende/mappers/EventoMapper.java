package br.com.softhouse.dende.mappers;

import br.com.softhouse.dende.dto.EventoRequestDTO;
import br.com.softhouse.dende.dto.EventoResponseDTO;
import br.com.softhouse.dende.dto.EventoResumoDTO;
import br.com.softhouse.dende.model.Evento;

// Mapper para converter entre Evento, EventoRequestDTO, EventoResponseDTO e EventoResumoDTO
public class EventoMapper {

    private EventoMapper() {}

    // Converte um EventoRequestDTO para um objeto Evento (entidade)
    public static Evento toEntity(EventoRequestDTO dto, Long organizadorId) {
        if (dto == null) return null;

        // Criar um novo objeto Evento e preencher seus campos com os dados do DTO
        Evento evento = new Evento();
        evento.setOrganizadorId(organizadorId);
        evento.setNome(dto.getNome());
        evento.setPagina(dto.getPagina());
        evento.setDescricao(dto.getDescricao());
        evento.setDataInicio(dto.getDataInicio());
        evento.setDataFinal(dto.getDataFinal());
        evento.setTipoEvento(dto.getTipoEvento());
        evento.setEventoPrincipalId(dto.getEventoPrincipalId());
        evento.setModalidade(dto.getModalidade());
        evento.setCapacidadeMaxima(dto.getCapacidadeMaxima());
        evento.setLocal(dto.getLocal());
        evento.setPrecoIngresso(dto.getPrecoIngresso());
        evento.setEstornaCancelamento(dto.getEstornaCancelamento());
        evento.setTaxaEstorno(dto.getTaxaEstorno());

        return evento;
    }

    // Converte um objeto Evento (entidade) para um EventoResponseDTO
    public static EventoResponseDTO toResponseDTO(Evento evento) {
        if (evento == null) return null;
        return new EventoResponseDTO(evento);
    }

    // Converte um objeto Evento (entidade) para um EventoResumoDTO
    public static EventoResumoDTO toResumoDTO(Evento evento) {
        if (evento == null) return null;
        return new EventoResumoDTO(evento);
    }

    // Atualiza os campos de um objeto Evento com os dados de um EventoRequestDTO (usado para update)
    public static Evento updateEntity(Evento evento, EventoRequestDTO dto) {
        if (dto == null) return evento;

        if (dto.getNome() != null) {
            evento.setNome(dto.getNome());
        }
        if (dto.getPagina() != null) {
            evento.setPagina(dto.getPagina());
        }
        if (dto.getDescricao() != null) {
            evento.setDescricao(dto.getDescricao());
        }
        if (dto.getDataInicio() != null) {
            evento.setDataInicio(dto.getDataInicio());
        }
        if (dto.getDataFinal() != null) {
            evento.setDataFinal(dto.getDataFinal());
        }
        if (dto.getTipoEvento() != null) {
            evento.setTipoEvento(dto.getTipoEvento());
        }
        if (dto.getEventoPrincipalId() != null) {
            evento.setEventoPrincipalId(dto.getEventoPrincipalId());
        }
        if (dto.getModalidade() != null) {
            evento.setModalidade(dto.getModalidade());
        }
        if (dto.getCapacidadeMaxima() != null) {
            evento.setCapacidadeMaxima(dto.getCapacidadeMaxima());
        }
        if (dto.getLocal() != null) {
            evento.setLocal(dto.getLocal());
        }
        if (dto.getPrecoIngresso() != null) {
            evento.setPrecoIngresso(dto.getPrecoIngresso());
        }
        if (dto.getEstornaCancelamento() != null) {
            evento.setEstornaCancelamento(dto.getEstornaCancelamento());
        }
        if (dto.getTaxaEstorno() != null) {
            evento.setTaxaEstorno(dto.getTaxaEstorno());
        }

        return evento;
    }
}
