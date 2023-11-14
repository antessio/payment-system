package antessio.paymentsystem.topup.domain;

import org.jmolecules.ddd.annotation.Identity;

import antessio.paymentsystem.common.Amount;
import antessio.paymentsystem.topup.TopUpId;
import antessio.paymentsystem.topup.TopUpStatus;

/**
 * A generic top-up
 */
public abstract class TopUp {

    @Identity
    private TopUpId id;
    private Amount amount;

    private TopUpStatus status;

    TopUp(TopUpId id, Amount amount) {
        this.id = id;
        this.amount = amount;
        this.status = TopUpStatus.REQUESTED;
    }

    /**
     * Top-up identifier
     * @return
     */
    public TopUpId getId() {
        return id;
    }

    /**
     * Top-up amount
     * @return
     */
    public Amount getAmount() {
        return amount;
    }

    public TopUpStatus getStatus() {
        return status;
    }

    protected void complete(){
        this.status = TopUpStatus.COMPLETED;
    }
    protected void cancel(){
        this.status = TopUpStatus.CANCELED;
    }
    void markAsInProgress(){
        this.status = TopUpStatus.IN_PROGRESS;
    }


}
