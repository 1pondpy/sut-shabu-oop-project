<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
            <!DOCTYPE html>
            <html xmlns:th="http://www.thymeleaf.org">

            <head>
                <meta charset="UTF-8">
                <title>Table Management</title>
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
                <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
                <script src="https://cdnjs.cloudflare.com/ajax/libs/qrcodejs/1.0.0/qrcode.min.js"></script>
                <style>
                    .soup-btn.selected {
                        border: 2px solid #fdd835;
                        background-color: #fff9c4;
                        font-weight: bold;
                        color: #333;
                    }

                    .time-remaining {
                        font-size: 24px;
                        color: #d32f2f;
                        font-weight: bold;
                    }

                    .package-options {
                        display: flex;
                        gap: 12px;
                        margin-top: 8px;
                    }

                    .package-card input[type="radio"] {
                        display: none;
                    }

                    .package-card {
                        flex: 1;
                        padding: 14px;
                        border-radius: 10px;
                        cursor: pointer;
                        text-align: center;
                        border: 2px solid transparent;
                        transition: all 0.2s ease;
                    }

                    .package-card.standard {
                        background: #f5f5f5;
                        border-color: #ccc;
                        color: #333;
                    }

                    .package-card.premium {
                        background: #fff8e1;
                        border-color: #fdd835;
                        color: #333;
                    }

                    .package-card input[type="radio"]:checked+.package-title,
                    .package-card input[type="radio"]:checked~.package-price {
                        font-weight: bold;
                    }

                    .package-card input[type="radio"]:checked {
                        display: none;
                    }

                    .package-card:has(input[type="radio"]:checked) {
                        box-shadow: 0 6px 14px rgba(255, 255, 255, 0.12);
                        border-color: #555;
                    }

                    .package-card {
                        outline: none;
                    }

                    .package-card:focus,
                    .package-card:focus-visible {
                        outline: none;
                    }

                    .package-card input[type="radio"] {
                        outline: none;
                        box-shadow: none;
                    }

                    .table-card.expired {
                        background: #474747 !important;
                        color: #fff !important;
                        position: relative;
                    }

                    .table-card.expired h2,
                    .table-card.expired span,
                    .table-card.expired small {
                        color: #fff !important;
                    }

                    .table-card .expired-icon {
                        position: absolute;
                        top: 8px;
                        right: 8px;
                        background: #fdd835;
                        color: #333;
                        padding: 6px;
                        border-radius: 50%;
                        font-size: 12px;
                    }

                    .left-noti-item {
                        background: #fff3e0;
                        color: #333;
                        padding: 10px;
                        border-radius: 8px;
                        margin-bottom: 8px;
                    }

                    .left-noti-item .ack-btn {
                        background: #fdd835;
                        border: none;
                        padding: 6px 8px;
                        border-radius: 6px;
                        cursor: pointer;
                        margin-top: 6px;
                    }

                    .logout:hover {
                        background-color: #fff;
                        color: rgb(0, 0, 0);
                    }

                    .logout {
                        display: inline-flex;
                        align-items: center;
                        gap: 8px;
                        padding: 8px 16px;
                        border-radius: 20px;
                        background: transparent;
                        color: white;
                        border: 1px solid rgba(255, 255, 255, 0.35);
                        cursor: pointer;
                        text-decoration: none;
                        font-weight: 600;
                        justify-self: end;
                    }
                </style>
            </head>

            <body>

                <div class="container">
                    <div class="sidebar-left"
                        style="display:flex; flex-direction:column; height:100vh; padding:20px; box-sizing:border-box;">
                        <div class="brand"><img src="${pageContext.request.contextPath}/images/logo.png" width="100">
                        </div>
                        <p style="color:#ddd; margin-bottom:10px;">การแจ้งเตือน</p>

                        <div id="noti-container">
                            <div style="color:#666; text-align:center; font-size:12px; margin-top:20px;">กำลังโหลด...
                            </div>
                        </div>

                        <div style="width:100%; display:flex; justify-content:center; margin-top:auto;">
                            <form action="${pageContext.request.contextPath}/logout" method="post"
                                style="width:100%; display:flex; justify-content:center;">
                                <button type="submit" class="logout" onclick="return confirm('ยืนยันออกจากระบบ?');"><i
                                        class="fas fa-sign-out-alt"></i>ออกจากระบบ</button>
                            </form>
                        </div>

                    </div>

                    <div class="main-content">
                        <div class="top-bar">
                            Table Management
                        </div>


                        <h3>โซน A</h3>
                        <div class="table-grid">
                            <c:forEach var="table" items="${zoneA}">
                                <div class="table-card ${table.status == 'FREE' ? 'green' : 'red'}"
                                    style="cursor:pointer;" data-number="${table.number}" data-status="${table.status}"
                                    data-pax="${table.pax}" data-start-time="${table.startTime}"
                                    data-remaining-seconds="${table.remainingSeconds}" data-soup="${table.soup}"
                                    data-alert-msg="${table.alertMsg}" data-phone="${table.phone}"
                                    data-package-type="${table.packageType}" data-total-price="${table.totalPrice}"
                                    data-discount="${table.discount}" data-net-price="${table.netPrice}"
                                    onclick="selectTableFromElement(this)">
                                    <h2>${table.number}</h2>
                                    <c:if test="${table.status == 'FREE'}">ว่าง</c:if>
                                    <c:if test="${table.status == 'BUSY'}">
                                        <span>${table.pax} ท่าน</span>
                                        <small>${table.alertMsg}</small>
                                    </c:if>
                                </div>
                            </c:forEach>
                        </div>

                        <h3 style="margin-top:20px;">โซน B</h3>
                        <div class="table-grid">
                            <c:forEach var="table" items="${zoneB}">
                                <div class="table-card ${table.status == 'FREE' ? 'green' : 'red'}"
                                    style="cursor:pointer;" data-number="${table.number}" data-status="${table.status}"
                                    data-pax="${table.pax}" data-start-time="${table.startTime}"
                                    data-remaining-seconds="${table.remainingSeconds}" data-soup="${table.soup}"
                                    data-alert-msg="${table.alertMsg}" data-phone="${table.phone}"
                                    data-package-type="${table.packageType}" data-total-price="${table.totalPrice}"
                                    data-discount="${table.discount}" data-net-price="${table.netPrice}"
                                    onclick="selectTableFromElement(this)">
                                    <h2>${table.number}</h2>
                                    <c:if test="${table.status == 'FREE'}">ว่าง</c:if>
                                    <c:if test="${table.status == 'BUSY'}">
                                        <span>${table.pax} ท่าน</span>
                                        <small>${table.alertMsg}</small>
                                    </c:if>
                                </div>
                            </c:forEach>
                        </div>
                    </div>

                    <div class="sidebar-right">
                        <div class="right-header">
                            <h2 id="headTableNo" style="font-size: 20px;">กรุณาเลือกโต๊ะ</h2>
                        </div>

                        <div class="right-body">
                            <div id="view-empty" style="text-align: center; margin-top: 50px; color:#888;">
                                <i class="fa-solid fa-hand-pointer" style="font-size: 40px;"></i>
                                <p>เลือกโต๊ะเพื่อทำรายการ</p>
                            </div>

                            <div id="view-open" style="display: none;">
                                <div class="form-group" style=" margin-top: -10px;">
                                    <label>เบอร์โทร (สมาชิก)</label>
                                    <div style="display: flex; gap: 5px;">
                                        <input type="text" id="inputPhone" class="form-input" placeholder="กรอกเบอร์โทร"
                                            maxlength="10" inputmode="numeric"
                                            oninput="this.value = this.value.replace(/[^0-9]/g, '').slice(0, 10)">
                                        <button onclick="searchMember()"
                                            style="background:#333; color:white; border:none; border-radius:5px; width:40px; cursor:pointer; height: 40px;"><i
                                                class="fa-solid fa-search"></i></button>
                                    </div>
                                    <small id="searchResult" style="color: gray; ">กดค้นหาเพื่อตรวจสอบสมาชิก</small>
                                </div>

                                <div class="form-group" style="margin-top: 10px;">
                                    <label>ชื่อลูกค้า</label>
                                    <input type="text" id="inputName" class="form-input" placeholder="ลูกค้าทั่วไป">
                                </div>

                                <div class="form-group">
                                    <label>จำนวนลูกค้า</label>
                                    <div class="counter-box">
                                        <button class="btn-count" onclick="adjPax(-1)">-</button>
                                        <span id="pax-val"
                                            style="font-size:20px; font-weight:bold; width:30px; text-align:center;">1</span>
                                        <button class="btn-count" onclick="adjPax(1)">+</button>
                                    </div>
                                </div>

                                <div class="form-group" style="margin-top: 20px; ">
                                    <label>เลือกน้ำซุป (สูงสุด 2)</label>
                                    <div class="soup-grid" style="margin-top: 20px; ">
                                        <div class="soup-btn" onclick="toggleSoup(this, 'น้ำใส')">น้ำใส</div>
                                        <div class="soup-btn" onclick="toggleSoup(this, 'น้ำดำ')">น้ำดำ</div>
                                        <div class="soup-btn" onclick="toggleSoup(this, 'ต้มยำ')">ต้มยำ</div>
                                        <div class="soup-btn" onclick="toggleSoup(this, 'หมาล่า')">หมาล่า</div>
                                    </div>
                                </div>

                                <div class="form-group" style="margin-top: 10px;">
                                    <label>แพ็กเกจ</label>

                                    <div class="package-options">
                                        <label class="package-card standard">
                                            <input type="radio" name="packageType" value="STANDARD" checked>
                                            <div class="package-title">Standard</div>
                                            <div class="package-price">฿${standardPrice}</div>
                                        </label>

                                        <label class="package-card premium">
                                            <input type="radio" name="packageType" value="PREMIUM">
                                            <div class="package-title">Premium</div>
                                            <div class="package-price">฿${premiumPrice}</div>
                                        </label>
                                    </div>
                                </div>


                                <div class="right-footer-action">
                                    <button class="btn-open" onclick="submitOpenTable()">เปิดโต๊ะ</button>
                                    <button class="btn-cancel" onclick="resetView()">ยกเลิก</button>
                                </div>
                            </div>

                            <div id="view-busy" style="display: none; height: 100%; flex-direction: column;">

                                <div
                                    style="display: flex; justify-content: space-between; border-bottom: 1px solid #eee; padding-bottom: 10px; align-items: center; align-content: center;">
                                    <span><i class="fa-regular fa-clock"></i> เริ่ม <span
                                            id="txtStartTime">12:00</span></span>
                                    <span>เหลือ <span id="txtTimeLeft" class="time-remaining"></span></span>
                                </div>

                                <div style="text-align: center; margin-bottom: 20px;">
                                    <div id="txtCustomerName"
                                        style="font-size: 20px; font-weight: 600; margin-top: 30px; margin-bottom: 8px;">
                                        คุณลูกค้า</div>
                                    <h2 style="color:#555; font-size: 20px;"><span id="txtPax">4</span> ท่าน</h2>
                                    <div id="txtPackage" style="color:#777; font-size:14px; margin-top:6px;">แพ็กเกจ:
                                        Standard (฿0)</div>
                                </div>

                                <div class="form-group">
                                    <label>น้ำซุปที่เลือก</label>
                                    <div id="txtSoupList"
                                        style="display: flex; gap: 10px; flex-wrap: wrap; margin-bottom: 10px;"></div>
                                </div>

                                <div class="bill-footer">
                                    <div id="priceContainer" style="margin-bottom: 15px;"></div>

                                    <button class="btn-member" onclick="openTableMemberModal()">
                                        แลกแต้ม</button>
                                    <button class="btn-checkout" onclick="doCheckout()">ชำระเงิน</button>
                                    <button class="btn-cancel" onclick="resetView()"
                                        style="margin-top:10px;">ปิดหน้าต่าง</button>
                                </div>
                            </div>

                        </div>
                    </div>
                </div>

                <script>
                    const CONTEXT_PATH = '${pageContext.request.contextPath}';
                    const CONFIG_STANDARD_PRICE = ${ standardPrice };
                    const CONFIG_PREMIUM_PRICE = ${ premiumPrice };
                    const TABLE_DURATION_MS = 2 * 60 * 60 * 1000; // 2 hours per table
                    let currentTable = null;
                    let selectedSoups = [];
                    let currentPax = 1;
                    let activeTimer = null;
                    let currentTablePoints = 0;
                    let pendingRedeemData = null;
                    function refreshTableStatuses() {
                        const now = Date.now();
                        document.querySelectorAll('.table-card').forEach(card => {
                            const ds = card.dataset || {};

                            if (ds.status !== 'BUSY') {
                                card.classList.remove('expired');
                                return;
                            }

                            let remainingMs = Number.POSITIVE_INFINITY;

                            if (ds.startTime) {
                                const started = new Date(ds.startTime);
                                if (!isNaN(started)) {
                                    remainingMs = TABLE_DURATION_MS - (now - started.getTime());
                                }
                            }

                            if (!isFinite(remainingMs) && ds.remainingSeconds) {
                                const sec = parseInt(ds.remainingSeconds, 10);
                                if (!isNaN(sec)) {
                                    remainingMs = sec * 1000;
                                }
                            }

                            if (isFinite(remainingMs)) {
                                card.dataset.remainingSeconds = Math.max(0, Math.floor(remainingMs / 1000));
                            } else {
                                delete card.dataset.remainingSeconds;
                            }

                            if (remainingMs <= 0) {
                                card.classList.add('expired');
                            } else {
                                card.classList.remove('expired');
                            }
                        });
                    }

                    document.addEventListener('DOMContentLoaded', () => {
                        refreshTableStatuses();
                        setInterval(refreshTableStatuses, 5000);
                    });


                    function loadNotifications() {
                        fetch(CONTEXT_PATH + '/api/call-staff/list')
                            .then(res => res.json())
                            .then(data => {
                                let container = document.getElementById('noti-container');
                                container.innerHTML = "";
                                let html = "";
                                if (!data || data.length === 0) {
                                    container.innerHTML = '<div style="color:#666; text-align:center; font-size:12px; margin-top:20px;">ไม่มีการแจ้งเตือนใหม่</div>';
                                } else {
                                    data.forEach(noti => {
                                        let timeStr = new Date(noti.requestTime).toLocaleTimeString('th-TH', { hour: '2-digit', minute: '2-digit' });
                                        html += '<div class="noti-card" id="noti-' + noti.id + '">';
                                        html += '<div class="noti-header" style="display: flex; justify-content: space-between; align-items: center;">';
                                        html += '<b>' + noti.tableNo + '</b>';
                                        html += '<span style="font-size:12px; color:#aaa;">' + timeStr + '</span>';
                                        html += '</div>';
                                        html += '<p>' + noti.requestType + '</p>';
                                        html += '<button class="btn-ack" onclick="ackRequest(' + noti.id + ')">รับทราบ</button>';
                                        html += '</div>';
                                    });
                                    container.innerHTML = html;
                                }
                            })
                            .catch(err => console.error("Error fetching noti:", err));
                    }

                    loadNotifications();
                    setInterval(loadNotifications, 3000);



                    function ackRequest(id) {
                        fetch(CONTEXT_PATH + '/api/call-staff/ack', {
                            method: 'POST',
                            headers: { 'Content-Type': 'application/json' },
                            body: JSON.stringify({ id: id })
                        }).then(res => { if (res.ok) { let el = document.getElementById('noti-' + id); if (el) el.remove(); } });
                    }

                    function selectTable(table) {
                        console.log("ข้อมูลโต๊ะ:", table);
                        currentTable = table;
                        document.getElementById('headTableNo').innerText = "โต๊ะ " + table.number;

                        document.getElementById('view-empty').style.display = 'none';
                        document.getElementById('view-open').style.display = 'none';
                        document.getElementById('view-busy').style.display = 'none';

                        if (table.status === 'FREE') {
                            resetForm();
                            document.getElementById('view-open').style.display = 'block';
                        } else {
                            showBusyDetails(table);
                            document.getElementById('view-busy').style.display = 'flex';
                        }
                    }

                    function selectTableFromElement(el) {
                        const ds = el.dataset;
                        const table = {
                            number: ds.number,
                            status: ds.status,
                            pax: parseInt(ds.pax || '0'),
                            startTime: ds.startTime || ds.startTime,
                            soup: ds.soup || '',
                            alertMsg: ds.alertMsg || '',
                            phone: ds.phone || '',
                            totalPrice: parseFloat(ds.totalPrice || 0),
                            discount: parseFloat(ds.discount || 0),
                            netPrice: parseFloat(ds.netPrice || 0)
                            , packageType: ds.packageType || 'STANDARD'
                        };
                        selectTable(table);
                    }

                    function showBusyDetails(table) {
                        document.getElementById('txtCustomerName').innerText = table.alertMsg;
                        document.getElementById('txtPax').innerText = table.pax;
                        try {
                            const pkgEl = document.getElementById('txtPackage');
                            const pkg = table.packageType ? table.packageType.toUpperCase() : 'STANDARD';
                            const unit = (pkg === 'PREMIUM') ? CONFIG_PREMIUM_PRICE : CONFIG_STANDARD_PRICE;
                            if (pkgEl) pkgEl.innerText = 'แพ็กเกจ: ' + (pkg === 'PREMIUM' ? 'Premium' : 'Standard') + ' (฿' + unit.toLocaleString() + ')';
                        } catch (e) { }
                        updatePriceUI(table);
                        let soupHtml = '';
                        let rawSoup = table.soup || '';
                        let soups = rawSoup.split(/[,，]/).map(s => s.trim()).filter(s => s.length > 0);
                        soups.forEach(s => { soupHtml += '<span class="soup-badge">' + s + '</span>'; });
                        const soupContainer = document.getElementById('txtSoupList');
                        if (soupContainer) {
                            soupContainer.innerHTML = soupHtml.length > 0 ? soupHtml : '<span style="color:#666">ยังไม่ได้เลือก</span>';
                        }
                        if (activeTimer) clearInterval(activeTimer);
                        const updateTime = () => {
                            if (table.startTime) {
                                let start = new Date(table.startTime);
                                let now = new Date();
                                if (isNaN(start.getTime())) {
                                    document.getElementById('txtStartTime').innerText = "00:00";
                                    document.getElementById('txtTimeLeft').innerText = "00:00:00";
                                } else {
                                    let startHour = start.getHours().toString().padStart(2, '0');
                                    let startMin = start.getMinutes().toString().padStart(2, '0');
                                    document.getElementById('txtStartTime').innerText = startHour + ':' + startMin;

                                    let limitMs = 2 * 60 * 60 * 1000;
                                    let diffMs = now - start;
                                    let remainingMs = limitMs - diffMs;
                                    if (remainingMs < 0) remainingMs = 0;

                                    let h = Math.floor(remainingMs / (1000 * 60 * 60));
                                    let m = Math.floor((remainingMs % (1000 * 60 * 60)) / (1000 * 60));
                                    let s = Math.floor((remainingMs % (1000 * 60)) / 1000);

                                    let timeDisplay = document.getElementById('txtTimeLeft');
                                    timeDisplay.innerText = h.toString().padStart(2, '0') + ':' + m.toString().padStart(2, '0') + ':' + s.toString().padStart(2, '0');
                                    timeDisplay.style.color = (h === 0 && m < 15) ? "red" : "#d32f2f";
                                }
                            } else {
                                document.getElementById('txtStartTime').innerText = '00:00';
                                document.getElementById('txtTimeLeft').innerText = '00:00:00';
                            }
                        };
                        updateTime();
                        activeTimer = setInterval(updateTime, 1000);
                    }

                    function updatePriceUI(table) {
                        let basePrice;
                        if (table.totalPrice > 0) {
                            basePrice = table.totalPrice;
                        } else {
                            const pkg = table.packageType ? table.packageType : 'STANDARD';
                            const unit = (pkg === 'PREMIUM') ? CONFIG_PREMIUM_PRICE : CONFIG_STANDARD_PRICE;
                            basePrice = table.pax * unit;
                        }
                        let discount = table.discount || 0;
                        let netPrice = table.netPrice > 0 ? table.netPrice : basePrice;

                        let html = '';
                        html += '<div style="display:flex; justify-content:space-between; font-size:16px; color:#666;">';
                        html += '<span>ยอดรวม</span><span>฿ ' + basePrice.toLocaleString("en-US", { minimumFractionDigits: 2 }) + '</span>';
                        html += '</div>';

                        if (discount > 0) {
                            html += '<div style="display:flex; justify-content:space-between; font-size:16px; color:#4caf50;">';
                            html += '<span>ส่วนลด</span><span>- ฿ ' + discount.toLocaleString("en-US", { minimumFractionDigits: 2 }) + '</span>';
                            html += '</div>';
                        }

                        html += '<div style="display:flex; justify-content:space-between; font-size:22px; font-weight:bold; margin-top:5px; border-top:1px dashed #ccc; padding-top:5px;">';
                        html += '<span>สุทธิ</span><span style="color:#b71c1c;">฿ ' + netPrice.toLocaleString("en-US", { minimumFractionDigits: 2 }) + '</span>';
                        html += '</div>';

                        const container = document.getElementById('priceContainer');
                        if (container) container.innerHTML = html;
                    }
                    function resetView() { location.reload(); }
                    function resetForm() {
                        document.getElementById('inputPhone').value = '';
                        document.getElementById('inputName').value = '';
                        document.getElementById('searchResult').innerText = 'กดค้นหาเพื่อตรวจสอบสมาชิก';
                        currentPax = 1;
                        document.getElementById('pax-val').innerText = 1;
                        selectedSoups = [];
                        document.querySelectorAll('.soup-btn').forEach(b => b.classList.remove('selected'));
                    }
                    function adjPax(n) {
                        currentPax += n;
                        if (currentPax < 1) currentPax = 1;
                        document.getElementById('pax-val').innerText = currentPax;
                    }
                    function toggleSoup(btn, soupName) {
                        if (selectedSoups.includes(soupName)) {
                            selectedSoups = selectedSoups.filter(s => s !== soupName);
                            btn.classList.remove('selected');
                        } else {
                            if (selectedSoups.length < 2) {
                                selectedSoups.push(soupName);
                                btn.classList.add('selected');
                            } else {
                                alert("เลือกได้สูงสุด 2 น้ำซุป");
                            }
                        }
                    }
                    function searchMember() {
                        let phone = document.getElementById('inputPhone').value;
                        fetch(CONTEXT_PATH + '/api/members/search?phone=' + encodeURIComponent(phone))
                            .then(res => { if (res.ok) return res.json(); throw new Error('Not found'); })
                            .then(data => {
                                document.getElementById('searchResult').innerHTML = '<span style="color:green">พบสมาชิก: ' + data.name + '</span>';
                                document.getElementById('inputName').value = data.name;
                            })
                            .catch(err => {
                                document.getElementById('searchResult').innerHTML = '<span style="color:orange">ไม่พบสมาชิก (ลูกค้าทั่วไป)</span>';
                                document.getElementById('inputName').value = "ลูกค้าทั่วไป";
                            });
                    }
                    function submitOpenTable() {
                        let name = document.getElementById('inputName').value;
                        let phone = document.getElementById('inputPhone').value;

                        if (selectedSoups.length === 0) {
                            alert("กรุณาเลือกน้ำซุปอย่างน้อย 1 อย่าง");
                            return;
                        }

                        let packageType = (document.querySelector('input[name="packageType"]:checked') || { value: 'STANDARD' }).value;

                        let payload = {
                            tableNo: currentTable.number,
                            customerName: name || "ลูกค้าทั่วไป",
                            customerPhone: phone,
                            pax: currentPax,
                            soup: selectedSoups.join(", "),
                            packageType: packageType
                        };

                        fetch(CONTEXT_PATH + '/api/orders/open-table', {
                            method: 'POST',
                            headers: { 'Content-Type': 'application/json' },
                            body: JSON.stringify(payload)
                        }).then(res => res.json()).then(data => {
                            if (data && data.ok) {
                                currentTable.status = 'BUSY';
                                currentTable.pax = payload.pax;
                                currentTable.soup = payload.soup;
                                currentTable.alertMsg = payload.customerName;
                                currentTable.phone = payload.customerPhone;
                                currentTable.startTime = new Date().toISOString();

                                const unitPrice = data.unitPrice ? Number(data.unitPrice) : (payload.packageType === 'PREMIUM' ? CONFIG_PREMIUM_PRICE : CONFIG_STANDARD_PRICE);
                                const total = data.totalAmount ? Number(data.totalAmount) : (payload.pax * unitPrice);
                                currentTable.totalPrice = total;
                                currentTable.discount = 0;
                                currentTable.netPrice = total;

                                let card = document.querySelector('.table-card[data-number="' + currentTable.number + '"]');
                                if (card) {
                                    card.classList.remove('green');
                                    card.classList.add('red');

                                    card.innerHTML = '<h2>' + currentTable.number + '</h2>' +
                                        '<span>' + currentTable.pax + ' ท่าน</span>' +
                                        '<small>' + currentTable.alertMsg + '</small>';
                                    card.dataset.status = 'BUSY';
                                    card.dataset.pax = currentTable.pax;
                                    card.dataset.soup = currentTable.soup;
                                    card.dataset.alertMsg = currentTable.alertMsg;
                                    card.dataset.phone = currentTable.phone;
                                    card.dataset.startTime = currentTable.startTime;
                                    card.dataset.totalPrice = currentTable.totalPrice;
                                    card.dataset.packageType = data.packageType || payload.packageType;
                                }
                                selectTable(currentTable);
                            } else {
                                alert('ไม่สามารถเปิดโต๊ะได้');
                            }
                        }).catch(err => { console.error(err); alert('Server error'); });
                    }
                    function doCheckout() {
                        if (confirm("ยืนยันการเช็คบิลโต๊ะ " + currentTable.number + " ?")) {
                            fetch(CONTEXT_PATH + '/api/orders/checkout', {
                                method: 'POST',
                                headers: { 'Content-Type': 'application/json' },
                                body: JSON.stringify({ tableNo: currentTable.number })
                            }).then(res => { if (res.ok) { alert("เช็คบิลเรียบร้อย"); location.reload(); } });
                        }
                    }
                    function openTableMemberModal() {
                        if (!currentTable) { alert("กรุณาเลือกโต๊ะก่อนครับ"); return; }
                        if (!currentTable.phone) { alert("ลูกค้าโต๊ะนี้ไม่ได้เป็นสมาชิก (ไม่มีเบอร์โทรในระบบ)"); return; }
                        document.getElementById('tableMemberModal').style.display = 'flex';
                        document.getElementById('modalMemberName').innerText = "กำลังตรวจสอบ...";
                        currentTablePoints = 0;
                        updateRewardButtons();

                        fetch(CONTEXT_PATH + '/api/members/search?phone=' + encodeURIComponent(currentTable.phone))
                            .then(res => { if (!res.ok) throw new Error('member not found'); return res.json(); })
                            .then(member => {
                                document.getElementById('modalMemberName').innerText = member.name || 'ไม่ระบุชื่อ';
                                document.getElementById('modalMemberPoints').innerText = member.points || 0;
                                currentTablePoints = parseInt(member.points || '0', 10);
                                updateRewardButtons();
                            })
                            .catch(err => {
                                document.getElementById('modalMemberName').innerText = "ไม่พบข้อมูลสมาชิก";
                                document.getElementById('modalMemberPoints').innerText = '0';
                                currentTablePoints = 0;
                                updateRewardButtons();
                            });
                    }
                    function closeTableMemberModal() { document.getElementById('tableMemberModal').style.display = 'none'; }

                    function updateRewardButtons() {
                        let buttons = document.querySelectorAll('.btn-redeem');
                        buttons.forEach((btn) => {
                            let cost = parseInt(btn.dataset.points || '0');
                            let row = btn.closest('.reward-item-row');
                            if (currentTablePoints >= cost) {
                                if (row) { row.style.display = ''; row.style.opacity = '1'; }
                                btn.classList.add('active');
                                btn.style.backgroundColor = "#fdd835";
                                btn.style.color = "#333";
                                btn.disabled = false;
                                btn.innerText = "แลกเลย";
                            } else {
                                if (row) { row.style.display = 'none'; row.style.opacity = '0.3'; }
                                btn.classList.remove('active');
                                btn.style.backgroundColor = "#ccc";
                                btn.style.color = "#666";
                                btn.disabled = true;
                                btn.innerText = "แต้มไม่พอ";
                            }
                        });
                    }

                    function redeemFromBtn(btn) {
                        const rewardId = btn.dataset.id;
                        const rewardName = btn.dataset.name;
                        const pointsRequired = parseInt(btn.dataset.points);

                        pendingRedeemData = {
                            rewardId: rewardId,
                            pointsRequired: pointsRequired
                        };
                        const msgEl = document.getElementById('confirmRedeemMsg');
                        msgEl.innerHTML = 'ต้องการแลก <b>' + rewardName + '</b><br>โดยใช้คะแนน <b>' + pointsRequired + '</b> แต้ม?';
                        document.getElementById('confirmRedeemModal').style.display = 'flex';
                    }
                    function closeConfirmRedeemModal() {
                        document.getElementById('confirmRedeemModal').style.display = 'none';
                        pendingRedeemData = null;
                    }
                    function doRealRedeem() {
                        if (!pendingRedeemData) return;

                        const rId = pendingRedeemData.rewardId;
                        closeConfirmRedeemModal();
                        const formData = new URLSearchParams();
                        formData.append('phone', currentTable.phone);
                        formData.append('rewardId', rId);
                        formData.append('tableNo', currentTable.number);
                        fetch(CONTEXT_PATH + '/api/rewards/redeem', {
                            method: 'POST',
                            headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' },
                            body: formData
                        })
                            .then(res => res.json())
                            .then(data => {
                                if (data.ok) {
                                    alert("แลกรางวัลสำเร็จ!");

                                    document.getElementById('modalMemberPoints').innerText = data.remainingPoints;
                                    currentTablePoints = data.remainingPoints;
                                    updateRewardButtons();

                                    if (currentTable) {
                                        currentTable.totalPrice = data.newTotalPrice;
                                        currentTable.discount = data.newDiscount;
                                        currentTable.netPrice = data.newNetPrice;
                                        updatePriceUI(currentTable);

                                        const card = document.querySelector('.table-card[data-number="' + currentTable.number + '"]');
                                        if (card) {
                                            card.dataset.totalPrice = data.newTotalPrice;
                                            card.dataset.discount = data.newDiscount;
                                            card.dataset.netPrice = data.newNetPrice;
                                        }
                                    }
                                } else {
                                    alert("เกิดข้อผิดพลาด: " + data.error);
                                }
                            })
                            .catch(err => {
                                console.error(err);
                                alert("Server Connection Error");
                            });
                    }


                    let currentPaymentMethod = 'CASH';
                    const SHOP_PROMPTPAY = "0812345678";
                    let successModalTimeout = null;
                    function doCheckout() {
                        if (!currentTable) return;


                        document.getElementById('paymentModal').style.display = 'flex';
                        document.getElementById('inputCash').value = '';
                        document.getElementById('txtChange').innerText = '0.00';
                        document.getElementById('inputCreditRef').value = '';
                        document.getElementById('qrcode').innerHTML = '';

                        let net = currentTable.netPrice || 0;
                        document.getElementById('payNetPrice').innerText = net.toLocaleString("en-US", { minimumFractionDigits: 2 });

                        let cashBtn = document.querySelectorAll('.method-card')[1];
                        selectMethod('CASH', cashBtn);
                    }
                    function closePaymentModal() {
                        document.getElementById('paymentModal').style.display = 'none';
                    }
                    function selectMethod(method, element) {
                        currentPaymentMethod = method;

                        document.querySelectorAll('.method-card').forEach(el => el.classList.remove('active'));
                        element.classList.add('active');

                        document.getElementById('cashInputSection').style.display = 'none';
                        document.getElementById('qrSection').style.display = 'none';
                        document.getElementById('creditSection').style.display = 'none';

                        if (method === 'CASH') {
                            document.getElementById('cashInputSection').style.display = 'block';
                            setTimeout(() => document.getElementById('inputCash').focus(), 100);
                        }
                        else if (method === 'SCAN') {
                            document.getElementById('qrSection').style.display = 'block';
                            generatePromptPayQR();
                        }
                        else if (method === 'CREDIT') {
                            document.getElementById('creditSection').style.display = 'block';
                            setTimeout(() => document.getElementById('inputCreditRef').focus(), 100);
                        }
                    }
                    function calcChange() {
                        let net = currentTable.netPrice || 0;
                        let cash = parseFloat(document.getElementById('inputCash').value) || 0;
                        let change = cash - net;

                        let changeEl = document.getElementById('txtChange');
                        if (change < 0) {
                            changeEl.innerText = "ขาด " + Math.abs(change).toLocaleString("en-US", { minimumFractionDigits: 2 });
                            changeEl.style.color = 'red';
                        } else {
                            changeEl.innerText = change.toLocaleString("en-US", { minimumFractionDigits: 2 });
                            changeEl.style.color = '#2e7d32';
                        }
                    }


                    function generatePromptPayQR() {
                        document.getElementById("qrcode").innerHTML = "";
                        let net = currentTable.netPrice || 0;
                        let payload = createPromptPayPayload(SHOP_PROMPTPAY, net);

                        new QRCode(document.getElementById("qrcode"), {
                            text: payload,
                            width: 180,
                            height: 180,
                            correctLevel: QRCode.CorrectLevel.L
                        });
                    }

                    function createPromptPayPayload(target, amount) {
                        let targetSanitized = target.replace(/[^0-9]/g, '');
                        let targetType = targetSanitized.length >= 13 ? '02' : '01';
                        if (targetType === '01' && targetSanitized.startsWith('0')) {
                            targetSanitized = '66' + targetSanitized.substring(1);
                        }
                        let amountStr = amount.toFixed(2);
                        let f_amount = '54' + ('00' + amountStr.length).slice(-2) + amountStr;
                        let f_target = '29' + ('00' + (37)).slice(-2) + '0016A000000677010111' + targetType + ('00' + targetSanitized.length).slice(-2) + targetSanitized;
                        let data = '000201010212' + f_target + '5303764' + f_amount + '5802TH6304';

                        let crc = 0xFFFF;
                        for (let i = 0; i < data.length; i++) {
                            let x = ((crc >> 8) ^ data.charCodeAt(i)) & 0xFF;
                            x ^= x >> 4;
                            crc = ((crc << 8) ^ (x << 12) ^ (x << 5) ^ x) & 0xFFFF;
                        }
                        return data + ('0000' + crc.toString(16).toUpperCase()).slice(-4);
                    }


                    function submitPayment() {
                        let net = currentTable.netPrice || 0;
                        let paidAmount = 0;

                        if (currentPaymentMethod === 'CASH') {
                            paidAmount = parseFloat(document.getElementById('inputCash').value) || 0;
                            if (paidAmount < net) { alert("ยอดเงินไม่พอชำระครับ"); return; }
                        }
                        else if (currentPaymentMethod === 'CREDIT') {
                            let ref = document.getElementById('inputCreditRef').value;
                            if (!ref) { alert("กรุณากรอกเลขท้ายบัตรอ้างอิง"); return; }
                            paidAmount = net;
                        }
                        else {
                            paidAmount = net;
                        }

                        if (!confirm("ยืนยันการชำระเงิน?")) return;

                        fetch(CONTEXT_PATH + '/api/orders/checkout', {
                            method: 'POST',
                            headers: { 'Content-Type': 'application/json' },
                            body: JSON.stringify({
                                tableNo: currentTable.number,
                                method: currentPaymentMethod,
                                paid: paidAmount
                            })
                        })
                            .then(res => res.json())
                            .then(data => {
                                if (data.ok) {
                                    let pointsMsg = '';
                                    if (data.pointsEarned > 0) {
                                        pointsMsg = '<p style="margin-top: 12px; font-size: 16px; color: #ffb300;">ลูกค้าได้รับ <b>' + data.pointsEarned + ' แต้ม!</b></p>';
                                    }
                                    
                                    document.getElementById('successPointsMsg').innerHTML = pointsMsg;
                                    document.getElementById('successModal').style.display = 'flex';
                                } else {
                                    alert("เกิดข้อผิดพลาด: " + data.error);
                                }
                            });
                    }

                </script>
                <div id="tableMemberModal" class="modal-overlay" style="display: none;">
                    <div class="modal-box reward-box">
                        <div class="modal-header">
                            <h3>ข้อมูลสมาชิก & แลกแต้ม</h3>
                            <i class="fa-solid fa-times close-btn" onclick="closeTableMemberModal()"></i>
                        </div>

                        <div class="points-bar">
                            <span id="modalMemberName">กำลังโหลด...</span>
                            <div class="coin-badge">
                                <i class="fa-solid fa-coins"></i>
                                <span id="modalMemberPoints">0</span> แต้ม
                            </div>
                        </div>

                        <h4 style="margin: 22px; color: #ffffff;">รายการของรางวัล</h4>

                        <div class="reward-list-scroll">
                            <c:forEach var="reward" items="${rewards}">
                                <div class="reward-item-row">
                                    <div class="reward-info">
                                        <div class="reward-name">${reward.name}</div>
                                        <div class="reward-cost">ใช้ <span>${reward.pointsRequired}</span> แต้ม</div>
                                    </div>
                                    <button class="btn-redeem" id="btn-redeem-${reward.id}" data-id="${reward.id}"
                                        data-name="${reward.name}" data-points="${reward.pointsRequired}"
                                        onclick="redeemFromBtn(this)">
                                        แลกเลย
                                    </button>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>

                <div id="confirmRedeemModal" class="modal-overlay" style="display: none; z-index: 9999;">
                    <div class="modal-box"
                        style="width: 350px; text-align: center; padding: 30px; border-radius: 15px; background: white; box-shadow: 0 10px 25px rgba(0,0,0,0.5);">

                        <i class="fa-solid fa-gift"
                            style="font-size: 60px; color: #fdd835; margin-bottom: 20px; text-shadow: 2px 2px 4px rgba(0,0,0,0.1);"></i>

                        <h3 style="color: #333; margin-bottom: 10px; font-size: 22px;">ยืนยันการแลกรางวัล</h3>

                        <p id="confirmRedeemMsg"
                            style="color: #666; font-size: 16px; margin-bottom: 30px; line-height: 1.6;">
                        </p>

                        <div style="display: flex; gap: 15px; justify-content: center;">
                            <button onclick="closeConfirmRedeemModal()"
                                style="padding: 10px 20px; border: none; background: #eee; color: #555; border-radius: 8px; cursor: pointer; font-size: 16px; font-weight: bold;">
                                ยกเลิก
                            </button>
                            <button onclick="doRealRedeem()"
                                style="padding: 10px 25px; border: none; background: #2e7d32; color: white; border-radius: 8px; cursor: pointer; font-size: 16px; font-weight: bold; box-shadow: 0 4px 6px rgba(0,0,0,0.1);">
                                ยืนยัน
                            </button>
                        </div>
                    </div>
                </div>

                <div id="paymentModal" class="modal-overlay" style="display: none; z-index: 10000;">
                    <div class="payment-box">
                        <div class="payment-header">
                            <h3>ชำระเงิน</h3>
                            <span class="close-btn" onclick="closePaymentModal()">&times;</span>
                        </div>

                        <div class="payment-body">
                            <div class="amount-display">
                                <p>ยอดชำระสุทธิ</p>
                                <h1 id="payNetPrice">0.00</h1>
                            </div>

                            <div class="payment-methods">
                                <div class="method-card" onclick="selectMethod('SCAN', this)">
                                    <i class="fas fa-qrcode"></i>
                                    <span>สแกนจ่าย</span>
                                </div>
                                <div class="method-card active" onclick="selectMethod('CASH', this)">
                                    <i class="fas fa-money-bill-wave"></i>
                                    <span>เงินสด</span>
                                </div>
                                <div class="method-card" onclick="selectMethod('CREDIT', this)">
                                    <i class="fas fa-credit-card"></i>
                                    <span>บัตรเครดิต</span>
                                </div>
                            </div>

                            <div id="cashInputSection" style="margin-top: 15px;">
                                <div class="form-group">
                                    <label style="font-size: 14px; color: #666;">รับเงินมา (บาท)</label>
                                    <input type="number" id="inputCash" class="form-input"
                                        style="text-align: center; font-size: 20px; font-weight: bold;"
                                        placeholder="0.00" onkeyup="calcChange()" onchange="calcChange()">
                                </div>
                                <div style="display: flex; justify-content: space-between; margin-top: 10px;">
                                    <span style="font-size: 16px;">เงินทอน:</span>
                                    <span id="txtChange"
                                        style="font-weight: bold; font-size: 20px; color: #2e7d32;">0.00</span>
                                </div>
                            </div>

                            <div id="qrSection" style="margin-top: 15px; display:none; text-align: center;">
                                <div
                                    style="background: white; padding: 15px; display: inline-block; border: 1px solid #ddd; border-radius: 8px;">
                                    <div id="qrcode"></div>
                                </div>
                                <p style="margin-top: 10px; color: #666; font-size: 14px;">สแกนผ่านแอปธนาคารได้ทุกธนาคาร
                                </p>
                            </div>

                            <div id="creditSection" style="margin-top: 15px; display:none;">
                                <div class="form-group">
                                    <label style="font-size: 14px; color: #666;">เลขที่อ้างอิง / 4 ตัวท้ายบัตร</label>

                                    <input type="text" id="inputCreditRef" class="form-input"
                                        style="text-align: center; font-size: 20px; letter-spacing: 2px; font-weight: bold;"
                                        placeholder="XXXX" maxlength="4"
                                        oninput="this.value = this.value.replace(/[^0-9]/g, '').slice(0, 4)">

                                </div>
                                <div style="text-align: center; margin-top: 10px; color: #1976d2; font-size: 13px;">
                                    <i class="fas fa-check-circle"></i> กรุณารูดบัตรที่เครื่อง EDC ก่อนบันทึก
                                </div>
                            </div>

                            <button onclick="submitPayment()" class="btn-confirm-pay">
                                ยืนยันการชำระเงิน
                            </button>
                        </div>
                    </div>
                </div>

                <div id="successModal" class="modal-overlay" style="display: none; z-index: 10001;" onclick="if(event.target.id === 'successModal') { document.getElementById('successModal').style.display = 'none'; location.reload(); }">
                    <div class="modal-box" style="width: 380px; text-align: center; padding: 40px; border-radius: 10px; background: white; box-shadow: 0 15px 35px rgba(0,0,0,0.3);" onclick="event.stopPropagation();">
                        <div style="animation: scaleIn 0.6s ease-out;">
                            <i class="fas fa-check-circle" style="font-size: 50px; color: #4caf50; margin-bottom: 20px; display: block;"></i>
                            <h2 style="color: #333; margin: 0; font-size: 20px; margin-bottom: 10px;">ชำระเงินเรียบร้อย</h2>
                            <div id="successPointsMsg" style="margin-top: 15px;"></div>
                        </div>
                    </div>
                </div>

                <style>
                    @keyframes scaleIn {
                        from {
                            opacity: 0;
                            transform: scale(0.8);
                        }
                        to {
                            opacity: 1;
                            transform: scale(1);
                        }
                    }
                </style>
            </body>

            </html>