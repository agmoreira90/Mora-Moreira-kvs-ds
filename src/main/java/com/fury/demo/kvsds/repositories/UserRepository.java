package com.fury.demo.kvsds.repositories;

import com.fury.demo.kvsds.dtos.UserDto;
import com.mercadolibre.json.exception.JsonException;
import com.mercadolibre.kvsclient.IQKVSContainerKvsClient;
import com.mercadolibre.kvsclient.QKVSContainerLowLevelClient;
import com.mercadolibre.kvsclient.exceptions.KvsException;
import com.mercadolibre.kvsclient.item.Item;
import com.mercadolibre.kvsclient.kvsapi.KvsapiConfiguration;

import java.util.concurrent.RejectedExecutionException;

public class UserRepository {
    private KvsapiConfiguration config;
    private IQKVSContainerKvsClient client;

    public UserRepository() {
        this.config = KvsapiConfiguration.builder().withMaxRetries(2).build();
        this.client = new QKVSContainerLowLevelClient(config, "KEY_VALUE_STORE_MY_CONTAINER_CONTAINER_NAME");
    }

    public UserDto getUser(Long userId) throws KvsException {
        UserDto user = new UserDto();
        try {
            user = (UserDto) client.get(userId.toString()).getValue();
        } catch (RejectedExecutionException | JsonException e) {
            e.printStackTrace();
        }
        return user;
    }

    public void insertUser(UserDto user) throws KvsException {
        try {
            Item item = new Item();
            item.setValue(user);
            item.setKey(user.getId().toString());
            client.save(item);
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
        }

    }

    public void updateUser(UserDto user) throws KvsException {
        try {
            Item item = new Item();
            item.setValue(user);
            item.setKey(user.getId().toString());
            client.update(item);
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
        }
    }

    public void deletetUser(UserDto user) throws KvsException {
        try {
            client.delete(user.getId().toString());
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
        }
    }
}
