package ru.jeki.schedulenow;

import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Workbook;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ru.jeki.schedulenow.controller.GroupChooseController;
import ru.jeki.schedulenow.controller.ScheduleController;
import ru.jeki.schedulenow.model.ScheduleModel;
import ru.jeki.schedulenow.parser.ReplacementsParser;
import ru.jeki.schedulenow.parser.ScheduleSource;
import ru.jeki.schedulenow.parser.spreadsheet.SpreadsheetScheduleParser;
import ru.jeki.schedulenow.schedule.CollapsedSchedule;
import ru.jeki.schedulenow.services.ApplicationPropertyCache;
import ru.jeki.schedulenow.services.SceneNavigationService;
import ru.jeki.schedulenow.services.Services;
import ru.jeki.schedulenow.spreadsheet.SpreadsheetScheduleSource;
import ru.jeki.schedulenow.spreadsheet.providers.SiteSpreadsheetWorkbookProvider;
import ru.jeki.schedulenow.spreadsheet.providers.SpreadsheetWorkbookProvider;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class ScheduleNow extends Application {

    private Properties configuration = new Properties();
    private ScheduleSource replacementSchedule, spreadsheetSchedule, collapsedSchedule;
    private ApplicationPropertyCache appProperties = new ApplicationPropertyCache();
    private ScheduleModel scheduleModel = new ScheduleModel(appProperties);
    private SpreadsheetWorkbookProvider spreadsheetWorkbookProvider = new SiteSpreadsheetWorkbookProvider(configuration);

    @Override
    public void init() throws Exception {
        loadConfiguration();
        loadSchedule();
        loadScheduleModel();
    }

    private void loadSchedule() {
        loadReplacementSchedule();
        loadSpreadsheetSchedule();
        loadCollapsedSchedule();
    }

    private void loadCollapsedSchedule() {
        CollapsedSchedule collapsedSchedule = new CollapsedSchedule();

        collapsedSchedule.add(spreadsheetSchedule);
        collapsedSchedule.add(replacementSchedule);

        this.collapsedSchedule = collapsedSchedule;
    }

    private void loadSpreadsheetSchedule() {
        List<String> spreadsheetSchedulePathList = getSpreadsheetSchedulePaths();

        List<ScheduleSource> scheduleSources = spreadsheetSchedulePathList.stream()
                .map(this::getSpreadsheetSchedule).collect(Collectors.toList());

        spreadsheetSchedule = new SpreadsheetScheduleSource(scheduleSources);
    }

    private List<String> getSpreadsheetSchedulePaths() {
        String departmentsListURL = String.join("/",
                configuration.getProperty("site.path"), configuration.getProperty("site.departments.list"));

        try {
            Document doc = Jsoup.connect(departmentsListURL).get();

            return doc.select("p[class='P1'] a")
                    .stream()
                    .filter(element -> !element.attr("href").contains("view_zamen.php"))
                    .map(element -> element.attr("href"))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private ScheduleSource getSpreadsheetSchedule(String spreadsheetHttpPath) {
        Workbook workbook = loadSpreadsheetWorkbookSchedule(spreadsheetHttpPath);

        SpreadsheetScheduleParser spreadsheetParser = new SpreadsheetScheduleParser();

        spreadsheetParser.parse(workbook);

        return spreadsheetParser;
    }

    private Workbook loadSpreadsheetWorkbookSchedule(String spreadsheetHttpPath) {
        return spreadsheetWorkbookProvider.provide(spreadsheetHttpPath);
    }

    private void loadReplacementSchedule() {
        String replacementScheduleLink = String.join("/",
                configuration.getProperty("site.path"),
                configuration.getProperty("schedule.site.filename"));

        Document rawReplacementSchedule = loadRawSiteSchedule(replacementScheduleLink);

        ReplacementsParser replacementParser = new ReplacementsParser(rawReplacementSchedule);
        replacementParser.parse();

        replacementSchedule = replacementParser;
    }

    private Document loadRawSiteSchedule(String link) {
        try {
            Connection connection = Jsoup.connect(link);
            return connection.get();
        } catch (IOException e) {
            throw new IllegalStateException("Расписание с сайта или сам сайт ntgmk.ru недоступен!", e);
        }
    }

    private void loadScheduleModel() {
        scheduleModel.setSpreadsheetSchedule(spreadsheetSchedule);
        scheduleModel.setReplacementsSchedule(replacementSchedule);
        scheduleModel.setCollapsedSchedule(collapsedSchedule);
        scheduleModel.init();
    }

    private void loadConfiguration() {
        try {
            configuration.load(getClass().getResourceAsStream("/app.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(Stage primaryStage) {
        loadSceneNavigator(primaryStage);

        Services.getService(SceneNavigationService.class).apply("groupChoose");
    }

    private void loadSceneNavigator(Stage primaryStage) {
        SceneNavigationService sceneNavigationService = new SceneNavigationService(primaryStage, configuration);

        GroupChooseController groupChooseController = new GroupChooseController(configuration);
        groupChooseController.setModel(scheduleModel);

        ScheduleController scheduleController = new ScheduleController();
        scheduleController.setModel(scheduleModel);

        sceneNavigationService.registerSceneRoot("groupChoose", groupChooseController);
        sceneNavigationService.registerSceneRoot("schedule", scheduleController);

        Services.addService(sceneNavigationService);
    }

    public static void main(String[] args) {
        launch(ScheduleNow.class, args);
    }

    @Override
    public void stop() throws Exception {
        appProperties.save();
    }
}
