package com.example.demo.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.entity.KitchenStaff;
import com.example.demo.entity.Manager;
import com.example.demo.entity.ServiceStaff;
import com.example.demo.entity.Staff;

public class StaffRepository {

    private static final String FILE_PATH = "data/staff.txt";

    public List<Staff> findAll() {
        List<Staff> list = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return list;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                
                String[] parts = line.split("\\|");
                if (parts.length >= 5) {
                   
                    Long id = Long.parseLong(parts[0]);
                    String username = parts[1];
                    String password = parts[2];
                    String name = parts[3];
                    String role = parts[4];
                    Staff s = null;

                    if ("Manager".equalsIgnoreCase(role)) {
                        s = new Manager(id, username, password, name);
                    } else if ("Kitchen Staff".equalsIgnoreCase(role)) {
                        s = new KitchenStaff(id, username, password, name);
                    } else {
                        s = new ServiceStaff(id, username, password, name);
                    }

                    if (s != null) {
                        list.add(s);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Staff findById(Long id) {
        return findAll().stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null);
    }
    
    
    public Staff findByUsername(String username) {
        return findAll().stream().filter(s -> s.getUsername().equals(username)).findFirst().orElse(null);
    }

    public void save(Staff staff) {
        List<Staff> list = findAll();

        if (staff.getId() == null) {
          
            long maxId = list.stream().mapToLong(Staff::getId).max().orElse(0);
            staff.setId(maxId + 1);
            list.add(staff);
        } else {
            
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getId().equals(staff.getId())) {
                    list.set(i, staff);
                    break;
                }
            }
        }
        rewriteFile(list);
    }

    public void deleteById(Long id) {
        List<Staff> list = findAll();
        list.removeIf(s -> s.getId().equals(id));
        rewriteFile(list);
    }

    private void rewriteFile(List<Staff> list) {
        File file = new File(FILE_PATH);
        file.getParentFile().mkdirs(); // สร้าง folder data ถ้ายังไม่มี

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            for (Staff s : list) {
                //ID|Username|Password|Name|Role
                String line = String.format("%d|%s|%s|%s|%s",
                        s.getId(), s.getUsername(), s.getPassword(), s.getName(), s.getRole());
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public List<Staff> searchAndSort(String keyword, String sortMode, String roleFilter) {
        List<Staff> list = findAll();
        
        //Filter Role
        if (roleFilter != null && !roleFilter.isEmpty()) {
            list = list.stream()
                .filter(s -> s.getRole().equalsIgnoreCase(roleFilter))
                .collect(Collectors.toList());
        }

        //Search
        if (keyword != null && !keyword.trim().isEmpty()) {
            String key = keyword.toLowerCase().trim();
            list = list.stream()
                .filter(s -> s.getName().toLowerCase().contains(key) || 
                             s.getUsername().toLowerCase().contains(key))
                .collect(Collectors.toList());
        }

        // Sort
        if (sortMode != null) {
            switch (sortMode) {
                case "name_asc": list.sort(Comparator.comparing(Staff::getName)); break;
                case "name_desc": list.sort(Comparator.comparing(Staff::getName).reversed()); break;
                case "role_asc": list.sort(Comparator.comparingInt(s -> getRolePriority(s.getRole()))); break;
                case "role_desc": list.sort((s1, s2) -> getRolePriority(s2.getRole()) - getRolePriority(s1.getRole())); break;
                default: list.sort(Comparator.comparingLong(Staff::getId));
            }
        }
        
        return list;
    }
    private int getRolePriority(String role) {
        if (role == null) return 99;
        if ("Manager".equalsIgnoreCase(role)) return 1;       
        if ("Kitchen Staff".equalsIgnoreCase(role)) return 2; 
        if ("Staff".equalsIgnoreCase(role)) return 3;         
        return 4; 
    }
}