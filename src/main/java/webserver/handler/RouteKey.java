package webserver.handler;

import webserver.http.request.HttpMethod;


public record RouteKey(HttpMethod httpMethod, String path) { }
