<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SUT Shabu - My Orders</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/menustyle.css">
    <link rel="stylesheet" href="css/menustyle.css">
    <style>
        
    /* Modal Overlay */
    .modal-overlay {
        display: none; 
        position: fixed;
        top: 0; left: 0;
        width: 100%; height: 100%;
        background-color: rgba(0, 0, 0, 0.6); 
        z-index: 1000;
        justify-content: center;
        align-items: center;
    }


    .modal-box {
        background: white;
        padding: 30px;
        border-radius: 15px;
        width: 320px;
        box-shadow: 0 5px 15px rgba(0,0,0,0.3);
    }


    .call-grid {
        display: grid;
        grid-template-columns: 1fr 1fr; 
        gap: 15px;
    }


    .btn-call-option {
        background-color: #fdd835;
        border: none;
        border-radius: 10px;
        padding: 20px 10px;
        font-size: 16px;
        font-weight: bold;
        color: #333;
        cursor: pointer;
        transition: transform 0.2s;
        height: 100px; 
    }

    .btn-call-option:hover {
        transform: scale(1.05);
        background-color: #fbc02d;
    }
    </style>
</head>
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
<body>

    <nav class="sidebar">
        <div class="logo">
            <img src="${pageContext.request.contextPath}/images/logo.png" style="width: 100px; margin-top: 10px;" >
        </div>
        
        <a href="${pageContext.request.contextPath}/menu?tableNo=${not empty tableNo ? tableNo : param.tableNo}" class="nav-item" style="text-decoration: none;">
            <i class="fas fa-utensils"></i>
            <span>Menu</span>
        </a>
    
        <a href="${pageContext.request.contextPath}/my-orders?tableNo=${not empty tableNo ? tableNo : param.tableNo}" class="nav-item active" style="text-decoration: none;">
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
                <c:if test="${not empty packageName}">
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


        <div class="order-area" style="flex-direction: column; overflow-y: auto;">
            
            <h2 style="margin-bottom: 5px; color: #333;">รายการที่สั่งไปแล้ว (<span>${not empty totalOrders ? totalOrders : 0}</span>)</h2>

            <div class="order-list-grid">
                <c:forEach var="order" items="${orders}">
                    <div class="order-summary-card">
                        <div class="order-card-header">
                            <div>
                                <span style="font-weight: bold; font-size: 18px; color: #b71c1c;">#${order.id}</span>
                                <span style="color: #888; font-size: 14px; margin-left: 10px;">เวลา: ${fn:replace(fn:substring(order.orderTime, 0, 16), 'T', ' ')}</span>
                            </div>
                            <div class="status-indicator ${order.status == 'PENDING' ? 'status-blue' : 'status-green'}">
                                <i class="fas fa-circle" style="font-size: 10px; margin-right: 5px;"></i>
                                <span>${order.status == 'PENDING' ? 'รอจัดเตรียม' : 'จัดเตรียมเสร็จแล้ว'}</span>
                            </div>
                        </div>

                        <hr style="border: 0; border-top: 1px solid #eee; margin: 10px 0;">

                        <div class="order-card-body">
                            <c:forEach var="item" items="${order.orderItems}">
                                <div class="order-item-row">
                                    <span>${item.menuItem.name}</span>
                                    <span style="font-weight: bold;">${item.quantity}</span>
                                </div>
                                 
                            </c:forEach>
                        </div>
                    </div>
                </c:forEach>
            </div>

        </div>
    </main>

    <script>
        // Timer (seconds)
        let duration = ${empty remainingSeconds ? 0 : remainingSeconds};

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

        window.sendCallRequest = function(type) {
            let currentTable = '${not empty tableNo ? tableNo : param.tableNo}';
            if(!currentTable) currentTable = 'A1';
            fetch('${pageContext.request.contextPath}/api/call-staff/send', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ tableNo: currentTable, type: type })
            })
            .then(res => {
                if(res.ok) {
                    alert('แจ้งพนักงานเรื่อง "' + type + '" เรียบร้อยแล้วครับ');
                    document.getElementById('callStaffModal').style.setProperty('display', 'none', 'important');
                }
            });
        };

        setTimeout(function() {
            window.location.reload();
        }, 10000);
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
