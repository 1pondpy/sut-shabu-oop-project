<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Kitchen Monitor</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/kitchen.css">
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+Thai:wght@100..900&display=swap" rel="stylesheet">
<style>
.logout-btn {
    margin-left: auto;
    display: inline-flex;
    align-items: center;
    gap: 8px;
    padding: 8px 14px;
    border-radius: 20px;
    background: transparent;
    color: white;
    border: 1px solid rgba(255,255,255,0.35);
    cursor: pointer;
    text-decoration: none;
    font-weight: 600;
    margin-left: 450px;
}

.logout-btn:hover {
    background: rgba(255,255,255,0.10);
}

    </style>
</head>
<body>

    <div class="kitchen-header">
        <div style="font-size: 24px; font-weight: bold;">
            <img src="${pageContext.request.contextPath}/images/logo.png" style="width: 60px; vertical-align: middle;"> 
        </div>
        
        <div class="tab-menu">
            <a href="${pageContext.request.contextPath}/kitchen?tab=PENDING" class="tab-item ${currentTab == 'PENDING' ? 'active' : ''}">
                รอจัดเตรียม <span class="stat-yellow">${waitingCount}</span>
            </a>
            
            <a href="${pageContext.request.contextPath}/kitchen?tab=FINISHED" class="tab-item ${currentTab == 'FINISHED' ? 'active' : ''}">
                เสร็จแล้ว <span class="stat-yellow">${finishedCount}</span>
            </a>
        </div>

        <form action="${pageContext.request.contextPath}/logout" method="post" style="margin:0;">
            <button type="submit" class="logout-btn" onclick="return confirm('ยืนยันออกจากระบบ?');">
                <i class="fas fa-sign-out-alt"></i>
                ออกจากระบบ
            </button>
        </form>
    </div>

    <div class="kitchen-grid">
        
        <c:if test="${empty orders}">
            <div class="no-data">ไม่มีคำสั่งในช่วงนี้</div>
        </c:if>

        <c:forEach var="order" items="${orders}">
            <div class="ticket-card status-${order.status}" id="card-${order.id}">
                <div class="ticket-header">
                    <div class="ticket-title">
                        โต๊ะ: <span>${order.session.tableNo}</span>
                        <span style="color:#888; font-size:14px;">#${order.id}</span>
                    </div>
                    <div class="ticket-time">
                        เวลา: ${fn:replace(fn:substring(order.orderTime, 0, 16), 'T', ' ')}</span>
                    </div>
                </div>

                <div class="ticket-items">
                    <c:forEach var="item" items="${order.orderItems}">
                        <div class="ticket-row">
                            <span>${item.menuItem.name}</span>
                            <span style="font-weight:bold;">x${item.quantity}</span>
                        </div>
                    </c:forEach>
                </div>

                <c:if test="${currentTab == 'PENDING'}">
                    <button class="btn-finish" onclick="finishOrder('${order.id}')">เสร็จสิ้น</button>
                </c:if>
                <c:if test="${currentTab == 'FINISHED'}">
                    <div style="text-align:center; color:green; font-weight:bold;">จัดเตรียมเสร็จสิ้น</div>
                </c:if>

            </div>
        </c:forEach>
    </div>

    <script>
        function finishOrder(orderId) {
            var idNum = parseInt(orderId + "", 10);
            fetch('${pageContext.request.contextPath}/api/kitchen/finish', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ orderId: idNum })
            })
            .then(res => {
                if (res.ok) {
                    var el = document.getElementById('card-' + orderId);
                    if (el) el.remove();
                    setTimeout(function(){ location.reload(); }, 800);
                } else {
                    console.error('Finish request failed', res.status);
                }
            }).catch(function(err){ console.error('Finish request error', err); });
        }
        setInterval(function(){ location.reload(); }, 8000);
    </script>

</body>
</html>
