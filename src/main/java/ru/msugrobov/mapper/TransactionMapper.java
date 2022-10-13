package ru.msugrobov.mapper;

import ru.msugrobov.DTO.TransactionDTO;
import ru.msugrobov.entities.Transaction;

/**
 * Mapper for TransactionDTO and Transaction entity conversion
 * {@inheritDoc}
 */
public class TransactionMapper implements MapperInterface<Transaction, TransactionDTO> {

    /**
     * Converts entity to dto
     *
     * @param transaction entity to convert
     * @return dto from the entity
     */
    public TransactionDTO dtoFromEntity(Transaction transaction) {
        return new TransactionDTO(transaction.getId(), transaction.getWalletId(), transaction.getType(),
                transaction.getValue());
    }

    /**
     * Converts dto to entity
     *
     * @param transactionDTO dto to convert
     * @return entity from the dto
     */
    public Transaction entityFromDto(TransactionDTO transactionDTO) {
        return new Transaction(transactionDTO.getId(), transactionDTO.getWalletId(), transactionDTO.getType(),
                transactionDTO.getValue());
    }
}
