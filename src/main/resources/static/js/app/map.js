// 지도를 생성할 시에 넣을 전체적인 지도 옵션
const mapOptions = {
    center: new naver.maps.LatLng(35.89683009319399, 128.62088787917372),
    zoom: 13
};

// Map(지도를 삽입할 HTML 요소의 id, 지도의 옵션 객체);
const map = new naver.maps.Map('map', mapOptions);

// 핫플레이스로 지정된 값들을 넣을 배열
let data = [];

$.getJSON("/api/placelist", function (result) {
    console.log('placelist : ' + result);

    $.each(result, function (inx, obj){
        data.push(obj);
    });

    console.log(data);
    setDBMarker(data);

    // 마커 리스트를 확인해서 infowindow 삭제하고 보여줌
    for(let i=0 ; i < markerList.length ; i++) {
        naver.maps.Event.addListener(map,"click",ClickMap(i));  // 맵을 클릭했을때 ClickMap 이 호출됨
        naver.maps.Event.addListener(markerList[i], "click", getClickHandler(i));
    }

});



let markerList = [];  // 지도 상 찍힌 마크
let infoWindowList = [];  // 마크에 해당하는 인포창
let search_arr = [];    // 검색한 값 리스트
let searchInfoList = [];    // 검색한 값의 해당하는 인포

// DB 상의 데이터를 받아와서 값대로 마크를 찍어줌
function setDBMarker(data) {
    for (let i in data) {
        let target = data[i];
        let latlng = new naver.maps.LatLng(target.pl_lat, target.pl_lng);
        marker = new naver.maps.Marker({
            map: map,
            position: latlng,
            icon: {
                content: "<div class='marker'></div>",
                anchor: new naver.maps.Point(12,12)
            }
        });
        console.log(target);

        const content = `<div id="place${i}" class='infowindow_wrap'>
        <input class="pl_id" type="hidden" value="${target.pl_id}"/>
        <div class='infowindow_title'>${target.pl_name}</div>
        <div>주소 : <span class='infowindow_address'>${target.pl_address}</span></div>
        <div>출발지 횟수 : ${target.pl_start}</div>
        <div>도착지 횟수 : ${target.pl_finish}</div>
        <button class="btn-default btn-sm" onclick="place.placeRemove($(this).parent())">삭제</button>
        </div>`;

        const infowindow = new naver.maps.InfoWindow({
            content: content,
            backgroundColor: "#00ff0000",
            borderColor: "#00ff0000",
            anchorSize: new naver.maps.Size(0,0)
        });


        markerList.push(marker);
        infoWindowList.push(infowindow);
    }
}





// 맵의 마커가 아닌 다른 곳이 클릭 될 경우 infoWindow가 열린 것을 찾은 뒤 닫아준다.
function ClickMap(i) {
    return function () {
        const infowindow = infoWindowList[i];
        const searchInfoWindow = searchInfoList[i];

        if(infowindow != null) {
            infowindow.close();
        }
        if(searchInfoWindow != null) {
            searchInfoWindow.close();
        }

    }
}


// 마커와 infowindow들을 찾아서 리스트에 등록하고 infowindow가 있으면 닫아준다.
// 만약 활성화 된 창이 없다면 해당 마커의 infowindow를 활성화한다.
function getClickHandler(i) {
    return function () {
        const marker = markerList[i];
        const infowindow = infoWindowList[i];
        if(infowindow.getMap()) {
            infowindow.close();

        }else {
            infowindow.open(map, marker);
        }
    }
}


function getSearchClickHandler(i) {
    return function () {
        const marker = search_arr[i];
        const infowindow = searchInfoList[i];
        if(infowindow.getMap()) {
            infowindow.close();
        }else {
            infowindow.open(map, marker);
        }
    }
}

function setMarker(latlng) {
    marker = new naver.maps.Marker({
        map: map,
        position: latlng,
        icon: { // 커스텀 아이콘으로 변경
            content: '<img class="pulse" draggable="false" unselectable="on" src="https://myfirstmap.s3.ap-northeast-2.amazonaws.com/circle.png">',
            anchor: new naver.maps.Point(11,11)
        }
    });
}

$("#current").click(() => {
    if("geolocation" in navigator) {
        navigator.geolocation.getCurrentPosition(function (position) {
            const lat = position.coords.latitude;   // 현재 위치 위도값
            const lng = position.coords.longitude;  // 현재 위치 경도값
            const latlng = new naver.maps.LatLng(lat,lng);  // 위도, 경도값 받아오기

            $('.pulse').remove();   // 기존의 마커를 제거
            setMarker(latlng);  // 마커 생성

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





/*
    data: 목적지를 바탕으로 검색한 결과
    status: 서버에 대한 상태를 받아옴
    pagination : 검색 결과가 얼마나 있는지 번호를 통해서 알 수 있음
*/
function placeSearchCB(data, status, pagination) {
    if(status === kakao.maps.services.Status.OK) {
        if(search_arr.length != 0) {
            $('#search-list').empty(); // 내부 요소 전체 제거

            for(i in search_arr) {
                console.log("실행됨");
                let pre_marker = search_arr[i];
                pre_marker.setMap(null);
            }

            search_arr = [];
            searchInfoList = [];
        }

        console.log(data);

        for(i in data){// 검색된 데이터의 결과를 가져옴.
            const target = data[i];
            const lat = target.y;
            const lng = target.x;
            const title = target.place_name;
            let latlng = new naver.maps.LatLng(lat, lng);

            console.log(target);

            // 검색 데이터 마커
            marker = new naver.maps.Marker({
                title: title,
                position: latlng,
                map: map
            });

            // 리스트에 값 추가
            $('#search-list').append(`
                <li>
                    <a id="${title}" onclick="selectSearchMarker($(this).attr('id'))">
                        <span>${title}</span>
                    </a>
                </li>
            `);

            const content = `<div class='searchwindow_wrap'>
                    <input class="infowindow_id" type="hidden" value="${target.id}"/>
                    <input class="infowindow_lat" type="hidden" value="${target.y}"/>
                    <input class="infowindow_lng" type="hidden" value="${target.x}"/>
                    <div class='infowindow_title'>${target.place_name}</div>
                    <div><i class="fas fa-map-signs"></i> <span class='infowindow_category'>${target.category_name}</span></div>
                    <div><i class="fas fa-map-marker-alt"></i> <span class='infowindow_address'>${target.address_name}</span></div>
                    <div><i class="fas fa-phone"></i> <span class='infowindow_tel'>${target.phone}</span></div>
                    <a href="${target.place_url}">링크</a>
                    <div>
                        <button class="btn-primary btn-sm" style="float: right" onclick="place.placeRegister($(this).parent().parent())">추가</button>
                    </div>
                </div>`;

            const infowindow = new naver.maps.InfoWindow({
                content: content,
                backgroundColor: "#00ff0000",
                borderColor: "#00ff0000",
                anchorSize: new naver.maps.Size(0,0)
            });

            search_arr.push(marker);
            searchInfoList.push(infowindow);


            naver.maps.Event.addListener(search_arr[i], "click", getSearchClickHandler(i));
        }

        const latlng = search_arr[0].position;

        map.setZoom(14, false); // 확대 위치 설정
        map.panTo(latlng);  // 지도 위치 변경

    }else {
        alert("검색 결과가 없습니다.");
    }
}



function selectSearchMarker(res) {
    console.log('값 넘어온다 : ' + res);
    for(i in search_arr) {
        if(search_arr[i].title == res){
            const latlng = search_arr[i].position;
            map.setZoom(18, false); // 확대 위치 설정
            map.panTo(latlng);  // 지도 위치 변경
            console.log('실행 완료');
            break;
        }
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
