package antessio.paymentsystem.wallet.application;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import antessio.paymentsystem.wallet.TransferId;
import antessio.paymentsystem.wallet.WalletID;
import antessio.paymentsystem.wallet.WalletOwnerId;
import antessio.paymentsystem.wallet.application.WalletRepository;
import antessio.paymentsystem.wallet.domain.Transfer;
import antessio.paymentsystem.wallet.domain.Wallet;
import antessio.paymentsystem.wallet.domain.WalletBuilder;
import antessio.paymentsystem.wallet.domain.WalletsUpdate;

public class InMemoryWalletRepository implements WalletRepository {

    private Map<WalletID, Wallet> wallets = new HashMap<>();
    private Map<WalletID, List<Transfer>> transfersByWalletId = new HashMap<>();
    private Map<TransferId, Transfer> transfers = new HashMap<>();

    @Override
    public Optional<Wallet> loadWalletById(WalletID id) {
        return Optional.ofNullable(wallets.get(id));
    }

    @Override
    public List<TransferId> updateWallet(WalletsUpdate walletsUpdate) {
        TransferId sourceTransferId = insertTransfer(walletsUpdate.getSourceWalletMovement());
        TransferId destinationTransferId = insertTransfer(walletsUpdate.getDestinationWalletMovement());
        updateWallet(wallets.get(walletsUpdate.getSourceWallet().getId()), walletsUpdate.getSourceWallet());
        updateWallet(wallets.get(walletsUpdate.getDestinationWallet().getId()), walletsUpdate.getDestinationWallet());
        return List.of(sourceTransferId, destinationTransferId);
    }


    @Override
    public WalletID insertWallet(Wallet wallet) {
        Wallet newWalletUpdated = WalletBuilder.aWallet()
                                               .withVersion(Optional.ofNullable(wallet.getVersion()).orElse(0L))
                                               .withId(Optional.ofNullable(wallet.getId())
                                                               .orElseGet(() -> new WalletID(UUID.randomUUID().toString())))
                                               .withAmountUnit(Optional.ofNullable(wallet.getAmountUnit())
                                                                       .orElse(0L))
                                               .withOwnerId(wallet.getOwnerId())
                                               .withOwner(wallet.getOwner())
                                               .withType(wallet.getType())
                                               .withInsertOn(Optional.ofNullable(wallet.getInsertOn())
                                                                     .orElseGet(Instant::now))
                                               .build();
        wallets.put(newWalletUpdated.getId(), newWalletUpdated);
        return newWalletUpdated.getId();
    }

    @Override
    public List<Wallet> loadWalletByOwnerId(WalletOwnerId ownerId) {
        return wallets.values().stream().filter(w -> w.getOwnerId().equals(ownerId)).collect(Collectors.toList());
    }

    @Override
    public Optional<Transfer> loadTransferById(TransferId transferId) {
        return Optional.ofNullable(transfers.get(transferId));
    }

    @Override
    public Stream<Transfer> loadTransfersByWalletId(WalletID walletID) {
        return transfersByWalletId.get(walletID).stream();
    }

    @Override
    public List<Transfer> loadTransfersByOperationId(String operationId) {
        return transfers.values().stream()
                        .filter(t -> t.getOperationId()==null || t.getOperationId().equals(operationId))
                        .collect(Collectors.toList());
    }

    private void updateWallet(Wallet existingWallet, Wallet newWallet) {
        Wallet newWalletUpdated = WalletBuilder.aWallet()
                                               .withVersion(existingWallet.getVersion() + 1)
                                               .withUpdatedOn(Instant.now())
                                               .withId(Optional.ofNullable(newWallet.getId()).orElseGet(existingWallet::getId))
                                               .withAmountUnit(Optional.ofNullable(newWallet.getAmountUnit()).orElseGet(existingWallet::getAmountUnit))
                                               .withOwnerId(Optional.ofNullable(newWallet.getOwnerId()).orElseGet(existingWallet::getOwnerId))
                                               .withOwner(Optional.ofNullable(newWallet.getOwner()).orElseGet(existingWallet::getOwner))
                                               .withType(Optional.ofNullable(newWallet.getType()).orElseGet(existingWallet::getType))
                                               .withInsertOn(Optional.ofNullable(newWallet.getInsertOn()).orElseGet(existingWallet::getInsertOn))
                                               .build();
        wallets.put(newWallet.getId(), newWalletUpdated);
    }

    TransferId insertTransfer(Transfer transfer) {
        transfers.put(transfer.getId(), transfer);
        if (!transfersByWalletId.containsKey(transfer.getWalletId())) {
            transfersByWalletId.put(transfer.getWalletId(), new ArrayList<>());
        }
        transfersByWalletId.get(transfer.getWalletId()).add(transfer);
        return transfer.getId();
    }


    public void deleteWallet(WalletID walletID) {
        Optional.ofNullable(transfersByWalletId.get(walletID))
                .ifPresent(m -> m.stream().map(Transfer::getId)
                                 .forEach(transfers::remove));
        transfersByWalletId.remove(walletID);
        wallets.remove(walletID);
    }

    public void cleanup() {
        List<WalletID> walletIds = wallets.values().stream().map(Wallet::getId).toList();
        walletIds.forEach(this::deleteWallet);

    }

}
