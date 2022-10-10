package Server;

import Interfaces.TaskManager;
import Utility.Managers;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

public class HttpTaskServer {
    private static TaskManager manager;
    private static final Gson gson = Managers.getGson();
    private final HttpServer httpServer;
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        final HttpTaskServer taskServer = new HttpTaskServer(Managers.getDefaultFile());
        taskServer.start();
    }

    public HttpTaskServer(TaskManager manager) throws IOException {
        this.manager = manager;
        httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        httpServer.createContext("/tasks", this::handler);
    }

    private void handler(HttpExchange httpExchange) {
        try {
            System.out.println("\n/tasks" + httpExchange.getRequestURI());
            final String path = httpExchange.getRequestURI().getPath().substring(7);
            System.out.println(path);
            switch (path) {
                case "":
                    getPrioritizedTasksHandler(httpExchange);
                    break;
                case "task":
                    tasksHandler(httpExchange);
                    break;
                case "epic":
                    epicHandler(httpExchange);
                    break;
                case "subtask":
                    subtaskHandler(httpExchange);
                    break;
                case "subtask/epic":
                    getEpicSubtasksHandler(httpExchange);
                    break;
                case "history":
                    historyHandler(httpExchange);
                    break;
            }
        } catch (Exception exception) {
            System.out.println("Ошибка при обработке запроса.");
        } finally {
            httpExchange.close();
        }
    }

    public void start() {
        System.out.println("Started httpTaskServer " + PORT);
        System.out.println("http://localhost:" + PORT + "/tasks");
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
    }

    private String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
    }

    private void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(DEFAULT_CHARSET);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

    private void tasksHandler(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        String query = httpExchange.getRequestURI().getQuery();
        switch (requestMethod) {
            case "GET": {
                if (Objects.nonNull(query)) {
                    String idString = query.substring(3);
                    int id = Integer.parseInt(idString);
                    Task task = manager.getTaskById(id);
                    String response = gson.toJson(task);
                    sendText(httpExchange, response);
                } else {
                    final List<Task> tasks = manager.getTasks();
                    final String response = gson.toJson(tasks);
                    sendText(httpExchange, response);
                }
                break;
            }

            case "POST": {
                String body = readText(httpExchange);
                if (body.isBlank()) {
                    httpExchange.sendResponseHeaders(400, 0);
                    return;
                }
                final Task task = gson.fromJson(body, Task.class);
                if (!manager.getTasks().contains(task)) {
                    manager.addTask(task);
                    final String response = gson.toJson(task.getId());
                    sendText(httpExchange, response);
                } else {
                    manager.updateTask(task);
                    httpExchange.sendResponseHeaders(200, 0);
                }
                final String response = gson.toJson(task);
                sendText(httpExchange, response);
            }
            case "DELETE": {
                if (Objects.nonNull(query)) {
                    String idString = query.substring(3);
                    int id = Integer.parseInt(idString);
                    manager.removeTasksById(id);
                    String response = gson.toJson(manager.getTasks());
                    sendText(httpExchange, response);
                } else {
                    manager.removeTasks();
                    final List<Task> tasks = manager.getTasks();
                    final String response = gson.toJson(tasks);
                    sendText(httpExchange, response);
                }
                break;
            }
            default: {
                httpExchange.sendResponseHeaders(400, 0);
                try (final var os = httpExchange.getResponseBody()) {
                    os.write(("Данный метод не можем обработать.").getBytes());
                    break;
                }
            }
        }

    }

    private void epicHandler(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        String query = httpExchange.getRequestURI().getQuery();
        switch (requestMethod) {
            case "GET": {
                if (Objects.nonNull(query)) {
                    String idString = query.substring(3);
                    int id = Integer.parseInt(idString);
                    Epic epic = manager.getEpicById(id);
                    String response = gson.toJson(epic);
                    sendText(httpExchange, response);
                } else {
                    final List<Epic> epics = manager.getEpics();
                    final String response = gson.toJson(epics);
                    sendText(httpExchange, response);
                }
            }
            case "POST": {
                String body = readText(httpExchange);
                if (body.isBlank()) {
                    httpExchange.sendResponseHeaders(400, 0);
                    return;
                }
                final Epic epic = gson.fromJson(body, Epic.class);
                if (!manager.getEpics().contains(epic)) {
                    manager.addEpic(epic);
                    final String response = gson.toJson(epic.getId());
                    sendText(httpExchange, response);
                } else {
                    manager.updateEpic(epic);
                    httpExchange.sendResponseHeaders(200, 0);
                }
                final String response = gson.toJson(epic);
                sendText(httpExchange, response);
            }
            case "DELETE": {
                if (Objects.nonNull(query)) {
                    String idString = query.substring(3);
                    int id = Integer.parseInt(idString);
                    manager.removeEpicById(id);
                    String response = gson.toJson(manager.getEpics());
                    sendText(httpExchange, response);
                } else {
                    manager.removeEpics();
                    final List<Epic> epics = manager.getEpics();
                    final String response = gson.toJson(epics);
                    sendText(httpExchange, response);
                }
                break;
            }
            default: {
                httpExchange.sendResponseHeaders(400, 0);
                try (final var os = httpExchange.getResponseBody()) {
                    os.write(("Данный метод не можем обработать.").getBytes());
                    break;
                }
            }
        }

    }

    private void subtaskHandler(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        String query = httpExchange.getRequestURI().getQuery();
        switch (requestMethod) {
            case "GET": {
                if (Objects.nonNull(query)) {
                    String idString = query.substring(3);
                    int id = Integer.parseInt(idString);
                    Subtask subtask = manager.getSubTaskById(id);
                    String response = gson.toJson(subtask);
                    sendText(httpExchange, response);
                } else {
                    final List<Subtask> subtasks = manager.getSubTasks();
                    final String response = gson.toJson(subtasks);
                    sendText(httpExchange, response);
                }
            }
            case "POST": {
                String body = readText(httpExchange);
                if (body.isBlank()) {
                    httpExchange.sendResponseHeaders(400, 0);
                    return;
                }
                final Subtask subtask = gson.fromJson(body, Subtask.class);
                if (!manager.getSubTasks().contains(subtask)) {
                    manager.addSubTask(subtask);
                    final String response = gson.toJson(subtask.getId());
                    sendText(httpExchange, response);
                } else {
                    manager.updateSubTask(subtask);
                    httpExchange.sendResponseHeaders(200, 0);
                }
                final String response = gson.toJson(subtask);
                sendText(httpExchange, response);
            }
            case "DELETE": {
                if (Objects.nonNull(query)) {
                    String idString = query.substring(3);
                    int id = Integer.parseInt(idString);
                    manager.removeSubTasksById(id);
                    String response = gson.toJson(manager.getSubTasks());
                    sendText(httpExchange, response);
                } else {
                    manager.removeSubTasks();
                    final List<Subtask> subtasks = manager.getSubTasks();
                    final String response = gson.toJson(subtasks);
                    sendText(httpExchange, response);
                }
                break;
            }
            default: {
                httpExchange.sendResponseHeaders(400, 0);
                try (final var os = httpExchange.getResponseBody()) {
                    os.write(("Данный метод не можем обработать.").getBytes());
                    break;
                }
            }
        }
    }

    private void getEpicSubtasksHandler(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        String query = httpExchange.getRequestURI().getQuery();
        switch (requestMethod) {
            case "GET": {
                if (Objects.nonNull(query)) {
                    String idString = query.substring(3);
                    int id = Integer.parseInt(idString);
                    Epic epic = manager.getEpicById(id);
                    List<Subtask> subtasks = manager.getEpicSubTasks(epic);
                    String response = gson.toJson(subtasks);
                    sendText(httpExchange, response);
                } else {
                    httpExchange.sendResponseHeaders(400, 0);
                    return;
                }
            }
        }
    }

    private void getPrioritizedTasksHandler(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        switch (requestMethod) {
            case "GET": {
                List<Task> prioritizedTasks = manager.getPrioritizedTasks();
                String response = gson.toJson(prioritizedTasks);
                sendText(httpExchange, response);
                break;
            }
            default: {
                httpExchange.sendResponseHeaders(400, 0);
                try (final var os = httpExchange.getResponseBody()) {
                    os.write(("Данный метод не можем обработать.").getBytes());
                    break;
                }
            }
        }
    }

    private void historyHandler(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        switch (requestMethod) {
            case "GET": {
                List<Task> history = manager.getHistory();
                String response = gson.toJson(history);
                sendText(httpExchange, response);
                break;
            }
            default:
                httpExchange.sendResponseHeaders(400, 0);
                try (final var os = httpExchange.getResponseBody()) {
                    os.write(("Данный метод не можем обработать.").getBytes());
                    break;
                }
        }
    }

}

