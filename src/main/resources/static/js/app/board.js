const board = {

    // 클릭 이벤트 연결
    init : function () {
        const _this = this;
        // 글 작성
        $('#notice-write').on('click', function () {
            _this.noticeWrite();
        });

        // 글 수정
        $('#notice-modify').on('click', function () {
            _this.noticeModify();
        });

        // 글 삭제
        $('#notice-delete').on('click', function () {
            _this.noticeDelete();
        })
    },

    // 공지 글 작성
    noticeWrite : function () {
        const data = {
            title: $('#title').val(),
            content: $('#content').val()
        };

        $.ajax({
            data: JSON.stringify(data),
            type: 'POST',
            url: '/api/noticewrite',
            dataType : 'JSON',
            contentType:'application/json; charset=utf-8'
        }).done(function () {
            alert("글 등록 완료");
            window.location.href = '/notice';
        }).fail(function (err) {
            alert(JSON.stringify(err));
        });
    },

    // 공지 글 수정
    noticeModify : function () {
        const data = {
            title: $('#title').val(),
            content: $('#content').val()
        };

        $.ajax({
            data: JSON.stringify(data),
            type: 'PUT',
            url: 'api/noticemodify',
            dataType: 'JSON',
            contentType: 'application/json; charset=utf-8'
        }).done(function () {
            alert("글 수정 완료");
            window.location.href = '/notice';
        }).fail(function (err) {
            alert(JSON.stringify(err));
        });
    },


    // 공지 글 삭제
    noticeDelete : function () {
        const data = {
            bd_id: $('#bd_id').val()
        }

        $.ajax({
            data: JSON.stringify(data),
            type: 'DELETE',
            url: 'api/noticedelete',
            dataType: 'JSON',
            contentType: 'application/json; charset=utf-8'
        }).done(function () {
            alert("글 삭제 완료");
            window.location.href = '/notice';
        }).fail(function (err) {
            alert(JSON.stringify(err));
        });
    }
}

board.init();