package com.revature.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.ORM;
import com.revature.models.CustInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class CustomerService {
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    private ORM<CustInfo> orm;
    private ObjectMapper mapper;

    public CustomerService(ORM<CustInfo> orm, ObjectMapper mapper){
        this.orm = orm;
        this.mapper = mapper;
    }

    public void insertCustomer(HttpServletRequest req, HttpServletResponse resp) {

        try {
            StringBuilder builder = new StringBuilder();
            req.getReader().lines()
                    .collect(Collectors.toList())
                    .forEach(builder::append);

            CustInfo cust = mapper.readValue(builder.toString(), CustInfo.class);
            int result = insert(cust);

            if(result > 0){
                resp.setStatus(HttpServletResponse.SC_CREATED);
                cust.setID(result);
                String JSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cust);
                resp.getWriter().print(JSON);
            } else{
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
            }

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            logger.warn(e.getMessage());
        }
    }

    public void getCustomers(HttpServletRequest req, HttpServletResponse resp){
        if(req.getParameter("customer_id") != null) {
            if(req.getParameter("customer_id").equals("")) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            try {
                CustInfo ci = getCustomer("customer_id", Integer.parseInt(req.getParameter("customer_id")));
                if(ci.getID() == 0){
                    resp.setStatus(HttpServletResponse.SC_CONFLICT);
                    return;
                }
                String json = mapper
                        .writerWithDefaultPrettyPrinter()
                        .writeValueAsString(ci);

                if(json.equals("null")) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }

                resp.getWriter().print(json);
                resp.setStatus(HttpServletResponse.SC_OK);

            } catch (IOException e) {
                logger.warn(e.getMessage(), e);
            }

        } else {

            req.getParameterNames();
            resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }
    }

    public void updateCustomer(HttpServletRequest req, HttpServletResponse resp) {
        StringBuilder builder = new StringBuilder();
        try {

            req.getReader().lines()
                    .collect(Collectors.toList())
                    .forEach(builder::append);

            CustInfo cust = mapper.readValue(builder.toString(), CustInfo.class);

            if(cust.getID() != 0){
                boolean result = update(cust);

                if(result){
                    resp.setStatus(HttpServletResponse.SC_OK);

                    String JSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cust);
                    resp.getWriter().print(JSON);
                }

            } else{
                resp.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
            }

        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    public void deleteCustomer(HttpServletRequest req, HttpServletResponse resp) {
        boolean result = delete(Integer.parseInt(req.getParameter("customer_id")));

        if(result){
            resp.setStatus(HttpServletResponse.SC_OK);
        } else{
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
        }
    }

    private int insert(CustInfo cust){
        try {
            return orm.insert(cust);
        } catch (SQLException e) {
            logger.warn(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            logger.warn(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            logger.warn(e.getMessage(), e);
        }
        return -1;
    }

    private CustInfo getCustomer(String column, int id) {
        try {
            return (CustInfo) orm.select(CustInfo.class).where(column, id);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e){
            logger.warn("Failed to create CustInfo model, please check annotations.", e);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return new CustInfo();
    }

    private boolean update(CustInfo cust){
        try {
            return orm.update(cust);
        } catch (SQLException e) {
            logger.warn(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            logger.warn(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            logger.warn(e.getMessage(), e);
        }
        return false;
    }

    private boolean delete(int id) {
        try {
            return orm.delete(id);
        } catch (SQLException e) {
            logger.warn(e.getMessage(), e);
        }
        return false;
    }
}
