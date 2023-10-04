package org.practice;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.sql.*;
import java.util.Objects;

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
            XSSFRow row;
            int rowid = 0;

            Connection con = testCon(db, user, pass);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            while(rs.next()){
                row = spreadsheet.createRow(rowid++);
                int cellid = 0;
                while(rsmd.getColumnCount() > cellid){

                    Cell cell = row.createCell(cellid++);

                    inputCell(rsmd, cell, cellid, rs);


                }
            }

            FileOutputStream out = new FileOutputStream(path);
            workbook.write(out);
            out.close();
            con.close();
        } catch(Exception e){MainWindow.exceptionWindow(e);}
    }

    //sets the appropriate Excel cell type
    public static void inputCell(ResultSetMetaData rsmd, Cell cell, int cellId, ResultSet rs) throws SQLException {
        if(Objects.equals(rsmd.getColumnClassName(cellId), "java.lang.Integer")){
            cell.setCellValue(rs.getInt(cellId));
        } else if (Objects.equals(rsmd.getColumnClassName(cellId), "java.lang.Timestamp")) {
            cell.setCellValue(rs.getTimestamp(cellId));
        } else if (Objects.equals(rsmd.getColumnClassName(cellId), "java.lang.Time")) {
            cell.setCellValue(rs.getTime(cellId));
        } else if (Objects.equals(rsmd.getColumnClassName(cellId), "java.lang.Date")) {
            cell.setCellValue(rs.getDate(cellId));
        } else{
            cell.setCellValue(rs.getString(cellId));
        }
    }
}
