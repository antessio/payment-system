package antessio.paymentsystem.wallet.infrastructure.api;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import antessio.paymentsystem.api.wallet.TransferDTO;
import antessio.paymentsystem.api.wallet.WalletDTO;
import antessio.paymentsystem.common.PaginatedList;
import antessio.paymentsystem.wallet.Amount;
import antessio.paymentsystem.wallet.TransferId;
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

    @GetMapping("/{ownerId}")
    public ResponseEntity<PaginatedList<WalletHttpResponse>> getWallets(
            @PathVariable String ownerId) {
        List<WalletHttpResponse> walletsResponse = walletApplicationService.getWalletsByWalletOwner(new WalletOwnerId(ownerId))
                                                                           .stream()
                                                                           .map(this::fromWalletDTO)
                                                                           .toList();
        return ResponseEntity.ok(new PaginatedList<>(walletsResponse, false));
    }

    @GetMapping("/{ownerId}/{walletIdStr}/transfer")
    public ResponseEntity<PaginatedList<TransferHttpResponse>> getWallets(
            @PathVariable String ownerId,
            @PathVariable String walletIdStr,
            @RequestParam Long limit,
            @RequestParam String cursor) {
        WalletID walletId = new WalletID(walletIdStr);
        WalletDTO wallet = walletApplicationService.getWallet(walletId);
        if (!wallet.getOwnerId().getId().equals(ownerId)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        PaginatedList<TransferHttpResponse> result = getTransfers(walletId, limit, Optional.ofNullable(cursor)
                                                                                     .map(TransferId::new)
                                                                                     .orElse(null));
        return ResponseEntity.ok(result);

    }


    private WalletHttpResponse fromWalletDTO(WalletDTO w) {
        return new WalletHttpResponse(
                w.getId().getId(),
                w.getType().name(),
                w.getOwnerId().getId(),
                new Amount(w.getAmountUnit(), "EUR"),
                getTransfers(w.getId(), 5, null));
    }

    private PaginatedList<TransferHttpResponse> getTransfers(WalletID id, long limit, TransferId cursor) {
        List<TransferDTO> firstSixTransfers = walletApplicationService.getTransfers(id, cursor)
                                                                      .limit(limit + 1)
                                                                      .toList();
        List<TransferHttpResponse> transfersHttpResponse = firstSixTransfers.stream()
                                                                            .map(WalletController::fromTransferDTO)
                                                                            .limit(limit)
                                                                            .collect(Collectors.toList());
        return new PaginatedList<>(transfersHttpResponse, firstSixTransfers.size() > transfersHttpResponse.size());
    }

    private static TransferHttpResponse fromTransferDTO(TransferDTO t) {
        return new TransferHttpResponse(
                t.getId().getId(),
                t.getDirection().name(),
                t.getAmount());
    }

}
