package org.authorizer.builder;

import org.authorizer.core.config.ApplicationCoreProperties;

public class ApplicationPropertiesBuilder {

    public static ApplicationCoreProperties.ErrorProperties accountAlreadyInitializedKey(String key) {
        var errorKey = new ApplicationCoreProperties.ErrorProperties();
        errorKey.setAccountAlreadyInitializedKey(key);
        return errorKey;
    }

    public static ApplicationCoreProperties.ErrorProperties accountNotInitializedKey(String key) {
        var errorKey = new ApplicationCoreProperties.ErrorProperties();
        errorKey.setAccountNotInitializedKey(key);
        return errorKey;
    }

    public static ApplicationCoreProperties.ErrorProperties cardNotActiveKey(String key) {
        var errorKey = new ApplicationCoreProperties.ErrorProperties();
        errorKey.setAccountCardNotActiveKey(key);
        return errorKey;
    }

    public static ApplicationCoreProperties.ErrorProperties insufficientLimitKey(String key) {
        var errorKey = new ApplicationCoreProperties.ErrorProperties();
        errorKey.setInsufficientLimitKey(key);
        return errorKey;
    }

    public static ApplicationCoreProperties.ErrorProperties highFrequencySmallIntervalKey(String key) {
        var errorKey = new ApplicationCoreProperties.ErrorProperties();
        errorKey.setHighFrequencySmallIntervalKey(key);
        return errorKey;
    }

    public static ApplicationCoreProperties.ErrorProperties duplicateTransactionKey(String key) {
        var errorKey = new ApplicationCoreProperties.ErrorProperties();
        errorKey.setDuplicateTransactionKey(key);
        return errorKey;
    }

    public static ApplicationCoreProperties.TransactionProperties anyMerchantFrequencyLimit(Long amount, Long minutes) {
        var transactionProperties = new ApplicationCoreProperties.TransactionProperties();
        transactionProperties.setAnyMerchantFrequencyLimit(transactionLimit(amount, minutes));
        return transactionProperties;
    }

    public static ApplicationCoreProperties.TransactionProperties sameTransactionFrequencyLimit(Long amount, Long minutes) {
        var transactionProperties = new ApplicationCoreProperties.TransactionProperties();
        transactionProperties.setSameTransactionFrequencyLimit(transactionLimit(amount, minutes));
        return transactionProperties;
    }

    private static ApplicationCoreProperties.TransactionLimit transactionLimit(Long amount, Long minutes) {
        var transactionLimit = new ApplicationCoreProperties.TransactionLimit();
        transactionLimit.setAmount(amount);
        transactionLimit.setMinutes(minutes);
        return transactionLimit;
    }
}
