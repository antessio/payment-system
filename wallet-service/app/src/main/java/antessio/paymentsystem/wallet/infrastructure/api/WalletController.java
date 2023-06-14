package antessio.paymentsystem.wallet.infrastructure.api;

import java.util.List;
import java.util.stream.Collectors;

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
            @PathVariable String ownerId,
            @RequestParam Long limit,
            @RequestParam String cursor) {
        List<WalletHttpResponse> walletsResponse = walletApplicationService.getWalletsByWalletOwner(new WalletOwnerId(ownerId))
                                                                           .stream()
                                                                           .map(this::fromWalletDTO)
                                                                           .toList();
        return ResponseEntity.ok(new PaginatedList<>(walletsResponse, false));
    }

    private WalletHttpResponse fromWalletDTO(WalletDTO w) {
        return new WalletHttpResponse(
                w.getId().getId(),
                w.getType().name(),
                w.getOwnerId().getId(),
                new Amount(w.getAmountUnit(), "EUR"),
                getTransfers(w.getId()));
    }

    private PaginatedList<TransferHttpResponse> getTransfers(WalletID id) {
        List<TransferDTO> firstSixTransfers = walletApplicationService.getTransfers(id)
                                                                               .limit(6)
                                                                               .toList();
        List<TransferHttpResponse> transfersHttpResponse = firstSixTransfers.stream()
                                                                            .map(WalletController::fromTransferDTO)
                                                                            .limit(5)
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
