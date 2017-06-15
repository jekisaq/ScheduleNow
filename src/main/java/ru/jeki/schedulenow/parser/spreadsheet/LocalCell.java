package ru.jeki.schedulenow.parser.spreadsheet;

import org.apache.poi.ss.usermodel.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocalCell {
    private final Workbook workbook;
    private final Cell cell;

    LocalCell(Workbook workbook, Cell cell) {
        this.workbook = workbook;
        this.cell = cell;
    }

    public String getContent() {
        return cell.getStringCellValue();
    }

    private boolean isTextBold() {
        CellStyle cellStyle = cell.getCellStyle();
        short fontIndex = cellStyle.getFontIndex();
        Font font = workbook.getFontAt(fontIndex);
        return font.getBold();
    }

    boolean isBorderTopStyle(BorderStyle borderStyle) {
        return cell.getCellStyle().getBorderTopEnum() == borderStyle;
    }

    boolean isTeacherDescription() {
        String lectureDescription = cell.getStringCellValue();
        Pattern descriptionPattern = Pattern.compile("^\\p{IsCyrillic}+(\\s\\d{1,3}-\\d?)?$");
        Matcher matcher = descriptionPattern.matcher(lectureDescription);
        return matcher.find() && !isTextBold();
    }

    boolean hasLessonName() {
        return isTextBold() && !cell.getStringCellValue().isEmpty();
    }
}
