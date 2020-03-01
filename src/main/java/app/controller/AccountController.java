package app.controller;

import app.account.Account;
import app.account.impl.AccountDTO;
import app.service.AccountsService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

import static app.util.RequestUtil.toJson;
import static app.util.RequestUtil.*;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Serves requests
 */
public class AccountController {

    private static final AccountsService SERVICE = AccountsService.INSTANCE;

    public static Route handleNew = (Request request, Response response) -> {
        try {
            ObjectMapper mapper = new ObjectMapper();

            AccountDTO account = mapper.readValue(request.body(), AccountDTO.class);
            Account created = SERVICE.createAccount(account.getValue());
            response.status(HTTP_OK);
            response.type("application/json");
            return toJson(created);
        } catch (JsonParseException e) {
            response.status(HTTP_BAD_REQUEST);
            return "";
        }
    };

    public static Route handleList = (Request request, Response response) -> {
        Collection<Account> collection = SERVICE.getAllAccountsCopied();
        String res = toJson(collection);
        response.status(HTTP_OK);
        response.type("application/json");
        return res;
    };

    public static Route handleGet = (Request request, Response response) -> {
        Long id = getId(request);
        Account account = SERVICE.getAccount(id);
        return responseAccount(response, account, id);
    };

    public static Route handleDelete = (Request request, Response response) -> {
            Long id = getId(request);
            Account account = SERVICE.deleteAccount(id);
            return responseAccount(response, account, id);
    };

    public static Route handleUpdate = (Request request, Response response) -> {
        try {
            ObjectMapper mapper = new ObjectMapper();
            AccountDTO account = mapper.readValue(request.body(), AccountDTO.class);
            Account changed = SERVICE.changeAccount(account.getId(), account.getValue());
            return responseAccount(response, changed, account.getId());
        } catch (JsonParseException e) {
            response.status(HTTP_BAD_REQUEST);
            return "";
        }
    };

    public static Route handleTransfer = (Request request, Response response) -> {
        Long from = getFrom(request);
        Long to = getTo(request);
        double amount = getAmount(request);
        SERVICE.transfer(from, to, amount);
        response.status(HTTP_OK);
        response.type("application/json");
        return "";

    };


    private static Object responseAccount(@Nonnull Response response, @Nullable Account account, long id) {
        if(account != null) {
            response.status(HTTP_OK);
            response.type("application/json");
            return toJson(account);
        } else {
            return noSuchAccount(response, id);
        }
    }

    private static Object noSuchAccount(Response response, Long id) {
        response.status(HTTP_BAD_REQUEST);
        response.type("application/json");
        return toJson("Account with id '" + id + "' not found");
    }
}
