package org.example.semaforos;

import classes.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class DashboardMetricsController extends Application {

    private Cidade cidade;
    private GerenciadorDeTrafego gerenciador;
    private Thread threadGerenciador;

    @FXML
    private Label labelTotalVeiculos;
    @FXML
    private Label labelCruzamentosAtivos;
    @FXML
    private Label labelSemaforosStatus;
    private Label labelCruzamentosInfo = new Label();
    private Label labelSemaforosAbertos = new Label();
    private Label labelSemaforsFechados = new Label();

    private TabPane tabPane;
    private Map<String, Label> mapsRuas = new HashMap<>();
    private AnimationTimer timer;

    @Override
    public void start(Stage primaryStage) throws Exception {
        cidade = new Cidade();
        gerenciador = new GerenciadorDeTrafego(cidade);

        threadGerenciador = new Thread(gerenciador);
        threadGerenciador.setDaemon(true);
        threadGerenciador.start();

        VBox root = criarInterfacePrincipal();
        Scene scene = new Scene(root, 1200, 800);

        primaryStage.setTitle("🚦 Sistema Inteligente de Gerenciamento de Semáforos");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> pararSistema());
        primaryStage.show();

        iniciarAtualizacao();
    }

    private VBox criarInterfacePrincipal() {
        VBox root = new VBox();
        root.setStyle("-fx-background-color: #f5f5f5;");

        root.getChildren().addAll(
                criarCabecalho(),
                criarAbas()
        );

        return root;
    }

    private VBox criarCabecalho() {
        VBox cabecalho = new VBox(10);
        cabecalho.setStyle("-fx-background-color: #2c3e50;");
        cabecalho.setPadding(new Insets(15));

        Label titulo = new Label("🚦 SISTEMA INTELIGENTE DE GERENCIAMENTO DE SEMÁFOROS");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        titulo.setTextFill(Color.WHITE);

        HBox metricas = new HBox(20);
        metricas.setPadding(new Insets(10));
        metricas.setStyle("-fx-background-color: #34495e;");
        metricas.setAlignment(Pos.CENTER_LEFT);

        labelTotalVeiculos = novaMetricaLabel("📊 Veículos: 0");
        labelSemaforosAbertos = novaMetricaLabel("🟢 Abertos: 0");
        labelSemaforsFechados = novaMetricaLabel("🔴 Fechados: 0");
        labelCruzamentosInfo = novaMetricaLabel("🔀 Cruzamentos: " + cidade.getCruzamentos().size());

        metricas.getChildren().addAll(
                labelTotalVeiculos,
                new Separator(javafx.geometry.Orientation.VERTICAL),
                labelSemaforosAbertos,
                new Separator(javafx.geometry.Orientation.VERTICAL),
                labelSemaforsFechados,
                new Separator(javafx.geometry.Orientation.VERTICAL),
                labelCruzamentosInfo
        );

        cabecalho.getChildren().addAll(titulo, metricas);
        return cabecalho;
    }

    private Label novaMetricaLabel(String texto) {
        Label label = new Label(texto);
        label.setFont(Font.font("Arial", 13));
        label.setTextFill(Color.WHITE);
        return label;
    }

    private TabPane criarAbas() {
        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        tabPane.getTabs().addAll(
                criarAbaGeral(),
                criarAbaCruzamentos(),
                criarAbaRuas(),
                criarAbaGraficos()
        );

        return tabPane;
    }

    private Tab criarAbaGeral() {
        Tab aba = new Tab("📊 Dashboard");
        aba.setClosable(false);

        VBox conteudo = new VBox(15);
        conteudo.setPadding(new Insets(20));
        conteudo.setStyle("-fx-background-color: #ecf0f1;");

        Label titulo = new Label("Informações do Sistema");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));
        grid.setStyle("-fx-border-color: #bdc3c7; -fx-padding: 15; -fx-background-color: white;");

        int linha = 0;
        for (Rua rua : cidade.getRuas()) {
            Label nomeRua = new Label("Rua " + rua.getNome());
            nomeRua.setStyle("-fx-font-weight: bold;");

            Label infoRua = new Label("Veículos: 0 | Orientação: " + rua.getOrientacao());

            grid.add(nomeRua, 0, linha);
            grid.add(infoRua, 1, linha);

            mapsRuas.put(rua.getNome(), infoRua);

            linha++;
        }

        ScrollPane scroll = new ScrollPane(grid);
        scroll.setFitToWidth(true);

        conteudo.getChildren().addAll(titulo, scroll);

        aba.setContent(conteudo);
        return aba;
    }

    private Tab criarAbaCruzamentos() {
        Tab aba = new Tab("🔀 Cruzamentos");
        aba.setClosable(false);

        VBox conteudo = new VBox(15);
        conteudo.setPadding(new Insets(20));
        conteudo.setStyle("-fx-background-color: #ecf0f1;");

        Label titulo = new Label("Status dos Cruzamentos");
        titulo.setFont(Font.font("Arial", 16));
        titulo.setStyle("-fx-font-weight: bold;");

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);

        int coluna = 0, linha = 0;
        for (Cruzamento c : cidade.getCruzamentos()) {
            VBox card = criarCardCruzamento(c);
            grid.add(card, coluna, linha);

            coluna++;
            if (coluna >= 4) {
                coluna = 0;
                linha++;
            }
        }

        ScrollPane scroll = new ScrollPane(grid);
        scroll.setFitToWidth(true);

        conteudo.getChildren().addAll(titulo, scroll);

        aba.setContent(conteudo);
        return aba;
    }

    private VBox criarCardCruzamento(Cruzamento c) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(12));
        card.setStyle("-fx-border-color: #3498db; -fx-border-width: 2; -fx-background-color: white; -fx-border-radius: 5;");
        card.setPrefWidth(200);

        Label numero = new Label("CRUZAMENTO #" + c.getNumero());
        numero.setStyle("-fx-font-weight: bold;");
        numero.setTextFill(Color.web("#2c3e50"));

        Label ruaH = new Label("H: " + c.getRuaHorizontal().getNome());
        Label ruaV = new Label("V: " + c.getRuaVertical().getNome());

        HBox semaforosBox = new HBox(10);
        semaforosBox.setAlignment(Pos.CENTER);

        // Semáforo Horizontal
        VBox semH = new VBox(5);
        semH.setAlignment(Pos.CENTER);
        Label textoH = new Label("HORIZONTAL");
        textoH.setFont(Font.font(10));
        Circle circuloH = new Circle(12);
        circuloH.setFill(Color.RED);
        semH.getChildren().addAll(textoH, circuloH);

        // Semáforo Vertical
        VBox semV = new VBox(5);
        semV.setAlignment(Pos.CENTER);
        Label textoV = new Label("VERTICAL");
        textoV.setFont(Font.font(10));
        Circle circuloV = new Circle(12);
        circuloV.setFill(Color.RED);
        semV.getChildren().addAll(textoV, circuloV);

        semaforosBox.getChildren().addAll(semH, semV);

        card.getChildren().addAll(numero, ruaH, ruaV, new Separator(), semaforosBox);

        return card;
    }

    private Tab criarAbaRuas() {
        Tab aba = new Tab("🛣️ Ruas");
        aba.setClosable(false);

        VBox conteudo = new VBox(15);
        conteudo.setPadding(new Insets(20));
        conteudo.setStyle("-fx-background-color: #ecf0f1;");

        Label titulo = new Label("Monitoramento de Ruas");
        titulo.setFont(Font.font("Arial", 16));
        titulo.setStyle("-fx-font-weight: bold;");

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(15);

        int coluna = 0;
        for (Rua rua : cidade.getRuas()) {
            VBox cardRua = criarCardRua(rua);
            grid.add(cardRua, coluna % 2, coluna / 2);
            coluna++;
        }

        conteudo.getChildren().addAll(titulo, grid);

        aba.setContent(conteudo);
        return aba;
    }

    private VBox criarCardRua(Rua rua) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-border-color: #2ecc71; -fx-border-width: 2; -fx-background-color: white; -fx-border-radius: 5;");

        Label nome = new Label("RUA: " + rua.getNome());
        nome.setStyle("-fx-font-weight: bold;");

        Label orientacao = new Label("Orientação: " + rua.getOrientacao());
        Label sentido = new Label("Sentido: " + rua.getSentido());

        Label veiculos = new Label("Veículos: 0");
        veiculos.setStyle("-fx-font-weight: bold; -fx-text-fill: #e74c3c;");

        ProgressBar progress = new ProgressBar(0);
        progress.setPrefWidth(280);

        mapsRuas.put("COUNT_" + rua.getNome(), veiculos);

        card.getChildren().addAll(nome, orientacao, sentido, new Separator(), veiculos, progress);

        return card;
    }

    private Tab criarAbaGraficos() {
        Tab aba = new Tab("📈 Gráficos");
        aba.setClosable(false);

        VBox conteudo = new VBox(15);
        conteudo.setPadding(new Insets(20));
        conteudo.setStyle("-fx-background-color: #ecf0f1;");

        Label titulo = new Label("Estatísticas do Sistema");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setStyle("-fx-font-family: monospace;");
        textArea.setPrefHeight(350);

        conteudo.getChildren().addAll(titulo, textArea);

        aba.setContent(conteudo);
        return aba;
    }

    private void iniciarAtualizacao() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                atualizarMetricas();
            }
        };
        timer.start();
    }

    private void atualizarMetricas() {
        int total = 0;
        for (Rua rua : cidade.getRuas()) {
            total += rua.getQuantidadeVeiculos();

            if (mapsRuas.containsKey("COUNT_" + rua.getNome())) {
                Label label = mapsRuas.get("COUNT_" + rua.getNome());
                label.setText("Veículos: " + rua.getQuantidadeVeiculos());
            }
        }

        labelTotalVeiculos.setText("📊 Veículos: " + total);

        int abertos = 0, fechados = 0;
        for (Cruzamento c : cidade.getCruzamentos()) {
            if (c.getSemaforoHorizontal().getStatus() == 1) abertos++;
            else fechados++;
            if (c.getSemaforoVertical().getStatus() == 1) abertos++;
            else fechados++;
        }

        labelSemaforosAbertos.setText("🟢 Abertos: " + abertos);
        labelSemaforsFechados.setText("🔴 Fechados: " + fechados);
        atualizarCabecalhoFXML(abertos, fechados);
    }

    private void atualizarCabecalhoFXML(int abertos, int fechados) {
        if (labelCruzamentosAtivos != null) {
            labelCruzamentosAtivos.setText("Cruzamentos: " + cidade.getCruzamentos().size());
        }
        if (labelSemaforosStatus != null) {
            labelSemaforosStatus.setText("Abertos: " + abertos + " | Fechados: " + fechados);
        }
    }

    private void pararSistema() {
        gerenciador.parar();
        if (timer != null) {
            timer.stop();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
