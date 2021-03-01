

console.log("history & point paging getjson module....");

let pageService = (function () {

    function getList(param, callback, error){
        console.log("getList...");
        let m_id = param.m_id;
        let page = param.page || 1;

        $.getJSON("/history/pages/"+ m_id + "/" + page,
            function (data) {
                if(callback){
                    callback(data.historyCnt,data.list); // 이용내역의 숫자와 목록 가져온다.
                }
            }).fail(function (xhr, status, err) {
            if(error){
                error();
            }
        });
    }

    function getPointList(param,callback,error) {
        console.log("getPointList");
        let m_id = param.m_id;
        let page = param.page || 1;

        $.getJSON("/point/pages/" + m_id + "/" + page,
            function(data){
                if(callback){
                    callback(data.pointCnt,data.list);
                }
            }).fail(function (xhr, status, err) {
            if(error){
                error();
            }
        });
    }


    return {
        getList:getList,
        getPointList:getPointList
    };

})();