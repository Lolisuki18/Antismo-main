let stompClient = null;
let authToken  = null;

document.addEventListener('DOMContentLoaded', () => {
    // 1) Xử lý form Đăng nhập
    const loginForm = document.getElementById('loginForm');
    loginForm.addEventListener('submit', async e => {
        e.preventDefault();
        const email    = document.getElementById('email').value.trim();
        const password = document.getElementById('password').value.trim();
        if (!email || !password) {
            return alert('Vui lòng nhập email và mật khẩu');
        }
        try {
            const res = await fetch('/api/signin', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email, password })
            });
            if (!res.ok) throw new Error('Đăng nhập thất bại');
            const data = await res.json();
            authToken = data.token;
            // Show chat, hide login
            document.getElementById('login-page').classList.add('hidden');
            document.getElementById('chat-page').classList.remove('hidden');
            // Mở websocket
            connectWebSocket();
        } catch (err) {
            console.error(err);
            alert(err.message);
        }
    });

    // 2) Gửi message khi click nút Send
    document.getElementById('send').addEventListener('click', () => {
        const txt = document.getElementById('message').value.trim();
        if (txt && stompClient) {
            stompClient.send('/app/chat.sendMessage', {}, JSON.stringify({ content: txt }));
            document.getElementById('message').value = '';
        }
    });
});

function connectWebSocket() {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect(
        { Authorization: 'Bearer ' + authToken }, // header JWT
        () => {
            // subscribe lịch sử và realtime
            stompClient.subscribe('/user/queue/history', onHistoryReceived);
            stompClient.subscribe('/topic/public',   onMessageReceived);
            // thông báo user mới (server sẽ lấy username từ token)
            stompClient.send('/app/chat.addUser', {}, {});
        },
        err => console.error('WebSocket error:', err)
    );
}

function onHistoryReceived(payload) {
    JSON.parse(payload.body).forEach(showMessage);
    scrollToBottom();
}

function onMessageReceived(payload) {
    showMessage(JSON.parse(payload.body));
    scrollToBottom();
}

function showMessage(msg) {
    const area = document.getElementById('messageArea');
    const li   = document.createElement('li');
    if (msg.type === 'JOIN' || msg.type === 'LEAVE') {
        li.className   = 'event-message';
        li.textContent = `${msg.sender} ${msg.type === 'JOIN' ? 'joined' : 'left'}`;
    } else {
        li.textContent = `${msg.sender}: ${msg.content}`;
    }
    area.appendChild(li);
}

function scrollToBottom() {
    const area = document.getElementById('messageArea');
    area.scrollTop = area.scrollHeight;
}
