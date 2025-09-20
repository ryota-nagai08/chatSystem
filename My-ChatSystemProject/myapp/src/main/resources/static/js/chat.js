let stompClient = null;

document.getElementById('createRoomForm').addEventListener('submit', function (event) {
    event.preventDefault();

    const csrfParameterName = document.querySelector('meta[name="_csrf_parameter"]').getAttribute('content');
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
    const name = document.getElementById('roomName').value;

    fetch('/api/chatrooms', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            [csrfHeader]: csrfToken
        },
        body: JSON.stringify({ name })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('ルーム作成に失敗しました');
            }
            return response.json();
        })
        .then(data => {
            document.getElementById('result').textContent =
                `作成完了！[${data.name}]`;

            //新しいルームを追加する
            const newRoom = document.createElement("li");
            newRoom.classList.add("list-factor");

            const newLink = document.createElement("a");
            newLink.href = `/home/chatrooms/${data.id}`;
            newLink.textContent = data.name;
            newLink.style.textDecoration = "none";

            // 新しい削除フォームを作成
            const deleteForm = document.createElement("form");
            deleteForm.action = `/delete/chatrooms/${data.id}`;
            deleteForm.method = "post";
            deleteForm.onsubmit = function () {
                return confirm('本当に削除しますか？');
            };

            const csrfInput = document.createElement("input");
            csrfInput.type = "hidden";
            csrfInput.name = csrfParameterName;
            csrfInput.value = csrfToken;
            deleteForm.appendChild(csrfInput);

            // 削除ボタン作成
            const deleteButton = document.createElement("button");
            deleteButton.type = "submit";
            deleteButton.textContent = "削除";
            deleteForm.appendChild(deleteButton);

            newRoom.appendChild(newLink);
            newRoom.appendChild(deleteForm);

            const roomList = document.querySelector(".room-list ul");
            roomList.appendChild(newRoom);

            // 入力フォームをクリア
            document.getElementById('roomName').value = '';

            loadHistory(data.id);
            setupSocket(data.id);
        })
        .catch(error => {
            document.getElementById('result').textContent = error.message;
        });
});

// 履歴取得
function loadHistory(roomId) {
    fetch(`/api/chatrooms/${roomId}/messages`)
        .then(res => res.json())
        .then(messages => {
            const chatBox = document.getElementById('chat-box');
            chatBox.innerHTML = '';
            messages.forEach(msg => {
                addMessage(msg);
            });
        });
}

// WebSocketセットアップ
function setupSocket(roomId) {
    const socket = new SockJS("/websocket");
    stompClient = Stomp.over(socket);
    console.log("roomId:", roomId);

    stompClient.connect({}, () => {
        stompClient.subscribe(`/topic/chat/${roomId}`, (message) => {
            const msg = JSON.parse(message.body);
            addMessage(msg);
        });
    });
}

// チャット画面の初期化
const chatContainer = document.getElementById("chat-container");
const roomId = chatContainer.dataset.roomId;
const currentUserId = Number(chatContainer.dataset.userId);

if (roomId) {
    loadHistory(roomId);
    setupSocket(roomId);
}


// メッセージを表示（左右分け＋画像）
function addMessage(msg) {
    const div = document.createElement('div');
    div.classList.add('message');

    if (msg.userId == currentUserId) {
        div.classList.add('my-message');
    } else {
        div.classList.add('other-message');
    }

    // ユーザ名部分
    const usernameP = document.createElement('p');
    usernameP.textContent = msg.username;
    usernameP.style.fontWeight = "bold";

    // メッセージ部分
    const contentP = document.createElement('p');
    contentP.textContent = msg.content;

    div.appendChild(usernameP);
    div.appendChild(contentP);

    if (msg.imageUrl) {
        const link = document.createElement('a');
        link.href = msg.imageUrl;
        link.target = "_blank";  // クリックで別タブ

        const img = document.createElement('img');
        img.src = msg.imageUrl;
        img.style.maxWidth = "200px";
        img.style.marginTop = "5px";

        link.appendChild(img);
        div.appendChild(link);

        img.onload = () => {
            const chatBox = document.getElementById('chat-box');
            chatBox.scrollTop = chatBox.scrollHeight;
        };
    }

    const chatBox = document.getElementById('chat-box');
    chatBox.appendChild(div);
    if (!msg.imageUrl) {
        chatBox.scrollTop = chatBox.scrollHeight;
    }
}

// メッセージ送信（画像付き）
const form = document.querySelector(".chat-form");

form.onsubmit = async function (e) {
    e.preventDefault();
    const input = this.querySelector("input[type='text']");
    const imageInput = document.getElementById("imageInput");
    const content = input.value;
    const imageFile = imageInput.files[0];

    let imageUrl = null;

    if (imageFile) {
        const formData = new FormData();
        formData.append("file", imageFile);
        formData.append("upload_preset", "myapp2_preset");

        const res = await fetch("https://api.cloudinary.com/v1_1/dobgkbhbs/image/upload", {
            method: "POST",
            body: formData
        });

        const data = await res.json();
        imageUrl = data.secure_url;
    }

    stompClient.send("/app/chat/send", {}, JSON.stringify({ roomId, content, imageUrl }));

    input.value = "";
    imageInput.value = "";
};
