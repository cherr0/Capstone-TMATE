const event = {

    // 클릭 이벤트 연결
    init : function () {
        const _this = this;

        // 글 작성
        $('#event-write').on('click', function () {
            _this.eventWrite();
        });

        // 글 수정
        $('#event-modify').on('click', function () {
            _this.eventModify();
        });

        // 글 수정 화면 이동
        $('#event-modify-move').on('click', function () {
            _this.eventModMove();
        });

        // 글 비공개 처리
        $('#event-remove').on('click', function () {
            _this.eventRemove();
        });

        // 리스트 이동
        $('#event-list').on('click', function () {
            _this.eventList();
        });

        // 캘린더 활성화
        $('#event-time-start').datetimepicker();
        $('#event-time-end').datetimepicker({
            useCurrent: false
        });

        // 셀렉터 활성화
        $('.selectpicker').selectpicker({
            style: 'btn-info',
            size: 4
        });

    },

    // 이벤트 작성
    eventWrite : function () {
        const data = {
            e_kind: $('#event-write-kind').val(),
            e_contents: $('#event-write-content').val(),
            e_s_date: new Date($('#event-time-start').val()),
            e_e_date: new Date($('#event-time-end').val())
        };

        $.ajax({
            data: JSON.stringify(data),
            type: 'POST',
            url: '/api/eventwrite',
            dataType : 'JSON',
            contentType:'application/json; charset=utf-8',
            success : function () {
                swal({
                    title: "글 작성 성공",
                    text: "작성이 완료되었습니다.",
                    icon: "success"
                }).then(() => {
                    window.location.href = "/event";
                });

            },
            error : function () {
                swal({
                    title: "글 작성 실패",
                    text: "어디 빠진 부분이 있는 것 같습니다.",
                    icon: "error"
                });
            }
        });
    },

    // 이벤트 글 수정
    eventModify : function () {
        const data = {
            e_id: $('#e_id').val(),
            e_kind: $('#event-modify-kind').val(),
            e_contents: $('#event-modify-content').val(),
            e_s_date: new Date($('#event-time-start').val()),
            e_e_date: new Date($('#event-time-end').val())
        };

        $.ajax({
            data: JSON.stringify(data),
            type: 'PUT',
            url: '/api/eventmodify',
            dataType: 'JSON',
            contentType: 'application/json; charset=utf-8'
        }).done(function () {
            swal({
                title: "글 수정 성공",
                text: "수정이 완료되었습니다.",
                icon: "success"
            }).then(() => {
                window.location.href = "/event";
            });
        }).fail(function (err) {
            alert(err);
        })
    },

    // 이벤트 글 수정 화면 이동
    eventModMove : function () {
        const e_id = $('#e_id').val();
        window.location.href = '/eventmodify/' + e_id;
    },

    // 이벤트 글 비공개 처리
    eventRemove : function () {
        e_id = $('#e_id').val()

        $.ajax({
            data: e_id,
            type: 'PUT',
            url: '/api/eventremove/' + e_id
        }).done(function () {
            swal({
                title: "글 삭제 성공",
                text: "삭제가 완료되었습니다.",
                icon: "success"
            }).then(() => {
                window.location.href = "/event";
            });
        }).fail(function (err) {
            swal({
                title: "글 삭제 실패",
                text: JSON.stringify(err),
                icon: "success"
            });
        });
    },


    // 이벤트 리스트 이동
    eventList : function () {
        window.location.href = "/event";
    },
}

event.init();
