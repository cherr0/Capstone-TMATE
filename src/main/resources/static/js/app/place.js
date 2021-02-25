const place = {

    // 클릭 이벤트 연결
    init : function () {
        const _this = this;

        $('hotplace-remove').on('click',function () {
           _this.placeRemove();
        });
    },

    // 핫플레이스 삭제
    placeRemove : function () {
        const data = {
            pl_id: $('#pl_id').val()
        };

        $.ajax({
           data: JSON.stringify(data),
           type: 'DELETE',
           url: '/api/hotplaceremove',
           contentType: 'application/json; charset=utf-8'
        }).done(function () {
            alert('핫플레이스 삭제 완료');
            window.location.href = '/hotplace';
        }).fail(function (err) {
            alert(JSON.stringify(err));
        });
    }
}