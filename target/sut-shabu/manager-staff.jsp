<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Staff Management - SUT Shabu</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/manager.css">
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+Thai:wght@100..900&display=swap" rel="stylesheet">
    <style>
        .header-yellow {
            background-color: #fdd835 !important;
            color: #222 !important;
        }
        
        .bg-cream {
            background-color: #fff8e1;
        }
        
        #formName,
        #formUsername,
        #formPassword,
        #formRole {
            font-family: 'Noto Sans Thai', sans-serif;
        }


        #formRole {
            appearance: none;
            -webkit-appearance: none;
            -moz-appearance: none;
            padding-right: 36px;
            background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='12' height='8' viewBox='0 0 12 8'%3E%3Cpath fill='%23666' d='M1.41 0 6 4.59 10.59 0 12 1.41 6 7.41 0 1.41z'/%3E%3C/svg%3E");
            background-repeat: no-repeat;
            background-position: calc(100% - 14px) center;
            background-color: #fff;
        }

        .sort-icon {
            color: inherit;
            margin-left: 5px;
            cursor: pointer;
            opacity: 0.5;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
        }
        
        .sort-icon:hover {
            opacity: 1;
        }

        .sort-icon.active {
            opacity: 1;
        }

        .filter-form {
            display: inline-block;
            position: relative;
        }

        .filter-form select {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            opacity: 0;
            cursor: pointer;
        }

        .filter-icon {
            cursor: pointer;
            font-size: 14px;
            opacity: 0.7;
        }
        .filter-icon:hover { opacity: 1; }
        
        .logout-btn {
            font-size: 14px;
            padding: 10px 10px;
            margin-top: auto;
            margin-bottom: 20px;
            background-color: none;
            color: white;
            border: #fff solid 1px;
            border-radius: 40px;
            width: 70%;
            align-self: center;
        }
        
        .logout-btn:hover {
            background-color: #fff;
            color: rgb(0, 0, 0);
        }
    </style>
</head>
<body>

    <nav class="sidebar">
        <div class="logo">
             <img src="${pageContext.request.contextPath}/images/logo.png" alt="SUT Shabu" style= "width: 100px; margin-top: 10px;">
        </div>
        <a href="${pageContext.request.contextPath}/dashboard" class="nav-item"><i class="fas fa-chart-line"></i> Dashboard</a>
        <a href="manager-menu" class="nav-item"><i class="fas fa-clipboard-list"></i> Menu Management</a>
        <a href="manager-staff" class="nav-item active"><i class="fas fa-users-cog"></i> Staff Management</a>
        <a href="manager-member" class="nav-item"><i class="fas fa-users"></i> Member Management</a>
        <a href="manager-reward" class="nav-item"><i class="fas fa-gamepad"></i> Reward & Game Management</a>
        <a href="${pageContext.request.contextPath}/logout" class="nav-item logout-btn" onclick="return confirm('ยืนยันออกจากระบบ?');"><i class="fas fa-sign-out-alt"></i> ออกจากระบบ</a>
    </nav>

    <main class="main-content">
        <div class="header">
            <h2>Staff Management</h2>
        </div>
        
        <div class="content-body bg-cream">
            <div class="action-bar">
                
                <form action="manager-staff" method="get" class="search-box">
                    <input type="hidden" name="sort" value="${currentSort}">
                    <input type="hidden" name="roleFilter" value="${currentRole}">
                    
                    <input type="text" id="liveSearchInput" name="search" 
                           placeholder="ค้นหาพนักงาน..." 
                           value="${currentSearch}" 
                           onkeyup="liveSearch()" 
                           autocomplete="off">
                           
                    <button type="submit" style="background:none; border:none; position:absolute; right:15px; top:50%; transform:translateY(-50%); cursor:pointer; color:#888;">
                        <i class="fas fa-search"></i>
                    </button>
                </form>
                
                <button class="btn-add" onclick="openModal()">+ เพิ่มพนักงาน</button>
            </div>

            <table class="menu-table">
                <thead>
                    <tr>
                        <th class="header-yellow" width="30%">
                            ชื่อ-นามสกุล
                            <a href="manager-staff?sort=name_asc&search=${currentSearch}" class="sort-icon ${currentSort == 'name_asc' ? 'active' : ''}">
                               <i class="fas fa-sort-alpha-down"></i>
                            </a>
                            <a href="manager-staff?sort=name_desc&search=${currentSearch}" class="sort-icon ${currentSort == 'name_desc' ? 'active' : ''}">
                                <i class="fas fa-sort-alpha-up"></i>
                            </a>
                        </th>
                        <th class="header-yellow" width="20%">ชื่อผู้ใช้</th>
                        <th class="header-yellow" width="25%">
                            ตำแหน่ง
                            <form action="manager-staff" method="get" class="filter-form">
                                <i class="fas fa-filter filter-icon"></i>
                                <select name="roleFilter" onchange="this.form.submit()">
                                    <option value="" ${empty currentRole ? 'selected' : ''}>ทั้งหมด</option> <option value="Manager" ${currentRole == 'Manager' ? 'selected' : ''}>Manager</option>
                                    <option value="Kitchen Staff" ${currentRole == 'Kitchen Staff' ? 'selected' : ''}>Kitchen</option>
                                    <option value="Staff" ${currentRole == 'Staff' ? 'selected' : ''}>Staff</option>
                                </select>
                            </form>
                        </th>

                        <th class="header-yellow" width="15%">จัดการ</th>
                    </tr>
                </thead>
                <tbody id="tableBody">
                    <c:forEach var="s" items="${staffList}">
                        <tr>
                            <td style="font-weight: bold;">${s.name}</td>
                            <td>${s.username}</td>
                            <td>
                                <span class="badge">
                                    ${s.role}
                                </span>
                            </td>
                            <td>
                                <button class="action-btn btn-edit" 
                                    onclick="editStaff('${s.id}', '${s.name}', '${s.username}', '${s.password}', '${s.role}')">
                                    <i class="far fa-edit"></i>
                                </button>
                                <a href="manager-staff?action=delete&id=${s.id}" class="action-btn btn-delete" 
                                   onclick="return confirm('ยืนยันลบพนักงาน ${s.name}?');">
                                    <i class="far fa-trash-alt"></i>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty staffList}">
                        <tr><td colspan="4" style="text-align:center; padding:20px;">ไม่พบข้อมูลพนักงาน</td></tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </main>

    <div id="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h3 id="modalTitle">เพิ่มพนักงานใหม่</h3>
            </div>
            <form action="manager-staff" method="post">
                <input type="hidden" name="id" id="formId">
                <div class="modal-body">
                    <div class="form-group">
                        <label>ชื่อ-นามสกุล</label>
                        <input type="text" name="name" id="formName" required placeholder="เช่น สมชาย ใจดี">
                    </div>
                    <div class="form-group">
                        <label>ชื่อผู้ใช้ (Username)</label>
                        <input type="text" name="username" id="formUsername" required placeholder="เช่น somchai_s">
                    </div>
                    <div class="form-group">
                        <label>รหัสผ่าน (Password)</label>
                        <input type="text" name="password" id="formPassword" placeholder="ตั้งรหัสผ่าน">
                    </div>
                    <div class="form-group">
                        <label>ตำแหน่ง</label>
                        <select name="role" id="formRole">
                            <option value="Staff">Staff (พนักงานทั่วไป)</option>
                            <option value="Kitchen Staff">Kitchen Staff (ครัว)</option>
                            <option value="Manager">Manager (ผู้จัดการ)</option>
                        </select>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn-cancel" onclick="closeModal()">ยกเลิก</button>
                    <button type="submit" class="btn-save">บันทึก</button>
                </div>
            </form>
        </div>
    </div>

    <script>
      
        function liveSearch() {
            let input = document.getElementById("liveSearchInput");
            let filter = input.value.toUpperCase();
            let table = document.getElementById("tableBody");
            let tr = table.getElementsByTagName("tr");

            for (let i = 0; i < tr.length; i++) {
                let tdName = tr[i].getElementsByTagName("td")[0]; 
                let tdUser = tr[i].getElementsByTagName("td")[1]; 
                
                if (tdName || tdUser) {
                    let txtName = tdName.textContent || tdName.innerText;
                    let txtUser = tdUser.textContent || tdUser.innerText;
                    
                    if (txtName.toUpperCase().indexOf(filter) > -1 || txtUser.toUpperCase().indexOf(filter) > -1) {
                        tr[i].style.display = "";
                    } else {
                        tr[i].style.display = "none";
                    }
                }       
            }
        }


        function openModal() {
            document.getElementById('modal').style.display = 'flex';
            document.getElementById('modalTitle').innerText = 'เพิ่มพนักงานใหม่';
            document.getElementById('formId').value = '';
            document.getElementById('formName').value = '';
            document.getElementById('formUsername').value = '';
            document.getElementById('formPassword').value = '';
            document.getElementById('formRole').value = 'Staff';
        }

        function editStaff(id, name, user, pass, role) {
            document.getElementById('modal').style.display = 'flex';
            document.getElementById('modalTitle').innerText = 'แก้ไขข้อมูลพนักงาน';
            document.getElementById('formId').value = id;
            document.getElementById('formName').value = name;
            document.getElementById('formUsername').value = user;
            document.getElementById('formPassword').value = pass;
            document.getElementById('formRole').value = role;
        }

        function closeModal() {
            document.getElementById('modal').style.display = 'none';
        }
        
        window.onclick = function(e) {
            if (e.target == document.getElementById('modal')) closeModal();
        }
    </script>
</body>
</html>