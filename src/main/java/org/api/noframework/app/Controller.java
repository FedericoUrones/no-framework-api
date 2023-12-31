package org.api.noframework.app;

import com.sun.net.httpserver.HttpServer;
import org.api.noframework.app.api.Constants;
import org.api.noframework.app.api.task.Handler;

import java.io.IOException;
import java.net.InetSocketAddress;


class Controller {

    public static void main(String[] args) throws IOException {
        int serverPort = 8000;
        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);

        Handler handler = new Handler(Configuration.getTaskService(), Configuration.getObjectMapper(),
                Configuration.getErrorHandler());
        server.createContext(Constants.TASKS_PATH, handler::handle);

        server.setExecutor(null);
        server.start();
    }
}