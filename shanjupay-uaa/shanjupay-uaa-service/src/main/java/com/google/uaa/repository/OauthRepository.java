package com.google.uaa.repository;

import com.google.uaa.domain.OauthClientDetails;

import java.util.List;

public interface OauthRepository {

    OauthClientDetails findOauthClientDetails(String clientId);

    List<OauthClientDetails> findAllOauthClientDetails();

    void updateOauthClientDetailsArchive(String clientId, boolean archive);

    void saveOauthClientDetails(OauthClientDetails clientDetails);
}