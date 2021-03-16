const approval = {




    // 클릭 이벤트 연결
    init : function () {
        const _this = this;

        // 관리자 승인 리스트중 하나 클릭시 기사 정보 얻어오는 이벤트
        $('#apptable tr').on('click',function () {
            var tr = $(this);
            var td = tr.children();
            _this.approvalCheck(td);
        });






    },

    // 관리자 승인
    approvalCheck : function (td) {
        let d_id = td.eq(0).text();
        let profileUL;
        let nameUL = $(".dname");
        let infoUL = $(".dInfo");
        let buttonUL = $(".dBtn");
        let str;

        $.ajax({
            url:'/api/approval/'+d_id,
            method:'get',
            contentType: 'application/json; charset=utf-8',
            success:function (data) {
                str = "<p class='apinfo-font'>"+"이름: "+data.m_name+"</p>";
                nameUL.html(str);
                // str += "<p class='apinfo-font'>"+"생년월일: "+data.m_birth.substr(2,8)+"</p>";
                str += "<p class='apinfo-font'>"+"생년월일: "+"<span>"+data.m_birth.substr(2,2)+"</span>.";
                str += "<span>"+data.m_birth.substr(5,2)+"</span>.";
                str += "<span>"+data.m_birth.substr(8,2)+"</span></p>";
                str += "<p class='apinfo-font'>"+"자격증번호: "+data.d_license_no+"</p>";
                str += "<p class='apinfo-font'>"+"차량모델: "+data.car_model+"</p>";
                str += "<p class='apinfo-font'>"+"차량번호: "+data.car_no+"</p>";
                str += "<p class='apinfo-font'>"+"지역: "+data.d_license_no.substr(3,2)+"</p>";
                // str += "<p class='apinfo-font'>"+"휴대폰번호: "+data.d_id.substr(2,11)+"</p>";
                str += "<p class='apinfo-font'>"+"휴대폰번호: "+"<span>"+data.d_id.substr(2,3)+"</span>-";
                str += "<span>"+data.d_id.substr(5,4)+"</span>-";
                str += "<span>"+data.d_id.substr(9,4)+"</span></p>";
                infoUL.html(str);
                str = "<button class='btn btn-primary' type='button' id='allow-approval'>승인</button>";
                str += "<button  class='btn btn-info' type='button' id='remove-approval'>거절</button>";
                buttonUL.html(str);

                $('#allow-approval').on('click',function () {
                    let did = data.d_id;

                    $.ajax({
                        url:'/api/approvalallow',
                        method: 'post',
                        data : did,
                        success:function (result) {
                            if(result){
                                alert("승인하였습니다.")
                                window.location.href="/approval";
                            }

                        },
                        error:function () {
                            alert('실패하셨습니다.');
                        }
                    });
                });

                $("#remove-approval").on("click", function () {
                    let rid = data.d_id;
                    console.log(rid);
                    $.ajax({
                        url:'/api/approvalremove/'+rid,
                        method:'delete',
                        data:rid,
                        success:function (result) {
                            if(result){
                                alert("삭제하셨습니다.")
                                window.location.href="/approval";
                            }

                        },
                        error:function () {
                            alert('실패하셨습니다.');
                        }
                    });

                });

            }
        });
    },




}

approval.init();