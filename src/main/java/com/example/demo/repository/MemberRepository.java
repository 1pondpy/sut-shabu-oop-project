package com.example.demo.repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.entity.Member;

public class MemberRepository {

    private static final String FILE_PATH = "data/members.txt";
    public List<Member> findAll() {
        List<Member> members = new ArrayList<>();
        BufferedReader reader = null;
        try {
            File f = new File(FILE_PATH);
            if (f.exists()) {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8));
            } else {
                InputStream is = getClass().getClassLoader().getResourceAsStream("data/members.txt");
                if (is == null) {
                    return members; 
                }
                reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            }

            String line;
            while ((line = reader.readLine()) != null) {
                Member member = parseLine(line);
                if (member != null) {
                    members.add(member);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try { reader.close(); } catch (IOException ignored) {}
            }
        }
        return members;
    }
    
    // ค้นหาด้วยเบอร์โทร
    public Member findByPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) return null;
        String normalized = phoneNumber.replaceAll("\\D", "").trim();
        List<Member> members = findAll();
        for (Member member : members) {
            String mp = member.getPhoneNumber() == null ? "" : member.getPhoneNumber().replaceAll("\\D", "").trim();
            if (mp.equals(normalized) || mp.endsWith(normalized) || normalized.endsWith(mp)) {
                return member;
            }
        }
        return null;
    }
    
    // ค้นหาด้วย ID
    public Member findById(Long id) {
        List<Member> members = findAll();
        for (Member member : members) {
            if (member.getId().equals(id)) {
                return member;
            }
        }
        return null;
    }

    public void save(Member member) {
        File f = new File(FILE_PATH);
        f.getParentFile().mkdirs();
        try { if (!f.exists()) f.createNewFile(); } catch (IOException ignored) {}

        if (member.getId() == null) {
            long id = nextId(f);
            member.setId(id);
            try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.OutputStreamWriter(new java.io.FileOutputStream(f, true), java.nio.charset.StandardCharsets.UTF_8))) {
                bw.write(formatLine(member));
                bw.newLine();
            } catch (IOException ignored) {}
        } else {
            List<String> lines = new ArrayList<>();
            try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
                String line;
                while ((line = r.readLine()) != null) lines.add(line);
            } catch (IOException ignored) {}

            String newLine = formatLine(member);
            boolean changed = false;
            for (int i = 0; i < lines.size(); i++) {
                String[] p = lines.get(i).split("\\s+", -1);
                if (p.length > 0) {
                    try { long id = Long.parseLong(p[0].trim()); if (id == member.getId()) { lines.set(i, newLine); changed = true; break; } } catch (Exception ignored) {}
                }
            }
            if (changed) {
                try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.OutputStreamWriter(new java.io.FileOutputStream(f, false), java.nio.charset.StandardCharsets.UTF_8))) {
                    for (String L : lines) { bw.write(L); bw.newLine(); }
                } catch (IOException ignored) {}
            }
        }
    }

    private String formatLine(Member m) {
        StringBuilder sb = new StringBuilder();
        sb.append(m.getId() != null ? m.getId() : "");
        sb.append(' ').append(m.getPhoneNumber() != null ? m.getPhoneNumber() : "");
        sb.append(' ').append(m.getName() != null ? m.getName() : "");
        sb.append(' ').append(m.getPoints() != null ? m.getPoints() : 0);
        sb.append(' ').append(m.getGameChances() != null ? m.getGameChances() : 0);
        return sb.toString();
    }

    private long nextId(File f) {
        long max = 0;
        if (!f.exists()) return 1;
        try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] p = line.split("\\s+", -1);
                if (p.length > 0) {
                    try { long id = Long.parseLong(p[0].trim()); if (id > max) max = id; } catch (Exception ignored) {}
                }
            }
        } catch (IOException ignored) {}
        return max + 1;
    }
    
 
    private Member parseLine(String line) {
        try {
            String[] parts = line.split("\\s+");
            if (parts.length < 5) return null;
            
            Member member = new Member();
            member.setId(Long.parseLong(parts[0]));
            member.setPhoneNumber(parts[1]);
            member.setName(String.join(" ", java.util.Arrays.copyOfRange(parts, 2, parts.length - 2)));
            member.setPoints(Integer.parseInt(parts[parts.length - 2]));
            member.setGameChances(Integer.parseInt(parts[parts.length - 1]));
            
            return member;
        } catch (Exception e) {
            return null;
        }
    }
    public void deleteById(Long id) {
        File f = new File(FILE_PATH);
        if (!f.exists()) return;

        List<String> lines = new ArrayList<>();
        try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
            String line;
            while ((line = r.readLine()) != null) {
                // อ่าน ID ตัวหน้าสุด
                String[] parts = line.split("\\s+");
                if (parts.length > 0) {
                    try {
                        long currentId = Long.parseLong(parts[0]);
                        // ถ้า ID ไม่ตรงกับที่จะลบ ให้เก็บไว้ (ถ้าตรง ให้ข้าม = ลบ)
                        if (currentId != id) {
                            lines.add(line);
                        }
                    } catch (Exception e) {
                        lines.add(line); // บรรทัด error เก็บไว้เหมือนเดิม
                    }
                }
            }
        } catch (IOException ignored) {}

       
        try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.OutputStreamWriter(new java.io.FileOutputStream(f, false), java.nio.charset.StandardCharsets.UTF_8))) {
            for (String L : lines) {
                bw.write(L);
                bw.newLine();
            }
        } catch (IOException ignored) {}
    }

    public List<Member>searchAndSort(String keyword, String sortMode) {
        List<Member> allMembers = findAll();
        
        //Search "ชื่อ" และ "เบอร์โทร"
        if (keyword != null && !keyword.trim().isEmpty()) {
            String lowerKey = keyword.toLowerCase().trim();
            allMembers = allMembers.stream()
                .filter(m -> m.getName().toLowerCase().contains(lowerKey) || 
                             m.getPhoneNumber().contains(lowerKey))
                .collect(Collectors.toList());
        }

        // Sort
        if (sortMode != null) {
            switch (sortMode) {
                case "name_asc": // ชื่อ ก-ฮ
                    allMembers.sort(Comparator.comparing(Member::getName));
                    break;
                case "name_desc": // ชื่อ ฮ-ก
                    allMembers.sort(Comparator.comparing(Member::getName).reversed());
                    break;
                case "points_desc": // แต้ม มาก->น้อย
                    allMembers.sort(Comparator.comparingInt(Member::getPoints).reversed());
                    break;
                case "points_asc": // แต้ม น้อย->มาก
                    allMembers.sort(Comparator.comparingInt(Member::getPoints));
                    break;
                default: // Default เรียงตาม ID
                    allMembers.sort(Comparator.comparingLong(Member::getId));
            }
        }
        
        return allMembers;
    }

}
