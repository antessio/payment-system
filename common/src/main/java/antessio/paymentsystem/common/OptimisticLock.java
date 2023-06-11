package antessio.paymentsystem.common;

public class OptimisticLock {
    protected Long version;

    public OptimisticLock(Long version) {
        this.version = version;
    }

    public Long getVersion() {
        return version;
    }

}
