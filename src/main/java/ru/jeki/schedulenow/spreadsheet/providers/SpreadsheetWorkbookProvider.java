package ru.jeki.schedulenow.spreadsheet.providers;

import org.apache.poi.ss.usermodel.Workbook;

public interface SpreadsheetWorkbookProvider {
    Workbook provide(String path);
}
