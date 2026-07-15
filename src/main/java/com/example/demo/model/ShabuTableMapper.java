package com.example.demo.model;

import com.example.demo.entity.TableSession;
import com.example.demo.entity.Tables;
import java.time.format.DateTimeFormatter;

public class ShabuTableMapper {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static ShabuTable fromSession(TableSession s, Tables t) {
        String number = (s != null && s.getTableNo() != null && !s.getTableNo().isEmpty()) ? s.getTableNo()
                : (t != null ? t.getTableNo() : "");
        String status = (s != null && s.getStatus() != null && !s.getStatus().isEmpty()) ? s.getStatus()
                : (t != null ? "AVAILABLE" : "EMPTY");
        int pax = (s != null && s.getPax() != null) ? s.getPax() : (t != null && t.getSeatCount() != null ? t.getSeatCount() : 0);
        String time = "";
        if (s != null && s.getStartTime() != null) time = s.getStartTime().format(FMT);

        ShabuTable st = new ShabuTable(number, status, pax, time, "");
        if (s != null) {
            st.setPhone(s.getCustomerPhone());
            st.setSoup(s.getSoup());
            st.setStartTime(s.getStartTime() != null ? s.getStartTime().format(FMT) : "");
        }
        return st;
    }

    public static ShabuTable fromTables(Tables t) {
        String number = (t != null && t.getTableNo() != null) ? t.getTableNo() : "";
        int pax = (t != null && t.getSeatCount() != null) ? t.getSeatCount() : 0;
        ShabuTable st = new ShabuTable(number, "AVAILABLE", pax, "", "");
        return st;
    }
}
