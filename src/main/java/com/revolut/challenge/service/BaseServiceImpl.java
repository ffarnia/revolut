package com.revolut.challenge.service;

import com.revolut.challenge.repository.InMemoryRepository;

/**
 * Created by Fazel on 1/25/2020.
 */
public class BaseServiceImpl {

    public static final InMemoryRepository repository = new InMemoryRepository();

    public void clearRepository() {
        if (repository != null) {
            repository.resetRepository();
        }
    }

}
