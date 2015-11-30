package com.todoapp.rest;

import org.junit.Test;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


public class CorsHeadersTest {

  @Test
  public void shouldAddCorsHeaders() throws IOException {
    CorsHeaders filter = new CorsHeaders();
    ContainerRequestContext requestContext = mock(ContainerRequestContext.class);
    ContainerResponseContext responseContext = mock(ContainerResponseContext.class);
    MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
    given(responseContext.getHeaders()).will(invocation -> headers);

    filter.filter(requestContext, responseContext);

    assertThat(headers.size(), is(equalTo(5)));
  }
}
