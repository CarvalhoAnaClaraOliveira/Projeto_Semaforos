package org.example.semaforos;

import classes.*;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.tools.FlowGridPane;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardMetrics extends Application {

    private Cidade cidade;
    private GerenciadorDeTrafego gerenciador;
    private Thread threadGerenciador;
    private Thread threadGerador;

    // Componentes de UI
    private Map<Integer, Label> labelsCruzamentos = new HashMap<>();
    private Map<String, Label> labelsRua = new HashMap<>();
    private Map<String, Label> labelsSemaforoStatus = new HashMap<>();
    private Map<String, Circle> semaforoCircles = new HashMap<>();
    private Map<String, ProgressBar> progressBarsRua = new HashMap<>();
    private Map<String, Tile> tilesMetricas = new HashMap<>();
    private VBox colunaStarvation;
    private VBox colunaFluxoNormal;
    private VBox colunaAlertas;
    private Label labelResumoRelatorio;
    private Label labelTotalVeiculos;
    private Label labelCruzamentosAtivos;
    private Label labelSemaforosStatus;
    private BarChart<String, Number> chartVeiculos;

    private static final long LIMITE_STARVATION_SEGUNDOS = 8;

    // Animação
    private AnimationTimer timer;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Use shared instances from AppState
        cidade = AppState.get().getCidade();
        gerenciador = AppState.get().getGer();

        if (cidade == null) {
            cidade = new Cidade();
            TrafficSimulationBootstrap.povoarCidadeInicialmente(cidade);
            AppState.get().setCidade(cidade);
        }

        if (gerenciador == null) {
            gerenciador = new GerenciadorDeTrafego(cidade);
            AppState.get().setGer(gerenciador);
            threadGerenciador = new Thread(gerenciador);
            threadGerenciador.setDaemon(true);
            threadGerenciador.start();
            threadGerador = TrafficSimulationBootstrap.iniciarGeradorDeTrafego(cidade);
        }

        // Cria a interface
        BorderPane root = criarInterface();

        Scene scene = new Scene(root, 1400, 900);
        primaryStage.setTitle("Sistema de Gerenciamento de Semáforos - Métricas em Tempo Real");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> pararSistema());
        primaryStage.show();

        // Inicia atualização de métricas em tempo real
        iniciarAtualizacaoMetricas();
    }

    private BorderPane criarInterface() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f0f0f0;");

        // Cabeçalho
        root.setTop(criarCabecalho());

        // Conteúdo principal (painel central com abas)
        TabPane tabPane = criarAbas();
        root.setCenter(tabPane);

        return root;
    }

    private VBox criarCabecalho() {
        VBox cabecalho = new VBox();
        cabecalho.setStyle("-fx-background-color: #2c3e50; -fx-padding: 15px;");
        cabecalho.setSpacing(10);

        Label titulo = new Label("🚦 SISTEMA INTELIGENTE DE GERENCIAMENTO DE SEMÁFOROS");
        titulo.setStyle("-fx-font-size: 24px; -fx-text-fill: white; -fx-font-weight: bold;");

        HBox metricsBar = new HBox(20);
        metricsBar.setPadding(new Insets(10));
        metricsBar.setStyle("-fx-background-color: #34495e;");
        metricsBar.setAlignment(Pos.CENTER_LEFT);

        labelTotalVeiculos = criarMetricaLabel("📊 Total de Veículos: 0", Color.web("#3498db"));
        labelCruzamentosAtivos = criarMetricaLabel("🔄 Cruzamentos: " + cidade.getCruzamentos().size(), Color.web("#2ecc71"));
        labelSemaforosStatus = criarMetricaLabel("🟢 Abertos: 0 | 🔴 Fechados: 0", Color.web("#2ecc71"));

        metricsBar.getChildren().addAll(
                labelTotalVeiculos,
                new Separator(javafx.geometry.Orientation.VERTICAL),
                labelCruzamentosAtivos,
                new Separator(javafx.geometry.Orientation.VERTICAL),
                labelSemaforosStatus
        );

        cabecalho.getChildren().addAll(titulo, metricsBar);
        return cabecalho;
    }

    private Label criarMetricaLabel(String texto, Color cor) {
        Label label = new Label(texto);
        label.setStyle("-fx-font-size: 14px; -fx-text-fill: white; -fx-font-weight: bold;");
        label.setTextFill(Color.WHITE);
        return label;
    }

    private TabPane criarAbas() {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setPrefHeight(780);

        // Aba 1: Dashboard Principal
        Tab abaDashboard = new Tab("Dashboard", criarDashboardPrincipal());
        abaDashboard.setStyle("-fx-font-size: 12px;");

        // Aba 2: Cruzamentos
        Tab abaCruzamentos = new Tab("Cruzamentos", criarPainelCruzamentos());
        abaCruzamentos.setStyle("-fx-font-size: 12px;");

        // Aba 3: Ruas
        Tab abaRuas = new Tab("Ruas", criarPainelRuas());
        abaRuas.setStyle("-fx-font-size: 12px;");

        Tab abaInfoSistema = new Tab("Informações", criarPainelInfoGeral());
        abaInfoSistema.setStyle("-fx-font-size: 12px;");

        tabPane.getTabs().addAll(abaDashboard, abaInfoSistema, abaCruzamentos, abaRuas);

        return tabPane;
    }

    private VBox criarDashboardPrincipal() {
        VBox dashboard = new VBox(15);
        dashboard.setPadding(new Insets(12));
        dashboard.setStyle("-fx-background-color: #ecf0f1;");

        // Painel de tiles (métricas visuais)
        FlowGridPane gridPane = new FlowGridPane(4, 3, // colunas, linhas
                new Tile[]{
                        criarTileMetrica("TOTAL DE VEÍCULOS", "0", "#3498db"),
                        criarTileMetrica("SEMÁFOROS ABERTOS", "0", "#2ecc71"),
                        criarTileMetrica("SEMÁFOROS FECHADOS", "0", "#e74c3c"),
                        criarTileMetrica("VEÍCULOS EM TRÂNSITO", "0", "#f39c12"),
                        criarTileMetrica("TEMPO MÉDIO ESPERA", "0s", "#9b59b6"),
                        criarTileMetrica("EFICIÊNCIA SISTEMA", "0%", "#1abc9c"),
                });

        tilesMetricas.put("total", (Tile) gridPane.getChildren().get(0));
        tilesMetricas.put("abertos", (Tile) gridPane.getChildren().get(1));
        tilesMetricas.put("fechados", (Tile) gridPane.getChildren().get(2));
        tilesMetricas.put("transito", (Tile) gridPane.getChildren().get(3));
        tilesMetricas.put("espera", (Tile) gridPane.getChildren().get(4));
        tilesMetricas.put("eficiencia", (Tile) gridPane.getChildren().get(5));

        gridPane.setPrefHeight(250);
        gridPane.setStyle("-fx-hgap: 15; -fx-vgap: 15;");

        ScrollPane scrollPane = new ScrollPane(gridPane);
        scrollPane.setFitToWidth(true);

        dashboard.getChildren().addAll(criarRelatorioOperacional());

        return dashboard;
    }

    private Tile criarTileMetrica(String titulo, String valor, String cor) {
        return TileBuilder.create()
                .prefSize(200, 150)
                .title(titulo)
                .text(valor)
                .backgroundColor(Color.web(cor))
                .textColor(Color.WHITE)
                .borderColor(Color.web(cor).brighter())
                .borderWidth(2)
                .build();
    }

    private VBox criarRelatorioOperacional() {
        VBox painel = new VBox(12);
        painel.setPadding(new Insets(15));
        painel.setPrefHeight(690);
        painel.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-border-width: 1;");

        Label titulo = new Label("Relatorio do Trafego em Tempo Real");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        labelResumoRelatorio = new Label("Aguardando leitura da simulacao...");
        labelResumoRelatorio.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #34495e;");
        labelResumoRelatorio.setWrapText(true);

        HBox colunas = new HBox(15);
        colunas.setAlignment(Pos.TOP_LEFT);
        colunas.setPrefHeight(590);
        colunas.setMaxHeight(590);

        colunaStarvation = criarColunaRelatorio("Veiculos e pedestres em starvation");
        colunaFluxoNormal = criarColunaRelatorio("Veiculos e pedestres em fluxo normal");
        colunaAlertas = criarColunaRelatorio("Possiveis problemas");

        colunas.getChildren().addAll(
                criarScrollColuna(colunaStarvation),
                criarScrollColuna(colunaFluxoNormal),
                criarScrollColuna(colunaAlertas)
        );
        painel.getChildren().addAll(titulo, labelResumoRelatorio, colunas);

        return painel;
    }

    private ScrollPane criarScrollColuna(VBox coluna) {
        ScrollPane scroll = new ScrollPane(coluna);
        scroll.setFitToWidth(true);
        scroll.setPrefViewportWidth(430);
        scroll.setPrefViewportHeight(570);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        return scroll;
    }

    private VBox criarColunaRelatorio(String titulo) {
        VBox coluna = new VBox(8);
        coluna.setPrefWidth(430);
        coluna.setMaxWidth(430);
        coluna.setPrefHeight(570);
        coluna.setMaxHeight(570);
        coluna.setMinHeight(260);
        coluna.setPadding(new Insets(12));
        coluna.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dfe6e9; -fx-border-width: 1;");

        Label labelTitulo = new Label(titulo);
        labelTitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        coluna.getChildren().add(labelTitulo);

        return coluna;
    }

    private VBox criarPainelInfoGeral() {
        VBox painel = new VBox(10);
        painel.setPadding(new Insets(15));
        painel.setStyle("-fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-border-radius: 5; -fx-background-color: white;");

        Label titulo = new Label("📋 Informações do Sistema");
        titulo.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        int linha = 0;
        for (Rua rua : cidade.getRuas()) {
            Label nomeRua = new Label("Rua " + rua.getNome() + ":");
            nomeRua.setStyle("-fx-font-weight: bold;");

            Label infoRua = new Label("Veículos: 0 | Sentido: " + rua.getSentido());

            grid.add(nomeRua, 0, linha);
            grid.add(infoRua, 1, linha);

            labelsRua.put(rua.getNome(), infoRua);

            linha++;
        }

        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(250);

        painel.getChildren().addAll(titulo, scrollPane);

        return painel;
    }

    private VBox criarPainelCruzamentos() {
        VBox painel = new VBox(10);
        painel.setPadding(new Insets(20));
        painel.setStyle("-fx-background-color: #ecf0f1;");

        Label titulo = new Label("🔀 Status dos Cruzamentos");
        titulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        GridPane gridCruzamentos = new GridPane();
        gridCruzamentos.setHgap(15);
        gridCruzamentos.setVgap(15);
        gridCruzamentos.setPadding(new Insets(15));

        int coluna = 0;
        int linha = 0;

        for (Cruzamento c : cidade.getCruzamentos()) {
            VBox cruzBox = criarCardCruzamento(c);
            gridCruzamentos.add(cruzBox, coluna, linha);

            labelsCruzamentos.put(c.getNumero(), new Label());

            coluna++;
            if (coluna >= 4) {
                coluna = 0;
                linha++;
            }
        }

        ScrollPane scrollPane = new ScrollPane(gridCruzamentos);
        scrollPane.setFitToWidth(true);

        painel.getChildren().addAll(titulo, scrollPane);

        return painel;
    }

    private VBox criarCardCruzamento(Cruzamento c) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(12));
        card.setStyle("-fx-border-color: #bdc3c7; -fx-border-width: 2; -fx-border-radius: 5; -fx-background-color: white;");
        card.setPrefWidth(200);

        Label numCruzamento = new Label("CRUZAMENTO #" + c.getNumero());
        numCruzamento.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label ruaH = new Label("Horizontal: " + c.getRuaHorizontal().getNome());
        ruaH.setStyle("-fx-font-size: 11px;");

        Label ruaV = new Label("Vertical: " + c.getRuaVertical().getNome());
        ruaV.setStyle("-fx-font-size: 11px;");

        HBox semaforosBox = criarSemaforosBox(c);

        card.getChildren().addAll(numCruzamento, ruaH, ruaV, new Separator(), semaforosBox);

        return card;
    }

    private HBox criarSemaforosBox(Cruzamento c) {
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER);

        // Semáforo Horizontal
        VBox semH = new VBox(5);
        semH.setAlignment(Pos.CENTER);
        semH.setStyle("-fx-border-color: #ecf0f1; -fx-padding: 8; -fx-border-radius: 3;");

        Label labelSemH = new Label("HORIZONTAL");
        labelSemH.setStyle("-fx-font-size: 10px; -fx-font-weight: bold;");

        Circle circuloH = new Circle(15);
        circuloH.setFill(Color.RED);

        labelsSemaforoStatus.put("semH_" + c.getNumero(), labelSemH);
        semaforoCircles.put("semH_" + c.getNumero(), circuloH);
        semH.getChildren().addAll(labelSemH, circuloH);

        // Semáforo Vertical
        VBox semV = new VBox(5);
        semV.setAlignment(Pos.CENTER);
        semV.setStyle("-fx-border-color: #ecf0f1; -fx-padding: 8; -fx-border-radius: 3;");

        Label labelSemV = new Label("VERTICAL");
        labelSemV.setStyle("-fx-font-size: 10px; -fx-font-weight: bold;");

        Circle circuloV = new Circle(15);
        circuloV.setFill(Color.RED);

        labelsSemaforoStatus.put("semV_" + c.getNumero(), labelSemV);
        semaforoCircles.put("semV_" + c.getNumero(), circuloV);
        semV.getChildren().addAll(labelSemV, circuloV);

        box.getChildren().addAll(semH, semV);

        return box;
    }

    private VBox criarPainelRuas() {
        VBox painel = new VBox(10);
        painel.setPadding(new Insets(20));
        painel.setStyle("-fx-background-color: #ecf0f1;");

        Label titulo = new Label("🛣️ Monitoramento de Ruas");
        titulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        GridPane gridRuas = new GridPane();
        gridRuas.setHgap(20);
        gridRuas.setVgap(15);
        gridRuas.setPadding(new Insets(15));

        int coluna = 0;
        for (Rua rua : cidade.getRuas()) {
            VBox cardRua = criarCardRua(rua);
            gridRuas.add(cardRua, coluna % 2, coluna / 2);
            coluna++;
        }

        ScrollPane scrollPane = new ScrollPane(gridRuas);
        scrollPane.setFitToWidth(true);

        painel.getChildren().addAll(titulo, scrollPane);

        return painel;
    }

    private VBox criarCardRua(Rua rua) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-border-color: #3498db; -fx-border-width: 2; -fx-border-radius: 5; -fx-background-color: white;");
        card.setPrefWidth(300);

        Label nome = new Label("RUA: " + rua.getNome());
        nome.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label orientacao = new Label("Orientação: " + rua.getOrientacao());
        orientacao.setStyle("-fx-font-size: 11px;");

        Label sentido = new Label("Sentido: " + rua.getSentido());
        sentido.setStyle("-fx-font-size: 11px;");

        Label veiculos = new Label("Veículos: 0");
        veiculos.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #e74c3c;");

        ProgressBar progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(280);

        labelsRua.put("COUNT_" + rua.getNome(), veiculos);
        progressBarsRua.put(rua.getNome(), progressBar);

        card.getChildren().addAll(nome, orientacao, sentido, new Separator(), veiculos, progressBar);

        return card;
    }

    private VBox criarPainelGraficos() {
        VBox painel = new VBox(15);
        painel.setPadding(new Insets(20));
        painel.setStyle("-fx-background-color: #ecf0f1;");

        Label titulo = new Label("📈 Gráficos de Desempenho");
        titulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        chartVeiculos = criarGraficoVeiculos();

        painel.getChildren().addAll(titulo, chartVeiculos);

        return painel;
    }

    private BarChart<String, Number> criarGraficoVeiculos() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Ruas");
        yAxis.setLabel("Quantidade de Veículos");

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Distribuição de Veículos por Rua");
        chart.setPrefHeight(400);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Veículos");

        for (Rua rua : cidade.getRuas()) {
            series.getData().add(new XYChart.Data<>(rua.getNome(), 0));
        }

        chart.getData().add(series);

        return chart;
    }

    private void iniciarAtualizacaoMetricas() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                atualizarMetricas();
            }
        };
        timer.start();
    }

    private void atualizarMetricas() {
        // Conta total de veículos
        int totalVeiculos = 0;
        for (Rua rua : cidade.getRuas()) {
            totalVeiculos += rua.getQuantidadeVeiculos();
        }

        labelTotalVeiculos.setText("📊 Total de Veículos: " + totalVeiculos);
        atualizarRelatorioOperacional(totalVeiculos);

        // Atualiza informações de ruas
        for (Rua rua : cidade.getRuas()) {
            String key = "COUNT_" + rua.getNome();
            if (labelsRua.containsKey(key)) {
                labelsRua.get(key).setText("Veículos: " + rua.getQuantidadeVeiculos());
            }
        }

        // Atualiza gráfico
        atualizarTile("total", String.valueOf(totalVeiculos));
        atualizarTile("transito", String.valueOf(totalVeiculos));

        for (Rua rua : cidade.getRuas()) {
            int quantidade = rua.getQuantidadeVeiculos();
            Label infoRua = labelsRua.get(rua.getNome());
            if (infoRua != null) {
                infoRua.setText("Veiculos: " + quantidade + " | Sentido: " + rua.getSentido());
            }

            ProgressBar progressBar = progressBarsRua.get(rua.getNome());
            if (progressBar != null) {
                progressBar.setProgress(Math.min(quantidade / 20.0, 1.0));
            }
        }

        if (chartVeiculos != null && !chartVeiculos.getData().isEmpty()) {
            XYChart.Series<String, Number> series = (XYChart.Series<String, Number>) chartVeiculos.getData().get(0);
            for (Rua rua : cidade.getRuas()) {
                for (XYChart.Data<String, Number> data : series.getData()) {
                    if (data.getXValue().equals(rua.getNome())) {
                        data.setYValue(rua.getQuantidadeVeiculos());
                    }
                }
            }
        }

        // Atualiza status dos semáforos
        int abertos = 0, fechados = 0;
        for (Cruzamento c : cidade.getCruzamentos()) {
            if (c.getSemaforoHorizontal().getStatus() == 1) abertos++;
            else fechados++;
            if (c.getSemaforoVertical().getStatus() == 1) abertos++;
            else fechados++;

            atualizarSemaforoVisual("semH_" + c.getNumero(), c.getSemaforoHorizontal().getStatus());
            atualizarSemaforoVisual("semV_" + c.getNumero(), c.getSemaforoVertical().getStatus());
            atualizarTile("abertos", String.valueOf(abertos));
            atualizarTile("fechados", String.valueOf(fechados));
            atualizarTile("espera", calcularTempoMedioEspera() + "s");
            atualizarTile("eficiencia", calcularEficiencia(abertos, fechados) + "%");
        }

        labelCruzamentosAtivos.setText("🔄 Cruzamentos: " + cidade.getCruzamentos().size());
        labelSemaforosStatus.setText("🟢 Abertos: " + abertos + " | 🔴 Fechados: " + fechados);
    }

    private void atualizarRelatorioOperacional(int totalVeiculos) {
        if (colunaStarvation == null || colunaFluxoNormal == null || colunaAlertas == null) {
            return;
        }

        limparColunaRelatorio(colunaStarvation);
        limparColunaRelatorio(colunaFluxoNormal);
        limparColunaRelatorio(colunaAlertas);

        List<Label> itensStarvation = new ArrayList<>();
        List<Label> itensNormais = new ArrayList<>();
        List<Label> itensAlertas = new ArrayList<>();
        int totalPedestres = 0;
        int totalEmergencias = 0;

        for (Rua rua : cidade.getRuas()) {
            long espera = rua.getSensor().getTempoSinalFechadoSegundos();
            int quantidadeRua = rua.getQuantidadeVeiculos();

            if (quantidadeRua >= 12) {
                itensAlertas.add(criarItemRelatorio(
                        "Congestionamento provavel | Rua " + rua.getNome()
                                + " com " + quantidadeRua + " veiculos",
                        false,
                        true
                ));
            }

            if (espera >= 15) {
                itensAlertas.add(criarItemRelatorio(
                        "Espera prolongada | Rua " + rua.getNome()
                                + " com sinal fechado ha " + espera + "s",
                        false,
                        true
                ));
            }

            synchronized (rua.getVeiculos()) {
                for (Veiculo veiculo : rua.getVeiculos()) {
                    boolean emergencia = veiculo.getPrioridade() == Prioridade.EMERGENCIA;
                    boolean parado = veiculo.getVelocidadeA() == 0;
                    boolean starvation = parado && espera >= LIMITE_STARVATION_SEGUNDOS;
                    if (emergencia) {
                        totalEmergencias++;
                    }

                    if (emergencia && parado) {
                        itensAlertas.add(criarItemRelatorio(
                                "Emergencia bloqueada | " + veiculo.getIdUnico()
                                        + " parado na Rua " + rua.getNome(),
                                true,
                                true
                        ));
                    }

                    if (veiculo.getPercurso() == null) {
                        itensAlertas.add(criarItemRelatorio(
                                "Veiculo sem percurso | " + veiculo.getIdUnico()
                                        + " na Rua " + rua.getNome(),
                                emergencia,
                                true
                        ));
                    }

                    String estado = parado ? "aguardando sinal" : "trafegando";
                    if (starvation) {
                        estado = "STARVATION ha " + espera + "s";
                    }

                    Label item = criarItemRelatorio(
                            veiculo.getIdUnico() + " | " + veiculo.getClass().getSimpleName()
                                    + " | Rua " + rua.getNome()
                                    + " | " + estado
                                    + " | " + veiculo.getVelocidadeA() + " km/h",
                            emergencia,
                            starvation
                    );

                    if (starvation) {
                        itensStarvation.add(item);
                    } else {
                        itensNormais.add(item);
                    }
                }
            }

            for (Pedestre pedestre : rua.getPedestres()) {
                totalPedestres++;
                boolean parado = pedestre.getVelocidade() == 0;
                boolean starvation = parado && espera >= LIMITE_STARVATION_SEGUNDOS;
                String estado = parado ? "aguardando travessia" : "passando";
                if (starvation) {
                    estado = "STARVATION ha " + espera + "s";
                }

                Label item = criarItemRelatorio(
                        "PE" + pedestre.getId()
                                + " | Pedestre | Rua " + rua.getNome()
                                + " | " + estado
                                + " | " + pedestre.getVelocidade() + " km/h",
                        false,
                        starvation
                );

                if (starvation) {
                    itensStarvation.add(item);
                } else {
                    itensNormais.add(item);
                }
            }
        }

        if (itensStarvation.isEmpty()) {
            itensStarvation.add(criarItemRelatorio("Nenhum item em starvation no momento.", false, false));
        }
        if (itensNormais.isEmpty()) {
            itensNormais.add(criarItemRelatorio("Nenhum veiculo ou pedestre em fluxo normal.", false, false));
        }
        adicionarAlertasSemaforos(itensAlertas);
        if (itensAlertas.isEmpty()) {
            itensAlertas.add(criarItemRelatorio("Nenhum problema provavel detectado no momento.", false, false));
        }

        colunaStarvation.getChildren().addAll(itensStarvation);
        colunaFluxoNormal.getChildren().addAll(itensNormais);
        colunaAlertas.getChildren().addAll(itensAlertas);

        labelResumoRelatorio.setText(
                "Veiculos: " + totalVeiculos
                        + " | Pedestres: " + totalPedestres
                        + " | Emergencias: " + totalEmergencias
                        + " | Starvation: " + (itensStarvation.size() == 1 && itensStarvation.get(0).getText().startsWith("Nenhum") ? 0 : itensStarvation.size())
                        + " | Limite: " + LIMITE_STARVATION_SEGUNDOS + "s parado com sinal fechado"
        );
    }

    private void limparColunaRelatorio(VBox coluna) {
        if (coluna.getChildren().size() > 1) {
            coluna.getChildren().remove(1, coluna.getChildren().size());
        }
    }

    private void adicionarAlertasSemaforos(List<Label> itensAlertas) {
        for (Cruzamento cruzamento : cidade.getCruzamentos()) {
            int statusH = cruzamento.getSemaforoHorizontal().getStatus();
            int statusV = cruzamento.getSemaforoVertical().getStatus();

            if (statusH == 1 && statusV == 1) {
                itensAlertas.add(criarItemRelatorio(
                        "Risco de conflito | Cruzamento #" + cruzamento.getNumero()
                                + " com horizontal e vertical abertos",
                        false,
                        true
                ));
            }

            if (statusH == 0 && statusV == 0) {
                itensAlertas.add(criarItemRelatorio(
                        "Bloqueio total | Cruzamento #" + cruzamento.getNumero()
                                + " com os dois sinais fechados",
                        false,
                        true
                ));
            }

            if ((statusH != 0 && statusH != 1) || (statusV != 0 && statusV != 1)) {
                itensAlertas.add(criarItemRelatorio(
                        "Estado invalido de semaforo | Cruzamento #" + cruzamento.getNumero(),
                        false,
                        true
                ));
            }
        }
    }

    private Label criarItemRelatorio(String texto, boolean emergencia, boolean starvation) {
        Label label = new Label(texto);
        label.setWrapText(true);
        label.setMaxWidth(350);
        label.setPrefWidth(350);
        label.setStyle("-fx-font-size: 12px; -fx-padding: 6; -fx-background-color: white; -fx-border-color: #ecf0f1;");

        if (emergencia) {
            label.setTextFill(Color.web("#c0392b"));
            label.setStyle(label.getStyle() + " -fx-font-weight: bold;");
        } else if (starvation) {
            label.setTextFill(Color.web("#8e44ad"));
            label.setStyle(label.getStyle() + " -fx-font-weight: bold;");
        } else {
            label.setTextFill(Color.web("#2c3e50"));
        }

        return label;
    }

    private void atualizarTile(String key, String value) {
        Tile tile = tilesMetricas.get(key);
        if (tile != null) {
            tile.setText(value);
        }
    }

    private void atualizarSemaforoVisual(String key, int status) {
        boolean aberto = status == 1;

        Label label = labelsSemaforoStatus.get(key);
        if (label != null) {
            label.setText(aberto ? "ABERTO" : "FECHADO");
            label.setTextFill(aberto ? Color.web("#27ae60") : Color.web("#c0392b"));
        }

        Circle circle = semaforoCircles.get(key);
        if (circle != null) {
            circle.setFill(aberto ? Color.web("#2ecc71") : Color.web("#e74c3c"));
        }
    }

    private long calcularTempoMedioEspera() {
        long total = 0;
        int sensores = 0;
        for (Rua rua : cidade.getRuas()) {
            total += rua.getSensor().getTempoSinalFechadoSegundos();
            sensores++;
        }
        return sensores == 0 ? 0 : total / sensores;
    }

    private int calcularEficiencia(int abertos, int fechados) {
        int totalSemaforos = abertos + fechados;
        if (totalSemaforos == 0) {
            return 0;
        }
        return (int) Math.round((abertos * 100.0) / totalSemaforos);
    }

    private void pararSistema() {
        gerenciador.parar();
        if (timer != null) {
            timer.stop();
        }
        if (threadGerador != null) {
            threadGerador.interrupt();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
