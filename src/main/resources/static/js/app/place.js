const place = {

    // 클릭 이벤트 연결
    init : function () {
        const _this = this;
        console.log('place 클릭 이벤트 연결');


    },

    // 핫플레이스 삭제
    placeRemove : function (event) {
        const pl_id = event.find('.pl_id').val();

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
                window.location.href = "/hotplace";
            })

        }).fail(function (err) {
            alert(JSON.stringify(err));
        });

    },

    placeRegister : function (event) {

        const data = {
            pl_id: event.find('.infowindow_id').val(),
            pl_address: event.find('.infowindow_address').html(),
            pl_name: event.find('.infowindow_title').html(),
            pl_lat: event.find('.infowindow_lat').val(),
            pl_lng: event.find('.infowindow_lng').val()
        }

        console.log('데이터 넘어옴');
        console.log(data);

        $.ajax({
            data: JSON.stringify(data),
            type: 'POST',
            url: '/api/hotplaceregister',
            dataType: 'JSON',
            contentType: 'application/json; charset=utf-8',
            success : function (result) {
                console.log(result);
                swal({
                    title: "등록 성공",
                    text: "핫플레이스 등록이 완료되었습니다.",
                    icon: "success"
                }).then(() => {
                    window.location.href = "/hotplace";
                })
            }
        })


    }
}

place.init();