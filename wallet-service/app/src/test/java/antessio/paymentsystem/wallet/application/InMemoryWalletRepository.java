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

import antessio.paymentsystem.wallet.MovementId;
import antessio.paymentsystem.wallet.WalletID;
import antessio.paymentsystem.wallet.WalletOwnerId;
import antessio.paymentsystem.wallet.domain.Movement;
import antessio.paymentsystem.wallet.domain.Wallet;
import antessio.paymentsystem.wallet.domain.WalletBuilder;
import antessio.paymentsystem.wallet.domain.WalletsUpdate;

public class InMemoryWalletRepository implements WalletRepository {

    private Map<WalletID, Wallet> wallets = new HashMap<>();
    private Map<WalletID, List<Movement>> movementsByWalletId = new HashMap<>();
    private Map<MovementId, Movement> movements = new HashMap<>();

    @Override
    public Optional<Wallet> loadWalletById(WalletID id) {
        return Optional.ofNullable(wallets.get(id));
    }

    @Override
    public List<MovementId> updateWallet(WalletsUpdate walletsUpdate) {
        MovementId sourceMovementId = insertMovement(walletsUpdate.getSourceWalletMovement());
        MovementId destinationMovementId = insertMovement(walletsUpdate.getDestinationWalletMovement());
        updateWallet(wallets.get(walletsUpdate.getSourceWallet().getId()), walletsUpdate.getSourceWallet());
        updateWallet(wallets.get(walletsUpdate.getDestinationWallet().getId()), walletsUpdate.getDestinationWallet());
        return List.of(sourceMovementId, destinationMovementId);
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
    public Optional<Movement> loadMovementById(MovementId movementId) {
        return Optional.ofNullable(movements.get(movementId));
    }

    @Override
    public Stream<Movement> loadMovementsByWalletId(WalletID walletID) {
        return movementsByWalletId.get(walletID).stream();
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

    MovementId insertMovement(Movement movement) {
        movements.put(movement.getId(), movement);
        if (!movementsByWalletId.containsKey(movement.getWalletId())) {
            movementsByWalletId.put(movement.getWalletId(), new ArrayList<>());
        }
        movementsByWalletId.get(movement.getWalletId()).add(movement);
        return movement.getId();
    }


    public void deleteWallet(WalletID walletID) {
        Optional.ofNullable(movementsByWalletId.get(walletID))
                .ifPresent(m -> m.stream().map(Movement::getId)
                                 .forEach(movements::remove));
        movementsByWalletId.remove(walletID);
        wallets.remove(walletID);
    }

    public void cleanup() {
        List<WalletID> walletIds = wallets.values().stream().map(Wallet::getId).toList();
        walletIds.forEach(this::deleteWallet);

    }

}
