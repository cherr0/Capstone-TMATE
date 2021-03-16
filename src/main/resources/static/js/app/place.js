const place = {

    // 클릭 이벤트 연결
    init : function () {
        const _this = this;
        console.log('place 클릭 이벤트 연결');


    },

    // 핫플레이스 삭제
    placeRemove : function (event) {
        const pl_id = event.find('.infowindow_content').html();

        $.ajax({
           data: pl_id,
           type: 'DELETE',
           url: '/api/hotplaceremove/' + pl_id,
           contentType: 'application/json; charset=utf-8'
        }).done(function () {
            swal({
                title: "삭제 성공",
                text: "핫플레이스 삭제가 완료되었습니다.",
                icon: "success"
            }).then(() => {
                window.location.href = '/hotplace';
            })

        }).fail(function (err) {
            alert(JSON.stringify(err));
        });
    }
}

place.init();