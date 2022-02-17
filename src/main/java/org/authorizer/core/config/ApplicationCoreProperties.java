package org.authorizer.core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "application.core", ignoreUnknownFields = false)
public class ApplicationCoreProperties {

    private ErrorProperties error = new ErrorProperties();
    private TransactionProperties transaction = new TransactionProperties();

    @Getter
    @Setter
    public static class ErrorProperties {
        private String accountAlreadyInitializedKey;
        private String accountNotInitializedKey;
        private String accountCardNotActiveKey;
        private String insufficientLimitKey;
        private String highFrequencySmallIntervalKey;
        private String duplicateTransactionKey;
    }

    @Getter
    @Setter
    public static class TransactionProperties {
        private TransactionLimit anyMerchantFrequencyLimit = new TransactionLimit();
        private TransactionLimit sameTransactionFrequencyLimit = new TransactionLimit();
    }

    @Getter
    @Setter
    public static class TransactionLimit {
        private Long minutes;
        private Long amount;
    }
}
