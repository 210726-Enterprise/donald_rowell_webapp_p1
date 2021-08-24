package com.revature.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.revature.DML.Select;
import com.revature.ORM;
import com.revature.models.CustInfo;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.stream.Stream;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    CustomerService service;
    ORM<CustInfo> ormMock;
    ObjectMapper mapperMock;
    ObjectWriter writerMock;
    HttpServletRequest requestMock;
    HttpServletResponse responseMock;
    PrintWriter printWriterMock;
    String json;
    BufferedReader bufferedReaderMock;
    @Mock
    CustInfo custMock;
    @Mock
    Select selectMock;

    @BeforeEach
    void setUp() {
        ormMock = Mockito.mock(ORM.class);
        mapperMock = Mockito.mock(ObjectMapper.class);
        writerMock = Mockito.mock(ObjectWriter.class);
        requestMock = Mockito.mock(HttpServletRequest.class);
        responseMock = Mockito.mock(HttpServletResponse.class);
        printWriterMock = Mockito.mock(PrintWriter.class);
        bufferedReaderMock = Mockito.mock(BufferedReader.class);

        service = new CustomerService(ormMock, mapperMock);

        json = "test json";
        custMock = Mockito.mock(CustInfo.class);
        selectMock = Mockito.mock(Select.class);
    }

    @Test
    void insertCustomerTest() throws SQLException, InvocationTargetException, IllegalAccessException, IOException {
        when(mapperMock.readValue(anyString(), (TypeReference<Object>) any())).thenReturn(custMock);
        when(ormMock.insert(any())).thenReturn(1);
        when(requestMock.getReader()).thenReturn(bufferedReaderMock);
        when(requestMock.getReader().lines()).thenReturn(Stream.<String>builder().build());
        when(mapperMock.writerWithDefaultPrettyPrinter()).thenReturn(writerMock);
        when(mapperMock.writerWithDefaultPrettyPrinter().writeValueAsString(any())).thenReturn(json);
        when(responseMock.getWriter()).thenReturn(printWriterMock);
        doThrow(new RuntimeException()).when(custMock).setID(anyInt());

        service.insertCustomer(requestMock,responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_CREATED);
        verify(printWriterMock).print(json);
    }

    @Test
    void insertCustomerTestButBadRequest() throws IOException {
        when(requestMock.getReader()).thenThrow(new IOException());

        service.insertCustomer(requestMock,responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void insertCustomerTestButORMFailedWithSQL() throws SQLException, InvocationTargetException, IllegalAccessException, IOException {
        when(mapperMock.readValue(anyString(), (TypeReference<Object>) any())).thenReturn(custMock);
        when(ormMock.insert(any())).thenThrow(new SQLException());
        when(requestMock.getReader()).thenReturn(bufferedReaderMock);
        when(requestMock.getReader().lines()).thenReturn(Stream.<String>builder().build());

        service.insertCustomer(requestMock,responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_CONFLICT);
    }

    @Test
    void insertCustomerTestButORMFailedToAccessConstructor() throws SQLException, InvocationTargetException, IllegalAccessException, IOException {
        when(mapperMock.readValue(anyString(), (TypeReference<Object>) any())).thenReturn(custMock);
        when(ormMock.insert(any())).thenThrow(new IllegalAccessException());
        when(requestMock.getReader()).thenReturn(bufferedReaderMock);
        when(requestMock.getReader().lines()).thenReturn(Stream.<String>builder().build());

        service.insertCustomer(requestMock,responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_CONFLICT);
    }

    @Test
    void getCustomersTest() throws Exception {
        when(mapperMock.writerWithDefaultPrettyPrinter()).thenReturn(writerMock);
        when(mapperMock.writerWithDefaultPrettyPrinter().writeValueAsString(custMock)).thenReturn(json);
        when(responseMock.getWriter()).thenReturn(printWriterMock);
        when(requestMock.getParameter(anyString())).thenReturn("12");
        when(ormMock.select(any())).thenReturn(selectMock);
        when(ormMock.select(any()).where(anyString(), anyInt())).thenReturn(custMock);
        when(custMock.getID()).thenReturn(12);

        service.getCustomers(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_OK);
        verify(printWriterMock).print(json);
    }

    @Test
    void getCustomersTestFailToWriteJSONResponse() throws Exception {
        when(mapperMock.writerWithDefaultPrettyPrinter()).thenReturn(writerMock);
        when(mapperMock.writerWithDefaultPrettyPrinter().writeValueAsString(custMock)).thenReturn(json);
        when(responseMock.getWriter()).thenThrow(new IOException());
        when(requestMock.getParameter(anyString())).thenReturn("12");
        when(ormMock.select(any())).thenReturn(selectMock);
        when(ormMock.select(any()).where(anyString(), anyInt())).thenReturn(custMock);
        when(custMock.getID()).thenReturn(12);

        service.getCustomers(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    void getCustomersTestAccountNotFound() throws Exception {
        when(mapperMock.writerWithDefaultPrettyPrinter()).thenReturn(writerMock);
        when(mapperMock.writerWithDefaultPrettyPrinter().writeValueAsString(custMock)).thenReturn("null");
        when(responseMock.getWriter()).thenReturn(printWriterMock);
        when(requestMock.getParameter(anyString())).thenReturn("12");
        when(ormMock.select(any())).thenReturn(selectMock);
        when(ormMock.select(any()).where(anyString(), anyInt())).thenReturn(custMock);
        when(custMock.getID()).thenReturn(12);

        service.getCustomers(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void getCustomersTestModelCreationFailure() throws Exception {
        when(mapperMock.writerWithDefaultPrettyPrinter()).thenReturn(writerMock);
        when(mapperMock.writerWithDefaultPrettyPrinter().writeValueAsString(custMock)).thenReturn(json);
        when(responseMock.getWriter()).thenReturn(printWriterMock);
        when(requestMock.getParameter(anyString())).thenReturn("12");
        when(ormMock.select(any())).thenReturn(selectMock);
        when(ormMock.select(any()).where(anyString(), anyInt())).thenThrow(new InstantiationException());

        service.getCustomers(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_CONFLICT);
    }

    @Test
    void getCustomersTestSQLFailure() throws Exception {
        when(mapperMock.writerWithDefaultPrettyPrinter()).thenReturn(writerMock);
        when(mapperMock.writerWithDefaultPrettyPrinter().writeValueAsString(custMock)).thenReturn(json);
        when(responseMock.getWriter()).thenReturn(printWriterMock);
        when(requestMock.getParameter(anyString())).thenReturn("12");
        when(ormMock.select(any())).thenReturn(selectMock);
        when(ormMock.select(any()).where(anyString(), anyInt())).thenThrow(new SQLException());

        service.getCustomers(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_CONFLICT);
    }

    @Test
    void getCustomersTestEmptyRequest(){
        when(requestMock.getParameter(anyString())).thenReturn("");

        service.getCustomers(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void getCustomersTestAllAccounts(){
        when(requestMock.getParameter(anyString())).thenReturn(null);

        service.getCustomers(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
    }

    @Test
    void updateCustomerTest() throws SQLException, InvocationTargetException, IllegalAccessException, IOException {
        when(mapperMock.readValue(anyString(), (TypeReference<Object>) any())).thenReturn(custMock);
        when(mapperMock.writerWithDefaultPrettyPrinter()).thenReturn(writerMock);
        when(mapperMock.writerWithDefaultPrettyPrinter().writeValueAsString(custMock)).thenReturn(json);
        when(requestMock.getParameter(anyString())).thenReturn("12");
        when(requestMock.getReader()).thenReturn(bufferedReaderMock);
        when(requestMock.getReader().lines()).thenReturn(Stream.<String>builder().build());
        when(ormMock.update(any())).thenReturn(true);
        doReturn(12).when(custMock).getID();
        when(responseMock.getWriter()).thenReturn(printWriterMock);

        service.updateCustomer(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_OK);
        verify(printWriterMock).print(json);
    }

    @Test
    void updateCustomerUserInputIDZeroTest() throws SQLException, InvocationTargetException, IllegalAccessException, IOException {
        when(mapperMock.readValue(anyString(), (TypeReference<Object>) any())).thenReturn(custMock);
        when(mapperMock.writerWithDefaultPrettyPrinter()).thenReturn(writerMock);
        when(mapperMock.writerWithDefaultPrettyPrinter().writeValueAsString(custMock)).thenReturn(json);
        when(requestMock.getReader()).thenReturn(bufferedReaderMock);
        when(requestMock.getReader().lines()).thenReturn(Stream.<String>builder().build());
        when(ormMock.update(any())).thenReturn(true);
        when(custMock.getID()).thenReturn(12);
        when(responseMock.getWriter()).thenReturn(printWriterMock);

        service.updateCustomer(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void updateCustomerIOExceptionTest() throws IOException {
        when(requestMock.getReader()).thenThrow(new IOException());

        service.updateCustomer(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void updateCustomerSQLExceptionTest() throws SQLException, InvocationTargetException, IllegalAccessException, IOException {
        when(mapperMock.readValue(anyString(), (TypeReference<Object>) any())).thenReturn(custMock);
        when(mapperMock.writerWithDefaultPrettyPrinter()).thenReturn(writerMock);
        when(mapperMock.writerWithDefaultPrettyPrinter().writeValueAsString(custMock)).thenReturn(json);
        when(requestMock.getReader()).thenReturn(bufferedReaderMock);
        when(requestMock.getReader().lines()).thenReturn(Stream.<String>builder().build());
        when(ormMock.update(any())).thenThrow(new SQLException());
        when(responseMock.getWriter()).thenReturn(printWriterMock);
        when(custMock.getID()).thenReturn(12);

        service.updateCustomer(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void deleteCustomerTest() throws SQLException {
        when(requestMock.getParameter(anyString())).thenReturn("12");
        when(ormMock.delete(anyInt())).thenReturn(true);

        service.deleteCustomer(requestMock,responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void deleteCustomerFailTest() throws SQLException {
        when(requestMock.getParameter(anyString())).thenReturn("12");
        when(ormMock.delete(anyInt())).thenThrow(new SQLException());

        service.deleteCustomer(requestMock,responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_CONFLICT);
    }
}