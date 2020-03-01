package app;

import app.controller.AccountController;
import app.util.Path;

import static spark.Spark.*;

/**
 * Entry point of the AccountsApplication
 */
public class AccountsApplication {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        port(PORT);
        threadPool(8);

        get(Path.Web.GET, AccountController.handleList);
        get(Path.Web.GET_ID, AccountController.handleGet);
        post(Path.Web.NEW, "application/json", AccountController.handleNew);
        delete(Path.Web.DELETE, AccountController.handleDelete);
        put(Path.Web.UPDATE, "application/json", AccountController.handleUpdate);
        put(Path.Web.TRANSFER, "application/json", AccountController.handleTransfer);
    }
}
