package ru.msugrobov.mapper;

import ru.msugrobov.DTO.PlayerDTO;
import ru.msugrobov.entities.Player;

/**
 * Mapper for PlayerDTO and Player entity conversion
 * {@inheritDoc}
 */
public class PlayerMapper implements MapperInterface<Player, PlayerDTO> {

    /**
     * Converts entity to dto
     *
     * @param player entity to convert
     * @return dto from the entity
     */
    public PlayerDTO dtoFromEntity(Player player) {
        return new PlayerDTO(player.getId(), player.getFirstName(), player.getLastName(),
                player.getLogin(), player.getPassword(), player.getRole());
    }

    /**
     * Converts dto to entity
     *
     * @param playerDTO dto to convert
     * @return entity from the dto
     */
    public Player entityFromDto(PlayerDTO playerDTO) {
        return new Player(playerDTO.getId(), playerDTO.getFirstName(), playerDTO.getLastName(),
                playerDTO.getLogin(), playerDTO.getPassword(), playerDTO.getRole());
    }
}
