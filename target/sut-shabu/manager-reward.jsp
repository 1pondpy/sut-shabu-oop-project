<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>Reward & Game Management - SUT Shabu</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/manager.css">
    <style>
  
        .header-yellow { background-color: #fdd835 !important; color: #222 !important; }
        .bg-cream { background-color: #fff8e1; }
        .sort-icon { color: inherit; margin-left: 5px; cursor: pointer; opacity: 0.5; text-decoration: none; display: inline-flex; align-items: center; transition: color 0.2s, opacity 0.2s; }
        .sort-icon:hover { opacity: 1; }
        .sort-icon.active { color: #000; opacity: 1; }
        .filter-form { display: inline-block; position: relative; margin-left: 5px; }
        .filter-form select { position: absolute; top: 0; left: 0; width: 100%; height: 100%; opacity: 0; cursor: pointer; }
        .filter-icon { cursor: pointer; font-size: 14px; opacity: 0.7; }
        .filter-icon:hover { opacity: 1; }
        /* CSS สำหรับการ์ดตั้งค่าแต้ม */
        .config-card {
            background: white;
            border-radius: 12px;
            padding: 25px;
            margin-bottom: 30px;
            box-shadow: 0 4px 10px rgba(0,0,0,0.05);
            display: flex;
            align-items: center;
            gap: 15px;
        }
        .config-title { font-size: 18px; font-weight: bold; margin-bottom: 15px; display: flex; align-items: center; gap: 10px; }
        .config-input {
            width: 80px; text-align: center; padding: 8px;
            border: 1px solid #ddd; border-radius: 8px; font-size: 16px;
        }
        .config-input:disabled { background-color: #f9f9f9; color: #555; }
        
        .btn-grey { background-color: #e0e0e0; color: #333; border: none; padding: 8px 15px; border-radius: 20px; cursor: pointer; font-weight: bold; }
        .btn-yellow-sm { background-color: #fdd835; color: #333; border: none; padding: 8px 20px; border-radius: 20px; cursor: pointer; font-weight: bold; }
        #formName, #formPoints, #formValue, #formType {
           font-family: 'Noto Sans Thai', sans-serif;
        }
        #formType{
             appearance: none;
            -webkit-appearance: none;
            -moz-appearance: none;
            padding-right: 36px;
            background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='12' height='8' viewBox='0 0 12 8'%3E%3Cpath fill='%23666' d='M1.41 0 6 4.59 10.59 0 12 1.41 6 7.41 0 1.41z'/%3E%3C/svg%3E");
            background-repeat: no-repeat;
            background-position: calc(100% - 14px) center;
            background-color: #fff;
        }
        
        /* Align numbers to right - points and discount value columns (after removing ID column) */
        .menu-table tbody tr td:nth-child(2),
        .menu-table tbody tr td:nth-child(3) {
            text-align: right;
            padding-right: 90px;
        }
        
        /* Center align headers */
        .menu-table thead tr th:nth-child(2),
        .menu-table thead tr th:nth-child(3),
        .menu-table thead tr th:nth-child(4) {
            text-align: center;
        }
        
        /* Left align unit column */
        .menu-table tbody tr td:nth-child(4) {
            text-align: left;
            padding-left: 80px;
        }
        
        /* Center align manage column */
        .menu-table tbody tr td:nth-child(5),
        .menu-table thead tr th:nth-child(5) {
            text-align: center;
        }
        
        /* Add left padding to reward name column */
        .menu-table tbody tr td:nth-child(1),
        .menu-table thead tr th:nth-child(1) {
            padding-left: 30px;
        }
        
        /* Style สำหรับปุ่ม Logout */
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
        <a href="manager-menu" class="nav-item"><i class="fas fa-clipboard-list"></i>Menu Management</a>
        <a href="manager-staff" class="nav-item"><i class="fas fa-users-cog"></i>Staff Management</a>
        <a href="manager-member" class="nav-item"><i class="fas fa-users"></i>  Member Management</a>
        <a href="#" class="nav-item active"><i class="fas fa-gamepad"></i>Reward & Game Management</a>
        <a href="${pageContext.request.contextPath}/logout" class="nav-item logout-btn" onclick="return confirm('ยืนยันออกจากระบบ?');"><i class="fas fa-sign-out-alt"></i> ออกจากระบบ</a>
    </nav>

    <main class="main-content">
        <div class="header">
            <h2>Rewards & Game Management</h2>
        </div>
        
        <div class="content-body bg-cream">
            
            <div class="config-card-container" style="background: white; padding: 20px; border-radius: 15px; margin-bottom: 20px;">
                <div class="config-title">
                    <i class="fas fa-coins"></i> ตั้งค่าอัตราการแจกแต้ม
                </div>
                <form action="manager-reward" method="post" id="configForm" style="display: flex; align-items: center; gap: 15px; flex-wrap: wrap;">
                    <input type="hidden" name="action" value="update_config">
                    
                    <span>ยอดชำระเงินครบ</span>
                    <input type="text" name="rateBaht" id="inputBaht" class="config-input" value="${configBaht}" disabled required>
                    <span>บาท</span>
                    
                    <span>ได้รับ</span>
                    <input type="text" name="ratePoints" id="inputPoints" class="config-input" value="${configPoints}" disabled required>
                    <span>แต้ม</span>

                    <button type="button" class="btn-grey" id="btnEditConfig" onclick="enableConfigEdit()">แก้ไข</button>
                    <button type="submit" class="btn-yellow-sm" id="btnSaveConfig" style="display: none;">บันทึก</button>

                </form>
            </div>
            <div class="config-title" style="margin-left: 20px;">
                    <i class="fas fa-dice"></i> ตั้งค่าของรางวัล
                </div>
            <div class="action-bar">
                <form action="manager-reward" method="get" class="search-box">
                    <input type="hidden" name="sort" value="${currentSort}">
                    <input type="hidden" name="discountTypeFilter" value="${currentDiscountType}">
                    
                    <input type="text" id="liveSearchInput" name="search" 
                           placeholder="ค้นหาชื่อรางวัล..." 
                           value="${currentSearch}" 
                           onkeyup="liveSearch()" 
                           autocomplete="off">
                           
                  
                        <i class="fas fa-search"></i>
                
                </form>
                
                <button class="btn-add" onclick="openModal()">+ เพิ่มของรางวัล</button>
            </div>

            <table class="menu-table">
                <thead>
                    <tr>
                        <th class="header-yellow" width="25%" >
                            ชื่อรางวัล 
                            <a href="manager-reward?sort=name_asc&search=${currentSearch}&discountTypeFilter=${currentDiscountType}" style="color:inherit; margin-left:5px;" class="sort-icon ${currentSort == 'name_asc' ? 'active' : ''}"><i class="fas fa-sort-alpha-down"></i></a>
                            <a href="manager-reward?sort=name_desc&search=${currentSearch}&discountTypeFilter=${currentDiscountType}" style="color:inherit; margin-left:2px;" class="sort-icon ${currentSort == 'name_desc' ? 'active' : ''}"><i class="fas fa-sort-alpha-up"></i></a>
                        </th>
                        <th class="header-yellow" width="20%">
                            แต้มที่ใช้
                            <a href="manager-reward?sort=points_asc&search=${currentSearch}&discountTypeFilter=${currentDiscountType}" class="sort-icon ${currentSort == 'points_asc' ? 'active' : ''}" style="color:inherit; margin-left:5px; font-family: 'Noto Sans Thai';" title="น้อยไปมาก"  ><i class="fas fa-sort-numeric-down"></i></a>
                            <a href="manager-reward?sort=points_desc&search=${currentSearch}&discountTypeFilter=${currentDiscountType}" class="sort-icon ${currentSort == 'points_desc' ? 'active' : ''}" style="color:inherit; margin-left:2px; font-family: 'Noto Sans Thai';" title="มากไปน้อย"><i class="fas fa-sort-numeric-up"></i></a>
                        </th>
                        <th class="header-yellow" width="20%">มูลค่าส่วนลด</th>
                        <th class="header-yellow" width="18%">
                            หน่วยส่วนลด
                            <form action="${pageContext.request.contextPath}/manager-reward" method="get" class="filter-form" style="margin-left:5px;">
                                <input type="hidden" name="search" value="${currentSearch}">
                                <input type="hidden" name="sort" value="${currentSort}">
                                <i class="fas fa-filter filter-icon" title="กรองหน่วยส่วนลด"></i>
                                <select name="discountTypeFilter" onchange="this.form.submit()">
                                    <option value="" ${empty currentDiscountType ? 'selected' : ''}>ทั้งหมด</option>
                                    <option value="PERCENT" ${currentDiscountType == 'PERCENT' ? 'selected' : ''}>%</option>
                                    <option value="BAHT" ${currentDiscountType == 'BAHT' ? 'selected' : ''}>บาท</option>
                                </select>
                            </form>
                        </th>
                        <th class="header-yellow" width="17%">จัดการ</th>
                    </tr>
                </thead>
                <tbody id="tableBody">
                    <c:forEach var="r" items="${rewards}">
                         <tr>
                            <td>${r.name}</td>
                            <td>
                                <span class="badge" style="color:#fbc02d;">
                                    <i class="fas fa-coins"></i> <fmt:formatNumber value="${r.pointsRequired}"/>
                                </span>
                            </td>
                            <td>
                                <fmt:formatNumber value="${r.discountValue}" maxFractionDigits="0"/>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${r.discountType == 'PERCENT'}">
                                        %
                                    </c:when>
                                    <c:otherwise>
                                        บาท
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <button class="action-btn btn-edit" 
                                    onclick="editReward('${r.id}', '${r.name}', '${r.pointsRequired}', '${r.discountType}', '${r.discountValue}')">
                                    <i class="far fa-edit"></i>
                                </button>
                                <a href="manager-reward?action=delete&id=${r.id}" class="action-btn btn-delete" 
                                   onclick="return confirm('ลบรางวัลนี้?');">
                                    <i class="far fa-trash-alt"></i>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    
                    <c:if test="${empty rewards}">
                        <tr><td colspan="5" style="text-align:center; padding:20px;">ไม่พบข้อมูลรางวัล</td></tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </main>

    <div id="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h3 id="modalTitle">เพิ่มรางวัล</h3>
            </div>
            <form action="manager-reward" method="post">
                <input type="hidden" name="id" id="formId">
                <div class="modal-body">
                    <div class="form-group">
                        <label>ชื่อรางวัล</label>
                        <input type="text" name="name" id="formName" required placeholder="เช่น ส่วนลด 10%">
                    </div>
                    <div class="form-group">
                        <label>แต้มที่ใช้แลก</label>
                        <input type="text" name="pointsRequired" id="formPoints" required placeholder="เช่น 500">
                    </div>
                    <div style="display: flex; gap: 15px;">
                        <div class="form-group" style="flex: 1;">
                            <label>ประเภท</label>
                            <select name="discountType" id="formType">
                                <option value="PERCENT">เปอร์เซ็นต์ (%)</option>
                                <option value="BAHT">บาท (THB)</option>
                            </select>
                        </div>
                        <div class="form-group" style="flex: 1;">
                            <label>มูลค่า</label>
                            <input type="text"  name="discountValue" id="formValue" required placeholder="จำนวน">
                        </div>
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
        // ฟังก์ชันใหม่: ปลดล็อคช่องตั้งค่า
        function enableConfigEdit() {
    // เปิดให้แก้ไข input
    document.getElementById('inputBaht').disabled = false;
    document.getElementById('inputPoints').disabled = false;

    // ซ่อนปุ่มแก้ไข
    document.getElementById('btnEditConfig').style.display = 'none';

    // แสดงปุ่มบันทึก
    document.getElementById('btnSaveConfig').style.display = 'inline-block';

    // โฟกัสช่องแรก
    document.getElementById('inputBaht').focus();
}

        // Form submission handler with alert
        (function() {
            const cfgForm = document.getElementById('configForm');
            if (cfgForm) {
                cfgForm.addEventListener('submit', function(ev) {
                    ev.preventDefault();
                    
                    // Enable inputs ก่อนสร้าง FormData (disabled inputs จะไม่ถูกส่ง)
                    const inputBaht = document.getElementById('inputBaht');
                    const inputPoints = document.getElementById('inputPoints');
                    inputBaht.disabled = false;
                    inputPoints.disabled = false;
                    
                    const formData = new FormData(cfgForm);
                    const body = new URLSearchParams(formData);
                    
                    fetch('manager-reward', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' },
                        body: body
                    }).then(res => {
                        if (res.ok || res.redirected) {
                            alert('บันทึกอัตราการแจกแต้มสำเร็จ');
                            window.location.href = 'manager-reward';
                        } else {
                            alert('บันทึกไม่สำเร็จ กรุณาลองใหม่');
                        }
                    }).catch(err => {
                        alert('เกิดข้อผิดพลาด: ' + err.message);
                    });
                });
            }
        })();

        function openModal() {
            document.getElementById('modal').style.display = 'flex';
            document.getElementById('modalTitle').innerText = 'เพิ่มรางวัลใหม่';
            document.getElementById('formId').value = '';
            document.getElementById('formName').value = '';
            document.getElementById('formPoints').value = '';
            document.getElementById('formValue').value = '';
            document.getElementById('formType').value = 'PERCENT';
        }

        function editReward(id, name, points, type, val) {
            document.getElementById('modal').style.display = 'flex';
            document.getElementById('modalTitle').innerText = 'แก้ไขรางวัล';
            document.getElementById('formId').value = id;
            document.getElementById('formName').value = name;
            document.getElementById('formPoints').value = points;
            document.getElementById('formType').value = type;
            document.getElementById('formValue').value = val;
        }

        function closeModal() {
            document.getElementById('modal').style.display = 'none';
        }
        
        window.onclick = function(e) {
            if (e.target == document.getElementById('modal')) closeModal();
        }

        function searchTable() {
            let input = document.getElementById("searchInput").value.toUpperCase();
            let tr = document.getElementById("tableBody").getElementsByTagName("tr");
            for (let i = 0; i < tr.length; i++) {
                let td = tr[i].getElementsByTagName("td")[1];
                if (td) {
                    let txt = td.textContent || td.innerText;
                    tr[i].style.display = txt.toUpperCase().indexOf(input) > -1 ? "" : "none";
                }
            }
        }
        function liveSearch() {
           
            let input = document.getElementById("liveSearchInput");
            let filter = input.value.toUpperCase();
            
            let table = document.getElementById("tableBody");
            let tr = table.getElementsByTagName("tr");

            
            for (let i = 0; i < tr.length; i++) {
            
                let tdName = tr[i].getElementsByTagName("td")[1];
                
                if (tdName) {
                    let txtValue = tdName.textContent || tdName.innerText;
                
                    if (txtValue.toUpperCase().indexOf(filter) > -1) {
                        tr[i].style.display = "";
                    } else {
                        tr[i].style.display = "none";
                    }
                }       
            }
        }
    </script>
</body>
</html>