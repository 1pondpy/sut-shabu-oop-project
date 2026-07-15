<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reward & Game</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/menustyle.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <style>
        /* CSS เฉพาะหน้านี้ (Dark Theme) */
        .reward-container { background-color: #333; color: white; border-radius: 20px; padding: 30px; display: flex; height: 100%; width: 100%; gap: 30px; }
        .member-info { flex: 1; border-right: 1px solid #555; padding-right: 30px; display: flex; flex-direction: column; align-items: center; justify-content: center; text-align: center; margin-top: 30px; }
        .points-big { font-size: 64px; font-weight: bold; color: #fdd835; margin: 10px 0; }
        .game-box { border: 1px solid #555; border-radius: 15px; padding: 20px; width: 100%; margin-top: 30px; background-color: #444; }
        .btn-play { background-color: #fdd835; color: #333; border: none; padding: 10px 30px; border-radius: 20px; font-weight: bold; cursor: pointer; margin-top: 15px; }
        .reward-list-section { flex: 2; overflow-y: auto; }
        .reward-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; margin-top: 20px; }
        .reward-card { background-color: #424242; border-radius: 15px; padding: 12px; display: flex; align-items: center; gap: 12px; cursor: pointer; transition: 0.2s; }
        .reward-card:hover { transform: scale(1.02); background-color: #505050; }
        .reward-icon { font-size: 20px; color: #ddd; background: #555; width: 48px; height: 48px; display: flex; align-items: center; justify-content: center; border-radius: 10px; }
        .reward-details h3 { margin: 0; font-size: 18px; color: white; }
        .reward-details p { margin: 5px 0 0; color: #fdd835; font-size: 14px; }
        .modal { display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.7); align-items: center; justify-content: center; z-index: 1000; }
        .modal-content { background: #2c2c2c; color: white; padding: 30px; border-radius: 20px; text-align: center; width: 350px; }
        .modal-icon { background: #fdd835; color: #333; width: 60px; height: 60px; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 30px; margin: 0 auto 20px; }
        /* toast styles */
        .toast { opacity: 0; transform: translateY(10px); transition: opacity .25s ease, transform .25s ease; background:#222; color:#fff; padding:10px 14px; border-radius:10px; border:1px solid #555; }
        .toast.show { opacity: 1; transform: translateY(0); }
        .toast.ok { border-color:#3fb950; }
        .toast.error { border-color:#f85149; }
        .toast.info { border-color:#fdd835; }
        .icon-close {
    background: none;
    border: none;
    padding: 0;
    cursor: pointer;
    color: #999;
    font-size: 18px;
}



    </style>
</head>
<body>
    <nav class="sidebar">
        <div class="logo">
            <img src="${pageContext.request.contextPath}/images/logo.png" style="width: 100px; margin-top: 10px;" >
        </div>
        <a href="${pageContext.request.contextPath}/menu?tableNo=${not empty tableNo ? tableNo : param.tableNo}" class="nav-item" style="text-decoration: none;"><i class="fas fa-utensils"></i><span>Menu</span></a>
        <a href="${pageContext.request.contextPath}/my-orders?tableNo=${not empty tableNo ? tableNo : param.tableNo}" class="nav-item" style="text-decoration: none;"><i class="fas fa-clipboard-list"></i><span>My Orders</span></a>
        <a href="${pageContext.request.contextPath}/reward.jsp?tableNo=${not empty tableNo ? tableNo : param.tableNo}" class="nav-item active" style="text-decoration: none;"><i class="fas fa-gamepad"></i><span>Reward & Game</span></a>
    </nav>

    <div class="reward-area">
        <div class="reward-container">
            <c:choose>
                <c:when test="${not empty member}">
                    <div class="member-info">
                        <h2>${member.name}</h2>
                        <div style="font-size: 40px; color: gold; margin-top: 10px;">©</div>
                        <p>แต้มสะสม (POINTS)</p>
                        <div id="pointsBig" class="points-big">${member.points}</div>
                        <div class="game-box">
                            <h3>คุณมีสิทธิ์เล่นเกม <span id="gameChancesCount" style="color: gold; font-size: 24px;">${member.gameChances}</span> ครั้ง</h3>
                            <button id="btnPlay" class="btn-play" onclick="tryPlayGame()">เล่นเลย</button>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="member-info guest-view">
                        <div style="font-size: 60px; color: #777; margin-bottom: 20px;"><i class="fa-solid fa-user-slash"></i></div>
                        <h2 style="color: #ccc;">ยังไม่ได้เป็นสมาชิก</h2>
                        <p style="color: #888; margin-top: 10px;">สมัครสมาชิกวันนี้ เพื่อสะสมแต้ม<br/>และรับสิทธิ์เล่นเกมลุ้นรางวัล!</p>
                        <p style="margin-top: 20px; color:#fdd835; font-weight: bold; ">สมัครสมาชิก กรุณาแจ้งพนักงาน</p>
                    </div>
                </c:otherwise>
            </c:choose>

            <div class="reward-list-section">
                <div style="display:flex; align-items:center; gap:12px; margin-bottom: 12px;">
                    <div style="border-bottom: 2px solid gold; display: inline-block; padding-bottom: 5px;">รายการของรางวัล</div>
                    <c:choose>
                        <c:when test="${not empty member}">
                            <button id="btnHistory" onclick="openHistory()" style="margin-left:auto; background:transparent; border:1px solid #777; color:#fdd835; padding:8px 16px; border-radius:16px; cursor:pointer;">
                                <i class="fa-solid fa-clock-rotate-left"></i> ประวัติการแลก
                            </button>
                        </c:when>
                        <c:otherwise>
                            <button id="btnHistory" disabled title="สำหรับสมาชิกเท่านั้น" style="margin-left:auto; background:#444; border:1px solid #555; color:#888; padding:8px 16px; border-radius:16px; cursor:not-allowed;">
                                <i class="fa-solid fa-lock"></i> ประวัติการแลก
                            </button>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="reward-grid">
                    <c:forEach var="reward" items="${rewards}">
                        <div class="reward-card" onclick="openModal(this)">
                            <div class="reward-icon">%</div>
                            <div class="reward-details">
                                <h3>${reward.name}</h3>
                                <p>ใช้ ${reward.pointsRequired} แต้ม</p>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>

    <div id="confirmModal" class="modal">
        <div class="modal-content">
            <div class="modal-icon">i</div>
            <h3>ต้องการแลกสิทธิ์นี้?</h3>
            <h1 id="modalRewardName" style="margin: 20px 0;">ส่วนลด 10%</h1>
            <p style="color: #aaa; font-size: 12px;">กรุณาแจ้งกับพนักงานก่อนชำระเงิน<br/>เพื่อทำการแลกแต้มและใช้สิทธิ์</p>
            <button onclick="closeModal()" style="background:transparent; border:1px solid white; color:white; padding:10px 30px; border-radius:20px; margin-top:20px; cursor:pointer;">ตกลง</button>
        </div>
    </div>

    <div id="historyModal" class="modal">
    <div class="modal-content" style="width: 500px; max-height: 70vh; overflow:auto; text-align:left;">
        <div style="display:flex; align-items:center; justify-content:space-between; margin-bottom:10px;">
            <h3 style="margin:0;">ประวัติการแลก</h3>

            <button class="icon-close" onclick="closeHistory()" aria-label="Close" title="ปิด">
                <i class="fa-solid fa-xmark"></i>
            </button>
        </div>

        <div id="historyBody" style="display:flex; flex-direction:column; gap:10px;"></div>
    </div>
</div>


    <script>
        


        function openModal(element) {
            let rewardName = element.querySelector('h3').innerText;
            document.getElementById('modalRewardName').innerText = rewardName;
            document.getElementById('confirmModal').style.display = 'flex';
        }
        function closeModal() { document.getElementById('confirmModal').style.display = 'none'; }
       
        function showToast(message, type){
            const cont = document.getElementById('toastContainer');
            if(!cont){ alert(message); return; }
            const t = document.createElement('div');
            t.className = 'toast ' + (type||'');
            t.textContent = message;
            cont.appendChild(t);
            requestAnimationFrame(()=> t.classList.add('show'));
            setTimeout(()=>{ t.classList.remove('show'); t.addEventListener('transitionend', ()=> t.remove(), {once:true}); }, 3000);
        }

        function tryPlayGame() {
            const phone = '${not empty member ? member.phoneNumber : ""}';
            const tableNo = '${not empty tableNo ? tableNo : param.tableNo}';
            if (!phone) { alert('กรุณาเข้าสู่ระบบก่อนเล่นเกม'); return; }
            if(!confirm('เริ่มเล่นเกม? (จะใช้สิทธิ์ 1 ครั้ง)')) return;
            const btn = document.getElementById('btnPlay'); if(btn) btn.disabled = true;
            fetch('${pageContext.request.contextPath}/api/game/start?tableNo=' + encodeURIComponent(tableNo), {
                method: 'POST', headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ phone: phone })
            }).then(async res => {
                if(!res.ok){ throw new Error(await res.text() || 'start failed'); }
                const data = await res.json();
                window.__gameToken = data && data.token ? data.token : null;
                window.__gameSeed = data && typeof data.seed === 'number' ? data.seed : Math.floor(Math.random()*1e6);
                
                const c = document.getElementById('gameChancesCount');
                if(c){ const now = Math.max(0,(parseInt(c.textContent||'0',10)||0)-1); c.textContent = String(now); if(now<=0 && btn) btn.disabled = true; }
                openGameModal();
            }).catch(err => {
                if(btn) btn.disabled = false;
                showToast('เริ่มเกมไม่สำเร็จ: ' + (err && err.message ? err.message : err), 'error');
            });
        }
                const birdImg = new Image();
                birdImg.src = "${pageContext.request.contextPath}/images/angelbacon.png";

                const bgImg = new Image();
                bgImg.src = "${pageContext.request.contextPath}/images/ShabuBG black.png";

                const pipeTopImg = new Image();
                pipeTopImg.src = "${pageContext.request.contextPath}/images/top.png";

                const pipeBottomImg = new Image();
                pipeBottomImg.src = "${pageContext.request.contextPath}/images/bottom.png";

                const PIPE_WIDTH = 80;
                const BIRD_SIZE = 50;


        
        let canvas, ctx, gameLoop, countdownInterval;
        let rnd = () => Math.random();
        let finishSubmitted = false; 
        let bird = { x: 100, y: 150, velocity: 0, gravity: 0.25, jump: -4.5 };
        let pipes = [], frame = 0, score = 0, isGameOver = false, isGameRunning = false;

        function openGameModal() {
            document.getElementById('gameModal').style.display = 'flex';
            canvas = document.getElementById('gameCanvas'); ctx = canvas.getContext('2d'); startCountdown();
        }
        function closeGameModal() { document.getElementById('gameModal').style.display = 'none'; clearInterval(gameLoop); clearInterval(countdownInterval); location.reload(); }
        function startCountdown() { let count = 3; isGameRunning = false; 
        drawScene(); drawCenterText(count); countdownInterval = setInterval(() => { count--; if(count>0){ drawScene(); drawCenterText(count); } else if(count===0){ drawScene(); drawCenterText('GO!'); } else { clearInterval(countdownInterval); initGame(); } },1000); }
        function drawScene(){
            ctx.clearRect(0, 0, canvas.width, canvas.height);

            // background
            ctx.drawImage(bgImg, 0, 0, canvas.width, canvas.height);

            // bird
            ctx.drawImage(birdImg, bird.x, bird.y, 60, 60);
        }

        function drawCenterText(text){ ctx.fillStyle='white'; ctx.strokeStyle='black'; ctx.lineWidth=3; ctx.font='bold 60px Arial'; ctx.textAlign='center'; let x=canvas.width/2,y=canvas.height/2+20; ctx.strokeText(text,x,y); ctx.fillText(text,x,y); }
        function initGame(){
           
            rnd = createPRNG(window.__gameSeed || 123456);
            bird.y=150; bird.velocity=0; pipes=[]; score=0; frame=0; isGameOver=false; isGameRunning=true; clearInterval(gameLoop); gameLoop=setInterval(update,1000/60); canvas.onmousedown=jump; document.onkeydown=(e)=>{ if(e.code==='Space') jump(); };
        }
        function jump(){ if(!isGameRunning||isGameOver) return; bird.velocity=bird.jump; }
        
        function update(){
            bird.velocity+=bird.gravity; bird.y+=bird.velocity;
            if(frame%100===0){ let gap=140; let topHeight=rnd()*(canvas.height-gap-100)+50; pipes.push({x:canvas.width,top:topHeight,gap:gap,passed:false}); }
            // Background darker for contrast
            ctx.drawImage(bgImg,0,0,canvas.width,canvas.height);
            // Pipes
            pipes.forEach(p => {
            p.x -= 2;

            //  ท่อบน
            ctx.drawImage(
                pipeTopImg,
                p.x,
                0,
                PIPE_WIDTH,
                p.top
            );

            // ท่อล่าง
            const bottomY = p.top + p.gap;
            const bottomHeight = canvas.height - bottomY;
            ctx.drawImage(
                pipeBottomImg,
                p.x,
                bottomY,
                PIPE_WIDTH,
                bottomHeight
            );

            if (
                bird.x + 40 > p.x &&
                bird.x < p.x + PIPE_WIDTH &&
                (bird.y < p.top || bird.y + 40 > p.top + p.gap)
            ) {
                gameOver();
            }

            // score
            if (p.x + PIPE_WIDTH < bird.x && !p.passed) {
                score++;
                p.passed = true;
            }
        });


            // Bird
            //ctx.fillStyle='#fbbf24';
            //ctx.fillRect(bird.x,bird.y,20,20);
            ctx.drawImage(birdImg, bird.x, bird.y, 60, 60);

            // Score text
            ctx.fillStyle='#ffffff'; ctx.strokeStyle='#000000'; ctx.lineWidth=2; ctx.font='bold 30px Arial'; ctx.textAlign='left';
            ctx.strokeText(score,canvas.width/2-10,50); ctx.fillText(score,canvas.width/2-10,50);
            if(bird.y+20>canvas.height||bird.y<0) gameOver();
            frame++;
        }
        function createPRNG(seed){
            let s = (seed>>>0) || 1;
            return function(){
                s = (s + 0x6D2B79F5) | 0;
                let t = Math.imul(s ^ (s >>> 15), 1 | s);
                t ^= t + Math.imul(t ^ (t >>> 7), 61 | t);
                return ((t ^ (t >>> 14)) >>> 0) / 4294967296;
            };
        }
        function gameOver(){
            clearInterval(gameLoop);
            isGameOver=true; isGameRunning=false;
            ctx.fillStyle='rgba(0,0,0,0.5)'; ctx.fillRect(0,0,canvas.width,canvas.height);
            drawCenterText('Game Over');
            ctx.font='20px Arial'; ctx.fillStyle='white';
            ctx.fillText('Score: '+score,canvas.width/2,canvas.height/2+60);
            submitGameFinish();
        }

        async function submitGameFinish(){
            if(finishSubmitted) return;
            finishSubmitted = true;
            try {
                const token = window.__gameToken;
                if(!token){ throw new Error('missing token'); }
                const res = await fetch('${pageContext.request.contextPath}/api/game/finish', {
                    method: 'POST', headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ token: token, score: score })
                });
                if(!res.ok){ throw new Error(await res.text() || 'finish failed'); }
                const data = await res.json();
                const earned = data && typeof data.pointsEarned === 'number' ? data.pointsEarned : 0;
                if(earned>0){
                    const el = document.getElementById('pointsBig');
                    if(el){
                        const current = parseInt(el.textContent || '0', 10) || 0;
                        el.textContent = String(current + earned);
                    }
                    showToast('ได้แต้มเพิ่ม ' + earned + ' แต้ม', 'ok');
                } else {
                    showToast('รอบนี้ไม่ได้แต้มเพิ่ม', 'info');
                }
            } catch (e){
                console.warn('submit finish error:', e);
            }
        }

        // Redemption history
        function openHistory(){
            const tableNo = '${not empty tableNo ? tableNo : param.tableNo}';
            fetch('${pageContext.request.contextPath}/api/rewards/history?tableNo=' + encodeURIComponent(tableNo))
                .then(r => {
                    if(!r.ok){ return r.text().then(t=>{ throw new Error(t||'Forbidden'); }); }
                    return r.json();
                })
                .then(rows => {
                    const body = document.getElementById('historyBody');
                    body.innerHTML = '';
                    if(!rows || rows.length===0){
                        body.innerHTML = '<div style="color:#aaa; text-align:center;">ยังไม่มีประวัติการแลก</div>';
                    } else {
                        rows.forEach(h => {
                            const item = document.createElement('div');
                            item.style.border = '1px solid #555';
                            item.style.borderRadius = '12px';
                            item.style.padding = '10px 12px';
                            item.style.background = '#3a3a3a';
                            const dateLabel = formatDateLabel(h.redeemDate);
                            item.innerHTML = '<div style="display:flex; justify-content:space-between; align-items:center;">'
                                + '<div><div style="color:#fff; font-weight:600;">' + escapeHtml(h.rewardName) + '</div>'
                                + '<div style="color:#bbb; font-size:12px;">ใช้ ' + (h.pointsUsed||0) + ' แต้ม</div></div>'
                                + '<div style="color:#fdd835; font-size:12px;">' + dateLabel + '</div>'
                                + '</div>';
                            body.appendChild(item);
                        });
                    }
                    document.getElementById('historyModal').style.display = 'flex';
                })
                .catch(e => { alert('ไม่สามารถดึงประวัติได้: ' + e.message); });
        }
        function closeHistory(){ document.getElementById('historyModal').style.display='none'; }
        function escapeHtml(s){ return (s||'').replace(/[&<>"]/g, c=>({"&":"&amp;","<":"&lt;",">":"&gt;","\"":"&quot;"}[c])); }
        function formatDateLabel(iso){
            if(!iso) return '';
            try {
                const d = (''+iso).substring(0,10); 
                const today = new Date().toISOString().substring(0,10);
                return d === today ? 'วันนี้' : d;
            } catch(e){ return ''; }
        }
    </script>

    <div id="gameModal" class="modal-overlay" style="display:none;">
        <div class="modal-box" style="width:700px; max-width:90vw; text-align:center; background:#333;">
            <div class="modal-header" style="justify-content:center; border:none;"><h3 style="color:#fdd835;">FLAPPY SHABU </h3></div>
            <canvas id="gameCanvas" width="640" height="360" style="background:#0f172a; border-radius:12px; cursor:pointer; width:100%; height:auto;"></canvas>
            <div style="margin-top:20px;"><button onclick="closeGameModal()" style="background:#555; color:white; border:none; padding:10px 20px; border-radius:20px; cursor:pointer;">ออกจากเกม</button></div>
        </div>
    </div>

  
    <div id="toastContainer" style="position:fixed; bottom:16px; left:50%; transform:translateX(-50%); z-index:2000; display:flex; flex-direction:column; gap:8px;"></div>

</body>
</html>
