package com.todoapp.rest;

import com.todoapp.logging.Log;

import org.junit.Test;

import java.net.URI;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ResourceLogTest {


  public static final String HTTP_EXAMPLE_COM = "http://example.com/";
  public static final String GET = "GET";

  @Test
  public void shouldLogRequest() throws Exception {
    final Log log = mock(Log.class);
    final ContainerRequestContext requestContext = mock(ContainerRequestContext.class);
    final UriInfo uriInfo = mock(UriInfo.class);
    given(requestContext.getUriInfo()).willReturn(uriInfo);
    given(requestContext.getUriInfo().getRequestUri()).willReturn(new URI(HTTP_EXAMPLE_COM));
    given(requestContext.getMethod()).willReturn(GET);
    given(requestContext.getHeaders()).willReturn(new MultivaluedHashMap<>());
    ResourceLog resourceLog = new ResourceLog();
    resourceLog.log = log;

    resourceLog.filter(requestContext);

    verify(log).info(
        eq("Server has received a request: {0}"),
        eq("\n\n   Request URL: http://example.com/\n   Request Method: GET\n   Request headers: " +
            "{}\n"));
  }

  @Test
  public void shouldLogResponse() throws Exception {
    final Log log = mock(Log.class);
    final ContainerRequestContext requestContext = mock(ContainerRequestContext.class);
    final UriInfo uriInfo = mock(UriInfo.class);
    given(requestContext.getUriInfo()).willReturn(uriInfo);
    given(requestContext.getUriInfo().getRequestUri()).willReturn(new URI(HTTP_EXAMPLE_COM));
    given(requestContext.getMethod()).willReturn(GET);

    ResourceLog resourceLog = new ResourceLog();
    resourceLog.log = log;

    final ContainerResponseContext responseContext = mock(ContainerResponseContext.class);
    given(responseContext.getStatus()).willReturn(Status.OK.getStatusCode());
    given(responseContext.getHeaders()).willReturn(new MultivaluedHashMap<>());

    resourceLog.filter(requestContext, responseContext);

    verify(log).info(
        eq("Server responded on request {0} {1} with response: {2}"),
        eq(GET),
        eq(HTTP_EXAMPLE_COM),
        eq("\n\n   Response status: 200\n   Response headers: {}\n"));
  }
}
