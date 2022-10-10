package Utility;

import Interfaces.HistoryManager;
import Interfaces.TaskManager;
import Server.KVServer;
import adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import service.FileBackedTasksManager;
import service.HTTPTaskManager;
import service.InMemoryHistoryManager;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

public final class Managers {

    private Managers() {
    }

    public static TaskManager getDefault() {
//        return new InMemoryTaskManager();
        return new HTTPTaskManager(KVServer.PORT);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefaultFile() {
        return new FileBackedTasksManager(new File("resources/tasks.csv"));
    }

    public static Gson getGson() {
        final var gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        return gsonBuilder.create();
    }

    public static KVServer getDefaultKVServer() throws IOException {
        return new KVServer();
    }
}
