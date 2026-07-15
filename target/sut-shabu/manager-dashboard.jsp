<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="refresh" content="10">
    <title>Dashboard - SUT Shabu Manager</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/manager.css">
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+Thai:wght@100..900&display=swap" rel="stylesheet">
    <style>
        .dashboard-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }

        .highlight-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(340px, 1fr));
            gap: 20px;
            margin-bottom: 24px;
        }

        .stat-card {
            background: white;
            border-radius: 10px;
            padding: 25px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            transition: transform 0.2s;
        }

        .stat-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 4px 12px rgba(0,0,0,0.15);
        }

        .stat-header {
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-bottom: 15px;
        }

        .stat-title {
            font-size: 14px;
            color: #666;
            font-weight: 500;
        }

        .stat-icon {
            font-size: 28px;
            width: 50px;
            height: 50px;
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 10px;
        }

        .icon-revenue  { background: #fff9c4; color: #fdd835; }
        .icon-order { background: #fff9c4; color: #fdd835; }
        .icon-table  { background: #fff9c4; color: #fdd835; }
        .icon-member  { background: #fff9c4; color: #fdd835; }
        .icon-staff  { background: #fff9c4; color: #fdd835; }
        .icon-reward { background: #fff9c4; color: #fdd835; }

        .stat-value {
            font-size: 32px;
            font-weight: bold;
            color: #222;
            margin: 10px 0;
        }

        .big-card .stat-value { font-size: 44px; }
        .big-card .stat-title { font-size: 16px; }

        .stat-subtitle {
            font-size: 13px;
            color: #999;
        }

        .growth-positive { color: #4caf50; font-weight: bold; }
        .growth-negative { color: #f44336; font-weight: bold; }

        .section-title {
            font-size: 20px;
            font-weight: bold;
            color: #c0392b;
            margin: 30px 0 15px 0;
            display: flex;
            align-items: center;
        }

        .section-title i {
            margin-right: 10px;
            color: #fdd835;
        }

        .table-wrapper {
            background: white;
            border-radius: 10px;
            padding: 20px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            margin-bottom: 20px;
        }

        .top-items-list {
            list-style: none;
            padding: 0;
            margin: 0;
        }

        .top-items-list li {
            padding: 12px 0;
            border-bottom: 1px solid #eee;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .top-items-list li:last-child {
            border-bottom: none;
        }

        .item-name {
            font-weight: 500;
            color: #333;
        }

        .item-count {
            background: #fdd835;
            color: #333;
            padding: 5px 12px;
            border-radius: 15px;
            font-weight: bold;
            font-size: 13px;
        }

        .payment-breakdown {
            display: flex;
            gap: 15px;
            flex-wrap: wrap;
        }

        .payment-item {
            flex: 1;
            min-width: 150px;
            background: #f5f5f5;
            padding: 15px;
            border-radius: 8px;
            text-align: center;
        }

        .payment-method {
            font-size: 14px;
            color: #666;
            margin-bottom: 8px;
        }

        .payment-amount {
            font-size: 24px;
            font-weight: bold;
            color: #222;
        }

        .refresh-notice {
            background: #e3f2fd;
            color: #1976d2;
            padding: 10px 15px;
            border-radius: 5px;
            font-size: 13px;
            text-align: center;
            margin-bottom: 20px;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .refresh-notice i {
            margin-right: 8px;
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
            <img src="${pageContext.request.contextPath}/images/logo.png" alt="SUT Shabu" style="width: 100px; margin-top: 10px;">
        </div>
        
        <a href="${pageContext.request.contextPath}/dashboard" class="nav-item active"><i class="fas fa-chart-line"></i> Dashboard</a>
        <a href="${pageContext.request.contextPath}/manager-menu" class="nav-item"><i class="fas fa-clipboard-list"></i> Menu Management</a>
        <a href="${pageContext.request.contextPath}/manager-staff" class="nav-item"><i class="fas fa-users-cog"></i> Staff Management</a>
        <a href="${pageContext.request.contextPath}/manager-member" class="nav-item"><i class="fas fa-users"></i> Member Management</a>
        <a href="${pageContext.request.contextPath}/manager-reward" class="nav-item"><i class="fas fa-gamepad"></i>Reward & Game Management</a>
        <a href="${pageContext.request.contextPath}/logout" class="nav-item logout-btn" onclick="return confirm('ยืนยันออกจากระบบ?');"><i class="fas fa-sign-out-alt"></i> ออกจากระบบ</a>
    </nav>

    <main class="main-content">
        <div class="header">
            <h2>Dashboard</h2>
        </div>

        <div class="content-body">
           <!--<div class="refresh-notice">
                <i class="fas fa-sync-alt"></i>
                หน้านี้จะรีเฟรชข้อมูลอัตโนมัติทุก 10 วินาที
            </div>--> 
            <div class="highlight-grid">
                <div class="stat-card big-card">
                    <div class="stat-header">
                        <div>
                            <div class="stat-title">รายได้วันนี้</div>
                            <div class="stat-value">฿${todayRevenue}</div>
                            <div class="stat-subtitle">บาท</div>
                        </div>
                        <div class="stat-icon icon-revenue">
                            <i class="fas fa-money-bill-wave"></i>
                        </div>
                    </div>
                </div>

                <!-- โต๊ะทั้งหมด -->
                <div class="stat-card big-card">
                    <div class="stat-header">
                        <div>
                            <div class="stat-title">โต๊ะทั้งหมด</div>
                            <div class="stat-value">${totalTables}</div>
                            <div class="stat-subtitle">ว่าง: ${availableTables} | ไม่ว่าง: ${occupiedTables}</div>
                        </div>
                        <div class="stat-icon icon-table">
                            <i class="fas fa-chair"></i>
                        </div>
                    </div>
                </div>
            </div>

            <div class="dashboard-grid">
                <!-- รายได้เดือนนี้ -->
                <div class="stat-card">
                    <div class="stat-header">
                        <div>
                            <div class="stat-title">รายได้เดือนนี้</div>
                            <div class="stat-value">฿${monthRevenue}</div>
                            <div class="stat-subtitle">
                                <c:choose>
                                    <c:when test="${revenueGrowth >= 0}">
                                        <span class="growth-positive"><i class="fas fa-arrow-up"></i> +${revenueGrowthText}%</span> จากเดือนที่แล้ว
                                    </c:when>
                                    <c:otherwise>
                                        <span class="growth-negative"><i class="fas fa-arrow-down"></i> ${revenueGrowthText}%</span> จากเดือนที่แล้ว
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        <div class="stat-icon icon-revenue">
                            <i class="fas fa-chart-line"></i>
                        </div>
                    </div>
                </div>

                <!-- อัตราการใช้โต๊ะ 
                <div class="stat-card">
                    <div class="stat-header">
                        <div>
                            <div class="stat-title">อัตราการใช้โต๊ะ</div>
                            <div class="stat-value">${occupancyRate}%</div>
                            <div class="stat-subtitle">จากโต๊ะทั้งหมด</div>
                        </div>
                        <div class="stat-icon icon-table">
                            <i class="fas fa-percentage"></i>
                        </div>
                    </div>
                </div>-->

                <!-- สมาชิกทั้งหมด -->
                <div class="stat-card">
                    <div class="stat-header">
                        <div>
                            <div class="stat-title">สมาชิกทั้งหมด</div>
                            <div class="stat-value">${totalMembers}</div>
                            <div class="stat-subtitle">สมาชิกใหม่เดือนนี้: ${newMembers} คน</div>
                        </div>
                        <div class="stat-icon icon-member">
                            <i class="fas fa-users"></i>
                        </div>
                    </div>
                </div>

                <!-- พนักงาน -->
                <div class="stat-card">
                    <div class="stat-header">
                        <div>
                            <div class="stat-title">พนักงานทั้งหมด</div>
                            <div class="stat-value">${totalStaff}</div>
                            <div class="stat-subtitle">Requests รอ: ${pendingRequests}</div>
                        </div>
                        <div class="stat-icon icon-staff">
                            <i class="fas fa-user-tie"></i>
                        </div>
                    </div>
                </div>

                <!-- Rewards แลกวันนี้ -->
                <div class="stat-card">
                    <div class="stat-header">
                        <div>
                            <div class="stat-title">Rewards แลกวันนี้</div>
                            <div class="stat-value">${todayRedemptions}</div>
                            <div class="stat-subtitle">รางวัลที่ถูกแลก</div>
                        </div>
                        <div class="stat-icon icon-reward">
                            <i class="fas fa-gift"></i>
                        </div>
                    </div>
                </div>
            </div>

            <!-- เมนูยอดนิยม -->
            <div class="section-title">
                <i class="fas fa-fire"></i> เมนูยอดนิยม (Top 5)
            </div>
            <div class="table-wrapper">
                <c:choose>
                    <c:when test="${not empty topMenus}">
                        <ul class="top-items-list">
                            <c:forEach var="menu" items="${topMenus}">
                                <li>
                                    <span class="item-name">${menu.name}</span>
                                    <span class="item-count">${menu.count} ครั้ง</span>
                                </li>
                            </c:forEach>
                        </ul>
                    </c:when>
                    <c:otherwise>
                        <p style="text-align: center; color: #999; padding: 20px;">ยังไม่มีข้อมูล</p>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Rewards ยอดนิยม -->
            <div class="section-title">
                <i class="fas fa-trophy"></i> Rewards ยอดนิยม (Top 3)
            </div>
            <div class="table-wrapper">
                <c:choose>
                    <c:when test="${not empty topRewards}">
                        <ul class="top-items-list">
                            <c:forEach var="reward" items="${topRewards}">
                                <li>
                                    <span class="item-name">${reward.name}</span>
                                    <span class="item-count">${reward.count} ครั้ง</span>
                                </li>
                            </c:forEach>
                        </ul>
                    </c:when>
                    <c:otherwise>
                        <p style="text-align: center; color: #999; padding: 20px;">ยังไม่มีข้อมูล</p>
                    </c:otherwise>
                </c:choose>
            </div>

            

        </div>
    </main>

</body>
</html>
