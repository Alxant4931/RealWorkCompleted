package ru.alxant.worker.model.support;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;

public class MyCellStyle {

    private static final Logger log = Logger.getLogger(MyCellStyle.class);

//    Workbook workbookStyle;
//    CellStyle style;
//
//    public MyCellStyle(Workbook workbookStyle) {
//        this.workbookStyle = workbookStyle;
//    }
//
//    public MyCellStyle(CellStyle style) {
//        this.style = style;
//    }

    public static CellStyle basicCellStyle(CellStyle style){
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }
}
