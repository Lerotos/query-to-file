package org.practice;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.sql.*;

public class Query {

    //tests user input credentials
    public static Connection testCon(String db, String user, String pass) throws SQLException {
        return DriverManager.getConnection(db, user, pass);
    }

    //queries the database and writes the result in an Excel file
    public static void query(String path, String query, String db, String user, String pass){
        try{
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet spreadsheet = workbook.createSheet("output");

            Connection con = testCon(db, user, pass);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();

            generateHeader(spreadsheet, rsmd);
            generateCells(rs, spreadsheet, rsmd);

            FileOutputStream out = new FileOutputStream(path);
            workbook.write(out);
            out.close();
            con.close();
        } catch(Exception e){MainWindow.exceptionWindow(e);}
    }

    //extracts and writes the column headers
    public static void generateHeader(XSSFSheet spreadsheet, ResultSetMetaData rsmd) throws SQLException {
        XSSFRow row = spreadsheet.createRow(0);
        int cellId = 0;
        while(rsmd.getColumnCount() > cellId){
            Cell cell = row.createCell(cellId++);
            cell.setCellValue(rsmd.getColumnLabel(cellId));
        }
    }

    //extracts and writes the cell data
    public static void generateCells(ResultSet rs, XSSFSheet spreadsheet, ResultSetMetaData rsmd) throws SQLException {
        int rowId = 1;
        while(rs.next()){
            XSSFRow row = spreadsheet.createRow(rowId++);
            int cellId = 0;
            while(rsmd.getColumnCount() > cellId){
                Cell cell = row.createCell(cellId++);
                inputCell(rsmd, cell, cellId, rs);
            }
        }
    }

    //sets the appropriate Excel cell type
    public static void inputCell(ResultSetMetaData rsmd, Cell cell, int cellId, ResultSet rs) throws SQLException {
        String className = rsmd.getColumnClassName(cellId);
        switch (className){
            case "java.lang.Integer":
                cell.setCellValue(rs.getInt(cellId));
                break;
            case "java.lang.Timestamp":
                cell.setCellValue(rs.getTimestamp(cellId));
                break;
            case "java.lang.Time":
                cell.setCellValue(rs.getTime(cellId));
                break;
            case "java.lang.Date":
                cell.setCellValue(rs.getDate(cellId));
                break;
            default:
                cell.setCellValue(rs.getString(cellId));
                break;
        }
    }
}
