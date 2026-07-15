<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>Member Management - SUT Shabu</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/manager.css">
    <style>
      
         #formName, #formPhone, #formPoints {
            font-family: 'Noto Sans Thai', sans-serif;
        }
        .header-yellow { background-color: #fdd835 !important; color: #000 !important; }
        .bg-cream { background-color: #fff8e1; }
        .sort-icon {
            color: inherit;
            margin-left: 5px;
            cursor: pointer;
            opacity: 0.5;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
        }
        .sort-icon:hover { opacity: 1; }
        .sort-icon.active {
            color: #000;     
            opacity: 1;
        }
        

        .menu-table tbody tr td:nth-child(3),
        .menu-table tbody tr td:nth-child(4) {
            text-align: right;
            padding-right: 50px;
        }
        
        
        .menu-table thead tr th:nth-child(3),
        .menu-table thead tr th:nth-child(4) {
            text-align: center;
        }
        
        .search-btn {
            position: absolute;
            right: 20px;
            top: 50%;
            transform: translateY(-50%);
            background: none;
            border: none;
            color: #888;
            cursor: pointer;
            font-size: 14px;
            padding: 5px;
        }
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
        <a href="manager-staff" class="nav-item"><i class="fas fa-users-cog"></i> Staff Management</a>
        <a href="#" class="nav-item active"><i class="fas fa-users"></i> Member Management</a>
        <a href="manager-reward" class="nav-item"><i class="fas fa-gamepad"></i> Reward & Game Management</a>
        <a href="${pageContext.request.contextPath}/logout" class="nav-item logout-btn" onclick="return confirm('ยืนยันออกจากระบบ?');"><i class="fas fa-sign-out-alt"></i> ออกจากระบบ</a>
    </nav>

    <main class="main-content">
        <div class="header">
            <h2>Member Management</h2>
        </div>
        
        <div class="content-body bg-cream">
            <div class="action-bar">
                
                <form action="manager-member" method="get" class="search-box">
                    <input type="hidden" name="sort" value="${currentSort}">
                    
                    <input type="text" id="liveSearchInput" name="search" 
                           placeholder="ค้นหาชื่อ หรือ เบอร์โทร" 
                           value="${currentSearch}" 
                           onkeyup="liveSearch()" 
                           autocomplete="off">
                           
                    <button type="submit" class="search-btn">
                            <i class="fas fa-search"></i>
                        </button>
                </form>
                
                <button class="btn-add" onclick="openModal()">+ เพิ่มสมาชิก</button>
            </div>

            <table class="menu-table">
                <thead>
                    <tr>
                        <th class="header-yellow" width="30%">
                            ชื่อลูกค้า
                            <a href="manager-member?sort=name_asc&search=${currentSearch}" class="sort-icon ${currentSort == 'name_asc' ? 'active' : ''}" title="ก-ฮ"><i class="fas fa-sort-alpha-down"></i></a>
                            <a href="manager-member?sort=name_desc&search=${currentSearch}" class="sort-icon ${currentSort == 'name_desc' ? 'active' : ''}" title="ฮ-ก"><i class="fas fa-sort-alpha-up"></i></a>
                        </th>
                        <th class="header-yellow" width="20%">เบอร์โทร</th>
                        <th class="header-yellow" width="20%">
                            แต้มสะสม
                            <a href="manager-member?sort=points_asc&search=${currentSearch}" class="sort-icon ${currentSort == 'points_asc' ? 'active' : ''}" title="น้อยไปมาก"><i class="fas fa-sort-numeric-down"></i></a>
                            <a href="manager-member?sort=points_desc&search=${currentSearch}" class="sort-icon ${currentSort == 'points_desc' ? 'active' : ''}" title="มากไปน้อย"><i class="fas fa-sort-numeric-up"></i></a>
                            
                        </th>
                        <th class="header-yellow" width="15%">สิทธิ์เล่นเกม</th>
                        <th class="header-yellow" width="15%">จัดการ</th>
                    </tr>
                </thead>
                <tbody id="tableBody">
                    <c:forEach var="m" items="${members}">
                        <tr>
                            <td style="font-weight: bold;">${m.name}</td>
                            <td>${m.phoneNumber}</td>
                            <td>
                                <span class="badge" style="color:#fbc02d;">
                                    <i class="fas fa-coins"></i> <fmt:formatNumber value="${m.points}" type="number"/>
                                </span>
                            </td>
                            <td>${m.gameChances}</td>
                            <td>
                                <button class="action-btn btn-edit" 
                                    onclick="editMember('${m.id}', '${m.name}', '${m.phoneNumber}', '${m.points}')">
                                    <i class="far fa-edit"></i>
                                </button>
                                <a href="manager-member?action=delete&id=${m.id}" class="action-btn btn-delete" 
                                   onclick="return confirm('ยืนยันลบสมาชิก ${m.name}?');">
                                    <i class="far fa-trash-alt"></i>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                     <c:if test="${empty members}">
                        <tr><td colspan="5" style="text-align:center; padding:20px;">ไม่พบข้อมูลสมาชิก</td></tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </main>

    <div id="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h3 id="modalTitle">เพิ่มสมาชิกใหม่</h3>
            </div>
            <form action="manager-member" method="post">
                <input type="hidden" name="id" id="formId">
                <div class="modal-body">
                    <div class="form-group">
                        <label>ชื่อ-นามสกุล</label>
                        <input type="text" name="name" id="formName" required placeholder="ชื่อ นามสกุล">
                    </div>
                    <div class="form-group">
                        <label>เบอร์โทรศัพท์</label>
                        <input type="text" name="phoneNumber" id="formPhone" required placeholder="08xxxxxxxx"  oninput="this.value = this.value.replace(/[^0-9]/g, '')"
       maxlength="10">
                    </div>
                    <div class="form-group">
                        <label>แต้มสะสม</label>
                        <input type="text" name="points" id="formPoints" value="0" >
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
                let tdPhone = tr[i].getElementsByTagName("td")[1]; 
                
                if (tdName || tdPhone) {
                    let txtName = tdName.textContent || tdName.innerText;
                    let txtPhone = tdPhone.textContent || tdPhone.innerText;
                    if (txtName.toUpperCase().indexOf(filter) > -1 || txtPhone.indexOf(filter) > -1) {
                        tr[i].style.display = "";
                    } else {
                        tr[i].style.display = "none";
                    }
                }       
            }
        }

        function openModal() {
            document.getElementById('modal').style.display = 'flex';
            document.getElementById('modalTitle').innerText = 'เพิ่มสมาชิกใหม่';
            document.getElementById('formId').value = '';
            document.getElementById('formName').value = '';
            document.getElementById('formPhone').value = '';
            document.getElementById('formPoints').value = '0';
        }

        function editMember(id, name, phone, points) {
            document.getElementById('modal').style.display = 'flex';
            document.getElementById('modalTitle').innerText = 'แก้ไขข้อมูลสมาชิก';
            document.getElementById('formId').value = id;
            document.getElementById('formName').value = name;
            document.getElementById('formPhone').value = phone;
            document.getElementById('formPoints').value = points;
        }

        function closeModal() {
            document.getElementById('modal').style.display = 'none';
        }
        
        window.onclick = function(e) {
            if (e.target == document.getElementById('modal')) closeModal();
        }
        document.querySelectorAll('.sort-icon').forEach(icon => {
    icon.addEventListener('click', function () {
        document.querySelectorAll('.sort-icon')
            .forEach(i => i.classList.remove('active'));

        this.classList.add('active');
    });
});

    </script>
</body>
</html>