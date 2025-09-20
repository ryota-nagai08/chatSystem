document.addEventListener('DOMContentLoaded', function () {
    const calendarEl = document.getElementById('calendar');
    const popup = document.getElementById('popup-form');
    const form = document.getElementById('form');

    const calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        selectable: true,
        events: 'http://localhost:8080/api/events',
        dateClick: function (info) {
            popup.style.left = info.jsEvent.pageX + 'px';
            popup.style.top = info.jsEvent.pageY + 'px';
            popup.style.display = 'block';

            document.getElementById('title').value = '';
            document.getElementById('start').value = info.dateStr + 'T09:00';
            document.getElementById('end').value = info.dateStr + 'T10:00';
            document.getElementById('description').value = '';
        },
        eventClick: function (info) {
            const start = info.event.start ? info.event.start.toLocaleString('ja-JP') : "なし";
            const end = info.event.end ? info.event.end.toLocaleString('ja-JP') : "なし";

            alert(
                "タイトル: " + info.event.title + "\n" +
                "詳細: " + (info.event.extendedProps.description || "なし") + "\n" +
                "開始時間: " + start + "\n" +
                "終了時間: " + end
            );
        }
    });

    form.addEventListener('submit', function (e) {
        e.preventDefault();
        const data = {
            title: document.getElementById('title').value,
            startTime: document.getElementById('start').value,
            endTime: document.getElementById('end').value,
            description: document.getElementById('description').value
        };

        const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
        const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

        fetch('http://localhost:8080/api/events', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                [csrfHeader]: csrfToken
            },
            body: JSON.stringify(data)
        }).then(() => {
            calendar.refetchEvents();
            hidePopup();
        });
    });

    calendar.render();
});

// ポップアップを閉じる関数（グローバル化）
function hidePopup() {
    document.getElementById('popup-form').style.display = 'none';
}

// 外部クリックでフォームを閉じる
window.addEventListener('click', function (e) {
    const popup = document.getElementById('popup-form');
    if (e.target.closest('#popup-form') === null && e.target.closest('.fc-daygrid-day') === null) {
        popup.style.display = 'none';
    }
});
