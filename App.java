package com.kismet;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class App {
    private static DataStore dataStore = new DataStore();

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        
        // Routes
        server.createContext("/", new LandingHandler());
        server.createContext("/dashboard", new DashboardHandler());
        server.createContext("/schedule", new ScheduleHandler());
        server.createContext("/analytics", new AnalyticsHandler());
        server.createContext("/api/tasks", new TaskAPIHandler());
        server.createContext("/api/energy", new EnergyAPIHandler());
        server.createContext("/api/optimize", new OptimizeAPIHandler());

        server.setExecutor(null);
        System.out.println("🚀 KISMET App successfully running at http://localhost:8080");
        server.start();
    }

    // ============ LANDING PAGE - ENGLISH ============
    static class LandingHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String htmlResponse = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>KISMET - Fate; Destiny</title>
                <style>
                    * {
                        margin: 0;
                        padding: 0;
                        box-sizing: border-box;
                    }
                    
                    body {
                        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
                        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                        min-height: 100vh;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        overflow: hidden;
                    }

                    .background-orbs {
                        position: fixed;
                        top: 0;
                        left: 0;
                        width: 100%;
                        height: 100%;
                        overflow: hidden;
                        z-index: 0;
                    }

                    .orb {
                        position: absolute;
                        border-radius: 50%;
                        opacity: 0.1;
                        animation: float 20s infinite ease-in-out;
                    }

                    .orb1 { width: 300px; height: 300px; top: -150px; left: -150px; animation-delay: 0s; }
                    .orb2 { width: 400px; height: 400px; bottom: -200px; right: -200px; animation-delay: 5s; }
                    .orb3 { width: 200px; height: 200px; top: 50%; left: 50%; animation-delay: 10s; }

                    @keyframes float {
                        0%, 100% { transform: translate(0, 0); }
                        33% { transform: translate(30px, -30px); }
                        66% { transform: translate(-30px, 30px); }
                    }

                    .container {
                        text-align: center;
                        z-index: 10;
                        position: relative;
                    }

                    .icon {
                        font-size: 80px;
                        margin-bottom: 20px;
                        animation: spin 3s linear infinite;
                    }

                    @keyframes spin {
                        0% { transform: rotate(0deg); }
                        100% { transform: rotate(360deg); }
                    }

                    h1 {
                        font-size: 72px;
                        font-weight: 700;
                        color: white;
                        margin-bottom: 10px;
                        letter-spacing: 2px;
                    }

                    .subtitle {
                        font-size: 24px;
                        color: rgba(255, 255, 255, 0.9);
                        margin-bottom: 30px;
                        font-weight: 300;
                    }

                    .description {
                        font-size: 16px;
                        color: rgba(255, 255, 255, 0.85);
                        max-width: 500px;
                        margin: 0 auto 40px auto;
                        line-height: 1.6;
                    }

                    .btn {
                        display: inline-block;
                        padding: 15px 50px;
                        background: white;
                        color: #667eea;
                        text-decoration: none;
                        border-radius: 50px;
                        font-weight: 600;
                        font-size: 16px;
                        transition: all 0.3s ease;
                        cursor: pointer;
                        border: none;
                    }

                    .btn:hover {
                        transform: translateY(-3px);
                        box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
                    }

                    .divider {
                        width: 100px;
                        height: 2px;
                        background: rgba(255, 255, 255, 0.3);
                        margin: 30px auto;
                    }
                </style>
            </head>
            <body>
                <div class="background-orbs">
                    <div class="orb orb1" style="background: white;"></div>
                    <div class="orb orb2" style="background: white;"></div>
                    <div class="orb orb3" style="background: white;"></div>
                </div>

                <div class="container">
                    <div class="icon">✨</div>
                    <h1>KISMET</h1>
                    <p class="subtitle">Fate; Destiny</p>
                    <div class="divider"></div>
                    <p class="description">
                        Your destiny scheduler awaits. Let your fate guide you to the optimal time for your passions and transform your dance journey into a masterpiece.
                    </p>
                    <a href="/dashboard" class="btn">Enter Your Destiny</a>
                </div>
            </body>
            </html>
            """;

            sendResponse(exchange, htmlResponse);
        }
    }

    // ============ DASHBOARD - ALBANIAN ============
    static class DashboardHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            List<Task> tasks = dataStore.getAllTasks();
            int energyLevel = dataStore.getEnergyLevel();
            OptimalTime optimal = EnergyOptimizer.findOptimalDanceTime(energyLevel, tasks);
            
            int completed = (int) tasks.stream().filter(t -> t.completed).count();
            int total = tasks.size();

            String htmlResponse = """
            <!DOCTYPE html>
            <html lang="sq">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>KISMET - Tabloja</title>
                <style>
                    * { margin: 0; padding: 0; box-sizing: border-box; }
                    
                    body {
                        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
                        background: #fbfbfa;
                        color: #37352f;
                    }

                    .header {
                        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                        color: white;
                        padding: 30px;
                        text-align: center;
                    }

                    .header h1 { font-size: 36px; margin-bottom: 5px; }
                    .header p { opacity: 0.9; }

                    .container {
                        max-width: 1200px;
                        margin: 0 auto;
                        padding: 30px;
                    }

                    .nav-tabs {
                        display: flex;
                        gap: 20px;
                        margin-bottom: 30px;
                        border-bottom: 2px solid #e9e9e6;
                        padding-bottom: 15px;
                    }

                    .nav-tabs a {
                        text-decoration: none;
                        color: #7c7b77;
                        font-weight: 500;
                        padding: 10px 0;
                        border-bottom: 3px solid transparent;
                        transition: all 0.3s;
                    }

                    .nav-tabs a:hover { color: #667eea; }
                    .nav-tabs a.active {
                        color: #667eea;
                        border-bottom-color: #667eea;
                    }

                    .grid {
                        display: grid;
                        grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
                        gap: 20px;
                        margin-bottom: 30px;
                    }

                    .card {
                        background: white;
                        border: 1px solid #e9e9e6;
                        border-radius: 12px;
                        padding: 20px;
                        box-shadow: 0 1px 3px rgba(0,0,0,0.05);
                        transition: all 0.2s;
                    }

                    .card:hover {
                        transform: translateY(-4px);
                        box-shadow: 0 4px 12px rgba(0,0,0,0.08);
                    }

                    .card h3 { font-size: 14px; color: #7c7b77; margin-bottom: 8px; text-transform: uppercase; }
                    .card .value { font-size: 32px; font-weight: 700; color: #667eea; }

                    .stat-badge {
                        display: inline-block;
                        background: #edf6ec;
                        color: #437943;
                        padding: 8px 12px;
                        border-radius: 6px;
                        font-size: 13px;
                        font-weight: 500;
                    }

                    .optimal-pill {
                        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                        color: white;
                        padding: 20px;
                        border-radius: 12px;
                        margin-bottom: 30px;
                        text-align: center;
                    }

                    .optimal-pill h2 { font-size: 18px; margin-bottom: 8px; }
                    .optimal-pill p { font-size: 24px; font-weight: 700; }

                    .form-section {
                        background: white;
                        border: 1px solid #e9e9e6;
                        border-radius: 12px;
                        padding: 20px;
                        margin-bottom: 30px;
                    }

                    .form-section h3 { margin-bottom: 15px; }

                    input, select, textarea {
                        width: 100%;
                        padding: 10px;
                        margin-bottom: 10px;
                        border: 1px solid #e9e9e6;
                        border-radius: 6px;
                        font-family: inherit;
                    }

                    .form-row {
                        display: grid;
                        grid-template-columns: 1fr 1fr;
                        gap: 10px;
                    }

                    .btn {
                        background: #667eea;
                        color: white;
                        padding: 10px 20px;
                        border: none;
                        border-radius: 6px;
                        cursor: pointer;
                        font-weight: 500;
                        transition: all 0.2s;
                    }

                    .btn:hover { background: #764ba2; }

                    .tasks-grid {
                        display: grid;
                        grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
                        gap: 15px;
                    }

                    .task-card {
                        background: white;
                        border-left: 4px solid #667eea;
                        border-radius: 8px;
                        padding: 15px;
                        box-shadow: 0 1px 3px rgba(0,0,0,0.05);
                    }

                    .task-card.completed {
                        opacity: 0.6;
                        border-left-color: #437943;
                    }

                    .tag {
                        display: inline-block;
                        font-size: 11px;
                        padding: 3px 8px;
                        border-radius: 4px;
                        background: #f1f1ef;
                        margin-bottom: 8px;
                        font-weight: 500;
                    }

                    .tag.dance { background: #fce4ec; color: #c2185b; }
                    .tag.work { background: #e3f2fd; color: #1976d2; }
                    .tag.health { background: #edf6ec; color: #437943; }
                    .tag.break { background: #fff3e0; color: #f57c00; }
                </style>
            </head>
            <body>
                <div class="header">
                    <h1>✨ KISMET</h1>
                    <p>Tabloja e Ditës</p>
                </div>

                <div class="container">
                    <div class="nav-tabs">
                        <a href="/dashboard" class="active">Tabloja</a>
                        <a href="/schedule">Orari</a>
                        <a href="/analytics">Analitika</a>
                    </div>

                    <div class="grid">
                        <div class="card">
                            <h3>Niveli i Energjisë</h3>
                            <div class="value">""" + energyLevel + """/5</div>
                        </div>
                        <div class="card">
                            <h3>Detyrat e Përfunduara</h3>
                            <div class="value">""" + completed + """/""" + total + """</div>
                        </div>
                        <div class="card">
                            <h3>Përqindja</h3>
                            <div class="value">""" + (total > 0 ? (completed * 100 / total) : 0) + """%</div>
                        </div>
                    </div>

                    <div class="optimal-pill">
                        <h2>⏰ Koha Optimale për Vallëzim</h2>
                        <p>""" + optimal.getTime() + """</p>
                        <p style="font-size: 14px; margin-top: 8px;">Përshkrimi: """ + optimal.getDescription() + """</p>
                    </div>

                    <div class="form-section">
                        <h3>➕ Shto një Detyra të Re</h3>
                        <form action="/api/tasks" method="POST">
                            <input type="text" name="title" placeholder="Titull detyra" required>
                            <div class="form-row">
                                <select name="category" required>
                                    <option value="">Zgjidh Kategorinë</option>
                                    <option value="dance">💃 Vallëzim</option>
                                    <option value="work">💼 Puna</option>
                                    <option value="health">🏋️ Shëndeti</option>
                                    <option value="break">☕ Pushim</option>
                                </select>
                                <select name="energy" required>
                                    <option value="">Energjia e Kërkuar</option>
                                    <option value="1">1 - Shumë e Lehtë</option>
                                    <option value="2">2 - E Lehtë</option>
                                    <option value="3">3 - Mesatare</option>
                                    <option value="4">4 - E Vështirë</option>
                                    <option value="5">5 - Shumë e Vështirë</option>
                                </select>
                            </div>
                            <textarea name="description" placeholder="Përshkrim (opsional)" rows="2"></textarea>
                            <button type="submit" class="btn">Shto Detyra</button>
                        </form>
                    </div>

                    <h3>📋 Detyrat e Ditës</h3>
                    <div class="tasks-grid">
            """ + tasks.stream().map(task -> """
                        <div class="task-card """ + (task.completed ? "completed" : "") + """">
                            <span class="tag """ + task.category + """">""" + getCategoryEmoji(task.category) + """ """ + getCategoryName(task.category) + """</span>
                            <h4>""" + task.title + """</h4>
                            <p style="font-size: 13px; color: #666; margin: 8px 0;">""" + task.description + """</p>
                            <p style="font-size: 12px; color: #999;">⚡ Energjia: """ + task.energyRequired + """/5</p>
                        </div>
            """).reduce("", String::concat) + """
                    </div>
                </div>
            </body>
            </html>
            """;

            sendResponse(exchange, htmlResponse);
        }
    }

    // ============ SCHEDULE - ALBANIAN ============
    static class ScheduleHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            List<Task> tasks = dataStore.getAllTasks();
            
            String htmlResponse = """
            <!DOCTYPE html>
            <html lang="sq">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>KISMET - Orari</title>
                <style>
                    * { margin: 0; padding: 0; box-sizing: border-box; }
                    
                    body {
                        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
                        background: #fbfbfa;
                        color: #37352f;
                    }

                    .header {
                        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                        color: white;
                        padding: 30px;
                        text-align: center;
                    }

                    .header h1 { font-size: 36px; margin-bottom: 5px; }

                    .container {
                        max-width: 1200px;
                        margin: 0 auto;
                        padding: 30px;
                    }

                    .nav-tabs {
                        display: flex;
                        gap: 20px;
                        margin-bottom: 30px;
                        border-bottom: 2px solid #e9e9e6;
                        padding-bottom: 15px;
                    }

                    .nav-tabs a {
                        text-decoration: none;
                        color: #7c7b77;
                        font-weight: 500;
                        padding: 10px 0;
                        border-bottom: 3px solid transparent;
                        transition: all 0.3s;
                    }

                    .nav-tabs a.active {
                        color: #667eea;
                        border-bottom-color: #667eea;
                    }

                    table {
                        width: 100%;
                        border-collapse: collapse;
                        background: white;
                        border-radius: 12px;
                        overflow: hidden;
                        box-shadow: 0 1px 3px rgba(0,0,0,0.05);
                    }

                    thead {
                        background: #f5f5f3;
                    }

                    th {
                        padding: 15px;
                        text-align: left;
                        font-weight: 600;
                        color: #7c7b77;
                        font-size: 13px;
                        text-transform: uppercase;
                    }

                    td {
                        padding: 15px;
                        border-bottom: 1px solid #e9e9e6;
                    }

                    tr:last-child td {
                        border-bottom: none;
                    }

                    .tag {
                        display: inline-block;
                        font-size: 12px;
                        padding: 6px 12px;
                        border-radius: 4px;
                        background: #f1f1ef;
                        font-weight: 500;
                    }

                    .tag.dance { background: #fce4ec; color: #c2185b; }
                    .tag.work { background: #e3f2fd; color: #1976d2; }
                    .tag.health { background: #edf6ec; color: #437943; }
                    .tag.break { background: #fff3e0; color: #f57c00; }

                    .status {
                        display: inline-flex;
                        align-items: center;
                        gap: 5px;
                        padding: 5px 10px;
                        border-radius: 4px;
                        font-size: 12px;
                        font-weight: 500;
                    }

                    .status.completed {
                        background: #edf6ec;
                        color: #437943;
                    }

                    .status.pending {
                        background: #fff3e0;
                        color: #f57c00;
                    }

                    .energy-bar {
                        display: inline-flex;
                        gap: 3px;
                    }

                    .energy-dot {
                        width: 8px;
                        height: 8px;
                        border-radius: 50%;
                        background: #e9e9e6;
                    }

                    .energy-dot.filled {
                        background: #667eea;
                    }
                </style>
            </head>
            <body>
                <div class="header">
                    <h1>📅 KISMET</h1>
                    <p>Orari i Detyrave</p>
                </div>

                <div class="container">
                    <div class="nav-tabs">
                        <a href="/dashboard">Tabloja</a>
                        <a href="/schedule" class="active">Orari</a>
                        <a href="/analytics">Analitika</a>
                    </div>

                    <table>
                        <thead>
                            <tr>
                                <th>Titull</th>
                                <th>Kategoria</th>
                                <th>Energjia</th>
                                <th>Përshkrim</th>
                                <th>Statusi</th>
                            </tr>
                        </thead>
                        <tbody>
            """ + tasks.stream().map(task -> """
                            <tr>
                                <td><strong>""" + task.title + """</strong></td>
                                <td><span class="tag """ + task.category + """">""" + getCategoryEmoji(task.category) + """ """ + getCategoryName(task.category) + """</span></td>
                                <td>
                                    <div class="energy-bar">
            """ + generateEnergyDots(task.energyRequired) + """
                                    </div>
                                </td>
                                <td>""" + task.description + """</td>
                                <td>
                                    <span class="status """ + (task.completed ? "completed" : "pending") + """">
                                        """ + (task.completed ? "✅ Përfunduar" : "⏳ Në Progres") + """
                                    </span>
                                </td>
                            </tr>
            """).reduce("", String::concat) + """
                        </tbody>
                    </table>
                </div>
            </body>
            </html>
            """;

            sendResponse(exchange, htmlResponse);
        }
    }

    // ============ ANALYTICS - ALBANIAN ============
    static class AnalyticsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            List<Task> tasks = dataStore.getAllTasks();
            int completed = (int) tasks.stream().filter(t -> t.completed).count();
            int pending = tasks.size() - completed;

            Map<String, Integer> categoryCount = new HashMap<>();
            categoryCount.put("Vallëzim", (int) tasks.stream().filter(t -> t.category.equals("dance")).count());
            categoryCount.put("Puna", (int) tasks.stream().filter(t -> t.category.equals("work")).count());
            categoryCount.put("Shëndeti", (int) tasks.stream().filter(t -> t.category.equals("health")).count());
            categoryCount.put("Pushim", (int) tasks.stream().filter(t -> t.category.equals("break")).count());

            String htmlResponse = """
            <!DOCTYPE html>
            <html lang="sq">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>KISMET - Analitika</title>
                <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
                <style>
                    * { margin: 0; padding: 0; box-sizing: border-box; }
                    
                    body {
                        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
                        background: #fbfbfa;
                        color: #37352f;
                    }

                    .header {
                        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                        color: white;
                        padding: 30px;
                        text-align: center;
                    }

                    .header h1 { font-size: 36px; margin-bottom: 5px; }

                    .container {
                        max-width: 1200px;
                        margin: 0 auto;
                        padding: 30px;
                    }

                    .nav-tabs {
                        display: flex;
                        gap: 20px;
                        margin-bottom: 30px;
                        border-bottom: 2px solid #e9e9e6;
                        padding-bottom: 15px;
                    }

                    .nav-tabs a {
                        text-decoration: none;
                        color: #7c7b77;
                        font-weight: 500;
                        padding: 10px 0;
                        border-bottom: 3px solid transparent;
                        transition: all 0.3s;
                    }

                    .nav-tabs a.active {
                        color: #667eea;
                        border-bottom-color: #667eea;
                    }

                    .grid {
                        display: grid;
                        grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
                        gap: 20px;
                        margin-bottom: 30px;
                    }

                    .chart-card {
                        background: white;
                        border: 1px solid #e9e9e6;
                        border-radius: 12px;
                        padding: 20px;
                        box-shadow: 0 1px 3px rgba(0,0,0,0.05);
                    }

                    .chart-card h3 { margin-bottom: 20px; color: #37352f; }

                    .metrics {
                        display: grid;
                        grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
                        gap: 15px;
                        margin-top: 30px;
                    }

                    .metric {
                        background: white;
                        border: 1px solid #e9e9e6;
                        border-radius: 12px;
                        padding: 20px;
                        text-align: center;
                    }

                    .metric h4 { color: #7c7b77; font-size: 12px; text-transform: uppercase; margin-bottom: 10px; }
                    .metric .value { font-size: 28px; font-weight: 700; color: #667eea; }

                    .insights {
                        background: #edf6ec;
                        border-left: 4px solid #437943;
                        border-radius: 8px;
                        padding: 20px;
                        margin-top: 30px;
                    }

                    .insights h3 { color: #437943; margin-bottom: 10px; }
                    .insights p { color: #4a5d4a; line-height: 1.6; }
                </style>
            </head>
            <body>
                <div class="header">
                    <h1>📊 KISMET</h1>
                    <p>Analitika</p>
                </div>

                <div class="container">
                    <div class="nav-tabs">
                        <a href="/dashboard">Tabloja</a>
                        <a href="/schedule">Orari</a>
                        <a href="/analytics" class="active">Analitika</a>
                    </div>

                    <div class="grid">
                        <div class="chart-card">
                            <h3>📈 Statusi i Përfundimit</h3>
                            <canvas id="completionChart"></canvas>
                        </div>
                        <div class="chart-card">
                            <h3>📌 Shpërndarja sipas Kategorive</h3>
                            <canvas id="categoryChart"></canvas>
                        </div>
                    </div>

                    <div class="metrics">
                        <div class="metric">
                            <h4>Detyrat e Përfunduara</h4>
                            <div class="value">""" + completed + """</div>
                        </div>
                        <div class="metric">
                            <h4>Detyrat në Progres</h4>
                            <div class="value">""" + pending + """</div>
                        </div>
                        <div class="metric">
                            <h4>Përqindja e Përfundimit</h4>
                            <div class="value">""" + (tasks.size() > 0 ? (completed * 100 / tasks.size()) : 0) + """%</div>
                        </div>
                        <div class="metric">
                            <h4>Totali i Detyrave</h4>
                            <div class="value">""" + tasks.size() + """</div>
                        </div>
                    </div>

                    <div class="insights">
                        <h3>💡 Njohuri Themelore</h3>
                        <p>
                            Ju keni përfunduar <strong>""" + completed + """</strong> detyra deri më tani.
                            Kategorija më e përdorur është <strong>""" + getCategoryName(getMostUsedCategory(tasks)) + """</strong>.
                            Vazhdoni me këtë ritëm për të arritur objektivat tuaja të ditës! 🚀
                        </p>
                    </div>
                </div>

                <script>
                    // Completion Chart
                    const completionCtx = document.getElementById('completionChart').getContext('2d');
                    new Chart(completionCtx, {
                        type: 'doughnut',
                        data: {
                            labels: ['Përfunduar', 'Në Progres'],
                            datasets: [{
                                data: [""" + completed + """, """ + pending + """],
                                backgroundColor: ['#437943', '#f57c00'],
                                borderColor: ['#fff', '#fff'],
                                borderWidth: 2
                            }]
                        },
                        options: {
                            responsive: true,
                            plugins: { legend: { position: 'bottom' } }
                        }
                    });

                    // Category Chart
                    const categoryCtx = document.getElementById('categoryChart').getContext('2d');
                    new Chart(categoryCtx, {
                        type: 'pie',
                        data: {
                            labels: ['Vallëzim', 'Puna', 'Shëndeti', 'Pushim'],
                            datasets: [{
                                data: [""" + categoryCount.get("Vallëzim") + """, """ + categoryCount.get("Puna") + """, """ + categoryCount.get("Shëndeti") + """, """ + categoryCount.get("Pushim") + """],
                                backgroundColor: ['#e91e63', '#2196f3', '#4caf50', '#ff9800'],
                                borderColor: ['#fff', '#fff', '#fff', '#fff'],
                                borderWidth: 2
                            }]
                        },
                        options: {
                            responsive: true,
                            plugins: { legend: { position: 'bottom' } }
                        }
                    });
                </script>
            </body>
            </html>
            """;

            sendResponse(exchange, htmlResponse);
        }
    }

    // ============ API HANDLERS ============
    static class TaskAPIHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                String query = new String(exchange.getRequestBody().readAllBytes());
                String[] params = query.split("&");
                Map<String, String> map = new HashMap<>();
                
                for (String param : params) {
                    String[] pair = param.split("=");
                    map.put(pair[0], pair.length > 1 ? java.net.URLDecoder.decode(pair[1], "UTF-8") : "");
                }

                Task task = new Task();
                task.id = UUID.randomUUID().toString();
                task.title = map.get("title");
                task.category = map.get("category");
                task.energyRequired = Integer.parseInt(map.getOrDefault("energy", "3"));
                task.description = map.getOrDefault("description", "");
                task.completed = false;

                dataStore.addTask(task);

                exchange.getResponseHeaders().set("Location", "/dashboard");
                exchange.sendResponseHeaders(302, 0);
            }
            exchange.close();
        }
    }

    static class EnergyAPIHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                String query = new String(exchange.getRequestBody().readAllBytes());
                String[] params = query.split("&");
                
                for (String param : params) {
                    String[] pair = param.split("=");
                    if ("level".equals(pair[0])) {
                        int level = Integer.parseInt(pair[1]);
                        dataStore.setEnergyLevel(level);
                    }
                }
                
                exchange.getResponseHeaders().set("Location", "/dashboard");
                exchange.sendResponseHeaders(302, 0);
            }
            exchange.close();
        }
    }

    static class OptimizeAPIHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            int energyLevel = dataStore.getEnergyLevel();
            List<Task> tasks = dataStore.getAllTasks();
            OptimalTime optimal = EnergyOptimizer.findOptimalDanceTime(energyLevel, tasks);
            
            String response = """
            {
                "time": \"""" + optimal.getTime() + """\",
                "description": \"""" + optimal.getDescription() + """\",
                "energy": """ + energyLevel + """
            }
            """;
            
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.close();
        }
    }

    // ============ UTILITY METHODS ============
    static String getCategoryEmoji(String category) {
        return switch (category) {
            case "dance" -> "💃";
            case "work" -> "💼";
            case "health" -> "🏋️";
            case "break" -> "☕";
            default -> "📌";
        };
    }

    static String getCategoryName(String category) {
        return switch (category) {
            case "dance" -> "Vallëzim";
            case "work" -> "Puna";
            case "health" -> "Shëndeti";
            case "break" -> "Pushim";
            default -> "Të Tjera";
        };
    }

    static String generateEnergyDots(int energy) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            sb.append("<div class='energy-dot").append(i < energy ? " filled" : "").append("'></div>");
        }
        return sb.toString();
    }

    static String getMostUsedCategory(List<Task> tasks) {
        if (tasks.isEmpty()) return "dance";
        return tasks.stream()
            .map(t -> t.category)
            .reduce((a, b) -> {
                long countA = tasks.stream().filter(t -> t.category.equals(a)).count();
                long countB = tasks.stream().filter(t -> t.category.equals(b)).count();
                return countA >= countB ? a : b;
            }).orElse("dance");
    }

    static void sendResponse(HttpExchange exchange, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(200, response.getBytes().length);
        exchange.getResponseBody().write(response.getBytes());
        exchange.close();
    }
}

// ============ SUPPORTING CLASSES ============

class Task {
    String id;
    String title;
    String category;
    String description;
    int energyRequired;
    boolean completed;
}

class OptimalTime {
    private String time;
    private String description;

    public OptimalTime(String time, String description) {
        this.time = time;
        this.description = description;
    }

    public String getTime() { return time; }
    public String getDescription() { return description; }
}

class DataStore {
    private List<Task> tasks = new ArrayList<>();
    private int energyLevel = 3;

    public void addTask(Task task) { tasks.add(task); }
    public List<Task> getAllTasks() { return new ArrayList<>(tasks); }
    public void setEnergyLevel(int level) { this.energyLevel = Math.max(1, Math.min(5, level)); }
    public int getEnergyLevel() { return energyLevel; }
}

class EnergyOptimizer {
    public static OptimalTime findOptimalDanceTime(int energyLevel, List<Task> tasks) {
        if (energyLevel <= 2) {
            return new OptimalTime("10:00 AM - 11:00 AM", "Koha e hershme - më pak detyra");
        } else if (energyLevel == 3) {
            return new OptimalTime("11:00 AM - 1:00 PM", "Orë mesatare - balancim ideal");
        } else {
            return new OptimalTime("2:00 PM - 4:00 PM", "Orë përnjëmjetu - energji maksimale");
        }
    }
}