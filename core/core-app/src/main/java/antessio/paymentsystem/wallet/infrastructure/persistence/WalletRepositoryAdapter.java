package antessio.paymentsystem.wallet.infrastructure.persistence;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.f4b6a3.ulid.Ulid;

import antessio.paymentsystem.wallet.MovementId;
import antessio.paymentsystem.wallet.WalletID;
import antessio.paymentsystem.wallet.WalletOwnerId;
import antessio.paymentsystem.wallet.application.WalletRepository;
import antessio.paymentsystem.wallet.domain.Movement;
import antessio.paymentsystem.wallet.domain.MovementAdapter;
import antessio.paymentsystem.wallet.domain.Wallet;
import antessio.paymentsystem.wallet.domain.WalletsUpdate;

@Component
public class WalletRepositoryAdapter implements WalletRepository {

    public static final int BATCH_SIZE = 20;
    private final MovementRepository movementRepository;
    private final antessio.paymentsystem.wallet.infrastructure.persistence.WalletRepository walletRepository;

    public WalletRepositoryAdapter(
            MovementRepository movementRepository,
            antessio.paymentsystem.wallet.infrastructure.persistence.WalletRepository walletRepository) {
        this.movementRepository = movementRepository;
        this.walletRepository = walletRepository;
    }

    @Override
    public Optional<Wallet> loadWalletById(WalletID id) {
        return walletRepository.findById(id.getId())
                               .map(WalletAdapter::new);
    }

    @Override
    @Transactional
    public List<MovementId> updateWallet(WalletsUpdate walletsUpdate) {
        MovementId sourceMovementId = insertMovement(walletsUpdate.getSourceWalletMovement());
        MovementId destinationMovementId = insertMovement(walletsUpdate.getDestinationWalletMovement());
        updateWallet(walletsUpdate.getSourceWallet());
        updateWallet(walletsUpdate.getDestinationWallet());
        return List.of(sourceMovementId, destinationMovementId);
    }


    private MovementId insertMovement(Movement movement) {

        MovementEntity movementInserted
                = movementRepository.save(newMovementEntity(movement));
        return new MovementId(movementInserted.getId());
    }

    private MovementEntity newMovementEntity(Movement movement) {

        return new MovementEntity(
                Ulid.from(UUID.randomUUID()).toString(),
                movement.getDirection().name(),
                movement.getAmount().getAmountUnit(),
                movement.getAmount().getCurrency(),
                new WalletEntity(movement.getWalletId().getId()),
                movement.getOperationId(),
                new java.util.Date(),
                null,
                0L);
    }

    private void updateWallet(Wallet wallet) {
        walletRepository.save(
                new WalletEntity(
                        wallet.getId().getId(),
                        wallet.getOwner().name(),
                        wallet.getOwnerId().getId(),
                        wallet.getType().name(),
                        wallet.getAmountUnit(),
                        Date.from(wallet.getInsertOn()),
                        Optional.ofNullable(wallet.getUpdatedOn())
                                .map(Date::from)
                                .orElse(null),
                        wallet.getVersion()));
    }

    @Override
    public WalletID insertWallet(Wallet wallet) {
        WalletEntity walletEntity = walletRepository.save(
                new WalletEntity(
                        wallet.getId().getId(),
                        wallet.getOwner().name(),
                        wallet.getOwnerId().getId(),
                        wallet.getType().name(),
                        wallet.getAmountUnit(),
                        Optional.ofNullable(wallet.getInsertOn())
                                .map(Date::from)
                                .orElseGet(Date::new),
                        null,
                        0L));
        return new WalletID(walletEntity.getId());
    }

    @Override
    public List<Wallet> loadWalletByOwnerId(WalletOwnerId ownerId) {
        return walletRepository.findByOwnerId(ownerId.getId())
                               .stream()
                               .map(WalletAdapter::new)
                               .collect(Collectors.toList());
    }

    @Override
    public Optional<Movement> loadTransferById(MovementId movementId) {
        return movementRepository.findById(movementId.getId())
                                 .map(MovementAdapter::new);
    }

    @Override
    public Stream<Movement> loadTransfersByWalletId(WalletID walletID, MovementId cursor) {

        return Stream.iterate(
                             fetchMovement(walletID, cursor.getId(), BATCH_SIZE),
                             List::isEmpty,
                             l -> fetchMovement(walletID, l.get(l.size() - 1).getId(), BATCH_SIZE))
                     .flatMap(List::stream)
                     .map(MovementAdapter::new);
    }

    @Override
    public List<Movement> loadTransfersByOperationId(String operationId) {
        return movementRepository.findByOperationId(operationId)
                                 .stream()
                                 .map(MovementAdapter::new)
                                 .collect(Collectors.toList());
    }

    private List<MovementEntity> fetchMovement(WalletID walletID, String cursor, int size) {
        return movementRepository.findByWalletIdStartingFrom(walletID.getId(), cursor, size);
    }

}
