<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SUT Shabu - Menu</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/menustyle.css">
    <script>
        
        function openCallModal() {
            var el = document.getElementById('callStaffModal');
            if (el) el.style.display = 'flex';
        }
        function closeCallModal(evt) {
            if (evt && evt.target && evt.target.classList && evt.target.classList.contains('modal-overlay')) {
                var el = document.getElementById('callStaffModal');
                if (el) el.style.display = 'none';
            }
        }
    </script>
    <style>
        .filter-bar {
            display: flex;
            justify-content: left; 
            margin: 5px 0;
        }

        .search-box {
            margin-bottom: 5px;
            margin-top: -5px;
            position: relative;
            display: flex;
            align-items: center;
            width: 100%;
            max-width: 400px; 
        }
        
        .search-input {
            width: 100%;
            padding: 10px 45px 10px 15px;
            border: 1px solid #ddd;
            border-radius: 25px; 
            font-size: 14px;
            outline: none;
            font-family: 'Noto Sans Thai', sans-serif;
            background-color: white;
            transition: border-color 0.3s;
            box-shadow: 0 2px 5px rgba(0,0,0,0.05);
        }
        
        .search-input:focus {
            border-color: #fdd835; 
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
        
    
        .categories { margin-bottom: 10px; }
    </style>

</head>
<body>
    <nav class="sidebar">
        <div class="logo">
              <img src="/sut-shabu/images/logo.png" alt="SUT Shabu logo" style= "width: 100px; margin-top: 10px;">
        </div>
        
        <a href="${pageContext.request.contextPath}/menu?tableNo=${tableNo}" class="nav-item active" style="text-decoration: none;">
            <i class="fas fa-utensils"></i>
            <span>Menu</span>
        </a>
    
        <a href="${pageContext.request.contextPath}/my-orders?tableNo=${not empty tableNo ? tableNo : param.tableNo}" class="nav-item" style="text-decoration: none;">
            <i class="fas fa-clipboard-list"></i>
            <span>My Orders</span>
        </a>
    
        <a href="${pageContext.request.contextPath}/reward?tableNo=${not empty tableNo ? tableNo : param.tableNo}" class="nav-item" style="text-decoration: none;">
            <i class="fas fa-gamepad"></i>
            <span>Reward & Game</span>
        </a>
    </nav>

    <main class="main-content">
        
        <header class="top-bar">
            <div class="table-info">
                <span>โต๊ะ <span>${not empty tableNo ? tableNo : param.tableNo}</span></span>
                <span style="font-size: 16px; color: #666; margin-left:12px;"><i class="fas fa-user"></i> <span>${not empty pax ? pax : 0}</span></span>
                <c:if test="${canOrder}">
                    <c:choose>
                        <c:when test="${fn:contains(fn:toLowerCase(packageName), 'premium')}">
                            <span class="package-info" style="font-size:14px; background:#fdd835; color:#6b5000; padding:8px 12px; border-radius:20px; margin-left:12px; display:inline-block;">
                                <c:out value="${packageName}" />
                            </span>
                        </c:when>
                        <c:otherwise>
                            <span class="package-info" style="font-size:14px; background:#cacaca; color:#555; padding:8px 12px; border-radius:20px; margin-left:12px; display:inline-block;">
                                <c:out value="${packageName}" />
                            </span>
                        </c:otherwise>
                    </c:choose>
                </c:if>
            </div>
            <div class="header-actions">
    <div class="timer-badge">
        <i class="fas fa-stopwatch"></i> <span id="timer"></span>
    </div>

    <c:choose>
        <c:when test="${canOrder or (not empty remainingSeconds and remainingSeconds <= 0)}">
            <button class="btn-call" onclick="openCallModal()">
                <i class="fas fa-bell"></i> Call Staff
            </button>
        </c:when>
        <c:otherwise>
            <button class="btn-call" disabled style="background-color: #9e9e9e; cursor: not-allowed; opacity: 0.7; box-shadow: none;">
                <i class="fas fa-bell-slash"></i> Call Staff
            </button>
        </c:otherwise>
    </c:choose>
</div>
        </header>

        <div class="order-area">
            
            <div class="menu-section">
                <div class="categories">
                     <button class="cat-pill" data-category="all">ทั้งหมด</button>
                    <button class="cat-pill" data-category="เนื้อสัตว์">เนื้อสัตว์</button>
                    <button class="cat-pill" data-category="ผัก">ผัก</button>
                    <button class="cat-pill" data-category="อาหารแปรรูป">อาหารแปรรูป</button>
                    <button class="cat-pill" data-category="ของทานเล่น">ของทานเล่น</button>
                    <button class="cat-pill" data-category="ของหวาน">ของหวาน</button>
                    <button class="cat-pill" data-category="ผลไม้">ผลไม้</button>
                </div>
                <div class="filter-bar">
                    <form action="menu" method="GET" class="search-box">
                        <input type="hidden" name="tableNo" value="${not empty tableNo ? tableNo : param.tableNo}">
                        
                        <button type="submit" class="search-btn">
                            <i class="fas fa-search"></i>
                        </button>
                        
                        <input type="text" name="search" class="search-input" id="liveSearchInput"
                               placeholder="ค้นหาเมนูอาหาร..." 
                               value="${currentSearch}" 
                               onkeyup="liveSearch()"
                               autocomplete="off">
                    </form>
                </div>
                <div class="menu-grid-container">
                    
    <c:if test="${!canOrder}">
        <div style="background-color: #ffebee; color: #c62828; padding: 15px; margin-bottom: 20px; border-radius: 8px; text-align: center; border: 1px solid #ef9a9a;">
            <i class="fas fa-exclamation-circle"></i> <c:out value="${warningMsg}" escapeXml="false" />
        </div>
    </c:if>

    <div class="menu-grid" id="menuGrid">
        <c:forEach var="item" items="${menuItems}">
            <c:set var="cardCategory" value="${empty item.category ? 'other' : item.category}" />
            <div class="food-card" data-category="${cardCategory}" data-id="${item.id}" data-name="${fn:escapeXml(item.name)}">
                
                <c:choose>
                    <c:when test="${empty item.imageUrl}">
                        <c:set var="imagePath" value="/images/placeholder.png" />
                    </c:when>
                    <c:otherwise>
                        <c:set var="imagePath" value="/images/${item.imageUrl}" />
                    </c:otherwise>
                </c:choose>
                <img src="${pageContext.request.contextPath}${imagePath}"
                     data-alt-src="${imagePath}"
                     onerror="if (this.getAttribute('data-alt-src') && !this.dataset.triedAlt) { this.dataset.triedAlt = '1'; this.src = this.getAttribute('data-alt-src'); } else { this.src='https://placehold.co/200x150?text=No+Image'; }"
                     class="food-img" alt="${fn:escapeXml(item.name)}" />
                
                <div class="food-name">${item.name}</div>

                <c:choose>
                    <c:when test="${canOrder}">
                        <button class="btn-add" onclick="addToCart(this.closest('.food-card').dataset.id)">Add</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn-add" disabled style="background-color: #ccc; cursor: not-allowed; color: #666;">
                            <i class="fas fa-lock"></i>
                        </button>
                    </c:otherwise>
                </c:choose>
                </div>
        </c:forEach>
    </div>
</div>
            </div>

            <aside class="cart-section">
                <div class="cart-header">
                    รายการที่เลือก <span id="cartCount">(0)</span>
                </div>
                <div class="cart-items" id="cartItems">
                    </div>
                <div class="cart-footer">
                    <button class="btn-confirm" onclick="submitOrder()">ยืนยันการสั่ง</button>
                </div>
            </aside>

        </div>
    </main>

    <script>
       function liveSearch() {
    const keyword = document.getElementById("liveSearchInput")
        .value.toLowerCase().trim();

    const activeCategory =
        document.querySelector('.cat-pill.active')?.dataset.category || 'all';

    document.querySelectorAll(".food-card").forEach(card => {
        const name = card.dataset.name.toLowerCase();
        const category = card.dataset.category;

        const matchName = name.includes(keyword);
        const matchCategory = activeCategory === 'all' || category === activeCategory;

        card.style.display = (matchName && matchCategory) ? "block" : "none";
    });
}
    document.addEventListener('DOMContentLoaded', function() {
        let rawDuration = '${empty remainingSeconds ? 0 : remainingSeconds}';
        function parseRawDuration(s) {
            if (!s) return 0;
            if (s.indexOf(':') >= 0) {
                const parts = s.split(':').map(p => parseInt(p, 10) || 0);
                if (parts.length === 3) return parts[0]*3600 + parts[1]*60 + parts[2];
                if (parts.length === 2) return parts[0]*60 + parts[1];
                return parseInt(s, 10) || 0;
            }
            return parseInt(s, 10) || 0;
        }
        let duration = parseRawDuration(rawDuration);

        function startTimer() {
            const timerEl = document.getElementById('timer');
            function updateDisplay() {
                if (duration <= 0) {
                    timerEl.innerText = "00:00:00";
                    timerEl.style.color = "red";
                    return;
                }
                let h = Math.floor(duration / 3600);
                let m = Math.floor((duration % 3600) / 60);
                let s = duration % 60;
                timerEl.innerText = h.toString().padStart(2,'0') + ':' + m.toString().padStart(2,'0') + ':' + s.toString().padStart(2,'0');
                duration--; 
            }
            updateDisplay();
            setInterval(updateDisplay, 1000);
        }
        startTimer();

        let cart = [];
        function addToCart(id) {
            id = id.toString();
            const card = document.querySelector('.food-card[data-id="' + id + '"]');
            const name = card ? card.dataset.name : '';
            let existingItem = cart.find(item => item.id === id);
            if (existingItem) { existingItem.qty++; } else { cart.push({ id: id, name: name, qty: 1 }); }
            renderCart();
        }
        window.addToCart = addToCart; 

        function updateQty(id, change) { let item = cart.find(item => item.id === id); if (item) { item.qty += change; if (item.qty <= 0) { removeItem(id); return; } } renderCart(); }
        function removeItem(id) { cart = cart.filter(item => item.id !== id); renderCart(); }
        function renderCart() {
            const cartContainer = document.getElementById('cartItems');
            const cartCountLabel = document.getElementById('cartCount');
            cartContainer.innerHTML = '';
            let totalItems = 0;
            if (cart.length === 0) {
                cartContainer.innerHTML = `<div style="text-align:center; color:#999; margin-top:50px; font-size:14px;">ยังไม่มีรายการ<br>กด Add เพื่อสั่งอาหาร</div>`;
                cartCountLabel.innerText = `(0)`;
                return;
            }
            cart.forEach(item => {
                totalItems += item.qty;
                const div = document.createElement('div');
                div.className = 'cart-item';
                div.innerHTML = '' +
                    '<div class="cart-item-name">' + item.name + '</div>' +
                    '<div class="cart-controls">' +
                        '<button class="btn-qty" onclick="updateQty(\'' + item.id + '\', -1)">-</button>' +
                        '<span style="font-weight:bold; width:20px; text-align:center;">' + item.qty + '</span>' +
                        '<button class="btn-qty" onclick="updateQty(\'' + item.id + '\', 1)">+</button>' +
                        '<i class="fa-solid fa-trash" onclick="removeItem(\'' + item.id + '\')" style="cursor:pointer; margin-left:15px; color:#c62828;"></i>' +
                    '</div>';
                cartContainer.appendChild(div);
            });
            try {
                cartCountLabel.innerText = '(' + totalItems + ')';
                cartCountLabel.textContent = '(' + totalItems + ')';
            } catch (e) {
                console.warn('Could not update cartCount label', e);
            }
        }
        function submitOrder() {
            if (cart.length === 0) { alert("กรุณาเลือกรายการอาหารก่อนครับ"); return; }
            let tableInput = document.querySelector('input[name="tableNo"]');
            let currentTable = tableInput && tableInput.value ? tableInput.value : 'A1';
            let orderData = cart.map(item => { return { id: parseInt(item.id), qty: item.qty }; });
            fetch('${pageContext.request.contextPath}/api/orders/submit?tableNo=' + encodeURIComponent(currentTable), {
                method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(orderData)
            })
            .then(response => { if (response.ok) { return response.text(); } return response.text().then(text => { throw new Error(text) }); })
            .then(data => { alert(data); cart = []; renderCart(); })
            .catch(error => { console.error('Error:', error); alert("เกิดข้อผิดพลาด: " + error.message); });
        }

        window.updateQty = updateQty;
        window.removeItem = removeItem;
        window.submitOrder = submitOrder;

        const pills = document.querySelectorAll('.cat-pill');
        const foodCards = document.querySelectorAll('.food-card');
        pills.forEach(pill => { pill.addEventListener('click', function() {
            pills.forEach(p => p.classList.remove('active'));
            this.classList.add('active');
            const categoryToFilter = this.getAttribute('data-category');
            foodCards.forEach(card => {
                const cardCategory = card.getAttribute('data-category');
                if (categoryToFilter === 'all') { card.style.display = 'block'; } else { if (cardCategory === categoryToFilter) { card.style.display = 'block'; } else { card.style.display = 'none'; } }
            });
        }); });
        const activePill = document.querySelector('.cat-pill.active');
        if (activePill) activePill.click();

        function openCallModal() { document.getElementById('callStaffModal').style.display = 'flex'; }
        function closeCallModal(event) { if (event.target.classList.contains('modal-overlay')) { document.getElementById('callStaffModal').style.display = 'none'; } }
        window.sendCallRequest = function(type) { let currentTable = '${not empty tableNo ? tableNo : param.tableNo}'; if(!currentTable) currentTable = 'A1'; fetch('${pageContext.request.contextPath}/api/call-staff/send', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ tableNo: currentTable, type: type }) }) .then(res => { if(res.ok) { alert('แจ้งพนักงานเรื่อง "' + type + '" เรียบร้อยแล้วครับ'); document.getElementById('callStaffModal').style.setProperty('display', 'none', 'important'); } }); }
    });
    </script>

    <div id="callStaffModal" class="modal-overlay" style="display: none;" onclick="closeCallModal(event)">
        <div class="modal-box">
            <div class="call-grid">
                <button class="btn-call-option" onclick="sendCallRequest('เรียกเก็บเงิน')">เรียกเก็บเงิน</button>
                <button class="btn-call-option" onclick="sendCallRequest('เติมน้ำซุป')">เติมน้ำซุป</button>
                <button class="btn-call-option" onclick="sendCallRequest('ขออุปกรณ์')">ขออุปกรณ์</button>
                <button class="btn-call-option" onclick="sendCallRequest('อื่นๆ')">อื่น ๆ</button>
            </div>
        </div>
    </div>
</body>
</html>
