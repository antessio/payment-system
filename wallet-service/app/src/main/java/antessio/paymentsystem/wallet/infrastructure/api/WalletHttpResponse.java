package antessio.paymentsystem.wallet.infrastructure.api;

import antessio.paymentsystem.common.PaginatedList;
import antessio.paymentsystem.wallet.Amount;

public class WalletHttpResponse {
    private String id;
    private String type;
    private String owner;
    private Amount amount;

    private PaginatedList<TransferHttpResponse> transfers;

    public WalletHttpResponse(String id, String type, String owner, Amount amount, PaginatedList<TransferHttpResponse> transfers) {
        this.id = id;
        this.type = type;
        this.owner = owner;
        this.amount = amount;
        this.transfers = transfers;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getOwner() {
        return owner;
    }

    public Amount getAmount() {
        return amount;
    }

}
