package antessio.paymentsystem.wallet.infrastructure.api;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import antessio.paymentsystem.api.wallet.MovementDTO;
import antessio.paymentsystem.api.wallet.WalletDTO;
import antessio.paymentsystem.api.wallet.WalletOwnerFundsLockCollectCommand;
import antessio.paymentsystem.api.wallet.WalletOwnerFundsLockCommand;
import antessio.paymentsystem.common.PaginatedList;
import antessio.paymentsystem.common.Amount;
import antessio.paymentsystem.wallet.MovementId;
import antessio.paymentsystem.wallet.WalletID;
import antessio.paymentsystem.wallet.WalletOwnerId;
import antessio.paymentsystem.wallet.application.WalletApplicationService;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletApplicationService walletApplicationService;

    public WalletController(WalletApplicationService walletApplicationService) {
        this.walletApplicationService = walletApplicationService;
    }

    @PostMapping("{ownerId}/fundLock")
    public ResponseEntity<String> createFundLock(@PathVariable String ownerId,
                                                 @RequestBody CreateFundLockHttpRequest createFundLockHttpRequest){
        MovementId fundLock = walletApplicationService.lockFunds(new WalletOwnerFundsLockCommand(
                createFundLockHttpRequest.getAmount(),
                new WalletOwnerId(ownerId)));
        return ResponseEntity.ok(fundLock.getId());
    }

    @PostMapping("{ownerId}/fundLock/{fundLockId}/collect")
    public ResponseEntity<Void> collectFundLock(@PathVariable String ownerId,
                                                @PathVariable String fundLockId){

        walletApplicationService.collectFundLock(WalletOwnerFundsLockCollectCommand.of(new MovementId(fundLockId), new WalletOwnerId(ownerId)));
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{ownerId}")
    public ResponseEntity<PaginatedList<WalletHttpResponse>> getMovements(
            @PathVariable String ownerId) {
        List<WalletHttpResponse> walletsResponse = walletApplicationService.getWalletsByWalletOwner(new WalletOwnerId(ownerId))
                                                                           .stream()
                                                                           .map(this::fromWalletDTO)
                                                                           .toList();
        return ResponseEntity.ok(new PaginatedList<>(walletsResponse, false));
    }

    @GetMapping("/{ownerId}/{walletIdStr}/movements")
    public ResponseEntity<PaginatedList<MovementHttpResponse>> getMovements(
            @PathVariable String ownerId,
            @PathVariable String walletIdStr,
            @RequestParam Long limit,
            @RequestParam String cursor) {
        WalletID walletId = new WalletID(walletIdStr);
        WalletDTO wallet = walletApplicationService.getWallet(walletId);
        if (!wallet.getOwnerId().getId().equals(ownerId)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        PaginatedList<MovementHttpResponse> result = getMovements(walletId, limit, Optional.ofNullable(cursor)
                                                                                           .map(MovementId::new)
                                                                                           .orElse(null));
        return ResponseEntity.ok(result);

    }


    private WalletHttpResponse fromWalletDTO(WalletDTO w) {
        return new WalletHttpResponse(
                w.getId().getId(),
                w.getType().name(),
                w.getOwnerId().getId(),
                new Amount(w.getAmountUnit(), "EUR"),
                getMovements(w.getId(), 5, null));
    }

    private PaginatedList<MovementHttpResponse> getMovements(WalletID id, long limit, MovementId cursor) {
        List<MovementDTO> firstSixMovements = walletApplicationService.getMovements(id, cursor)
                                                                      .limit(limit + 1)
                                                                      .toList();
        List<MovementHttpResponse> transfersHttpResponse = firstSixMovements.stream()
                                                                            .map(WalletController::fromTransferDTO)
                                                                            .limit(limit)
                                                                            .collect(Collectors.toList());
        return new PaginatedList<>(transfersHttpResponse, firstSixMovements.size() > transfersHttpResponse.size());
    }

    private static MovementHttpResponse fromTransferDTO(MovementDTO t) {
        return new MovementHttpResponse(
                t.getId().getId(),
                t.getDirection().name(),
                t.getAmount());
    }

}
