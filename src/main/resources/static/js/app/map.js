const mapOptions = {
    center: new naver.maps.LatLng(35.89683009319399, 128.62088787917372),
    zoom: 13
};

const map = new naver.maps.Map('map', mapOptions);

let data = [];

$.getJSON("/api/placelist", function (result) {
    console.log('placelist : ' + result);

    $.each(result, function (inx, obj){
        data.push(obj);
    });

    console.log(data);
    setMarker(data);

    // 마커 리스트를 확인해서 infowindow 삭제
    for(let i=0 ; i < markerList.length ; i++) {
        naver.maps.Event.addListener(map,"click",ClickMap(i));  // 맵을 클릭했을때 ClickMap 이 호출됨
        naver.maps.Event.addListener(markerList[i], "click", getClickHandler(i));
    }
});



const markerList = [];  // 지도 상 찍힌 마크
const infowindowlist = [];  // 마크에 해당하는 인포창

// DB 상의 데이터를 받아와서 값대로 마크를 찍어줌
function setMarker(data) {
    for (let i in data) {
        let target = data[i];
        let latlng = new naver.maps.LatLng(target.pl_lttd, target.pl_lngtd);
        marker = new naver.maps.Marker({
            map: map,
            position: latlng,
            icon: {
                content: "<div class='marker'></div>",
                anchor: new naver.maps.Point(12,12)
            }
        });

        const content = `<div class='infowindow_wrap'>
        <div class='infowindow_title'>${target.pl_name}</div>
        <div class='infowindow_content'>우편번호 : ${target.pl_id}</div>
        <div class='infowindow_date'>출발지 횟수 : ${target.pl_start}</div>
        <div class='infowindow_date'>도착지 횟수 : ${target.pl_finish}</div>
        </div>`;

        const infowindow = new naver.maps.InfoWindow({
            content: content,
            backgroundColor: "#00ff0000",
            borderColor: "#00ff0000",
            anchorSize: new naver.maps.Size(0,0)
        });


        markerList.push(marker);
        infowindowlist.push(infowindow);
    }
}





// 맵의 마커가 아닌 다른 곳이 클릭 될 경우 infoWindow가 열린 것을 찾은 뒤 닫아준다.
function ClickMap(i) {
    return function () {
        const infowindow = infowindowlist[i];
        infowindow.close();
    }
}


// 마커와 infowindow들을 찾아서 리스트에 등록하고 infowindow가 있으면 닫아준다.
// 만약 활성화 된 창이 없다면 해당 마커의 infowindow를 활성화한다.
function getClickHandler(i) {
    return function () {
        const marker = markerList[i];
        const infowindow = infowindowlist[i];
        if(infowindow.getMap()) {
            infowindow.close();

        }else {
            infowindow.open(map, marker);
        }
    }
}

// 현재 위치 마커 최초 1회만 실행되도록 설정
let currentUse = true;

$("#current").click(() => {
    if("geolocation" in navigator) {
        navigator.geolocation.getCurrentPosition(function (position) {
            const lat = position.coords.latitude;   // 현재 위치 위도값
            const lng = position.coords.longitude;  // 현재 위치 경도값
            const latlng = new naver.maps.LatLng(lat,lng);  // 위도, 경도값 받아오기
            if (currentUse) {
                marker = new naver.maps.Marker({
                    map: map,
                    position: latlng,
                    icon: { // 커스텀 아이콘으로 변경
                        content: '<img class="pulse" draggable="false" unselectable="on" src="https://myfirstmap.s3.ap-northeast-2.amazonaws.com/circle.png">',
                        anchor: new naver.maps.Point(11,11)
                    }
                });
                currentUse = false;
            }else {

            }


            // 현재 위치가 찍혀질 경우 화면이 이동됨
            map.setZoom(14,false);
            map.panTo(latlng);
        });
    }else {
        alert('위치 정보 사용 불가능');
    }
});

let ps = new kakao.maps.services.Places();

$("#search_input").on("keydown", function(e){
    if(e.keyCode === 13) {
        let content = $(this).val(); // content 값을 가져옴
        ps.keywordSearch(content, placeSearchCB);
    }
});

$("#search_button").on("click", function(e) {
    let content = $("#search_input").val();
    ps.keywordSearch(content, placeSearchCB);
})



let search_arr = [];

/*
    data: 목적지를 바탕으로 검색한 결과
    status: 서버에 대한 상태를 받아옴
    pagination : 검색 결과가 얼마나 있는지 번호를 통해서 알 수 있음
*/
function placeSearchCB(data, status, pagination) {
    if(status === kakao.maps.services.Status.OK) {

        for(target in data){// 데이터의 결과를 가져옴.
            const lat = target.y;
            const lng = target.x;
            const latlng = new naver.maps.LatLng(lat, lng);

            marker = new naver.maps.Marker({
                position: latlng,
                map: map
            });

            if(search_arr.length == 0) {
                search_arr.push(marker);
            }else {
                search_arr.push(marker);
                let pre_marker = search_arr.splice(0,1);
                pre_marker[0].setMap(null);
            }
        }


        map.setZoom(14, false); // 확대 위치 설정
        map.panTo(latlng);  // 지도 위치 변경

    }else {
        alert("검색 결과가 없습니다.");
    }
}


// sidebar
$(".sidebar-dropdown > a").click(function() {
    $(".sidebar-submenu").slideUp(200);
    if (
        $(this)
            .parent()
            .hasClass("active")
    ) {
        $(".sidebar-dropdown").removeClass("active");
        $(this)
            .parent()
            .removeClass("active");
    } else {
        $(".sidebar-dropdown").removeClass("active");
        $(this)
            .next(".sidebar-submenu")
            .slideDown(200);
        $(this)
            .parent()
            .addClass("active");
    }
});

$("#close-sidebar").click(function() {
    $(".page-wrapper").removeClass("toggled");
});
$("#show-sidebar").click(function() {
    $(".page-wrapper").addClass("toggled");
});

