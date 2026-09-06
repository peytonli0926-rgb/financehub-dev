package com.utfinancing.financehub.engine.finance.service;

import org.springframework.scheduling.annotation.Async;

public interface IContractAsyncService {

    @Async
    public void contractInfoAsync(int startIndex, int endIndex, String leaseDateStart);
}
