const event = {

    // 클릭 이벤트 연결
    init : function () {
        const _this = this;

        // 글 작성
        $('#board-write').on('click', function () {
            _this.eventWrite();
        });

        // 글 수정
        $('#board-modify').on('click', function () {
            _this.eventModify();
        });

        // 글 삭제
        $('#board-delete').on('click', function () {
            _this.eventDelete();
        })

    },

    // 이벤트 작성
    eventWrite : function () {
        const data = {
            title: $('#title').val(),
            content: $('#content').val()
        };

        $.ajax({
            data: JSON.stringify(data),
            type: 'POST',
            url: '/api/eventwrite',
            dataType : 'JSON',
            contentType:'application/json; charset=utf-8'
        }).done(function () {
            alert('글 작성 완료');
            window.location.href = '/event';
        }).fail(function (err) {
            alert(JSON.stringify(err));
        });
    },

    // 이벤트 글 수정
    eventModify : function () {
        const data = {
            title: $('title').val(),
            content: $('#content').val()
        };

        $.ajax({
            data: JSON.stringify(data),
            type: 'PUT',
            url: 'api/eventmodify',
            dataType: 'JSON',
            contentType: 'application/json; charset=utf-8'
        }).done(function() {
            alert('글 수정 완료');
            window.location.href = '/event';
        }).fail(function (err) {
            alert(JSON.stringify(err));
        });
    },

    // 이벤트 글 삭제
    eventDelete : function () {
        const data = {
            e_id: $('e_id').val()
        };

        $.ajax({
            data: JSON.stringify(data),
            type: 'DELETE',
            url: 'api/eventremove',
            dataType: 'JSON',
            contentType: 'application/json; charset=utf-8'
        }).done(function () {
            alert('글 삭제 완료');
            window.location.href = '/event';
        }).fail(function (err) {
            alert(JSON.stringify(err));
        });
    }
}

event.init();