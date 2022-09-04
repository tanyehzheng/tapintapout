package com.gmail.tanyehzheng.model;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class MyFile {

    private final Path path;
    private final LocalDate firstDate;
    private Map<Integer, String> header;// column index => header value
    private List<WorkingMonth> employees;

    public MyFile(Path path) {
        this.path = path;
        this.firstDate = LocalDate.parse(path.getFileName().toString().substring(0, 10));
        this.header = new TreeMap<>();
    }

    public List<WorkingMonth> getEmployees() {
        if (null == employees) {
            parseEmployees();
        }
        return employees;
    }

    private void parseHeader(Row row) {
        for (Cell cell : row) {
            CellType type = cell.getCellType();
            switch (type) {
                case STRING:
                    header.put(cell.getColumnIndex(), cell.getStringCellValue());
                    break;
                case NUMERIC:
                    header.put(cell.getColumnIndex(), String.valueOf((int) cell.getNumericCellValue()));
                    break;
                default:

            }
        }
        // header.forEach((k,v)->{
        // System.out.printf("%d : %s\n", k,v);
        // });
    }

    private String getEmployeeId(Cell cell) {
        CellType type = cell.getCellType();
        switch (type) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue()); // usually integer
            default:
                // ignore
        }
        return null;
    }

    private WorkingMonth parseEmployee(Row row) {
        WorkingMonth ret = new WorkingMonth();
        Employee employee = new Employee();
        ret.setEmployee(employee);
        for (Cell cell : row) {
            switch (cell.getColumnIndex()) {
                case 0: // 姓名
                    employee.setName(cell.getStringCellValue());
                    break;
                case 1: // 工号
                    employee.setEmployeeId(getEmployeeId(cell));
                    break;
                default: //rest are numbers representing the day of the month
                    ret.putWorkingDay(Integer.parseInt(header.get(cell.getColumnIndex())), parseTapInTapOut(cell));
            }
        }
        return ret;
    }

    private WorkingDay parseTapInTapOut(Cell cell) {
        // System.out.println(cell.getStringCellValue() + "\n");
        String content = cell.getStringCellValue();
        String[] splitted = content.split("\n");
        if (splitted.length == 1) {
            // day off?
            if (splitted[0].length() == 0)
                return WorkingDay.ON_LEAVE;
            // else
            //     throw new RuntimeException("Unknown input at row " + (cell.getRowIndex()+1) + " column " + (cell.getColumnIndex()+1) + " with content '" + content + "'");
        }
        List<LocalDateTime> ret = new ArrayList<>(splitted.length);
        LocalDate currentDay = firstDate.withDayOfMonth(Integer.parseInt(header.get(cell.getColumnIndex())));
        for (int i = 0; i < splitted.length; i++) {
            String time = splitted[i];
            if (time.length() == 0) {
                // System.out.println("time: '" + time.length() + "'");
                continue;
            }
            if (time.length() > 5) {
                time = time.substring(time.length() - 5);
                ret.add(LocalTime.parse(time).atDate(currentDay.plusDays(1)));
            } else
                ret.add(LocalTime.parse(time).atDate(currentDay));
        }
        return new WorkingDay(ret);
    }

    private void parseEmployees() {
        try (XSSFWorkbook workbook = new XSSFWorkbook(Files.newInputStream(path, StandardOpenOption.READ))) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            boolean isFirstRow = true;
            List<WorkingMonth> list = new ArrayList<>();
            for (Row row : sheet) {
                if (isFirstRow) {
                    parseHeader(row);
                    isFirstRow = false;
                    continue;
                }
                WorkingMonth month = parseEmployee(row);
                list.add(month);
            }

            this.employees = list;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
