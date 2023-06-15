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

import antessio.paymentsystem.wallet.TransferId;
import antessio.paymentsystem.wallet.WalletID;
import antessio.paymentsystem.wallet.WalletOwnerId;
import antessio.paymentsystem.wallet.application.WalletRepository;
import antessio.paymentsystem.wallet.domain.Transfer;
import antessio.paymentsystem.wallet.domain.TransferAdapter;
import antessio.paymentsystem.wallet.domain.Wallet;
import antessio.paymentsystem.wallet.domain.WalletsUpdate;

@Component
public class WalletRepositoryAdapter implements WalletRepository {

    public static final int BATCH_SIZE = 20;
    private final TransferRepository transferRepository;
    private final antessio.paymentsystem.wallet.infrastructure.persistence.WalletRepository walletRepository;

    public WalletRepositoryAdapter(
            TransferRepository transferRepository,
            antessio.paymentsystem.wallet.infrastructure.persistence.WalletRepository walletRepository) {
        this.transferRepository = transferRepository;
        this.walletRepository = walletRepository;
    }

    @Override
    public Optional<Wallet> loadWalletById(WalletID id) {
        return walletRepository.findById(id.getId())
                               .map(WalletAdapter::new);
    }

    @Override
    @Transactional
    public List<TransferId> updateWallet(WalletsUpdate walletsUpdate) {
        TransferId sourceTransferId = insertMovement(walletsUpdate.getSourceWalletMovement());
        TransferId destinationTransferId = insertMovement(walletsUpdate.getDestinationWalletMovement());
        updateWallet(walletsUpdate.getSourceWallet());
        updateWallet(walletsUpdate.getDestinationWallet());
        return List.of(sourceTransferId, destinationTransferId);
    }


    private TransferId insertMovement(Transfer transfer) {

        TransferEntity movementInserted
                = transferRepository.save(newMovementEntity(transfer));
        return new TransferId(movementInserted.getId());
    }

    private TransferEntity newMovementEntity(Transfer transfer) {

        return new TransferEntity(
                Ulid.from(UUID.randomUUID()).toString(),
                transfer.getDirection().name(),
                transfer.getAmount().getAmountUnit(),
                transfer.getAmount().getCurrency(),
                new WalletEntity(transfer.getWalletId().getId()),
                transfer.getOperationId(),
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
        return walletRepository.loadWalletByOwnerId(ownerId.getId())
                               .stream()
                               .map(WalletAdapter::new)
                               .collect(Collectors.toList());
    }

    @Override
    public Optional<Transfer> loadTransferById(TransferId transferId) {
        return transferRepository.findById(transferId.getId())
                                 .map(TransferAdapter::new);
    }

    @Override
    public Stream<Transfer> loadTransfersByWalletId(WalletID walletID, TransferId cursor) {

        return Stream.iterate(
                             fetchMovement(walletID, cursor.getId(), BATCH_SIZE),
                             List::isEmpty,
                             l -> fetchMovement(walletID, l.get(l.size() - 1).getId(), BATCH_SIZE))
                     .flatMap(List::stream)
                     .map(TransferAdapter::new);
    }

    @Override
    public List<Transfer> loadTransfersByOperationId(String operationId) {
        return transferRepository.findByOperationId(operationId)
                                 .stream()
                                 .map(TransferAdapter::new)
                                 .collect(Collectors.toList());
    }

    private List<TransferEntity> fetchMovement(WalletID walletID, String cursor, int size) {
        return transferRepository.findByWalletIdStartingFrom(walletID.getId(), cursor, size);
    }

}
