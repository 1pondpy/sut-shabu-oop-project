<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Menu Management - SUT Shabu</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/manager.css">
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+Thai:wght@100..900&display=swap" rel="stylesheet">
    <style>
        .header-yellow { background-color: #fdd835 !important; color: #222 !important; }
        .bg-cream { background-color: #fff8e1; }
        
        #formName, #formCategory, #formImage, .btn-choose-file, #fileNameDisplay, .btn-add {
            font-family: 'Noto Sans Thai', sans-serif;
            
        }
      #formCategory {
            appearance: none;
            -webkit-appearance: none;
            -moz-appearance: none;
            padding-right: 36px;
            background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='12' height='8' viewBox='0 0 12 8'%3E%3Cpath fill='%23666' d='M1.41 0 6 4.59 10.59 0 12 1.41 6 7.41 0 1.41z'/%3E%3C/svg%3E");
            background-repeat: no-repeat;
            background-position: calc(100% - 14px) center;
            background-color: #fff;
            
        }

        
        /* Style for sort icons */
        .sort-icon {
            color: inherit;
            margin-left: 5px;
            cursor: pointer;
            opacity: 0.5;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            transition: color 0.2s, opacity 0.2s;
        }

        .sort-icon:hover {
            opacity: 1;
        }

        .sort-icon.active {
            color: #000;
            opacity: 1;
        }

        
        /* Style for filter dropdown in table header */
        .filter-form {
            display: inline-block;
            position: relative;
            margin-left: 5px;
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
        
        /* Buffet price config */
            .buffet-config {
            display: flex;
            gap: 15px;
            align-items: center;
            background: white;
            padding: 20px;
            border-radius: 15px; 
            margin-bottom: 30px;
            margin-top: -10px;
            flex-wrap: wrap;
        }

        /* label */
        .buffet-config label {
            font-weight: normal;
            color: #333;
            font-size: 14px;
        }

        /* ช่องกรอก */
        .buffet-config input[type="text"] {
            width: 50px;
            padding: 5px 10px;
            border: 1px solid #ddd;               
            border-radius: 10px;          
            background: #ffffff;
            outline: none;
            font-size: 14px;
             text-align: right;  
        }

        
        .buffet-config input[type="text"]:focus {
             border-color: #fdd835;
        }

       
        .buffet-config .save-btn {
            background: #fdd835;
            border: none;
            padding: 8px 16px;
            border-radius: 20px;
            cursor: pointer;
            font-weight: 600;
            transition: background 0.2s ease;
            color: #333;
        }

        .buffet-config .save-btn:hover {
            background: #fbc02d;
        }
        .search-box2 {
    display: flex;
    align-items: center;       gap: 10px;
}

.search-input2 {
    position: relative;
    width: 400px;
}

.search-input2 input {
    width: 100%;
    box-sizing: border-box;
    padding: 10px 45px 10px 15px;
    border-radius: 20px;
    border: 1px solid #ddd;
    outline: none;
    font-family: 'Noto Sans Thai', sans-serif;
}

.search-input2 button {
    position: absolute;
    right: 15px;
    top: 50%;
    transform: translateY(-50%);
    color: #888;
    background: none;
    border: none;
    cursor: pointer;
}
.action-bar{
    margin-top: -10px;
}
.select-wrapper {
    position: relative;
    display: inline-block;
}

.premium-select {
    border: 1px solid #ddd;
    border-radius: 20px;
    padding: 8px 36px 8px 14px;
    background: #fff;
    appearance: none;
    -webkit-appearance: none;
    cursor: pointer;
    width: 150px;
}

.premium-select:focus {
    outline: none;
}

.select-icon {
    position: absolute;
    right: 14px;
    top: 50%;
    transform: translateY(-50%);
    pointer-events: none;
    font-size: 12px;
    color: #555;
}
        .btn-grey { background-color: #e0e0e0; color: #333; border: none; padding: 8px 15px; border-radius: 20px; cursor: pointer; font-weight: bold; }
        .btn-yellow-sm { background-color: #fdd835; color: #333; border: none; padding: 8px 20px; border-radius: 20px; cursor: pointer; font-weight: bold; }

        /* Premium checkbox style */
        #formIsPremium {
            width: 15px;
            height: 15px;
            cursor: pointer;
            accent-color: #fdd835;
        }

    </style>
</head>
<body>

    <nav class="sidebar">
        <div class="logo">
             <img src="${pageContext.request.contextPath}/images/logo.png" alt="SUT Shabu" style= "width: 100px; margin-top: 10px;">
        </div>
        
        <a href="${pageContext.request.contextPath}/dashboard" class="nav-item"><i class="fas fa-chart-line"></i> Dashboard</a>
        
        <a href="#" class="nav-item active"><i class="fas fa-clipboard-list"></i> Menu Management</a>
        
        <a href="${pageContext.request.contextPath}/manager-staff" class="nav-item"><i class="fas fa-users-cog"></i> Staff Management</a>
        
        <a href="${pageContext.request.contextPath}/manager-member" class="nav-item"><i class="fas fa-users"></i> Member Management</a>
        
        <a href="${pageContext.request.contextPath}/manager-reward" class="nav-item"><i class="fas fa-gamepad"></i>Reward & Game Management</a>
        <a href="${pageContext.request.contextPath}/logout" class="nav-item logout-btn" onclick="return confirm('ยืนยันออกจากระบบ?');"><i class="fas fa-sign-out-alt"></i> ออกจากระบบ</a>
    </nav>

    <main class="main-content">
        <div class="header">
            <h2>Menu Management</h2>
        </div>
        

        <div class="content-body">
            <div class="buffet-config">
                <div style="font-size: 18px; font-weight: bold; display: flex; align-items: center; gap: 10px; width: 100%;">
                    <i class="fas fa-baht-sign"></i> ราคาแพ็กเกจชาบู
                </div>
                <form id="buffetConfigForm" action="${pageContext.request.contextPath}/manager-buffet-config" method="post" style="display: flex; align-items: center; gap: 15px; flex-wrap: wrap; width: 100%;">
                    <span>Standard</span>
                    <input type="text" id="standardPriceInput" name="standardPrice" value="${standardPrice}" class="config-input" style="width: 80px; text-align: center; padding: 8px; border: 1px solid #ddd; border-radius: 8px; font-size: 16px;" disabled autocomplete="off">
                    <span>บาท</span>
                    
                    <span>Premium</span>
                    <input type="text" id="premiumPriceInput" name="premiumPrice" value="${premiumPrice}" class="config-input" style="width: 80px; text-align: center; padding: 8px; border: 1px solid #ddd; border-radius: 8px; font-size: 16px;" disabled autocomplete="off">
                    <span>บาท</span>

                    <button type="button" id="buffetEditBtn" class="btn-grey" onclick="enableConfigEdit(); return false;">แก้ไข</button>
                    <button type="submit" id="buffetSaveBtn" class="btn-yellow-sm" style="display:none;">บันทึก</button>
                </form>
            </div>

            <div class="action-bar">
                
                    <form action="${pageContext.request.contextPath}/manager-menu"
                    method="get"
                    class="search-box2">

                    <input type="hidden" name="sort" value="${currentSort}">
                    <input type="hidden" name="categoryFilter" value="${currentCategory}">

                    <div class="search-input2">
                        <input type="text" id="liveSearchInput" name="search"
                            placeholder="ค้นหาเมนู..."
                            value="${currentSearch}"
                            onkeyup="liveSearch()"
                            autocomplete="off">

                        <button type="submit">
                            <i class="fas fa-search"></i>
                        </button>
                    </div>

                    <div class="select-wrapper">
                        <select name="premiumFilter" class="premium-select" onchange="this.form.submit()">
                            <option value="all" ${currentPremiumFilter == 'all' || empty currentPremiumFilter ? 'selected' : ''}>ทั้งหมด</option>
                            <option value="only" ${currentPremiumFilter == 'only' ? 'selected' : ''}>เฉพาะ Premium</option>
                            <option value="non" ${currentPremiumFilter == 'non' ? 'selected' : ''}>เฉพาะ Standard</option>
                        </select>
                        <i class="fa-solid fa-chevron-down select-icon"></i>
                    </div>


                </form>

                  

                        
                <button class="btn-add" onclick="openModal()">+ เพิ่มเมนู</button>
            </div>

            <table class="menu-table">
                <thead>
                    <tr>
                        <th width="10%">รูป</th>
                        <th width="30%">
                            ชื่อเมนู
                            <a href="${pageContext.request.contextPath}/manager-menu?sort=name_asc&search=${currentSearch}&categoryFilter=${currentCategory}" class="sort-icon ${currentSort == 'name_asc' ? 'active' : ''}"><i class="fas fa-sort-alpha-down"></i></a>
                            <a href="${pageContext.request.contextPath}/manager-menu?sort=name_desc&search=${currentSearch}&categoryFilter=${currentCategory}" class="sort-icon ${currentSort == 'name_desc' ? 'active' : ''}"><i class="fas fa-sort-alpha-up"></i></a>
                        </th>
                        
                        <th width="25%">
                            หมวดหมู่
                            <form action="${pageContext.request.contextPath}/manager-menu" method="get" class="filter-form">
                                <input type="hidden" name="search" value="${currentSearch}">
                                <input type="hidden" name="sort" value="${currentSort}">
                                
                                <i class="fas fa-filter filter-icon" title="กรองหมวดหมู่"></i>
                                <select name="categoryFilter" onchange="this.form.submit()">
                                    <option value="" ${empty currentCategory ? 'selected' : ''}>ทั้งหมด</option>
                                    <option value="เนื้อสัตว์" ${currentCategory == 'เนื้อสัตว์' ? 'selected' : ''}>เนื้อสัตว์</option>
                                    <option value="ผัก" ${currentCategory == 'ผัก' ? 'selected' : ''}>ผัก</option>
                                    <option value="อาหารแปรรูป" ${currentCategory == 'อาหารแปรรูป' ? 'selected' : ''}>อาหารแปรรูป</option>
                                    <option value="ของหวาน" ${currentCategory == 'ของหวาน' ? 'selected' : ''}>ของหวาน</option>
                                    <option value="ของทานเล่น" ${currentCategory == 'ของทานเล่น' ? 'selected' : ''}>ของทานเล่น</option>
                                    <option value="ผลไม้" ${currentCategory == 'ผลไม้' ? 'selected' : ''}>ผลไม้</option>
                                </select>
                            </form>
                        </th>
                        
                        <th width="15%">
                            สถานะ
                        </th>
                        <th width="20%">จัดการ</th>
                    </tr>
                </thead>
                <tbody id="menuTableBody">
                    <c:forEach var="item" items="${menuItems}">
                        <tr class="${!item.active ? 'menu-row-inactive' : ''}">
                            <td>
                                <img src="${pageContext.request.contextPath}/images/${item.imageUrl}" class="menu-img" 
                                     onerror="this.src='https://placehold.co/100x100?text=No+Img'">
                            </td>
                            <td>
                                ${item.name}
                                <c:if test="${item.premium}">
                                    <span class="badge" style="background:#fdd835; margin-left:8px; padding: 8px; border-radius: 20px; font-size: 12px; font-weight: 600;">Premium</span>
                                </c:if>
                            </td>
                            <td><span class="badge">${item.category}</span></td>
                            <td>
                                <label class="switch">
                                    <input type="checkbox" ${item.active ? 'checked' : ''} 
                                           onchange="window.location.href='${pageContext.request.contextPath}/manager-menu?action=toggle&id=${item.id}'">
                                    <span class="slider"></span>
                                </label>
                            </td>
                            <td>
                                <button class="action-btn btn-edit" onclick="editMenu('${item.id}', '${item.name}', '${item.category}', '${item.imageUrl}', '${item.premium}')">
                                    <i class="far fa-edit"></i>
                                </button>
                                <a href="${pageContext.request.contextPath}/manager-menu?action=delete&id=${item.id}" class="action-btn btn-delete" onclick="return confirm('ยืนยันการลบเมนูนี้?');">
                                    <i class="far fa-trash-alt"></i>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty menuItems}">
                        <tr><td colspan="5" style="text-align:center; padding:20px;">ไม่พบรายการเมนู</td></tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </main>

    <div id="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h3 id="modalTitle">เพิ่มเมนูใหม่</h3>
            </div>
            
            <form action="${pageContext.request.contextPath}/manager-menu" method="post" enctype="multipart/form-data">
                <input type="hidden" name="id" id="formId">
                
                <div class="modal-body">
                    <div class="form-group">
                        <label>ชื่อเมนู</label>
                        <input type="text" name="name" id="formName" placeholder="เช่น เนื้อสไลซ์" required>
                    </div>
                    <div class="form-group">
                        <label>หมวดหมู่</label>
                        <select name="category" id="formCategory">
                            <option value="เนื้อสัตว์">เนื้อสัตว์</option>
                            <option value="ผัก">ผัก</option>
                            <option value="อาหารแปรรูป">อาหารแปรรูป</option>
                            <option value="ของทานเล่น">ของทานเล่น</option>
                            <option value="ของหวาน">ของหวาน</option>
                            <option value="ผลไม้">ผลไม้</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>อัปโหลดรูปภาพเมนู</label>
                        <div class="file-upload-container">
                            <label for="formImage" class="btn-choose-file">Choose File</label>
                            <span id="fileNameDisplay">No file chosen</span>
                            <input type="file" name="imageFile" id="formImage" accept="image/*" onchange="updateFileName(this)" style="display: none;">
                        </div>
                        <input type="hidden" name="oldImageUrl" id="formOldImage">
                    </div>
                    <div class="form-group">
                        <label>Premium Menu</label>
                        <div>
                            <label><input type="checkbox" name="isPremium" id="formIsPremium"><span style="font-weight: 500;"> เป็นเมนูพรีเมียม (เฉพาะแพ็กเกจ Premium)</span></label>
                        </div>
                    </div>
                </div>
                
                <div class="modal-footer">
                    <button type="button" class="btn-cancel" onclick="closeModal()">ยกเลิก</button>
                    <button type="submit" class="btn-save">เพิ่มเมนู</button>
                </div>
            </form>
        </div>
    </div>

    <script>
        
        document.querySelectorAll('.sort-icon').forEach(icon => {
            icon.addEventListener('click', function () {
                document.querySelectorAll('.sort-icon')
                    .forEach(i => i.classList.remove('active'));

                this.classList.add('active');
            });
        });
        function liveSearch() {
            let input = document.getElementById("liveSearchInput");
            let filter = input.value.toUpperCase();
            let table = document.getElementById("menuTableBody");
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

        function updateFileName(input) {
            const fileNameDisplay = document.getElementById('fileNameDisplay');
            if (input.files && input.files.length > 0) {
                fileNameDisplay.textContent = input.files[0].name;
            } else {
                fileNameDisplay.textContent = 'No file chosen';
            }
        }

        function openModal() {
            document.getElementById('modal').style.display = 'flex';
            document.getElementById('modalTitle').innerText = 'เพิ่มเมนูใหม่';
            document.getElementById('formId').value = '';
            document.getElementById('formName').value = '';
            document.getElementById('formOldImage').value = '';
            document.getElementById('fileNameDisplay').textContent = 'No file chosen';
            document.querySelector('.btn-save').innerText = 'เพิ่มเมนู';
        }

        function editMenu(id, name, category, img, premium) {
            document.getElementById('modal').style.display = 'flex';
            document.getElementById('modalTitle').innerText = 'แก้ไขเมนู';
            document.getElementById('formId').value = id;
            document.getElementById('formName').value = name;
            document.getElementById('formCategory').value = category;
            document.getElementById('formOldImage').value = img;
            document.getElementById('fileNameDisplay').textContent = img ? img : 'No file chosen';
            document.querySelector('.btn-save').innerText = 'บันทึก';
            document.getElementById('formIsPremium').checked = (premium === 'true' || premium === 'True' || premium === 'on');
        }

        function closeModal() {
            document.getElementById('modal').style.display = 'none';
        }
        
        window.onclick = function(e) {
            if (e.target == document.getElementById('modal')) closeModal();
        }

        function enableConfigEdit() {
            const stdInput = document.getElementById('standardPriceInput');
            const premInput = document.getElementById('premiumPriceInput');
            const saveBtn = document.getElementById('buffetSaveBtn');
            const editBtn = document.getElementById('buffetEditBtn');
            if (stdInput) stdInput.disabled = false;
            if (premInput) premInput.disabled = false;
            if (saveBtn) saveBtn.style.display = 'inline-block';
            if (editBtn) editBtn.style.display = 'none';
            if (stdInput) stdInput.focus();
        }

       
        (function() {
            const cfgForm = document.getElementById('buffetConfigForm');
            if (!cfgForm) return;
            const editBtn = document.getElementById('buffetEditBtn');
            const saveBtn = document.getElementById('buffetSaveBtn');
            const stdInput = document.getElementById('standardPriceInput');
            const premInput = document.getElementById('premiumPriceInput');

            function enterEditMode() {
                if (stdInput) stdInput.disabled = false;
                if (premInput) premInput.disabled = false;
                if (saveBtn) saveBtn.style.display = '';
                if (editBtn) editBtn.style.display = 'none';
                if (stdInput) stdInput.focus();
            }

            if (editBtn) editBtn.addEventListener('click', enterEditMode);
            window.enableConfigEdit = enterEditMode;
            cfgForm.addEventListener('submit', function (ev) {
                ev.preventDefault();
                const url = cfgForm.action;
                const body = new URLSearchParams(new FormData(cfgForm));
                fetch(url, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' },
                    body: body
                }).then(res => {
                    if (res.ok || res.redirected) {
                        alert('บันทึกราคาสำเร็จ');
                        window.location.href = '${pageContext.request.contextPath}/manager-menu';
                    } else {
                        alert('บันทึกราคาไม่สำเร็จ');
                    }
                }).catch(err => { console.error(err); alert('ไม่สามารถติดต่อเซิร์ฟเวอร์ได้'); });
            });
        })();
    </script>
</body>
</html>