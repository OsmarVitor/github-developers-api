package com.github.developers.service.github.impl;

import com.github.developers.service.github.RequestService;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RequestServiceImpl implements RequestService {

  @Value("${github.base.search.name.url}")
  private String endpoint;

  @Override
  public HttpResponse<JsonNode> searchByName(String username) {
    return Unirest.get(endpoint).routeParam("param", "users").routeParam("q", username).asJson();
  }

  @Override
  public HttpResponse<JsonNode> searchByLanguage(String language) {
    return Unirest.get(endpoint)
        .routeParam("param", "repositories")
        .routeParam("q", "topic:" + language)
        .asJson();
  }
}
