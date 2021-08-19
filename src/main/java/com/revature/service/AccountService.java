package com.revature.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.BankAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;

public class AccountService {
    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    private ORM<BankAccount> orm;
    private ObjectMapper mapper;

    public AccountService(){
        orm = new ORM(BankAccount.class);
        mapper = new ObjectMapper();
    }

    public void insertAccounts(HttpServletRequest req, HttpServletResponse resp) {

        try {
            StringBuilder builder = new StringBuilder();
            req.getReader().lines()
                .collect(Collectors.toList())
                .forEach(builder::append);

            BankAccount account = mapper.readValue(builder.toString(), BankAccount.class);
            int result = insert(account);

            if(result != 0){
                resp.setStatus(HttpServletResponse.SC_CREATED);
            } else{

                // TODO: refactor with exception propagation to set better status codes
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
            }

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            logger.warn(e.getMessage());
        }
    }

    public void getAccount(HttpServletRequest req, HttpServletResponse resp) {

    }

    public void updateAccount(HttpServletRequest req, HttpServletResponse resp) {

    }

    public void deleteAccount(HttpServletRequest req, HttpServletResponse resp) {

    }

    private int insert(BankAccount account){
        return ORM.
    }
}
