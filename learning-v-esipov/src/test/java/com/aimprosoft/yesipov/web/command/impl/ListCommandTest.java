package com.aimprosoft.yesipov.web.command.impl;

//import com.aimprosoft.yesipov.web.Path;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.junit.Assert.*;

public class ListCommandTest extends Mockito {

    /*@Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @InjectMocks
    ListCommand listCommand;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void positiveSendTest() throws ServletException, IOException {
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getParameter("name")).thenReturn("departments");
        listCommand.execute(request, response);
        //assertEquals(request.getRequestDispatcher(Path.DEPARTMENTS_JSP), "/WEB-INF/jsp/departments.jsp");
        assertEquals(listCommand.execute(request, response), Path.DEPARTMENTS_JSP);
    }
    @Test
    public void negativeSendTest() throws ServletException, IOException {
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getParameter("name")).thenReturn(anyString());
        //new ListCommand().execute(request,response);
        assertEquals(listCommand.execute(request, response), Path.ALL_EMPLOYEES_JSP);
    }*/
}