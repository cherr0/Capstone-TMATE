const member = {

    // 이벤트 연결
    init : function () {
        const _this = this;

        // form-head 클릭
        $('.form-head').on('click',function () {
            console.log('form-head onclick() use');
            _this.formHead();
        });

        // 로그인 다음창 넘어가기
        $('.form-action').on('click', function () {
            let form_id = $('.grop-from').attr('id');
            console.log('form-action onclick() use');

            switch(form_id) {
                case "name" :
                    _this.formAction();
                    break;
                case "phone" :
                    _this.sendNumber();
                    break;
                case "certNo" :
                    _this.checkConfirm();
                    break;
                default :
                    _this.formAction();
                    break;
            }
        });

        // 내용 채워넣는 경우 이벤트
        $('input').keyup(function(){
            _this.inputKeyup();
        });


        $("#myBtn").click(function(){
            $("#myModal").modal();
        });

    },

    // 인증번호 보내기
    sendNumber : function () {
        const _this = this;
        const data = {
            phoneNumber: $('#control-phone').val()
        }

        $.ajax({
            type: 'POST',
            url: '/api/sendSMS',
            data: JSON.stringify(data),
            dataType: 'JSON',
            contentType: 'application/json; charset=utf-8'
        }).done(function (res) {
            swal({
                title: "인증번호 전송 완료",
                text: "인증번호를 확인해주세요",
                icon: "success"
            });
            $('#confirm-number').val(res);
            _this.formAction();

        }).fail(function (err){
            swal({
                title: "전송에 실패했습니다.",
                text: err,
                icon: "error"
            });
        });
    },

    // 인증 확인
    checkConfirm : function() {
        const _this = this;
        const data = {
            name: $('#control-name').val(),
            phoneNumber: $('#control-phone').val(),
            confirm: $('#control-certNo').val()
        }

        $.ajax({
            type: 'POST',
            url: '/api/confirm',
            data: JSON.stringify(data),
            dataType: 'JSON',
            contentType: 'application/json',
            success : function (verify) {
                if(verify == 1) {
                    swal({
                        title: "인증 성공",
                        text: "휴대폰 인증에 성공하였습니다.",
                        icon: "success"
                    });
                    _this.formAction();
                }else {
                    swal({
                        title: "인증 실패",
                        text: "인증 번호를 다시 확인해주세요",
                        icon: "error"
                    });
                    $('#control-certNo').val('');
                }
            }
        });
    },

    // 폼 헤드(시작, 끝) 동작
    formHead : function () {
        console.log($('.form-head').closest('.grop-from').attr('id'));

        if($('.form-head').closest('.grop-from').attr('id')=='signup'){
            $('.grop-from').attr('id' , 'name');
            $('.icon-action').addClass('back');
        }
        else if($('.form-head').closest('.grop-from').attr('id')=='success'){

            const data = {
                phoneNumber: $('#control-phone').val(),
                name: $('#control-name').val()
            }

            $.ajax({
                type: 'POST',
                url: '/api/login',
                data: JSON.stringify(data),
                dataType: 'JSON',
                contentType: 'application/json',
                success : function (per) {
                    const permission = per.m_id.substr(0,1);
                    console.log("member permission : '" + permission + "'");

                    switch (permission) {
                        case 'c' :
                        case 'd' :
                            window.location.href = '/usermain';
                            break;
                        case 'a' :
                            window.location.href = '/main';
                            break;
                        default :
                            window.location.href = '/registerqrcode';
                    }
                }
            });
        }
    },

    // 로그인 중간과정 동작
    formAction : function () {
        let form_id = $('.grop-from').attr('id');
        if($('#control-' + form_id).val() !== '') {
            console.log("true");
            switch (form_id) {
                case "name" :
                    form_id = "phone"
                    break;
                case "phone" :
                    form_id = "certNo";
                    break;
                case "certNo" :
                    form_id = "success";
                    break;
            }
            $('.icon-action').addClass('back');
        } else {
            console.log("false");
            switch (form_id) {
                case "name" :
                    form_id = "signup";
                    $('.icon-action').removeClass('back');
                    break;
                case "phone" :
                    form_id = "name";
                    break;
                case "certNo" :
                    form_id = "phone";
                    break;
                case "success" :
                    form_id = "signup";
                    break;
            }
            $('.icon-action').removeClass('back');
        }

        $('.grop-from').attr('id' , form_id);
    },

    // 폼 null 값 확인해서 뒤로가기 추가
    inputKeyup : function () {

        $('.grop-from').removeClass('error');
        $('.error-text').fadeOut();

        let form_id = $('.grop-from').attr('id');

        if($('#control-' + form_id).val() !== ''){
            $('.icon-action').removeClass('back');
        }
        else{
            $('.icon-action').addClass('back');
        }
    }


};

member.init();