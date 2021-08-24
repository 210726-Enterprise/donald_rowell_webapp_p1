package com.revature.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.ORM;
import com.revature.models.BankAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class AccountService {
    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    private ORM<BankAccount> orm;
    private ObjectMapper mapper;

    public AccountService(ORM<BankAccount> orm, ObjectMapper mapper){
        this.mapper = mapper;
        this.orm = orm;
    }

    public void insertAccounts(HttpServletRequest req, HttpServletResponse resp) {

        try {
            StringBuilder builder = new StringBuilder();
            req.getReader().lines()
                .collect(Collectors.toList())
                .forEach(builder::append);

            BankAccount account = mapper.readValue(builder.toString(), BankAccount.class);
            int result = insert(account);

            if(result > 0){
                resp.setStatus(HttpServletResponse.SC_CREATED);
                account.setAccountID(result);
                String JSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(account);
                resp.getWriter().print(JSON);
            } else{
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
            }

        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            logger.warn(e.getMessage(), e);
        }
    }

    public void getAccounts(HttpServletRequest req, HttpServletResponse resp){
        if(req.getParameter("accountID") != null) {
            if(req.getParameter("accountID").equals("")) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            try {
                BankAccount account = getAccount(Integer.parseInt(req.getParameter("accountID")));
                if(account.getAccountID() == 0){
                    resp.setStatus(HttpServletResponse.SC_CONFLICT);
                    return;
                }
                String json = mapper
                        .writerWithDefaultPrettyPrinter()
                        .writeValueAsString(account);

                if(json.equals("null")) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }

                resp.getWriter().print(json);
                resp.setStatus(HttpServletResponse.SC_OK);

            } catch (IOException e) {
                logger.warn("Failed to get", e);
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

        } else {
            resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }
    }

    public void updateAccount(HttpServletRequest req, HttpServletResponse resp) {
        StringBuilder builder = new StringBuilder();
        try {

            req.getReader().lines()
                    .collect(Collectors.toList())
                    .forEach(builder::append);

            BankAccount account = mapper.readValue(builder.toString(), BankAccount.class);

            if(account.getAccountID() == 0){
                resp.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
            } else{
                boolean result = update(account);

                if(result){
                    resp.setStatus(HttpServletResponse.SC_OK);

                    String JSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(account);
                    resp.getWriter().print(JSON);
                }
            }

        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    public void deleteAccount(HttpServletRequest req, HttpServletResponse resp) {
        boolean result = delete(Integer.parseInt(req.getParameter("account_id")));

        if(result){
            resp.setStatus(HttpServletResponse.SC_OK);
        } else{
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
        }
    }

    private int insert(BankAccount account){
        try {
            return orm.insert(account);
        } catch (SQLException e) {
            logger.warn(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            logger.warn(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            logger.warn(e.getMessage(), e);
        }
        return -1;
    }

    private BankAccount getAccount(int id) {
        try {
            return (BankAccount) orm.select(BankAccount.class).where(id);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e){
            logger.warn("Failed to create BankAccount model, please check annotations.", e);
        } catch (Exception e) {
            logger.warn("Some other failure occurred", e);
        }
        return new BankAccount();
    }

    private boolean update(BankAccount account){
        try {
            return orm.update(account);
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
