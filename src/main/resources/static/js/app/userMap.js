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
            },
            title: target.pl_name
        });
        console.log(target);

        const content = `<div id="place${i}" class='infowindow_wrap'>
        <input class="pl_id" type="hidden" value="${target.pl_id}"/>
        <div class='infowindow_title'>${target.pl_name}</div>
        <div>주소 : <span class='infowindow_address'>${target.pl_address}</span></div>
        <div>출발지 횟수 : ${target.pl_start}</div>
        <div>도착지 횟수 : ${target.pl_finish}</div>
        </div>`;

        // 리스트에 값 추가
        $('#search-list').append(`
                <li>
                    <a id="${target.pl_name}" onclick="selectSearchMarker($(this).attr('id'))">
                        <span>${target.pl_name}</span>
                    </a>
                </li>
            `);

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

        if(infowindow != null) {
            infowindow.close();
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
    console.log(navigator);
    if("geolocation" in navigator) {
        navigator.geolocation.getCurrentPosition(function (position) {
            const lat = position.coords.latitude;   // 현재 위치 위도값
            const lng = position.coords.longitude;  // 현재 위치 경도값
            const latlng = new naver.maps.LatLng(lat,lng);  // 위도, 경도값 받아오기

            $('.pulse').remove();   // 기존의 마커를 제거
            setMarker(latlng);  // 마커 생성

            console.log('위치 정보 가져오는 중');

            // 현재 위치가 찍혀질 경우 화면이 이동됨
            map.setZoom(14,false);
            map.panTo(latlng);
        });
    }else {
        alert('위치 정보 사용 불가능');
    }
});

function selectSearchMarker(res) {
    console.log('값 넘어온다 : ' + res);
    for(i in markerList) {
        console.log(markerList[i]);
        if(markerList[i].title == res){
            const latlng = markerList[i].position;
            map.setZoom(18, false); // 확대 위치 설정
            map.panTo(latlng);  // 지도 위치 변경
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