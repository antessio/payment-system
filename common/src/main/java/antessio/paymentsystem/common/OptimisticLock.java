package antessio.paymentsystem.common;

public class OptimisticLock {
    protected Integer version;

    public OptimisticLock(Integer version) {
        this.version = version;
    }

    public Integer getVersion() {
        return version;
    }

}
