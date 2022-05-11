package com.github.developers.service.github;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;

public interface RequestService {

  HttpResponse<JsonNode> searchByName(String username);

  HttpResponse<JsonNode> searchByLanguage(String language);
}
