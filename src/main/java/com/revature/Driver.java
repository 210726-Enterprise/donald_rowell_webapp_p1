package com.revature;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.revature.models.BankAccount;
import com.revature.models.CustInfo;
import com.revature.service.AccountService;
import com.revature.service.CustomerService;
import com.revature.servlet.AccountServlet;
import com.revature.servlet.CustomerServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class Driver implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ORM<BankAccount> ormAccount = new ORM<>(BankAccount.class);
        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        AccountService accountService = new AccountService(ormAccount, objectMapper);

        ORM<CustInfo> ormCustomer = new ORM<>(CustInfo.class);
        CustomerService customerService = new CustomerService(ormCustomer, objectMapper);

        ServletContext context = sce.getServletContext();
        context.addServlet("accountServlet", new AccountServlet(accountService)).addMapping("/accounts");
        context.addServlet("customerServlet", new CustomerServlet(customerService)).addMapping("/customers");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }
}
