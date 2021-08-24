package com.revature.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.revature.DML.Select;
import com.revature.ORM;
import com.revature.model.BasicModel;
import com.revature.models.BankAccount;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    AccountService service;
    ORM<BankAccount> ormMock;
    ObjectMapper mapperMock;
    ObjectWriter writerMock;
    HttpServletRequest requestMock;
    HttpServletResponse responseMock;
    PrintWriter printWriterMock;
    String json;
    BufferedReader bufferedReaderMock;
    @Mock
    BankAccount accountMock;
    @Mock
    Select selectMock;
    int param;

    @BeforeEach
    void setUp() {
        ormMock = Mockito.mock(ORM.class);
        mapperMock = Mockito.mock(ObjectMapper.class);
        writerMock = Mockito.mock(ObjectWriter.class);
        requestMock = Mockito.mock(HttpServletRequest.class);
        responseMock = Mockito.mock(HttpServletResponse.class);
        printWriterMock = Mockito.mock(PrintWriter.class);
        bufferedReaderMock = Mockito.mock(BufferedReader.class);

        service = new AccountService(ormMock, mapperMock);

        json = "test json";
        accountMock = Mockito.mock(BankAccount.class);
        selectMock = Mockito.mock(Select.class);
    }

    @Test
    void insertAccountsTest() throws SQLException, InvocationTargetException, IllegalAccessException, IOException {
        when(mapperMock.readValue(anyString(), (TypeReference<Object>) any())).thenReturn(accountMock);
        when(ormMock.insert(any())).thenReturn(1);
        when(requestMock.getReader()).thenReturn(bufferedReaderMock);
        when(requestMock.getReader().lines()).thenReturn(Stream.<String>builder().build());
        when(mapperMock.writerWithDefaultPrettyPrinter()).thenReturn(writerMock);
        when(mapperMock.writerWithDefaultPrettyPrinter().writeValueAsString(any())).thenReturn(json);
        when(responseMock.getWriter()).thenReturn(printWriterMock);
        doThrow(new RuntimeException()).when(accountMock).setAccountID(anyInt());

        service.insertAccounts(requestMock,responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_CREATED);
        verify(printWriterMock).print(json);
    }

    @Test
    void insertAccountsTestButBadRequest() throws IOException {
        when(requestMock.getReader()).thenThrow(new IOException());

        service.insertAccounts(requestMock,responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void insertAccountsTestButORMFailedWithSQL() throws SQLException, InvocationTargetException, IllegalAccessException, IOException {
        when(mapperMock.readValue(anyString(), (TypeReference<Object>) any())).thenReturn(accountMock);
        when(ormMock.insert(any())).thenThrow(new SQLException());
        when(requestMock.getReader()).thenReturn(bufferedReaderMock);
        when(requestMock.getReader().lines()).thenReturn(Stream.<String>builder().build());

        service.insertAccounts(requestMock,responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_CONFLICT);
    }

    @Test
    void insertAccountsTestButORMFailedToAccessConstructor() throws SQLException, InvocationTargetException, IllegalAccessException, IOException {
        when(mapperMock.readValue(anyString(), (TypeReference<Object>) any())).thenReturn(accountMock);
        when(ormMock.insert(any())).thenThrow(new IllegalAccessException());
        when(requestMock.getReader()).thenReturn(bufferedReaderMock);
        when(requestMock.getReader().lines()).thenReturn(Stream.<String>builder().build());

        service.insertAccounts(requestMock,responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_CONFLICT);
    }

    @Test
    void getAccountsTest() throws Exception {
        when(mapperMock.writerWithDefaultPrettyPrinter()).thenReturn(writerMock);
        when(mapperMock.writerWithDefaultPrettyPrinter().writeValueAsString(accountMock)).thenReturn(json);
        when(responseMock.getWriter()).thenReturn(printWriterMock);
        when(requestMock.getParameter(anyString())).thenReturn("12");
        when(ormMock.select(any())).thenReturn(selectMock);
        when(ormMock.select(any()).where(anyString(), anyInt())).thenReturn(accountMock);
        when(accountMock.getAccountID()).thenReturn(12);

        service.getAccounts(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_OK);
        verify(printWriterMock).print(json);
    }

    @Test
    void getAccountsTestAccountNotFound() throws Exception {
        when(mapperMock.writerWithDefaultPrettyPrinter()).thenReturn(writerMock);
        when(mapperMock.writerWithDefaultPrettyPrinter().writeValueAsString(accountMock)).thenReturn("null");
        when(responseMock.getWriter()).thenReturn(printWriterMock);
        when(requestMock.getParameter(anyString())).thenReturn("12");
        when(ormMock.select(any())).thenReturn(selectMock);
        when(ormMock.select(any()).where(anyString(), anyInt())).thenReturn(accountMock);
        when(accountMock.getAccountID()).thenReturn(12);

        service.getAccounts(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void getAccountsTestModelCreationFailure() throws Exception {
        when(mapperMock.writerWithDefaultPrettyPrinter()).thenReturn(writerMock);
        when(mapperMock.writerWithDefaultPrettyPrinter().writeValueAsString(accountMock)).thenReturn(json);
        when(responseMock.getWriter()).thenReturn(printWriterMock);
        when(requestMock.getParameter(anyString())).thenReturn("12");
        when(ormMock.select(any())).thenReturn(selectMock);
        when(ormMock.select(any()).where(anyString(), anyInt())).thenThrow(new InstantiationException());

        service.getAccounts(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_CONFLICT);
    }

    @Test
    void getAccountsTestSQLFailure() throws Exception {
        when(mapperMock.writerWithDefaultPrettyPrinter()).thenReturn(writerMock);
        when(mapperMock.writerWithDefaultPrettyPrinter().writeValueAsString(accountMock)).thenReturn(json);
        when(responseMock.getWriter()).thenReturn(printWriterMock);
        when(requestMock.getParameter(anyString())).thenReturn("12");
        when(ormMock.select(any())).thenReturn(selectMock);
        when(ormMock.select(any()).where(anyString(), anyInt())).thenThrow(new SQLException());

        service.getAccounts(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_CONFLICT);
    }

    @Test
    void getAccountsTestEmptyRequest(){
        when(requestMock.getParameter(anyString())).thenReturn("");

        service.getAccounts(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void getAccountsTestAllAccounts(){
        when(requestMock.getParameter(anyString())).thenReturn(null);

        service.getAccounts(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
    }

    @Test
    void updateAccountTest() throws SQLException, InvocationTargetException, IllegalAccessException, IOException {
        when(mapperMock.readValue(anyString(), (TypeReference<Object>) any())).thenReturn(accountMock);
        when(mapperMock.writerWithDefaultPrettyPrinter()).thenReturn(writerMock);
        when(mapperMock.writerWithDefaultPrettyPrinter().writeValueAsString(accountMock)).thenReturn(json);
        when(requestMock.getParameter(anyString())).thenReturn("12");
        when(requestMock.getReader()).thenReturn(bufferedReaderMock);
        when(requestMock.getReader().lines()).thenReturn(Stream.<String>builder().build());
        when(ormMock.update(any())).thenReturn(true);
        doReturn(12).when(accountMock).getAccountID();
        when(responseMock.getWriter()).thenReturn(printWriterMock);

        service.updateAccount(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_OK);
        verify(printWriterMock).print(json);
    }

    @Test
    void updateAccountUserInputIDZeroTest() throws SQLException, InvocationTargetException, IllegalAccessException, IOException {
        when(mapperMock.readValue(anyString(), (TypeReference<Object>) any())).thenReturn(accountMock);
        when(mapperMock.writerWithDefaultPrettyPrinter()).thenReturn(writerMock);
        when(mapperMock.writerWithDefaultPrettyPrinter().writeValueAsString(accountMock)).thenReturn(json);
        when(requestMock.getReader()).thenReturn(bufferedReaderMock);
        when(requestMock.getReader().lines()).thenReturn(Stream.<String>builder().build());
        when(ormMock.update(any())).thenReturn(true);
        when(accountMock.getAccountID()).thenReturn(12);
        when(responseMock.getWriter()).thenReturn(printWriterMock);

        service.updateAccount(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void updateAccountIOExceptionTest() throws IOException {
        when(requestMock.getReader()).thenThrow(new IOException());

        service.updateAccount(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void updateAccountSQLExceptionTest() throws SQLException, InvocationTargetException, IllegalAccessException, IOException {
        when(mapperMock.readValue(anyString(), (TypeReference<Object>) any())).thenReturn(accountMock);
        when(mapperMock.writerWithDefaultPrettyPrinter()).thenReturn(writerMock);
        when(mapperMock.writerWithDefaultPrettyPrinter().writeValueAsString(accountMock)).thenReturn(json);
        when(requestMock.getReader()).thenReturn(bufferedReaderMock);
        when(requestMock.getReader().lines()).thenReturn(Stream.<String>builder().build());
        when(ormMock.update(any())).thenThrow(new SQLException());
        when(responseMock.getWriter()).thenReturn(printWriterMock);
        when(accountMock.getAccountID()).thenReturn(12);

        service.updateAccount(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void deleteAccountTest() throws SQLException {
        when(requestMock.getParameter(anyString())).thenReturn("12");
        when(ormMock.delete(anyInt())).thenReturn(true);

        service.deleteAccount(requestMock,responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void deleteAccountFailTest() throws SQLException {
        when(requestMock.getParameter(anyString())).thenReturn("12");
        when(ormMock.delete(anyInt())).thenThrow(new SQLException());

        service.deleteAccount(requestMock,responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_CONFLICT);
    }
}