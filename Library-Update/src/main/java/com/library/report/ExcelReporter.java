package com.library.report;


import com.library.dto.BookDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.util.*;

public class ExcelReporter {

    // !!! USER ******************
    static String SHEET_TOTAL = "TOTAL";
    static String[] TOTAL_HEADERS = {"Books","Authors","Publishers","Categories",
            "Loans","unReturnedBooks","Members","Expired Book"};


    // !!! CAR ******************
    static String SHEET_BOOK="UnReturned Books";
    static  String[] BOOK_HEADERS = {"id", "Name", "ISBN", "Page Count",
            "PublishDate", "Loanable", "ShelfCode",
            "Active", "Feature", "CreateDate","Author Name","Publisher Name",
            "Category Name","ImageId"};



    //*********************************************************
    //*******************USER_REPORT***********************
    //*********************************************************
    public static ByteArrayInputStream getTotalExcelReport(Long userCount,Long loanCount,Long bookCount,
                                                           Long publisherCount,Long categoryCount,Long authorCount,Long unReturnedBooks,Long expiredBook) throws IOException {
        Workbook workbook = new XSSFWorkbook();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Sheet sheet = workbook.createSheet(SHEET_TOTAL);
        Row headerRow =  sheet.createRow(0);

        // header row dolduruluyor
        for(int i=0; i< TOTAL_HEADERS.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(TOTAL_HEADERS[i]);
        }

        // dataları dolduruyoruz
        int rowId = 1;

        Row row = sheet.createRow(rowId++);
        row.createCell(0).setCellValue(bookCount);
        row.createCell(1).setCellValue(authorCount);
        row.createCell(2).setCellValue(publisherCount);
        row.createCell(3).setCellValue(categoryCount);
        row.createCell(4).setCellValue(loanCount);
        row.createCell(5).setCellValue(unReturnedBooks);
        row.createCell(6).setCellValue(userCount);
        row.createCell(7).setCellValue(expiredBook);

        // Customer , Administrator
        workbook.write(out);
        workbook.close();

        return new ByteArrayInputStream(out.toByteArray());
    }



    //*********************************************************
    //*******************CAR_REPORT***********************
    //*********************************************************
    public static ByteArrayInputStream getBookExcelReport(List<BookDTO> books) throws IOException {
        Workbook workbook = new XSSFWorkbook();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Sheet sheet = workbook.createSheet(SHEET_BOOK);
        Row headerRow =  sheet.createRow(0);

        // header row dolduruluyor
        for(int i=0; i< BOOK_HEADERS.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(BOOK_HEADERS[i]);
        }

        // dataları dolduruyoruz
        int rowId = 1;
        for(BookDTO car : books) {
            Row row = sheet.createRow(rowId++);
            row.createCell(0).setCellValue(car.getId());
            row.createCell(1).setCellValue(car.getName());
            row.createCell(2).setCellValue(car.getIsbn());
            row.createCell(3).setCellValue(car.getPageCount());
            row.createCell(4).setCellValue(car.getPublishDate());
            row.createCell(5).setCellValue(car.getLoanable());
            row.createCell(6).setCellValue(car.getShelfCode());
            row.createCell(7).setCellValue(car.getActive());
            row.createCell(8).setCellValue(car.getFeature());
            row.createCell(9).setCellValue(car.getCreateDate().toString());
            row.createCell(10).setCellValue(car.getAuthorName());
            row.createCell(11).setCellValue(car.getPublisherName());
            row.createCell(12).setCellValue(car.getCategoryName());
            row.createCell(13).setCellValue(car.getImageId());
        }
        workbook.write(out);
        workbook.close();
        return new ByteArrayInputStream(out.toByteArray());

    }
}