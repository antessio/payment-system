package antessio.paymentsystem.topup.application;

import java.util.Map;
import java.util.Optional;

import antessio.paymentsystem.common.Amount;
import antessio.paymentsystem.topup.TopUpService;
import antessio.paymentsystem.topup.domain.BankAccount;
import antessio.paymentsystem.topup.domain.BankToWalletDomainService;
import antessio.paymentsystem.topup.domain.BankToWalletTopUp;
import antessio.paymentsystem.topup.domain.BankTransfer;
import antessio.paymentsystem.topup.domain.BankTransferService;
import antessio.paymentsystem.topup.domain.TopUp;
import antessio.paymentsystem.topup.TopUpId;
import antessio.paymentsystem.topup.domain.TopUpRepository;
import antessio.paymentsystem.topup.domain.Wallet;
import antessio.paymentsystem.topup.domain.WalletTransfer;
import antessio.paymentsystem.topup.domain.WalletTransferService;
import antessio.paymentsystem.topup.dto.BankToWalletTopUpCreateDto;
import antessio.paymentsystem.topup.dto.TopUpDto;

public class TopUpApplicationService implements TopUpService {

    private final TopUpRepository topUpRepository;
    private final BankToWalletDomainService bankToWalletDomainService;
    private final BankTransferService bankTransferService;

    private final WalletTransferService walletTransferService;

    private final TopUpProcessor topUpProcessor;

    public TopUpApplicationService(
            TopUpRepository topUpRepository,
            BankToWalletDomainService bankToWalletDomainService,
            BankTransferService bankTransferService,
            WalletTransferService walletTransferService) {
        this.topUpRepository = topUpRepository;
        this.bankToWalletDomainService = bankToWalletDomainService;
        this.bankTransferService = bankTransferService;
        this.walletTransferService = walletTransferService;
        topUpProcessor = new TopUpProcessor();
    }

    public BankToWalletTopUp createBankToWalletTopUp(
            Amount amount,
            BankAccount from,
            Wallet to) {
        return bankToWalletDomainService.create(from, to, amount);
    }

    public void processBankTransfer(TopUpId topUpId) {
        TopUp topUp = topUpRepository.loadById(topUpId)
                                     .orElseThrow(() -> new IllegalArgumentException("top-up with id %s doesn't exist".formatted(topUpId.getId().toString())));
        topUpProcessor.processBankTransfer(topUp);

    }

    public void processWalletTransfer(TopUpId topUpId) {
        TopUp topUp = topUpRepository.loadById(topUpId)
                                     .orElseThrow(() -> new IllegalArgumentException("top-up with id %s doesn't exist".formatted(topUpId.getId().toString())));
        topUpProcessor.processWalletTransfer(topUp);
    }

    public void markAsInProgress(TopUpId topUpId) {
        TopUp topUp = topUpRepository.loadById(topUpId)
                                     .orElseThrow(() -> new IllegalArgumentException("top-up with id %s doesn't exist".formatted(topUpId.getId().toString())));
        topUpProcessor.markAsInProgress(topUp);

    }

    public void bankTransferExecuted(BankTransfer.BankTransferId bankTransferId) {
        TopUp topUp = topUpRepository.loadTopUpByBankTransferId(bankTransferId)
                                     .orElseThrow(() -> new IllegalArgumentException("top-up with bank transfer id id %s doesn't exist".formatted(bankTransferId.getId())));
        topUpProcessor.bankTransferExecuted(topUp);
    }

    public void confirmWalletTransfer(TopUpId topUpId) {
        TopUp topUp = topUpRepository.loadById(topUpId)
                                     .orElseThrow(() -> new IllegalArgumentException("top-up with id %s doesn't exist".formatted(topUpId.getId().toString())));
        topUpProcessor.confirmWalletTransfer(topUp);
    }

    public void processWalletTransferFailed(WalletTransfer.WalletTransferId walletTransferId) {
        TopUp topUp = topUpRepository.loadByWalletTransferId(walletTransferId)
                                     .orElseThrow(() -> new IllegalArgumentException("top-up with wallet transfer id %s doesn't exist".formatted(
                                             walletTransferId.getId())));
        topUpProcessor.processWalletTransferFailed(topUp);
    }

    public void processBankTransferFailed(BankTransfer.BankTransferId bankTransferId) {
        TopUp topUp = topUpRepository.loadTopUpByBankTransferId(bankTransferId)
                                     .orElseThrow(() -> new IllegalArgumentException("top-up with bank transfer id id %s doesn't exist".formatted(bankTransferId.getId())));
        topUpProcessor.processBankTransferFailed(topUp);
    }

    public void processTopUpCanceled(TopUpId topUpId) {
        TopUp topUp = topUpRepository.loadById(topUpId)
                                     .orElseThrow(() -> new IllegalArgumentException("top-up with id %s doesn't exist".formatted(topUpId.getId().toString())));
        topUpProcessor.processTopUpCanceled(topUp);
    }

    @Override
    public Optional<TopUpDto> loadById(TopUpId id) {
        return topUpRepository.loadById(id)
                              .map(t -> new TopUpDto(t.getId(), t.getAmount(), t.getStatus()));
    }

    @Override
    public TopUpId createBankToWalletTopUp(BankToWalletTopUpCreateDto createDto) {
        BankToWalletTopUp bankToWalletTopUp = createBankToWalletTopUp(
                createDto.getAmount(),
                new BankAccount(createDto.getBankAccountId()),
                new Wallet(createDto.getWalletId()));
        return bankToWalletTopUp.getId();
    }

    class TopUpProcessor implements TopUpProcessingStrategy {

        private final Map<Class<? extends TopUp>, TopUpProcessingStrategy> strategies = Map.of(
                BankToWalletTopUp.class,
                new BankToWalletTopUpProcessingStrategy());


        @Override
        public void processBankTransfer(TopUp topUp) {
            getStrategy(topUp.getClass()).processBankTransfer(topUp);
        }

        @Override
        public void processWalletTransfer(TopUp topUp) {
            getStrategy(topUp.getClass()).processWalletTransfer(topUp);
        }

        @Override
        public void markAsInProgress(TopUp topUp) {
            getStrategy(topUp.getClass()).markAsInProgress(topUp);
        }

        @Override
        public void bankTransferExecuted(TopUp topUp) {
            getStrategy(topUp.getClass()).bankTransferExecuted(topUp);
        }

        @Override
        public void confirmWalletTransfer(TopUp topUp) {
            getStrategy(topUp.getClass()).confirmWalletTransfer(topUp);
        }

        @Override
        public void processBankTransferFailed(TopUp topUp) {
            getStrategy(topUp.getClass()).processBankTransferFailed(topUp);
        }

        @Override
        public void processWalletTransferFailed(TopUp topUp) {
            getStrategy(topUp.getClass()).processWalletTransferFailed(topUp);
        }

        @Override
        public void processTopUpCanceled(TopUp topUp) {
            getStrategy(topUp.getClass()).processBankTransfer(topUp);
        }

        private TopUpProcessingStrategy getStrategy(Class<? extends TopUp> topUpClass) {

            return strategies
                    .entrySet()
                    .stream()
                    .filter(e -> e.getKey().isAssignableFrom(topUpClass))
                    .findFirst()
                    .map(Map.Entry::getValue)
                    .orElseThrow(() -> new IllegalArgumentException("top-up class %s not handled".formatted(topUpClass)));
        }

    }

    class BankToWalletTopUpProcessingStrategy implements TopUpProcessingStrategy {

        public Class<BankToWalletTopUp> getTopUpClass() {
            return BankToWalletTopUp.class;
        }

        private BankToWalletTopUp castBankToWalletTopUp(TopUp topUp) {
            return getTopUpClass().cast(topUp);
        }

        @Override
        public void processBankTransfer(TopUp topUp) {
            BankToWalletTopUp bankToWalletTopUp = castBankToWalletTopUp(topUp);
            BankTransfer bankTransfer = bankTransferService.createBankTransfer(
                    bankToWalletTopUp.getBankAccount(),
                    bankToWalletTopUp.getAmount());
            bankToWalletDomainService.bankTransferCreated(
                    bankToWalletTopUp, bankTransfer.id());
        }

        @Override
        public void processWalletTransfer(TopUp topUp) {
            BankToWalletTopUp bankToWalletTopUp = castBankToWalletTopUp(topUp);
            WalletTransfer walletTransfer = walletTransferService.createWalletTransfer(bankToWalletTopUp.getWallet(), bankToWalletTopUp.getAmount());
            bankToWalletDomainService.walletTransferCreated(
                    bankToWalletTopUp, walletTransfer.id());
        }

        @Override
        public void markAsInProgress(TopUp topUp) {
            BankToWalletTopUp bankToWalletTopUp = castBankToWalletTopUp(topUp);
            bankToWalletDomainService.markAsInProgress(bankToWalletTopUp);

        }

        @Override
        public void bankTransferExecuted(TopUp topUp) {
            BankToWalletTopUp bankToWalletTopUp = castBankToWalletTopUp(topUp);
            bankToWalletDomainService.bankTransferExecuted(bankToWalletTopUp);
        }

        @Override
        public void confirmWalletTransfer(TopUp topUp) {
            BankToWalletTopUp bankToWalletTopUp = castBankToWalletTopUp(topUp);
            WalletTransfer walletTransfer = bankToWalletTopUp.getWalletTransfer()
                                                             .orElseThrow(() -> new IllegalStateException("top-up has no wallet transfer"));
            walletTransferService.confirmWalletTransfer(walletTransfer.id());
            bankToWalletDomainService.complete(bankToWalletTopUp, walletTransfer.id());
        }

        @Override
        public void processBankTransferFailed(TopUp topUp) {
            BankToWalletTopUp bankToWalletTopUp = castBankToWalletTopUp(topUp);
            bankToWalletDomainService.markBankTransferFailed(bankToWalletTopUp);
        }

        @Override
        public void processWalletTransferFailed(TopUp topUp) {
            BankToWalletTopUp bankToWalletTopUp = castBankToWalletTopUp(topUp);
            WalletTransfer walletTransfer = bankToWalletTopUp.getWalletTransfer()
                                                             .orElseThrow(() -> new IllegalStateException("top-up has no wallet transfer"));
            walletTransferService.confirmWalletTransfer(walletTransfer.id());
            bankToWalletDomainService.complete(bankToWalletTopUp, walletTransfer.id());
        }

        @Override
        public void processTopUpCanceled(TopUp topUp) {
            BankToWalletTopUp bankToWalletTopUp = castBankToWalletTopUp(topUp);
            WalletTransfer walletTransfer = bankToWalletTopUp.getWalletTransfer()
                                                             .orElseThrow(() -> new IllegalStateException("top-up has no wallet transfer"));
            walletTransferService.cancelWalletTransfer(walletTransfer.id());
            bankToWalletDomainService.markWalletTransferCanceled(bankToWalletTopUp, walletTransfer.id());
        }

    }

    interface TopUpProcessingStrategy {

        void processBankTransfer(TopUp topUp);

        void processWalletTransfer(TopUp topUp);

        void markAsInProgress(TopUp topUp);

        void bankTransferExecuted(TopUp topUp);

        void confirmWalletTransfer(TopUp topUp);

        void processBankTransferFailed(TopUp topUp);

        void processWalletTransferFailed(TopUp topUp);

        void processTopUpCanceled(TopUp topUp);

    }

}
