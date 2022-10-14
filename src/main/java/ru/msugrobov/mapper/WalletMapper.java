package ru.msugrobov.mapper;

import ru.msugrobov.DTO.WalletDTO;
import ru.msugrobov.entities.Wallet;

/**
 * Mapper for WalletDTO and Wallet entity conversion
 * {@inheritDoc}
 */
public class WalletMapper implements MapperInterface<Wallet, WalletDTO> {

    /**
     * Converts entity to dto
     *
     * @param wallet entity to convert
     * @return dto from the entity
     */
    public WalletDTO dtoFromEntity(Wallet wallet) {
        return new WalletDTO(wallet.getId(), wallet.getPlayerId(), wallet.getBalance());
    }

    /**
     * Converts dto to entity
     *
     * @param walletDTO dto to convert
     * @return entity from the dto
     */
    public Wallet entityFromDto(WalletDTO walletDTO) {
        return new Wallet(walletDTO.getId(), walletDTO.getPlayerId(), walletDTO.getBalance());
    }
}