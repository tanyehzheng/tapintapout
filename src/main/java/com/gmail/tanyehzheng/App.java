package com.gmail.tanyehzheng;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

import com.gmail.tanyehzheng.model.MyFile;
import com.gmail.tanyehzheng.model.WorkingDay;
import com.gmail.tanyehzheng.model.WorkingMonth;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        // Select file
        final Path path = Paths.get("/Users/yehzhengtan/Downloads/2022-08-01_2022-08-31月签卡记录报表.xls");

        // Parse file into mem
        MyFile file = new MyFile(path);

        // Transform into domain objects
        List<WorkingMonth> workingMonth = file.getEmployees();
        WorkingDay day = workingMonth.get(0).getWorkingDay(1);
        Duration total = day.getTotalHoursWorked();
        System.out.printf("%s, totalHours: %s, hour: %d, minutes: %d", day, total, total.toHoursPart(), total.toMinutesPart());
    }
}
