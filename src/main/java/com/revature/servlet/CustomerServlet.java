package com.revature.servlet;

import com.revature.service.CustomerService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/customers")
public class CustomerServlet extends HttpServlet{

    private CustomerService service;

    public CustomerServlet(CustomerService service){
        this.service = service;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp){
        service.insertCustomer(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        service.getCustomers(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp){
        service.updateCustomer(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp){
        service.deleteCustomer(req, resp);
    }
}
