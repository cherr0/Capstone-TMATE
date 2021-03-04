const board = {

    // 클릭 이벤트 연결
    init : function () {
        const _this = this;
        // 글 작성
        $('#notice-write').on('click', function () {
            _this.noticeWrite();
        });

        // 글 작성 화면 이동
        $('#notice-write-move').on('click', function () {
            _this.noticeWriMove();
        })

        // 글 수정
        $('#notice-modify').on('click', function () {
            _this.noticeModify();
        });

        // 글 수정 화면 이동
        $('#notice-modify-move').on('click', function () {
            _this.noticeModMove();
        });

        // 글 삭제
        $('#notice-remove').on('click', function () {
            _this.noticeRemove();
        });

        // 글 목록
        $('#notice-list').on('click', function () {
           _this.noticeList();
        });

    },

    // 공지 글 작성
    noticeWrite : function () {
        const data = {
            bd_title: $('#notice-write-title').val(),
            bd_contents: $('#notice-write-content').val(),
            bd_status: $('#notice-write-status').text(),
            m_id: $('#m_id').val()
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

    // 공지 글 작성 화면 이동
    noticeWriMove : function () {
        window.location.href = '/noticewrite';
    },

    // 공지 글 수정
    noticeModify : function () {
        const data = {
            bd_id: $('#notice-modify-id').val(),
            bd_title: $('#notice-modify-title').val(),
            bd_contents: $('#notice-modify-content').val(),
            bd_status: $('#notice-modify-status').val()
        };

        $.ajax({
            data: JSON.stringify(data),
            type: 'PUT',
            url: '/api/noticemodify',
            dataType: 'JSON',
            contentType: 'application/json; charset=utf-8'
        }).done(function () {
            alert("글 수정 완료");
            window.location.href = '/notice';
        }).fail(function (err) {
            alert(JSON.stringify(err));
        });
    },

    // 글 수정 화면 이동
    noticeModMove : function () {
      const bd_id = $('#bd_id').val();

      window.location.href = '/noticemodify/' + bd_id;
    },


    // 공지 글 삭제
    noticeRemove : function () {
        const data = {
            bd_id: $('#bd_id').val()
        };
        console.log(data.bd_id);

        $.ajax({
            data: data,
            type: 'DELETE',
            url: '/api/noticeremove',
        }).done(function () {
            alert("글 삭제 완료");
            window.location.href = '/notice';
        }).fail(function (err) {
            alert(JSON.stringify(err));
        });
    },

    // 공지 리스트 돌아가기
    noticeList : function () {
        window.location.href = "/notice";
    }
}

board.init();