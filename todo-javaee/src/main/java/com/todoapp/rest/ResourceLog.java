package com.todoapp.rest;

import com.todoapp.logging.Log;

import java.io.IOException;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@ResourceLogged
@Provider
public class ResourceLog implements ContainerResponseFilter, ContainerRequestFilter {

  private static final String NEW_LINE_AND_IDENT = "\n   ";
  private static final String NEW_LINE = "\n";

  @Inject
  Log log;

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    StringBuilder stringBuilder = new StringBuilder();
    createRequestMessage(requestContext, stringBuilder);
    log.info("Server has received a request: {0}", stringBuilder.toString());
  }

  @Override
  public void filter(ContainerRequestContext requestContext, ContainerResponseContext
      responseContext)
      throws IOException {

    StringBuilder message = new StringBuilder();
    createResponseMessage(responseContext, message);

    String method = requestContext.getMethod();
    String requestUri = requestContext.getUriInfo().getRequestUri().toASCIIString();
    log.info("Server responded on request {0} {1} with response: {2}", method,
        requestUri, message.toString());
  }

  private void createRequestMessage(ContainerRequestContext requestContext, StringBuilder
      stringBuilder) {
    stringBuilder.append(NEW_LINE);

    stringBuilder
        .append(NEW_LINE_AND_IDENT)
        .append("Request URL: ").append(requestContext.getUriInfo().getRequestUri().toASCIIString())
        .append(NEW_LINE_AND_IDENT)
        .append("Request Method: ").append(requestContext.getMethod());

    stringBuilder
        .append(NEW_LINE_AND_IDENT)
        .append("Request headers: ").append(requestContext.getHeaders());

    stringBuilder.append(NEW_LINE);
  }

  private void createResponseMessage(ContainerResponseContext responseContext, StringBuilder
      stringBuilder) {
    stringBuilder.append(NEW_LINE);

    stringBuilder
        .append(NEW_LINE_AND_IDENT)
        .append("Response status: ").append(responseContext.getStatus());

    stringBuilder
        .append(NEW_LINE_AND_IDENT)
        .append("Response headers: ").append(responseContext.getHeaders());

    stringBuilder.append(NEW_LINE);
  }

}
