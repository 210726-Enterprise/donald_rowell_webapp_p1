package com.revature.servlet;

import com.revature.service.AccountService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AccountServlet extends HttpServlet {

    private AccountService service;

    public AccountServlet(AccountService accountService){
        this.service = accountService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp){
        service.insertAccounts(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        service.getAccounts(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp){
        service.updateAccount(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp){
        service.deleteAccount(req, resp);
    }
}
